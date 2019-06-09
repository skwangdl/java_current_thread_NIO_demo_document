#第一章 Semaphore Exchanger

##(一)Semaphore的使用
此类的主要作用就是限制并发数量，不能保证线程安全

Semaphore为信号的意思

####1.类Semaphore的同步性
	
	public class Service {
		private Semaphore semaphore = new Semaphore(1);
		
		public void testMethod(){
			try {
				semaphore.acquire();
				System.out.println();
				semaphore.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

定义最多允许1个线程执行acquire()和release()之间的代码，Semaphore(1)的效果等同于Synchronized

####2.类Semaphore构造方法permits参数
new Semaphore(int perimts)，permits参数为最多有permits个线程可以执行acquire()与release()之间的代码

####3.方法acquire(int permits)参数及动态添加permits许可数量
有参方法acquire(int permits)，每调用一次，就使用permits个许可

	public class Service {
		private Semaphore semaphore = new Semaphore(10);
		public void testMethod(){
			try {
				semaphore.acquire(2);
				System.out.println();
				semaphore.release(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
共有10个许可，每次线程调用2个，归还2个，最多有5个线程进入acquire(2)与release(2)之间
调用数与归还数可以任意更改

####4.acquireUninterruptibly()方法使用
使进入acquireUninterruptibly()与release()之间的线程不允许被中断

acquireUninterruptibly(int perimts)，在等待许可的情况下不允许被中断，如果成功获得锁，则取得指定的permits许可个数

####5.availablePermits()方法与drainPermits()方法

availablePermits()方法返回此Semaphore对象中当前许可的许可数，用于调试，许可数时刻在变。

drainPermits()方法立即返回可用的许可数个数，并将可用许可置0，清空

####6.getQueueLength()方法  hasQueueThreads()
getQueueLength()方法，获取正在等待许可的线程个数

hasQueueThreads方法，判断有没有线程正在等待这个Semaphore对象的许可

####7.公平与非公平信号量的测试
公平信号：获得锁的顺序与线程启动的顺序有关，但不代表是完全的，仅在概率上得到保障
非公平信号：获得锁的顺序与线程启动的顺序无关

底层原理与公平锁与非公平锁类似，异步队列实现，公平锁放入队列，线程一个一个排队等待锁，非公平锁则无序

####8.tryAcquire()方法 tryAcquire(int permits)方法 
tryAcquire方法，尝试取得Semaphore对象的1个许可，如果没拿到返回false,否则返回true

tryAcquire(int permits)方法，尝试取得Semaphore对象的permits许可，如果没拿到返回false,否则返回true

####9.tryAcquire(long time, TimeUnit unit)方法  tryAcquire(int permits, long time, TimeUnit unit)

tryAcquire(long time, TimeUnit unit)方法，在time时间内，尝试取得Semaphore对象的1个许可，如果没拿到返回false,否则返回true，有阻塞效果

tryAcquire(int permits, long time, TimeUnit unit)方法，在time时间内，尝试取得Semaphore对象的permits许可，如果没拿到返回false,否则返回true，有阻塞效果

####10使用Semaphore创建字符串池
利用Semaphore限流作用与ReentrantLock锁实现

####11使用Semaphore实现生产消费模式
采用Semaphore进行线程个数限制，Condition进行唤醒 A_TestProject_11 test0315_10_S

##(二)Exchanger
主要作用就是让2个线程可以传输任意数据

####1.exchange()方法
主要作用就是让2个线程可以传输任意数据,如果存在第三个线程，则一直阻塞，等待获取值,**阻塞方法**

####2.exchange(V x，long timeout, TimeUnit unit)

在指定的时间内没有其他线程将数据获取，则出现超时异常

####3.Exchanger实现数据交互步骤

线程A放入数据“老猫”，线程B放入数据“狂牛”

>1.A走到Exchanger.class，565行进入slotExchange方法，执行455行，将自身对象赋值Node p与slot， 464行将数据“老猫”传给Node q的match属性，504行判断为true,unsafe对象调用park方法阻塞

>2.B走到Exchanger.class，565行进入slotExchange方法，463行q将线程A传入的数据“老猫”给Object v,464行B将数据“狂牛”传给q的match属性，467行unsafe对象调用unpark方法激活线程A，B将Object v即数据“老猫”返回

>3.A线程被激活后，执行490行，通过p.match属性，为数据“狂牛”传给Object v,A线程517行返回数据“狂牛”


#第二章 CountDownLatch  CyclicBarrier
latch门栓  使线程互相等待，使多个线程组团一起执行任务，控制线程执行任务的时机

CyclicBarrier 环形障碍物

这两个工具类可以使线程在同步处理上更加灵活，如支持同步计数重置，等待同步线程个数等功能，这两个工具将同步与线程“组团做任务完美进行了支持”

##(一)CountDownLatch
等到多个分线程运行完countDown()之后，main线程才可以从latch.await()处继续向下运行

使用CountDownLatch时，会存在两种角色的线程：
>(1)A角色：1个或多个线程
>
>(2)B角色：1个或多个线程

当使用类CountDownlatch时由A角色来决定阻塞中的B角色是否继续向下运行，也就是B角色线程是否继续执行任务取决于A角色

当七颗龙珠集齐之后，才可以召唤神龙，每颗龙珠就是A角色，神龙就是B角色，两者有关联

CountDownLatch对象的计数为减法操作，CountDownLatch对象的计数无法被重置，如果需要重置，则需要多个对象或使用CyclicBarrier类

####1.await() countDownLatch()方法

await()方法使当前线程阻塞在此，如果计数没有CountDownLatch构造方法的参数没有减到0的话

countDownLatch()方法，使CountDownLatch的计数减1

只有CountDownLatch的计数减到0时，在await()方法处阻塞的线程才可以继续运行

分线程类
	
	public class ThreadA extends Thread {
		private Service service;
		public ThreadA(Service service) {
			super();
			this.service = service;
		}
		@Override
		public void run() {
			super.run();
			try {
				service.testMethod();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

服务类
	
	public class Service {
		private CountDownLatch latch;
		public Service(CountDownLatch latch) {
			super();
			this.latch = latch;
		}
		public void testMethod() throws InterruptedException{
			System.out.println(Thread.currentThread().getName() + " run begin");
			Thread.sleep(5000);
			latch.countDown();
			Thread.sleep(5000);
			System.out.println(Thread.currentThread().getName() + " run   end");
		}
	}

运行类
	
	public class Run {
		/**
		 * 等到3个分线程运行完countDown()之后，main线程才可以从latch.await()处继续向下运行
		 * @param args
		 * @throws InterruptedException
		 */
		public static void main(String[] args) throws InterruptedException {
			CountDownLatch latch = new CountDownLatch(3);
			Service service = new Service(latch);
			ThreadA ta = new ThreadA(service);
			ThreadA tb = new ThreadA(service);
			ThreadA tc = new ThreadA(service);
			System.out.println("main begin");
			ta.start();
			tb.start();
			tc.start();
			Thread.sleep(1000);
			System.out.println("main end");
			latch.await();
		}
	}

####2.await(long timeout, TimeUnit unit)方法测试
使线程在指定的最大时间内进入阻塞状态，如果超过这个时间则自动唤醒，程序继续向下运行，可以被其他线程提前唤醒

如果在指定的timeout时间内CountDownLatch的count计数归0（通过countDown方法），则方法返回为true，如果超时则返回false

####3.getCount()方法

获取当前计数的值，没调用一次countDown()方法，计数减1


##(二) CyclicBarrier
Cyclic,周期，循环； Barrier,门栓，关卡，障碍

类Cyclic可以实现多次阻塞效果，而不是像CountDownLatch一样一个对象仅仅支持1次阻塞的特性

CyclicBarrier的计数是加法操作

多个线程之间互相等待，任何一个线程完成前，所有的线程都必须等待

类CyclicBarrier与CountDownLatch在功能上相似，但在细节上有区别如下：
	
>(1)A角色：1个或多个线程
>
>(2)B角色：1个或多个线程

当使用类CyclicBarrier时由A角色来决定阻塞中的A角色是否继续向下运行，A角色线程在执行await()方法的次数小于parites个数的情况下A角色之间互相等待，直到await()方法执行次数等于parites，所有阻塞在await()方法的A角色线程才可以继续往下走，这期间不与B角色线程发生联系

####1.await()方法

public int await()返回值代表当前线程到达屏障点的顺序，即执行await()方法的顺序，其中返回getParites-1代表第一个到达屏障点，0代表最后一个到达屏障点

####2.CyclicBarrier构造方法，Runnable

在CyclicBarrier构造方法中，可以新建Runnable对象并重写run()方法，当所有线程通过屏障点时，main线程会执行重写的run()方法

####3.CyclicBarrier分批处理线程
如果CyclicBarrier设置的parties数，小于线程执行await()方法的个数，每个线程只能执行一次await()方法，线程则分批处理

如：parties为2，A,B,C三个线程，如果A,B先执行await()，则A,B不阻塞继续运行，C线程执行await()方法后出现阻塞

####4.多屏障 getNumberWaiting()方法
getNumberWaiting()方法返回的是当前CyclicBarrier方法有多少个线程在等待通过

线程全部通过await()方法屏障点后，自动将parites重置归零，为下一个屏障点，即await()方法做准备，在运行过程中，如果有一个线程报错，则不影响其他线程，该在await()屏障点处等待的继续等待

####5.isBroken()方法 BrokenBarrierException异常

isBroken()方法查询此屏障是否处于损坏状态，损坏状态抛出BrokenBarrierException异常

类CyclicBarrier对于线程的中断interrupted处理使用全有或全无的模式，**如果有一个线程由于中断或异常离开屏障点，其他所有线程都会抛出BrokenBarrierException异常，并离开屏障点**

####6.await(long timeout, TimeUnit unit)
如果在指定的时间内到达parites的数量，程序继续向下运行，否则如果出现超时，抛出TimeoutException异常

####7.getParites() reset()
getParites()方法返回parties个数

reset()重置屏障，使当前屏障销毁，在屏障点等待的线程出现BrokenExecutionException异常，并且getNumberWaiting值归零

#第三章  Phaser

CyclicBarrier类有一些自身上的缺陷，比如不可以动态的添加parties计数，调用一次await()方法仅仅占用一个parties数

在jdk1.7中新添加一个名称为Phaser类来解决这样的问题，Phaser就是CyclicBarrier的加强版

Phaser 移相器，Phaser类对计数的操作是加法操作

####1.arriveAndAwaitAdvance()方法

arriveAndAwaitAdvance()方法可以设置多重屏障，返回值X就是经过了第X个屏障
	
服务类
	
	public class Service {
		Phaser phaser = new Phaser(3);
	
		public void testMethod() throws InterruptedException {
			System.out.println(Thread.currentThread().getName() + " start");
			int arrive = phaser.arriveAndAwaitAdvance();
			System.out.println(Thread.currentThread().getName() + " " + arrive);
			System.out.println(Thread.currentThread().getName() + "   end");
			Thread.sleep(3000);
			System.out.println(Thread.currentThread().getName() + " start");
			int arrive1 = phaser.arriveAndAwaitAdvance();
			System.out.println(Thread.currentThread().getName() + " " + arrive1);
			System.out.println(Thread.currentThread().getName() + "   end");
		}
	}

线程类
	
	public class ThreadA extends Thread {
		private Service service;
		public ThreadA(Service service) {
			super();
			this.service = service;
		}
		@Override
		public void run() {
			try {
				service.testMethod();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

运行类
	
	public class Run {
		public static void main(String[] args) throws InterruptedException {
			Service service = new Service();
			ThreadA ta = new ThreadA(service);
			ThreadA tb = new ThreadA(service);
			ThreadA tc = new ThreadA(service);
			ta.start();
			Thread.sleep(1000);
			tb.start();
			Thread.sleep(1000);
			tc.start();
		}
	}

####2.public int getRegisteredParties()方法   public int register()方法

执行方法public int getRegisteredParties()获得注册的parties数量，返回值为当前的parties数量

执行方法public int register()使注册的parites数量+1

####3.public int arriveAndDeregister()方法

方法public int arriveAndDeregister()使线程不在阻塞，并使parties值减1，相当于这个线程退出了，方法返回值为当前线程经过了多少个阻塞点，即arriveAndAwaitAdvance()方法

####4.public final int getPhase()方法

方法public final int getPhase()获取的是当前线程已经到达了第几个屏障

####5.protected boolean onAdvance(int phase, int registeredParties)方法

方法protected boolean onAdvance(int phase, int registeredParties)的作用是，当线程将要通过屏障时,被调用，由最后一个到达屏障点的线程来执行onAdvance方法

onAdvance()方法返回true，phaser对象呈无效，销毁状态，所有线程无阻塞；返回false则phaser继续工作

####6.public int bulkRegister(int parties)方法

bulkRegister(int parties)可以批量增加parties数量

####7.getArrivedParties()方法    getUnarrivedParties()方法

方法getArrivedParties()获取当前Phaser对象已经被使用的parties个数

方法getUnarrivedParties()获取当前Phaser未被使用的parties个数

####8.类Phaser具有parties计数归零的特性

类Phaser，当线程通过屏障点后，phaser.getArrivedParties()计数归零

####9.arrive()方法测试

public int arrive()的作用能使phaser.getArrivedParties()值+1，不在屏障处等待，直接向下面的代码继续运行，并将计数进行重置

####10.awaitAdvance(int phase)方法    awaitAdvanceInterruptibly(int phase)
方法public int awaitAdvance(int phase)的作用是：如果传入参数phase值和当前phaser.getPhase()方法返回值一样，则当前线程在awaitAdvance(int phase)处阻塞，否则继续向下运行

如果在阻塞的过程中，getPhase()方法返回值发生改变，则取消当前线程在awaitAdvance(int phase)方法处的阻塞状态，继续向下运行

线程在awaitAdvance()处阻塞时，不可以中断,awaitAdvanceInterruptibly(int phaser)方法可以中断

方法awaitAdvance()与awaitAdvanceInterruptibly(int phaser)并不参与arrivedParties计数的操作，仅仅具有条件成立就阻塞的效果

####11.awaitAdvanceInterruptibly(int phase, long timeout, TimeUnit unit)方法
如果传入的参数phase值与当前线程phaser.getPhase()的返回值一样，则当前线程在此方法处阻塞最多timeout时间，如果在阻塞期间内phaser.getPhase()的返回值未变，出现异常

如果传入的参数phase值与当前线程phaser.getPhase()的返回值不一样，则线程不阻塞，继续运行

awaitAdvanceInterruptibly(int phase, long timeout, TimeUnit unit)方法可中断

####12.forceTermination()方法  isTeminated()方法

public void forceTermination()使Phaser对象的屏障功能失效，即线程不阻塞

public boolean isTermination()方法判断Phaser对象是否已经失效，如失效返回true

**forceTermination()方法仅仅将屏障取消，而CyclicBarrier类的reset方法则重置屏障，在其屏障点等待的线程报BrokenExecutionException异常**

####13.getRoot()方法  getParent()方法

getRoot()方法，返回当前Phaser对象的Phaser树根节点

getParent()方法，返回当前Phaser对象的父节点

####14.Phaser树  相位树
Phaser对象可以实现父子关系，构建Phaser树
	
**1.对父Phaser1添加Phaser2导致父Phaser1的registeredParties + 1**

如果父Phaser1有一个子Phaser2，则父Phaser1的registeredParties + 1， 如果父Phaser1有2个子Phaser2，则父Phaser1的registeredParties + 2

**2.根root节点，线程未到齐，则所有线程阻塞，包括子节点Phaser的线程,即使子节点Phaser的线程已经到齐**、

**3.根root节点，线程到齐，则所有线程运行，包括子节点Phaser的线程，即使子节点线程未到齐**

	public class Run {
		public static void main(String[] args) {
			Phaser p1 = new Phaser(1);
			Phaser p2 = new Phaser(p1, 2);
			Phaser p3 = new Phaser(p2, 2);
	
			System.out.println("p1 = " + p1);
			System.out.println("p2 = " + p2);
			System.out.println("p3 = " + p3);
			System.out.println();
			System.out.println("p1.getRoot()=" + p1.getRoot());
			System.out.println("p1.getparent()=" + p1.getParent());
			System.out.println();
			System.out.println("p2.getRoot()=" + p2.getRoot());
			System.out.println("p2.getparent()=" + p2.getParent());
			System.out.println();
			System.out.println("p3.getRoot()=" + p3.getRoot());
			System.out.println("p3.getparent()=" + p3.getParent());
			
			p3.arrive();
			p3.arrive();
			p2.arrive();
	//		p2.arrive();
			p1.arrive();
			p1.arriveAndAwaitAdvance();
			System.out.println("main end");
		}
	}

>真正正确的使用Phaser的情况下是实例化Phaser p1 = new Phaser(x)传入的x个parties，就需要启动x个线程去到达。创建p1对象的子对象p2时会使p1registeredParties加1，p1多出的这1个parties需要借助于p2线程全部到达来抵消


####15.控制Phaser类的运行时机

可以实现线程到达屏障后，不在继续运行的效果

在所有线程到达屏障点【arriveAndAwaitAdvance()方法处】之前，主线程调用phaser对象的register()方法，使phaser的注册数加1，导致及时所有分线程到达屏障点后，仍然凑不够数，无法继续运行

####16.线程分层操作Phaser树（官方文档实例）
递归为任务分组，每 TASK_PER_PHASER 个线程一分组，每组有一个Phaser对象，每组内操作自己的Phaser对象，操作单个Phaser的线程个数不能超过65535，否则报错，但使用此种分组形式，利用Phaser继承关系，可以将单个Phaser对象扩展成Phaser对象链表，增加可以操作Phaser对象的线程数

>jps jdk命令，查看当前JVM有多少线程在运行
>
>jstact -l 10000	具体查看ID为10000的线程信息

任务类

	public class MyRunnable implements Runnable {
		private Phaser phaser;
	
		public MyRunnable(Phaser phaser) {
			super();
			this.phaser = phaser;
			phaser.register();
		}
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + " start:"
					+ System.currentTimeMillis() + " phaser.hashCode() "
					+ phaser.hashCode());
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			phaser.arriveAndAwaitAdvance();
			System.out.println(Thread.currentThread().getName() + "   end:"
					+ System.currentTimeMillis() + "phaser.hashCode()"
					+ phaser.hashCode());
		}
	}

运行类
	
	public class Run {
		private static int TASK_PER_PHASER = 5;
	
		public static void main(String[] args) {
			Phaser rootPhaser = new Phaser();
			rootPhaser.getRegisteredParties();
			MyRunnable[] runnableArray = new MyRunnable[10];
			build(runnableArray, 0, 10, rootPhaser);
		}
	
		public static void build(MyRunnable[] tasks, int lo, int hi, Phaser phaser) {
			if (hi - lo > TASK_PER_PHASER) {
				for (int i = lo; i < hi; i += TASK_PER_PHASER) {
					int j = Math.min(i + TASK_PER_PHASER, hi);
					Phaser newPhaser = new Phaser(phaser);
					build(tasks, i, j, newPhaser);
				}
			} else {
				System.out.println("执行了 else分支:");
				for (int i = lo; i < hi; i++) {
					System.out.println(i + " ");
				}
				System.out.println();
				for (int i = lo; i < hi; i++) {
					tasks[i] = new MyRunnable(phaser);
					new Thread(tasks[i]).start();
				}
			}
		}
	}
	

#第四章 线程池 Executor ThreadPoolExecutor
使线程对象得到重用，这样在并发情况下，会减少服务器创建线程的开销，使服务器资源更多的关注业务逻辑

##（一）Executor接口
接口内只有一种方法 execute(Runnable),官方建议使用Executors工厂类来创建线程池对象

####1.使用newCachedThreadPool()方法创建无界线程池 
所谓无界就是池中存放线程的最大个数为Integer.MAX_VALUE，线程池内的线程为异步执行，线程池内的线程对象可以复用，但只有闲置，已经执行完上一次任务的线程才可以复用

运行类

	public class Run {
		public static void main(String[] args) {
			ExecutorService service = Executors.newCachedThreadPool();
			service.execute(new MyRunnable());
			System.out.println("main end");
		}
	}

线程类
	
	public class MyRunnable implements Runnable {
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + " start");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + "   end");
		}
	}

####2.newCachedThreadPool(ThreadFactory定制线程工厂
可以自定义线程工厂类，来自定义新建线程对象的过程，在调用newCachedThreadPool()方法时传入自定义线程工厂对象，该对象的类需要实现ThreadFactory接口
	
	public class MyThreadFactory implements ThreadFactory {
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setName("kepler");
			return thread;
		}
	}

使用无参newCachedThreadPool()方法时，jdk默认使用DefaultThreadFactory类创建线程工厂对象

####3.使用newFixedThreadPool(int)方法创建有界线程池
使用newFixedThreadPool(int)方法，可以指定线程池内线程对象的最大数量

####4.使用newSingleThreadExecutor()方法创建单一线程池

线程池内只有一个线程对象，以队列的方式执行任务

##（二）ThreadPoolExecutor的使用
Executors工厂类对象调用newSingleThreadExecutor()方法时，jdk实例化了1个ThreadPoolExecutor类

ThreadPoolExecutor构造方法可以定义其对象属性，使用ThreadPoolExecutor对象的原则为：

**核心池内如果有线程闲置，使用核心池；**

**核心池内线程都在执行任务，则在队列中放入任务对象，等待核心池内的线程来执行；**

**如果队列已经放不下了，则在非核心池内创建线程并执行任务，非核心池内的线程与核心池内的线程为异步运行；**

**如果非核心池内的线程闲置后，超过设定时间，则自动清除；如果其未完成任务，则在其完成任务后立即清除**

**如果非核心池内也放不下任务了，报错 rejectedExecutionException异常**

####1.LinkedBlockingQueue ArrayBlockingQueue SynchronousQueue 三种队列的使用

LinkedBlockingQueue阻塞队列，底层为链表实现，理论最大节点个数Integer.MAX_VALUE，可以自定义传入参数，定义节点个数，之后不可以更改节点个数

ArrayBlockingQueue阻塞队列，底层为数组实现，初始化时就需要传入节点个数，之后不可以更改节点个数，poll()方法取节点数据

SnchronousQueue，不存储任何数据，有一个线程放入数据，同时必须要有一个线程取出数据

####2.ThreadPoolExecutor
**1.常用构造方法**

	public ThreadPoolExecution(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue)

corePoolSize:核心池大小

maximumPoolSize:池的最大线程数

keepAliveTime：超过此时间，从非核心池内删除空闲线程；正在执行任务的线程在任务完成后立即删除；如果为0则非核心线程池内的线程在执行完任务后立即删除

unit：时间单位

workQueue：任务存放队列

调试类
	
	public class Run {
		public static void main(String[] args) throws InterruptedException {
			LinkedBlockingQueue queue = new LinkedBlockingQueue(3);
			ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 7, 5L,
					TimeUnit.SECONDS, queue);
			for(int i = 0; i < 11; i ++){
				executor.execute(new MyRunnable());
			}
			Thread.sleep(2000);
			System.out.println("queue.size()=" + queue.size());
			System.out.println("executor.getPoolSize()=" + executor.getPoolSize());
			System.out.println("main end");
		}
	}

**2.getCorePoolSize()  getMaximumPoolSize()方法**

ThreadPoolExecutor对象调用getCorePoolSize()返回核心池大小；getMaximumPoolSize()获取池最大线程数

**3.shutdown()  shutdownNow() isShutdown()方法**

shutdown()方法使ThreadPoolExecutor对象不允许添加新的任务，已经在运行与已经在队列里的任务不删除，继续运行，运行结束后对象关闭，进程消失

shutdownNow()方法使ThreadPoolExecutor对象不允许添加新的任务，已经在运行的任务不删除，继续运行，在队列中等待的任务删除，运行结束后对象关闭，进程消失，方法返回值List<Runnable>存储队列中被删除，未被执行的任务

当正在执行的任务中有if判断结合isInterrupted()方法，或者抛出InterruptedException异常时，调用shutdownNow()后，可以自定义中断正在运行的线程对象

isShutdown()方法判断线程池是否已经关闭，如果是返回true

**4.isTerminating()  isTerminated()方法**

isTerminating方法，在shutdown()或shutdownNow()方法被调用后，返回true

isTerminated()方法,在shutdown()或shutdownNow()方法被调用后，且线程池内没有线程执行任务了，返回true;

如果isTerminating方法返回true，则isTerminated()方法方法返回false

**5.awaitTermination(long timeout, TimeUnit unit)**

方法awaitTermination(long timeout, TimeUnit unit)被调用后，线程阻塞timeout时间后，判断ThreadPoolExecutor对象是否terminated,方法具有阻塞效果

**6.ThreadFactory Thread UncaughtExceptionHandler自定义处理异常**

在自定义ThreadFactory时，创建Thread对象后，使用Thread对象调用setUncaughtExceptionHandler(new UncaughtExceptionHandler()), 在UncaughtExceptionHandler()对象内重写uncaughtException()方法，自定义处理异常

**7.set/getRejectedExecutionHandler()**

设置或获取任务被拒绝后的行为

**8.getCompletedTaskCount()**

取得已经执行完的任务总数，每个线程执行完一次任务，总数+1

**9.prestartCoreThread()  preStartAllCoreThreads()方法  ThreadPoolExecutor的拒绝策略**

1.AbortPolicy：有线程被拒绝执行则抛出RejectedExecutionException异常，默认

2.CallerRunsPolicy：有线程被拒绝执行，会使用调用线程池的线程去运行该任务，如果main实例化线程池并添加任务，则main线程执行该被拒绝的任务

3.DiscardOldestPolicy：有线程被拒绝执行，会将队列中最后一个放入的任务对象删除，将该被执行任务放入队列

4.DiscardPolicy：有线程被拒绝执行，直接将该线程清除，不报异常

可以通过ThreadPoolExecutor对象调用setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy())方法设置拒绝策略，也可以直接在构造ThreadPoolExecutor
对象时，在最后一个参数传入拒绝策略对象new ThreadPoolExecutor.AbortPolicy()

**10.afterExecute()  beforeExecute()方法**

自定义线程池类，继承ThreadPoolExecutor并重写afterExecute()  beforeExecute()方法
	
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		t.setName("kepler" + Math.random());
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		System.out.println("after execute");
	}

**11.remove(Runnable)**

如果是execute()方法提交的任务，remove(Runnable)方法可以删除尚未执行的任务，已经在运行则不能删除

如果是submit()方法提交的任务，则remove(Runnable)方法不能删除

**12.其他get方法**

1.public int getActiveCount()：取得当前有多少个线程在执行任务

2.public int getCorePoolSize()：获取核心线程池大小

3.public int getMaximumPoolSize()：获取池的最大数

4.public int getTaskCount()：取得有多少任务发送给了线程池

5.public int getLargestPoolSize()：获取池内历史最多的线程数

#第五章 Future Callable

默认情况下，线程Thread对象不具有返回值的功能，使用Future与Callable可以获得线程的返回值

##Future
获得线程的返回值

####1.Callable接口  Runnable接口的区别
接口Callable与Runnable区别在于，Callable的call方法有返回值，且可以抛出异常；Runnable接口中的run方法没有返回值，且不可以抛出异常

####2.使用ExecutorService中的submit(Callable<T>)实现获得返回值
	
	任务类

	public class MyCallable implements Callable<String> {
		private int age;
		public MyCallable(int age) {
			super();
			this.age = age;
		}
		@Override
		public String call() throws Exception {
			Thread.sleep(8000);
			return "返回值：" + age;
		}
	}

运行类
	
	public class Run {
		public static void main(String[] args) throws InterruptedException {
			MyCallable callable = new MyCallable(100);
			ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 3, 5L,
					TimeUnit.SECONDS, new LinkedBlockingDeque());
			Future<String> future = executor.submit(callable);
			System.out.println("future get begin:" + System.currentTimeMillis());
			try {
				System.out.println(future.get());
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			System.out.println("future get   end:" + System.currentTimeMillis());
		}
	}

运行后可以看出，future.get()方法具有阻塞特征

####3.接口ExecutorService中的submit(Runnable)和boolean isDone()的使用

submit方法可以传入Callable对象，也可以传入Runnable对象，不过传入Runnable对象后无返回值

isDone()方法判断future对象是否完成获得返回值

**submit()方法提交的任务，任务抛异常之后，不会打印异常信息,使用future对象调用get()方法，则抛出异常信息**

####4.cancel(boolean) isCancel()方法

cancel(boolean)方法：传入ture,如果future对象等待返回值的线程正在运行则中断，但是需要在任务类中加入if判断与isInterrupted()方法来自定义抛出异常

cancel(boolean)方法传入false则不中断正在运行的任务

isCancel()方法：返回future等待返回值的线程对象是否已经取消

####5.get(long timeout, TimeUnit unit)
方法get(long timeout, TimeUnit unit)：在指定的最大时间内等待获得返回值，超时则抛出TimeoutException

####6.Future对象 submit()方法与execute()方法的区别

1.submit()方法有返回值，为Future对象；execute()方法没有返回值

2.submit()在默认的情况下，可以用catch捕获异常,但是需要用Future对象调用get方法时才能捕获；execute()方法默认情况下直接抛出异常，不能捕获，但可以通过ThreadFactory，在构建Thread对象时，调用setUncaughtExceptionHandler()方法进行捕捉异常

submit()方法提交的任务会被立即执行；execute()方法提交的任务，在对象调用get()方法之后才会运行；

submit()方法提交的任务，执行结果封装进FutureTask对象内，通过get()方法获得Callable的返回值或异常信息,在任务类内使用catch可以直接捕获submit方法提交的任务所抛出的异常

####7.Future get()方法阻塞性
Future对象调用get()方法，只能按照线程运行的顺序来获取返回值，多个Future对象获取多个线程的返回值，运行快的任务如果在运行慢的任务之后启动，只能等到运行慢的任务返回其结果后，早已运行完的线程才能返回其值，效率较慢

#第六章 ExecutorCompletionService

哪个任务先完成，就先处理哪个线程的返回值，底层用队列实现，哪个线程先完成，就将Future对象结果放入队列头，ExecutorCompletionService对象调用take()方法获取队列Future对象，如果队列没有对象就阻塞

####1.使用ExecutorCompletionService提高获取线程返回值的效率

任务类

	public class MyCallable implements Callable<String> {
		private long sleepTime;
		public MyCallable(long sleepTime) {
			super();
			this.sleepTime = sleepTime;
		}
		@Override
		public String call() throws Exception {
			System.out.println(Thread.currentThread().getName() + " start "
					+ System.currentTimeMillis());
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + "   end "
					+ System.currentTimeMillis());
			return Thread.currentThread().getName() + " 经过" + sleepTime + "秒后，返回数据"
					+ System.currentTimeMillis();
		}
	}

运行类
	
	public class Run {
		public static void main(String[] args) throws InterruptedException,
				ExecutionException {
			System.out.println("main start=" + System.currentTimeMillis());
			LinkedBlockingQueue queue = new LinkedBlockingQueue(3);
			ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 7, 5L,
					TimeUnit.SECONDS, queue);
			CompletionService service = new ExecutorCompletionService(executor);
			MyCallable c1 = new MyCallable(3000);
			MyCallable c2 = new MyCallable(1000);
			MyCallable c3 = new MyCallable(5000);
			MyCallable c4 = new MyCallable(4000);
			MyCallable c5 = new MyCallable(2000);
			service.submit(c1);
			service.submit(c2);
			service.submit(c3);
			service.submit(c4);
			service.submit(c5);
			System.out.println(service.take().get());
			System.out.println("main end=" + System.currentTimeMillis());
		}
	}

####2.ExecutorCompletionService对象  take()方法具有阻塞性

ExecutorCompletionService对象在调用take()方法后，如果底层workQueue没有存入Future对象，则阻塞直到队列头有对象可以取

####3.ExecutorCompletionService对象  poll()方法没有阻塞性

ExecutorCompletionService对象在调用take()方法后，返回底层队列workQueue的队列头Future对象，并将队列头清除。如果没有取到，说明还没有线程完成任务放入Future对象，返回false，poll()方法不阻塞

####4.poll(long timeout, TimeUnit unit)方法
等待指定的timeout时间，在timeout时间内获取到值，则立即继续运行并清除队列头的Future对象，如果超时不报错立即运行

####5.ExecutorCompletionService对象 异常处理
由于ExecutorCompletionService对象使用submit方法提交任务，所以当线程执行任务时抛出异常，并不是立即打印异常，而将异常封装进FutureTask对象内并返回，调用Future对象get()方法才能抛出异常并打印


#第七章 接口ExecutorService

####1.invokeAny()方法
只取得最先完成任务的结果值

返回值，为首先执行完任务的线程的返回值
	
短时间任务

	public class MyCallableA implements Callable<String> {
		@Override
		public String call() throws Exception {
			for(int i = 0; i < 1234; i ++){
				String str = new String();
				String str1 = new String();
				String str2 = new String();
				String str3 = new String();
				String str4 = new String();
			}
			return " callable A";
		}
	}

长时间任务
	
	public class MyCallableC implements Callable<String> {
		@Override
		public String call() throws Exception {
			for(int i = 0; i < 1234567890; i ++){
				String str = new String();
				String str1 = new String();
				String str2 = new String();
				String str3 = new String();
				String str4 = new String();
			}
			return " callable C";
		}
	}

运行类
	
	public class Run {
		public static void main(String[] args) throws InterruptedException, ExecutionException {
			ExecutorService service = Executors.newCachedThreadPool();
			List lists = new ArrayList();
			lists.add(new MyCallableA());
			lists.add(new MyCallableB());
			lists.add(new MyCallableC());
			String str = (String) service.invokeAny(lists);
			System.out.println(str);
		}
	}

main线程首先得到"callable A"，如果获得首个返回值之后，会Interrupted其他线程，但是其他线程不打印出异常，而是将异常信息封装到FutureTask对象内

**invokeAny提交的任务，如果运行快的线程报异常，则不获取其返回值，直到获取到其他某个线程正确的返回值；如果报异常的任务，在内部使用catch自行捕获异常，则jdk不会查看到这个线程已经报异常了，可以获取这个线程的返回值，虽然这个值不是正确的**

如果invokeAny()方法提交的任务全都报异常，则最终打印出最后抛出的异常，之前的异常信息被覆盖

####2.invokeAll()
取得所有任务返回的Future对象集合

如果线程在执行任务过程中报错，放入FutureTask对象内,不影响其他线程
	
更改运行类
	
	public class Run {
		public static void main(String[] args) throws InterruptedException, ExecutionException {
			ExecutorService service = Executors.newCachedThreadPool();
			List lists = new ArrayList();
			lists.add(new MyCallableA());
			lists.add(new MyCallableB());
			lists.add(new MyCallableC());
			List list = service.invokeAll(lists);
			System.out.println(list);
		}
	}

方法为阻塞方法，要把所有的FutureTask对象都取回后，在继续向下运行，FutureTask对象内为线程返回的结果值或者异常信息

**invokeAll()方法提交的任务当其中某个线程执行时抛异常，不影响其他线程执行任务，不Interrupted其他线程**

**invokeAll()方法返回的List集合中的FutureTask对象的顺序，与放入方法参数集合的任务顺序一致**

####3.invokeAny(Collection Tasks, timeout, timeUnit)
在timeout时间内获取第一个未报错，执行完任务的线程的返回值，超时则报出TimeoutException；

超时后，jdk会对正在执行任务的线程进行interrupted，可以结合if判断与throw抛出异常来终止正在执行任务的线程

####4.invokeAll(Collection tasks, long timeout, TimeUnit unit)

指定时间内获取全部线程的返回值；如果超时，则返回的List中存储任务完成结果的Future对象，**但Future对象的状态可能是cancel,而且超时后，jdk对所有正在执行任务的线程interrupted**，结合if与throw可以中断运行中的任务

#第八章 接口ScheduledExecutorService
Timer工具类提供了以计时器或计划任务的功能来实现按照指定时间或周期执行任务。但由于Timer不是以ThreadPool,而是以queue来管理线程，在并发情况下效率较低，ScheduleThreadPoolExecutor类可以解决这个问题

类ScheduledExecutorService的主要作用为：将定时任务与线程池结合使用

ScheduledExecutorService对象主要由Executors工厂类构建，默认的实现类只有一个:ScheduledThreadPoolExecutor

####1.schedule()方法，延时执行任务
1.public ScheduledFuture<?> schedule(Runnable command,long delay, TimeUnit unit)

>参数delay就是延迟的时间，此方法不阻塞

如果传入Callable则有返回值，传入Runnable的返回值是为了操作任务对象，例如可以删除Future

任务类
	
	public class MyCallableA implements Callable<String> {
		@Override
		public String call() throws Exception {
			System.out.println("call A begin=" + Thread.currentThread().getName()
					+ " " + System.currentTimeMillis());
			Thread.sleep(3000);
			System.out.println("call A   end=" + Thread.currentThread().getName()
					+ " " + System.currentTimeMillis());
			return "A";
		}
	}

	public class MyCallableB implements Callable<String> {
		@Override
		public String call() throws Exception {
			System.out.println("call B begin=" + Thread.currentThread().getName()
					+ " " + System.currentTimeMillis());
			Thread.sleep(3000);
			System.out.println("call B   end=" + Thread.currentThread().getName()
					+ " " + System.currentTimeMillis());
			return "B";
		}
	}

运行类
	
	public class Run {
		public static void main(String[] args) throws InterruptedException, ExecutionException {
			ScheduledExecutorService service = Executors
					.newSingleThreadScheduledExecutor();
			ScheduledFuture<String> scheduleA = service.schedule(new MyCallableA(),
					4L, TimeUnit.SECONDS);
			ScheduledFuture<String> scheduleB = service.schedule(new MyCallableB(),
					6L, TimeUnit.SECONDS);
			System.out.println(scheduleA.get());
			System.out.println(scheduleB.get());
		}
	}

单线程池执行两个任务

####2.scheduleAtFixedRate()方法，循环执行任务

public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,long initialDelay,long period,TimeUnit unit)

以两个循环开始时间为周期

如果任务的执行时间长于周期时间，一个循环结束后，马上执行另一个循环

####3.scheduleWithFixedDelay()方法，循环执行任务
public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,long initialDelay,long delay,TimeUnit unit)

