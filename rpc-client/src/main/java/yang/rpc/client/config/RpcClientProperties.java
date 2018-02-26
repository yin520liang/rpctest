/**
 * 
 */
package yang.rpc.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Title RpcClientProperties
 * @Description 
 * @Author lvzhaoyang
 * @Date 2018年2月26日
 */
@Component
public class RpcClientProperties {
	
	@Value("registry.address")
	private String registryAddress;
	
	@Value("server.address")
	private String serverAddress;
	
	
	public String getRegistryAddress() {
		return registryAddress;
	}
	
	public String getServerAddress() {
		return serverAddress;
	}

}
