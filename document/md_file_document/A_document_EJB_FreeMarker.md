#EJB Enterprise Java Bean
核心组件：会话Bean(Session Bean), 消息驱动Bean(Message Driven Bean), EJB官方推荐使用注解配置，也可以使用xml配置

在EJB3技术中，还有一种容器，叫做JavaEE容器，真正的意义就是实现JavaEE标准接口（WebLogin, JBoss等）

##1.会话Bean
有状态会话：类似Web技术中的Session对象，可以在服务器端保存客户端的会话状态

无状态会话：JavaEE容器内有一个无状态会话Bean实例池，类似于JDBC连接池，使用完放回池内，并且所有池内的无状态会话都为客户端共享

####1.无状态会话使用

构建Web工程

1.web.xml
	
	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" id="WebApp_ID" version="3.0">
	  <display-name>A_TestProject_WEB</display-name>
	  <servlet>
	    <description>This is the description of my J2EE component</description>
	    <display-name>This is the display name of my J2EE component</display-name>
	    <servlet-name>test</servlet-name>
	    <servlet-class>controller.Test</servlet-class>
	  </servlet>
	
	  <servlet-mapping>
	    <servlet-name>test</servlet-name>
	    <url-pattern>/test</url-pattern>
	  </servlet-mapping>
	  <welcome-file-list>
	    <welcome-file>index.html</welcome-file>
	    <welcome-file>index.htm</welcome-file>
	    <welcome-file>index.jsp</welcome-file>
	    <welcome-file>default.html</welcome-file>
	    <welcome-file>default.htm</welcome-file>
	    <welcome-file>default.jsp</welcome-file>
	  </welcome-file-list>
	</web-app>

servlet-mapping与servlet标签获取url到指定的class内

2.无状态bean构建

接口, @Local标签设定为本地无状态bean

	@Local
	public interface A_TestProject_EJBLocal {
		public void sayHello();
	}

实现类	

	@Stateless
	public class A_TestProject_EJB implements A_TestProject_EJBLocal {
		/**
	     * Default constructor. 
	     */
	    public A_TestProject_EJB() {
	        
	    }
		@Override
		public void sayHello() {
			System.out.println("hello kepler");
		}
	}

3.servlet
	
	public class Test extends HttpServlet {
		@EJB
		A_TestProject_EJBLocal SayHelloLocalRef;
	
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			SayHelloLocalRef.sayHello();
		}
	}

@EJB获取本地无状态Bean，注入到SayHelloLocalRef引用内，http get方法执行doGet内的业务

####2.本地和远程无状态会话Bean的区别

如果提供服务的组件与调用服务的组件在一个JVM内，则为本地无状态会话Bean,否则为远程

如果将EJB工程与Web工程共同构建一个Enterprise Application Project，共同运行，则为本地；
如果EJB部署到WebLogic， Web部署到tomcat，则为远程

**注意，如果使用远程Bean, 需要在@Stateless注解添加mappedName属性，属性值代表在WebLogic中注册JNDI节点的名称**

####3.无状态会话Bean的回调函数和生命周期

无状态会话Bean的回调函数有两种：@PostConstruct和@PreDestroy

无状态会话Bean的生命周期只有两个状态：不存在（does not exist）状态，与准备被调用（method ready pool）状态，由does not exist转为method ready pool执行@PostConstruct注解定义的方法，由method ready pool转为does not exist调用@PreDestroy注解定义的方法

**注意，不要在EJB无状态会话Bean内执行带有写的功能的业务，因为非线程安全**

新的请求进入WebLogic容器后，会判断当前无状态会话Bean池中有没有空闲的会话Bean，如果没有则创建一个，有则复用

####4.有状态会话Bean基本流程

类似于Servlet内的HttpSession对象，该对象可以在服务器端以sessionid为标识记录客户端的用户信息，EJB也能实现这种有状态的会话Bean

有状态会话Bean不共享，每个线程使用自己的，一个客户使用一个有状态会话Bean

1.接口
	
	@Local
	public interface ShopCartLocal {
		public void add(String shopname);
		public void delete(int shopIndex);
		public int getCartSize();
	}

2.接口实现
	
	@Stateful
	public class ShopCart implements ShopCartLocal {
	    public ShopCart() {
	        System.out.println("创建了1个新的ShopCart实例！");
	    }
	    
	    private ArrayList shopList = new ArrayList();
	
		@Override
		public void add(String shopname) {
			shopList.add(shopname);
		}
	
		@Override
		public void delete(int shopIndex) {
			shopList.remove(shopIndex);
		}
	
		@Override
		public int getCartSize() {
			return shopList.size();
		}
	}

@Stateful定义为有状态会话bean

