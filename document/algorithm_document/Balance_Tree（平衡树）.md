#Balance Tree(B- B+) 二叉搜索树
##二叉搜索树
>1.所有非叶子结点至多拥有两个儿子（Left和Right）；

>2.所有结点存储一个关键字；

>3.非叶子结点的左指针指向小于其关键字的子树，右指针指向大于其关键字的子树；

普通B树在实际应用中需要考虑平衡问题， 实际使用的B树都是在原B树的基础上添加平衡性算法
	
										20
					10										30
			15				18						25				36
		

##B-树
>从算法逻辑上来讲，二叉查找树的查找速度和比较次数都是最小的，但是在现实中需要考虑磁盘IO， 磁盘页对应着索引树的节点， B-树的作用是调整二叉查找树的高度，降低IO次数，提高效率，内存中的比较耗时与磁盘IO相比可以忽略不计

>B-树是一种多路平衡查找树，每个节点最多包含k个孩子，k成为B-树的阶, k的大小取决于磁盘页的大小

m阶B-树具有如下特征

1. 根节点至少有两个子女
2. 每个中间节点都包含k-1个元素和k个孩子，其中m/2<=k<=m
3. 每个叶子节点都包含k-1个元素，其中m/2<=k<=m
4. 所有叶子节点都位于同一层
5. 每个节点中的元素从小到大排列，节点当中k-1个元素正好是k个孩子包含的元素的值域划分

							9
				2 6						12
		1		3 5		8			11		13 15
>只要树的高度足够低，IO次数足够少，就可以提升查询性能

>B-树添加删除需要进行自平衡调整， B-树主要应用于文件系统以及部分数据库索引（MongoDB），大部分关系型数据库（Mysql）则使用B+树作为索引

