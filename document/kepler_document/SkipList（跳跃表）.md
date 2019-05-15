#SkipList(跳跃表)
>跳跃表（skiplist）是一种基于有序链表的扩展，简称跳表，针对原链表进行索引提取

	原链表:		0———1————2————3————4————5————6————7————8————9————10————11————12————13————14
	1级索引：		1—————————3—————————5—————————7—————————9——————————11——————————13
	2级索引：				  3———————————————————7————————————————————11
	3级索引：									  7	
>新的节点可以先和2级索引比较，确定大体范围，然后在和1级索引比较，最后在和原链表比较找到并插入对应位置

>每层节点数量是上一层节点数量的一半

>提取的极限则是同一层只有两个节点的时候，这样的多层链表结构，就是跳跃表

跳跃表插入节点过程：

1. 新节点和各层索引节点逐一比较，确定原链表的插入位置,O(logN)
2. 把索引插入到原链表，O(1)
3. 利用抛硬币的随机方式，决定新节点是否提升为上一级索引，概率为50%

>跳跃表维持结构平衡的成本较低，完全依靠随机
>Redis当中的Sorted-set应用了跳跃表并进行改进

##SkipList实现
节点实现类

	public class Node {
		private int param;
		private Node indexParam;
		private Node next;
	
		public int getParam() {
			return param;
		}
	
		public void setParam(int param) {
			this.param = param;
		}
	
		public Node getIndexParam() {
			return indexParam;
		}
	
		public void setIndexParam(Node indexParam) {
			this.indexParam = indexParam;
		}
	
		public Node getNext() {
			return next;
		}
	
		public void setNext(Node next) {
			this.next = next;
		}
	
		@Override
		public String toString() {
			return "Node [param=" + param + ", indexParam=" + indexParam
					+ ", next=" + next + "]";
		}
	
		public Node(int param, Node indexParam, Node next) {
			super();
			this.param = param;
			this.indexParam = indexParam;
			this.next = next;
		}
		
		public Node(int param){
			super();
			this.param = param;
		}
	
		public Node() {
			super();
		}
	}

跳跃表实现类
	
	public class SkipList {
		List<Node> lists = new ArrayList<Node>();
	
		public List<Node> getLists() {
			return lists;
		}
	
		public void setLists(List<Node> lists) {
			this.lists = lists;
		}
	
		@Override
		public String toString() {
			return "SkipList [lists=" + lists + "]";
		}
	}

跳跃表Util
	
	public class SkipListUtil {
		SkipList skipList = new SkipList();
	
		public boolean isRise() {
			Random random = new Random();
			if (random.nextInt() > 0) {
				return true;
			} else {
				return false;
			}
		}
	
		public List<Node> add(int data, List<Node> lists) {
			List<Node> templists = this.addNode(data, lists);
			List<Node> indexLists = this.changeToSkipList(templists);
			return indexLists;
		}
	
		// 构建链表，增加节点
		public List<Node> addNode(int data, List<Node> lists) {
			Node node = new Node(data);
			if (lists == null) {
				lists = new ArrayList<Node>();
			}
			if (lists.size() == 0) {
				lists.add(node);
			} else {
				lists.get(lists.size() - 1).setNext(node);
				lists.add(node);
			}
			return lists;
		}
	
		// 递归，构建跳跃表
		public List<Node> changeToSkipList(List<Node> list) {
			if (list.size() < 3) {
				return list;
			} else {
				List<Node> tempList = new ArrayList<Node>();
				for (int i = 0; i < list.size(); i++) {
					if (this.isRise() == true) {
						Node node = list.get(i);
						tempList.add(node);
					} else {
						continue;
					}
				}
				for (int i = 0; i < tempList.size() - 1; i++) {
					tempList.get(i).setNext(tempList.get(i + 1));
				}
				return changeToSkipList(tempList);
			}
		}
	}

测试类
	
	public class SkipListTest {
		SkipListUtil util = new SkipListUtil();
		List<Node> lists = new ArrayList<Node>();
		
		@Test
		public void test(){
			this.add(1, lists);
			this.add(2, lists);
			this.add(3, lists);
			this.add(4, lists);
			this.add(5, lists);
			this.add(6, lists);
		}
		
		public void add(int data, List<Node> lists){
			List<Node> indexList = util.add(data, lists);
			System.out.println("数据链表：");
			for(int i = 0; i < lists.size(); i ++){
				System.out.print(lists.get(i).getParam() + " ");
			}
			System.out.println();
			System.out.println("顶级索引链表:");
			for(int i = 0; i < indexList.size(); i ++){
				System.out.print(indexList.get(i).getParam() + " ");
			}
			System.out.println();
		}
	}
注：构建索引链表时可能有问题
###判断一个链表是否有环

#####链表实现
节点类

	public class Node {
	protected Node next; // 指针域
	protected int data;// 数据域

	public Node(int data) {
		this.data = data;
	}

	public Node() {
		super();
	}

	// 显示此节点
	public void display() {
		System.out.print(data + " ");
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Node [next=" + next + ", data=" + data + "]";
	}
}

