/**
 * 
 */
package yang.rpc.serialize;

/**
 * 序列化接口
 * 
 * @Title Serializer
 * @Author lvzhaoyang
 * @Date 2018年2月26日
 */
public interface Serializer {
	
	<T> byte[] serialize(T obj);

	<T> T deserialize(byte[] data, Class<T> cls);
	
	default boolean canHandle(Class<?> obj) {
		return true;
	}
}
