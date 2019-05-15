#KMeans(K均值算法)
###聚类算法的一种，属于硬聚类算法，是典型的基于原型的目标函数聚类方法的代表，较为简单高效

K_Mean算法聚类的结果为局部最优，但是结果易收到初始质点位置的影响

K_mean算法执行过程

1）从N个文档随机选取K个文档作为质心

2）对剩余的每个文档测量其到每个质心的距离，并把它归到最近的质心的类

3）重新计算已经得到的各个类的质心，遍历类中的各点，如得到点A到其他类中点的距离的平均值小于该类的质心到其他类中各点的平均值，该点A即为新的质点

4）迭代2～3步直至新的质心不在变化或者小于指定阈值，满足特定条件，算法结束

###实现
	
节点类
	
	public class Node {
		private int x;
		private int y;
		public int cluster = -1;
		public int value = 0;
		public boolean isSource = false;
	
		public int getX() {
			return x;
		}
	
		public void setX(int x) {
			this.x = x;
		}
	
		public int getY() {
			return y;
		}
	
		public void setY(int y) {
			this.y = y;
		}
	
		public Node() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		public Node(int x, int y, int value) {
			super();
			this.x = x;
			this.y = y;
			this.value = value;
		}
	
		@Override
		public String toString() {
			return "Node [x=" + x + ", y=" + y + ", cluster=" + cluster
					+ ", value=" + value + ", isSource=" + isSource + "]";
		}
	}

测试类
	
	public class Kmean {
		List<Node> KmeanLists = new ArrayList<Node>();
	
		@Test
		public void test() {
			KmeanLists = this.init(KmeanLists, 10);
			System.out.println("初始化散点图");
			System.out.println(KmeanLists);
			List<Node> gatherLists = this.gather(KmeanLists, 2);
			System.out.println("质点位置");
			for (int i = 0; i < gatherLists.size(); i++) {
				if (gatherLists.get(i).isSource == true) {
					System.out.print(gatherLists.get(i) + " ");
				}
			}
			System.out.println();
			System.out.println("聚合后散点图");
			System.out.println(KmeanLists);
		}
	
		// 聚类
		public List<Node> gather(List<Node> lists, int k) {
			boolean flag = true;
			// 初始化质点
			for (int i = 0; i < k; i++) {
				int temp = (int) (Math.random() * 10);
				lists.get(temp).isSource = true;
			}
			while (flag) {
				lists = this.setClusterValue(lists);
				List<List<Node>> updateClusterLists = this.updateCluster(lists);
				List<Integer> flagLists = new ArrayList<Integer>();
				for (int i = 0; i < updateClusterLists.size(); i++) {
					Integer num = this.updateSourceNode(updateClusterLists.get(i));
					if (num == -1) {
						flagLists.add(num);
					}
				}
				if (flagLists.size() == k) {
					flag = false;
				}
			}
			return lists;
		}
	
		// 将同群属性的点划分到一个类中
		public List<List<Node>> updateCluster(List<Node> kmeanlists) {
			List<List<Node>> lists = new ArrayList<List<Node>>();
			List<Integer> clusterNumLists = new ArrayList<Integer>();
			for (int i = 0; i < kmeanlists.size(); i++) {
				if (!clusterNumLists.contains(kmeanlists.get(i).cluster)) {
					clusterNumLists.add(kmeanlists.get(i).cluster);
				}
			}
			for (int i = 0; i < clusterNumLists.size(); i++) {
				List<Node> childrenLists = new ArrayList<Node>();
				for (int j = 0; j < kmeanlists.size(); j++) {
					if (kmeanlists.get(j).cluster == clusterNumLists.get(i)) {
						childrenLists.add(kmeanlists.get(j));
					}
				}
				lists.add(childrenLists);
			}
			return lists;
		}
	
		// 设置点的群属性
		public List<Node> setClusterValue(List<Node> lists) {
			List<Node> sourceNodeList = new ArrayList<Node>();
			for (int i = 0; i < lists.size(); i++) {
				if (lists.get(i).isSource == true) {
					sourceNodeList.add(lists.get(i));
				}
			}
			for (int i = 0; i < lists.size(); i++) {
				Node node = lists.get(i);
				Node tempNode = sourceNodeList.get(0);
				double distance = this.getDistance(node, sourceNodeList.get(0));
				for (int j = 0; j < sourceNodeList.size(); j++) {
					double distance1 = this
							.getDistance(node, sourceNodeList.get(j));
					if (distance > distance1) {
						tempNode = sourceNodeList.get(j);
					}
				}
				node.cluster = tempNode.value;
			}
			return lists;
		}
	
		// 更新原点位置
		public Integer updateSourceNode(List<Node> clusterLists) {
			Node node = new Node();
			for (int i = 0; i < clusterLists.size(); i++) {
				if (clusterLists.get(i).isSource == true) {
					node = clusterLists.get(i);
					break;
				}
			}
			double avgDistance = this.getAvgDistance(clusterLists);
			for (int i = 0; i < clusterLists.size(); i++) {
				double tempAvgDistance = this.getAvgDistance(clusterLists);
				if (tempAvgDistance < avgDistance) {
					clusterLists.get(i).isSource = true;
					node.isSource = false;
					return 1;
				}
			}
			return -1;
		}
	
		// 获取集合内各点到质点距离的平均值
		public double getAvgDistance(List<Node> clusterLists) {
			Node sourceNode = new Node();
			for (int i = 0; i < clusterLists.size(); i++) {
				Node node = clusterLists.get(i);
				sourceNode = node;
				break;
			}
			double totalDistance = 0.0;
			double avgDistance = 0.0;
			for (int i = 0; i < clusterLists.size(); i++) {
				Node node = clusterLists.get(i);
				int x = node.getX() - sourceNode.getX();
				int y = node.getY() - sourceNode.getY();
				double distance = Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);
				totalDistance = totalDistance + distance;
			}
			avgDistance = totalDistance / clusterLists.size();
			return avgDistance;
		}
	
		// 得到两点之间的距离
		public double getDistance(Node a, Node b) {
			double x = a.getX() - b.getX();
			double y = a.getY() - b.getY();
			return Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);
		}
	
		// 初始化图
		public List<Node> init(List<Node> lists, int num) {
			int tempNum = 0;
			for (int i = 0; i < num; i++) {
				int x = (int) (Math.random() * 100000);
				int y = (int) (Math.random() * 100000);
				Node node = new Node(x, y, i);
				lists.add(node);
			}
			return lists;
		}
	}

