#最小栈，最小队列的实现
###实现一个栈，带有出栈（pop）,入栈（push）,取最小元素（getMin）三个方法。要保证这三个方法的时间复杂度都是O(1)

基本栈
	
	public class Stack {
		Stack1 temp_stack = new Stack1();
		List<Integer> lists = new ArrayList<Integer>();
		
		public void push(Integer n){
			if(lists.size() == 0){
				temp_stack.push(0);
				lists.add(n);
			}else{
				int temp = lists.get(0);
				int index = 0;
				for(int i = 0; i < lists.size(); i ++){
					if(temp > lists.get(i)){
						temp = lists.get(i);
						index = i;
					}
				}
				if(temp <= n){
					temp_stack.push(index);
				}else{
					temp_stack.push(lists.size());
				}
				lists.add(n);
			}
		}
		
		public Integer pop(){
			int a = lists.get(lists.size() - 1);
			lists.remove(lists.size() - 1);
			temp_stack.pop();
			return a;
		}
		
		public Integer getMin(){
			int pop = temp_stack.pop();
			return lists.get(pop);
		}
		
		public List<Integer> getLists() {
			return lists;
		}
		
		public void setLists(List<Integer> lists) {
			this.lists = lists;
		}
		
		@Override
		public String toString() {
			return "Stack [lists=" + lists + "]";
		}
	}
	
记录最小值栈
	
	public class Stack1 {
		List<Integer> lists = new ArrayList<Integer>();
		
		public void push(Integer n){
			lists.add(n);
		}
		
		public int pop(){
			int a = lists.get(lists.size() - 1);
			lists.remove(lists.size() - 1);
			return a;
		}
	}
	
测试类
	
	public class TestStack {
		public static void main(String[] args){
			Stack stack = new Stack();
			stack.push(10);
			System.out.println(stack);
			stack.push(12);
			System.out.println(stack);
			stack.push(9);
			System.out.println(stack);
			stack.push(14);
			System.out.println(stack);
			stack.push(7);
			System.out.println(stack);
			stack.pop();
			System.out.println(stack);
			Integer min = stack.getMin();
			System.out.println(min);
		}
	}
	
运行结果
	
	Stack [lists=[10]]
	Stack [lists=[10, 12]]
	Stack [lists=[10, 12, 9]]
	Stack [lists=[10, 12, 9, 14]]
	Stack [lists=[10, 12, 9, 14, 7]]
	Stack [lists=[10, 12, 9, 14]]
	9
	
#最小二乘法

>它通过最小化误差的平方和寻找数据的最佳函数匹配。利用最小二乘法可以简便地求得未知的数据，并使得这些求得的数据>与实际数据之间误差的平方和为最小

>目的为根据散点拟合成一个散点坐标的关系函数，即曲线拟合，线性回归只要使用方法

#####一元线性回归分析
实体类
	public class Entity {
		private int w;
		private double x;
		private double y;
	
		public int getW() {
			return w;
		}
	
		public void setW(int w) {
			this.w = w;
		}
	
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
	
		public Entity(int w, double x, double y) {
			super();
			this.w = w;
			this.x = x;
			this.y = y;
		}
	
		public Entity() {
			super();
			// TODO Auto-generated constructor stub
		}
	
		@Override
		public String toString() {
			return "Entity [w=" + w + ", x=" + x + ", y=" + y + "]";
		}
	
	}

测试类
	
	public class Demo {

		// m_1 * a + m_2 * b = f1
		// n_1 * a + n_2 * b = f2
		// y = a + bx
	
		private static double a;
		private static double b;
		private static double m_1;
		private static double m_2;
		private static double n_1;
		private static double n_2;
		private static double f_1;
		private static double f_2;
	
		public static void main(String[] args) {
			Demo demo = new Demo();
			Entity data_1 = new Entity(2, 0.1, 1.1);
			Entity data_2 = new Entity(1, 0.2, 1.9);
			Entity data_3 = new Entity(1, 0.3, 3.1);
			Entity data_4 = new Entity(1, 0.4, 3.9);
	
			m_1 = data_1.getW() + data_2.getW() + data_3.getW() + data_4.getW();
			m_2 = data_1.getX() * data_1.getW() + data_2.getX() * data_2.getW()
					+ data_3.getX() * data_3.getW() + data_4.getX() * data_4.getW();
			n_1 = m_2;
			double data1_x_power = demo.power2(data_1.getX());
			double data2_x_power = demo.power2(data_2.getX());
			double data3_x_power = demo.power2(data_3.getX());
			double data4_x_power = demo.power2(data_4.getX());
			n_2 = data_1.getW() * data1_x_power + data_2.getW() * data2_x_power
					+ data_3.getW() * data3_x_power + data_4.getW() * data4_x_power;
	
			f_1 = data_1.getW() * data_1.getY() + data_2.getW() * data_2.getY()
					+ data_3.getW() * data_3.getY() + data_4.getW() * data_4.getY();
			f_2 = data_1.getW() * data_1.getX() * data_1.getY() + data_2.getW()
					* data_2.getX() * data_2.getY() + data_3.getW() * data_3.getX()
					* data_3.getY() + data_4.getW() * data_4.getX() * data_4.getY();
			
			double temp_1 = m_1 * f_2 - n_1 * f_1;
			double temp_2 = n_2 * m_1 - m_2 * n_1;
			b = demo.div(temp_1, temp_2);                                                                                                                                                                                     
	
			double temp_3 = n_1 * f_2 - n_2 * f_1;  
			double temp_4 = n_1 * m_2 - m_1 * n_2;
			a = demo.div(temp_3, temp_4);
	 
			System.out.println("y=" + a + " + " + b + "x");
		}
	
		private double power2(double a) {
			return a * a;
		}
	
		private double div(double a, double b) {
			if (b == 0) {
				System.out.println("number is 0");
				return -1;
			} else {
				return a / b;
			}
		}
	}

最小二乘法
	
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
	
		public static void main(String args[]) {
			double[] x = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
			double[] y = { 23, 44, 32, 56, 33, 34, 55, 65, 45, 55 };
			Theleastsquaremethod.train(x, y);
			System.out.println(Theleastsquaremethod.predict(10.0));
		}
	
	}

