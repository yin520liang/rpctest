/**
 * 
 */
package yang.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import yang.rpc.annotations.RpcService;
import yang.rpc.serialize.Serializer;
import yang.rpc.service.registry.ServiceRegistry;

/**
 * @Title RpcServer
 * @Description 
 * @Author lvzhaoyang
 * @Date 2018年2月11日
 */
public class RpcServer implements ApplicationContextAware, InitializingBean{

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
	
	private String serverAddress;
	private ServiceRegistry serviceRegistry;
	
	// store the mapping of interface and handler
	private Map<String, Object> handlerMap = new HashMap<>();
	
	private List<Serializer> serializers;


	public RpcServer(String serverAddress, ServiceRegistry serviceRegistry, List<Serializer> serializers) {
		this.serverAddress = serverAddress;
		this.serviceRegistry = serviceRegistry;
		this.serializers = serializers;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline()
							.addLast(new RpcDecoder(RpcRequest.class, serializers))
							.addLast(new RpcEncoder(RpcResponse.class, serializers))
							.addLast(new RpcHandler(handlerMap));
					}			
				})
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			// set host,port
			String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);
            // start server
            ChannelFuture future = bootstrap.bind(host, port).sync();
            LOGGER.debug("server started on port {}", port);

            if (serviceRegistry != null) {
                serviceRegistry.register(serverAddress); // 注册服务地址
            }

            future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
		
	}


	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		// get all the beans with 'RpcService' annotation
		Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
		if(MapUtils.isNotEmpty(serviceBeanMap)) {
			for(Object serviceBean : serviceBeanMap.values()) {
				String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
				handlerMap.put(interfaceName, serviceBean);
			}
		}
	}

}
