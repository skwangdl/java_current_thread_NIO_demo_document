#AES加密算法
AES算法是典型的对称加密算法，摘要算法（MD5, SHA）是不可逆的，它的主要作用是对信息一致性和完整性的校验，对称加密算法是可逆的，主要作用是保证私密信息不被泄露

AES（Advanced Encryption Standard）,是DES算法的替代者，当前最流行的对称加密算法之一。

###密钥
AES支持三种长度的密钥：128Bit， 192Bit， 256Bit， 平时的AES128，AES192,AES256就是指不同长度密钥的AES
从安全性看AES256安全性最高，AES128性能最高。

###填充
	
							全量明文
		明文块0		明文块1		明文块2		明文块3
		128Bit
							AES加密器<——————密钥		
		密文块0		密文块1		密文块2		密文块3
		128Bit
AES在对明文进行加密时，先把明文拆分成几个独立的明文快，每块长128Bit，每个经过AES加密器处理成密文块，最后拼接成最终的AES加密结果，如果最后一块明文块不足128Bit，则需要对其进行填充

不同的语言对填充有不同的实现：

1.不做任何填充，但要求明文必须是16字节的整数 （NoPadding）

2.如果明文块少于16个字节，在明文块末尾补足相应数量的字符，每个字节的值等于缺少的字符数（PKCS5Padding）

3.如果明文块少于16个字节，在明文块末尾补足相应数量的字符，最后一位为缺少的字符数，其他随机产生字符填充（ISO10126Padding）

###模式

AES的工作模式体现在将明文快加密成米密文块的处理过程

1.CBC模式（电码本模式）（Electronic Codebook Book）
2.ECB模式（默认）（密码分组链接模式）(Cipher Block Chaining)
3.CTR模式（计算器模式）（Counter）
4.CFB模式（密码反馈模式）（Cipher FeedBack）
5.OFB模式（输出反馈模式）（Output FeedBack）

java拥有封装好的AES包，在调用时，表面上使用的Key不是真正的加密密钥，而是用来生成密钥，如果明文长度是16字节的整数倍，除了NoPadding外，其他填充方式都会填充一组额外的16字节明文块

##AES加密器加密原理
加密过程分多轮进行，分为初始轮，普通轮，最终轮

初始轮：加轮密钥

普通轮：1.字节代替 2.行位移 3.列混淆 4.加轮密钥

最终轮：1.字节代替 2.行位移 3.加轮密钥

除去初始轮，AES128共10轮，AES192共12轮，AES256共14轮

###字节代替
16字节的明文块在每一处理步骤中都被排成4X4的二维数组

字节代替就是把明文块的每一个字节代替成另一个字节，根据一个16X16大小的二维常亮数组S进行

######如明文当中a[2,2] = 5B, 一个字节为8Bit， 两位十六进制，那么输出的值b[2,2]=S[5][11] 

###行位移
得到字节代替后的二维数组，第一行不变，第二行循环左移1位，第三行2位，第四行3位

###列混淆
二维数组的每一列要和一个二维常量数组做矩阵相乘，名为修补矩阵，得到对应的输出列

###加轮密钥
如128位密钥，排列成4X4的矩阵。让输入数组的每一个字节a[i,j]与密钥对应坐标的字节k[i,j]异或一次，输出值b[i,j]
加密的每一轮所用到的密钥不是相同的，使用扩展密钥

###扩展密钥
AES128源码中使用4*4*（10+1）字节的数组W来存储所有轮的密钥，W（0-15）的值等同于原始密钥的值，用于初始轮，后续每一个元素W[i]有w[i-4]与w[i-1]计算而来，直到数组W所有元素都赋值完成

工作模式：
ECB模式下，每一个明文块的加密都是独立完成的，互不干涉的，简单，有利于并行计算，相同的明文块经过加密会变成相同的密文块，安全性较差

