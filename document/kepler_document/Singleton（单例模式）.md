#singleton（单例模式）
保证系统中，应用该模式的类，一个类只有一个实例，即一个类只有一个对象

####非线程安全
	
	public class Singleton_1 {
		private Singleton_1(){
		}

		private static Singleton_1 instance = null;
	
		public static Singleton_1 getInstance(){
			if(instance == null){
				instance = new Singleton_1();
			}
			return instance;
		}
	}

####线程安全
	
	public class Singleton_2 {
		private Singleton_2() {
		}
	
		private volatile static Singleton_2 instance = null;
	
		public static Singleton_2 getInstance() {
			if (instance == null) {
				synchronized (Singleton_2.class) {
					if (instance == null) {
						instance = new Singleton_2();
					}
				}
			}
			return instance;
		}
	}

#####volatile作用：

当线程A正在构建对象，线程B刚刚进入方法时，表面看，如果对象没有被A构建，B执行if（instance == null）得到true，对象被A构建后，B执行得到false，但是实际上不是
	
JVM指令重排：比如instance = new Singleton(), 会被编译器编译成如下JVM指令

	memory = allocate();		1.分配对象的内存空间
	ctorInstance(memory);		2.初始化对象
	instance = memory;			3.设置instance指向刚分配的内存地址

JVM指令有可能经过JVM与CPU的优化，指令重排成下面的顺序：
	
	memory = allocate();		1.分配对象的内存空间
	instance = memory;			3.设置instance指向刚分配的内存地址
	ctorInstance(memory);		2.初始化对象
	
线程A执行完1,3时，instance对象还未完成初始化，但已经不再指向null，如果此时线程B抢占到CPU，执行if（instance == null），结果为false,会返回一个没有初始化完成的instance对象

为了避免以上情况，需要在instance对象前加volatile,保证JVM指令不被重排

####线程安全（内部类）
	
	public class Singleton_4 {
		private static class LazyHolder{
			private static final Singleton_4 INSTANCE = new Singleton_4();
		}
		private Singleton_4(){}
		public static Singleton_4 getInstance(){
			return LazyHolder.INSTANCE;
		}
	}

INSTANCE对象在调用getInstance方法，使静态内部类LazyHolder被加载的时候才完成初始化，即懒加载，保证构建单例的线程安全

单例模式可以被反射进行恶意初始化，新建两个不同的对象

	public class SingletonTest {
		@Test
		public void test() throws Exception{
				Constructor con = Singleton_1.class.getDeclaredConstructor();
				con.setAccessible(true);
				Singleton_1 s1 = (Singleton_1) con.newInstance();
				Singleton_1 s2 = (Singleton_1) con.newInstance();
				System.out.println(s1 + " " + s2);
		}
	}

####防反射

	public class Singleton_3 implements Serializable {
		private static boolean flag = false;
	
		private Singleton_3() {
			synchronized (Singleton_3.class) {
				if (flag == false) {
					flag = !flag;
				} else {
					throw new RuntimeException("单例模式被侵犯！");
				}
			}
		}
	
		private static class SingletonHolder {
			private static final Singleton_3 INSTANCE = new Singleton_3();
		}
	
		public static Singleton_3 getInstance() {
			return SingletonHolder.INSTANCE;
		}
	
		public void doSomethingElse() {
	
		}
	}
	
如果要抵御反射攻击单例，可以修改构造器，让它在被要求创建第二个实例的时候抛出异常
利用枚举也可以防止反射恶意构造非单例对象，可以保证线程安全，但不是懒加载