以上一个任务的结束时间到下一个任务的开始时间为周期

####4.setExecuteExistingDelayedTasksAfterShutdownPolicy(boolean)
当执行ScheduledThreadPoolExecutor类的shutdown()方法时，使用schedule()方法添加的任务会继续执行，而调用这个方法传入false,scheduled()方法提交的任务不在运行。

**这个方法没有对线程执行Interrupted，只是将队列中未执行的任务删除**

####5.setContinueExistingPeriodicTasksAfterShutdownPolicy(boolean)
使用scheduleAtFixedRate()方法或scheduleWithFixedDelay()方法提交的循环执行任务，在ScheduleThreadPoolExecutor对象调用shutdown()后，循环会被取消；当调用这个方法传入true时，循环会继续

####6.使用shutdown前，清除队列定时任务的四种情况

1.清除schedule方法任务与scheduleAtFixedRate方法任务

>使用shutdown前，执行setExecuteExistingDelayedTasksAfterShutdownPolicy(false)

2.不清除schedule方法任务与不清除scheduleAtFixedRate方法任务

>使用shutdown前，执行setContinueExistingPeriodicTasksAfterShutdownPolicy(true)

3.清除schedule方法任务与不清除scheduleAtFixedRate方法任务

>使用shutdown前，执行setContinueExistingPeriodicTasksAfterShutdownPolicy(true)，与
setExecuteExistingDelayedTasksAfterShutdownPolicy(false)

