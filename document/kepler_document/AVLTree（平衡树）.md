#AVLTree(平衡二叉树)
树中任意一个节点下左右两个子树的高度差不超过1，最先发明的自平衡二叉查找树，也称为高度平衡树

如果插入的是一组有序上升或下降的数据，则一颗普通的二叉查找树必然会退化成一个单链表，AVL树因为平衡的限制，可以始终保持O（logn）的时间复杂度

不管是插入还是删除，AVLTree都会有四种失衡的情况，：左左失衡，左右失衡，右左失衡，右右失衡，每次遇到失衡，判断为那种失衡在对应处理，保证树平衡

节点类
	
	public class AVLNode<AnyType> {
		AnyType element;
		AVLNode<AnyType> left;
		AVLNode<AnyType> right;
		int height;
	
		AVLNode(AnyType theElement) {
			this(theElement, null, null);
		}
	
		AVLNode(AnyType theElement, AVLNode<AnyType> lt, AVLNode<AnyType> rt) {
			element = theElement;
			left = lt;
			right = rt;
			height = 0;
		}
	}

测试类
	
	public class AVLTree<AnyType extends Comparable<? super AnyType>> {
		private AVLNode<AnyType> root;
	
		private int height(AVLNode<AnyType> node) {
			return node == null ? -1 : node.height;
		}
	
		public AVLTree() {
			makeEmpty();
		}
	
		/**
		 * 使树为空树
		 */
		public void makeEmpty() {
			root = null;
		}
	
		/**
		 * 该树是否为空树
		 * 
		 * @return 是否空
		 */
		public boolean isEmpty() {
			return root == null;
		}
	
		/**
		 * 该树是否存在含有参数值的节点
		 * 
		 * @param value
		 *            元素值
		 * @return 是否含该元素
		 */
		public boolean contains(AnyType value) {
			return contains(value, root);
		}
	
		/**
		 * 某个节点及它的子节点是否存在含有参数值的节点
		 * 
		 * @param value
		 *            元素值
		 * @param node
		 *            节点
		 * @return
		 */
		private boolean contains(AnyType value, AVLNode<AnyType> node) {
			if (node == null) {
				return false;
			}
			int compareResult = value.compareTo(node.element);
			if (compareResult < 0) { // 插入节点值小于节点值，则递归查找左子树下
				return contains(value, node.left);
			} else if (compareResult > 0) { // 插入节点值大于节点值，则递归查找右子树下
				return contains(value, node.right);
			} else {
				return true;
			}
		}
	
		/**
		 * 查找该树最小元素值
		 * 
		 * @return 最小元素值
		 */
		public AnyType findMin() {
			if (isEmpty()) {
				throw new NullPointerException();
			}
			return findMin(root).element;
		}
	
		/**
		 * 查找某节点及其子树中的最小元素
		 * 
		 * @param node
		 *            父节点
		 * @return 最小元素所在节点
		 */
		private AVLNode<AnyType> findMin(AVLNode<AnyType> node) {
			if (node == null) {
				return null;
			} else if (node.left == null) {
				return node;
			}
			return findMin(node.left);
		}
	
		/**
		 * 查找该树最大元素值
		 * 
		 * @return 最大元素值
		 */
		public AnyType findMax() {
			if (isEmpty()) {
				throw new NullPointerException();
			}
			return findMavalue(root).element;
		}
	
		/**
		 * 查找某节点及其子树中的最大元素
		 * 
		 * @param node
		 *            父节点
		 * @return 最大元素
		 */
		private AVLNode<AnyType> findMavalue(AVLNode<AnyType> node) {
			if (node == null) {
				return null;
			} else if (node.right == null) {
				return node;
			}
			return findMavalue(node.right);
		}
		
		//添加节点
		public void insert(AnyType value) {
			root = insert(value, root);
		}
		private AVLNode<AnyType> insert(AnyType x, AVLNode<AnyType> node) {
			if (node == null) {
				return new AVLNode<AnyType>(x);
			}
			int compareResult = x.compareTo(node.element);
			if (compareResult < 0) {
				node.left = insert(x, node.left);
				if (height(node.left) - height(node.right) == 2) {
					if (x.compareTo(node.left.element) < 0) {
						node = rotateWithLeftChild(node);
					} else {
						node = doubleWithLeftChild(node);
					}
				}
			} else if (compareResult > 0) {
				node.right = insert(x, node.right);
				if (height(node.right) - height(node.left) == 2) {
					if (x.compareTo(node.right.element) > 0) {
						node = rotateWithRightChild(node);
					} else {
						node = doubleWithRightChild(node);
					}
				}
			}
			node.height = Math.max(height(node.left), height(node.right)) + 1;
			return node;
		}
	
		//调整左左失衡
		private AVLNode<AnyType> rotateWithLeftChild(AVLNode<AnyType> k2) {
			AVLNode<AnyType> k1 = k2.left;
			k2.left = k1.right;
			k1.right = k2;
			k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
			k1.height = Math.max(height(k1.left), k2.height) + 1;
			return k1;
		}
		
		//调整左右失衡
		private AVLNode<AnyType> doubleWithLeftChild(AVLNode<AnyType> k3) {
			k3.left = rotateWithRightChild(k3.left);
			return rotateWithLeftChild(k3);
		}
	
		//调整右左失衡
		private AVLNode<AnyType> rotateWithRightChild(AVLNode<AnyType> k2) {
			AVLNode<AnyType> k1 = k2.right;
			k2.right = k1.left;
			k1.left = k2;
			k2.height = Math.max(height(k2.right), height(k2.left)) + 1;
			k1.height = Math.max(height(k1.right), k2.height) + 1;
			return k1;
		}
		
		//调整右右失衡
		private AVLNode<AnyType> doubleWithRightChild(AVLNode<AnyType> k3) {
			k3.right = rotateWithLeftChild(k3.right);
			return rotateWithRightChild(k3);
	
		}
	
		/**
		 * 遍历输出树
		 */
		public void printTree() {
			if (isEmpty()) {
				System.out.println("Empty tree");
			} else {
				printTree(root);
			}
		}
	
		/**
		 * 先序遍历输出某节点下元素
		 * 
		 * @param node
		 *            节点
		 */
		private void printTree(AVLNode<AnyType> node) {
			if (node != null) {
				printTree(node.left);
				System.out.print(node.element + " ");
				printTree(node.right);
			}
		}
	
		public static void main(String[] args) {
			AVLTree<Integer> tree = new AVLTree<Integer>();
			tree.insert(10);
			tree.insert(5);
			tree.insert(9);
			tree.insert(4);
			tree.insert(8);
			tree.insert(3);
			tree.insert(7);
			tree.insert(2);
			tree.insert(6);
			tree.insert(1);
			tree.printTree();
		}
	}