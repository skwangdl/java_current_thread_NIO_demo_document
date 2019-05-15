#MD5
####Message Digest Algorithm MD5 消息摘要算法第五版
>计算机安全领域广泛使用的一种散列函数，用以提供消息的完整性保护， 又译摘要算法，哈希算法

MD5算法具有以下特点：

1. 压缩性：任意长度的数据，算出的MD5值长度都是固定的

2. 容易计算:从原数据计算出MD5值较容易

3. 抗修改性：对原数据进行的任何改动，所得到的MD5值都有很大区别

4. 强碰撞性:已知原数据和其MD5值，想找到一个具有相同的MD5值的数据（伪造数据）是非常困难的

>MD5的作用：让大容量信息在用数字签名软件签署私人秘钥前被"压缩"成一种保密格式，把任意长度的字节串变换成一定长度的十六进制数字符串

>其他的加密算法还有：SHA-1,RIPEMD,Haval等

>MD5可以被破解，查分攻击，碰撞算法等

MD5加密过程：MD5以512位分组来处理输入的信息，且每一分组又被划分为16个32位子分组，经过了一系列的处理后，算法的输出由四个32位分组组成，将这四个32位分组级联后将生成一个128位散列值

1.填充 2.初始化变量 3.处理分组数据 4.输出

###SHA
SHA-1与MD5类似，但有一下不同：
1. 对强行攻击的安全性，SHA-1摘要比MD5摘要长32位， MD5摘要长128位，SHA-1 160位

2. SHA-1不易受密码分析攻击

3. SHA-1运行速度较慢

##MD5实现
####数字处理工具类
	public class NumUtil {
		//32位二进制字符串转long
		public static long stringToLong(String binaryString){
			char[] charArray = binaryString.toCharArray();
			long temp = 0;
			for(int i = 0; i < charArray.length; i ++){
				String s = String.valueOf(charArray[charArray.length - i - 1]);
				Integer m = Integer.valueOf(s);
				if(m == 0){
					continue;
				}else if(m == 1){
					temp = temp + power(i + 1);
				}
			}
			return temp;
		}
		
		//求2次幂数
		public static int power(int b){
			int result = 1;
			for(int i = 0; i < b; i ++){
				result = result * 2;
			}
			return result;
		}
	}
