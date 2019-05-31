#第二章 Channel通道 FileChannel
将操作的数据打包到Buffer缓冲区中，而缓冲区中的数据想要传输到目的地是需要依赖于Channel通道
通道是传输数据需要的通路，Channel只接受字节数据

接口继承关系
	
	AutoCloseable -> Closeable -> Channel -> NetworkChannel -> MulticastChannel
										  -> AsynchronousChannel -> AsynchronousChannel
										  -> ReadableByteChannel ->ScatteringByteChannel
										  -> WriteByteChannel -> GatheringByteChannel
										  -> InterruptibleChannel

	ReadableByteChannel ->	ByteChannel -> SeekableByteChannel
	WritableByteChannel ->

Buffer缓冲区都是class类，而Channel通道都是Interface接口，这是因为通道Channel的功能实现依赖于操作系统，所以在接口中只定义有哪些功能

####1.AutoCloseable
类实现了该接口，具有自动关闭资源的功能，jdk1.7

####2.AsynchronousChannel
支持异步IO操作，有两种方式进行实现

1.Future<V> operation(...)
>operation代表IO操作的名称，大多数都是read或者write，泛型V代表经过IO操作后，返回结果的数据类型

2.void operation(...A attachment, CompletionHandler<V, ? super A>handler)
>回调函数，且CompletionHandler可以被复用，当IO成功或失败时，CompletionHandler对象中的指定方法会被调用

如果调用通道对象的cancel()方法时，所有等待I/O操作结果的线程会抛出CancellationException异常，并且其他在此通道中未完成的操作将会出现AsynchronousCloseException异常

####3.AsynchronousByteChannel
继承AsynchronousChannel， 支持异步操作，单位为字节，上一个read()方法未完成之前再次调用read()就抛出ReadPendingException；一个write()未完成在调用一个write()抛出WritePendingException

####4.ReadableByteChannel
允许读操作，如果有一个读操作正在进行，会阻塞其他的读操作

将通道当前位置的字节序列读入1个ByteBuffer对象内，read(ByteBuffer buffer)方法是同步的

####5.ScatteringByteChannel
分散，能从通道内读取字节到多个缓冲区Buffer中

####6.WritableByteChannel
允许写操作，如果有一个写操作正在进行，会阻塞其他的写操作

将一个ByteBuffer缓冲区内的字节序列写入通道的当前位置，write(ByteBuffer buffer)方法是同步的

####7.GatheringByteChannel
聚集，能将多个缓冲区Buffer中的数据写入通道内

####8.ByteChannel
继承于WritableByteChannel，ReadableByteChannel，读与写操作都是同步的，双向操作

####9.SeekableByteChannel
能在字节通道中维护position位置，以及允许position位置发生改变

####10.NetworkChannel
使通道与套接字Socket进行关联，使通道中的数据能在Socket技术上进行传输，bind()方法用于将套接字绑定到本地地址上

####11.MulticastChannel
多路广播，支持IP多播，将多个IP进行打包到一个group内，然后将IP报文向这个group进行发送，即向多个主机进行发送

####12.InterruptibleChannel
可中断通道，能以异步的方式进行关闭与中断。一个线程对通道进行中断，会关闭通道并导致阻塞的线程接受到ClosedByInterruptException异常

##1.AbstractInterruptibleChannel
FileChannel的父类，主要作用是提供了一个可以被中断的通道基本实现类，当调用close方法关闭该通道时，底层调用了implCloseChannel()方法

抽象了AbstractInterruptibleChannel,实现了InterruptibleChannel接口

##2.FileChannel类的使用
继承于AbstractInterruptibleChannel, 继承于AbstractInterruptibleChannel实现了InterruptibleChannel接口

该类的主要作用是读取，写入，映射和操作文件的通道，该通道永远是阻塞的操作

该类在内部维护当前文件的position，可对其进行查询与修改。可以对文件自动扩容

此类中出了常见的读取写入关闭操作之外，还定义了一下文件的操作：
>1.以不影响通道当前位置的方式，对文件中绝对位置的字节进行读取与写入

>2.将文件中的某个区域直接映射到内存中；对于较大的文件，比read和write效率更高

>3.强制对底层存储设备进行文件的更新，保证系统崩溃后文件不丢失

>4.以一种可被很多操作系统优化为直接向文件系统缓存发送，或从中读取的高速传输方法，将字节从文件传输到某个其他通道中

>5.可以锁定某个文件区域，以阻止其他程序对其进行访问

该类的实例可以从FileOutputStream对象调用getChannel()方法获得

FileOutputStream(File, boolean)构造方法第二个参数为true时，每次调用相关写操作都会在文件末尾处写入

