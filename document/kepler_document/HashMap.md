#HashMap
存储Key-value键值对的集合，每一个键值对也叫做Entry，Entry分散在一个数组当中，这个数组就是HashMap的主干，
数组每一个元素的初始值为Null

HashMap最常使用的两个方法，Get,Put

##Put方法原理
调用Put方法时，比如hashMap.put("apple", 0),需要利用一个哈希函数确定Entry的插入位置（index），假设最后计算出的index是2，结果如下

	0		1		2		3		4		5		6		7		8		9
	null	null	Entry1	null	null	null	null	null	null	null

当index冲突时，利用链表，先插入index的元素在后，每一个Entry通过指针指向下一个同index的Entry，Entry1为先插入，放在后面，因为HashMap源代码默认为后插入的Entry被查找的可能性更大
	
	0		1		2		3		4		5		6		7		8		9
	null	null	Entry1	null	null	null	null	null	null	null
					Entry2

##Get方法原理
hashMap.get("apple"),首先对apple进行Hash运算，得到index等于2，查到数组下标2的链表，一个一个向下查找，知道找到key为"apple"时结束，返回对应的value。

##HashMap默认长度
HashMap初始默认长度为16，每次自动或手动扩展或初始化时，长度必须为2的次幂

为了实现一个尽量均匀分布的Hash函数，需要利用Key的HashCode值来做某种运算
>index = HashCode(Key) & (Length - 1)

以“book”为Key演示过程：

1.book的HashCode为3029737，二进制：10 1110 0011 1010 1110 1001

2.HashMap默认长度为16，length-1二进制为1111，

3.两个二进制结果做与运算，结果为1001， 十进制9，index=9

4，如果HashMap长度为10，legnth-1二进制为1001，如果HashCode换成10 1110 0011 1010 1110 1011
与1001做与运算，结果为1001，两个HashCode对应了一个index，导致有些index结果的出现几率更大，有的会不出现，不符合Hash的均匀分布原则

5，如果hashMap长度为2的次幂，length-1的值是所有二进制位全为1，index的结果等同于HashCode后几位的值，，只要输入的HashCode分布均匀，Hash算法的结果就是均匀的

##高并发下的HashMap
HashCode容量有限，多次插入元素后，Key映射位置发生冲突的几率会逐渐提高，，HashMap需要扩展长度，就是进行Resize

影响发生Resize的因素：
>1. Capacity:HashMap的当前长度
>2. LoadFactor:HashMap的负载因子,默认0,75f

衡量HashMap是否进行Resize的条件： HashMap.size >= Capacity * LoadFactor

####HashMap扩容过程

1.创建一个新的Entry空数组，长度是原数组的2倍

2.ReHash，遍历原Entry数组，把所有的Entry重新进行hash到新数组，Hash算法：index = HashCode（key） & （Length-1）

###HashMap key可以为空 null
	
	@Test
	public void testMethod(){
		Map map = new HashMap();
		map.put(null, "二十");
		map.put("老王", "三十");
		System.out.println(map);
	}

####多线程HashMap问题

1.假设一个HashMap已经到了Resize的临界点，此时有两个线程A和B，在同一时刻对HashMap进行Put

					线程A					线程B
					Entry4					Entry5
					|						|
		0			1			2			3
		null		null		Entry3		null
								Entry2
								Entry1


		
		0			1			2			3
		null		Entry4		Entry3		Entry5
								Entry2
								Entry1

2.此时HashMap已经达到Resize条件，两个线程各自进行扩容，但是在操作原数组链表时，在线程内可能会形成链表环，在调用HashMap.get时，会进入死循环，如下所示：

（1）线程A执行完毕，线程B执行到指针e指向Entry3，next指向Entry2
	
	线程A:
	
	0			1			2			3			4			5			6			7	
	null		Entry4		null		Entry2		Entry1		null		Entry5		null	
										Entry3
	e:Entry3	e.next:Entry2

	线程B:
	
	0			1			2			3			4			5			6			7	
	null		null		null		null		null		null		null		null

（2）线程B插入e指向的Entry3到index为3的位置，e变为指向原e.next指向的对象Entry2，e.next指向不变
	
	线程A:
	
	0			1			2			3			4			5			6			7	
	null		Entry4		null		Entry2		Entry1		null		Entry5		null	
										Entry3
	e:Entry2	e.next:Entry2

	线程B:
	
	0			1			2			3			4			5			6			7	
	null		null		null		Entry3		null		null		null		null

（3）线程B,e.next指针指向Entry3，e指向Entry2
	
	线程A:
	
	0			1			2			3			4			5			6			7	
	null		Entry4		null		Entry2		Entry1		null		Entry5		null	
										Entry3
	e:Entry2	e.next:Entry3

	线程B:
	
	0			1			2			3			4			5			6			7	
	null		null		null		Entry3		null		null		null		null

（4）线程B，由于是头插法，Entry2在插入index为3的位置时在Entry3之前，e重新指向原e.next指向的对象Entry3
	
	线程A:
	
	0			1			2			3			4			5			6			7	
	null		Entry4		null		Entry2		Entry1		null		Entry5		null	
										Entry3
	e:Entry3	e.next:Entry3

	线程B:
	
	0			1			2			3			4			5			6			7	
	null		null		null		Entry2		null		null		null		null
										Entry3

在进行e.next变换指向时，e指向了Entry3,而此时index为3的对象首先查找的是Entry2,Entry2被e.next指向，形成了Entry3.next = Entry2,**但是在线程B中，形成的index3链表为Entry2.next=Entry3,Entry2与Entry3形成了一个有环链表**

##concurrentHashMap

高并发下，想要避免HashMap的线程安全问题有很多办法，比如改用HashTable或者Collections.synchronizedMap，
但是性能较低，无论是读写，他们都会对整个集合加锁，导致同一时间的其他操作为之阻塞

concurrentHashMap兼顾了线程安全和执行效率

####Segment
Segment本身就相当于一个HashMap对象，包含一个HashEntry数组，这个concurrentHashMap结果如下：
	
	Segment1			null			segment2			null
	
	0	1	2	3						0	1	2	3
	N	E1	N	N						N	E1	N	N
		E2									E2
concurrentHashMap是一个二级Hash表在一个总的Hash表下有若干个子Hash表，类似于数据库水平拆分，在线程安全前提下保证性能

concurrentHashMap采用锁分段，每一个segment被锁起来之后，不影响其他的segment，只有同一个segment的并发写入会产生阻塞

concurrentHashMap在读写时都需要进行二次定位，首先定位到segment，之后定位到数组下标

concurrentHashMap.size(),方法执行时，会计算方法执行前每个segment被修改次数之和，与方法执行后每个segment被修改次数之和，如果两者不同，重新计算，如果重新计算次数超过某一个值，所有segment加锁进行计算

concurrentHashMap在对Key求Hash值时，为了segment的均匀分布，进行了两次Hash算法
	


