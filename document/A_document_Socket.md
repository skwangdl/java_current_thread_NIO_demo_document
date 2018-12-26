#第四章 实现Socket通讯
TCP/IP协议的基础上使用Java实现Socket通讯

##1.基于TCP的Socket通讯
TCP协议提供基于“流”的“长连接”的数据传递，发送的数据带有顺序性；长连接在连接成功后，保持开启状态，短连接在数据传输后关闭，不保持开启状态

####1.类ServerSocket的accept()方法具有阻塞性

创建Socket套接字的服务端，accept方法具有阻塞性；侦听并接受到此套接字的连接
	
	public class Client {
		public static void main(String[] args) throws UnknownHostException,
				IOException {
			System.out.println("client连接准备=" + System.currentTimeMillis());
			Socket socket = new Socket("127.0.0.1", 8088);
			System.out.println("client连接结束=" + System.currentTimeMillis());
			socket.close();
		}
	}
	
	public class Server {
		public static void main(String[] args) throws IOException {
			ServerSocket serverSocket = new ServerSocket(8088);
			System.out.println("server阻塞开始=" + System.currentTimeMillis());
			serverSocket.accept();
			System.out.println("server阻塞结束=" + System.currentTimeMillis());
			serverSocket.close();
		}
	}

利用Socket阻塞性模拟服务器

	public class Server {
		public static void main(String[] args) throws IOException {
			ServerSocket serverSocket = new ServerSocket(8088);
			System.out.println("server阻塞开始=" + System.currentTimeMillis());
			Socket socket = serverSocket.accept();
			InputStream inputStream = socket.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String getString = "";
			while(!"".equals(getString = bufferedReader.readLine())){
				System.out.println(getString);
			}
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write("hello kepler".getBytes());
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			socket.close();
			serverSocket.close();
			
			System.out.println("server阻塞结束=" + System.currentTimeMillis());
			serverSocket.close();
		}
	}

####2.InputStream类的read()方法也具有阻塞性
如果客户端并未发送到服务端，服务端一直在尝试读取从客户端传递过来的数据，由于客户端从未发送数据，则服务端一直在read()方法处阻塞
	
	public class Client {
		public static void main(String[] args) throws UnknownHostException,
				IOException {
			Socket socket = new Socket("127.0.0.1", 8088);
			OutputStream outputStream = socket.getOutputStream();
			byte[] byteArray = new byte[1024];
			for(int i = 0; i < byteArray.length; i ++){
				byteArray[i] = 1;
			}
			outputStream.write(byteArray);
			outputStream.close();
			socket.close();
		}
	}
	
	public class Server {
		public static void main(String[] args) throws IOException {
			ServerSocket serverSocket = new ServerSocket(8088);
			Socket socket = serverSocket.accept();
			InputStream inputStream = socket.getInputStream();
			byte[] byteArray = new byte[1024];
			System.out.println("read start");
			int read = inputStream.read(byteArray);
			System.out.println("read   end" + read);
			inputStream.close();
			socket.close();
			serverSocket.close();
		}
	}

####3.write()方法允许多次写入
OutputStream对象可以多次调用write()方法进行写入并输出

ObjectInputStream对象可以调用readFully(byte[] byteArray)方法将传输过来的数据读进byteArray字节数组中，阻塞方法

####4.调用流的close()方法造成Socket关闭
当流使用close()方法关闭后，获取此流对象的Socket对象也会关闭

使用Socket可以传输字节数据

####5.TCP协议的三次握手
wireshark抓取网络包，过滤规则：(ip.src==127.0.0.1 and tcp.port==8088) or (ip.dst==127.0.0.1 and tcp.port==8088) 查看本地目标为8088端口与本地资源为8088端口的包传输过程

第一次握手
	
	51812 -> 8088 [SYN] Seq=0 Win=8192 Len=0

>第一次握手时，客户端51812向服务端8088发送标志位 SYN，目的是与服务建立连接

>SYN标志位的值表示发送数据流序号sequence number的最大值，例如如果seq为5，说明在数据流中曾经一共发送了5个字节；本次仅仅是51812向8088发送了一个SYN标志位


第二次握手

	8088 -> 51812 [SYN, ACK] Seq=0 Ack=1 Win=8192 Len=0
