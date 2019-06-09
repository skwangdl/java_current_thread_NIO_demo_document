#Bitmap（位图算法）

>如果数据库字段过多，拼接的sql过长，严重影响数据库查询性能，利用Bitmap

>运用内存中连续的二进制位（bit），用于对大量整型数据做去重和查询，倒排索引

>如果用HashSet或Hashmap储存，每一个ID都要存成int， 占32bit， 一个ID在Bitmap中只占一个bit，内存节省32倍

>每一个属性就是一个bitmap,bitmap算法不能用于非运算（可以利用一个全量的Bitmap进行非运算）， 位运算效率高

##Bitmap实现（EWAHCompressedBitmap） maven依赖

	<dependency>
		<groupId></groupId>
		<artifactId>JavaEWAH</artifactId>
		<version>1.1.0</version>
	</dependency>

##Bitmap举例说明
原始表

	ID			姓名			年龄				职业				专业
	1			老王			10				 工人			     机械
	2			老王			11				 保洁				 电气
	3			老张			11				 主任				 机械
	4			老钱			13				 主任				 信息
Bitmap表

	姓名   	  Bit		年龄		Bit			职业		Bit				专业		Bit
	老王		1 1 0 0		10		1 0 0 0		工人		1 0 0 0			机械		1 0 1 0
	老张		0 0 1 0		11		0 1 1 0		保洁		0 1 0 0			电气		0 1 0 0
	老钱		0 0 0 1		13		0 0 0 1		主任		0 0 1 1			信息		0 0 0 1

##Bitmap实现
entity
	public class Data {
		private int id;
		private String name;
		private int age;
		private String job;
		private String major;
	
		public int getId() {
			return id;
		}
	
		public void setId(int id) {
			this.id = id;
		}
	
		public String getName() {
			return name;
		}
	
		public void setName(String name) {
			this.name = name;
		}
	
		public int getAge() {
			return age;
		}
	
		public void setAge(int age) {
			this.age = age;
		}
	
		public String getJob() {
			return job;
		}
	
		public void setJob(String job) {
			this.job = job;
		}
	
		public String getMajor() {
			return major;
		}
	
		public void setMajor(String major) {
			this.major = major;
		}
	
		public Data(int id, String name, int age, String job, String major) {
			super();
			this.id = id;
			this.name = name;
			this.age = age;
			this.job = job;
			this.major = major;
		}
	}

测试类
	public class BitmapDemo {
		public static void main(String[] args){
			Data d1 = new Data(1, "老王", 10, "工人", "机械");
			Data d2 = new Data(2, "老王", 11, "保洁", "电气");
			Data d3 = new Data(3, "老张", 11, "主任", "机械");
			Data d4 = new Data(4, "老钱", 13, "主任", "信息");
			
			Map<String, Integer> map_name = new HashMap<String, Integer>();
			Map<String, Integer> map_age = new HashMap<String, Integer>();
			Map<String, Integer> map_job = new HashMap<String, Integer>();
			Map<String, Integer> map_major = new HashMap<String, Integer>();
			
			map_name.put("老王", 0xc);
			map_name.put("老张", 02);
			map_name.put("老钱", 0x1);
			map_age.put("10", 0x8);
			map_age.put("11", 0x6);
			map_age.put("13", 0x1);
			map_job.put("工人", 0x8);
			map_job.put("保洁", 0x4);
			map_job.put("主任", 0x3);
			map_major.put("机械", 0xa);
			map_major.put("电气", 0x4);
			map_major.put("信息", 0x1);
			
			//查询姓名：老王， 职业：工人
			int integer1 = map_name.get("老王");
			int integer2 = map_job.get("工人");
			Integer temp = integer1 & integer2;
			System.out.println(Integer.toBinaryString(temp));
			System.out.println("第一位为1， 说明下标为1的value符合条件");
		}
	}
##Bitmap数据稀疏内存优化举例

######如果在一个很长的Bitmap里只存一两个用户，EWAHCompressedBitmap优化

>EWAH把BItmap存储于long数组当中，long数组的每一个元素都可以当做是64位的二进制数，也是整个Bitmap的子集，
这些子集叫做 Word

>Word分为两种，直接存储数据的叫做Literal Word, 简称LW， 存储跨度信息的叫做 Running Length Word 简称RLW

>当创建一个空的Bitmap时，初始只有4个word，随着数据的不断插入，word数量会随之扩充
	
	0000 0000B		0000 0000B		0000 0000B		0000 0000B
		0L				0L				0L				0L
		Word0			Word1			Word2			Word3
>Word0储存的是Bitmap的头信息，Word0为RLW， 当插入ID分别为1，4，64,129的用户：

	(跨度信息)		0001 0010B		0000 0001B		0000 0010B
						18L				1L				2L
	Word0				Word1			Word2			Word3
	
	0000 0000B		0000 0000B		0000 0000B		0000 0000B
		0				0				0				0
		Word4			Word5			Word6			Word7

>插入ID为400000的用户：

	400000+1 除以64商6250 余1 Word值为1L， Word4成为RLW Word5成为存储了400000的LW

	25769803776L		0001 0010B		0000 0001B		0000 0010B
						18L				1L				2L
	Word0				Word1			Word2			Word3
	
	8589947086L		0000 0001B		0000 0000B		0000 0000B
						1L				0				0
	Word4				Word5			Word6			Word7

>每一个RLW分成两部分，低32位表示当前Word横跨了多少个空Word，高32位表示当前RLW后方有多少个连续的LW

>Word0值二进制：11 000...000（32个） 后方有三个连续的LW 自身横跨0个Word

>Word4值二进制：1 0...0(19个)11000011001110 后方有1个连续LW自身横跨6247个Word

>当新数据插入的时候，依靠每一个RLW作为路标， 如果插入的ID刚好在RLW范围内，涉及原RLW的分裂

##Bitmap数据稀疏内存优化实现
	
	public class Word {
		private Long param;
	
		public Long getParam() {
			return param;
		}
	
		public void setParam(Long param) {
			this.param = param;
		}
	
		public Word(Long param) {
			super();
			this.param = param;
		}
	
		public Word() {
			super();
		}
		
	}
>Bitmap实现（位运算）

	public class BitmapTest {
		private Word[] result = {new Word(0L), new Word(0L)};
		private long l1 = 0L;
		private long l2 = 0L;
		
		public static void main(String[] args){
			Word[] expandBitmap = new BitmapTest().expandBitmap(135);
			for(int i = 0; i < expandBitmap.length; i ++){
				System.out.print(expandBitmap[i].getParam() + " ");
			}
			
		}

		public Word[] expandBitmap(int id){
			int a = id/64;
			int b = id%64;
			if(a != 0){
				long num1 = (long) Math.pow(2, b);
				long temp = l2 | num1;
				long temp1 = l1 | a;
				Word[] result_temp = {new Word(temp1), new Word(temp)};
				Word[] result1 = new Word[result_temp.length + result.length];
				System.arraycopy(result, 0, result1, 0, result.length);
				System.arraycopy(result_temp, 0, result1, result.length, result_temp.length);
				return result1;
			}else{
				long num1 = (long) Math.pow(2, b);
				long temp = result[result.length - 1].getParam() | num1;
				result[result.length - 1].setParam(temp);
				return result;
			}
		}
	
		public Word[] getResult() {
			return result;
		}
	
		public void setResult(Word[] result) {
			this.result = result;
		}
		
	}
	
	
	
	