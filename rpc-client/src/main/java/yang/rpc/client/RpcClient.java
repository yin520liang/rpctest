/**
 * 
 */
package yang.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yang.rpc.serialize.DefaultSerializer;
import yang.rpc.serialize.Serializer;
import yang.rpc.server.RpcDecoder;
import yang.rpc.server.RpcEncoder;
import yang.rpc.server.RpcRequest;
import yang.rpc.server.RpcResponse;

/**
 * @Title RpcClient
 * @Description
 * @Author lvzhaoyang
 * @Date 2018年2月26日
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RpcClient.class);

	private String host;

	private int port;

	private RpcResponse response;

	private List<Serializer> serializers;

	private final Object obj = new Object();

	public RpcClient(String host, int port) {
		this.host = host;
		this.port = port;
		setDefaultSerializers();
	}

	private void setDefaultSerializers() {
		serializers = new ArrayList<>();
		serializers.add(new DefaultSerializer());

	}

	public RpcResponse send(RpcRequest request) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel channel)
								throws Exception {
							channel.pipeline()
									.addLast(new RpcEncoder(RpcRequest.class, serializers))
									.addLast(new RpcDecoder(RpcResponse.class, serializers))
									.addLast(RpcClient.this);
						}
					}).option(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture future = bootstrap.connect(host, port).sync();
			future.channel().writeAndFlush(request).sync();

			synchronized (obj) {
				obj.wait(); // 未收到响应，使线程等待
			}

			if (response != null) {
				future.channel().closeFuture().sync();
			}
			return response;
		} finally {
			group.shutdownGracefully();
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response)
			throws Exception {
		this.response = response;

		synchronized (obj) {
			obj.notifyAll(); // 收到响应，唤醒线程
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		LOGGER.error("client caught exception", cause);
		ctx.close();
	}

}