4.不清除schedule方法任务与不清除scheduleAtFixedRate方法任务

>不清除schedule方法任务与清除scheduleAtFixedRate方法任务

>只执行shutdown()

####7.setRemoveOnCancelPolicy(boolean)
Future对象调用后，传入true,则取消任务；如果任务正在执行,则通过if与throw来停止任务；

方法setRemoveOnCancelPolicy(boolean)对正在执行的任务Interrupted

该方法会移除队列中的任务，如果调用Future对象 cancel方法不会移除队列中的任务，只是取消

####8单计划任务池执行多个循环任务

当单计划任务池执行多个循环任务，多个循环任务在执行队列中的顺序会不断变化，但队列头的任务的循环开始时间距离当前时间最短。

可以在核心池内增加线程数量，使多个循环任务异步执行

#第九章 Fork Join分治编程

jdk1.7，把大任务分割成若干个小任务，再把每个小任务得到的结果进行汇总

工作窃取

>要完成一个比较大的任务，完全可以把这个大的任务分割成若干个小任务，为了更加方便的管理任务，于是把这些小任务分别放到不同的队列里，这时有时候会出现有的线程会先把自己队列里的任务快速执行完毕，而其他线程对应的队列里还有任务等待处理。完成任务的线程，去帮助其他线程分担要执行的任务，这就是"工作窃取算法"

