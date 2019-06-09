#Random 随机数
生成随机数

##蒙特卡洛方法与拉斯维加斯方法
蒙特卡洛方法：采样越多，越接近最优解

假如筐里有100个苹果，让我每次闭眼拿1个，挑出最大的。于是我随机拿1个，再随机拿1个跟它比，留下大的，再随机拿1个……我每拿一次，留下的苹果都至少不比上次的小。拿的次数越多，挑出的苹果就越大，但我除非拿100次，否则无法肯定挑出了最大的。这个挑苹果的算法，就属于蒙特卡罗算法——尽量找好的，但不保证是最好的。

拉斯维加斯方法：采样越多，越有机会找到最优解

假如有一把锁，给我100把钥匙，只有1把是对的。于是我每次随机拿1把钥匙去试，打不开就再换1把。我试的次数越多，打开（最优解）的机会就越大，但在打开之前，那些错的钥匙都是没有用的。这个试钥匙的算法，就是拉斯维加斯的——尽量找最好的，但不保证能找到。

##随机数的产生

java Random类中实现的随机算法是伪随机，在进行随机时，随机算法的起源数字称为种子数，在种子数的基础上进行一定的变换，产生伪随机数，种子数一般为系统时间

线性同余法：

	public class Run {
		private final AtomicLong seed = new AtomicLong();
		private static final long multiplier = 0x5DEECE66DL;
	    private static final long addend = 0xBL;
	    private static final long mask = (1L << 48) - 1;
		
		public static void main(String[] args){
			Run run = new Run();
			int next = run.next(26);
			System.out.println(next);
		}
		
		protected int next(int bits) {
	        long oldseed, nextseed;
	        AtomicLong seed = this.seed;
	        do {
	            oldseed = seed.get();
	            System.out.println(oldseed);
	            nextseed = (oldseed * multiplier + addend) & mask;
	            System.out.println(nextseed);
	        } while (!seed.compareAndSet(oldseed, nextseed));
	        return (int)(nextseed >>> (48 - bits));
	    }
	}

产生随机序列A1,A2,A3,...An

	A0 = d
	An = (b * An-1 + C) & M

d称为种子，m取值越大越好，m,b互质，常取b为质数