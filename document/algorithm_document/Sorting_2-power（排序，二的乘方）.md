
#排序算法（广义）

##插入排序
	public class charu {
		public static void main(String[] args) {
			int[] arr = { 67, 34, 12, 53, 79, 23 };
			for (int i = 0; i < arr.length; i++) {
				System.out.print(arr[i] + " ");
			}
			System.out.println();
			for (int i = 1; i < arr.length; i++) {
				for (int j = i; j > 0; j--) {
					if (arr[j] < arr[j - 1]) {
						int temp = arr[j];
						arr[j] = arr[j - 1];
						arr[j - 1] = temp;
					} else {
						break;
					}
				}
			}
			for (int i = 0; i < arr.length; i++) {
				System.out.print(arr[i] + " ");
			}
			System.out.println();
		}
	}
O(n^2)
	
##希尔排序
希尔排序是按照不同步长对元素进行插入排序，当刚开始元素很无序的时候，步长最大，所以插入排序的元素个数很少，速度很快；当元素基本有序了，步长很小，插入排序对于有序的序列效率很高
	
	public static void main(String [] args){
	    int[]a={49,38,65,97,76,13,27,49,78,34,12,64,1};
	        System.out.println("排序之前：");
	        for(int i=0;i<a.length;i++)
	        {
	            System.out.print(a[i]+" ");
	        }
	        //希尔排序
	        int d=a.length;
	            while(true)
	            {
	                d=d/2;
	                for(int x=0;x<d;x++)
	                {
	                    for(int i=x+d;i<a.length;i=i+d)
	                    {
	                        int temp=a[i];
	                        int j;
	                        for(j=i-d;j>=0&&a[j]>temp;j=j-d)
	                        {
	                            a[j+d]=a[j];
	                        }
	                        a[j+d]=temp;
	                    }
	                }
	                if(d==1)
	                {
	                    break;
	                }
	            }
	            System.out.println();
	            System.out.println("排序之后：");
	                for(int i=0;i<a.length;i++)
	                {
	                    System.out.print(a[i]+" ");
	                }
	    }
小于O(n^2)
##选择排序
	public class xuanze {
		public static void main(String[] args) {
			int[] arr = { 1, 3, 2, 45, 65, 33, 12 };
			System.out.println("交换之前：");
			for (int num : arr) {
				System.out.print(num + " ");
			}
			// 选择排序的优化
			for (int i = 0; i < arr.length - 1; i++) {// 做第i趟排序
				int k = i;
				for (int j = k + 1; j < arr.length; j++) {// 选最小的记录
					if (arr[j] < arr[k]) {
						k = j; // 记下目前找到的最小值所在的位置
					}
				}
				// 在内层循环结束，也就是找到本轮循环的最小的数以后，再进行交换
				if (i != k) { // 交换a[i]和a[k]
					int temp = arr[i];
					arr[i] = arr[k];
					arr[k] = temp;
				}
			}
			System.out.println();
			System.out.println("交换后：");
			for (int num : arr) {
				System.out.print(num + " ");
			}
		}
	}
O(n^2)
##冒泡排序
	public class maopao {
		public static void main(String[] args) {
			int[] a = {45,13,34,67,12,54,32};
			for(int i = 0; i < a.length; i ++){
				System.out.print(a[i] + " ");
			}
			System.out.println();
			int temp = 0;
			for (int i = a.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					if (a[j + 1] < a[j]) {
						temp = a[j];
						a[j] = a[j + 1];
						a[j + 1] = temp;
					}
				}
			}
			for(int i = 0; i < a.length; i ++){
				System.out.print(a[i] + " ");
			}
			System.out.println();
		}
	}
O(n^2)
##鸡尾酒排序
鸡尾酒排序等于是冒泡排序的轻微变形。不同的地方在于从低到高然后从高到低，而冒泡排序则仅从低到高去比较序列里的每个元素。他可以得到比冒泡排序稍微好一点的效能，原因是冒泡排序只从一个方向进行比对(由低到高)，每次循环只移动一个项目。

	public class Jiweijiu {
		public static void main(String[] args){
			int src[] = {34,12,76,38,29,75,39};
			// 将最小值排到队尾
			for (int i = 0; i < src.length / 2; i++) {
				for (int j = i; j < src.length - i - 1; j++) {
					if (src[j] < src[j + 1]) {
						int temp = src[j];
						src[j] = src[j + 1];
						src[j + 1] = temp;
					}
					System.out.println("交换小" + Arrays.toString(src));
				}
				// 将最大值排到队头
				for (int j = src.length - 1 - (i + 1); j > i; j--) {
					if (src[j] > src[j - 1]) {
						int temp = src[j];
						src[j] = src[j - 1];
						src[j - 1] = temp;
					}
					System.out.println("交换大" + Arrays.toString(src));
				}
				System.out.println("第" + i + "次排序结果：" + Arrays.toString(src));
			}
		}
	}
