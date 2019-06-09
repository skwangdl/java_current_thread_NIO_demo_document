#动态代理

代理模式：对客户端隐藏委托类实现细节，实现客户与委托类间的解耦，在不修改委托类代码的情况下做额外的处理

静态代理：代理类在程序运行前就已经存在

动态代理：代理类在程序运行前不存在

##动态代理实现
Spring AOP（切面）的拦截功能由JDK中的动态代理实现，AOP源码中用到了两种实现方式：JDK动态代理与cglib动态代理

jdk动态代理由反射实现，cglib动态代理底层借助ASM框架（Java字节码操纵框架）实现

比较：jdkProxy在生成类的过程中比较高效，cglib在生成类之后的操作比较高效（可以利用缓存提高效率）；jdkProxy目标类必须实现统一的接口，cglib不需要，所以cglib应用较广泛；cglib无法代理final方法

cglib应用：Spring AOP dynaop, Hibernate single-ended(多对一和一对一)关联

###JDK proxy

统一接口
	
	package jdkproxy;

	public interface Service {
		public void add();
		public void update();
	}

目标类
	
	package jdkproxy;
	
	public class AService implements Service {
	
		@Override
		public void add() {
			System.out.println("AService add>>>>>>>>>>>>>");
		}
	
		@Override
		public void update() {
			System.out.println("AService update>>>>>>>>>>");
		}
		
	}

动态代理实现类
	
	package jdkproxy;

	import java.lang.reflect.InvocationHandler;
	import java.lang.reflect.Method;
	
	public class MyInvocationHandler implements InvocationHandler {
	
		private Object target;
	
		MyInvocationHandler() {
			super();
		}
	
		MyInvocationHandler(Object target) {
			super();
			this.target = target;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// 程序执行前加入逻辑，MethodBeforeAdviceInterceptor
			System.out.println("before-----------------------------");
			// 程序执行
			Object result = method.invoke(target, args);
			// 程序执行后加入逻辑，MethodAfterAdviceInterceptor
			System.out.println("after------------------------------");
			return result;
		}
	}

测试类
	
	package jdkproxy;

	import java.lang.reflect.Proxy;
	
	public class Test {  
	    public static void main(String[] args) {  
	        Service aService = new AService();  
	        MyInvocationHandler handler = new MyInvocationHandler(aService);  
	        // Proxy为InvocationHandler实现类动态创建一个符合某一接口的代理实例  
	        Service aServiceProxy = (Service) Proxy.newProxyInstance(aService
	                .getClass().getClassLoader(), aService.getClass()  
	                .getInterfaces(), handler);  
	        // 由动态生成的代理对象来aServiceProxy 代理执行程序，其中aServiceProxy 符合Service接口  
	        aServiceProxy.add();  
	        System.out.println();  
	        aServiceProxy.update();  
	    }  
	}

##cglib proxy

目标类
	
	package cglibproxy;

	public class Base {
		public void add() {
			System.out.println("add ------------");
		}
	}

动态代理类
	
	package cglibproxy;

	import java.lang.reflect.Method;
	import net.sf.cglib.proxy.MethodInterceptor;
	import net.sf.cglib.proxy.MethodProxy;
	
	public class CglibProxy implements MethodInterceptor {
	
		public Object intercept(Object object, Method method, Object[] args,
				MethodProxy proxy) throws Throwable {
			System.out.println("before-----------");
			proxy.invokeSuper(object, args);
			System.out.println("after------------");
			return null;
		}
	
	}

工厂类
	
	package cglibproxy;

	import net.sf.cglib.proxy.Enhancer;
	
	public class Factory {
		public static Base getInstance(CglibProxy proxy) {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(Base.class);
			enhancer.setCallback(proxy);
			Base base = (Base) enhancer.create();
			return base;
		}
	}

测试类
	
	package cglibproxy;

	public class Test {
		public static void main(String[] args) {
			CglibProxy proxy = new CglibProxy();
			Base base = Factory.getInstance(proxy);
			base.add();
		}
	}

##ASM（asm3.2.jar）
	
	package asm;

	import java.io.File;
	import java.io.FileOutputStream;
	import java.io.IOException;
	
	import org.objectweb.asm.ClassWriter;
	import org.objectweb.asm.Opcodes;
	
	/**
	 * 通过asm生成类的字节码
	 * @author Administrator
	 *
	 */
	public class GeneratorClass {
	
	    public static void main(String[] args) throws IOException {
	        //生成一个类只需要ClassWriter组件即可
	        ClassWriter cw = new ClassWriter(0);
	        //通过visit方法确定类的头部信息
	        cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT+Opcodes.ACC_INTERFACE,
	                "com/asm3/Comparable", null, "java/lang/Object", new String[]{"com/asm3/Mesurable"});
	        //定义类的属性
	        cw.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL+Opcodes.ACC_STATIC,
	                "LESS", "I", null, new Integer(-1)).visitEnd();
	        cw.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL+Opcodes.ACC_STATIC,
	                "EQUAL", "I", null, new Integer(0)).visitEnd();
	        cw.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL+Opcodes.ACC_STATIC,
	                "GREATER", "I", null, new Integer(1)).visitEnd();
	        //定义类的方法
	        cw.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT, "compareTo",
	                "(Ljava/lang/Object;)I", null, null).visitEnd();
	        cw.visitEnd(); //使cw类已经完成
	        //将cw转换成字节数组写到文件里面去
	        byte[] data = cw.toByteArray();
	        File file = new File("C://Comparable.class");
	        FileOutputStream fout = new FileOutputStream(file);
	        fout.write(data);
	        fout.close();
	    }
	}

在C盘下生成Comparable.class文件，反编译结果
	
	public interface com.asm3.Comparable extends com.asm3.Mesurable {
	  public static final int LESS;
	
	  public static final int EQUAL;
	
	  public static final int GREATER;
	
	  public abstract int compareTo(java.lang.Object);
	}