####1.ForkJoinPool

继承于AbstractExecutorService

类ForkJoinPool所提供的功能是一个任务线程池，而执行具体任务却不是ForkJoinPool，而是ForkJoinTask类，该类是抽象类，不能实例化，需要该类的三个子类，CountedCompleter，RecursiveAction，RecursiveTask来进行实现
	
分解任务类
	
	public class MyRecursiveAction extends RecursiveAction {
		private int beginValue;
		private int endValue;
		public MyRecursiveAction(int beginValue, int endValue) {
			super();
			this.beginValue = beginValue;
			this.endValue = endValue;
		}
		@Override
		protected void compute() {
			System.out.println(Thread.currentThread().getName() + "------");
			if (endValue - beginValue > 2) {
				int middleValue = (beginValue + endValue) / 2;
				MyRecursiveAction leftAction = new MyRecursiveAction(beginValue,
						middleValue);
				MyRecursiveAction rightAction = new MyRecursiveAction(
						middleValue + 1, endValue);
				this.invokeAll(leftAction, rightAction);
			} else {
				System.out.println("打印组合：" + beginValue + "-" + endValue);
			}
		}
	}

运行类
	
	public class Run {
		public static void main(String[] args) throws InterruptedException {
			ForkJoinPool pool = new ForkJoinPool();
			pool.submit(new MyRecursiveAction(1, 10));
			Thread.sleep(5000);
			System.out.println("main end");
		}
	}



