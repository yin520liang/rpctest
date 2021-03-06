/**
 * 
 */
package yang.rpc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Title RpcService
 * @Description 
 * @Date 2018年2月11日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

	Class<?> value ();
}
