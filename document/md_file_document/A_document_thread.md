#第一章 Java多线程技术
##(一)进程和多线程的感念，及线程的优缺点
####1.进程的概念

>进程是操作系统的基本单位，是一次程序的执行，是一个程序极其数据在处理机上顺序执行
时所发生的活动，是程序在一个数据集合上运行的过程，它是系统进行资源分配和调度的一
个独立单位。在操作系统中运行的exe文件程序，就可以看做是进程。

####2.线程的概念
>是进程中独立运行的子任务

####3.线程的特点
单线程的缺点：
>CPU的利用率低（同步）

单线程的优点：
>可控性

多线程的缺点：
>随机不可控（异步）

多线程的优点：
>效率高，运行速度快

####4.线程的使用场景
阻塞
>一旦系统中出现了阻塞现象，则可以根据实际情况来使用多线程提高运行效率

依赖
>业务分为两个执行过程，分别是A和B，当A业务有阻塞情况发生，B业务的执行
不依赖A业务的执行结果，这个时候可以使用多线程来提高运行效率，如果B业务
依赖A业务的执行结果，则不适用多线程技术，按顺序进行业务执行。

##(二)使用多线程
创建新执行的线程有两种方法（实际为一种）

1.**将类声明为 Thread 的子类**，该子类应重写 Thread 类的 run 方法。
接下来可以分配并启动该子类的实例。

2.**声明实现 Runnable 接口的类**，该类然后实现 run 方法。
然后可以分配该类的实例，在创建 Thread 时作为一个参数来传递并启动。

####1.继承Thread类
1.重写run()方法 

2.使用strat()启动1个线程，启动后自动调用线程对象中的方法。

3.调用strat()方法与调用run（）方法的区别

>a:调用start()方法，过程是让“线程规划器”让系统安排线程时间去调用run（）方法，
不启动新的线程，具有异步的效果，效率高。

>b:调用run（）方法，过程，是让main主线程立即调用run（）方法，并不用“线程规划器”
启动新的线程，具有同步效果，效率低。

####2.实现Runnable接口
1.需要实现接口

2.Thread类，也实现了Runnable接口

3.Runnable 为非 Thread 子类的类提供了一种激活方式。
通过实例化某个 Thread 实例并将自身作为运行目标，
就可以运行实现 Runnable 的类而无需创建 Thread 的子类。

注意：
>方法 run

>方法 run 的常规协定是，它可能执行任何所需的动作。

>可以理解为，创建新的线程，所执行的代码，都在run（）中进行编写。

####3.实例变量与线程安全
a.实例变量针对其他线程，有共享数据，与不共享数据之分

b.共享数据情况下，出现非线程安全问题，是因为，
数据共享，多个线程执行赋值，出现值被覆盖的情况。
解决这一问题，用synchronized关键字，对执行顺序进行排序同步。

c.synchronized关键字 排队方式进行处理，解决非线程安全问题

d.非线程安全: 非线程安全主要指的是多个线程对同一个对象中的同一个实例变量进行操作时，
会出值被更改，值不同步的情况进而影响程序的执行流程。

>（1）非线程安全原因
>
>非线程安全主要指的是多个线程对同一个对象中的同一个实例变量进行操作时，
会出值被更改，值不同步的情况进而影响程序的执行流程。

>当Runnable上的数据，被共享，就是造成非线程安全问题
>
>(2)非线程安全解决办法 synchronized

**注意：在web开发中，servlet也是单例模式，为了不出现非线程安全问题，
建议不要在servlet中出现实例变量。**

####4.留意i--与System.out.println()的异常
1.会出现异常原因
>虽然printIn（）方法在内部是同步执行的，但i--的操作却是在进入printIn（）
之前发生的，所以有发生非线程安全问题的概率

2.解决方法
>还是使应继续使用同步方法。i--与输出System.out.println()不联合使用，避免出现问题。

##（三）多线程方法总结

####1.currentThread()方法
返回当前正在执行这行代码的线程对象的引用。

注意：执行方法run()和start()的区别
>run()：立即执行run（）方法 ，并不启动线程，相当于执行普通方法

>start()：执行run（）方法 时机不确定，启动新的线程

用启动线程启动一个新的线程，线程启动后会自动调用对象中的run()方法
run()方法里面的代码就是线程对象要执行的任务。

####2.isAlive()方法
测试线程是否处于活动状态

注意：Thread.currentThread().getName()与this的差异
>this是代表当前类的对象，与线程无关。

>currentThread()是代表返回当前正在执行这行代码的线程对象的引用。

####3.sleep(long millis)方法
在指定的毫秒数内让当前正在执行的线程休眠（暂停执行）

此操作受到系统计时器和调度程序精度和准确性的影响

####4.getId()方法
返回该线程的标识符

##（四）线程的停止
####停止线程的三种方式：

>1.使用退出标志，使线程正常退出，也就是当run方法完成后线程终止

>2.使用stop方法强行终止线程，（作废过期方法）

>3.使用interrupted(不会立即停止线程，只是做标记)

####1.停止不了的线程
使用interrupt方法中断线程。线程不能立即停止，仅仅是在线程中打了一个停止的标记，
并不是真的停止线程。

####2.判断线程是否为停止状态
1.interrupted()

>静态方法  public static boolean interrupted(),测试currentThread（）是否已经中断

2.isInterrupted()

>非静态方法   XXX.isInterrputed() 判断线程对象XXX是否已经中断

####3.能停止的线程--异常
线程自己抛出异常---线程即停止

