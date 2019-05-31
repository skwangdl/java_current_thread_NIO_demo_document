#第六章 异步AIO的使用
异步IO模型

##1.AsynchronousFileChannel的API
类AsynchronousFileChannel使用open静态工厂方法构建实例，实例化后会创建一个异步文件通道，对文件进行读取，写入，操作等。可以对文件的一部分进行锁定，文件其他的部分还可以由其他通道操作，达到异步效果

####1.方法public final Future<FileLock> lock()
获取此通道文件的独占锁，该方法返回一个表示操作挂起结果的Future对象，Future对象的get()方法在成功完成时返回FileLock
	
	public class Run1 {
		public static void main(String[] args) throws IOException,
				InterruptedException, ExecutionException {
			AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
					Paths.get("e:\\abc.txt"), StandardOpenOption.WRITE);
			System.out.println(System.currentTimeMillis());
			Future<FileLock> future = fileChannel.lock();
			System.out.println(System.currentTimeMillis());
			FileLock fileLock = future.get();
			System.out.println(System.currentTimeMillis());
			fileLock.release();
			fileChannel.close();
		}
	}
	
	public class Run {
		public static void main(String[] args) throws IOException,
				InterruptedException, ExecutionException {
			AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
					Paths.get("e:\\abc.txt"), StandardOpenOption.WRITE);
			Future<FileLock> future = fileChannel.lock();
			FileLock fileLock = future.get();
			Thread.sleep(8000);
			fileLock.release();
			fileChannel.close();
		}
	}

如果一个线程获得了文件锁，另一个线程在获得该文件锁时被阻塞，一直等到文件锁释放后才能获得

####2.方法public final Future<FileLock> lock(long position. long size, boolean shared)

锁定该文件的特定区域，从position位置开始，向后size个字节，shared参数为是否为共享锁即读读共享

两个进程对同一个文件的锁定范围如果有重叠时会出现阻塞

####3.方法public abstract long size()，public boolean isOpen()
1.size()返回此通道文件的当前大小

2.isOpen()判断当前通道是否打开

####4.方法public final < A > void lock(A attachment, CompletionHandler<FileLock, ? super A> handler)
获取此通道文件的独占锁，此方法启动一个操作以获取此通道文件的给定对象

handler参数是在获取锁(或失败)时调用的CompletionHandler对象，回调函数	

CompletionHandler内的completed()方法在成功获得文件锁后调用，failed()方法在获得锁失败时调用，exc为异常信息对象，attachment为附加对象

	public class Run {
		public static void main(String[] args) throws IOException {
			AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
					Paths.get("e:\\abc.txt"), StandardOpenOption.WRITE);
			System.out.println("begin time=" + System.currentTimeMillis());
			fileChannel.lock("我是附加值", new CompletionHandler<FileLock, String>() {
				@Override
				public void completed(FileLock result, String attachment) {
					System.out.println(attachment);
					try {
						result.release();
						fileChannel.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				@Override
				public void failed(Throwable exc, String attachment) {
					System.out.println(attachment);
					System.out.println("getMessage=" + exc.getMessage());
				}
			});
			System.out.println("end time=" + System.currentTimeMillis());
		}
	}

####5.方法public abstract Future<Integer> read(ByteBuffer dst, long position)
从给定的文件位置开始，即方法内的参数position为文件位置，从该通道将字节序列读入给定的缓冲区；

如果给定的position大于文件的最大位置，则Future调用get()返回-1

####6.方法public abstract Future< Integer > write(ByteBuffer src, long position)

向此通道的文件内，从position位置开始，写入缓冲区src内的数据，可以自动对文件扩容
	
	public class Run1 {
		public static void main(String[] args) throws IOException,
				InterruptedException, ExecutionException {
			AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
					Paths.get("e:\\abc.txt"), StandardOpenOption.WRITE);
			Future<Integer> future = fileChannel.write(
					ByteBuffer.wrap("sfgdsfasd".getBytes()), 15);
			System.out.println(future.get());
			fileChannel.close();
		}
	}

##2.类AsynchronousServerSocketChannel AsynchronousSocketChannel API的使用
面向流的异步套接字通道，线程安全

类AsynchronousServerSocketChannel继承于NetworkChannel, AsynchronousChannel

