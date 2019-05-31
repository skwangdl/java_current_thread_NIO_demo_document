#第五章 Selector选择器

Select选择器结合SelectableChannel可选择通道使用实现了非阻塞效果，并实现了IO通道的多路复用，即复用操作可选择通道的线程，避免CPU进行线程上下文切换，因为切换线程比较耗时

线程数会随着通道的多少而动态的增减进行配置，在内部其实并不永远是一个线程；通过openjdk发现1023个通道配置一个线程，1024配置两个线程；通知机制由操作系统内部实现

适合于高并发高频短处理的业务环境

##1.核心类Selector，SelectionKey, SelectableChannel
当SelectableChannel在Selector对象里使用register方法注册后，返回一个新的SelectionKey对象，表示该非阻塞通道已经注册了，SelectableChannel注册之前需要设置成非阻塞并且对同一个Selector只能注册一次

SelectableChannel注册后，在其注销之前一直保持注册状态，不能直接注销通道，而需要返回的SelectionKey调用cancel方法显式注销

SelectableChannel并发安全

1.只有SelectableChannel通道对象才能被Selector选择器对象复用

2.ServerSocketChannel类的父类为SelectableChannel类，父类为抽象类；SelectableChannel类与FileChannel平级，都实现了AbstractInterruptibleChannel接口

3.SelecttionKey对象代表ServerSocketChannel向Selector注册的标记，通过SelectionKey对象可以操作对应的ServerSocketChannel对象的状态

##2.通道类SelectableChannel
该类继承于AbstractInterruptibleChannel接口，添加了register()注册方法

##3.通道类ServerSocketChannel与接口NetworkChannel
类ServerSocketChannel是针对面向流的侦听套接字的可选择通道，必须通过socket()方法获得所关联的ServerSocket对象来完成对套接字的完整抽象

ServerSocketChannel并发安全

##4.ServerSocketChannel Selector SelectionKey的API使用

####1.static ServerSocketChannel open()，ServerSocket socket()

1.ServerSocketChannel为抽象类，可使用open方法进行实例化;open()静态方法的作用是打开服务器套接字通道

2.socket()方法是从当前ServerSocketChannel对象中获取ServerSocket对象

####2.public final ServerSocketChannel bind(SocketAddress local)/bind(SocketAddress local, int backlog)

1.bind(SocketAddress local)方法将通道对象的套接字绑定到本地地址并侦听连接
	
	ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
	ServerSocket serverSocket = serverSocketChannel.socket();
	serverSocket.bind(new InetSocketAddress("127.0.0.1", 8088));

	//上下代码等效

	ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
	serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8088));

2.bind(SocketAddress local, int backlog)方法限制连接当前通道的个数为backlog

####3.阻塞与非阻塞和方法public abstract socketChannel accept()的使用效果，configureBlocking(boolean block)方法
accept()方法的作用是接受到此通道套接字的连接

如果此通道处于非阻塞模式，那么在不存在挂起的连接，此方法将直接返回null

使用ServerSocketChannel对象中的configureBlocking(boolean block)方法即可设置是否为阻塞模式
	
	public class Run {
		public static void main(String[] args) throws IOException {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			System.out.println(serverSocketChannel.isBlocking());
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8088));
			System.out.println("begin" + System.currentTimeMillis());
			SocketChannel socketChannel = serverSocketChannel.accept();
			System.out.println("end" + System.currentTimeMillis());
			socketChannel.close();
			serverSocketChannel.close();
		}
	}

####4.类Selector的public static Selector open()， public final SelectionKey register(Selector sel, int ops)

1.Selector类不能直接实例化，需要调用open()静态工厂方法（非单例）构建实例

2.register(Selector sel, int ops)向给定的选择器对象sel注册此通道对象，ops为此通道的操作行为（读，写，接受，连接）
	
	ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
	serverSocketChannel.configureBlocking(false);
	ServerSocket serverSocket = serverSocketChannel.socket();
	serverSocket.bind(new InetSocketAddress("127.0.0.1", 8088));
	
	//将通道注册到选择器对象上
	
	Selector selector = Selector.open();
	serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

