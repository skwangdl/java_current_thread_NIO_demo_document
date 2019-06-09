#线性判别分析（LDA）
LDA，全程是Linear Discriminant Analysis（线性判别分析），主要用于机器学习，数据挖掘，特征识别

LDA核心原理：将带上标签的数据点，通过投影的方式，投影到维度更低的空间中，使得投影后的点，会形成按类别区分，一簇一簇的情况，相同类别的点，将会在投影后的空间中更接近。

如图LDA，红色的方形的点为0类的原始点，蓝色的方形为1类的原始点，经过原点的那条线就是投影的直线，从图上可以清楚的看到，红色的点和蓝色的点被投影线明显的分开了，这个数据只是随便画的，如果在高维的情况下，看起来会更好一点。

LDA分类的目标是：使得类别内的点距离尽量小，类别间的点尽量大

###线性判别分析举例
	
最小二乘法实现
	
	/**
	 * 最小二乘法 y=ax+b
	 * 
	 * @author Administrator
	 * 
	 */
	public class Theleastsquaremethod {
	
		private static double a;
	
		private static double b;
	
		private static int num;
	
		/**
		 * 训练
		 * 
		 * @param x
		 * @param y
		 */
		public static void train(double x[], double y[]) {
			num = x.length < y.length ? x.length : y.length;
			calCoefficientes(x, y);
		}
	
		/**
		 * a=(NΣxy-ΣxΣy)/(NΣx^2-(Σx)^2) b=y(平均)-a*x（平均）
		 * 
		 * @param x
		 * @param y
		 * @return
		 */
		public static void calCoefficientes(double x[], double y[]) {
			double xy = 0.0, xT = 0.0, yT = 0.0, xS = 0.0;
			for (int i = 0; i < num; i++) {
				xy += x[i] * y[i];
				xT += x[i];
				yT += y[i];
				xS += Math.pow(x[i], 2.0);
			}
			a = (num * xy - xT * yT) / (num * xS - Math.pow(xT, 2.0));
			b = yT / num - a * xT / num;
		}
	
		/**
		 * 预测
		 * 
		 * @param xValue
		 * @return
		 */
		public static double predict(double xValue) {
			System.out.println("a=" + a);
			System.out.println("b=" + b);
			return a * xValue + b;
		}
		
		public static double getA() {
			return a;
		}
	
		public static void setA(double a) {
			Theleastsquaremethod.a = a;
		}
	
		public static double getB() {
			return b;
		}
	
		public static void setB(double b) {
			Theleastsquaremethod.b = b;
		}
	
		public static void main(String args[]) {
			double[] x = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
			double[] y = { 23, 44, 32, 56, 33, 34, 55, 65, 45, 55 };
			Theleastsquaremethod.train(x, y);
			System.out.println(Theleastsquaremethod.predict(10.0));
		}
	
	}

节点类
	
	public class Node {
		private double x;
		private double y;
		private int label;
	
		public double getX() {
			return x;
		}
	
		public void setX(double x) {
			this.x = x;
		}
	
		public double getY() {
			return y;
		}
	
		public void setY(double y) {
			this.y = y;
		}
	
		public int getLabel() {
			return label;
		}
	
		public void setLabel(int label) {
			this.label = label;
		}
	
		@Override
		public String toString() {
			return "Node [x=" + x + ", y=" + y + ", label=" + label + "]";
		}
	
		public Node(double x, double y, int label) {
			super();
			this.x = x;
			this.y = y;
			this.label = label;
		}
	
		public Node() {
			super();
		}
	
	}

线性判别分析过程实现
	
	public class LDATest {
	
		@Test
		public void test(){
			Node node1 = new Node(2,1.5,1);
			Node node2 = new Node(3,1,1);
			Node node3 = new Node(2,0.5,2);
			Node node4 = new Node(3,0.7,2);
			
			double[] a_arg = {2,3,2,3};
			double[] b_arg = {1.5,1,0.5,0.7};
			
			List<Node> lists = new ArrayList<Node>();
			List<Node> lists1 = new ArrayList<Node>();
			List<Node> lists2 = new ArrayList<Node>();
			lists.add(node1);
			lists.add(node2);
			lists.add(node3);
			lists.add(node4);
			double k = this.getLine(a_arg, b_arg);
				
			for(int i = 0; i < lists.size(); i ++){
				Node n = this.getInterNode(lists.get(i), k, 0);
				if(n.getLabel() == 1){
					lists1.add(n);
				}else{
					lists2.add(n);
				}
			}
			System.out.println(lists1);
			System.out.println(lists2);
		}
		
		//输入数据点得出投影线的斜率
		private double getLine(double[] x, double[] y){
			Theleastsquaremethod.train(x, y);
			double k = Theleastsquaremethod.getA();
			Theleastsquaremethod.predict(3.0);
			return k;
		}
		
		//过一点做垂直于直线的垂线，得出垂点
		private Node getInterNode(Node node, double k, double b){
			double k_temp = -1/k;
			double temp_1 = node.getX() * k_temp;
			double b_temp = node.getY() - temp_1;
			double temp_3 = b_temp - b;
			double temp_4 = k - k_temp;
			double x_result = temp_3 / temp_4;
			double y_result = k_temp * x_result + b_temp;
			Node resultNode = new Node(x_result, y_result, node.getLabel());
			return resultNode;
		}
	}
