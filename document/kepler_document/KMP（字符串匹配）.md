#KMP算法
>KMP(Knuth-Morris-Pratt),算法目的为匹配一堆字符串中是否有符合要求的字符串

##KMP说明
1.字符串1的第一个字符与搜索词，字符串2的第一个字符，进行比较，因为不匹配，所以搜索词后移一位,知道字符串有一个字符，与搜索词第一个字符相同为止

	字符1	BBC ABCDAB ABCDABCDABDE
			|
	字符2	ABCDABD

2.接着比较字符串和搜索词的下一个字符，直到字符串有一个字符，与搜索词对应的字符不相同为止,这时可以将搜索词整个后移一位，再从头逐个比较，但是这样效率很差

	字符1	BBC ABCDAB ABCDABCDABDE
				 |
	字符2		 ABCDABD		 
	
3.KMP算法的想法是利用之前比较得到的已知信息，不要把搜索位置移回已经比较过的位置，继续把它向后移动，这样提高效率

	字符1	BBC ABCDAB ABCDABCDABDE
				 	  |
	字符2		ABCDABD

4.当搜索到字符2内字符与字符1内不匹配时，，查询字符2内匹配字符前一个字符对应的“部分匹配表”内的部分匹配值，如上面代码，“D”与空格不匹配，字符2内字符“B”对应的部分匹配值为2，移动位数=已匹配的字符数-对应的部分匹配值，6-2=4，所以将搜索词后移4位，知道字符2找到全部匹配的字符
	
	字符1	BBC ABCDAB ABCDABCDABDE
				 	  |
	字符2		    ABCDABD

###部分匹配表如何产生
前缀：除了最后一个字符外，一个字符串的全部头部组合；后缀：除了第一个字符外，一个字符串的全部尾部组合，部分匹配值就是前缀和后缀的最长共有元素的长度

以“ABCDABD”为例，部分匹配表如下
	
	搜索词：			A	B	C	D	A	B	D
	部分匹配值：		0	0	0	0	1	2	0

1.“A”的前缀后缀都为空集，共有元素的长度为0

2.“AB”前缀为：A， 后缀为：B，共有元素长度为0

3.“ABC”前缀：A AB 后缀：BC C，共有元素长度：0

4.“ABCD”前缀：A AB ABC 后缀：BCD CD D，共有元素长度：0

5.“ABCDA”前缀：A AB ABC ABCD 后缀：BCDA CDA DA A，共有元素为“A” 长度：1

6.“ABCDAB”前缀：A AB ABC ABCD ABCDA  后缀：BCDAB CDAB DAB AB A，共有元素“AB”长度：2

7.“ABCDABD”前缀：A AB ABC ABCD ABCDA ABCDAB 后缀：BCDABD CDABD DABD ABD BD D，共有元素长度：0

###部分匹配的实质
有时候，字符串头部与尾部会有重复，比如“ABCDAB”之中有两个“AB”，那么它的部分匹配值就是2，“AB”的长度，搜索词移动时，第一个“AB”向后移动4位（字符串长度-部分匹配值），就可以到第二个“AB”的位置

##KMP实现
	
	public class Demo {
		@Test
		public void test() {
			String des_str = "BBC ABCDAB ABCDABCDABDE";
			String src_str = "ABCDABD";
			Map<Integer, Integer> searchMap = this.getSearchMap(src_str);
			String indexFromDes = this.getIndexFromDes(des_str, src_str, searchMap);
			System.out.println(indexFromDes);
		}
	
		public String getIndexFromDes(String des_str, String src_str,
				Map<Integer, Integer> map) {
			char[] des_arg = des_str.toCharArray();
			char[] src_arg = src_str.toCharArray();
			int index = 0;
			int j = 1;
			while (index < src_arg.length) {
				if (des_arg[j - 1] == src_arg[index]) {
					index++;
					j ++;
				} else {
					j = j + index - map.get(index + 1);
					index = 1;
				}
			}
			int start = j - index;
			int end = j - 1;
			String temp = "";
			for(int i = start - 1; i < end ; i ++){
				temp = temp + des_arg[i];
			}
			return start + " to " + end + ":" + temp;
		}
		
		//获取部分匹配表
		public Map<Integer, Integer> getSearchMap(String str) {
			Map<Integer, Integer> searchMap = new HashMap<Integer, Integer>();
			List<String> frontLists = new ArrayList<String>();
			List<String> backLists = new ArrayList<String>();
			char[] charArray = str.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				String temp_str = this.getTempStr(str, i);
				char[] charArray2 = temp_str.toCharArray();
				for (int j = 0; j < charArray2.length; j++) {
					String frontStr = this.getFrontStr(charArray2, j);
					if (!frontStr.equals("")) {
						frontLists.add(frontStr);
					}
					String backStr = this.getBackStr(charArray2, j);
					if (!backStr.equals("")) {
						backLists.add(backStr);
					}
				}
				int length = 0;
				for (int j = 0; j < frontLists.size(); j++) {
					String front = frontLists.get(j);
					for (int x = 0; x < backLists.size(); x++) {
						String back = backLists.get(x);
						if (front.equals(back)) {
							length = front.toCharArray().length;
						}
					}
				}
				frontLists.clear();
				backLists.clear();
				searchMap.put(i + 1, length);
			}
			return searchMap;
	
		}
	
		public String getTempStr(String str, int m) {
			char[] charArray = str.toCharArray();
			String temp = "";
			for (int i = 0; i <= m; i++) {
				temp = temp + charArray[i];
			}
			return temp;
		}
		
		//获取前缀
		public String getFrontStr(char[] charArray, int m) {
			String temp = "";
			for (int i = m + 1; i < charArray.length; i++) {
				temp = temp + charArray[i];
			}
			return temp;
		}
		
		//获取后缀
		public String getBackStr(char[] charArray, int m) {
			String temp = "";
			for (int i = m; i > 0; i--) {
				temp = temp + charArray[m - i];
			}
			return temp;
		}
	}