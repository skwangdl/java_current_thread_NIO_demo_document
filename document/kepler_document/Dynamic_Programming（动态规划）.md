#Dynamic Programming(动态规划)
>每次决策依赖于当前状态，又随机引起状态的转移

>动态规划有3个核心元素：状态转移方程，最优子结构，边界
####有一座高度为10级台阶的楼梯，从下往上走，每跨一步只能向上1级或者2级台阶，要求用程序来求解一共有多少种走法

>暂且不管从0走到8级台阶的过程，想要走到10级，最后一步必然是从8级或者9级开始

>从1级到9级的走法种类记做F(9)， 从1级到8级的走法种类记做F(8)， 可以推断从1级到10级的走法种类等于F（9）+F(8)， 即为F(10) = F(9) + F(8)

>以此类推，从1到n级的走法有F(n)种，F(n) = F(n - 1) + F(n - 2)

	阶数：	1	2	3	4	5	6	7	8	9	10
	种类：	1	2	3	5	8	13	21	34	55	89

递归求解
	public class Demo {
	
		public static void main(String[] args){
			Demo demo = new Demo();
			int num = demo.getNum(10);
			System.out.println(num);
		}
		
		private int getNum(int n){
			if(n < 1){
				return 0;
			}else if(n == 1){
				return 1;
			}else if(n == 2){
				return 2;
			}
			return getNum(n - 1) + getNum(n - 2);
		}
近似O(2^n)

备忘录算法求解
	public class Demo1 {
		public static void main(String[] args){
			Demo1 demo = new Demo1();
			int num = demo.getNum(10, new HashMap<Integer, Integer>());
			System.out.println(num);
		}
		
		private int getNum(int n , HashMap<Integer, Integer> map){
			if(n < 1){
				return 0;
			}else if(n == 1){
				return 1;
			}else if(n == 2){
				return 2;
			}else if(map.containsKey(n)){
				return map.get(n);
			}else{
				int value = this.getNum(n - 1, map) + this.getNum(n - 2, map);
				map.put(n, value);
				return value;
			}
		}
	}

动态规划求解
	public class Demo3 {
		public static void main(String[] args){
			Demo3 demo = new Demo3();
			int num = demo.getNum(10);
			System.out.println(num);
		}
		
		private int getNum(int n){
			if(n < 1){
				return 0;
			}else if(n == 1){
				return 1;
			}else if(n == 2){
				return 2;
			}
			int a = 1;
			int b = 2;
			int temp = 0;
			for(int i = 3; i <= n; i ++){
				temp = a + b;
				a = b;
				b = temp;
			}
			return temp;
		}
	}

####有一个国家发现了5座金矿，每座金矿的黄金储量不同，需要参与的挖掘的工人数也不同，参与挖掘的工人数为10，每座金矿要么全挖，要么不挖，黄金储量与所需人数为：500金（5人），400金（5人），350金（3人），300金（4人），200金（3人）， 要得到尽可能多的黄金，如何选择

>5个金矿的最优选择，就是"前4座金矿10个工人的挖金数量"和"前4座金矿7个工人的挖金数量+第5座金矿的挖金数量"的最大值

>表格第一列给定金矿的情况，设为N座金矿，第一列代表工人数， 设为W，F(N,W)代表获得的黄金数

>状态转换方程：金矿数N,工人数W,金矿储量G[],金矿用工量P[]

F(n，w) = 0 (n<=1, w<p[0]);

F(n, w)=g[0] (n==1, w >=p[0]);

F(n, w)=F(n-1, w) (n>1,w<p[n-1]);

F(n, w)=max(F(n-1, w), F(n-1, w-p[n-1] + g[n-1])) (n>1, w>=p[n-1]);

		1		2		3		4		5		6		7		8		9		10
	1	0		0		0		0		400		400		400		400		400		400	
	2	0 		0		0		0		500		500		500		500		500		900
	3	0		0		350		350		500		500		500		850		850		900
	4	0		0		350		350		500		500		500		850		850		900
	5	0		0		350		350		500		500		500		850		850		900


实现方法
	
	private int getNum(int n, int w, int[] g, int[] p){
		int[] preResults = new int[p.length];
		int[] results = new int[p.length];
		//填充边界值
		for(int i = 0; i <= n; i ++){
			if(i < p[0]){
				preResults[i] = 0;
			}else{
				preResults[i] = g[0];
			}
		}
		
		//填充其他值
		for(int i = 0; i < n; i ++){
			for(int j = 0; j <= w; j ++){
				if(j < p[i]){
					results[j] = preResults[j];	
				}else{
					results[j] = Math.max(preResults[j], preResults[j - p[i]] + g[i]);
				}
			}
			preResults = results;
		}
		return results[n];
	}
O(n * w), (w)

注：当工人数较大时，使用动态规划空间复杂度较大，使用递归性能较高
##01背包问题
####一个背包，承重有限为w，有N种物体 第i种物体，每种物体有1个，价值V[i]， 占用重量为W[i]，选择物品放入背包，使得总重量不超过背包的承重，且总价值最大

设f[i][w]表示前i件物品放入容量为w的背包可以获得的最大价值，当前i件物品放入的时候，有如下两种情况：

1.第i件不放进去，这时所得价值f[i-1][w]
2.第i件放进去，这时所得价值f[i][w-w[i]]+v[i]
 
状态转移方程：f[i][w]=max{f[i-1][w], f[i-1][w-w[i]] + v[i]}


	public class Demo {
	    private final int MIN = Integer.MIN_VALUE;
	
	    @Test
	    public void test() {
	        int[] w = {3, 2, 2};
	        int[] v = {5, 10, 20};
	        knapsackOptimal(5, w, v);
	    }
	
	    /**
	     * 01背包-容量压缩
	     *
	     * @param c      包容量
	     * @param weight 各物品质量
	     * @param value  各物品价值
	     */
	    public void knapsackOptimal(int c, int[] weight, int[] value) {
	        int n = weight.length; //物品数量
	        int[] w = new int[n + 1];
	        int[] v = new int[n + 1];
	        int[][] G = new int[n + 1][c + 1];
	        for (int i = 1; i < n + 1; i++) {
	            w[i] = weight[i - 1];
	            v[i] = value[i - 1];
	        }
	
	        //初始化values[0...c]=0————在不超过背包容量的情况下，最多能获得多少价值
	        //原因：如果背包并非必须被装满，那么任何容量的背包都有一个合法解“什么都不装”，这个解的价值为0，所以初始时状态的值也就全部为0了
	        int[] values = new int[c + 1];
	        //初始化values[0]=0，其它全为负无穷————解决在恰好装满背包的情况下，最多能获得多少价值的问题
	        //原因：只有容量为0的背包可以什么物品都不装就能装满，此时价值为0，其它容量背包均无合法的解，属于未定义的状态，应该被赋值为负无穷
	        /*for (int i = 1; i < values.length; i++) {
	            values[i] = MIN;
	        }*/
	
	        for (int i = 1; i < n + 1; i++) {
	            for (int t = c; t >= w[i]; t--) {
	                if (values[t] < values[t - w[i]] + v[i]) {
	                    values[t] = values[t - w[i]] + v[i];
	                    G[i][t] = 1;
	                }
	            }
	        }
	        System.out.println("最大价值为： " + values[c]);
	        System.out.print("装入背包的物品编号为： ");
	        /*
		        输出顺序:逆序输出物品编号
		        注意：这里另外开辟数组G[i][v],标记上一个状态的位置
	        G[i][v] = 1:表示物品i放入背包了，上一状态为G[i - 1][v - w[i]]
	        G[i][v] = 0:表示物品i没有放入背包，上一状态为G[i - 1][v]
	        */
	        int i = n;
	        int j = c;
	        while (i > 0) {
	            if (G[i][j] == 1) {
	                System.out.print(i + " ");
	                j -= w[i];
	            }
	            i--;
	        }
	    }
	}

