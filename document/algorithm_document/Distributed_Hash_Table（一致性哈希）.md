#Distriuted Hash Table（一致性哈希）(DHT)
MIT(麻省理工)提出的一种算法，目前主要应用于分布式缓存当中

一致性哈希可以有效解决分布式存储结构下动态增加和删除节点所带来的问题

例如：一亿条数据创建单库30个分表，后来数据大量增加，原30个分表不够用，直接增加分表会打乱原来的Hash规则，做全量数据迁移可以解决但是代价太大

##一致性哈希过程
1.首先，把全量的缓存空间当作一个环形存储结构，环形空间共2^32个缓存区（Redis中则是把Key]分配到16384个slot中）

2.每一个缓存Key都可以通过Hash算法转化为一个32位的二进制数，对应这环形空间某一个缓存区，把所有的缓存Key映射到环形空间的不同位置
	
		Key1————>key2————>key3————>key4————>key1

3.每一个缓存节点遵循相同的Hash算法，比如利用IP做Hash,映射到环形空间中

		Key1————>key2————>key3————>key4————>key1
		|		  			|		|		 |
		node1		 	 node2    node3	   node1

4.增加节点，当缓存集群的节点有所增加的时候，整个环形空间的映射保持原规则，一小部分key的归属会收到影响，如在key2与key3之间增加node4,调整key2指向node4,把key2的缓存数据从node2迁移到node4。
	
		Key1————>key2————>key3————>key4————>key1
		|		  |			|		|		 |
		node1	node4	  node2    node3    node1

5.删除节点，删除node3，原本node3的缓存数据交给顺时针后续节点node1,受影响的只有key4，将key4的缓存数据从node3迁移到node1，迁移是指查询时去找顺时针的后续节点
	
		Key1————>key2————>key3————>key4————>key1
		|		  |			|				 |
		node1	node4	  node2        		node1

6.为了避免节点太少而产生的不均匀情况，引入虚拟节点的概念，基于原来的物理节点映射出N个子节点，最后把所有的子节点映射到环形空间上

注：分布式缓存节点变化频繁，传统的关系型数据库的分库分表相对稳定

##一致性哈希实现（模拟）
	
	public class Demo {
		@Test
		public void test(){
			Key key = this.initData();
			Key src_Key = this.initData();
			key = this.addNode(key, src_Key);
		}
		
		private Key addNode(Key key, Key src_Key){
			Key tempKey = key.getNextKey();
			key.setNextKey(src_Key);
			src_Key.setNextKey(tempKey);
			return key;
		}
		
		public Key initData(){
			Key key1 = this.initKey();
			Key key2 = this.initKey();
			Key key3 = new Key();
			Key key4 = this.initKey();
			Key key5 = this.initKey();
			
			key1.setNextKey(key2);
			key2.setNextKey(key3);
			key3.setNextKey(key4);
			key4.setNextKey(key5);
			key5.setNextKey(key1);
			return key1;
		}
		
		public Key initKey(){
			Key key = new Key();
			Node node = new Node();
			List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
			Map<String, String> map1 = new HashMap<String, String>();
			Map<String, String> map2 = new HashMap<String, String>();
			map1.put("姓名", "老王");
			map1.put("年龄：", "10");
			map2.put("姓名", "老李");
			map2.put("年龄：", "11");
			lists.add(map1);
			lists.add(map2);
			node.setData(lists);
			key.setNode(node);
			return key;
		}
	}

节点类
	
	public class Node {
		private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	
		public List<Map<String, String>> getData() {
			return data;
		}
	
		public void setData(List<Map<String, String>> data) {
			this.data = data;
		}
	
		@Override
		public String toString() {
			return "Node [data=" + data + "]";
		}
	}
键类
	
	public class Key {
		private Key nextKey;
		private Node node;
	
		public Key getNextKey() {
			return nextKey;
		}
	
		public void setNextKey(Key nextKey) {
			this.nextKey = nextKey;
		}
	
		public Node getNode() {
			return node;
		}
	
		public void setNode(Node node) {
			this.node = node;
		}
	
		@Override
		public String toString() {
			return "Key [node=" + node + "]";
		}
	}
	