在ForkJoinPool对象提交任务后，MyRecursiveAction对象内的compute()方法，调用本对象的invokeAll()方法，invokeAll()内继续调用compute方法，形成互相递归

####2.使用RecursiveTask取得返回值与join()和get()方法的区别

1.关于异常
>join()不抛出异常，如果任务执行时抛出异常则直接报错，无法通过自定义catch捕获

>get()抛出异常，可以自定义catch捕获

2.关于阻塞,两个方法相同

>get(), join()方法执行后，会等待同一级别的分解任务获得返回值后才能一起执行

####3.fork()方法
fork()方法的作用与invokeAll()方法相似，都是分发新的任务，但fork()方法会创建更多的线程

####4.public void execute(ForkJoinTask<?> task)， invoke()如何处理返回值

execute没有返回值，如果想处理返回值，直接使用task.get()

如果使用invoke()方法，可以直接获取任务的返回值，而不像execute()与submit()方法需要ForkJoinTask对象调用get()方法获取返回值

####5.awaitQuiescence(long time, TimeUnit unit)方法
awaitQuiescence(long time, TimeUnit unit)判断任务池在指定时间内是否是静止状态，即没有线程正在执行任务或者被阻塞

####6.其他get方法

1.public int getParallelism()
>获得并行的数量，指CPU的内核数

