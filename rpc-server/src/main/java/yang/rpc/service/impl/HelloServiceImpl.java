/**
 * 
 */
package yang.rpc.service.impl;

import yang.rpc.annotations.RpcService;
import yang.rpc.service.HelloService;

/**
 * @Title HelloServiceImpl
 * @Description 
 * @Date 2018年2月11日
 */
@RpcService(value = HelloService.class)
public class HelloServiceImpl implements HelloService{


	@Override
	public String hello(String name) {
		return "Hello, " + name;
	}

}