O(n^2) 如原序列已经大致排好序，则接近O（n）
##快速排序
通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行

	public class kuaisu {
		public static void sort(int a[], int low, int hight) {
			int i, j, index;
			if (low > hight) {
				return;
			}
			i = low;
			j = hight;
			index = a[i]; // 用子表的第一个记录做基准
			while (i < j) { // 从表的两端交替向中间扫描
				while (i < j && a[j] >= index)
					j--;
				if (i < j)
					a[i++] = a[j];// 用比基准小的记录替换低位记录
				while (i < j && a[i] < index)
					i++;
				if (i < j) // 用比基准大的记录替换高位记录
					a[j--] = a[i];
			}
			a[i] = index;// 将基准数值替换回 a[i]
			sort(a, low, i - 1); // 对低子表进行递归排序
			sort(a, i + 1, hight); // 对高子表进行递归排序
	
		}
	
		public static void quickSort(int a[]) {
			sort(a, 0, a.length - 1);
		}
	
		public static void main(String[] args) {
	
			int a[] = { 49, 38, 65, 97, 76, 13, 27, 49 };
			quickSort(a);
			System.out.println(Arrays.toString(a));
		}
	}
	
O(nlgn)
##堆排序
选择排序变形，大根堆（根节点最大），小根堆（根节点最小）

完全二叉树：即任何一非叶节点的关键字不大于或者不小于其左右孩子节点的关键字。

堆排序过程：
	
	index parameter			70					index parameter			10
	0		70		  60		12				0		10			60		12
	1		60		40  30	  8  10				1		60		  40  30  8  70
	2		12									2		12
	3		40									3		40
	4		30									4		30
	5		8									5		8
	6		10									6		70
	
	index parameter			60					index parameter			60
	0		60		  10		12				0		60			40		12
	1		10		40  30	  8  70				1		40		  10  30  8  70
	2		12									2		12
	3		40									3		10
	4		30									4		30
	5		8									5		8
	6		70									6		70
	
	index parameter			8					index parameter			40
	0		8		  40		12				0		40			8		12
	1		40		10  30	  60   70			1		8		  10  30  60  70
	2		12									2		12
	3		10									3		10
	4		30									4		30
	5		60									5		60
	6		70									6		70
	
	index parameter			40					index parameter			8
	0		40		  30		12				0		8			30		12
	1		30		10  8	  60   70			1		30		  10  40  60  70
	2		12									2		12
	3		10									3		10
	4		8									4		40
	5		60									5		60
	6		10									6		70

	index parameter			30					index parameter			10
	0		30		  8			12				0		10			8		12
	1		8		10  40	  60   70			1		60		  30   40  60  70
	2		12									2		12
	3		10									3		40
	4		40									4		30
	5		60									5		8
	6		70									6		70
	
	index parameter			12					index parameter			10
	0		70		  8			10				0		10			8		12
	1		60		30  40	  60   70			1		8		  30  40  60  70
	2		12									2		12
	3		40									3		30
	4		30									4		40
	5		8								    5		60
	6		10									6		70

	index parameter			8
	0		8		 10			12		
	1		10		30  40    60   70
	2		12
	3		30
	4		40
	5		60
	6		70

Util类
	
	public class ArrayUtils {
		public static void printArray(int[] array) {
			System.out.print("{");
			for (int i = 0; i < array.length; i++) {
				System.out.print(array[i]);
				if (i < array.length - 1) {
					System.out.print(", ");
				}
			}
			System.out.println("}");
		}
	
		public static void exchangeElements(int[] array, int index1, int index2) {
			int temp = array[index1];
			array[index1] = array[index2];
			array[index2] = temp;
		}
	}