####4.线程沉睡时停止异常
当线程sleep()时，停止线程，进入catch块内，反过来一样

####5.暴力停止线程
用stop()停止线程，是暴力的（过期作废的方法）

####6.方法stop（）与Java.lang.ThreadDead异常
调用stop()会出现Java.lang.ThreadDeath异常，通常情况，不需要显式捕捉。

用stop()停止线程，会导致数据得不到同步处理，出现数据不一致问题。

####7.stop强制释放锁的不良后果
调用stop()停止线程，会释放锁，导致数据得不到同步处理，出现数据不一致问题。
原因是，线程运行一半时，线程被强制停止销毁，锁被释放，数据处理不完全造成的。

####8.return停止线程
用 return来停止线程，虽然简单，但是还是建议用“抛异常”方法停止线程。
因为抛异常方法，可以在，catch块中，对异常信息进行相关的统一处理，更流程，更简洁。

##（五）暂停线程（过期作废方法）
####1.suspend与resume方法的使用
suspend方法暂停线程 resume方法恢复线程

####2.suspend与resume方法的缺点，独占
suspend方法不释放锁

使用不当会出现当前线程独占锁，其他线程无法获得锁的情况

####3.suspend与resume方法的缺点，不同步
在使用suspend与resume方法，还会出现，因为暂停，导致的数据不同步现象。

##（六）yeild方法
放弃当前的CPU资源。放弃的时间不确定，可能刚放弃，就马上执行了。
##（七）线程的优先级
线程的优先级分为1—10,10个等级，大于或者小于，都会抛出异常：throw new IllegalArgumentException

>JDK 中使用三个常量来预置定义优先级的值：
>
	public final static int  MIN_PRIORITY  =  1 ;
	public final static int NORM_PRIORITY  =  5 ;
	public final static int MAX_PRIORITY  =  10 ;

####1.线程优先级的继承性
比如  A线程 启动 B 线程 则B线程的优先级与A是一样的。

####2.优先级与运行结果具有规则性
CPU尽量将执行资源，让给，优先级高的线程。**注意： 线程的优先级，与代码执行顺序无关**

####3.优先级与运行结果具有随机性
优先级高的线程，不一定每次都先执行完。即：线程的优先级与打印顺序无关。

####4、看谁运行的快
优先级高的运行的快

##（八）守护线程
用户线程：用户线程，线程结束，销毁。

守护线程：伴随最后一个用户线程的销毁，而销毁

最典型的守护线程就是，“垃圾回收器”。

##第二章 对象及变量的并发访问

##(一)synchronized同步快
线程安全：不会出现“脏读”现象的，就是线程安全的

非线程安全：多个线程对同一个实例变量进行并发访问（异步执行）时，产生的后果，就是“脏读”，也就是说，取到的数据，被更改过。

对实例变量进行，同步处理（synchronized关键字）就不会出现“脏读”情况。

####1.方法内的变量为线程安全
方法内部的私有变量，不存在非线程安全问题，是线程安全。

方法内部变量是私有变量，不公开的特性，确定，它是安全的。

####2.实例变量的非线程安全

实例变量公开的特性，确定，它是非安全的。

对实例变量，需要用synchronized关键字对方法或块进行声明，进行同步化处理，变成安全的。

####3.多个对象多个锁
两个线程分别访问，同一个类的两个不同对象，产生两个锁

两个线程分别获取各自的锁，以异步方式运行。

关键字synchronized取得的锁，都是对象锁，而不是一段代码或者方法  当做锁。

同步的单词为synchronized 异步的单词为asynchronized

####4.synchronized 方法与锁对象
调用synchronized关键字，一定是排队运行的

只有线程之间的“共享资源”才需要调用synchronized关键字，进行同步处理。

synchronized结论：
>A线程先持有object对象的Lock锁，B线程可以 以 异步的方式调用object对象中的非synchronized类型的方法。

>A线程先持有object对象的Lock锁，B线程如果在这个时候调用object对象中的synchronized类型的方法，则需要等待排队执行，也就是同步。

synchronized，锁的是对象，哪些资源需要“共享”时，就对访问共享资源的方法或代码块声明synchronized。

####5、脏读

在读取实例变量时，变量值已经被其他线程更改过了，这样数据交叉的现象叫脏读

//TODO

####6.synchronized 锁重入
自己可以再次获得自己的内部锁

锁重入支持父子继承的环境中，（即子类有锁，并且调用父类（父类也持有锁）线程获得子类锁，也获得父类锁)

####7、出现异常，锁自动释放
当线程出现异常，锁会自动释放。

**注意：类Thread.java中的suspend()和sleep(millis)方法被调用后不释放锁**

####8、同步不具有继承性
同步不能继承，必须在父类和与子类方法中加synchronized关键字，才能达到同步效果

##(二)synchronized同步语句块
同步方法与同步代码块两者区别

synchronized方法
>是对当前对象进行枷锁。 同一时间，只有一个线程执行synchronized “同步方法中”的代码

synchronized代码块
>是对某一个对象枷锁。 同一时间，只有一个线程执行synchronized “同步代码块中”的代码

####1.synchronized方法的弊端
比如A线程调用同步方法执行时间很长，B线程只能等待很长时间才能调用同步方法

synchronized代码块可以解决，只将一部分代码作为同步处理，而不是整个方法，只需要加在，需要同步运行的地方即可

####2.一半同步，一半异步
在synchronized代码块中，同步

不在synchronized代码块中，异步

####3.synchronized代码块的同步性
synchronized代码块，锁的是对象，并不是代码