2.public int getPoolSize()
>获得任务池的大小

3.public int getQueuedSubmissionCount()
>获得已经提交但尚未被执行的任务数量

4.public boolean hasQueuedSubmissions()
>判断队列中是否有未执行的任务

5.public int getActiveThreadCount()
>获得活动的线程个数

6.public long getQueuedTaskCount()
>获得任务的总个数

7.public long getStealCount()
>获得偷窃的任务个数

8.public int getRunningThreadCount()
>获得正在运行并且不再阻塞状态下的线程个数

9.public boolean isQuiescent()
>判断任务池是否是静止未执行任务的状态

10.public static ForkJoinPool commonPool()
>返回任务池对象的实例

11.public static int getCommonPoolParallelism()
>返回任务池对象的并行级别

####7.对异常的处理
1.isCompletedAbnormally()
>判断任务是否出现异常，完成的不自然

2.isCompletedNormally
>判断任务是否自然的完成，未报异常

3.getException()
>返回报错异常


#第十章 并发集合框架

jdk集合框架父接口Iterable,内部定义了一个迭代方法,iterator()

##1.定义的接口

Iterable子接口Collection提供了集合框架最主要，最常用的操作，集合的CRUD

1.List
>列，对Collection接口进行了扩展，允许根据索引位置操作数据,允许内容重复

