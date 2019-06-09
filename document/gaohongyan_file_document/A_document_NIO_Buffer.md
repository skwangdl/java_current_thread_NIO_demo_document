#第一章 缓冲区Buffer的使用
数据放到缓冲区中，对数据进行处理

##1.NIO概述
jdk1.4添加，为non blocking IO， 无阻塞IO即缓存，提高IO输入输出的吞吐量

####1.缓冲区Buffer的介绍
传统IO中，InputStream与OutputStream常常把字节流中的数据放入byte[]或char[]数组中，但是jdk对数组的操作功能比较少，如果进行复杂的操作需要自己实现，可以用NIO中的缓冲区Buffer来解决

####2.Buffer类
Buffer为抽象类，具有七个直接抽象子类：ByteBuffer, CharBuffer, DoubleBuffer, FloatBuffer, IntBuffer, LongBuffer, ShortBuffer

类Java.lang.StringBuffer是在lang包下的，nio内没有StringBuffer与BooleanBuffer

####3.Buffer类的API使用
7个抽象的子类不能直接实例化，需要借助于wrap()静态工厂方法进行实例化，如果抽象类为ByteBuffer,返回的对象为HeapByteBuffer类的对象，HeapByteBuffer修饰符为protected,不能直接使用

1.静态方法wrap()和方法int capacity()的使用

>在NIO内，存在4个核心的属性：（1）capacity容量 （2） limit限制 （3） position位置 （4） mark标记

>4个属性的大小关系：  0 <= mark <= position <= limit <= capacity

>capacity,容量，代表缓存内包含元素的数量，不能为负数，不能更改，方法int capacity()返回缓存区的容量

>HeapByteBuffer为ByteBuffer的子类，缓存数据存在于父类中，使用子类的重写方法操作数据缓存，
capacity容量的值就是buf.length的大小，buf为当初使用wrap()静态工厂方法传入的数组

>方法wrap(byte[] array):将byte数组包装到缓存内，新的缓存将由给定的byte[]数组支持

>方法wrap(byte[] array, int offset, int length):将byte数组包装到缓存内，新的缓存将由给定的byte[]数组支持，新的缓存容量：array.length, position:offset, limit:offset + length

2.方法int limit()和Buffer limit(int newLimit)的使用

>限制：代表第一个不能读取或写入元素的index索引，limit不能为负，不能大于容量capacity

>方法limit返回限制属性大小，Buffer limit(int newLimit)设置Buffer对象的限制值，并返回Buffer对象

>如果操作limit之后的缓存下标，直接抛出IndexOutOfBoundsException异常

3.方法int position()和Buffer position(int newPosition)的使用

>位置：代表下一个要读取或者要写入元素的index索引， position不能为负，并且不能大于limit，如果大于limit则设定当前position的值为新的limit，不报错。

>方法int position()返回当前位置的属性值，Buffer position(int newPosition)设定缓存，位置的属性值

4.方法int remaining()，方法 Buffer mark()

>方法int remaining()返回当前位置position与限制属性limit之间的元素个数，即limit-position得出的数字

>方法Buffer mark()，设定当前position的位置为标记，Buffer对象调用reset()方法后，则会返回这个标记的位置，mark不能为负数，不能大于position，初始化为-1。

>如果未调用mark()方法初始化标记属性，调用reset()抛异常，如果limit或position小于mark的值，则mark被丢弃，变成-1，调用reset抛出InvalidMarkException异常

5.方法allocate(int capacity)()

>分配一个新的缓冲区，传入的参数为容量属性值


6.方法boolean isReadOnly()

>返回此缓冲区是否为只读的，默认为非只读

7.final boolean hasArray()

>返回此缓存对象是否有可访问的底层数组，使用allocateDirect()方法创建的缓存无底层可用数组，因为非直接缓存直接在内核内，JVM堆内没有，即jdk源码内的byte[] hb为空

8.final boolean hasRemaining()方法 final int remaining()方法

>hasRemaining()方法：判断当前位置position与限制limit之间是否有元素

>remaining()方法：返回当前位置position与限制limit之间的元素个数

####4.直接缓冲区与非直接缓冲区