注意：
在一个类中，有多个synchronized 代码块，多个线程访问不同的synchronized 代码块，形成同步按顺序执行情况，说明，这些synchronized 代码块，锁的对象是一个。

####4.synchronized(this)
将当前类的对象作为锁，锁定当前对象的synchronized方法与synchronized代码块

####5.将任意对象作为对象监视器
synchronized代码块可以锁定任意对象:synchronized(Object o)

synchronized（非this对象） 代码块，优点:
>在一个类中，有很多的同步方法，虽然能同步，会堵塞 ，但效率低，使用synchronized（非this对象），则synchronized（非this对象）与同步方法是异步执行的，不去与this同步方法挣抢同一把锁。
即让锁的对象不一样，形成两个锁，成异步执行，提高效率

synchronized(非this对象)也可以解决脏读问题

####6.细化验证三个结论
>1.当多个线程同时执行synchronized （x）{}同步代码块时，呈现出同步效果。   

>2.当其他线程同时执行X对象中synchronized 同步方法时，呈现出同步效果。
       
>3.当其他线程同时执行X对象方法里面的synchronized （this）{}同步代码块时，呈现出同步效果。

####7.静态同步synchronized 方法与synchronized （this）代码块
synchronized加在static静态方法上，是给class类对象，上锁，以下两个方法为同一把锁
	synchronized public static void method(){}

	public void method(){
		synchronized(Object.class)
	}

	
synchronized加在（非）static静态方法上，是给对象，上锁，以下两个方法为同一把锁

	synchronized public void method(){}

	public void method(){
		synchronized(this)
	}

给class类对象上锁可以对类的所有对象实例起作用，**虽然不同对象，但静态的同步方法还是同步运行的，因为Class类在内存中是单例的，是一把锁**

####8.数据类型String的常量池特性

synchronized(非this)代码块，都不使用String作为锁的对象，而是改用，new object() 实例化一个object对象，但他并不放入缓存中

####9.同步synchronized 无限等待与解决
同步方法容易造成死循环，使其他线程永远无法得到该锁，造成无限等待的情况。

解决方法：使用同步块并使用2把锁解决。

####10.多线程的死锁
两个线程互相等待对方持有的锁，从而任务无法完成，造成线程假死的情况

程序设计时，要避免双方互相持有锁的情况，容易造成死锁情形，在代码层面要避免使用syn嵌套。

####11.内置类与静态内置类
想把外部类中的代码，再抽象封装，而不产生新的.java文件。

静态内置类可直接创建对象，

非静态内置类，必须要外部类.内置类创建对象。

####12.内部类与同步验证：实验1
内置类中有两个方法，但使用的却是不同的锁。打印的结果也是异步的。
即内置类中的方法所使用的锁是不同的，是异步执行的。

####13.内部类与同步验证：实验2
synchronized(lock)代码块对lock上锁的，其他的线程只能已同步的方式调用lock中的同步方法。

####14.锁对象的改变
synchronized(非this)

将任何类型当成锁时，需要注意，是否同时持有相同锁的对象，是，同步执行;不是，异步执行

相同时间，争抢的是同一把锁，

不同时间，锁对象改变，会造成异步执行，（锁对象改了）

**只要对象不变，即使是对象的属性改变，运行结果还是同步**

##(三)volatile关键字
主要作用：是使变量在多个线程间立即可见

####1.关键字volatile与死循环
用多线程解决死循环

####2.解决同步死循环
关键字Volatile作用：
>强行从公共堆栈中取得变量的值，而不是从线程私有的数据栈中取得变量的值

####3.解决异步死循环
volatile关键字是解决变量在多个线程的可见性

synchronized关键字是解决的多线程访问资源的同步性

####4.volatile非原子特性
volatile虽然增加了可见性，但不具有同步性也不具有原子性。

**不可再分，即为原子性**

####5.使用原子类进行i++操作
i++除了使用原子类synchronized关键字实现同步外，AtomicInteger原子类也可以实现。(CAS机制，轻量级)


####6.原子类也并不完全安全
原子类具有逻辑的情况，输出的结果也具有随机性

####7.synchronized代码块有 volatile同步的功能
synchronized可以使多个线程访问同一个数据具有同步性，还具有跟volatile一样的强行从公共堆栈中取得变量的值的功能。

**synchronized两个特性：同步性，可见性**

##第三章 线程间的通讯
##（一）等待通知机制
####1.不使用等待/通知机制实现 线程通信
使用sleep()结合while死循环的方法实现，多个线程的通信。

>弊端：要不停的通过while循环语句，轮询进行判断，非常浪费CPU资源，
轮询间隔很小，等浪费，间隔大，有可能会取不到想要的数据。
wait/notify机制 就是解决这个问题的

####2.什么是等待/通知机制
wait的作用是使当前执行代码的线程停止运行。

notify的作用是使停止的线程继续运行

####3.方法wait()锁释放 与 notify()锁不释放
当方法wait()被执行后，锁自动释放。

当notify()被执行后，只有在synchronized代码块或synchronized方法执行完后，锁才释放，否则不释放

####4.当interrupt 方法 遇到wait方法
当线程呈现出wait()状态时，调用线程对象interrupt()方法会抛异常。InterruptedException

####5.只通知一个线程
notify方法只唤醒同锁的一个线程，多次调用后，**唤醒的顺序，与线程执行wait的顺序相同，先wait的先启动（JVM实现）**

####6.唤醒所有线程
notifyAll()方法，一次能唤醒所有同锁线程，**唤醒顺序与wait调用顺序想法，先wait的后唤醒**