>ArrayList Vector

2.Set
>集合，扩展Collection接口功能，默认特点是不允许元素重复，防止重复的原理是元素需要重写，hashCode()和equals()方法，如果放入重复元素则覆盖

>HashSet(无序) LinkedHashSet（有序） TreeSet

3.Queue
>队，扩展Collection接口功能，可以方便的操作队头

>PriorityQueue(非线程安全)

4.Deque
>继承于Queue，允许操作队头与队尾

>ArrayDeque:从队列两端获取数据

>LinkedList:从队列两端获取数据，并根据索引位置操作数据

##2.非阻塞队列
非阻塞队列就是队列没有数据时，操作队列时出现异常或返回null，不具有阻塞特性

常见的非阻塞队列
>ConcurrentHashMap ConcurrentSkipListMap ConcurrentSkipListSet ConcurrentLinkedQueue ConcurrentLinkedDeque CopyOnWriteArrayList CopyOnWriteArraySet

####1.concurrentHashMap
支持并发操作的Map

HashMap不是线程安全的，如果想在并发环境下使用key-value,可以使用Hashtable

Hashtable类支持并发环境下的put()添加操作，却不支持remove删除操作，但ConcurrentHashMap却支持这两个功能

ConcurrentHashMap不支持排序,虽然LinkedHashMap支持key的顺序性，但又不支持并发，这种情况下可以使用ConcurrentSkipListMap

