#Union-find（并查集）
首先在地图上有若干城镇，这些城镇都可以看做点，然后哪些与哪些之间是有道路直接相连，最后要解决整幅图的连通性问题

##并查集原理
使用并查集，首先会存在一组不相交的动态集合，每个集合可能包含一个或多个元素，只关心对于给定的元素，可以很快找到这个元素所在的集合

实现原理为使用树来表示集合，每个节点就表示集合中的一个元素，树根就是该集合的代表

比如随意给你几个点，让你判断他们之间是否连通，或者整幅图一共有几个连通分支，也就是几个互相独立的块，像畅通路径这类问题，问还需要修几条路，实质就是求有几个连通分支，如果只有一个连通分支，说明图上的点都被连起来了，如果是2个，则需要在修一条路
	
	4——————	2
			|
			1		3
	
共2个连通分支，4,2,1被连接成一部分，3一部分，需要在修一条路，连接3与4,2,1任意一个

采用数结构实现并查集的基本思想是，每个集合用一棵树表示。树的结点用于存储集合中的元素名。每个树结点还存放一个指
向其父结点的指针。数根结点处的元素代表该数所表示的集合。利用映射可以找到集合中所对应的数结点。

#####应用场景
1.网络连接诊断：如果每个pair中的两个整数分别代表一个网络节点，那么该pair就是用来表示这两个节点是需要连通的，那么为所有的pair建立了动态连通图后，就能够尽可能减少布线的需要，因为已经连通的两个节点会被直接忽略掉

2.变量名等同性（指针）
在程序中，可以声明多个引用来指向同一对象，这个时候就可以通过为程序中声明的引用和实际对象建立动态连通图来判断哪些引用实际上是指向同一对象

##路径压缩原理
	
				曹公公										曹公公
	三营长				九营长			
	六组长				十八组长			
	白面葫芦娃			仙子狗尾巴花			三营长 九营长 六组长 十八组长 白面葫芦娃 仙子狗尾巴花

##并查集实现(压缩路径)

	public class Find {

		@Test
		public void test(){
			Point point_1 = new Point(1);
			Point point_2 = new Point(2);
			Point point_3 = new Point(3);
			Point point_4 = new Point(4);
			
			this.put(point_1, point_2);
			this.put(point_2, point_3);
			this.put(point_2, point_4);
		}
		
		public Point put(Point point_1, Point point_2){
			point_2.setLastPoint(point_1);
			point_1.getNextNodes().add(point_2);
			Point tempPoint = point_1;
			while(tempPoint.lastPoint != null){
				tempPoint = tempPoint.lastPoint;
			}
			this.shrinkHight(tempPoint);
			return tempPoint;
		}
		
		//合并两个集合
		public Point union(Point point_1, Point point_2){
			Point tempPoint_2 = point_2;
			while(tempPoint_2.lastPoint != null){
				tempPoint_2 = tempPoint_2.lastPoint;
			}
			Point tempPoint_1 = point_1;
			while(tempPoint_1.lastPoint != null){
				tempPoint_1 = tempPoint_1.lastPoint;
			}
			tempPoint_2.setLastPoint(tempPoint_1);
			return tempPoint_1;
		}
		
		//路径压缩
		public Point shrinkHight(Point point){
			boolean flag = false;
			List<Integer> lists = new ArrayList<Integer>();
			for(int i = 0; i < point.nextNodes.size(); i ++){
				if(point.nextNodes.get(i).nextNodes.size() != 0){
					flag = true;
				}
			}
			if(!flag){
				return point;
			}else{
				for(int i = 0; i < point.nextNodes.size(); i ++){
					if(point.nextNodes.get(i).nextNodes.size() != 0){
						for(int j = 0; j < point.nextNodes.get(i).nextNodes.size(); j ++){
							point.nextNodes.add(point.nextNodes.get(i).nextNodes.get(i));
							this.shrinkHight(point.nextNodes.get(i).nextNodes.get(i));
							point.nextNodes.get(i).nextNodes.remove(i);
						}
					}
				}
				return null;
			}
		}
	}
	

	