####7.方法wait（long）的使用
设置时间线程自动唤醒

ext：wait（5000）5秒后自动唤醒。

####8.通知过早
通知过早会打乱程序正常的运行逻辑。

####9.等待wait的条件发生变化
等待wait的条件发生变化了，也会打乱程序正常的运行逻辑。将if改成while，每次都要重复的判断wait()条件。

####10.生产者/消费者模式实现
1.一对一
>Consumer/Producer 
>、消费方法 synchronized代码块内，唤醒条件使用if，因为只有一个生产者或消费者，阻塞条件不会被其他线程改变，不需要循环判断阻塞条件

2.一对多（一生产多消费）
>Consumer 消费方法需要while判断阻塞条件，因为有多个生产者或消费者，阻塞条件有可能被其他线程改变，需要循环判断阻塞条件，并且为了避免假死（同类唤醒同类），使用notifyAll代替notify

3.多对多
>生产者/消费者 业务方法阻塞条件都要使用while循环判断，避免同类线程更改阻塞条件

4.多对多连续生产消费
>Service需要单独增加Check方法，并单独启动两个check线程，循环检测是否满足线程唤醒条件（如生产者生产的消息数量达到阈值，激活消费者线程开始消费），如果满足立即将锁对象.notifyAll
####11.通过管道进行线程间的通信： 字节流
PipedInputStream PipedOutputStream
>使用完毕之后，记得关闭流，使用connect方法连接输入与输出流的对象
####12.通过管道进行线程间的通信： 字符流
PipedWriter PipedReader
>使用完毕之后，记得关闭流，使用connect方法连接输入与输出流的对象
####13.实战：等待/通知之交叉备份
使用volatile修饰flag，在线程间显示上一次执行备份的线程是哪个，本次启动另一个

##（二）join方法的使用
A线程启动并创建B线程，如果B线程执行时间较长，A线程往往在B线程之前结束，如果A线程需要B线程的执行结果，必须等到B线程执行完，A才能继续执行，这时需要使用join方法

####1.学习方法join的铺垫
如果使用A线程进行固定时间的sleep，有时可以解决等待B线程结果的问题，但是无法确定B线程的执行时间，所以使用sleep不合适，况且sleep不释放锁，如果为同步方法，其他线程无法得到锁，无法执行

####2.用join方法来解决
如果在main线程内调用obj.join()，会使main线程阻塞，obj执行完run方法的代码后，main才会执行

join与synchronized的区别
>join内部使用wait方法进行等待，synchronized使用锁进行同步

####3.join方法与异常
方法join与interrupt方法如果彼此遇到，则出现异常，不管先后顺序。进程按钮还呈“红色”，原因是有线程还在运行，进程并未停止

####4.方法join（long）的使用
设定参数是等待的时间，不管x线程是否执行完毕，时间到了并且重新获得了锁，则当前线程会继续向后运行。如果没有重新获得锁，则一直在尝试，直到获得锁位置

**注意：join（long x）到时间之后，当前线程必须要抢到锁才能够执行，因为wait（long x）语句下还有语句**

####5.方法join（long）与sleep（long）的区别
join内部使用wait方法进行等待，所以join方法会释放锁
sleep(long)不释放锁

**注意：join执行完wait方法后，锁被释放，main线程需要抢到锁，才能继续执行，否则不执行**
####6.方法join()后面的代码提前运行
以下代码共三个线程：main MyThreadA MyThreadB,  两个锁:MyThreadB对象锁， PrintStream锁
且MyThreadB run方法为同步方法，如运行先要得到MyThreadB对象锁

main打印在中间：
>MyThreadA在释放ThreadB对象锁后，main线程与MyThreadB线程争抢ThreadB对象锁，main先争抢到，join中wait方法执行完，打印main end 释放锁，MyThreadB线程得到锁后执行

main打印在倒数第二：
>MyThreadB线程与main线程争抢ThreadB对象锁时，main抢到，先执行完 join（200。 main线程释放了ThreadB对象锁， MyThreadB线程得到MyThreadB对象锁后执行，打印Thread B beign, MyThreadB在执行sleep（500）时，main 可以继续执行因为此时join已经执行完，main线程拿到打印锁，打印出main end ，释放打印锁后，MyThreadB线程拿到打印锁，打印 Thread B end