CBC模式，每一个明文块加密前会让明文块和一个值先做异或操作，初始向量参与第一个明文块的异或，后续每一个明文块和他前一个明文块所加密出的密文块相异或，CBC安全性更高，相同的明文块加密生成不同的密文块，但无法并行计算，性能较低，增加复杂度

##AES实现
	
	public class AES {
		@Test
		public void test1() {
			AES aes = new AES();
			String content = "hello world";
			String password = "123";
			System.out.println("加密之前：" + content);
	
			// 加密
			byte[] encrypt = aes.encrypt(content, password);
			System.out.println("加密后的内容：" + new String(encrypt));
	
			// 解密
			byte[] decrypt = aes.decrypt(encrypt, password);
			System.out.println("解密后的内容：" + new String(decrypt));
		}
		
		@Test
		public void test2() {
			AES aes = new AES();
	        String content = "hello world";
	        String password = "123";
	        System.out.println("加密之前：" + content);
	        // 加密
	        byte[] encrypt = aes.encrypt(content, password);
	        System.out.println("加密后的内容：" + new String(encrypt));
	        
	        //如果想要加密内容不显示乱码，可以先将密文转换为16进制
	        String hexStrResult = ParseSystemUtil.parseByte2HexStr(encrypt);
	        System.out.println("16进制的密文："  + hexStrResult);
	        
	        //如果的到的是16进制密文，别忘了先转为2进制再解密
	        byte[] twoStrResult = ParseSystemUtil.parseHexStr2Byte(hexStrResult);
	                
	        // 解密
	        byte[] decrypt = aes.decrypt(encrypt, password);
	        System.out.println("解密后的内容：" + new String(decrypt));    
	    }
	
		/**
		 * 解密AES加密过的字符串
		 * 
		 * @param content
		 *            AES加密过过的内容
		 * @param password
		 *            加密时的密码
		 * @return 明文
		 */
		public static byte[] decrypt(byte[] content, String password) {
			try {
				KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
				kgen.init(128, new SecureRandom(password.getBytes()));
				SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
				byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
				SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
				Cipher cipher = Cipher.getInstance("AES");// 创建密码器
				cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
				byte[] result = cipher.doFinal(content);
				return result; // 明文
	
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			}
			return null;
		}
	
		/**
		 * AES加密字符串
		 * 
		 * @param content
		 *            需要被加密的字符串
		 * @param password
		 *            加密需要的密码
		 * @return 密文
		 */
		public static byte[] encrypt(String content, String password) {
			try {
				KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
	
				kgen.init(128, new SecureRandom(password.getBytes()));// 利用用户密码作为随机数初始化出
																		// 128位的key生产者
				// 加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
	
				SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
	
				byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
																// null。
	
				SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
	
				Cipher cipher = Cipher.getInstance("AES");// 创建密码器
	
				byte[] byteContent = content.getBytes("utf-8");
	
				cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
	
				byte[] result = cipher.doFinal(byteContent);// 加密
	
				return result;
	
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

进制转换工具类
	
	/**
	 * 进制转换工具类
	 * @author tanjierong
	 *
	 */
	public class ParseSystemUtil {

	    /**将二进制转换成16进制 
	     * @param buf 
	     * @return 
	     */  
	    public static String parseByte2HexStr(byte buf[]) {  
	            StringBuffer sb = new StringBuffer();  
	            for (int i = 0; i < buf.length; i++) {  
	                    String hex = Integer.toHexString(buf[i] & 0xFF);  
	                    if (hex.length() == 1) {  
	                            hex = '0' + hex;  
	                    }  
	                    sb.append(hex.toUpperCase());  
	            }  
	            return sb.toString();  
	    } 
    
	    /**将16进制转换为二进制 
	     * @param hexStr 
	     * @return 
	     */  
    	public static byte[] parseHexStr2Byte(String hexStr) {  
            if (hexStr.length() < 1)  
                    return null;  
            byte[] result = new byte[hexStr.length()/2];  
            for (int i = 0;i< hexStr.length()/2; i++) {  
                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
                    result[i] = (byte) (high * 16 + low);  
            }  
            return result;  
    	}
	}