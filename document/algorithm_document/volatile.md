#volatile
Java关键字,C与CPP中也存在

##volatile关键字的两层语义
一旦一个共享变量（类的成员变量，类的静态成员变量）被volatile修饰后，那么就具备了两层语义：

1.保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的

2.禁止进行指令重排序

###禁止指令重排
1.当程序执行到volatile变量的读写操作时，在其前面的更改一定全部已经进行了，且结果对后面的操作可见，后面的操作肯定还没有进行

2.在CPU进行指令优化时，不能将在对volatile变量访问的语句放在其后面执行，也不能把volatile变量后面的语句放在其前面执行
	
	volatile int flag = 1；

	x=1;	//语句1
	y=2;	//语句2
	flag = 3;		//语句3
	x=4			//语句4
	y = -1		//语句5
	
语句3不能放到语句1，语句2前执行，不能放到语句4，语句5后执行，但语句1,2的顺序，语句3,4的顺序不能保证

##java内存模型（JVM）
JVM(Java Memory Model), Java内存模型，是Java虚拟机所定义的一种抽象规范，屏蔽不同硬件和操作系统的内存访问差异，让Java在各种平台下都能达到一致的内存访问效果

线程A，线程B并行处理各自的工作内存与读写操作，然后同时进行主内存的读写操作

1.主内存：被所有线程所共享

2.工作内存：可理解位CPU告诉缓存，但不完全相等，对于一个共享变量，工作内存存储了一个“副本”

线程对共享变量的所有操作都必须在工作内存进行，不能直接读写主内存中的变量，不同线程之间也无法访问彼此的工作内存，多线程传递变量只能通过主内存

例如：
>对于一个静态变量 static int s = 0,线程A执行如下代码：s=3;JVM工作流程如下：

>1.主内存创建S赋值0；2.线程A工作内存创建s赋值0；3.线程A工作内存，s赋值3；4.主内存s赋值3

JVM内存模型共定义了8种内存操作指令

如上如果在线程A进行到第三步，此时线程B执行读取s值，**此时s值在主内存中为0，但是在线程A中为3**

可以使用synchronized保证线程安全，但过多影响性能，在变量类型前添加volatile关键字，可以保证变量对所有线程的一致性，当一个线程修改了变量的值，新的值会立刻同步到主内存中，其他线程读取这个变量时，也会从主内存中拉取最新的变量值

Java线性发生原则：对于volatile变量的写操作，先行发生于后面对这个变量的读操作

volatile不能保证变量的原子性，不能保证线程安全，如下代码：
	
	public class VolatileTest {
		public volatile static int count = 0;
	
		public static void main(String[] args) {
			for (int i = 0; i < 10; i++) {
				new Thread(new Runnable() {
	
					@Override
					public void run() {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						for (int j = 0; j < 1000; j++) {
							count++;
						}
					}
				}).start();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.print("count=" + count);
		}
	}
	

count结果有可能小于1000，

count++代码在JVM中拆分成如下指令：1.getstatic（读取静态变量），2.iconst_1（定义常量1），3.iadd（count增加1），4.putstatic（count结果同步到主内存），每次执行第一步时，获取到的都是主内存最新的值，但在进行iadd时，其他线程可能会让count自增，导致本线程的count值是一个陈旧的值，所以变量对所有线程可见无法做到线程安全


适用volatile情况:

1.运行结果并不依赖变量的当前值，或者能够确保只有单一的线程修改变量的值

2.变量不需要与其他的状态变量共同参与不变约束（如在线程A中循环线程B，但B影响A的执行条件）