main打印在最后：
>main线程在执行join时，抢锁就都没抢过MyThreadB线程，导致main end最后打印

	public class Run {
		/**
		 * 三个线程：main MyThreadA MyThreadB,  两个锁:MyThreadB对象锁， PrintStream锁，
		 * System.out.println方法为同步方法，锁为PrintStream对象
		 * @param args
		 */
		public static void main(String[] args) {
			MyThreadB tb = new MyThreadB();
			MyThreadA ta = new MyThreadA(tb);
			tb.setName("B");
			ta.setName("A");
			ta.start();
			tb.start();
			try {
				tb.join(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("     main end " + System.currentTimeMillis());
		}
	}
	
	public class MyThreadA extends Thread {
		private MyThreadB tb;
	
		public MyThreadA(MyThreadB tb) {
			super();
			this.tb = tb;
		}
	
		@Override
		public void run() {
			synchronized (tb) {
				System.out.println(Thread.currentThread().getName()
						+ "抢到了锁ThreadB，开始执行");
				System.out.println("begin A ThreadName="
						+ Thread.currentThread().getName() + " "
						+ System.currentTimeMillis());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("  end A ThreadName="
						+ Thread.currentThread().getName() + " "
						+ System.currentTimeMillis());
				System.out
						.println(Thread.currentThread().getName() + "释放了锁ThreadB");
			}
		}
	}

		public class MyThreadB extends Thread {
		
		@Override
		synchronized public void run() {
			System.out.println(Thread.currentThread().getName()
					+ "抢到了锁ThreadB，开始执行");
			System.out.println("begin B ThreadName="
					+ Thread.currentThread().getName() + " "
					+ System.currentTimeMillis());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("  end B ThreadName="
					+ Thread.currentThread().getName() + " "
					+ System.currentTimeMillis());
			System.out.println(Thread.currentThread().getName() + "释放了锁ThreadB");
		}
	}

##（三）ThreadLocal的使用
将数据放入当前线程对象中的Map里，这个Map为每个线程特有，线程各自使用各自的Map，Map key为ThreadLocal对象，value为存储的值

线程销毁，Map也销毁 Map中的数据如果没有引用，被GC回收
####1.方法get()与null
如果从未在Thread中的ThreadLocalMap中存储值，则get返回null

####2.类ThreadLocal存取数据流程分析
ThreadLocal的本质为Thread对象内的Entry数组

测试类
	
	public class Test {
		/**
		 * ThreadLocal存取数据过程分析
		 */
		public static void main(String[] args) {
			ThreadLocal t = new ThreadLocal();
			t.set("data");
			System.out.println(t.get());
		}
	}

（1）t.set()源代码过程：
	
	public void set(T value) {
        Thread t = Thread.currentThread();	//获取当前运行的线程， 即main线程
        ThreadLocalMap map = getMap(t);		//方法返回的是main线程对象内的threadLocals属性，本属性所属类为ThreadLocal.ThreadLocalMap
        if (map != null)					//判断取得的map是否为空
            map.set(this, value);			//map非空，在map内设置值，key为当前ThreadLocal对象，value为要设置的值
        else
            createMap(t, value);			//map为空则新创建map并添加键值
    }

（2）ThreadLocalMap map = getMap(t);源代码
	
	ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;				//方法返回的是main线程对象内的threadLocals属性，本属性所属类为ThreadLocal.ThreadLocalMap
    }

（3）t.threadLocals;源代码

	public Class Thread implements Runnable{
		ThreadLocal.ThreadLocalMap threadLocals = null;	//返回的是当前线程对象的实例变量
	}

（4）createMap(t, value);源代码

>取得Thread中的ThreadLocal.ThreadLocalMap后，第一次向其存放数据时会调用createMap()方法来创建ThreadLocal.ThreadLocalMap对象，因为对象现在是null

	void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, firstValue);	//新创建ThreadLocalMap并传值
    }

（5）new ThreadLocalMap(this, firstValue);源代码
	
	ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
        table = new Entry[INITIAL_CAPACITY];				//新建Entry数租
        int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);	//计算要赋值的下标值
        table[i] = new Entry(firstKey, firstValue);		//将要赋予的键值对赋给Entry数组
        size = 1;
        setThreshold(INITIAL_CAPACITY);
    }

**经过上面的5个步骤，将vlaue通过ThreadLocal放入当前线程currentThread()中的ThreadLocalMap对象内**

（6）t.get();源码
	
	public T get() {
        Thread t = Thread.currentThread();		//获取当前线程对象
        ThreadLocalMap map = getMap(t);			//按照当前线程对象获取当前线程的map对象
        if (map != null) {						//判断map是否为空
            ThreadLocalMap.Entry e = map.getEntry(this);		//map非空则按照当前TheadLocal对象获取，获取map内的Entry对象
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;		//返回Entry对象内的值
                return result;
            }
        }
        return setInitialValue();		//map为空则初始化map， 返回null
    }

**ThreadLocal默认是包级访问 protected，所以不能通过外部直接获取Thread对象内的ThreadLocalMap对象**

##（四）类InheritableThreadLocal的使用
可以使子线程中取得父线程继承下来的值

####1.值继承
子线程即在父线程中启动的线程，例如在main线程启动threadA线程，main线程即为threadA线程的父线程

**注意：如果父线程对InheritableThreadLocal对象赋值在子线程创建对象之后，则子线程不能继承父线程放入InheritableThreadLocal对象的值**

####2.值继承过程分析

（1）InheritableThreadLocal源码

	public class InheritableThreadLocal<T> extends ThreadLocal<T> {
	    protected T childValue(T parentValue) {
	        return parentValue;
	    }
	    ThreadLocalMap getMap(Thread t) {
	       return t.inheritableThreadLocals;
	    }
	    void createMap(Thread t, T firstValue) {
	        t.inheritableThreadLocals = new ThreadLocalMap(this, firstValue);
	    }
	}

类InheritableThreadLocal中存在三个方法，其中getMap(Thread t)方法与createMap(Thread t, T firstValue)方法是**对父类ThreadLocal中的同名方法进行重写**

（2）Tools.itl.set("main线程放入的值");源代码
	
	public class Run {
		public static void main(String[] args) {
			ThreadA ta = new ThreadA();
			Tools.itl.set("main线程放入的值");
		}
	}

InheritableThreadLocal对象中的set()方法其实调用的是ThreadLocal类中的set()，因为没有重写
	
	public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }

以上源码的两个方法，getMap(t)与createMap(t, value)两个方法是InheritableThreadLocal类中重写的方法

调用重写方法之后，不在向Thread类中的ThreadLocal.ThreadLocalMap threadlocals属性存入数据，而是向ThreadLocal.ThreadLocalMap inheritableThreadLocals存入数据，代码见上面(1)