3.servlet
	
	public class Test extends HttpServlet {
		private static final long serialVersionUID = 1L;
		public void doGet(HttpServletRequest request, HttpServletResponse response) {
			String shopName = request.getParameter("shopname");
			ShopCartLocal local = (ShopCartLocal) request.getSession()
					.getAttribute("shopList");
			if (local == null) {
				System.out.println("第一次运行");
				try {
					Context env = new InitialContext();
					local = (ShopCartLocal) env
							.lookup("java:comp/env/myStateFulSessionBean");
					local.add(shopName);
					request.getSession().setAttribute("shopList", local);
					System.out.println("购物车中商品数量：" + local.getCartSize());
				} catch (NamingException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("不是第一次运行");
				local.add(shopName);
				request.getSession().setAttribute("shopList", local);
				System.out.println("购物车中的商品数量：" + local.getCartSize());
			}
		}
	}

把远程或本地中有状态会话Bean存储到HttpSession对象中和把普通Service服务类存储到HttpSession对象有什么区别？

如果使用EJB，可以将业务具体实现对客户端隐藏，以服务形式让其他组件调用，而普通service需要在Servlet内实现业务，EJB更利于解耦与封装

有状态会话Bean负责关联某一个用户，每当有一个新的会话创建时，都要重新构建一个有状态会话Bean，不可重用，线程安全

**注意，调用本地会话Bean，在接口内使用@Local注解，定义远程Bean使用@Remote**

####5.有状态会话Bean的钝化与激活

1.钝化:JavaEE容器发现一些不经常使用的有状态会话Bean，就将这些Bean序列化到disk内，减小内存消耗

2.激活:对disk内的有状态会话Bean,进行反序列化，放入内存，共业务使用

3.回调函数：

>@PostConstruct：实例化后调用一次

>@PreDestroy：对象销毁前调用一次

>@PrePassivate:钝化前调用一次

>@PostActivate:激活完成后调用一次

>@Remove：对象从内存中删除调用一次，释放资源

4.JMS Java Message Service
>消息驱动，解耦降低组件之间的关联，互相仅以消息进行通讯；异步方式发送接收消息，消息进行排队处理，适合于并发环境，WebLogic可以配置消息驱动服务器，也可以使用ActiveMQ, Kafka等消息驱动组件

5.计时器与作业调度

EJB支持计时器（Timer类），与作业调度功能

##2.JPA Java Persistence Application
Java持久层应用接口 Hibernate实现类JPA接口，还有其他的持久层框架

1.为什么要使用JNDI（Java Naming Directory Interface Java命名目录接口
>实现不同框架对对象处理方式的一致性

2.Myeclipse 构建EJB逆向工程

>1.首先对EJB工程添加逆向工程关联，构建项目，myeclipse -> project Facets -> install JPA facets, 构建JPA接口关联

>2.通过Myeclipse Database Explorer，对EJB工程逆向添加映射

生成的实体类如下：
	
	@Entity
	@Table(name = "student", catalog = "kepler_db")
	public class Student implements java.io.Serializable {
		// Fields
		private Integer userId;
		private String password;
		private String userName;
		// Constructors
		/** default constructor */
		public Student() {
		}
		/** full constructor */
		public Student(String password, String userName) {
			this.password = password;
			this.userName = userName;
		}
		@Id
		@GeneratedValue(strategy = IDENTITY)
		@Column(name = "userId", unique = true, nullable = false)
		public Integer getUserId() {
			return this.userId;
		}
		public void setUserId(Integer userId) {
			this.userId = userId;
		}
		@Column(name = "password")
		public String getPassword() {
			return this.password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		@Column(name = "userName")
		public String getUserName() {
			return this.userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
	}

如果映射Oracle，需要在主键id属性上添加自增注解

	@SequenceGenerator(name = "idautoRef" sequenceName="idauto")
	@GeneratedValue(strategy = GeneratorType.SEQUENCE, generator = "idautoRef")

sequenceName属性代表的是oracle自增序列名，name代表序列对象在EJB中的别名，也就是主键生成器的名称

strategy = GeneratorType.SEQUENCE代表主键生成策略是用序列，generator代表用哪个生成器生成主键，只要和@SequenceGenerator注解的name相同就可以

servlet:
	
	public class test extends HttpServlet {
		@EJB
		UserinfoFacadeLocal userinfoFacadeLocal;
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
	
			Userinfo userinfo = new Userinfo();
			userinfo.setUsername("gaohongyan");
			userinfo.setPassword("gaohongyan88");
			userinfo.setAge(90L);
			userinfoFacadeLocal.save(userinfo);
		}
	}

####1.WebLogic配置JNDI数据源
在weblogic控制台下，服务节点，数据源节点下，建立JNDI数据源

####2.WebLogic中实现JDBC + JNDI全局性分布式事务

JTA Java Transaction API 全局性事务，保证多个数据源间数据的同步性，即有一处出现意外导致数据不全，事务回滚，将所有数据源恢复到原先状态

2PC 2 phase 两阶段提交
>收集阶段：事务管理器发送命令告诉每个source DB进行事务提交，是一个准提交，预提交的状态

>提交回滚阶段：事务管理器收集完每个DB的事务提交后，如果发现有一个DB有异常，则发送命令，告诉所有的DB进行事务回滚

####3.KODO
WebLogic的JPA接口实现ORM的框架，运行稳定 OpenJPA也是由KODO演化而来

##3.JPA核心

EntityManager类的主要功能就是通过管理实体类而转化对数据表中的数据进行操作

基本过程：presistence.xml文件取得一个数据库连接对象后，将这个数据库连接注入@PresistenceContext注解的EntityManagerFactory对象中，再使用EntityManagerFactory对象创建一个EntityManager对象并注入，这样EntityManager就有操作数据库的能力

1.presistence.xml
	
	<?xml version="1.0" encoding="UTF-8"?>
	<persistence xmlns="http://java.sun.com/xml/ns/persistence"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://java.sun.com/xml/ns/persistence
	    http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd"
		version="1.0">
		<persistence-unit name="ejb3_3_2EJBPU" transaction-type="JTA">
			<properties>
				<property name="kodo.ConnectionURL"
					value="jdbc:oracle:thin:@localhost:1522:accp11g" />
				<property name="kodo.ConnectionDriverName"
					value="oracle.jdbc.driver.OracleDriver" />
				<property name="kodo.ConnectionUserName" value="y246" />
				<property name="kodo.ConnectionPassword" value="y246" />
				<property name="kodo.Log"
					value="DefaultLevel=WARN, Tool=INFO" />
			</properties>
	
		</persistence-unit>
	</persistence>

2.EntityManager注入
	
	@Stateless
	public class GhyTestFacade implements GhyTestFacadeLocal, GhyTestFacadeRemote {
		@PersistenceContext
		private EntityManager entityManager;
	}

3.EntityManager操作数据库
	
	public void createNativeQuery1Update() {
		System.out.println("--------执行了createNativeQuery1Update方法开始--------");
		Query updateQuery = entityManager
				.createNativeQuery("update jpatest set username='zzzzzz' where id=2003");
		updateQuery.executeUpdate();
		System.out.println("--------执行了createNativeQuery1Update方法结束--------");
	}

4.通过代码显示使用EntityManagerFactory创建EntityManager对象

	@Stateless
	public class GetEntityManagerFactoryTestLocal implements
			GetEntityManagerFactoryTestLocalLocal {
	
		@PersistenceUnit(unitName = "ejb3_3_7EJBPU")
		private EntityManagerFactory emf;
		private EntityManager em;
	
		public void save(Table36 t36) {
			em = emf.createEntityManager();
			em.persist(t36);
		}
	
	}

5.EJB DB数据一对多， 多对一

Entity中配置多对一
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHENGID")
	public Sheng getSheng() {
		return this.sheng;
	}

配置一对多
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sheng")

fetch = FetchType.LAZY代表懒加载

cascade代表级联属性，cascade = CascadeType.ALL代表主表删除时，关联的子表数据一并删除

##4.JPQL Java Persistence Query Language
持久层查询语句，类似于Hibernate的HQL

1.参数索引式查询
>参数索引式查询和JDBC中的“？”问号占位符功能相似，JPQL支持这种特性，避免sql注入

>Query对象调用setparameter(int index, new Long(1))在对应的索引处设置数值

2.JPQL支持命名式参数查询
	
	Query query = entityManager.createQuery("select m from table where id=:findid");
	query.setParameter("findid", new Long(2))

3.JPQL支持链接查询，“=”， 与关联查询 “join”，并且支持各种常用运算符， not运算符， “对当前查询条件进行取反”

4.JPQL支持对结果集进行排序 order by

5.JPQL支持各种聚合函数：avg count max min sum

6.JPQL支持对数据进行分组与分组后条件过滤：	group by having； distinct去除重复数据

7.JPQL支持对字符串的各种处理，截取subString, 拼接concat, 去除指定字符串trim, 转大小写lower upper, 返回位置locate等

8、JPQL支持4个数学函数:abs()绝对值， sqrt()平方根， mod()返回余数， size()取集合的元素数量

9.JPQL的分页
	
	query.setFirstResult(startLocation); //设置起始位置
	query.setMaxResults(maxResult); 	//从起始位置开始读多少数据

##5.FreeMarker

1.test.ftl
	
	<html> 
		<head> 
		    <title>Welcome!</title> 
		</head> 
		<body> 
		    <h1>Welcome ${user}!</h1> 
		    <p>Our latest product: 
		    <a href="${url}">${name}</a>! 
		</body> 
	</html>

设置输出字符格式

2.测试类
	
	public class Test {
		private Configuration cfg; // 模版配置对象
	
		public void init() throws Exception {
			String path = Test.class.getClassLoader().getResource("").getPath()
					.substring(1);
			// 初始化FreeMarker配置
			// 创建一个Configuration实例
			cfg = new Configuration();
			// 设置FreeMarker的模版文件夹位置
			cfg.setDirectoryForTemplateLoading(new File(path));
		}
	
		public void process() throws Exception {
			// 构造填充数据的Map
			Map map = new HashMap();
			map.put("user", "lavasoft");
			map.put("url", "http://www.baidu.com/");
			map.put("name", "百度");
			// 创建模版对象
			Template t = cfg.getTemplate("test.ftl");
			// 在模版上执行插值操作，并输出到制定的输出流中
			t.process(map, new OutputStreamWriter(System.out));
		}
	
		public static void main(String[] args) throws Exception {
			Test hf = new Test();
			hf.init();
			hf.process();
		}
	}