####2.ConcurrentSkipListMap
ConcurrentSkipListMap支持并发与key排序，需要在要放入map的实体类中实现Comparable

####3.ConcurrentSkipListSet
类ConcurrentSkipListSet支持排序而且不允许重复的元素，如果放入重复元素则覆盖

####4.ConcurrentLinkedQueue
并发环境的队列操作

方法poll()，当没有获得数据时返回null，如果有数据时则移除表头，并将表头进行返回

方法element()，当没有获得数据时出现NoSuchElementException异常，如果有数据时不移除表头，并将表头进行返回

方法peek()，当没有获得数据时返回null，如果有数据时则不移除表头，并将表头进行返回

####5.ConcurrentLinkedDeque
支持对列头列尾双向操作，支持并发

####6.CopyOnWriteArrayList
线程安全，有序集合

####7.CopyOnWriteArraySet
可以解决并发情况下，HashSet不是线程安全的问题，无序集合

##3.阻塞队列

如果阻塞队列是空的，有线程从中获取数据则会被阻塞，直到阻塞队列内被添加数据后；同样如果队列是满的，向其添加数据时也会阻塞，直到队列内有多余空间

####1.ArrayBlockingQueue与公平锁，非公平锁的使用
方法put(object o)向队列内添加对象，take()获取队列头的对象

如果在创建ArrayBlockingQueue对象时，构造器设置的是公平锁，那么被其阻塞的线程唤醒顺序与线程的启动顺序一致，阻塞线程放入队列中按顺序启动，如果是非公平锁则随机抢锁

####2.PriorityBlockingQueue
支持在并发情况下的优先级队列，如果放入其中的对象实现了Comparable接口，则可以按照比较顺序放入队列内，与调用queue.add(Object o)的顺序无关

####3.LinkedBlockingQueue
与ArrayBlockingQueue相似，都是有界的，都具有阻塞性，ArrayBlockingQueue运行效率要强于LinkedBlockingQueue

####4.LinkedBlockingDeque
类似LinkedBlockingQueue，可以头尾同时操作的阻塞队列

####5.SynchronousQueue
同步队列，有一个线程加入数据，必须要有一个线程获得数据，只起到管道的作用，无容量概念

####6.DelayQueue
延时执行队列，实体类内需要实现Delayed接口，实体类内compareTo()方法决定了entity对象在队列中的顺序，getDelay()方法返回延时的时间到了，就将队列中列头的任务取出并执行，getDelay方法返回小的值的entity对象，放在队列的前面

####7.LinkedTransferQueue
类似于SynchronousQueue，但具有嗅探功能，可以尝试性添加数据

1.take()方法：具有阻塞性，如果没有数据可以获取就阻塞，直到有数据获取

2.transfer(Object o)方法：
>1.如果当前存在一个正在等待获取值的消费者线程，则立即把数据Object o对象传过去

>2.如果没有消费者线程，则将Object o放入队列尾，并且进入阻塞状态，直到有消费者线程取数据

3.tryTransfer()方法：
>1.如果当前存在一个正在等待获取值的消费者线程，则立即把数据Object o对象传过去

>2.如果没有消费者线程，则返回false，数据Object o 丢弃，不阻塞

4.tryTransfer(Object o, long time, TimeUnit unit)方法：
>1.如果当前存在一个正在等待获取值的消费者线程，则立即把数据Object o对象传过去

>2.如果在指定的时间内元素Object o 没有被消费者线程取走，返回false，不阻塞，数据Object o 丢弃

5.Boolean hasWaitingConsumer()  int getWaitingConsumerCount()
>1.hasWaitingConsumer()方法：判断是否有消费者线程正在等待数据

>2.int getWaitingConsumerCount()方法，获取有多少个消费者线程正在等待数据