（3）子线程继承值源码

	private void init(ThreadGroup g, Runnable target, String name,
                      long stackSize, AccessControlContext acc,
                      boolean inheritThreadLocals) {
	
	 if (inheritThreadLocals && parent.inheritableThreadLocals != null)
            this.inheritableThreadLocals =
                ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
	}

上面方法在Thread构造器内，说明创建Thread对象时，init方法就会被调用，将父线程inheritableThreadLocals传入本线程

（4）createInheritedMap(parent.inheritableThreadLocals);源码
	
	static ThreadLocalMap createInheritedMap(ThreadLocalMap parentMap) {
        return new ThreadLocalMap(parentMap);
    }
	
	private ThreadLocalMap(ThreadLocalMap parentMap) {
        Entry[] parentTable = parentMap.table;
        int len = parentTable.length;
        setThreshold(len);
        table = new Entry[len];

        for (int j = 0; j < len; j++) {
            Entry e = parentTable[j];
            if (e != null) {
                @SuppressWarnings("unchecked")
                ThreadLocal<Object> key = (ThreadLocal<Object>) e.get();
                if (key != null) {
                    Object value = key.childValue(e.value);
                    Entry c = new Entry(key, value);		//本处即将父线程传入inheritableThreadLocals对象的值传入子线程，子线程新建Entry数组
                    int h = key.threadLocalHashCode & (len - 1);
                    while (table[h] != null)
                        h = nextIndex(h, len);
                    table[h] = c;
                    size++;
                }
            }
        }
    }

子线程对象创建完毕后，子线程inheritableThreadLocal对象中的数据即为父线程的数据，如果之后父线程数据改变，子线程数据不变，因为子线程与父线程使用的是两个Entry数组，各自存储各自的值

继承InheritableThreadLocal后，可以通过重写ChildValue()方法与initialValue()方法来初始化线程放入inheritableThreadLocals属性的值

##第四章 Lock的使用

##（一）ReentrantLock
功能与ynchronized关键字一样，为了实现同步效果，但是RenntrantLock功能更加强大，具有嗅探锁定，多路分支通知等功能，内部原理为异步队列
####1.使用ReentrantLock实现同步
使用ReentrantLock对象的lock()方法获得锁，调用unlock()方法释放锁，这两个方法成对进行使用。

想要实现同步某些代码，就把这些代码放在lock()与unlock()之间
####2.使用ReentrantLock Condition实现等待通知与部分通知
condition对象调用await()方法前，必须执行 lock.lock()使当前线程获得锁

>Object类中的wait()方法相当于Condition类中的await()方法

>Object类中的wait(long time)方法相当于Condition类中的await(long time, TimeUtil util)方法

>Object类中的notify()方法相当于Condition类中的signal()方法

>Object类中的notifyAll()方法相当于Condition类中的signalAll()方法

####3.生产消费模式
利用Condition对象实现生产者只唤醒消费者，消费者只唤醒生产者，及时使用signal也不会出现假死状态

####4.公平锁与非公平锁
锁Lock分为“公平锁”与“非公平锁”

公平锁表示线程获取锁的顺序是按照线程加锁的顺序来分配的，队列实现

非公平锁是一种获取锁的抢占机制，是随机获得锁的，先await的不一定先得到锁

####5.lock unlock方法实现原理（unpark park方法调试过程 多线程debug使用）

