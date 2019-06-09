#EM(Expectation Maximization)最大期望
一种迭代算法，用于含有隐变量（latent variable）的概率参数模型的最大似然估计或极大后验概率估计，所谓隐变量，是指没有办法观测到的变量

比如：有两枚硬币A,B，每一次随机取一枚进行抛掷，只能观测到硬币的正面与反面，不能观测到每一次取的硬币是否为A，则称每一次的选择抛掷硬币为隐变量

EM算法的主要目的是提供一个简单的迭代算法计算后验密度函数，它的最大优点是简单和稳定，但容易陷入局部最优。

最大期望算法经过两个步骤交替进行计算

>第一步是计算期望（E），利用概率模型参数的现有估计值，计算隐藏变量的期望


>第二步是最大化（M），利用E 步上求得的隐藏变量的期望，对参数模型进行最大似然估计。

##EM举例
	
有两枚硬币A,B，每一次随机取一枚进行抛掷，只能观测到硬币的正θ面与反面，不能观测到每一次取的硬币是否为A，记P（+|A）表示A硬币正面朝上的概率，P（+|B）表示B硬币正面朝上的概率，这里A,B两个硬币的正反面出现的概率是不同的。
假设取出5次硬币，每次取出后抛掷10次，结果如下，其中1代表正面，0代表反面

	第一次：1 0 0 0 0 1 1 0 1 1
	第二次：0 0 0 0 1 0 1 0 1 1
	第三次：1 1 1 1 1 0 0 1 0 0
	第四次：1 0 0 0 1 0 0 1 0 0
	第五次：0 1 1 1 1 1 0 1 1 1

###EM计算步骤：
#####1.设定P(+|A),P(+|B)的初始值
设定P（+|A）=0.6， P（+|B)=0.5

#####2.估计每组试验是硬币A的概率
第一次抛出A硬币的概率：P(A)=C(10,5)(0.6^5)(0.4^5) = 252 X 0.07776 X 0.01024 =0.201  B硬币的概率：P(B)=C(10,5)(0.5^5)(0.5^5) = 252 X 0.03125 X 0.03125 = 0.246，得出第一次抛出A的概率为P(A)/(P(A)+P(B)) = 0.201 / 0.447 = 0.45,抛出B的概率为：1-0.45 = 0.55

#####3.利用第二步得到的两个概率，计算每组试验中A朝上的期望值，B朝上的期望值，并每次讲之前的同一组期望值相加
第一次A的期望值，A正面: count X P(A) = 5 X 0.45=2.2，A反面：(totalcount - count) X P(A) = (10-5)X0.45=2.2
第一次B的期望值，B正面: count X P(B) = 5 X 0.55=2.75=2.8，B反面：(totalcount - count) X P(B) = (10-5)X0.55=2.75=2.8

#####4.根据得到的A，B期望值，重新计算初始概率P（+|A），P(+|B)
P（+|A）=2.2 / (2.2 + 2.2)=0.5, P(+|B) = 2.8 / (2.8 + 2.8) = 0.5

#####5.迭代计算2-4步过程，当达到一定次数或者收敛到一定精度，输出两组期望值：
迭代计算5次后的期望值之和：
A正面：21.3，A反面：8.6，B正面：11.7，B反面：8.4

得到最后输出的概率：θ(A)=21.3/(21.3+8.6)=0.8,θ（B）=11.7/(11.7+8.4)=0.52

