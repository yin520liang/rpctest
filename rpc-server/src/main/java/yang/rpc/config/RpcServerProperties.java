/**
 * 
 */
package yang.rpc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * RPC Server Properties
 * @Author lvzhaoyang
 * @Date 2018年2月11日
 */
@Component
public class RpcServerProperties {
	
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

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

}