####MD5工具类
	public class MD5Util {
		NumUtil numUtil = new NumUtil();
		
		// 步骤1：处理原文
		public String disposeData(String data) {
			Map<String, String> map = this.StringToBinaryAndDec(data);
			String binarydata = map.get("binary");
			int length = binarydata.length();
			if (length % 512 > 448) {
				int num = 512 - binarydata.length() + 447;
				binarydata = binarydata + "1";
				for (int i = 0; i < num; i++) {
					binarydata = binarydata + "0";
				}
			} else if (length % 512 < 448) {
				int num = 447 - length;
				binarydata = binarydata + "1";
				for (int i = 0; i < num; i++) {
					binarydata = binarydata + "0";
				}
			}
			Map<String, String> map1 = this.StringToBinaryAndDec(length + "");
			String length_binary = map1.get("binary");
			int num1 = 64 - length_binary.length();
			String temp_str = "";
			for (int i = 0; i < num1; i++) {
				temp_str = temp_str + "0";
			}
			length_binary = temp_str + length_binary;
			binarydata = binarydata + length_binary;
			return binarydata;
		}
	
		//步骤2：设置加密初始值,循环处理,返回MD5值
		public String getMD5Value(String value){
			long A = 0x01234567;
			long B = 0x89ABCDEF;
			long C = 0xFEDCBA98;
			long D = 0x76543210;
			long[][] resolve = this.resolve(value, value.length()/512);
			long[] ki = this.getKi();
			Map<String, Long> map = new HashMap<String, Long>();
			//加密主循环
			for(int i = 0; i < value.length()/512; i ++){
				map = this.circulation(A, B, C, D, resolve[i], ki);
			}
			String A_str = Long.toHexString(map.get("A"));
			String B_str = Long.toHexString(map.get("B"));
			String C_str = Long.toHexString(map.get("C"));
			String D_str = Long.toHexString(map.get("D"));
			return A_str + B_str + C_str + D_str;
		}
		
		//加密子循环
		private Map<String, Long> circulation(long A, long B, long C, long D, long[] Mi, long Ki[]){
			int num = 0;
			for(int j = 0; j < 16; j ++){
				long temp = this.function_F(B, C, D);
				temp = A + temp;
				temp = temp + Mi[j];
				temp = temp + Ki[num];
				num ++;
				temp = temp << 3;
				A = D;
				D = C;
				C = B;
				B = temp;
			}
			for(int j = 0; j < 16; j ++){
				long temp = this.function_G(B, C, D);
				temp = A + temp;
				temp = temp + Mi[j];
				temp = temp + Ki[num];
				num ++;
				temp = temp << 3;
				A = D;
				D = C;
				C = B;
				B = temp;
			}
			for(int j = 0; j < 16; j ++){
				long temp = this.function_H(B, C, D);
				temp = A + temp;
				temp = temp + Mi[j];
				temp = temp + Ki[num];
				num ++;
				temp = temp << 3;
				A = D;
				D = C;
				C = B;
				B = temp;
			}
			for(int j = 0; j < 16; j ++){
				long temp = this.function_I(B, C, D);
				temp = A + temp;
				temp = temp + Mi[j];
				temp = temp + Ki[num];
				num ++;
				temp = temp << 3;
				A = D;
				D = C;
				C = B;
				B = temp;
			}
			Map<String, Long> map = new HashMap<String, Long>();
			map.put("A", A);
			map.put("B", B);
			map.put("C", C);
			map.put("D", D);
			return map;
		}
		
		//分解字符串方法
		public long[][] resolve(String value, int num){
			char[] charArray = value.toCharArray();
			String temp1 = "";
			long[][] result = new long[num][16];
			for(int m = 0; m < num; m ++){
				for(int i = 0; i < 16; i ++){
					for(int j = i; j < 32 + i; j ++){
						temp1 = temp1 + charArray[j];
					}
					result[m][i] = numUtil.stringToLong(temp1);				
				}
			}
			return result;
		}
		
		//MD5官方非线性函数
		private long function_F(long X, long Y, long Z){
			return (X & Y)|((~X) & Z);
		}
		private long function_G(long X, long Y, long Z){
			return (X & Z)|((~Y) & Z);
		}
		private long function_H(long X, long Y, long Z){
			return X^Y^Z;
		}
		private long function_I(long X, long Y, long Z){
			return Y^(X|(~Z));
		}
		
		public Map<String, String> StringToBinaryAndDec(String context) {
			String result_binary = "";
			String result_dec = "";
			Map<String, String> map = new HashMap<String, String>();
			int max = context.length();
			for (int i = 0; i < max; i++) {
				char c = context.charAt(i);
				int b = (int) c;
				String binaryString = Integer.toBinaryString(b);
				result_dec = result_dec + b;
				result_binary = result_binary + binaryString;
			}
			map.put("binary", result_binary);
			map.put("dec", result_dec);
			return map;
		}
		
		private long[] getKi(){
			long[] values = {
			    0xd76aa478,0xe8c7b756,0x242070db,0xc1bdceee,
			    0xf57c0faf,0x4787c62a,0xa8304613,0xfd469501,0x698098d8,
			    0x8b44f7af,0xffff5bb1,0x895cd7be,0x6b901122,0xfd987193,
			    0xa679438e,0x49b40821,0xf61e2562,0xc040b340,0x265e5a51,
			    0xe9b6c7aa,0xd62f105d,0x02441453,0xd8a1e681,0xe7d3fbc8,
			    0x21e1cde6,0xc33707d6,0xf4d50d87,0x455a14ed,0xa9e3e905,
			    0xfcefa3f8,0x676f02d9,0x8d2a4c8a,0xfffa3942,0x8771f681,
			    0x6d9d6122,0xfde5380c,0xa4beea44,0x4bdecfa9,0xf6bb4b60,
			    0xbebfbc70,0x289b7ec6,0xeaa127fa,0xd4ef3085,0x04881d05,
			    0xd9d4d039,0xe6db99e5,0x1fa27cf8,0xc4ac5665,0xf4292244,
			    0x432aff97,0xab9423a7,0xfc93a039,0x655b59c3,0x8f0ccc92,
			    0xffeff47d,0x85845dd1,0x6fa87e4f,0xfe2ce6e0,0xa3014314,
			    0x4e0811a1,0xf7537e82,0xbd3af235,0x2ad7d2bb,0xeb86d391
			};
			return values;
		}
	}