该类不能直接实例化，需要静态工厂方法open()
	
	public class Server {
		public static void main(String[] args) throws IOException {
			AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel
					.open().bind(new InetSocketAddress(8088));
			serverSocketChannel.accept(null,
					new CompletionHandler<AsynchronousSocketChannel, Void>() {
						@Override
						public void completed(AsynchronousSocketChannel channel,
								Void attachment) {
							serverSocketChannel.accept(null, this);
							ByteBuffer buffer = ByteBuffer.allocate(10);
							Future<Integer> future = channel.read(buffer);
							try {
								System.out.println(new String(buffer.array(), 0,
										future.get()));
								channel.close();
							} catch (InterruptedException | ExecutionException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
	
						@Override
						public void failed(Throwable exc, Void attachment) {
							System.out.println(exc.getMessage());
						}
					});
			while (true) {
	
			}
		}
	}

	public class Client_1 {
		public static void main(String[] args) throws UnknownHostException, IOException {
			Socket socket = new Socket("127.0.0.1", 8088);
			OutputStream out = socket.getOutputStream();
			out.write("client_1".getBytes());
			out.flush();
			out.close();
		}
	}

以上代码先运行Server，在运行client；

server在serverSocketChannel.accept(null, this);处不阻塞，在future对象调用get()时阻塞；

当client连接进来时，当前线程继续向下执行业务，会另外启动一个线程在System.out.println(new String(buffer.array(), 0,future.get()));处等待

重复读出现异常ReadPendingException,重复写出现异常WritePendingException

使用AsynchronousServerSocketChannel进行写操作，调用write()方法后，只会将发送缓冲区内的数据写入,不能讲所有数据完全写入

如果对write()或read()方法指定超时时间参数，则超时后，会抛出异常InterruptedByTimeoutException

##2.同步，异步，阻塞，非阻塞的组合
1.普通的InputStream， OutputStream类属于同步阻塞，执行当前读写任务的一直是当前线程，而且读不到或者写不出去就一直是阻塞的状态，阻塞的意思就是方法不返回，直到读到或者写出数据为止

2.NIO属于同步非阻塞，一直由当前线程操作，但是读不到或者写不出数据时,方法就返回了继续执行之后的代码

3.同步与异步关注的是互相的消息通知，阻塞与非阻塞关注的是程序在等待调用结果时的状态；文件通道FileChannel永远是阻塞的

##3.大文件传输实例
实例存在问题，需要更改

	public class Server {
		public static void main(String[] args) throws Exception {
			ServerSocketChannel channel1 = ServerSocketChannel.open();
			channel1.configureBlocking(false);
			channel1.bind(new InetSocketAddress("localhost", 8088));
	
			Selector selector = Selector.open();
			channel1.register(selector, SelectionKey.OP_ACCEPT);
			boolean isRun = true;
			while (isRun == true) {
				selector.select();
				Set<SelectionKey> set = selector.selectedKeys();
				Iterator<SelectionKey> iterator = set.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					iterator.remove();
					if (key.isAcceptable()) {
						SocketChannel socketChannel = channel1.accept();
						socketChannel.configureBlocking(false);
						socketChannel.register(selector, SelectionKey.OP_WRITE);
					}
					if (key.isWritable()) {
						SocketChannel socketChannel = (SocketChannel) key.channel();
						FileInputStream file = new FileInputStream(
								"e:\\java_source\\solr-7.2.1.zip");
						FileChannel fileChannel = file.getChannel();
						ByteBuffer byteBuffer = ByteBuffer
								.allocateDirect(524288000);// 500MB空间
	
						while (fileChannel.position() < fileChannel.size()) {
							fileChannel.read(byteBuffer);
							byteBuffer.flip();
							while (byteBuffer.hasRemaining()) {
								socketChannel.write(byteBuffer);
							}
							byteBuffer.clear();
							System.out.println(fileChannel.position() + " "
									+ fileChannel.size());
						}
						System.out.println("结束写操作");
						socketChannel.close();
					}
				}
			}
			channel1.close();
		}
	}
	
	public class Client {
		public static void main(String[] args) throws Exception {
			SocketChannel channel1 = SocketChannel.open();
			channel1.configureBlocking(false);
			channel1.connect(new InetSocketAddress("localhost", 8088));
			Selector selector = Selector.open();
			channel1.register(selector, SelectionKey.OP_CONNECT);
			boolean isRun = true;
			while (isRun == true) {
				System.out.println("begin selector");
				if (channel1.isOpen() == true) {
					selector.select();
					System.out.println("  end selector");
					Set<SelectionKey> set = selector.selectedKeys();
					Iterator<SelectionKey> iterator = set.iterator();
					while (iterator.hasNext()) {
						SelectionKey key = iterator.next();
						iterator.remove();
						if (key.isConnectable()) {
							while (!channel1.finishConnect()) {
							}
							channel1.register(selector, SelectionKey.OP_READ);
						}
						if (key.isReadable()) {
							ByteBuffer byteBuffer = ByteBuffer.allocate(50000);
							int readLength = channel1.read(byteBuffer);
							byteBuffer.flip();
							long count = 0;
							while (readLength != -1) {
								count = count + readLength;
								System.out.println("count=" + count
										+ " readLength=" + readLength);
								readLength = channel1.read(byteBuffer);
								byteBuffer.clear();
							}
							System.out.println("读取结束");
							channel1.close();
						}
					}
				} else {
					break;
				}
			}
		}
	}