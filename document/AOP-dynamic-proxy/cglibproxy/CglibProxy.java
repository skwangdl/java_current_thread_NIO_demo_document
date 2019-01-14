package cglibproxy;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 此为代理类，用于在pointcut处添加advise
 * 
 * @author typ
 * 
 */
public class CglibProxy implements MethodInterceptor {

	public Object intercept(Object object, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		// 添加切入逻辑（一句输出语句），此处是在目标类代码执行之前，即为MethodBeforeAdviceInterceptor。
		System.out.println("拦截-------------");
		// 执行目标类add方法
		proxy.invokeSuper(object, args);
		return null;
	}

}