##B-树实现
	public class BPlusTree<Key extends Comparable<Key>, Value> {
		private static final int M = 4;
	
		private Node root; // root of the B-tree
		private int height; // height of the B-tree
		private int n; // number of key-value pairs in the B-tree
	
		// helper B-tree node data type
		private static final class Node {
			private int m; // number of children
			private Entry[] children = new Entry[M]; // the array of children
	
			// create a node with k children
			private Node(int k) {
				m = k;
			}
		}
	
		private static class Entry {
			private Comparable key;
			private Object val;
			private Node next; // helper field to iterate over array entries
	
			public Entry(Comparable key, Object val, Node next) {
				this.key = key;
				this.val = val;
				this.next = next;
			}
		}
	
		public BPlusTree() {
			root = new Node(0);
		}
	
		public boolean isEmpty() {
			return size() == 0;
		}
	
		public int size() {
			return n;
		}
	
		public int height() {
			return height;
		}
	
		public Value get(Key key) {
			if (key == null) {
				throw new NullPointerException("key must not be null");
			}
			return search(root, key, height);
		}
	
		@SuppressWarnings("unchecked")
		private Value search(Node x, Key key, int ht) {
			Entry[] children = x.children;
	
			// external node到最底层叶子结点，遍历
			if (ht == 0) {
				for (int j = 0; j < x.m; j++) {
					if (eq(key, children[j].key)) {
						return (Value) children[j].val;
					}
				}
			}
	
			// internal node递归查找next地址
			else {
				for (int j = 0; j < x.m; j++) {
					if (j + 1 == x.m || less(key, children[j + 1].key)) {
						return search(children[j].next, key, ht - 1);
					}
				}
			}
			return null;
		}
	
		public void put(Key key, Value val) {
			if (key == null) {
				throw new NullPointerException("key must not be null");
			}
			Node u = insert(root, key, val, height); // 分裂后生成的右结点
			n++;
			if (u == null) {
				return;
			}
	
			// need to split root重组root
			Node t = new Node(2);
			t.children[0] = new Entry(root.children[0].key, null, root);
			t.children[1] = new Entry(u.children[0].key, null, u);
			root = t;
			height++;
		}
	
		private Node insert(Node h, Key key, Value val, int ht) {
			int j;
			Entry t = new Entry(key, val, null);
	
			// external node外部结点，也是叶子结点，在树的最底层，存的是内容value
			if (ht == 0) {
				for (j = 0; j < h.m; j++) {
					if (less(key, h.children[j].key)) {
						break;
					}
				}
			}
	
			// internal node内部结点，存的是next地址
			else {
				for (j = 0; j < h.m; j++) {
					if ((j + 1 == h.m) || less(key, h.children[j + 1].key)) {
						Node u = insert(h.children[j++].next, key, val, ht - 1);
						if (u == null) {
							return null;
						}
						t.key = u.children[0].key;
						t.next = u;
						break;
					}
				}
			}
	
			for (int i = h.m; i > j; i--) {
				h.children[i] = h.children[i - 1];
			}
			h.children[j] = t;
			h.m++;
			if (h.m < M) {
				return null;
			} else { // 分裂结点
				return split(h);
			}
		}
	
		// split node in half
		private Node split(Node h) {
			Node t = new Node(M / 2);
			h.m = M / 2;
			for (int j = 0; j < M / 2; j++) {
				t.children[j] = h.children[M / 2 + j];
			}
			return t;
		}
	
		public String toString() {
			return toString(root, height, "") + "\n";
		}
	
		private String toString(Node h, int ht, String indent) {
			StringBuilder s = new StringBuilder();
			Entry[] children = h.children;
	
			if (ht == 0) {
				for (int j = 0; j < h.m; j++) {
					s.append(indent + children[j].key + " " + children[j].val
							+ "\n");
				}
			} else {
				for (int j = 0; j < h.m; j++) {
					if (j > 0) {
						s.append(indent + "(" + children[j].key + ")\n");
					}
					s.append(toString(children[j].next, ht - 1, indent + "   "));
				}
			}
			return s.toString();
		}
	
		// comparison functions - make Comparable instead of Key to avoid casts
		private boolean less(Comparable k1, Comparable k2) {
			return k1.compareTo(k2) < 0;
		}
	
		private boolean eq(Comparable k1, Comparable k2) {
			return k1.compareTo(k2) == 0;
		}
	
		public static void main(String[] args) {
			BPlusTree<String, String> st = new BPlusTree<String, String>();
	
			st.put("a", "1");
			st.put("b", "2");
			st.put("c", "3");
			st.put("d", "4");
			st.put("e", "5");
			st.put("f", "6");
			st.put("g", "7");
			st.put("h", "8");
			st.put("i", "9");
			st.put("j", "10");
			st.put("k", "11");
			st.put("l", "12");
			st.put("m", "13");
			st.put("n", "14");
			st.put("o", "15");
			st.put("p", "16");
			st.put("q", "17");
			System.out.println();
			System.out.println("size:  " + st.size());
			System.out.println("height: " + st.height());
			System.out.println(st);
			System.out.println();
		}
	}

##B+树
>应用于大部分关系型数据库索引， B+树相比于B-树查询效率更高（稳定）

>在B-树的基础上，为叶子节点增加链表指针，所有关键字都在叶子节点中出现，非叶子节点作为叶子节点的索引

B-树的特征B+树都满足，B+具备一些新的特征：

1. 所有关键字都出现在叶子节点的链表中，且链表中的关键字恰好是有序的
2. 不可能在非叶子节点命中，非叶子节点只做为查找叶子节点的条件，相当于叶子节点的索引，叶子节点相当于存储数据的数据层
3. B+树更适合文件索引系统

######B+树与B树相比：
1. B+树的磁盘读写代价更低：B+树的内部节点并没有指向关键字具体信息的指针，因此内部节点相对B-树更小，如果把所有同一内部节点的关键字存放在同一盘快中，那么盘快所能容纳的关键字数量也就越多，一次性读入内存的需要查询的关键字也越多，IO次数越小。
2. B+树查询效率更稳定：B+树关键字都存储于叶子节点，非叶子节点只是叶子节点关键字的索引，所有关键字查询的路径长度相同，所以每一个数据的查询效率相当