使用CAS（compare and swap）机制与异步队列实现
	
	public void testMethod() {
		try {
			lock.lock();
			Thread.sleep(15000);
			lock.unlock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

（1） lock() 源代码

	public class ReentrantLock implements Lock, java.io.Serializable {
		public void lock() {
	        sync.lock();
	    }
	}

（2）sync.lock()源代码
	
>1.非公平锁

	static final class NonfairSync extends Sync {
		final void lock() {
		    if (compareAndSetState(0, 1))		//尝试获取内存地址，即获取锁
		        setExclusiveOwnerThread(Thread.currentThread());	//如成功，将当前线程对象赋给exclusiveOwnerThread属性
		    else
		        acquire(1);		//如未获得锁，尝试获得
		}
	}

>2.公平锁

	static final class FairSync extends Sync {
		final void lock() {
	        acquire(1);
	    }
	}

JVM调用unsafe对象，对内存进行地址比较，如果获取地址成功，即得到锁，返回true,否则false
	protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

	public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }

tryAcquire(arg)多次尝试获得锁，如未获得运行acquireQueued方法，尝试进入队列

	final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {				//	死循环，轮循请求队列
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())		//调用unsafe对象，阻塞当前线程
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
	
	private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

运行完park()方法，当前线程即阻塞，如另一线程运行LockSupport.unpark(s.thread)后，当前线程才可以继续运行

####6 unsafe类
sun.misc.Unsafe UNSAFE类为不可直接实例化的，需要经过反射构建实例
	
	public class UnsafeDemo {
		public static void main(String[] args) throws NoSuchFieldException,
				SecurityException, IllegalArgumentException,
				IllegalAccessException, InstantiationException {
			Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
			unsafe.park(false, 0L);
			System.out.println("kepler");
		}
	}

反射，getDeclaredField()
>对象反映此 Class 对象所表示的类或接口的指定已声明字段，可以获取一个类的所有字段 字符串“theUnsafe”为字段简称

f.setAccessible(true)
>设置field对象可见

unsafe.park(true, System.currentTimeMillis() + 3000);
>运行后，程序阻塞在此3秒后继续运行

unsafe.park(false, 3000000000L);
>运行后，程序阻塞在此3秒后继续运行，输入true，程序阻塞直到后面的数字则为1970-1-1 00：00：00到当前的毫秒数继续运行，计算机绝对时间；输入false，数字则表示延时多少纳秒后，继续运行



**公平锁与非公平锁区别：公平锁在lock时，会尝试运行acquire获取内存地址，方法内有尝试获取队列的操作；而非公平锁lock时，直接运行unsafe对象，CAS机制对内存进行取地址操作，如果未成功，才运行acquire方法**

####6.ReentrantLock方法总结

1.getholdCount()
>查询**当前线程**保持此锁的个数即调用lock（）方法的次数，调用一次加1，unlock一次减1

2.getQueueLength()
>返回正等待此锁的线程估计数，有多少个线程在队列里正在等待此锁

3.getWaitQueueLength()
>返回等待与此锁定相关的给定条件Condition的线程数

4.hasQueuedThread() hasQueueThreads() hasWaiters()

>hasQueuedThread(Thread thread) 查询这个线程是否正在等待获取此锁

>hasQueueThreads() 查询是否有线程正在等待获取此锁

>hasWaiters() 是否有线程正在等待与此锁有关的condition条件

5.isFair() isHeldByCurrentTHread() isLocked()

>isFair() 此锁是否为公平锁

>isHeldByCurrentTHread() 返回当前线程是否保持此锁

>isLocked() 返回此锁是否由任意线程保持

6.lockInterruptibly() tryLock() tryLock(long time)
>lockInterruptibly() 当某个线程尝试获得锁并且阻塞在lockInterruptibly方法时，该线程可以被中断，用此方法加锁后，线程被打断则报异常

>tryLock() 尝试获取锁，成功获得锁返回ture否则返回false，如果当前锁被其他线程持有了，返回false，**非阻塞方法**

>tryLock(long time) 在制定时间内尝试获得锁，过时则继续执行，**非阻塞方法**，如果当前锁被其他线程持有了，返回false

7.Condition类  await()  await(long time)  awaitUninterruptibly()  awaitUnit(Date date)  awaitNanos(long time)

>await()释放锁，**阻塞方法**，效果等同于Object.wait()

>await(long time)在设定时间内释放所并阻塞

>awaitUnit(Time time)在规定时间后唤醒，可以被其他线程提前唤醒

>awaitUninterruptibly() 线程在等待的过程中，不允许被中断

####7.利用Condition实现线程顺序执行

##（二）ReentrantReadWriteLock类
读写互斥，写读互斥，写写互斥，读读异步

##第五章 定时器Timer
JDK库中，Timer类主要负责计划任务的功能，也就是在指定的时间开始执行某一任务

封装任务的类是TimerTask类

####1.方法schedule(TimerTask task, Date time)
该方法的作用是在指定的日期执行一次某一任务

执行任务的时间晚于当前时间，在未来执行，如果执行任务的时间早于当前时间，则立即执行任务

**任务执行完成之后，进程未销毁说明内部还有非守护线程正在执行**

线程TimerThread不销毁的原因
>在创建Timer对象时，启动了1个新的非守护线程，此线程内部有一个死循环，TimerThread类中的mainLoop()方法

>只有在执行了TimerThread类中的cancel方法时，newTasksMayBeScheduled布尔量为false，死循环退出

####2.Timer中执行多个TimerTask任务
TimerTask以队列的方式一个一个执行，如果前面的任务耗时较长，则后面的任务也会被延后

1个TimerThread对象，运行为单线程，操作1个queue，只能一个一个执行

####3.schedule(TimeTask task, Date firstTime, long period)
在指定的日期后按指定的间隔周期无限循环的执行某一任务

如果计划晚于当前时间，在未来执行，如果早于当前时间，就立即执行并循环

aaaa.TimerTask cancel()方法与Timer cancel()方法
TimerTask cancel()
>将自身从任务队列中清除，其他的任务不受影响

Timer cancel()
>将任务队列中的全部任务清除，包括已经在执行的任务，进程销毁，console按钮变灰

**Timer cancel()方法有时不一定会停止计划任务，原因是因为Timer类中的cancel()方法有时并没有争抢到queue锁，让TimerTask类中的任务正常执行**

	public void cancel() {
        synchronized(queue) {
            thread.newTasksMayBeScheduled = false;
            queue.clear();
            queue.notify();  // In case queue was already empty.
        }
    }

####5.间隔执行Task任务算法

当队列中有三个任务A,B,C时，这三个任务执行顺序的算法是每次将最后一个任务放在队列头，再执行队列头的Task的Run方法

>ABC CAB BCA

####6.Timer类 schedule（TimerTask task， long delay）
以当前时间为参考，延时delay毫秒后执行一次任务，以delay为循环周期

####7.Timer类 schedule（TimerTask task，long delay, long period）
以当前时间为参考，延时指定的delay毫秒数，以period循环执行任务

凡是Timer使用方法中，**带有period参数的**，都是无限循环执行TimerTask中的任务

####8.Timer类 scheduleAtFixedRate(TimerTask task, Date firstTime, long period)
任务执行的追赶性，如果计划执行时间已经晚于当前时间，那么之前需要执行的任务，被补充性的执行

##第六章 单例模式与多线程
在多线程环境下，需要保证单例模式的安全性

##(一) 立即加载与延时加载
立即加载（饿汉式）
>使用类的时候已经将对象创建完毕，立即加载在多线程条件下不能有其他实例变量，因为getInstance()方法没有同步，有可能出现非线程安全问题
	
	public class MyObject{
		private static MyObject myObject = new MyObject();
		public MyObject() {
			super();
		}
		public static MyObject getInstance(){
			return myObject;
		}
	}

延时加载（懒汉式）
>使用类的时候对象未创建，当对象被使用时，才创建对象，需要在实例化代码处添加同步锁，并采用两次判断机制，才能保证线程安全,并且兼顾效率

	public class MyObject{
		private static MyObject myObject;
		public MyObject() {
			super();
		}
		public static MyObject getInstance() throws InterruptedException{
			if(myObject == null){
				Thread.sleep(3000);			//模拟执行业务
				myObject = new MyObject();
			}
			return myObject;
		}
}

##(二)静态内置类，静态块实现单例
立即加载，利用static在JVM加载class的特性，就运行的特性，语法糖

##(三)序列化，反序列化与单例
如果将单例的对象进行序列化，使用默认的反序列化行为取出的对象是多例的

实现Serializable接口后，写入protected Object readResolve()方法的作用是反序列化时不创建新的Object，二十复用原有的Object

##(四)枚举与单例
枚举enum与静态代码块类似，在使用枚举时，构造方法会自动调用，也可以利用这个特性设计单例模式
	
	public class MyObject{
		public enum MyEnum{
			INSTANCE;
			private Object object;
			
			private MyEnum(){
				object = new Object();
			}
			
			public Object getObject(){
				return object;
			}
		}
		
		public static Object getMyObject(){
			return MyEnum.INSTANCE.getObject();
		}
	}

枚举的构造方法只能在使用枚举的时候运行一次，所以可以保证为单例，可以防止反射攻击恶意构建多个对象

防止反射攻击
>1.使用枚举类型

>2.在单例类中，当在构建第二个对象时，就抛出异常

##第七章 多线程补充

##(一)线程的状态
线程对象在不同的运行时期存在不同的形态，状态信息存在于State枚举类中

>NEW：至今尚未启动

>RUNNABLE：正在JVM中执行

>BLOCKED：受阻塞并等待某个锁

>WAITING：无限期地等待另一个线程来执行某一特定操作

>TIMED_WAITING：等待另一个线程来执行取决于指定时间的操作

>TERMINATE：已经退出

##(二)线程组
为了对某些具有相同功能的线程进行方便的管理，可以把线程归属到某一个线程组中，线程组中可以有线程对象，也可以是线程组，组中也有线程或线程组，形成树状结构

####1.线程对象关联线程组，一级关联
在用Thread类构建线程对象时，将线层组与另一个线程对象关联起来，形成一级关联

####2.线程对象关联线程组，多级关联
父对象中有子对象，子对象中再创建子对象，也就是出现子孙对象的效果
	
	public ThreadGroup(ThreadGroup parent, String name)

创建一个组时可以指定一个父组

线程必须启动然后才归属到相应组内，因为在调用start()方法时会调用group.add(this)

####3.线程组自动归属特性

如果实例化一个ThreadGroup线程组X时，如果不指定所属的线程组，则X线程组自动归到当前线程对象所属的线程组中

JVM根线程组为 System

线程组可以批量停止线程，但需要启动线程则需要每个线程对象start()

####4.递归获取线程对象与线程组

	Thread.currentThread().getThreadGroup().enumerate(listGroup1, true);

enumerate方法传入true,为递归获取线程组与线程对象，复制到listGroup1线程组数组中，如果存在子组的子组，则将其放入数组中，传入false则无此效果

####5.使线程具有顺序性
利用ThreadLocal类，每个线程取得各自的打印数量，分别判断当前位置

####6.SimpleDateFormat非线程安全
SimpleDateFormat在多线程环境下，非线程安全

解决办法：
>1.使用多个SimpelDateFormat对象，每个对象处理一个日期

>2.使用ThreadLocal,绑定SimpleDateFormat对象到每个线程

	public class Tool {
		private static ThreadLocal<SimpleDateFormat> t1 = new ThreadLocal<SimpleDateFormat>();
	
		public static SimpleDateFormat getSimpleDateFormat() {
			SimpleDateFormat sdf = t1.get();
			if (sdf == null) {
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				t1.set(sdf);
			}
			return sdf;
		}
	}

####7.线程出现异常 UncaughtExceptionHandler 
对线程对象设定异常处理器
	
	public class Run {
		public static void main(String[] args) {
			ThreadA ta = new ThreadA();
			ta.setUncaughtExceptionHandler(new UncaughtExceptionHandler(){
				@Override
				public void uncaughtException(Thread t, Throwable e) {
					System.out.println("线程：" + t.getName() + " 出现了异常");
					e.printStackTrace();
				}
			});
			ta.start();
		}
	}

如果线程对象ta运行时报错，uncaughtException方法会捕捉到，并自定义处理逻辑

setDefaultUncaughtExceptionHandler方法是对所有线程对象设定异常处理
####8.线程组内处理异常 uncaughtException
线程组内的一个线程报错，不会影响组内其他线程运行

如果实现组内一个线程报异常，组内所有线程停止的效果，自定义线程组类，继承ThreadGroup类，在类内重写uncaughtException方法，在内使用this对象interrpted方法，打断组内所有线程

**注意，如果使用上述方法终止线程组内线程，需要每个线程对象的run方法内不要有catch块，否则不走uncaughtException方法**

####9.线程异常处理的优先
线程对象处理异常优先，如果发生多处异常，则抛出线程对象内发生的异常，其他的异常不抛出

如果只存在静态的异常setDefaultUncaughtExceptionHandler, 与线程组内定义的异常处理，则两个都抛出，只要发生异常
