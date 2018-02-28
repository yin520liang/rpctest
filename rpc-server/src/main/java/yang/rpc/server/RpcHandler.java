/**
 * 
 */
package yang.rpc.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title RpcHandler
 * @Description 
 * @Author lvzhaoyang
 * @Date 2018年2月23日
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest>{
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcHandler.class);
	
	private Map<String, Object> handlerMap;

	public RpcHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request)
			throws Exception {
		RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Throwable t) {
            response.setError(t);
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		
	}

	private Object handle(RpcRequest request) throws InvocationTargetException {
		String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        LOGGER.debug("call '{}' method of class {}", methodName, serviceClass.getName());
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        // 实际的接口调用
        return serviceFastMethod.invoke(serviceBean, parameters);
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("server caught exception", cause);
        ctx.close();
    }

}
