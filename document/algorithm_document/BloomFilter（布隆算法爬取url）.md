#BloomFilter(布隆算法)
以Bitmap集合为基础的排重算法，使用在url的排重，垃圾邮箱地址的过滤等

爬虫：通过种子url来顺藤摸瓜，爬取出网站关联的所有子网页，存入网页库

####如何实现爬虫时url的去重复：
1.使用HashSet 每一个URL字符作为HashSet的Key，可以利用HashSet唯一性来对url进行去重，但是URL数量巨大，占用资源太多

2.可以使用Bitmap，但是字符串的HashCode会有重复，不同url的HashCode很可能相同

##BloomFilter过程

1.把第一个url按照三种Hash算法，分别生成三个不同的hash值

						www.aaa.com
				HashA		HashB		HashC
				5			17			9
2.把第二个url也按照三种hash算法，分别生成三个不同的Hash值

						www.bbb.com
				HashA		HashB		HashC
				10			12			9
3.依次比较每一个hash结果，只有当全部结果都相等时，才判定两个url相同
						
4.BloomFilter算法会把每一个Hash结果都映射到同一个Bitmap上，创建一个空的Bitmap集合
		
	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0
	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	17	18	19	20

5.把url按照三种hash算法，分别生成三个不同的url，以www.aaa.comw为例，生成的hash为5,17,9，则存入Bitmap的结果如下
	
	0	0	0	0	1	0	0	0	1	0	0	0	0	0	0	0	1	0	0	0
	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	17	18	19	20

6.第二个URL（www.bbb.com）生成的三个Hash为10,12,9，分别判断三个值在Bitmap对应位置是否为1，只要不同时为1，就认为URL没有重复，将第二个URL生成的值存入Bitmap
	
	0	0	0	0	1	0	0	0	1	1	0	1	0	0	0	0	1	0	0	0
	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	17	18	19	20
	
BitmapFilter存在Hash冲突的情况，为了减小误判的几率，可以让Bitmap空间扩大，让URL做更多的hash值（一般取8次hash）,在空间和准确率上作出取舍

如果让每一个Hash结果对应一个独立的Bitmap，占用的空间也会相应的加倍，反而不如hashset

对于爬虫来讲，可以容许极少量的url误判，可以加入一个白名单，专门存储被误判的正常url

##Hash算法
Hash表， 也叫做散列表，是依据关键码值而直接进行访问的数据结构，基于告诉存取设计，以空间换时间，通过把关键码值映到表中一个位置来访问记录，以加快查找速度，这个映射函数成为散列函数，存放记录的数组称为散列表或hash表