>第二次握手时，服务端向客户端发送SYN与ACK标志位， ACK表示对收到数据包的确认，ACK
的数值表示8088希望51812下次发送的数据流的序号是1， Seq=0表示8088没有向51812发送数据， 所以Len代表的数据长度为0

第三次握手

	51812 -> 8088 [ACK] Seq=1 Ack=1 Win=8192 Len=0
>第三次握手， Seq=1代表正是上次8088所期望的Ack=1，虽然Seq=1但Len=0说明51812这次还是没有给8088传递任何字节的数据，而51812向8088传递Ack=1表示期望下次8088发送的数据Seq=1

Seq与Ack的数值具有自增性，如果本次A端口向B端口发送Seq=1， Ack=1，Len=3，下次A端口再想B端口发送的就是Seq=4，并且B端口向A端口发送的Ack=4

####6.TCP断开连接的4次挥手
当服务器与客户端断开时，TCP需要进行4此挥手确认断开

	53165 -> 8088 [FIN, ACK] Seq=1 Ack=1 Win=8192 Len=0
	8088 -> 53165 [ACK] Seq=1 Ack=2 Win=8192 Len=0
	8088 -> 53165 [FIN, ACK] Seq=1 Ack=2 Win=8192 Len=0
	53165 -> 8088 [ACK] Seq=2 Ack=2 Win=8192 Len=0

FIN代表结束会话

服务器与客户端进行握手的时候，是在ServerSocket对象创建出来并且绑定到指定的地址与端口时就进行三次握手

##2.类ServerSocket的API

####1.方法accept()，方法setSoTimeout()
1.accept()
>侦听并接受到此套接字的连接，阻塞方法

2.setSoTimeout()
>设置超时时间，通过指定超时timeout值启动与禁用SO_TIMEOUT，以毫秒为单位

>如果超时，则抛出SocketTimeoutException，但ServerSocket对象仍然有效，超时值设置为0时为无穷大超时值

####2.类ServerSocket构造方法
1.构造方法public ServerSocket(int port, int backlog)
>参数backlog表示允许接受客户端连接请求的个数，设置最大等待队列长度，如果队列满时则拒绝该连接，默认为50

2.构造方法public ServerSocket(int port, int backlog, InetAddress bindAddr)
>bindAddr参数代表ServerSocket对象绑定的网络IP地址对象

3.bind(SocketAddress endpoint)的主要作用是将ServerSocket绑定到特定的Socket地址对象（IP地址和端口号），SocketAddress为抽象类，具体实现类为InetSocketAddress

4.bind(SocketAddress endpoint， int backlog)
>endpoint参数为网络IP地址对象，backlog为允许连接Socket对象的客户端的数量，windows下backlog极限数量为200

####3.方法getLocalSocketAddress()，方法getLocalPort()
1.getLocalSocketAddress()：获取本地的SocketAddress对象，返回此套接字绑定的端点的地址

2.getLocalPort()：获取Socket对象绑定到本地的端口

####4.InetSocketAddress的API使用
InetSocketAddress代表此类实现IP套接字地址，IP+端口号，还可以是主机名+端口号

构造方法：public InetSocketAddress(InetAddress addr, int port)

1.方法getHostName()与getHostString()区别
>getHostName()作用是获取主机名，如果是用IP地址创建，则此方法会通过DNS解析到主机名或域名

>getHostString()作用是返回主机名或地址的字符串形式，不会通过DNS解析

2.方法public static InetSocketAddress createUnresolved(String host, int port)
>根据IP字符串与端口号创建未解析的套接字地址，不会尝试将主机名解析为InetAddress

3.方法public final boolean isUnresolved()
>如果未解析主机名，即IP字符串，则返回true

4.方法public void close(), public boolean isClosed(), public boolean isBound()
>close()方法关闭此套接字；isClosed()如果此套接字已经关闭则返回true；isBound()如果ServerSocket对象成功绑定到一个地址，则返回true

5.方法getInetAddress()
>获取ServerSocket对象绑定的本地IP地址信息，如果套接字是未绑定的，则方法返回null

6.方法public void setReuseAddress(boolean on)，public boolean getReuseAddress()
>setReuseAddress()：启用或禁用SO_REUSEADDR选项。当TCP断开时，该连接会保持占用端口状态一段时间即超时状态，如果启用SO_REUSEADDR，则当该Socket端口处于超时状态后，允许其他Socket对象绑定到此端口