测试类
	
	public class dui {
		public static void main(String[] args) {  
	        int[] array = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3 };  
	
	        System.out.println("Before heap:");  
	        ArrayUtils.printArray(array); 
	
	        heapSort(array);  
	
	        System.out.println("After heap sort:");  
	        ArrayUtils.printArray(array);  
	    }  
	
	    public static void heapSort(int[] array) {  
	        if (array == null || array.length <= 1) {  
	            return;  
	        }  
	
	        buildMaxHeap(array);  
	
	        for (int i = array.length - 1; i >= 1; i--) {  
	            ArrayUtils.exchangeElements(array, 0, i);  
	
	            maxHeap(array, i, 0);  
	        }  
	    }  
	
	    private static void buildMaxHeap(int[] array) {  
	        if (array == null || array.length <= 1) {  
	            return;  
	        }  
	
	        int half = array.length / 2;  
	        for (int i = half; i >= 0; i--) {  
	            maxHeap(array, array.length, i);  
	        }  
	    }  
	
	    private static void maxHeap(int[] array, int heapSize, int index) {  
	        int left = index * 2 + 1;  
	        int right = index * 2 + 2;  
	
	        int largest = index;  
	        if (left < heapSize && array[left] > array[index]) {  
	            largest = left;  
	        }  
	
	        if (right < heapSize && array[right] > array[largest]) {  
	            largest = right;  
	        }  
	
	        if (index != largest) {  
	            ArrayUtils.exchangeElements(array, index, largest);  
	
	            maxHeap(array, heapSize, largest);  
	        }  
	    }  
	}


最坏情况O(nlgn)
##归并排序
建立在归并操作上

将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。

分解：将序列每次折半划分，合并：将划分后的序列段两两合并后排序
	
	public class Binggui {
		public void Merge(int[] array, int low, int mid, int high) {
			int i = low; // i是第一段序列的下标
			int j = mid + 1; // j是第二段序列的下标
			int k = 0; // k是临时存放合并序列的下标
			int[] array2 = new int[high - low + 1]; // array2是临时合并序列
	
			// 扫描第一段和第二段序列，直到有一个扫描结束
			while (i <= mid && j <= high) {
				// 判断第一段和第二段取出的数哪个更小，将其存入合并序列，并继续向下扫描
				if (array[i] <= array[j]) {
					array2[k] = array[i];
					i++;
					k++;
				} else {
					array2[k] = array[j];
					j++;
					k++;
				}
			}
	
			// 若第一段序列还没扫描完，将其全部复制到合并序列
			while (i <= mid) {
				array2[k] = array[i];
				i++;
				k++;
			}
	
			// 若第二段序列还没扫描完，将其全部复制到合并序列
			while (j <= high) {
				array2[k] = array[j];
				j++;
				k++;
			}
	
			// 将合并序列复制到原始序列中
			for (k = 0, i = low; i <= high; i++, k++) {
				array[i] = array2[k];
			}
		}
	
		public void MergePass(int[] array, int gap, int length) {
			int i = 0;
	
			// 归并gap长度的两个相邻子表
			for (i = 0; i + 2 * gap - 1 < length; i = i + 2 * gap) {
				Merge(array, i, i + gap - 1, i + 2 * gap - 1);
			}
	
			// 余下两个子表，后者长度小于gap
			if (i + gap - 1 < length) {
				Merge(array, i, i + gap - 1, length - 1);
			}
		}
	
		public int[] sort(int[] list) {
			for (int gap = 1; gap < list.length; gap = 2 * gap) {
				MergePass(list, gap, list.length);
				System.out.print("gap = " + gap + ":\t");
				this.printAll(list);
			}
			return list;
		}
	
		// 打印完整序列
		public void printAll(int[] list) {
			for (int value : list) {
				System.out.print(value + "\t");
			}
			System.out.println();
		}
	
		public static void main(String[] args) {
			int[] array = { 9, 1, 5, 3, 4, 2, 6, 8, 7 };
	
			Binggui b = new Binggui();
			System.out.print("排序前:\t\t");
			b.printAll(array);
			b.sort(array);
			System.out.print("排序后:\t\t");
			b.printAll(array);
		}
	}

O(nlg2n)

##桶排序
桶排序是稳定的，是常见排序里最快的一种，大多数情况下比快速排序还要快，但是最耗空间

