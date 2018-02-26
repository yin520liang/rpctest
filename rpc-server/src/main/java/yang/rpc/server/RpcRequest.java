/**
 * 
 */
package yang.rpc.server;

/**
 * RPC请求
 * 
 * @Author lvzhaoyang
 * @Date 2018年2月24日
 */
public class RpcRequest {
	private String requestId;
	
	private String className;
	
	private String methodName;
	
	private Class<?>[] parameterTypes;
	
	private Object[] parameters;

	
	public String getRequestId() {
		return requestId;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	
	
}
