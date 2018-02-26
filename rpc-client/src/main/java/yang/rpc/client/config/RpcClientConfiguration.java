/**
 * 
 */
package yang.rpc.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import yang.rpc.client.service.RpcProxy;
import yang.rpc.client.service.ServiceDiscovery;

/**
 * @Title RpcClientConfiguration
 * @Description 
 * @Author lvzhaoyang
 * @Date 2018年2月26日
 */
@Configuration
public class RpcClientConfiguration {
	
	@Autowired
	private RpcClientProperties properties;
	
	@Bean
	public ServiceDiscovery serviceDiscovery() {
		return new ServiceDiscovery(properties.getRegistryAddress());
	}

	@Bean
	public RpcProxy rpcProxy(@Autowired ServiceDiscovery serviceDiscovery) {
		return new RpcProxy(properties.getServerAddress(), serviceDiscovery);
	}
}
