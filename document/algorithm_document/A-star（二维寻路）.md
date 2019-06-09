#A*算法（A_star）
>静态路网中求解最短路径最有效的直接搜索算法（最有效的直接搜索算法）

>预处理寻路算法（ALT, CH, HL等）

>基本公式：F(n) = G(n) + H(n) G(n):起始点到当前点步数  H（n）:当前点到目标点步数
>基本原则为寻找F(n)最小的路径

>递归

##实现代码

节点实现

    public class Node {
	private int local_X;
	private int local_Y;
	private int F;
	private int H;
	private int G;
	private Node parent_node;
	private Node child_node;
	
	public Node(int local_X, int local_Y) {
		super();
		this.local_X = local_X;
		this.local_Y = local_Y;
	}
	public int getF() {
		return F;
	}
	public void setF(int f) {
		F = f;
	}
	public int getH() {
		return H;
	}
	public void setH(int h) {
		H = h;
	}
	public int getG() {
		return G;
	}
	public void setG(int g) {
		G = g;
	}
	public Node getParent_node() {
		return parent_node;
	}
	public void setParent_node(Node parent_node) {
		this.parent_node = parent_node;
	}
	public Node getChild_node() {
		return child_node;
	}
	public void setChild_node(Node child_node) {
		this.child_node = child_node;
	}
	public int getLocal_X() {
		return local_X;
	}
	public void setLocal_X(int local_X) {
		this.local_X = local_X;
	}
	public int getLocal_Y() {
		return local_Y;
	}
	public void setLocal_Y(int local_Y) {
		this.local_Y = local_Y;
	}
	@Override
	public String toString() {
		return "Node [local_X=" + local_X + ", local_Y=" + local_Y + "]";
	}
	｝

寻路实现

    public class A_star_util {
	
		private List<Node> wall;
		private List<Node> openLists = new ArrayList<Node>();
		private List<Node> closeLists = new ArrayList<Node>();
		private Node startNode;
		private Node endNode;
	
		public void setStartNode(Node node) {
			this.startNode = node;
		}
	
		public void setEndNode(Node node) {
			this.endNode = node;
		}
	
		public List<Node> getCloseLists() {
			return closeLists;
		}
	
		public void setCloseLists(List<Node> closeLists) {
			this.closeLists = closeLists;
		}
		
		//设置节点G值
		public int setValue_G(Node node) {
			int X = Math.abs(node.getLocal_X() - endNode.getLocal_X());
			int Y = Math.abs(node.getLocal_Y() - endNode.getLocal_Y());
			node.setG(X + Y);
			return X + Y;
		}
		
		//设置节点H值
		public int setValue_H(Node node) {
			int X = Math.abs(node.getLocal_X() - startNode.getLocal_X());
			int Y = Math.abs(node.getLocal_Y() - startNode.getLocal_Y());
			node.setH(X + Y);
			return X + Y;
		}
	
		public void setValue(Node node) {
			this.setValue_F(node);
			this.setValue_G(node);
			this.setValue_H(node);
		}
		
		//设置节点F值
		public int setValue_F(Node node) {
			this.setValue_G(node);
			this.setValue_H(node);
			node.setF(node.getG() + node.getH());
			return node.getF();
		}
		
		
		public Node findMinFNode(List<Node> lists) {
			Node temp = lists.get(0);
			if (lists.size() > 1) {
				for (int i = 0; i < lists.size() - 1; i++) {
					Node node = lists.get(i);
					Node node2 = lists.get(i + 1);
					this.setValue(node);
					this.setValue(node2);
					Node compare = this.compare(node, node2);
					temp = this.compare(compare, temp);
				}
			} else {
				return temp;
			}
			return temp;
		}
	
		public Node compare(Node x, Node y) {
			if (x.getG() < y.getG()) {
				return x;
			} else if (x.getG() > y.getG()) {
				return y;
			} else if (x.getG() == y.getG()) {
				if (x.getH() > y.getH()) {
					return x;
				} else if (x.getH() < y.getH()) {
					return y;
				} else {
					return y;
				}
			}
			return null;
		}
	
		public boolean contains(List<Node> lists, Node node){
			for(Node temp : lists){
				if((temp.getLocal_X() == node.getLocal_X()) && (temp.getLocal_Y() == node.getLocal_Y())){
					return true;
				}
			}
			return false;
		}
		
		public int setFValue(Node node) {
			int a = Math.abs(node.getLocal_X() - this.endNode.getLocal_X());
			int b = Math.abs(node.getLocal_Y() - this.endNode.getLocal_Y());
			node.setF(a + b);
			return a + b;
		}
	
		public List<Node> findNeighbors(Node node) {
			List<Node> temp_lists = new ArrayList<Node>();
			Node node1 = new Node(node.getLocal_X() + 1, node.getLocal_Y());
			Node node2 = new Node(node.getLocal_X(), node.getLocal_Y() + 1);
			Node node3 = new Node(node.getLocal_X() - 1, node.getLocal_Y());
			Node node4 = new Node(node.getLocal_X(), node.getLocal_Y() - 1);
			if (!wall.contains(node1)) {
				temp_lists.add(node1);
			}
			if (!wall.contains(node2)) {
				temp_lists.add(node2);
			}
			if (!wall.contains(node3)) {
				temp_lists.add(node3);
			}
			if (!wall.contains(node4)) {
				temp_lists.add(node4);
			}
			return temp_lists;
		}
	
		public boolean equals(Node x, Node y) {
			if ((x.getLocal_X() == y.getLocal_X())
					&& (x.getLocal_Y() == y.getLocal_Y())) {
				return true;
			} else {
				return false;
			}
		}
	
		public List<Node> getWall() {
			return wall;
		}
	
		public void setWall(List<Node> wall) {
			this.wall = wall;
		}
	
		public List<Node> getOpenLists() {
			return openLists;
		}
	
		public void setOpenLists(List<Node> openLists) {
			this.openLists = openLists;
		}
	
		public Node getStartNode() {
			return startNode;
		}
	
		public Node getEndNode() {
			return endNode;
		}
	}

测试类实现

    public class A_star_test {
		private A_star_util util = new A_star_util();
		private static List<Node> wall = new ArrayList<Node>();
		static{
			wall.add(new Node(3,2));
			wall.add(new Node(3,3));
			wall.add(new Node(3,4));
		}
		public static void main(String[] args) {
			Node start = new Node(1, 3);
			Node end = new Node(5, 3);
			new A_star_test().run(start, end);
		}
		
		public void run(Node start, Node end){
			util.setWall(wall);
			util.setStartNode(start);;
			util.setEndNode(end);
			List<Node> search = this.search(start);
			System.out.println("移动路径：");
			System.out.println(util.getStartNode().toString());
			for(int i =0; i < search.size(); i ++){
				System.out.println(search.get(i).toString());
			}
		}
		
		//递归查找路径节点
		public List<Node> search(Node node){
			List<Node> findNeighbors = util.findNeighbors(node);
			
			List<Node> lists = new ArrayList<Node>();
			for (int i = 0; i < findNeighbors.size(); i ++) {
				Node n = findNeighbors.get(i);
				for(Node m : util.getWall()){
					if ((n.getLocal_X() == m.getLocal_X() && n.getLocal_Y() == m.getLocal_Y())){
						lists.add(n);
					}
				}
				if(util.equals(node, util.getStartNode())){
					node.setParent_node(node);
				}else if(util.equals(n, node.getParent_node())){
					lists.add(n);
				}
			}
			for(Node i : lists){
				findNeighbors.remove(i);
			}
			Node minFNode = util.findMinFNode(findNeighbors);
			node.setChild_node(minFNode);
			minFNode.setParent_node(node);
			util.getOpenLists().add(minFNode);
			if(!util.contains(util.getOpenLists(), util.getEndNode())){
				search(minFNode);
			}
			return util.getOpenLists();
		}
	}