##BloomFilter实现
	
	public class Demo {
		int[] bitmap = new int[10000000];
		@Test
		public void test(){
			String url_1 = "www.baidu.com";
			String url_2 = "www.google.com";
			String url_3 = "www.oracle.com";
			boolean exists = this.isExists(url_1);
			System.out.println(exists);
			boolean exists2 = this.isExists(url_2);
			System.out.println(exists2);
			boolean exists3 = this.isExists(url_3);
			System.out.println(exists3);
			boolean exists4 = this.isExists(url_1);
			System.out.println(exists4);
		}
		
		private boolean isExists(String url){
			int hash_1 = this.getHash_1(url);
			int hash_2 = this.getHash_2(url);
			int hash_3 = this.getHash_3(url);
			if(bitmap[hash_1] == 1 && bitmap[hash_2] == 1 && bitmap[hash_3] == 1){
				return true;
			}else{
				bitmap[hash_1] = 1;
				bitmap[hash_2] = 1;
				bitmap[hash_3] = 1;
				return false;
			}
		}
		
		private int getHash_1(String str){
			byte[] encrypt = AES.encrypt(str, "DSERFSAKCXD");
			String temp = "";
			String temp_1 = "";
			for(int i = 0; i < encrypt.length; i ++){
				if(Integer.valueOf(String.valueOf(encrypt[i])) > 0){
					temp = temp + encrypt[i];
				}
			}
			char[] charArray = temp.toCharArray();
			for(int i = 0; i < charArray.length; i ++){
				String binaryString = Integer.toBinaryString(Integer.valueOf(String.valueOf(charArray[i])));
				temp_1 = temp_1 + binaryString;
			}
			char[] charArray1 = temp.toCharArray();
			for(int i = 0; i < charArray1.length; i ++){
				String binaryString = Integer.toBinaryString(Integer.valueOf(String.valueOf(charArray1[i])));
				temp_1 = temp_1 + binaryString;
			}
			char[] charArray2 = temp_1.toCharArray();
			int value = 0;
			for(int i = 0; i < 16; i ++){
				if(String.valueOf(charArray2[i]).equals("1")){
					int power = this.power(i);
					value = value + power;
				}
			}
			return value;
		}
		
		private int getHash_2(String str){
			char[] charArray = SHA_1.sha1(str).toCharArray();
			String temp = "";
			for(int i = 0; i < charArray.length; i ++){
				if(String.valueOf(charArray[i]).equals("a")){
					temp = temp + "10";
				}else if(String.valueOf(charArray[i]).equals("b")){
					temp = temp + "11";
				}else if(String.valueOf(charArray[i]).equals("c")){
					temp = temp + "12";
				}else if(String.valueOf(charArray[i]).equals("d")){
					temp = temp + "13";
				}else if(String.valueOf(charArray[i]).equals("e")){
					temp = temp + "14";
				}else if(String.valueOf(charArray[i]).equals("f")){
					temp = temp + "15";
				}else{
					temp = temp + charArray[i];
				}
			}
			String temp_1 = "";
			char[] charArray1 = temp.toCharArray();
			for(int i = 0; i < charArray1.length; i ++){
				String binaryString = Integer.toBinaryString(Integer.valueOf(String.valueOf(charArray1[i])));
				temp_1 = temp_1 + binaryString;
			}
			char[] charArray2 = temp_1.toCharArray();
			int value = 0;
			for(int i = 0; i < 16; i ++){
				if(String.valueOf(charArray2[i]).equals("1")){
					int power = this.power(i);
					value = value + power;
				}
			}
			return value;
		}
		
		private int getHash_3(String str){
			String md5 = Md5Util.getMd5(str);
			char[] charArray = md5.toCharArray();
			String temp = "";
			for(int i = 0; i < charArray.length; i ++){
				if(String.valueOf(charArray[i]).equals("a")){
					temp = temp + "10";
				}else if(String.valueOf(charArray[i]).equals("b")){
					temp = temp + "11";
				}else if(String.valueOf(charArray[i]).equals("c")){
					temp = temp + "12";
				}else if(String.valueOf(charArray[i]).equals("d")){
					temp = temp + "13";
				}else if(String.valueOf(charArray[i]).equals("e")){
					temp = temp + "14";
				}else if(String.valueOf(charArray[i]).equals("f")){
					temp = temp + "15";
				}else{
					temp = temp + charArray[i];
				}
			}
			String temp_1 = "";
			char[] charArray1 = temp.toCharArray();
			for(int i = 0; i < charArray1.length; i ++){
				String binaryString = Integer.toBinaryString(Integer.valueOf(String.valueOf(charArray1[i])));
				temp_1 = temp_1 + binaryString;
			}
			char[] charArray2 = temp_1.toCharArray();
			int value = 0;
			for(int i = 0; i < 16; i ++){
				if(String.valueOf(charArray2[i]).equals("1")){
					int power = this.power(i);
					value = value + power;
				}
			}
			return value;
		}
		
		private int power(int b){
			int value = 2;
			if(b == 0){
				return 1;
			}else{
				for(int i = 0; i < b; i ++){
					value = value * 2;
				}
				return value;
			}
		}
	}