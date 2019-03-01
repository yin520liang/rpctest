/**
 * 
 */
package yang.rpc.config;

/**
 * @Title Constant
 * @Description 
 * @Date 2018年2月23日
 */
public interface Constant {
	int ZK_SESSION_TIMEOUT = 5000; // 5s
	
	String ZK_REGISTRY_PATH = "/registry";
	
	String ZK_DATA_PATH =  ZK_REGISTRY_PATH + "/data";
}