>getReuseAddress()：获取SO_REUSEADDR状态,true或false，CentOS默认为true， Windows不能使用该方法实现超时端口复用

7.方法public void setReceiveBufferSize()，public int getReceiveBufferSize()
>setReceiveBufferSize()：为从此ServerSocket接受的套接字的SO_RCVBUF选项设置新的值。SO_RCVBUF的值，就是TCP中的Win属性，代表通知对方，当要下次要发送过来数据时，一次发送多少字节

>getReceiveBufferSize()：获取SO_RCVBUF缓冲数值的大小

##3.Socket API使用
Socket类主要作用是使Server与client进行通讯

####1.方法bind(SocketAddress bindpoint)，connect(SocketAddress endpoint)
1.bind(SocketAddress bindpoint)：将此套接字对象绑定到本地地址bindpoint,如果为空则随机挑选一个空闲的端口和一个有效的本地IP来绑定此套接字对象

2.connect(SocketAddress endpoint)：将此Socket套接字对象连接到服务器地址对象endpoint；
先bind方法绑定本地端口，在使用connect连接套接字对象到服务器地址对象endpoint

>Socket类的无参构造方法结合connect()方法实现功能是connect()方法执行时，在connect内部自动bind绑定了一个空闲的随机端口，再使用这个端口连接到服务端

####2.方法public void connect(SocketAddress endpoint, int timeout)
将此套接字对象连接到服务器地址对象endpoint，并指定一个超时值。如果超时值为0含义是无限超时，即阻塞。但在windows中，默认超时时间为20秒；当超过timeout的时间没有连接到服务器，则出现超时异常

####3.方法public int getPort()，public int getLocalPort()
1.getPort()：返回此套接字对象连接到的远程服务器端口

2.getLocalPort()：返回此套接字对象绑定到本地的端口

####4.方法public InetAddress getLocalAddress()，public SocketAddress getLocalSocketAddress()

1.getLocalAddress()：获取套接字绑定的本地InetAddress地址信息

2.getLocalSocketAddress()：返回此套接字绑定的端点的SocketAddress地址信息，如果尚未绑定返回null，SocketAddress地址信息包括InetAddress地址信息

####5.方法public InetAddress getInetAddress()，public SocketAddress getRemoteSocketAddress()
1.getInetAddress()：返回此套接字连接到的远程的InetAddress地址，如果此套接字对象时未连接的返回null

2.getRemoteSocketAddress()：返回此套接字远程端点的SocketAddress地址，如果未连接则返回null

####6.方法public void shutdownInput()，public void shutdownOutput()

1.shutdownInput()：关闭当前Socket对象的输入流，即该套接字不能再进行数据发送

2.shutdownOutput()：关闭当前Socket对象的输出流，即该套接字不能再进行数据接收

>如果client使用shutdownInput(),server使用shutdownOutput();之后数据只能从client发送到server

>isInputShutdown()，isOutputShutdown()方法判断输入输出流是否关闭

####7.方法public void setTcpNoDelay(boolean on)，public boolean getTcpNoDelay

启动或禁用TCP_NODELAY，即启用或禁用TCP延时发送数据包，Nagle算法；

Nagle算法主要目的是应对网络中，存在大量的小数据量网络包的情况，在将要TCP传输时，先进行延时并将小数据网络包结合成数据量较大的单包后在发送。

该算法可以将许多要发送的数据进行本地缓存，以减少发送数据包的个数来增加网络软件运行的效率

在未确认ACK之前让发送器把数据送到缓存里，后面的数据也继续放入缓存中，直到得到确认ACK或者直到攒到了一定size大小的数据再发送

当Server端收到数据之后，它并不会马上向client端发送ACK，而是会将ACK发送延迟一段时间

采用Nagle算法后，一个数据包攒到最大报文长度（1500-20-20=1460字节）

####8.方法public synchronized void setSendBufferSize(int size)，public int getSendBufferSize()
1.setSendBufferSize(int size)方法作用是设置此Socket对象的SO_SNDBUF的值，即发送缓冲区大小，设置适当的发送缓冲区提高运行效率

2.getSendBufferSize()返回发送缓冲区的大小