**注意：必须将通道设置成非阻塞的才能向选择器进行注册，否则报错**

3.isRegistered()方法判断此通道是否已经向某个选择器进行注册，新创建的通道都是未注册的

4.isOpen()方法判断此通道是否处于打开状态

####5.方法public final Object blockingLock()， Set<SocketOption<?>> supportedOptions()， setOption(SocketOption<T> name, T value), getOption(SocketOption<T> name)， getLocalAddress()
1.blockingLock()的作用是获取其configureBlocking和register方法实现同步的对象，防止重复注册

2.supportedOptions()的作用是返回通道支持的套接字行为选项

3.setOption()方法作用：设置当前通道对象的行为选项值，该通道支持什么就只能设置什么

4.getOption()方法获取该通道的支持行为选项
	
	public class Run1 {
		public static void main(String[] args) throws IOException {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			System.out.println(serverSocketChannel
					.getOption(StandardSocketOptions.SO_RCVBUF));
			System.out.println(serverSocketChannel.setOption(
					StandardSocketOptions.SO_RCVBUF, 2000));
			System.out.println(serverSocketChannel
					.getOption(StandardSocketOptions.SO_RCVBUF));
		}
	}

5.getLocalAddress()获取此通道绑定的SocketAddress对象

####6.方法public final SelectionKey keyFor(Selector sel)， 方法public final SelectorProvider provider()，方法public final int validOps()

1.keyFor()方法为获取此此通道向选择器sel注册的SelectionKey,同一个通道对象可以注册到不同的选择器上

2.provider()方法的作用：返回创建此通道的SelectorProvider,类SelectorProvider的作用是用于选择器和可选择通道的服务提供者，选择器提供者是此类的一个具体子类

3.validOps()：返回一个操作集，标识此通道所支持的操作

####7.类SocketChannel中的方法 public abstract boolean connect(SocketAddress remote)， isConnectionPending， finishConnect()

1.connect(SocketAddress remote)：将此通道连接到远程通道的Socket，如果此通道处于非阻塞模式，则此方法的调用将启动非阻塞连接操作

通道呈阻塞模式，则立即发起连接，如果非阻塞则在随后的某个时刻发起连接

阻塞模式的通道与非阻塞模式的通道，连接时间相差不大，阻塞模式在执行connect()方法内部发起了三次SYN请求，完成3次握手之后再返回；而非阻塞模式是在执行connect()方法后立即返回，虽然非阻塞也执行了3次握手

2.isConnectionPending()方法判断当前通道对象是否正在发起连接操作

3.finishConnect()完成套接字通道的连接过程，当连接成功后返回true；如果连接失败则抛出IOException

####8.类FileChannel 方法long transferTo(position, count, WritableByteChannel)

transferTo(position, count, Writable)：想目标通道传输数据，如果目标通道是非阻塞的，运行transferTo()方法后，直接将发送缓冲区内的数据发出，可能发送的数据字节数少于对方缓冲区的大小

####9.方法public static SocketChannel open(SocketAddress remote)与SocketOption的执行顺序

需要先执行open()方法，open()底层调用connect()进行本地连接，在执行SocketOption设置，即以StandardSocketOptions类的属性值进行设置

##5.类Selector的API使用
选择器Selector主要作用是SelectableChannel对象的多路复用

可通过调用此类的open方法创建选择器，该方法将使用系统的默认SelectorProvider创建新的Selector选择器

选择器Selector维护了三种SelectionKey-Set选择键集，键集本身不可以直接被修改
>1.键集：包含的键表示当前通道到此选择器的注册

>2.已选择键集：在首先调用select()方法选择操作期间，检测每个键的通道是否已经至少为该键的相关操作集所标识的一个操作准备，已选择键集为键集的子集

>3.已取消键集：是已被取消但其通道尚未注销的键的集合，调用select()后，更新键集并将已取消键删除