链表类
	
	public class LinkList {
		public Node first; // 定义一个头结点
		private int pos = 0;// 节点的位置
	
		public LinkList() {
			this.first = null;
		}
	
		// 插入一个头节点
		public void addFirstNode(int data) {
			Node node = new Node(data);
			node.next = first;
			first = node;
		}
	
		// 删除一个头结点,并返回头结点
		public Node deleteFirstNode() {
			Node tempNode = first;
			first = tempNode.next;
			return tempNode;
		}
	
		// 在任意位置插入节点 在index的后面插入
		public void add(int index, int data) {
			Node node = new Node(data);
			Node current = first;
			Node previous = first;
			while (pos != index) {
				previous = current;
				current = current.next;
				pos++;
			}
			node.next = current;
			previous.next = node;
			pos = 0;
		}
	
		// 删除任意位置的节点
		public Node deleteByPos(int index) {
			Node current = first;
			Node previous = first;
			while (pos != index) {
				pos++;
				previous = current;
				current = current.next;
			}
			if (current == first) {
				first = first.next;
			} else {
				pos = 0;
				previous.next = current.next;
			}
			return current;
		}
	
		// 根据节点的data删除节点(仅仅删除第一个)
		public Node deleteByData(int data) {
			Node current = first;
			Node previous = first; // 记住上一个节点
			while (current.data != data) {
				if (current.next == null) {
					return null;
				}
				previous = current;
				current = current.next;
			}
			if (current == first) {
				first = first.next;
			} else {
				previous.next = current.next;
			}
			return current;
		}
	
		// 显示出所有的节点信息
		public void displayAllNodes() {
			Node current = first;
			while (current != null) {
				current.display();
				current = current.next;
			}
			System.out.println();
		}
	
		// 根据位置查找节点信息
		public Node findByPos(int index) {
			Node current = first;
			if (pos != index) {
				current = current.next;
				pos++;
			}
			return current;
		}
	
		// 根据数据查找节点信息
		public Node findByData(int data) {
			Node current = first;
			while (current.data != data) {
				if (current.next == null)
					return null;
				current = current.next;
			}
			return current;
		}
	}

测试类
	
	public class TestLinkList {  
	    public static void main(String[] args) {  
	         LinkList linkList = new LinkList();  
	         linkList.addFirstNode(20);  
	         linkList.addFirstNode(21);  
	         linkList.addFirstNode(19);  
	         linkList.add(1, 22); //19,22,21,20  
	         linkList.add(2, 23); //19,22,23,21,20  
	         linkList.add(3, 99); //19,22,23,99,21,20  
	         linkList.displayAllNodes();  
	         Node node = linkList.deleteByData(19);  
	         System. out.println( "node : " + node. data);  
	         linkList.displayAllNodes();  
	         Node node1 = linkList.findByPos(0);  
	         System. out.println( "node1: " + node1. data);  
	         Node node2 = linkList.findByData(22);  
	         System. out.println( "node2: " + node2. data);  
	    }  
	}

判断链表是否有环

测试类
	
	public class TestLinkList {  
	    public static void main(String[] args) {
	    	TestLinkList test = new TestLinkList();
	    	LinkList createCircleLink = test.createCircleLink();
	    	boolean circle = test.isCircle(createCircleLink);
	    	System.out.println(circle);
	    	LinkList createNoCircleLink = test.createNoCircleLink();
	    	boolean circle2 = test.isCircle(createNoCircleLink);
	    	System.out.println(circle2);
	    }
	    
	    //产生有环链
	    public LinkList createCircleLink(){
	    	LinkList linkList = new LinkList();  
	        linkList.addFirstNode(0);
	        linkList.add(1, 10); 
	        linkList.add(2, 11);   
	        linkList.add(3, 12);
	        linkList.add(4, 13);
	        linkList.add(5, 14);
	        linkList.add(6, 15);
	        linkList.add(7, 16);
	        Node node3 = linkList.findByPos(3);
	        Node next = linkList.findByPos(7);
	        next.setNext(node3);
	        return linkList;
	    }
	    
	    //产生无环链
	    public LinkList createNoCircleLink(){
	    	LinkList linkList = new LinkList();  
	        linkList.addFirstNode(0);
	        linkList.add(1, 10); 
	        linkList.add(2, 11);   
	        linkList.add(3, 12);
	        linkList.add(4, 13);
	        linkList.add(5, 14);
	        linkList.add(6, 15);
	        linkList.add(7, 16);
	        return linkList;
	    }
	    
	    //判断链表是否有环
	    public boolean isCircle(LinkList lists){
	    	Node node1 = new Node();
	    	Node node2 = new Node();
	    	node1 = lists.first.getNext();
	    	node2 = lists.first.getNext().getNext();
	    	while(node1 != node2){
	    		node1 = node1.getNext();
	    		node2 = node2.getNext().getNext();
	    		if(node1 == null || node2 == null){
	    			return false;
	    		}
	    	}
	    	return true;
	    }
	} 
	
	