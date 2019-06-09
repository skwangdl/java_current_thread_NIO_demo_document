#Aho-Corasick automaton（AC自动机）
多模匹配算法，典型应用是用于统计和排序大量的字符串（但不仅限于字符串），所以经常被搜索引擎系统用于文本词频统计

典型应用：给出n个单词，再给出一段包含m个字符的文章，找出有多少个单词在文章里出现过

可以暴力进行处理，用每一个单词对字符串做KMP算法，时间复杂度很大

KMP算法：给出一个字符串，再给出一段包含m个字符的文章，找出这个字符串再文章内出现的次数

####AC自动机过程：

1.建立模式的Trie树

2.构造失败指针，给Trie树添加失败路径

3.KMP算法（字符串匹配）（根据AC自动机，搜索待处理的文本）

###1.建立多模式集合的Trie树
为需要匹配的字符串集合构建Trie树

###2.构造失败指针
假设一个节点上的字母为C，沿着这个节点的父节点的失败节点走，知道走到一个节点，他的子节点有字母为C，然后把需要构建失败指针的字母的失败指针，指向这个子节点。

如果一直找到root都没有找到，将失败指针指向root

如图 第三排字符为h的节点，父节点的失败节点指向root，root的子节点包含h字符，所以这个节点的失败节点指向这个子节点（第二排右侧）（5）

###3.匹配字符串
依次从文章中取字符到字典树内匹配，如果从root到叶子节点全部匹配到，说明匹配出的字符串再文章内出现过，如果中途出现不匹配的情况，走失败指针，继续匹配字典树

过程类似KMP算法，如果出现走失败指针的情况，说明，失败指针指向的父节点与之前的字符串，已经部分匹配了集合内某个需要匹配的字符串，提高匹配效率

##AC自动机实现

	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.LinkedList;
	import java.util.List;
	import java.util.Map.Entry;
	
	public class AC {
		/* 本示例中的AC自动机只处理英文类型的字符串，所以数组的长度是128 */
		private static final int ASCII = 128;
		/* AC自动机的根结点，根结点不存储任何字符信息 */
		private Node root;
		/* 待查找的目标字符串集合 */
		private List<String> target;
		/* 表示在文本字符串中查找的结果，key表示目标字符串， value表示目标字符串在文本串出现的位置 */
		private HashMap<String, List<Integer>> result;
	
		/* 内部静态类，用于表示AC自动机的每个结点，在每个结点中我们并没有存储该结点对应的字符 */
		private static class Node {
			/* 如果该结点是一个终点，即，从根结点到此结点表示了一个目标字符串，则str != null, 且str就表示该字符串 */
			String str;
			/* ASCII == 128, 所以这里相当于128叉树 */
			Node[] table = new Node[ASCII];
			/* 当前结点的孩子结点不能匹配文本串中的某个字符时，下一个应该查找的结点 */
			Node fail;
	
			public Boolean isWord() {
				return str != null;
			}
		}
	
		/* target表示待查找的目标字符串集合 */
		public AC(List<String> target) {
			root = new Node();
			this.target = target;
			buildTrieTree();
			build_AC_FromTrie();
		}
	
		/* 由目标字符串构建Trie树 */
		private void buildTrieTree() {
			for (String targetStr : target) {
				Node curr = root;
				for (int i = 0; i < targetStr.length(); i++) {
					char ch = targetStr.charAt(i);
					if (curr.table[ch] == null) {
						curr.table[ch] = new Node();
					}
					curr = curr.table[ch];
				}
				/* 将每个目标字符串的最后一个字符对应的结点变成终点 */
				curr.str = targetStr;
			}
		}
	
		/* 由Trie树构建AC自动机，本质是一个自动机，相当于构建KMP算法的next数组 */
		private void build_AC_FromTrie() {
			/* 广度优先遍历所使用的队列 */
			LinkedList<Node> queue = new LinkedList<Node>();
			/* 单独处理根结点的所有孩子结点 */
			for (Node x : root.table) {
				if (x != null) {
					/* 根结点的所有孩子结点的fail都指向根结点 */
					x.fail = root;
					queue.addLast(x);
					/* 所有根结点的孩子结点入列 */
				}
			}
			while (!queue.isEmpty()) {
				/* 确定出列结点的所有孩子结点的fail的指向 */
				Node p = queue.removeFirst();
				for (int i = 0; i < p.table.length; i++) {
					if (p.table[i] != null) {
						/* 孩子结点入列 */
						queue.addLast(p.table[i]);
						/* 从p.fail开始找起 */
						Node failTo = p.fail;
						while (true) {
							/* 说明找到了根结点还没有找到 */
							if (failTo == null) {
								p.table[i].fail = root;
								break;
							}
							/* 说明有公共前缀 */
							if (failTo.table[i] != null) {
								p.table[i].fail = failTo.table[i];
								break;
							} else {
								/* 继续向上寻找 */
								failTo = failTo.fail;
							}
						}
					}
				}
			}
		}
	
		/* 在文本串中查找所有的目标字符串 */
		public HashMap<String, List<Integer>> find(String text) {
			/* 创建一个表示存储结果的对象 */
			result = new HashMap<String, List<Integer>>();
			for (String s : target) {
				result.put(s, new LinkedList<Integer>());
			}
			Node curr = root;
			int i = 0;
			while (i < text.length()) {
				/* 文本串中的字符 */
				char ch = text.charAt(i);
				/* 文本串中的字符和AC自动机中的字符进行比较 */
				if (curr.table[ch] != null) {
					/* 若相等，自动机进入下一状态 */
					curr = curr.table[ch];
					if (curr.isWord()) {
						result.get(curr.str).add(i - curr.str.length() + 1);
					}
					/*
					 * 这里很容易被忽视，因为一个目标串的中间某部分字符串可能正好包含另一个目标字符串，
					 * 即使当前结点不表示一个目标字符串的终点，但到当前结点为止可能恰好包含了一个字符串
					 */
					if (curr.fail != null && curr.fail.isWord()) {
						result.get(curr.fail.str).add(
								i - curr.fail.str.length() + 1);
					}
					/* 索引自增，指向下一个文本串中的字符 */
					i++;
				} else {
					/* 若不等，找到下一个应该比较的状态 */
					curr = curr.fail;
					/*
					 * 到根结点还未找到，说明文本串中以ch作为结束的字符片段不是任何目标字符串的前缀， 状态机重置，比较下一个字符
					 */
					if (curr == null) {
						curr = root;
						i++;
					}
				}
			}
			return result;
		}
	
		public static void main(String[] args) {
			List<String> target = new ArrayList<String>();
			target.add("abcdef");
			target.add("abhab");
			target.add("bcd");
			target.add("cde");
			target.add("cdfkcdf");
			String text = "bcabcdebcedfabcdefababkabhabk";
			AC aca = new AC(target);
			HashMap<String, List<Integer>> result = aca.find(text);
			System.out.println(text);
			for (Entry<String, List<Integer>> entry : result.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
		}
	}

