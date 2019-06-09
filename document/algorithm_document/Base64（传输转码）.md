#Base64
>Http传输时，需要将Byte数组进行Base64编码

>Htp为文本协议，不同于二进制协议（Thrift）可以直接进行二进制传输

>Base64编码算法只支持64个可打印字符

###ASCII基本常识
1.范围0-127， 0-31和127， 33个字符属于控制字符（Control characters), 32-126， 95个属于可打印字符
2.早期进行Byte传输，Byte对应十进制不属于ASCII码范围，因此无法被传输。

###Base64作用：利用6Bit字符表达原本的8Bit字符， 用4个Base64字符（24Bit，每个6Bit）来表示3个传统的8Bit字符

##Base64举例

	source ASCII (if<128)          M                   a                    n
	source octets                77(0x4d)           97(0x61)               110(0x6e)
	Bit pattern                 0100 1101           0110 0001              0110 1110
	Index                       010011          010110          000101         101110
	Base64-encoded				   T              W               F              u

注：如第三，第四个Base64字符，没有匹配的8Bit字符，使用【=】字符填充

	source ASCII (if<128)          M                                      
	source octets                77(0x4d)           0(0x00)                 0(0x00)
	Bit pattern                 0100 1101           0000 0000              0000 0000
	Index                       010011          010000          000000        000000
	Base64-encoded				   T              Q               =             =

注：Base64算法不是加密算法， 加密算法包括：MD5 SHA AES RSA等

##Base64实现代码
	
	//工具类
	public class Base64_Util {
		public String toASCII(String msg) {
			StringBuffer sbu = new StringBuffer();
			char[] array = msg.toCharArray();
			for (int i = 0; i < array.length; i++) {
				if (i != array.length - 1) {
					sbu.append((int) array[i]).append(",");
				} else {
					sbu.append((int) array[i]);
				}
			}
			return sbu.toString();
		}
	
		public char asciiToString(String value) {
			return(char) Integer.parseInt(value);
		}
	
		public String toBinary(String str) {
			String binaryString = Integer.toBinaryString(Integer.valueOf(str));
			return binaryString;
	
		}
	}

	//编码过程实现
	public class Base64 {
		public static void main(String[] args){
			String msg = "KEPLER";
			Base64_Util util = new Base64_Util();
			String ascii = util.toASCII(msg);
			System.out.println(ascii);
			List<String> lists = new ArrayList<String>();
			String[] split = ascii.split(",");
			for(int i = 0; i < split.length; i ++){
				String binary = util.toBinary(split[i]);
				if(binary.toCharArray().length < 8){
					binary = "0" + binary;
				}
				lists.add(binary);
			}
			System.out.println(lists);
			
			String temp = "";
			for(int i = 0 ; i < lists.size(); i ++){
				temp = temp + lists.get(i);
			}
			char[] charArray = temp.toCharArray();
			System.out.println(charArray);
			
			List<String> lists1 = new ArrayList<String>();
			
			int j = -1;
			for(int i = 0; i < charArray.length/6; i ++){
				String temp1 = "";
				for(int m = 0; m < 6; m ++){
					j ++;
					temp1 = temp1 + charArray[j];
				}
				lists1.add(temp1);
			}
			System.out.println(lists1);
			
			List<String> lists2 = new ArrayList<String>();
			for(int i =0 ; i < lists1.size(); i ++){
				int parseInt = Integer.parseInt(lists1.get(i), 2);
				lists2.add(String.valueOf(parseInt));
			}
			System.out.println(lists2);
			
			List<String> lists3 = new ArrayList<String>();
			for(int i = 0; i < lists2.size(); i ++){
				String string = lists2.get(i);
				char c = util.asciiToString(string);
				lists3.add(String.valueOf(c));
			}
			System.out.println(lists3);
		}
	}