####1.int write(ByteBuffer src), int read(ByteBuffer dst)，long position()
1.int write(ByteBuffer src)
>从给定的缓冲区写入此通道的当前位置，通道只有位置position的概念，没有容量capacity和限制limit，同步方法

>write(ByteBuffer src)方法是将ByteBuffer的remaining写入通道

2.int read(ByteBuffer dst)
>从此通道的当前位置读入给定的缓冲区的当前位置，返回值代表的读取的字节数，如果该通道已经达到流的末尾，则返回-1

>read(ByteBuffer dst)从通道的当前位置开始读取，读入到ButeBuffer dst内的当前位置position

>read()方法为同步方法

>ByteBuffer缓冲区remaining为多少，就从通道中读多少字节的数据

3.long position()
>返回此通道内的位置值

####2.long write(ByteBuffer[] srcs)， long write(ByteBuffer[] srcs, int offset, int length)
>long write(ByteBuffer[] srcs)方法是将每个缓冲区的remaining字节序列写入此通道的当前位置
，从通道的当前位置开始写入

>write()方法是同步的，将读个ByteBuffer缓冲区中的remaining剩余字节序列写入通道的当前位置中

>数组srcs是按照下标从小到大写入channel通道内

>long write(ByteBuffer[] srcs, int offset, int length)：以指定缓冲区数组的offset下标开始，向后使用length个字节缓冲区；offset必须为正数并且不能大于srcs.length；length必须为正数并且不能大于srcs.length - offset

>write(ByteBuffer[] srcs, int offset, int length)方法是同步的

####3.long read(ByteBuffer[] dsts), long read(ByteBuffer[] dsts, int offset, int length)
>read(ByteBuffer[] dsts):将通道当前位置position中的字节序列读入1个ByteBuffer缓冲区中的remaining空间中

>read()方法具有同步性

	public class Run {
		private static FileInputStream fisRef;
		private static FileChannel fileChannel;
		public static void main(String[] args) throws IOException {
			fisRef = new FileInputStream(new File("e:\\abc.txt"));
			fileChannel = fisRef.getChannel();
			ByteBuffer buffer_1 = ByteBuffer.allocate(8);
			ByteBuffer buffer_2 = ByteBuffer.allocate(8);
			buffer_1.position(3);
			buffer_2.position(4);
			ByteBuffer[] byteBufferArray = new ByteBuffer[] { buffer_1, buffer_2 };
			fileChannel.read(byteBufferArray, 0, 2);
	
			for (int i = 0; i < byteBufferArray.length; i++) {
				ByteBuffer buffer = byteBufferArray[i];
				byte[] array = buffer.array();
				for (int j = 0; j < array.length; j++) {
					if (array[j] == 0) {
						System.out.print("空格");
					} else {
						System.out.print((char) array[j]);
					}
				}
				System.out.println("");
			}
			fileChannel.close();
			fisRef.close();
		}
	}

####4.write(ByteBuffer src, long position), read(ByteBuffer dst, long position)
>方法write(ByteBuffer src, long position)作用是将缓冲区src剩余字节序列写入通道的position位置

>write(ByteBuffer src, long position)方法具有同步特性

>使用write(ByteBuffer src, long position)为操作绝对位置，可以自动扩容，不影响channel通道的position位置

>read(ByteBuffer dst, long position)将通道的指定位置的字节序列读入给定的缓冲区的当前位置，如果给定的位置大于该文件的当前大小，则不读取任何字节；绝对操作不影响通道当前的位置position

>read(ByteBuffer dst, long position)方法具有同步性，即一个线程运行read方法时，另一个线程不能操作当前channel的read方法

####5.方法truncate(long size)
>方法truncate(long size)：将此通道的文件截取为给定大小，从下标0处，开始截取size个字节，如果需要截取的大小大于文件字节数，则将文件全部字节读入

####6.方法long transferTo(int position, int count, WritableByteChannel dest)
>直接操作当前通道的以position位置开始，count个字节数据传输到给定的可写入字节通道dest；不需要中间文件，直接将文件从资源通道到目标通道

>绝对操作不影响通道的position位置

####7.long transferFrom(ReadableByteChannel src, int position, int count)
>transferFrom()方法：将字节从资源通道src对象内，从src的position位置处，读取count个字节到当前通道对象的position位置，注意当前方法内的position为当前通道对象的position

>transferFrom()方法为绝对操作，不改变当前通道的position位置

####8.FileLock lock(long position, long size, boolean shared)
>lock方法是获取此通道的文件给定区域上的锁定，从参数position开始，锁定size个字节；如果无参数则锁定最大Long.MAX_VALUE个字节的区域