1.非直接缓冲区
	
	public class Run {
		public static void main(String[] args) {
			byte[] byteArray = { 1, 2, 3, 4, 5, 6 };
			ByteBuffer buffer = ByteBuffer.wrap(byteArray);
			ByteBuffer buffer1 = ByteBuffer.allocate(6);
			System.out.println(buffer.isDirect());
			System.out.println(buffer1.isDirect());
		}
	}

使用wrap()方法与allocate(int i)方法创建的是非直接缓冲区，创建非直接缓冲区，一个在JVM堆内，另一个在内核空间内，每次操作都会创建2个缓冲区，会降低吞吐量，并占用较大内存。但是JVM堆内的缓冲区较轻量，适合于对频繁修改的数据进行缓存。

2.直接缓冲区
	
	ByteBuffer buffer = ByteBuffer.allocateDirect(5);
	System.out.println(buffer.isDirect());

使用静态方法allocateDirect(5)创建的就是直接缓冲区，直接在内核空间内创建缓存，效率较高，但是较重，适合于大量但不常修改的数据做缓存

####5.clear() flip() rewind()方法作用与区别
	
1.clean()
	
	public class Run {
		public static void main(String[] args) {
			 byte[] byteArray = { 1, 2, 3, 4, 5, 6 };
			 ByteBuffer buffer = ByteBuffer.wrap(byteArray);
			 buffer.position(2);
			 buffer.limit(4);
			 buffer.mark();
			 System.out.println(buffer);
			 buffer.clear();
			 System.out.println(buffer);
		}
	}

还原缓冲区到初始状态，包含将位置position设置为0，将限制limit设置为容量capacity，并丢弃标记mark，设置一切为默认

主要使用场景为：准备向缓存内写数据，对缓存进行还原，此方法不能真正清除数据，只是通过position设置为0后，在写数据时，新数据将旧数据覆盖

2.flip()

	public class Run {
		public static void main(String[] args) {
			 byte[] byteArray = { 1, 2, 3, 4, 5, 6 };
			 ByteBuffer buffer = ByteBuffer.wrap(byteArray);
			 buffer.position(2);
			 buffer.limit(4);
			 buffer.mark();
			 System.out.println(buffer);
	//		 buffer.clear();
			 buffer.flip();
			 System.out.println(buffer);
		}
	}

反转此缓存，首先将限制limit设置为当前位置position,然后将位置position设置为0，并丢弃标记

flip()使用场景：复用Buffer对象，多次向BUuffer存放数据时，每次都从0处开始取

flip()方法相当于一下代码：

	clarBuffer.limit(charBuffer.position());
	charBuffer.position(0);

3.rewind()
	
	public class Run {
		public static void main(String[] args) {
			byte[] byteArray = { 1, 2, 3, 4, 5, 6 };
			ByteBuffer buffer = ByteBuffer.wrap(byteArray);
			buffer.position(2);
			buffer.limit(4);
			System.out.println(buffer);
			buffer.rewind();
			System.out.println(buffer);
		}
	}

重绕此缓冲区，将位置position置为0，丢弃标记，limit不变

rewind()方法适用场景：重新读取缓存内的数据

4.final int arrayOffset()

返回此缓存的底层实现数组中的第一个缓存元素的偏移量，与父子缓存有关

父缓存下标
	
	0	1	2	3	4	5	6	7	8

子缓存下标

			2	3	4
子缓存对象调用arrayOffset()方法，返回2，子缓存复用了父缓存的一部分元素，不新创建内存

####6.ByteBuffer其他的API

ByteBuffer提供了6类操作

>1.以绝对和相对位置读写单个byte字节的get和put方法

>2.使用相对批量get(byte[] dst)方法，可以将缓冲区中的连续字节传输到byte[] dst目标数组中

>3.使用相对批量put(byte[] src)方法可以将byte[]数组或其他字节缓存区中的连续字节储存到此缓存内

>4.使用绝对和相对getType()[如 getLong getInt]和putType()方法可以按照字节顺序在字节序列中读写其他基本数据类型，例如在byteBuffer对象内读取int， 一次需要2个元素，2位来获得一个int

>5.提供了转换为其他缓存类型的方法，例如asCharBuffer, asFloatBuffer()等，如果转换后多余的部分，则自动补0，转换为对应的数据类型

