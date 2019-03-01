/**
 * 
 */
package yang.rpc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * RPC Server Properties
 * @Date 2018年2月11日
 */
@Component
public class RpcServerProperties {
	
	@Value("${yang.rpc.registry.address}")
	private String registryAddress;
	
	@Value("${yang.rpc.server.address}")
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
