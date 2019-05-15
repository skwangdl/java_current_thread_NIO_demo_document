package com.kepler.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author typ
 *
 */
public class MyInvocationHandler implements InvocationHandler {
	private Object target;

	MyInvocationHandler() {
		super();
	}

	MyInvocationHandler(Object target) {
		super();
		this.target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// 程序执行前加入逻辑
		System.out.println("before-----------------------------");
		// 程序执行
		Object result = method.invoke(target, args);
		// 程序执行后加入逻辑
		System.out.println("after------------------------------");
		return result;
	}

}