排序过程：
	
	数组：{3,6,2,9,5,7,1}
	桶排序数组:{0,1,2,3,0,5,6,7,0,9}
	桶排序下标: 0 1 2 3 4 5 6 7 8 9
	
	public class Tong {
		public static void main(String[] args){
			int[] arr = {3,6,2,9,5,7,1};
			int max = arr[0];
			for(int i = 0; i < arr.length; i ++){
				if(max < arr[i]){
					max = arr[i];
				}
			}
			
			int[] temp_arr = new int[max + 1];
			for(int i = 0; i < arr.length; i ++){
				temp_arr[arr[i]] = arr[i];
			}
			
			int index = 0;
			for(int i = 0; i < temp_arr.length; i ++){
				if(temp_arr[i] == 0){
					continue;
				}
				arr[index] = temp_arr[i];
				index ++;
			}
			
			for(int i = 0; i < arr.length; i ++){
				System.out.print(arr[i] + " ");
			}
			System.out.println();
			
		}
	}

O(2n)
##计数排序
非比较排序， 牺牲空间换取时间，当O(k) > O(nlgn),效率不如比较排序， 类似于桶排序

计数排序的过程类似小学选班干部的过程,如某某人10票,作者9票,那某某人是班长,作者是副班长

大体分两部分,第一部分是拉选票和投票,第二部分是根据你的票数入桶
	
	排序数组：{9,3,6,4,8,1,2}
    计数排序：{6,2,4,3,5,0,1}
    排序后：  {1,2,3,4,6,8,9}

	public class jishu {
		public static void main(String[] args){
			int[] arr = {9,3,6,4,8,1,2};
			int[] arr_1 = new int[arr.length];
			for(int i = 0; i < arr.length; i ++){
				int temp = arr[i];
				for(int j = 0; j < arr.length; j ++){
					if(temp > arr[j]){
						arr_1[i] = arr_1[i] + 1;
					}
				}
			}
			
			int[] result = new int[arr.length];
			for(int i = 0; i < arr_1.length; i ++){
				result[arr_1[i]] = arr[i];
			}
			
			for(int i = 0; i < result.length; i ++){
				System.out.print(result[i] + " ");
			}
			System.out.println();
		}
	}
	
O（n + k）
##基数排序
建立在计数排序上，适用于较大数，稀疏数组的排序
	
	public class jishu {
		public static void sort(int[] number, int d) // d表示最大的数有多少位
		{
			int k = 0;
			int n = 1;
			int m = 1; // 控制键值排序依据在哪一位
			int[][] temp = new int[10][number.length]; // 数组的第一维表示可能的余数0-9
			int[] order = new int[10]; // 数组orderp[i]用来表示该位是i的数的个数
			while (m <= d) {
				for (int i = 0; i < number.length; i++) {
					int lsd = ((number[i] / n) % 10);
					temp[lsd][order[lsd]] = number[i];
					order[lsd]++;
				}
				for (int i = 0; i < 10; i++) {
					if (order[i] != 0)
						for (int j = 0; j < order[i]; j++) {
							number[k] = temp[i][j];
							k++;
						}
					order[i] = 0;
				}
				n *= 10;
				k = 0;
				m++;
			}
		}
	
		public static void main(String[] args) {
			int[] data = { 73, 22, 93, 43, 55, 14, 28, 65, 39, 81, 33, 100 };
			jishu.sort(data, 3);
			for (int i = 0; i < data.length; i++) {
				System.out.print(data[i] + " ");
			}
		}
	}

##无序数组排序后的最大相邻差值
###有一个无序整型数组，如何求出这个数组排序后的任意两个相邻元素的最大差值？要求时间与空间复杂度尽可能低

##判断一个整数是否为2的乘方
	publican static boolean isPowerOf2（Integer number）{
		return (number & number - 1) == 0;
	}
O(1)

##计算一个正整数转换为二进制后的数字“1”的个数
	
	@Test
	public void test(){
		int number = 8563;
		int r = 1;
		while(number != 0){
			number = number >> 1;
			if(number % 2 != 0){
				r ++;
			}
		}
		System.out.println(r);
	}
O(1)

##寻找缺失的整数
###一个无序数组里面有99个不重复的正整数，范围从1到100，唯独缺少一个整数，如何找出这个缺失的整数，要求效率尽可能高
	
先算出1到100的和，在减去无序数组的元素和，差即为缺失的整数，时间复杂度O(n)， 空间复杂度O(1)