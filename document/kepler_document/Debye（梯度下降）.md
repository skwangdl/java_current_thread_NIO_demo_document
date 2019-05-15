#梯度下降
最优化算法，常用于机器学习当中用来递归性的逼近最小偏差模型

计算过程就是沿梯度下降的方向求解极小值，或沿梯度想上的方向求解极大值

梯度：对于可微的数量场f(x,y,z),以（δf/δx, δf/δy,δf/δz）为分量的向量场称为梯度或斜量
	
###最速降线问题
	
如果使分成的层数n无限地增加，即每层的厚度无限地变薄，则质点的运动便趋于空间A、B两点间质点运动的真实情况，此时折线也就无限增多，其形状就趋近我们所要求的曲线——最速降线．而折线的每一段趋向于曲线的切线，因而得出最速降线的一个重要性质：任意一点上切线和铅垂线所成的角度的余弦与该点落下的高度的平方根的比是常数．而具有这种性质的曲线就是摆线．所谓摆线，它是一个圆沿着一条直线滚动（无滑动）时，圆周上任意一点的轨迹。


###求解函数极值

f(x) = x^3-x^2 x定义域[0,1]

实现
	public class Debye {
		public static double x = 0.0;
		public static double y = 0.0;
		public static double step = 0.1;
		public static double error = 0.000001;
	
		@Test
		public void test() {
			Debye debye = new Debye();
			double min = debye.getMin();
			System.out.println(min);
		}
	
		public double getMin() {
			double temp_x = x + step;
			double temp_y = this.getY(temp_x);
			double m = y - temp_y;
			while (m > error) {
				if (x < 0 || x > 1) {
					return -1;
				} else {
					m = y - temp_y;
					if (m > step / 5) {
						step = step / 5;
					}
					y = temp_y;
					x = temp_x;
					temp_x = x + step;
					temp_y = this.getY(temp_x);
				}
			}
			return x;
		}
	
		public double getY(double x) {
			return x * x * x - x * x;
		}
	}
	