####9.方法setSoLinger(boolean on, int linger)，getSoLinger()
1.setSoLinger(boolean on, int linger)方法作用为启用/禁用具有指定逗留时间的SO_LINGER，即TCP连接关闭后，延时设定的时间后再关闭，时间单位为秒

2.getSoLinger()返回设定的TCP关闭延时时间

3.当server调用close()方法后，server向client发送RST标记传给客户端重置连接，且server缓冲区内要发送的数据被丢弃，client在执行read()方法时出现异常

####10.方法public void setSoTimeout(int timeout)，public int getSoTimeout()
1.setSoTimeout(int timeout)：启用/禁用带有指定超时值的SO_TIMEOUT以毫秒为单位，在与此Socket关联的InputStream上调用read()将阻塞，如果read()阻塞时间超过timeout毫秒，将引发SocketTimeoutException

2.getSoTimeout()：获取超时时间

####11.方法public void setOOBInline(boolean on)，public boolean getOOBInline()，sendUrgentData(Data data)
1.setOOBInline(boolean on)方法：设定TCP紧急数据都将通过套接字输入流接收；禁用该选项时（默认），将悄悄丢弃紧急数据；OOB out of bound，带外数据；紧急数据发送时在正常数据之前
>紧急数据可以用于网络心跳检测，即检验互相是否连接上，ping

2.sendUrgentData(Data data)方法：Socket对象发送紧急数据data

>write()方法发送的数据先放入缓存，执行flush()方法后在发送；执行sendUrgentData(Data data)不会讲数据放入缓存，直接发送

####12.构造方法public Socket(String host, int port, InetAddress localAddr, int localPort)
创建一个套接字并将其连接到指定远程主机上的指定远程端口；host目的远程地址，port远程主机端口，localAddr本地网络地址对象，localPort本地绑定端口

####13.public void setKeepAlive(boolean on)，getKeepAlive()
1.setKeepAlive(boolean on)：设置为true，当对方在某个时间内没有发送任何数据过来，那么端点都会发送一个ACK探测包到对方，探测双方连接是否有效

2.getKeepAlive()：判断当前连接判是否执行检测连接方法

####14.public void setTrafficClass(int tc)，public int getTrafficClass()
1.setTrafficClass(int tc)：设定IP的服务类型，参数：0x02（IPTOS_LOWCOST），成本低；0x04（IPTOS_RELIABILITY），高可靠性；0x08（IPTOS_THROUGHPUT），最高吞吐量；0x10（IPTOS_LOWDELAY），最小延迟

##4.基于UDP的Socket通讯
User Datagram Protocol用户数据报协议，不可靠容易丢包，包发送无序；传输的包不一定走的是同一条路造成乱序

####DatagramSocket类
UDP协议两端都使用DatagramSocket类构建套接字对象
	
	public class Server {
		public static void main(String[] args) throws IOException {
			DatagramSocket serverSocket = new DatagramSocket(8088);
			byte[] byteArray = new byte[12];
			DatagramPacket packet = new DatagramPacket(byteArray, 10);
			serverSocket.receive(packet);
			serverSocket.close();
			System.out.println("包中数据的长度：" + packet.getLength());
			System.out.println(new String(packet.getData()));
		}
	}
	
	public class Client {
		public static void main(String[] args) throws IOException {
			DatagramSocket clientSocket = new DatagramSocket();
			clientSocket.connect(new InetSocketAddress("127.0.0.1", 8088));
			String data = "1234567";
			byte[] charArray = data.getBytes();
			DatagramPacket datagramPacket = new DatagramPacket(charArray,
					charArray.length);
			clientSocket.send(datagramPacket);
			clientSocket.close();
		}
	}

一个UDP包最大长度为2^16-1，65535；其中包含IP协议头20 byte，UDP协议头8 byte，UDP传输数据最大长度65507；如果传输的数据大意65507，则在发送端就出现异常

使用setBroadcast(boolean on)设置是否实现广播，使用socket.connect(InetAddress.getByName("192.168.0.255"), 7777)，使用IP通配符192.168.0.255通配0网段下0-255所有的IP

使用socket.joinGroup(InetAddress.getByName("224.0.0.5"))实现UDP组播

如果发送数据包，可以不调用joinGroup()方法加入多播组；如果是接收数据包，则必须调用joinGroup()方法加入多播组