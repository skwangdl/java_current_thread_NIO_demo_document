#TOK-k问题（BFPRT算法 线性查找算法）

####在一堆树中秋其前K大或者前K小的问题
目前解决Top-k问题最有效的算法为BFPRT算法，又称为"中位数的中位数算法"，最坏时间复杂度O(n)

首先接触Top-k问题时，可以先对所有数据进行一个排序，然后取前k即可，但性能较差

通过修改快速排序中主元的选取方法可以降低快速排序在最坏情况的时间复杂度

##TOP-k实现
	
	public class Demo {
		public static void main(String[] args){
			Demo demo = new Demo();
			int k = 5;
			int[] array = {1,10,22,45,57,68,32,71,86,92};
			System.out.print("原数组：");
			for(int i = 0; i < array.length; i ++){
				System.out.print(array[i] + " ");
			}
			System.out.println();
			System.out.println("第" + k + "小的数：" + demo.BFPTR(array, 0, 9, k));
			System.out.print("变换后的数组：");
			for(int i = 0; i < array.length; i ++){
				System.out.print(array[i] + " ");
			}
		}
	
		// 插入排序
		private void InsertSort(int a[], int l, int r) {
			for (int i = l + 1; i <= r; i++) {
				if (a[i - 1] > a[i]) {
					int t = a[i];
					int j = i;
					while (j > l && a[j - 1] > t) {
						a[j] = a[j - 1];
						j--;
					}
					a[j] = t;
				}
			}
		}
	
		// 寻找中位数的中位数
		private int FindMid(int a[], int l, int r) {
			if (l == r)
				return a[l];
			int i = 0;
			int n = 0;
			for (i = l; i < r - 5; i += 5) {
				InsertSort(a, i, i + 4);
				n = i - l;
				swap(a[l + n / 5], a[i + 2]);
			}
	
			// 处理剩余元素
			int num = r - i + 1;
			if (num > 0) {
				InsertSort(a, i, i + num - 1);
				n = i - l;
				swap(a[l + n / 5], a[i + num / 2]);
			}
			n /= 5;
			if (n == l)
				return a[l];
			return FindMid(a, l, l + n);
		}
	
		// 寻找中位数的所在位置
		private int FindId(int a[], int l, int r, int num) {
			for (int i = l; i <= r; i++)
				if (a[i] == num)
					return i;
			return -1;
		}
	
		// 进行划分过程
		private int Partion(int a[], int l, int r, int p) {
			swap(a[p], a[l]);
			int i = l;
			int j = r;
			int pivot = a[l];
			while (i < j) {
				while (a[j] >= pivot && i < j)
					j--;
				a[i] = a[j];
				while (a[i] <= pivot && i < j)
					i++;
				a[j] = a[i];
			}
			a[i] = pivot;
			return i;
		}
	
		// 交换函数
		private void swap(int i, int j) {
			int temp = i;
			i = j;
			j = temp;
		}
	
		private int BFPTR(int a[], int l, int r, int k) {
			int num = FindMid(a, l, r); // 寻找中位数的中位数
			int p = FindId(a, l, r, num); // 找到中位数的中位数对应的id
			int i = Partion(a, l, r, p);
	
			int m = i - l + 1;
			if (m == k)
				return a[i];
			if (m > k)
				return BFPTR(a, l, i - 1, k);
			return BFPTR(a, i + 1, r, k - m);
		}
	}
	
	
	