####1.方法public abstract int select()具有阻塞性
select()方法的作用是选择一组键，其相应的通道已为I/O操作准备就绪，select()方法只有等到有外部套接字连接到当前选择器内的注册通道后，才会继续运行，否则阻塞

select()方法有可能不阻塞，即出现死循环；当client连接server时，server中的通道对accept事件并未处理导致accept事件一直存在，select()方法一直检测到有准备好的通道要对accept事件处理，所以出现死循环,以下代码及时对事件进行处理，不会死循环
	
	public class Client {
		public static void main(String[] args) throws UnknownHostException,
				IOException {
			Socket socket = new Socket("127.0.0.1", 8088);
			socket.close();
		}
	}
	
	public class Server {
		public static void main(String[] args) throws IOException {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8088));
			serverSocketChannel.configureBlocking(false);
	
			Selector selector = Selector.open();
			SelectionKey keys = serverSocketChannel.register(selector,
					SelectionKey.OP_ACCEPT);
			
			while (1 == 1) {
				int select = selector.select();
				Set<SelectionKey> set1 = selector.keys();
				Set<SelectionKey> set2 = selector.selectedKeys();
				System.out.println("keycount=" + select);
				System.out.println("key set size=" + set1.size());
				System.out.println("key set size=" + set2.size());
				System.out.println();
				Iterator<SelectionKey> iterators = set2.iterator();
				while (iterators.hasNext()) {
					SelectionKey key = iterators.next();
					ServerSocketChannel channel = (ServerSocketChannel) key
							.channel();
					channel.accept();
				}
			}
		}
	}

select()方法的返回值代表已更新其准备就绪操作集的键的数目，可能为0，非0的情况就是向selectKeys添加了对象，为0的情况就是selectKeys没有更改

对相同的通道注册不同的感兴趣的事件返回同一个SelectionKey

1个SocketChannel通道注册2个事件并没有创建出2个SelectionKey，而是创建出1个，read和write事件是在同一个SelectionKey中进行注册的

####2.方法public abstract boolean isOpen()，public abstract void close()，public abstract SelectorProvider provider() 
1.isOpen()：告知此选择器是否已打开，返回值代表当且仅当此选择器打开时才返回true

2.close()：关闭此选择器，如果某个线程正在此选择器的某个方法阻塞，则中断该线程，释放该选择器的所有资源，继续使用该选择器抛出ClosedSelectorException

3.provider()：返回创建此通道的提供者对象，在Selector底层中主要为提供者对象构建其他实例对象

####3.public abstract Set<SelectionKey> keys()
keys()方法返回此选择器对象的键集，该键集不可以直接进行修改，仅在已取消某个键并且注销其通道后才移除该键，直接操作键集抛异常

####4.public abstract int select(long timeout)
select(long timeout)：在等待某个通道准备就绪时最多阻塞timeout毫秒，默认为0，0代表无期限一直阻塞，如果在timeout毫秒内获得某个通道，则也阻塞timeout毫秒

####5.public abstract int selectNow()
selectNow()方法的作用：选择一组键，其相应的通道已为I/O操作准备就绪，此方法执行非阻塞的选择操作，非阻塞方法

####6.public abstract Selector wakeup()
wakeup()方法作用：使尚未返回的第一个选择操作立即返回；如果另一个线程目前正在阻塞在select()或select(long)方法的调用中，则该调用将立即返回，将该返回的通道的SelectionKey从已选择键集内移除

####7.细节说明
1.对SelectionKey执行cancel后的效果
>调用SelectionKey对象的cancel方法，会取消该键，下一次select()方法被调用时，该被取消的键对应的通道将被删除

2.对通道对象执行close()
>关闭某个通道，通道对应的键都被添加到其选择器的已取消键集中，下一次select()会将已取消键集内的键删除，并将这个ServerSocketChannel通道从Selector中注销掉

3.在新创建的Selector中，键集，已选择键集，已取消键集都为空