###实现
	
	public class EM {

		private List<Map<String, Double>> lists = new ArrayList<Map<String, Double>>();
	
		@Test
		public void test() {
			EM em = new EM();
			int[][] data = {
					{1, 0, 0, 0, 1, 1, 0, 1, 0, 1},
					{1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
					{1, 0, 1, 1, 1, 1, 1, 0, 1, 1},
					{1, 0, 1, 0, 0, 0, 1, 1, 0, 0},
					{0, 1, 1, 1, 0, 1, 1, 1, 0, 1}
			};
			Map<String, Double> emResult = em.getEmResult(data, 0.6, 0.5);
			System.out.println(emResult);
		}
	
		// 循环获取EM结果
		private Map<String, Double> getEmResult(int[][] data, double P_A, double P_B) {
			Map<String, Double> map = new HashMap<String, Double>();
			int totalCount = data[0].length;
			for(int i = 0; i < data.length; i ++){
				int count = 0;
				if(map.get("P_A") != null){
					P_A = map.get("P_A");
				}
				if(map.get("P_B") != null){
					P_B = map.get("P_B");
				}
				int[] tempArg = data[i];
				for(int j = 0; j < tempArg.length; j ++){
					if(tempArg[j] == 1){
						count ++;
					}
				}
				map = this.getNewPro(totalCount, count, P_A, P_B);
			}
			return map;
		}
	
		// 获取新的概率值
		private Map<String, Double> getNewPro(int totalCount, int count,
				double P_A, double P_B) {
			Map<String, Double> map = this.getExpect(totalCount, count, P_A, P_B);
			Map<String, Double> resultMap = new HashMap<String, Double>();
			double a = 0.0;
			double b = 0.0;
			double result_a = 0.0;
			double result_b = 0.0;
			lists.add(map);
			for (int i = 0; i < lists.size(); i++) {
				Map<String, Double> tempMap = lists.get(i);
				a = a + tempMap.get("A_1");
				b = b + tempMap.get("A_0");
			}
			double temp_1 = a + b;
			result_a = a / temp_1;
			a = 0.0;
			b = 0.0;
			for (int i = 0; i < lists.size(); i++) {
				Map<String, Double> tempMap = lists.get(i);
				a = a + tempMap.get("B_1");
				b = b + tempMap.get("B_0");
			}
			double temp_2 = a + b;
			result_b = a / temp_2;
			resultMap.put("P_A", result_a);
			resultMap.put("P_B", result_b);
			return resultMap;
		}
	
		// 获取期望值
		private Map<String, Double> getExpect(int totalCount, int count,
				double P_A, double P_B) {
			Map<String, Double> map = new HashMap<String, Double>();
			map.put("A_1", count * this.getProbability(totalCount, count, P_A, P_B));
			map.put("A_0",
					(totalCount - count)
							* this.getProbability(totalCount, count, P_A, P_B));
			map.put("B_1",
					count * (1 - this.getProbability(totalCount, count, P_A, P_B)));
			map.put("B_0",
					(totalCount - count)
							* (1 - this.getProbability(totalCount, count, P_A, P_B)));
			return map;
		}
	
		// 获取概率
		private double getProbability(int n, int r, double P_A, double P_B) {
			double res_1 = this.getFourPoint(
					this.getGroup(n, r) * this.getPower(P_A, r), 4);
			double res_2 = this.getFourPoint(
					res_1 * this.getPower((1 - P_A), (n - r)), 4);
			double res_3 = this.getFourPoint(
					this.getGroup(n, (n - r)) * this.getPower(P_B, r), 4);
			double res_4 = this.getFourPoint(
					res_3 * this.getPower((1 - P_B), (n - r)), 4);
			double temp_1 = res_2 + res_4;
			double temp_2 = res_2 / temp_1;
			return this.getFourPoint(temp_2, 2);
		}
	
		// 排列组合
		private int getGroup(int n, int r) {
			int temp_1 = 1;
			int c = n;
			while (c != 1) {
				temp_1 = temp_1 * c;
				c = c - 1;
			}
			int temp_2 = 1;
			int x = n - r;
			while (x != 1) {
				temp_2 = temp_2 * x;
				x = x - 1;
			}
			int temp_3 = 1;
			int y = r;
			while (y != 1) {
				temp_3 = temp_3 * y;
				y = y - 1;
			}
			int a = temp_1 / temp_2;
			int b = a / temp_3;
			return b;
		}
	
		// 次幂
		private double getPower(double a, int b) {
			double x = a;
			for (int i = 1; i < b; i++) {
				x = x * a;
			}
			return x;
		}
	
		// 保留几位小数
		private double getFourPoint(double f, int x) {
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(x, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f1;
		}
	}

注：未完成，需要调试
	

##混合高斯模型
混合高斯模型的本质上为一个概率密度函数，将一种概率密度函数分解成多个子概率密度函数，每个子概率密度函数为高斯分布，即正态分布

使用K个（基本为3到5个）个高斯模型来表征图像中各个像素点的特征

混合高斯模型主要由方差和均值两个参数决定

每一个子概率密度函数的系数由EM算法（最大期望计算）
	
