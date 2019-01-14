package cglibproxy;

import net.sf.cglib.proxy.Enhancer;

/**
 * 工厂类，生成增强过的目标类（已加入切入逻辑）
 * 
 * @author typ
 * 
 */
public class Factory {
	/**
	 * 获得增强之后的目标类，即添加了切入逻辑advice之后的目标类
	 * 
	 * @param proxy
	 * @return
	 */
	public static Base getInstance(CglibProxy proxy) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Base.class);
		enhancer.setCallback(proxy);
		// 此刻，base不是单纯的目标类，而是增强过的目标类
		Base base = (Base) enhancer.create();
		return base;
	}
}
