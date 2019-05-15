#Dijkstra算法（迪杰特斯拉算法）
典型的单源最短路径算法，用于计算一个节点到其他节点的最短路径

主要特点：以起始点为中心向外层层扩展，知道扩展到终点为止
	
##Dijkstra实现
	
测试类

	public class Demo {

		public Map<Integer, Node> dijtstraMap = new HashMap<Integer, Node>();
	
		@Test
		public void test() {
			Demo demo = new Demo();
			dijtstraMap = demo.initdijtstraMap();
			Integer update = demo.update(dijtstraMap, 1, 16);
			System.out.print(update);
		}
		
		//循环更新有向图节点到起始点距离
		public Integer update(Map<Integer, Node> map, int beginNum, int endNum){
			List<Node> needUpdateDistanceNodeLists = new ArrayList<Node>();
			List<Node> tempNodeLists = new ArrayList<Node>();
			boolean flag = true;
			Node beginNode = map.get(beginNum);
			Node endNode = map.get(endNum);
			beginNode.isOut = true;
			endNode.isEnd = true;
			tempNodeLists.add(beginNode);
			while(flag){
				needUpdateDistanceNodeLists.clear();
				for(int i = 0; i < tempNodeLists.size(); i ++){
					Node node = tempNodeLists.get(i);
					for(int j = 0; j < node.distanceMapKey.size(); j ++){
						Node node2 = dijtstraMap.get(node.distanceMapKey.get(j));
						if(!needUpdateDistanceNodeLists.contains(node2) && node2.isOut == false){
							needUpdateDistanceNodeLists.add(node2);
						}
					}
				}
				tempNodeLists.clear();
				for(int i = 0; i < needUpdateDistanceNodeLists.size(); i ++){
					Node node = needUpdateDistanceNodeLists.get(i);
					node = this.updateDistance(node);
					tempNodeLists.add(node);
				}
				for(int i = 0; i < tempNodeLists.size(); i ++){
					if(tempNodeLists.get(i).isEnd == true){
						return tempNodeLists.get(i).totalDistance;
					}
				}
			}
			return -1;
		}
		
		//单个节点更新到起点的距离
		public Node updateDistance(Node node){
			int tempDistance = 0;
			List<Node> lists = new ArrayList<Node>();
			for(int i = 0; i < node.distanceMapKey.size(); i ++){
				Node tempNode = dijtstraMap.get(node.distanceMapKey.get(i));
				if(tempNode.isOut == true){
					lists.add(tempNode);
				}
			}
			tempDistance = lists.get(0).getDistanceMap().get(node.getValue()) + lists.get(0).getTotalDistance();
			for(int i = 0; i < lists.size(); i ++){
				Node node2 = lists.get(i);
				for(int j = 0; j < node2.distanceMapKey.size(); j ++){
					Integer key = node2.distanceMapKey.get(j);
					if(tempDistance >= node2.distanceMap.get(node.getValue()) + node2.totalDistance && dijtstraMap.get(key).isOut == true){
						tempDistance = node2.distanceMap.get(node.getValue()) + node2.totalDistance;
					}
				}
			}
			node.totalDistance = tempDistance;
			node.isOut = true;
			return node;
		}
		
		//初始化有向图
		public Map<Integer, Node> initdijtstraMap(){
			Demo demo = new Demo();
			dijtstraMap = demo.addNode(1, 2, 6);
			dijtstraMap = demo.addNode(2, 3, 15);
			dijtstraMap = demo.addNode(3, 4, 10);
			dijtstraMap = demo.addNode(1, 5, 2);
			dijtstraMap = demo.addNode(2, 6, 9);
			dijtstraMap = demo.addNode(3, 7, 7);
			dijtstraMap = demo.addNode(4, 8, 7);
			dijtstraMap = demo.addNode(5, 6, 13);
			dijtstraMap = demo.addNode(6, 7, 3);
			dijtstraMap = demo.addNode(7, 8, 13);
			dijtstraMap = demo.addNode(5, 9, 7);
			dijtstraMap = demo.addNode(6, 10, 8);
			dijtstraMap = demo.addNode(7, 11, 17);
			dijtstraMap = demo.addNode(8, 12, 17);
			dijtstraMap = demo.addNode(9, 10, 10);
			dijtstraMap = demo.addNode(10, 11, 6);
			dijtstraMap = demo.addNode(11, 12, 1);
			dijtstraMap = demo.addNode(9, 13, 4);
			dijtstraMap = demo.addNode(10, 14, 2);
			dijtstraMap = demo.addNode(11, 15, 8);
			dijtstraMap = demo.addNode(12, 16, 10);
			dijtstraMap = demo.addNode(13, 14, 8);
			dijtstraMap = demo.addNode(14, 15, 9);
			dijtstraMap = demo.addNode(15, 16, 6);
			return dijtstraMap;
		}
		
		//有向图添加节点
		public Map<Integer, Node> addNode(int desNum, int srcNum, int distance) {
			Node desNode = dijtstraMap.get(desNum);
			if (desNode == null) {
				desNode = new Node(desNum, false);
			}
			Node srcNode = new Node(srcNum, false);
			desNode.distanceMap.put(srcNum, distance);
			desNode.distanceMapKey.add(srcNum);
			srcNode.distanceMap.put(desNum, distance);
			srcNode.distanceMapKey.add(desNum);
			dijtstraMap.put(srcNum, srcNode);
			dijtstraMap.put(desNum, desNode);
			return dijtstraMap;
		}
	}