##B+树实现
######工具类（util）
	public class BPlusUtil {
		public BPlusTree put(int data, BPlusTree tree) {
			BPlusNode node = this.findLastNode(tree);
			if (node.getParams().length < tree.getDegree()) {
				tree = this.insertDataNotFull(data, tree);
			} else {
				tree = this.splitTree(data, tree);
			}
			return tree;
		}
		
		//初始化B+树
		public BPlusTree validate() {
			BPlusTree tree = new BPlusTree();
			BPlusNode rootNode = new BPlusNode(true, false, new int[] { 1 });
			BPlusNode node = new BPlusNode(false, true, new int[] { 1 });
			node.setParentNode(rootNode);
			BPlusNode[] args = new BPlusNode[3];
			args[0] = node;
			rootNode.setChildrenNodes(args);
			tree.setRootNode(rootNode);
			return tree;
		}
		
		//添加节点数值（未满）
		public BPlusTree insertDataNotFull(int data, BPlusTree tree) {
			BPlusNode tempNode = this.findLastNode(tree);
			int[] temp_params = new int[tempNode.getParams().length + 1];
			System.arraycopy(tempNode.getParams(), 0, temp_params, 0,
					tempNode.getParams().length);
			temp_params[tempNode.getParams().length] = data;
			tempNode.setParams(temp_params);
			return tree;
		}
		
		//重构树（节点数值已满）
		public BPlusTree splitTree(int data, BPlusTree tree) {
			BPlusNode leafNode = new BPlusNode(false, true, new int[] { data });
			BPlusNode tempNode = this.findLastNode(tree);
			tempNode.setNextNode(leafNode);
			leafNode.setLastNode(tempNode);
			int num = 0;
			while (tempNode.getParams().length == tree.getDegree()) {
				tempNode = tempNode.getParentNode();
				num++;
			}
			int[] temp_params = new int[tempNode.getParams().length + 1];
			System.arraycopy(tempNode.getParams(), 0, temp_params, 0,
					tempNode.getParams().length);
			temp_params[tempNode.getParams().length] = data;
			tempNode.setParams(temp_params);
			BPlusNode[] childrenNodes = tempNode.getChildrenNodes();
			int temp_num = 0;
			for (int i = 0; i < childrenNodes.length; i ++) {
				if (childrenNodes[i] != null) {
					continue;
				}
				temp_num = i;
				childrenNodes[i] = this.makeLink(data, tempNode, num - 1, 0);
				break;
			}
			tempNode.getChildrenNodes()[temp_num] = leafNode;
			tempNode.getChildrenNodes()[temp_num].setParentNode(tempNode);
			return tree;
		}
		
		//查找需要添加值的节点
		private BPlusNode findLastNode(BPlusTree tree) {
			BPlusNode rootNode = tree.getRootNode();
			return this.findNeedNode(rootNode);
		}
		
		private BPlusNode findNeedNode(BPlusNode node){
			BPlusNode[] childrenNodes = node.getChildrenNodes();
			for(BPlusNode tempNode : childrenNodes){
				if(tempNode.getNextNode() == null && tempNode.isLeaf()){
					return tempNode;
				}
				continue;
			}
			return this.findNeedNode(node);
		}
		
		//添加重构后节点的后续节点
		private BPlusNode makeLink(int data, BPlusNode root, int time, int now) {
			if (now < time) {
				BPlusNode node = new BPlusNode(false, false, new int[] { data });
				BPlusNode[] childrenNodes = root.getChildrenNodes();
				if (!(childrenNodes == null)) {
					for (BPlusNode tempNode : childrenNodes) {
						if (tempNode != null) {
							continue;
						}
						tempNode = node;
						break;
					}
				} else {
					BPlusNode[] argsNodes = new BPlusNode[] { node };
					root.setChildrenNodes(argsNodes);
				}
				this.makeLink(data, node, time, now + 1);
			}
			return root;
		}
	
	}

