/**
 * 
 */
package yang.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import yang.rpc.server.RpcRequest;
import yang.rpc.server.RpcResponse;
import yang.rpc.service.ServiceDiscovery;

/**
 * @Title RpcProxy
 * @Description
 * @Date 2018年2月26日
 */
public class RpcProxy {

	private ServiceDiscovery serviceDiscovery;

	private String serverAddress;

	public RpcProxy(String serverAddress, ServiceDiscovery serviceDiscovery) {
		this.serverAddress = serverAddress;
		this.serviceDiscovery = serviceDiscovery;
	}

	@SuppressWarnings("unchecked")
	public <T> T create(Class<?> interfaceClass) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class<?>[] { interfaceClass }, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						RpcRequest request = new RpcRequest();
						// request ID
						request.setRequestId(UUID.randomUUID().toString());
						request.setClassName(method.getDeclaringClass().getName());
						request.setMethodName(method.getName());
						request.setParameterTypes(method.getParameterTypes());
						request.setParameters(args);

						if (serviceDiscovery != null) {
							serverAddress = serviceDiscovery.discover(); // 发现服务
						}

						String[] array = serverAddress.split(":");
						String host = array[0];
						int port = Integer.parseInt(array[1]);
						// 初始化RPC客户端
						RpcClient client = new RpcClient(host, port);
						RpcResponse response = client.send(request);

						if (response.isError()) {
							throw response.getError();
						} else {
							return response.getResult();
						}
					}
				});
	}

}