节点类
	
	public class Node {
		private int value;
		public boolean isOut = false;
		public boolean isEnd = false;
		public int totalDistance = 0;
		public Map<Integer, Integer> distanceMap = new HashMap<Integer, Integer>();
		public List<Integer> distanceMapKey = new ArrayList<Integer>();
	
		public int getValue() {
			return value;
		}
	
		public void setValue(int value) {
			this.value = value;
		}
	
	
		public Map<Integer, Integer> getDistanceMap() {
			return distanceMap;
		}
	
		public void setDistanceMap(Map<Integer, Integer> distanceMap) {
			this.distanceMap = distanceMap;
		}
	
		public List<Integer> getDistanceMapKey() {
			return distanceMapKey;
		}
	
		public void setDistanceMapKey(List<Integer> distanceMapKey) {
			this.distanceMapKey = distanceMapKey;
		}
	
		public int getTotalDistance() {
			return totalDistance;
		}
	
		public void setTotalDistance(int totalDistance) {
			this.totalDistance = totalDistance;
		}
	
		public Node(int value, boolean isOut) {
			this.value = value;
			this.isOut = isOut;
		}
	
		public Node() {
			super();
		}
	
		@Override
		public String toString() {
			return "Node [value=" + value + ", isOut=" + isOut + ", totalDistance="
					+ totalDistance + "]";
		}
	
	}

注：现只可判断终点到起点同一方向最小值，如需要转向不能判断

#Floyd-Warshall算法
解决任意两点间的最短路径的一种算法，可以正确处理有向图或负权的最短路径问题，同时也被用于计算有向图的传递闭包

#####Floyd算法是一个经典的动态规划算法， 假设D(i,j)为点i到点j的最短路径距离，对于每一个节点k，检查D(i,k) + D(k,j) < D(i,j),如果成立，证明从i到k再到j的路径比i直接到j短，设置D(i,j) = D(i,k) + D(k,j),当遍历所有的节点后，D(i,j)即为点i到点j的最短路径距离

#####借助一个辅助二维数组Path，如果Path[A][B] = P,说明A点到B点的最短路径是A->...->P->B,这样一来，在此之上，查找Path（AP）的值为L，接着查找Path(AL),如果Path(AL)=A查找结束，最短路径为A->L->P->B.一下代码辅助数组为spot[][]

