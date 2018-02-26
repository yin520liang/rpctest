/**
 * 
 */
package yang.rpc.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

import yang.rpc.serialize.Serializer;

/**
 * @Title RpcEncoder
 * @Description
 * @Author lvzhaoyang
 * @Date 2018年2月23日
 */
public class RpcEncoder extends MessageToByteEncoder {

	private Class<?> genericClass;
	
	private List<Serializer> serializers;

	public RpcEncoder(Class<?> genericClass, List<Serializer> serializers) {
		this.genericClass = genericClass;
		this.serializers = serializers;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
			throws Exception {
		if (genericClass.isInstance(msg)) {
			byte[] data = getSerializer(msg.getClass()).serialize(msg);
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}
	
	protected Serializer getSerializer(Class<?> cl) {
		for(Serializer se : serializers) {
			if(se.canHandle(cl)) {
				return se;
			}
		}
		return serializers.get(0);
	}

}
