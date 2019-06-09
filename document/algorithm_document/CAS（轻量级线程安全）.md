#CAS（Compare And Swap）比较并替换

CAS机制当中使用了3个基本操作数：内存地址V，旧的预期值A，要修改的新值B

更新一个变量的时候，只有当变量的预期值A和内存地址V当中的实际值相同时，才会将内存地址V对应的值修改为B

##CAS举例
1 在内存地址V当中，存储着值为10的变量

	index：			V				
	value:	NA		10		NA		NA

2 此时线程1想要把变量的值增加1，对于线程1来说，旧的预期值A=10，要修改的新值B=11
	
	index：			V				
	value:	NA		10		NA		NA

	线程1：A=10 B=11

3 在线程1要提交更新之前，另一个线程2抢先一步，把内存地址V的变量值率先更新了11
	
	index：			V				
	value:	NA		11		NA		NA

	线程1：A=10 B=11
	线程2：把地址V的变量值更新为11

4 线程1开始提交更新，首先进行A和地址V的实际值比较，发现A不等于V的实际值，提交失败

	index：			V				
	value:	NA		11		NA		NA

	线程1：A=10 B=11（A！= V的值，提交失败）
	线程2：把地址V的变量值更新为11

5 线程1重新获得内存地址V当前的值，重新计算想要修改的新值，此时对于线程1来说，A=11，B=12，这个重新尝试的过程称为自旋

	index：			V				
	value:	NA		11		NA		NA

	线程1：A=11 B=12

6 这一次比较幸运，没有其他线程修改地址为V的值，线程1进行compare，发现A和地址V的实际值相等
	
	index：			V				
	value:	NA		11		NA		NA

	线程1：A=11 B=12 A==V的值

7 线程1将地址V的值替换为B，也就是12
	
	index：			V				
	value:	NA		12		NA		NA

	线程1：A=11 B=12 地址V的值更新为12

##CAS思想
从思想上来说，Synchronized属于**悲观锁**，悲观地认为程序中的并发情况严重，所以严防死守。CAS属于**乐观锁**，乐观的认为程序中的并发情况不那么严重，所以让线程不断去尝试更新

在并发严重的情况下，Synchronized效率要高于CAS

应用CAS机制：Atomic类， Lock类底层实现，Java1.6以上版本Synchronized转变为重量级锁之前，也采用CAS机制

####CAS缺点：

1 CPU开销比较大，在高并发情况下，许多线程反复尝试更新一个变量，缺又一直不成功，会给CPU带来较大压力

2 CAS保证的只是一个变量的原子性操作，不能保证整个代码块的原子性

#####3 ABA问题

上个例子中，线程2将V的值改成11后，在将V的值改成10，之后，如果线程1操作的时候，会判定V的值没有改变过

####ABA问题举例

有一个单向链表实现的栈顶，栈顶为A，A.next=B，然后希望用CAS将栈顶替换为B，线程1希望将栈顶替由A换为B

	A
	B

在线程1执行之前，线程2介入，将A，B出栈，再pushD，C，A，此时栈结构如下，B处于游离状态
	
	A
	C
	D

此时线程1执行，发现栈顶为A，CAS成功，栈顶替换为B，
		
		B
	C
	D

其中栈中只有B，B.next=null，C,D丢失

为了解决ABA问题，各种乐观锁在实现中会加入版本戳 version在对记录或对象标记，避免并发操作带来的问题

##CAS实现
悲观锁

	package test;

	public class CountTest {
	
		public static int count = 0;
	
		public static void main(String[] args) {
			for (int i = 0; i < 2; i++) {
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							e.printStackTrace();
						}
						for (int j = 0; j < 100; j++) {
							synchronized (CountTest.class) {
								count++;
							}
						}
					}
				}
				).start();
			}
			
			try{
				Thread.sleep(1000);
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("count=" + count);
		}
	}

乐观锁
	
	package test;

	import java.util.concurrent.atomic.AtomicInteger;
	
	public class CountTest {
	
		public static AtomicInteger count = new AtomicInteger(0);
	
		public static void main(String[] args) {
			for (int i = 0; i < 2; i++) {
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							e.printStackTrace();
						}
						for (int j = 0; j < 100; j++) {
								count.incrementAndGet();
						}
					}
				}
				).start();
			}
			
			try{
				Thread.sleep(1000);
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("count=" + count);
		}
	}
	




