#Future Stream
Java多线程创建流任务

##Future

在Java中，创建线程一般有两种方式，一种是继承Thread类，一种是实现Runnable接口

然而，这两种方式的缺点是在线程任务执行结束后，无法获取执行结果。我们一般只能采用共享变量或共享存储区以及线程通信的方式实现获得任务结果的目的。

不过，Java中，也提供了使用Callable和Future来实现获取任务结果的操作。Callable用来执行任务，产生结果，而Future用来获得结果。

实例1：代码计算1到10000的和，分为两个线程计算，前一个计算1-5000， 后一个计算5001-10000


任务类

	public class AddNumberTask implements Callable<Integer> {
		private int start;
		private int end;
	
		public AddNumberTask(int start, int end) {
			this.start = start;
			this.end = end;
		}
	
		@Override
		public Integer call() throws Exception {
			int totalSum = 0;
			for (int i = start; i <= end; i++) {
				totalSum += i;
			}
			return totalSum;
		}
	}

测试类
	
	public class FutureTest {
		public static void main(String[] args) throws InterruptedException,
				ExecutionException {
			long start = System.currentTimeMillis();
			ExecutorService executor = Executors.newCachedThreadPool();
			ArrayList<Future<Integer>> resultList = new ArrayList<>();
	
			// 创建并提交任务1
			AddNumberTask task1 = new AddNumberTask(1, 5000);
			Future<Integer> future1 = executor.submit(task1);
			resultList.add(future1);
	
			// 创建并提交任务2
			AddNumberTask task2 = new AddNumberTask(5001, 10000);
			Future<Integer> future2 = executor.submit(task2);
			resultList.add(future2);
	
			executor.shutdown();
	
			int total = 0;
	
			for (Future<Integer> future : resultList) {
				while (true) {
					if (future.isDone() && !future.isCancelled()) {
						int sum = future.get();
						total += sum;
						break;
					}
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("total sum is " + total);
			System.out.println("time:" + (end - start));
		}
	}

实例2：假如你突然想做饭，但是没有厨具，也没有食材。网上购买厨具比较方便，食材去超市买更放心。

实现分析：在快递员送厨具的期间，我们肯定不会闲着，可以去超市买食材。所以，在主线程里面另起一个子线程去网购厨具。
	
	public class FutureCook {

	    public static void main(String[] args) throws InterruptedException, ExecutionException {
	        long startTime = System.currentTimeMillis();
	        // 第一步 网购厨具
	        Callable<Chuju> onlineShopping = new Callable<Chuju>() {
	
	            @Override
	            public Chuju call() throws Exception {
	                System.out.println("第一步：下单");
	                System.out.println("第一步：等待送货");
	                Thread.sleep(5000);  // 模拟送货时间
	                System.out.println("第一步：快递送到");
	                return new Chuju();
	            }
	            
	        };
	        FutureTask<Chuju> task = new FutureTask<Chuju>(onlineShopping);
	        new Thread(task).start();
	        // 第二步 去超市购买食材
	        Thread.sleep(2000);  // 模拟购买食材时间
	        Shicai shicai = new Shicai();
	        System.out.println("第二步：食材到位");
	        // 第三步 用厨具烹饪食材
	        if (!task.isDone()) {  // 联系快递员，询问是否到货
	            System.out.println("第三步：厨具还没到，心情好就等着（心情不好就调用cancel方法取消订单）");
	        }
	        Chuju chuju = task.get();
	        System.out.println("第三步：厨具到位，开始展现厨艺");
	        cook(chuju, shicai);
	        
	        System.out.println("总共用时" + (System.currentTimeMillis() - startTime) + "ms");
	    }
	    
	    //  用厨具烹饪食材
	    static void cook(Chuju chuju, Shicai shicai) {}
	    
	    // 厨具类
	    static class Chuju {}
	    
	    // 食材类
	    static class Shicai {}
	
	}

##Stream （流）
Stream不同于IO中的Stream，它提供了对集合操作的增强，极大的提高了操作集合对象的便利性，类似数据在数据库中的各种操作在内存中用Stream实现

新的API对所有的集合操作都提供了生成流操作的方法

Java8在Collection接口添加了Stream方法，可以将任何集合转换成一个Stream

Stream操作分为两类：处理，聚合
>1.处理操作：诸如filter，map等处理操作将Stream一层一层的进行抽离，返回一个Stream给下一层使用

>2.聚合操作：从最后一次Stream中生成一个结果

####filer过滤方法
	
	List<AccountInfo> selectByGender = mapper.selectByGender("7");
	Stream<AccountInfo> stream_1 = selectByGender.stream().filter(P -> P.getId() < 0);
	System.out.println(stream_1.count());

过滤掉List中Id小于0的元素

####map数据类型转换
	
	Stream<String> stream = Stream.of("I", "love", "you", "too");
	stream.map(str -> str.toUpperCase()).forEach(str -> System.out.println(str));
小写字符转大写

####distinct去除重复元素
	
	Stream<String> stream= Stream.of("I", "love", "you", "too", "too");
	stream.distinct().forEach(str -> System.out.println(str));

####flatMap提取子流
	
	Stream<List<Integer>> stream = Stream.of(Arrays.asList(1,2), Arrays.asList(3, 4, 5));
	stream.flatMap(list -> list.stream()).forEach(i -> System.out.println(i));
总流（1，2，3，4，5）提取出5个子流1，2，3，4，5

####max/min获取最大最小值
	Stream<Integer> stream= Stream.of(1,4,8,9,5);
	Optional<Integer> max = stream.max((e1,e2)->Integer.compare(e1, e2));
	System.out.println(max);

####collect聚合
reduce操作，未测试
	
	Map<Status, Map<String, List<Employee>>> map = emps.stream()  
        .collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((e) -> {  
            if(e.getAge() >= 60)  
                return "老年";  
            else if(e.getAge() >= 35)  
                return "中年";  
            else  
                return "成年";  
        })));  
      
    System.out.println(map); 

Stream并行

默认的Stream方法创建的是一个串行流，有两种方法可以创建并行流

>1.调用Stream对象的parallel

>2.创建流的时候调用parallelStream方法