>6.提供了对字节缓存对象进行压缩compacting,复制duplicating，截取slicing方法；压缩即保留未读的元素，读过的数据直接覆盖，将未读的往前移动。

1.方法put(byte[] src, int offset, int length), 方法get(byte[] dst, int offset, int length)
>put(byte[] src, int offset, int length)：相对批量，将把给定源数组中的字节，从offset下标开始，取length为长度，到缓存对象中;如果要从该数组中复制的字节多于此缓冲区中的剩余字节(即如果length > remaining()),则不传输字节抛出BufferOverflowException

put方法效果相当于：
	
	for(int i = off; i <off + len; i ++){
		dst.put[a[i]]
	}

>get(byte[] dst, int offset, int length)：相对批量，将缓冲区当前位置的字节传输到给定的目标数组内；如果此缓冲区剩余的字节少于满足请求所需的字节（即如果length > remaining()），则不传输字节且抛出BufferUnderflowException异常

get方法效果相当于：
	
	for(int i = 0; i < off + len; i ++){
		dst[i] = buffer.get();
	}

2.方法put(byte[] src), 方法get(byte[] dst)
>put(byte[] src):相对批量，将src字节数组在position处放入缓存

>get(byte[] dst):相对批量，将缓存内position处开始，dst.length长度的数据放入目标数组dst内

3.方法put(int index, byte b), 方法get(int index)
>put(int index, byte b):绝对操作， **不改变position位置**，在底层数组hb， index处存入 byte b

>get(int index)：绝对操作，**不改变position位置**， 获取底层数组hb， index处存储的数据

4.方法put(ByteBuffer src), 
>put(ByteBuffer src), 相对批量， 将源缓存src内的数据存入本缓存，position位置为起始，如果超出limit则抛出异常，异常后则不传输任何数据

5.相对（绝对） putType(), getType()， asTyteBuffer()
>根据基本类型所占多少字节，互相转换，如ByteBuffer.putInt(1),则需要占用4个字节, charBuffer.asIntBuffer()返回的IntBuffer对象的容量capacity和限制limit为CharBuffer对象
剩余的字节数的二分之一，其他类型以此类推

6.方法slice(), arrayOffSet()为非0
>slice()创建新的字符缓冲区，两个Buffer对象(直接，非直接)公用一个底层数组，position, limit互相独立

>使用slice方法后，再调用arrayOffSet()方法时会出现返回值为非0的情况，返回的数值为当前被切割的子缓存对象的第一个元素在父缓存对象中时第几个，即索引偏移值

7.方法ByteOrder order()， 方法order(ByteOrder, bo)
>不同的CPU读取字节的顺序可能不同，从高位到低位或者从低位到高位，，当这两种CPU传递数据时就需要进行字节排列的统一

>例如一个双字节，16bit的数据：FF1A,高位为FF， 低位为1A；右边的低位，左边是高位

>方法orde()返回此缓存对象的字节读取顺序，order(ByteOrder, bo)设定当前缓存对象的字节读取顺序

8.asReadOnlyBuffer()
>根据当期缓存对象，创建一个只读缓存区，新的只读缓存区与当前缓存区公用一个底层数组，position, limit互相独立

9.方法compact()
>压缩当前缓存对象;将当前缓存对象，position之后到limit之前的数据整体向前覆盖，position位置的数据覆盖到第一个，丢去capacity到limit之间的空间，释放多余空间即压缩

>可以使用compact()方法后，在flip()读取缓存中剩余的数据

10.方法equals(Object obj), compareTo(ByteBuffer that)

(1)equals(Object obj)方法源码
	
	public boolean equals(Object ob) {
        if (this == ob)
            return true;
        if (!(ob instanceof ByteBuffer))
            return false;
        ByteBuffer that = (ByteBuffer)ob;
        if (this.remaining() != that.remaining())
            return false;
        int p = this.position();
        for (int i = this.limit() - 1, j = that.limit() - 1; i >= p; i--, j--)
            if (!equals(this.get(i), that.get(j)))
                return false;
        return true;
    }

说明只有两个缓存对象，position与limit之间的数据每个都相同，或者是同一个对象，返回true，否则返回false。返回true之后，两个缓存对象的容量capacity可能不相同