####测试类
	public class MD5Test {
		@Test
		public void run(){
			MD5Util util = new MD5Util();
			String disposeData = util.disposeData("123");
			System.out.println(disposeData.length());
			this.show(disposeData);
			String md5Value = util.getMD5Value(disposeData);
			System.out.println(md5Value);
		}
		
		private void show(Object obj){
			System.out.println(obj);
		}
	}


##MD5破解
>MD5值是有穷的，原文是无穷的，即一个MD5对应了多个原文，如X的MD5值为M，破解M得到Y，即可以看做MD5破解或者碰撞

>破解方法：1.暴力枚举（时间交换空间）2.字典（空间交换时间）3. 彩虹表

####彩虹表
>针对特定算法，尤其是不对称算法进行有效破解的一种方法，其过程就是建立一个源数据与加密数据之间对应的Hash表，这样在获得加密数据后，通过比较，查询或者一定的运算，可以快速定位源数据

>彩虹表的根本原理就是组合了暴力枚举与字典，在两者之中取折中

>彩虹表下载地址：http://ophcrack.sourceforge.net/tables.php

####彩虹表原理
两个基本函数：

1. H(X) 生成信息摘要的哈希函数，比如MD5，SHA256
2. R(X) 从信息摘要转换成另一个字符串的衰减函数
>其中R(X)的值域是H（X）的定义域，H(X)是R(X)的值域，但是R(X)不是H(X)的反函数
	
通过交替运算H和R若干次，可以形成一个原文与哈希值的链条，假设原文是 aaaaaa， 哈希值长度32bit，那么哈希链表如下：

aaaaaa : H(aaaaaa) -> 281DAF40 : R(281DAF40) -> sgfnyd : H(sgfnyd) -> 920ECF10 : R(920ECF10)-> kiebgt

>假设H()和R()的交替重复K次， 那么链条长度就是2K+1.只需要把链条的首端与末端存入哈希表中，

首端 		末端
aaaaaa		kiebgt

破解过程如下：如给定信息摘要：920ECF10，对其进行R（）运算，如得到R(920ECF10)=kiebgt，说明摘要920ECF10的原文有可能在aaaaaa到kiebgt的这个链条中

从aaaaa开始，重新交替运算R()与H()，看一看摘要920ECF10是否是其中一次H()的结果，如果是则这次H()的计算值就是920ECF10的原文

>如果给定的摘要值经过一次R()运算，结果在哈希表中找不到，则继续交替H()，R()运算，知道第K次为止
	
	找到首端节点aaaaaa
	920ECF10  ————>   kiebgt				首端			末端
			   R()						aaaaaa		kiebgt
	
	找到链条中的哈希值920ECF10和前置节点sgfnyd
	aaaaaa   ————>281DAF40   ————>   sgfnyd   ————>920ECF10   ————>kiebgt
			H()				R()				  H()			  R()
>假设K=10那么存储空间只有全量字典的十分之一，代价则是破解一个摘要的运算次数也提高了十倍

######当K值较大时，哈希链很长，一旦两条不同的哈希链在某个节点出现碰撞既值相同，后面所有的值或变成一摸一样，造成冗余

######把原先的R()函数该进程R1()到Rk()一共K个衰减函数，如此碰撞只会发生在同一级运算，减小存储重复的几率，每一步运算都是一种不同函数