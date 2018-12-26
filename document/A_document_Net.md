#第三章 获取网络设备信息
数据通讯使用最多的HTTP协议，为BS架构的数据通讯协议，底层技术为Socket

##1.类NetworkInterface的常用方法
类NetworkInterface表示一个由名称和分配给此接口的IP地址列表组成的网络接口，也就是NetworkInterface类包含网络接口名称与IP地址列表

想要取得NetworkInterface对象，必须要通过getNetworkInterfaces()静态工厂方法获得

####1.getName()-getDisplayName()-getIndex()-isUp()-isLoopback()
1.getName()：取得网络设备在操作系统中的名称

2.getDisplayName()：取得设备在操作系统中的显示名称

3.getIndex()：获得网络接口的索引

4.isUp()：判断网络接口是否已经开启并正常工作与运行

5.isLoopback()：判断该网路接口是否为127.0.0.1，回环接口

	public class Run {
		public static void main(String[] args) throws SocketException {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface nextElement = networkInterfaces.nextElement();
				System.out.println("getName=" + nextElement.getName());
				System.out
						.println("getDisplayName=" + nextElement.getDisplayName());
				System.out.println("getIndex=" + nextElement.getIndex());
				System.out.println("isUp=" + nextElement.isUp());
				System.out.println("isLoopback=" + nextElement.isLoopback());
				System.out.println();
				System.out.println();
			}
		}
	}

####2.int getMTU()
返回MTU大小，在网络传输中是以数据包为基本传输单位，可以使用MTU（Maximum Transmission Unit）最大传输单元来规定网络传输最大数据包的大小，单位为byte字节

####3.getSubInterfaces()-isVirtual()-getParent()
1.getSubInterfaces()
>取得子接口，子接口的作用是在不添加新的物理网卡的基础上，基于原有的网络接口设备再创建出一个虚拟的网络接口设备进行通讯，可以理解成为一个由软件模拟的网卡。Windows不支持子接口，而Linux中支持

>如果父接口关闭，子接口也会关闭

2.isVirtual()
>判断当前网路接口是否为虚拟子接口

####4.getHardwareAddress()
>获得网卡的硬件地址，MAC（Media Access Control）,每一个网卡拥有一个唯一的ID，即MAC

>windows下 cmd ipconfig -all来查看MAC地址

####5.Enumeration<InetAddress> getInetAddress()方法与InetAddress类的使用
>getInetAddresses()：获得绑定到此网络接口的InetAddress列表

>InetAddress类可以表示成互联网协议IP地址，通过使用InetAddress对象中的若干方法来获取该IP地址相关信息，一个网络接口可以使用多个IP地址，该类只能使用静态方法来实现对象的创建

InetAddress方法
>1.getCanonicalHostName()：获取此IP地址的完全限定域名，指主机名加上全路径

>2.getHostName()：获取此IP地址的主机名

>3.getHostAddress()：返回IP地址字符串

>4.getAddress()返回此InetAddress对象的原始IP地址，返回byte[]数组

>5.static getLocalHost()：返回本地主机的IP地址信息，InetAddress对象；如果本机拥有多个IP，则getLocalHost()方法只返回下标为0的第一个IP

>6.static getLoopbackAddress()：返回回环的IP地址信息，InetAddress对象；

>7.static InetAddress getByName(String host)：按照给定的主机名，返回主机的IP，InetAddress对象；参数host可以是计算机名，IP地址，也可以是域名

>8.static InetAddress[] getAllByName(String host)：在给定的主机名的情况下，根据系统上配置的名称服务返回其IP地址所组成的数组

>9.static InetAddress getByAddress(byte[] addr)：在给定原始IP地址的情况下，返回InetAddress对象；输入参数为byte数组

>10.static InetAddress getByAddress(String host, byte[] addr)：在给定的IP， host地址的情况下，返回InetAddress对象；输入参数为byte数组与host地址

####6.方法getInterfaceAddresses()与InterfaceAddress类的使用
>public List<InterfaceAddress> getInterfaceAddresses()的作用是获取网络接口的InterfaceAddress列表

>InterfaceAddress类对应网络接口的信息，一个InetAddress可以对应多个InterfaceAddress

>getAddress():返回此InterfaceAddress的InetAddress

>getBroadcast()：返回此InterfaceAddress广播地址的InetAddress，只有IPv4有广播地址，IPv6没有广播地址

>getNetworkPrefixLength()：返回子网掩码

>boolean isPointToPoint()：判断当前网络设备是否为点对点，通过拨号或专线方式建立

>boolean supportsMulticast()：判断当前网络设备是否支持组播

##2.类NetworkInterface的静态方法
1.public static NetworkInterface getByIndex(int index)
>根据指定的索引取得NetworkInterface对象

2.public static NetworkInterface getByName(String name)
>根据指定的NetworkInterface的name名称来获取NetworkInterface对象

3.public static NetworkInterface getByInetAddress(InetAddress addr)
>根据指定的InetAddress对象获取NetworkInterface，如果指定的IP地址绑定到多个网络接口，则不确定返回哪个接口

>Linux bonding的含义是将多个物理的网卡抽象成一个虚拟网卡，能够提升网络吞吐量