实现（百度）
	
	//以无向图G为入口，得出任意两点之间的路径长度length[i][j]，路径path[i][j][k]，
	//途中无连接得点距离用0表示，点自身也用0表示
	public class Floyd {
		int[][] length = null;// 任意两点之间路径长度
		int[][][] path = null;// 任意两点之间的路径
	
		public static void main(String[] args) {
			int data[][] = {
					{ 0, 27, 44, 17, 11, 27, 42, 0, 0, 0, 20, 25, 21, 21, 18, 27, 0 },// x1
					{ 27, 0, 31, 27, 49, 0, 0, 0, 0, 0, 0, 0, 52, 21, 41, 0, 0 },// 1
					{ 44, 31, 0, 19, 0, 27, 32, 0, 0, 0, 47, 0, 0, 0, 32, 0, 0 },// 2
					{ 17, 27, 19, 0, 14, 0, 0, 0, 0, 0, 30, 0, 0, 0, 31, 0, 0 },// 3
					{ 11, 49, 0, 14, 0, 13, 20, 0, 0, 28, 15, 0, 0, 0, 15, 25, 30 },// 4
					{ 27, 0, 27, 0, 13, 0, 9, 21, 0, 26, 26, 0, 0, 0, 28, 29, 0 },// 5
					{ 42, 0, 32, 0, 20, 9, 0, 13, 0, 32, 0, 0, 0, 0, 0, 33, 0 },// 6
					{ 0, 0, 0, 0, 0, 21, 13, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0 },// 7
					{ 0, 0, 0, 0, 0, 0, 0, 19, 0, 11, 20, 0, 0, 0, 0, 33, 21 },// 8
					{ 0, 0, 0, 0, 28, 26, 32, 0, 11, 0, 10, 20, 0, 0, 29, 14, 13 },// 9
					{ 20, 0, 47, 30, 15, 26, 0, 0, 20, 10, 0, 18, 0, 0, 14, 9, 20 },// 10
					{ 25, 0, 0, 0, 0, 0, 0, 0, 0, 20, 18, 0, 23, 0, 0, 14, 0 },// 11
					{ 21, 52, 0, 0, 0, 0, 0, 0, 0, 0, 0, 23, 0, 27, 22, 0, 0 },// 12
					{ 21, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0 },// 13
					{ 18, 41, 32, 31, 15, 28, 0, 0, 0, 29, 14, 0, 22, 0, 0, 11, 0 },// 14
					{ 27, 0, 0, 0, 25, 29, 33, 0, 33, 14, 9, 14, 0, 0, 11, 0, 9 },// 15
					{ 0, 0, 0, 0, 30, 0, 0, 0, 21, 13, 20, 0, 0, 0, 0, 9, 0 } // 16
			};
			for (int i = 0; i < data.length; i++)
				for (int j = i; j < data.length; j++)
					if (data[i][j] != data[j][i])
						return;
			Floyd test = new Floyd(data);
			for (int i = 0; i < data.length; i++)
				for (int j = i; j < data[i].length; j++) {
					System.out.println();
					System.out.print("From " + i + " to " + j + " path is: ");
					for (int k = 0; k < test.path[i][j].length; k++)
						System.out.print(test.path[i][j][k] + " ");
					System.out.println();
					System.out.println("From " + i + " to " + j + " length :"
							+ test.length[i][j]);
				}
		}
	
		public Floyd(int[][] G) {
			int MAX = 100;
			int row = G.length;// 图G的行数
			int[][] spot = new int[row][row];// 定义任意两点之间经过的点，辅助二维数组
			int[] onePath = new int[row];// 记录一条路径
			length = new int[row][row];
			path = new int[row][row][];
			for (int i = 0; i < row; i++)
				// 处理图两点之间的路径
				for (int j = 0; j < row; j++) {
					if (G[i][j] == 0)
						G[i][j] = MAX;// 没有路径的两个点之间的路径为默认最大
					if (i == j)
						G[i][j] = 0;// 本身的路径长度为0
				}
			for (int i = 0; i < row; i++)
				// 初始化为任意两点之间没有路径
				for (int j = 0; j < row; j++)
					spot[i][j] = -1;
			for (int i = 0; i < row; i++)
				// 假设任意两点之间的没有路径
				onePath[i] = -1;
			for (int v = 0; v < row; ++v)
				for (int w = 0; w < row; ++w)
					length[v][w] = G[v][w];
			//状态方程转移代码
			for (int u = 0; u < row; ++u)
				for (int v = 0; v < row; ++v)
					for (int w = 0; w < row; ++w)
						if (length[v][w] > length[v][u] + length[u][w]) {
							length[v][w] = length[v][u] + length[u][w];// 如果存在更短路径则取更短路径
							spot[v][w] = u;// 把经过的点加入
						}
			for (int i = 0; i < row; i++) {// 求出所有的路径
				int[] point = new int[1];
				for (int j = 0; j < row; j++) {
					point[0] = 0;
					onePath[point[0]++] = i;
					outputPath(spot, i, j, onePath, point);
					path[i][j] = new int[point[0]];
					for (int s = 0; s < point[0]; s++)
						path[i][j][s] = onePath[s];
				}
			}
		}
	
		public void outputPath(int[][] spot, int i, int j, int[] onePath,
				int[] point) {// 输出i//
			// 到j//
			// 的路径的实际代码，point[]记录一条路径的长度
			if (i == j)
				return;
			if (spot[i][j] == -1)
				onePath[point[0]++] = j;
			else {
				outputPath(spot, i, spot[i][j], onePath, point);
				outputPath(spot, spot[i][j], j, onePath, point);
			}
		}
	}

时间复杂度：O(n^3)
空间复杂度：O(n^2)

时间复杂度较大，不适合做大量运算，可以求任意两点之间的最短距离，代码简单易懂

#Prim算法（普里姆算法）
#####在加权连通图里搜索最小生成树

#####由此算法搜索到的边子集所构成的树中，不但包括了连通图里的所有顶点（英语：Vertex (graph theory)），且其所有边的权值之和亦为最小。

Prim算法执行过程：

1.初始化加权图，随机找到一点为起点a，创建结果集合M， 添加a到M内

2.找出a直接连接的点中权值最小的点b,添加b到M内

3.遍历M，找出与M内点相连接的点中，距离最小的点c，添加c到M内

4.重复执行2,3 知道所有的点都在M内，结束算法

#Bellman_Ford
含负权图的单源最短路径的一种算法，效率较低，代码难度较小，Dijtstra算法在处理负权回路的图时会失效，

Bellman_Ford算法不仅可以求出最短路径，也可以检测负权回路的问题，利用已经获得到原点最短路径的点，不断更新其他未获得最短路径的点

Bellman_Ford算法执行过程

1.设图定点数n，边树m，设原点为source，数组dist[i]为原点到顶点i的最短路径，dist[source]初始化为0外，其他dist[]初始化为MAX，

2.对于每一条边arc(u,v)，如果dist[u] + w(u,v) < dist[v]，则使dist[v] = dist[u] + w(u,v),其中，w(u,v)为边arc(u,v)的权值 ,2步循环n-1次

3.遍历n的节点，只要存在一条边arc(u,v),使得dist[u] + w(u,v) < dist[v],则该图存在负权回路。






