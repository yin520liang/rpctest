/**
 * 
 */
package yang.rpc.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import yang.rpc.serialize.Serializer;

/**
 * @Title RpcDecoder
 * @Description
 * @Date 2018年2月23日
 */
public class RpcDecoder extends ByteToMessageDecoder {

	private Class<?> genericClass;
	
	private List<Serializer> serializers;

	public RpcDecoder(Class<?> genericClass, List<Serializer> serializers) {
		this.genericClass = genericClass;
		this.serializers = serializers;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		// 格式：字节数+数据
		if (in.readableBytes() < 4) {
			return;
		}
		in.markReaderIndex();
		int dataLength = in.readInt();
		if (dataLength < 0) {
			ctx.close();
		}
		if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] data = new byte[dataLength];
		in.readBytes(data);

		Object obj = getSerializer(genericClass).deserialize(data, genericClass);
		out.add(obj);

	}
	
	protected Serializer getSerializer(Class<?> obj) {
		for(Serializer se : serializers) {
			if(se.canHandle(obj)) {
				return se;
			}
		}
		return serializers.get(0);
	}

}
