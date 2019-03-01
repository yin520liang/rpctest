/**
 * 
 */
package yang.rpc.service;

/**
 * client和server端需要保持接口一致：接口名、方法签名、包名
 * @Title HelloService
 * @Date 2018年2月11日
 */
public interface HelloService {

	String hello(String name);
}