4.不能直接将键从键集内删除，会导致UnsupportedOperationException

5.阻塞在select()或select(long)中的线程，当Selector对象调用interrupt()方法或close()方法后被中断，close()方法调用后删除全部键并且注销全部通道

##6.SelectionKey的API使用
类SelectionKey表示SelectableChannel在Selector中的注册标记；选择器是线程安全的，但是键集非线程安全，所以不可以直接操作键集

####1.方法public final boolean isAcceptable()，public final boolean isConnectable()
1.isAcceptable()方法：测试此键的通道是否已准备好接受新的套接字连接

2.isConnectable()方法：测试此键的通道是否已完成其套接字连接操作

####2.方法public final boolean isReadable()， public final boolean isWritable()
1.isReadable()：方法测试此键的通道是否已准备好进行读取

2.isWritable()：测试此键是否已准备好进行写入

####3.方法public abstract Selector selector()
返回此键关联的选择器Selector对象，如果该键是已取消的，也可以返回选择器对象

####4.方法public final SelectionKey register(Selector sel, int ops, Object att)，public final Object attachment()， public final Object attach(Object ob)
1.该register方法为类SelectableChannel内的方法，对指定的选择器对象sel进行注册，并且添加附件对象att

2.attachment()方法：获取当前键的附件对象，如果没有附件对象则返回null

3.attach(Object ob)：将附件对象ob添加到当前键中，一次只能附加一个对象，多次附加保留最后一个

####5.方法public abstract int interestOps()， public abstract SelectionKey interestOps(int ops)， public abstract int readyOps()
1.interestOps()方法：获取此键的interest事件状态未，每一个感兴趣事件为一位，此方法为位运算；interestOps(int ops)设定此键的interest状态参数

2.readyOps()方法返回此键的ready操作状态位，关联位运算

####6.方法public abstract boolean isValid()
isValid()查看此键是否有效；如果键被close()，其通道已关闭或选择器关闭，则此键为无效，isValid方法返回false

####7.类DatagramChannel的API使用
类DatagramChannel是针对DatagramSocket ,UDP协议的可选择通道，DatagramChannel支持并发读写，需要两个物理机进行测试

1.public abstract DatagramChannel connect(SocketAddress remote)
>让此通道连接远程的套接字地址

2.public abstract DatagramChannel disconnect()
>让此通道断开连接

3.MembershipKey join(InetAddress group, NetworkInterface interf)
>将此通道加入到组播地址group中，interf代表使用哪个网卡配置对象

4.MembershipKey join(InetAddress group, NetworkInterface interf, InetAddress source)
>将通道加入到组播地址中，但会通过source参数接受指定客户端IP发送过来的数据包

##7.类Pipe.SinkChannel和Pipe.SourceChannel
Pipe.SinkChannel表示可写入结尾的通道，Pipe.SourceChannel表示可读取结尾的通道

实现单向管道传输的通道对，但Pipe.SinkChannel与Pipe.SourceChannel必须在一个进程中且有一个Pipe对象创建出来
	
	public class Server {
		public static void main(String[] args) throws IOException {
			Pipe pipe = Pipe.open();
			SourceChannel sourceChannel = pipe.source();
			SinkChannel sinkChannel = pipe.sink();
			new Thread() {
				public void run() {
					try {
						sinkChannel.write(ByteBuffer.wrap("hello".getBytes()));
						sinkChannel.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
			}.start();
			ByteBuffer buffer = ByteBuffer.allocate(10);
			int read = sourceChannel.read(buffer);
			while (read != -1) {
				String str = new String(buffer.array(), 0, read);
				System.out.println(str);
				read = sourceChannel.read(buffer);
			}
			sourceChannel.close();
		}
	}

##8.类SelectorProvider的API
用于选择器与可选择通道的服务提供者类，JVM自带提供；
SelectorProvider调用provider()静态工厂方法构建实例，实例内提供各种open方法可以构建各种通道实例