(2)compareTo(ByteBuffer that)方法源码
	
	public int compareTo(ByteBuffer that) {
        int n = this.position() + Math.min(this.remaining(), that.remaining());
        for (int i = this.position(), j = that.position(); i < n; i++, j++) {
            int cmp = compare(this.get(i), that.get(j));
            if (cmp != 0)
                return cmp;
        }
        return this.remaining() - that.remaining();
    }

	public static int compare(byte x, byte y) {
        return x - y;
    }

判断两个缓存对象，范围从当前缓存对象的position开始，以两个缓存对象最小的remaining()值结束，判断范围是两个缓存对象有效范围索引的交集；

如果在交集内有一个字符不同，返回两个缓存对象remaining的差值；如果每个字符都相等，返回0；

返回0时，两个缓存的capacity可以不同

11.方法duplicate()

>根据当前缓存对象的capacity, limit, position, mark，复制出一个与之相同的缓存对象，两个对象共用一个底层数组，此方法不改变原缓存对象的任何信息


####7.CharBuffer API
1.append()
>append()方法与ByteBuffer中的put方法类似

2.public final chad charAt(int index)
>读取“相对位置”的给定索引处的字符，如果position为3，charAt(4)读取的是底层数组index为7的字符信息

3.put(String src), int read(CharBuffer target), subSequence(int start, int end)
>put(string src):将src字符串中的所有内容传输到缓存对象内，以当前位置position为起点存储

>int read(CharBuffer target):以position为起点，读取target长度的信息，存储到target缓存对象内

>subSequence(int start, int end):返回一个CharBuffer对象，存储的是原缓存对象底层数组中position + start到 position + end索引之间的数据

4.public static CharBuffer wrap(CharSequence csq, int start, int end)
>方法返回一个CharBuffer对象，该对象存储csq内， 索引从start 到end的字符信息，CharBuffer对象容量为csq.length, 限制limit为end，位置position为start

**此方法如果传入的是字符串信息，返回的CharBuffer对象为只读的**

5.final int length()
>返回此字符缓冲区的长度，为position-limit的数值，与remaining()返回的数值相等

####8.手动释放缓存内存，与JVM释放缓存内存

1.手动释放
	
	public class Run {
		public static void main(String[] args) throws NoSuchMethodException,
				SecurityException, IllegalAccessException,
				IllegalArgumentException, InvocationTargetException,
				InterruptedException {
			ByteBuffer buffer = ByteBuffer.allocateDirect(100000000);
			byte[] byteArray = new byte[] { 1 };
			for (int i = 0; i < 100000000; i++) {
				buffer.put(byteArray);
			}
			System.out.println("put end");
			Thread.sleep(15000);
			Method cheanerMethod = buffer.getClass().getMethod("cleaner");
			cheanerMethod.setAccessible(true);
			Object returnValue = cheanerMethod.invoke(buffer);
			Method cleanMethod = returnValue.getClass().getMethod("clean");
			cleanMethod.setAccessible(true);
			cleanMethod.invoke(returnValue);
			System.out.println("main end");
		}
	}

反射调用cleaner对象内的clean方法，手动掉用C++ free函数释放内存

2.JVM释放，在创建对象后不用操作，一段时间后，GC会自动进行内存回收

####9.直接缓冲区与非直接缓冲区的运行效率的比较

直接缓冲区会直接作用于本地操作系统的IO，处理数据的效率相比非直接缓冲区会快一些

直接缓冲区DirectByteBuffer在内部使用sun.mics.Unsafe类直接与操作系统进行交互；而非直接缓冲区ByteBuffer在JVM内需要创建内存对象，后通过JVM与操作系统实现交互，所以直接缓冲区存取数据速度更快

使用方法allocateDirect()创建出来的缓冲区类型为DirectByteBuffer,JVM内不存在底层数组； 使用方法allocate()创建出来的缓冲区类型为HeapByteBuffer，存在底层数组

####10.编码问题

CharBuffer.get()使用的是utf-16BE编码, String对象调用.getBytes()使用的是utf-8，可以在getBytes()方法内传入“utf-16BE”,使字符串按照utf-16BE编码传入缓存内，这样不会出现乱码

或者使用CharSet.forName("utf-8").decode(byteBuffer),按照utf-8对ByteBuffer对象进行utf-8编码

