/**
 * 
 */
package yang.rpc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import yang.rpc.serialize.DefaultSerializer;
import yang.rpc.serialize.Serializer;
import yang.rpc.server.RpcServer;
import yang.rpc.service.registry.ServiceRegistry;

/**
 * RPC Server Configuration
 * @Date 2018年2月11日
 */
@Configuration
public class RpcServiceConfiguration {
	
	@Autowired
	private RpcServerProperties rpcProperties;	
	
	
	@Qualifier("serviceRegistry")
	@Bean
	public ServiceRegistry serviceRegistry() {
		System.out.println("=======" + rpcProperties.getRegistryAddress());
		return new ServiceRegistry(rpcProperties.getRegistryAddress());
	}
	
	@Bean
	public List<Serializer> defaultSerializers() {
		List<Serializer> serializers = new ArrayList<>();
		serializers.add(new DefaultSerializer());
		return serializers;
	}

	@Qualifier("rpcServer")
	@Bean
	public RpcServer rpcServer(@Autowired ServiceRegistry serviceRegistry, @Autowired List<Serializer> serializers) {
		return new RpcServer(rpcProperties.getServerAddress(), serviceRegistry, serializers);
	}
	
}