>如果一个区域被锁定，其他线程来操作锁定区域内的字节，其他线程将阻塞；如果另一个线程关闭了此通道，则当前调用lock方法的线程抛出AsynchronousCloseException；如果在等待获取锁定的同时中断了调用线程，则将状态设置为中断并抛出FileLockInterruptionException

>由position和size参数所指定的区域无需包含在实际的底层文件中，锁定区域大小固定；

>参数boolean shared设定是否为共享锁还是独占锁；lock方法具有阻塞性；设置为true为共享锁，共享锁可以进行读共享，但读写与写写互斥；独享锁所有情况下都互斥

	public class Run {
		public static void main(String[] args) throws IOException,
				InterruptedException {
			RandomAccessFile file_A = new RandomAccessFile("e:\\abc.txt", "rw");
			FileChannel channel = file_A.getChannel();
			channel.lock(2, 2, true);
			Thread.sleep(Integer.MAX_VALUE);
		}
	}
	
	public class Run1 {
		public static void main(String[] args) throws IOException {
			RandomAccessFile file_A = new RandomAccessFile("e:\\abc.txt", "rw");
			FileChannel channel = file_A.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(10);
			channel.read(buffer);
			buffer.rewind();
			for (int i = 0; i < buffer.limit(); i++) {
				System.out.println((char) buffer.get());
			}
		}
	}

>lock方法可以提前进行锁定，即文件大小小于指定的position时，可以提前在position位置处加锁

>一个相同的通道对象，可以获得多个不同的共享锁对象，但与独享锁是互斥的

####9.FileLock tryLock(long position, long size, boolean shared),FileLock try()
>尝试获取一次给定区域的文件锁定，此方法不阻塞，如果未获得锁，则返回null；其他与lock方法相同

##3.类FileLock的API使用
表示文件区域锁定的标记，调用该类对象的release()方法释放锁； isValid验证锁对象是否有效

####1.方法channel()， 方法acquiredBy()
>两个方法都是获得当前所属的FileChannel文件通道对象，新版jdk1.8中，acquiredBy()方法已经取代channel()方法

####2.方法overlaps(long position, long size)
>判断此锁定是否与给定的锁定区域重叠，即位置position，size个字节的区域，至少重叠一个字节时返回true

####3.void force(boolean metaData)
>强制将所有对此通道的文件更新写入磁盘内，metaDate为true时必须写入对文件内容和元数据的更新，为false时只更新内容，显示调用force方法强制将内存内的更新内容写入磁盘，但不能完全保证数据不丢失，force方法使用过频影响效率

####4.方法map(MapMode mode, long position, long size)
>将此通道的文件区域直接映射到内存中，MapMode为枚举类型，提供三种映射属性：READ_ONLY（只读），READ_WRITE（读写）， private（专用）

>READ_ONLY模式下如果对映射区修改将会抛出只读异常；READ_WRITE模式会将对映射区的更改更新到文件内；PRIVATE模式下对映射区的更改不会更新到磁盘文件内

>调用map()方法后，返回MappedByteBuffer直接字节缓冲区，如果position超出映射范围则抛出异常

>只有当此MappedByteBuffer对象是由读写模式下的Filechannel（READ_WRITE）映射出时，才可以调用force方法强制将更改的数据写入磁盘文件内



####5.方法FileChannel open（Path path, OpenOption option1, .....options）
>打开一个文件，以便于对这个文件进行处理

OpenOption为枚举常量
>使用OpenOption.CREATE 与 OpenOption.WRITE时，如果当前参数path内没有该文件则会新创建一个，单独使用OpenOption.CREATE无效

>OpenOption.APPEND：如果打开该文件进行写入操作，将数据写入文件的末尾处；

>OpenOption设置读取访问权限

>OpenOption.TRUNCATE_EXISTING作用：将可写入文件清空，长度截为0，入股为只读文件则忽略

>OpenOption.CREATE_NEW：创建新文件，如果文件已经存在则抛异常，创建新文件同样需要OpenOption.WRITE

>OpenOption.ON_CLOSE：使用通道结束后，删除该文件

>OpenOption.ON_CLOSE：稀疏文件，避免只有一个或几个有效字符，却占用大量磁盘空间的情况，即忽略当前文件内有效数据前的空格

>OpenOption.SYNC：对文件内容或元数据的每次更新都会同步写入到磁盘文件内，保证数据完整但会降低效率

>OpenOption.DSYNC：对文件内容的每次更新都会同步写入到磁盘文件内，保证数据完整但会降低效率