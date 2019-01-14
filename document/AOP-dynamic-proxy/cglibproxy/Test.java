package cglibproxy;

/**
 * @author typ
 *
 */
public class Test {
	public static void main(String[] args) {
		CglibProxy proxy = new CglibProxy();
		// base为生成的增强过的目标类
		Base base = Factory.getInstance(proxy);
		base.add();
	}
}