######节点类
	public class BPlusTree {
		private int degree = 3;
		private int hight = 2;
		private BPlusNode rootNode;
		
		public BPlusTree(int degree, int hight, BPlusNode rootNode) {
			super();
			this.degree = degree;
			this.hight = hight;
			this.rootNode = rootNode;
		}
		
		public void show(BPlusNode node){
			BPlusNode[] childrenNodes = node.getChildrenNodes();
			for(int i = 0; i < childrenNodes.length; i ++){
				if(childrenNodes[i] != null){
					if(!childrenNodes[i].isLeaf() && !childrenNodes[i].isRoot()){
						System.out.print(childrenNodes[i] + " ");
						this.show(childrenNodes[i]);
					}else if(childrenNodes[i].isLeaf()){
						System.out.print(childrenNodes[i] + " ");
					}
				}else{
					System.out.print("NULL" + " ");
				}
			}
			System.out.println();
		}
	
		public BPlusTree() {
			super();
		}
	
		public int getDegree() {
			return degree;
		}
	
		public void setDegree(int degree) {
			this.degree = degree;
		}
	
		public int getHight() {
			return hight;
		}
	
		public void setHight(int hight) {
			this.hight = hight;
		}
	
		public BPlusNode getRootNode() {
			return rootNode;
		}
	
		public void setRootNode(BPlusNode rootNode) {
			this.rootNode = rootNode;
		}
	}

######测试类
	public class BPlusTest {
		static BPlusUtil util = new BPlusUtil();
		
		public static void main(String[] args){
			BPlusTree tree = util.validate();
			BPlusTest test = new BPlusTest();
			test.show(2, tree);
			test.show(3, tree);
			test.show(4, tree);
			test.show(5, tree);
			test.show(6, tree);
			test.show(7, tree);
			test.show(8, tree);
			test.show(9, tree);
			test.show(10, tree);
		}
		public void show(int num, BPlusTree tree){
			util.put(num, tree);
			this.showRoot(tree);
			tree.show(tree.getRootNode());
		}
		public void showRoot(BPlusTree tree){
			System.out.print("B+树根节点：");
			System.out.println(tree.getRootNode());
		}
	}
######结果
	B+树根节点：[1 ]
	[1 2 ] NULL NULL 
	B+树根节点：[1 ]
	[1 2 3 ] NULL NULL 
	B+树根节点：[1 4 ]
	[1 2 3 ] [4 ] NULL 
	B+树根节点：[1 4 ]
	[1 2 3 ] [4 5 ] NULL 
	B+树根节点：[1 4 ]
	[1 2 3 ] [4 5 6 ] NULL 
	B+树根节点：[1 4 7 ]
	[1 2 3 ] [4 5 6 ] [7 ] 
	B+树根节点：[1 4 7 ]
	[1 2 3 ] [4 5 6 ] [7 8 ] 
	B+树根节点：[1 4 7 ]
	[1 2 3 ] [4 5 6 ] [7 8 9 ] 
	Exception in thread "main" java.lang.NullPointerException
		at bplustree.BPlusUtil.splitTree(BPlusUtil.java:43)
		at bplustree.BPlusUtil.put(BPlusUtil.java:10)
		at bplustree.BPlusTest.show(BPlusTest.java:23)
		at bplustree.BPlusTest.main(BPlusTest.java:17)
住：代码存在问题，树高为3时添加错误
##B*树
>在B+树的基础上，为非叶子节点也增加链表指针，将节点的最低利用率从1/2提高到2/3

##R树
##R树实现
>多维平衡树， R树是B树的高维空间扩展，每个R树的叶子节点包含了多个指向不同数据的指针

>应用于商用数据库的空间数据索引, 地图查找地点索引

>当需要进行一个高维空间查询时，只需要遍历少数几个叶子节点所包含的指针，查看这些指针指向的数据是否满足要求，
>不必遍历所有坐标数据

>R树采用最小边界矩形（Minimal Bounding Rectangle), 从叶子结点开始用矩形（rectangle）将空间框起来，结点越往上，框住的空间就越大，以此对空间进行分割, 适用于2至6维的数据

>R树变体：R*树 R+树 压缩R树等