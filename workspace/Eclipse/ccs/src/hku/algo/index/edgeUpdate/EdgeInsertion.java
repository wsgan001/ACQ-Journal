package hku.algo.index.edgeUpdate;

import java.util.*;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;


public class EdgeInsertion {
	private TNode root=null;
	private TNode[] invert=null;
	private String[][]node=null;
	private int graph[][] = null;
	private int core[] = null;
	private TKDEalgoInsert tkdEalgo=null;
	
	public void  getCore(){
		for(int i=1;i<core.length;i++){
			System.out.println("node: "+i+"core number is "+core[i]);
		}
	}
	
	
	public EdgeInsertion(TNode root,TNode[] invert,int[][] graph,String[][]node,int[] core){	
		this.root=root;
		this.invert=invert;
		this.graph=graph;
		this.node=node;
		this.core=core;
		this.tkdEalgo=new TKDEalgoInsert(graph, core);
	}
	
	
	
	public TNode[] rebuild(){
		AdvancedIndex ac=new AdvancedIndex(this.graph,this.node);
		ac.build();
		TNode[] invert=ac.getInvert();
		return invert;
	}
	
	
	//to check whether in one tree(has lineal kin relationship)
	public boolean isLinealKin(TNode son,TNode father){
		while(son!=root){
			if(son.getFather()==father){
				return true;
			}else{
				son=son.getFather();
			}
		}
		 return false;
	}

	//default: edge does not exist 
	public TNode insertEdge(int nodeU,int nodeV){
		//step1: invoke tkde algorithm 
		TNode u=invert[nodeU];
		TNode v=invert[nodeV];
		this.graph=tkdEalgo.getGragh();
		this.core=tkdEalgo.getCore();
		Set<Integer> set=new HashSet<Integer>();
			//do  downALevel;
		if(u.getCore()>v.getCore()){
			
			if(!isLinealKin(u, v)){
				set=tkdEalgo.updateCore(v, nodeU, nodeV,nodeV);
				TNode newNode=downALevel(set,v);
				ArrayList<LinkedList<TNode>> list= trace(u, newNode);
				 merge(list.get(0), list.get(1));
			}
			else{
				set=tkdEalgo.updateCore(v, nodeU, nodeV,nodeV);
				downALevel(set, v);
			}
		}
		else if(u.getCore()<v.getCore()){
			if(!isLinealKin(v, u)){
				set=tkdEalgo.updateCore( u, nodeU, nodeV,nodeU);
				TNode newNode=downALevel(set, u);
				ArrayList<LinkedList<TNode>> list= trace(v, newNode);
				merge(list.get(0), list.get(1));
			}
			else{
				set=tkdEalgo.updateCore(u,nodeU,nodeV,nodeU);
				downALevel(set, u);
			}
		}
		else {
			if(u==v){
				set=tkdEalgo.updateCore( u, nodeU, nodeV,nodeU);
				downALevel(set, u);
			}
			else {
				set=tkdEalgo.updateCore( u, nodeU, nodeV,nodeU);
				TNode newNodeU=downALevel(set, u);
				TNode newNodeV=downALevel(set, v);
				ArrayList<LinkedList<TNode>> list= trace(newNodeV, newNodeU);
				merge(list.get(0), list.get(1));
			}
		}
	return root;
}


	public TNode downALevel(Set<Integer> nodeSet, TNode father){
		List<TNode> sonsChildren=new ArrayList<TNode>();
		List<TNode> fatherChidren=new ArrayList<TNode>();// new children list of father
		Set<Integer> tmpNodeSet=new HashSet<Integer>();
		TNode biggest=new TNode(father.getCore()+1);// to mark the node corenumber is k+1,and the size of its nodeSet is biggest(to speed up)
		TNode returnNode=null;// to return 
		ArrayList<Map<String, int[]>> kwMapList=new ArrayList<Map<String,int[]>>();
		
		if(nodeSet.size()!=0){			
			Set<TNode> changedNodeSet=new HashSet<TNode>();//those who needs to merge or change relationship =k+1 or >k+1
			for(int vertex:nodeSet){
				for(int neighbor:graph[vertex]){
					if(core[neighbor]>father.getCore() && !nodeSet.contains(neighbor)){
						TNode node=invert[neighbor];
				// here to notice that: after insert an edge,there could be a neighbor whose core number is larger than father
				// but are not the subnode of the father
				// because new edge(u,v)  this.graph has been update, and new edge with larger core number should not be considered here
						while(node.getFather()!=father&&node!=node.getFather()  ){ 
							node=node.getFather();
						}
						if(node!=node.getFather()){
							changedNodeSet.add(node);
							kwMapList.add(node.getKwMap());
							if(node.getNodeSet().size()>biggest.getNodeSet().size()&&node.getCore()==biggest.getCore()) biggest=node;
						}
					}	
				}
			}
		
			tmpNodeSet=biggest.getNodeSet();
			Map<String, int[]> baseKwMap=biggest.getKwMap();
			sonsChildren=biggest.getChildList();
			for(TNode x:changedNodeSet){
				if(x.getCore()==father.getCore()+1){// for those =k+1 merge them 
					if(x!=biggest){
					tmpNodeSet.addAll(x.getNodeSet());
					sonsChildren.addAll(x.getChildList());
					}
				}else{ 								// for those >k+1, relink them as children of new node 
					sonsChildren.add(x);
				}
			}
			
			tmpNodeSet.addAll(nodeSet);
			
			// create new node
			TNode newSon=new TNode(father.getCore()+1);
			returnNode=newSon;// for return
			newSon.setNodeSet(tmpNodeSet);
			newSon.setKwMap( mergeKw(kwMapList, baseKwMap) );
			addKeyword(newSon.getKwMap(), nodeSet);
			
			for(int x:newSon.getNodeSet()) invert[x]=newSon;
			
			
			newSon.setChildList(sonsChildren);
			for(TNode x:sonsChildren) x.setFather(newSon);// set children's father 
				
			//update father node
			father.getNodeSet().removeAll(nodeSet);
			deleteKeyword(father.getKwMap(), nodeSet);
			fatherChidren=father.getChildList();
			for(TNode x:changedNodeSet){fatherChidren.remove(x);}
			fatherChidren.add(newSon);
			
			//if father is empty, then clear this node and link children to father's father
			if(father.getNodeSet().size()==0 && father.getCore()!=0){
				List<TNode> tmplist= father.getFather().getChildList();
				for(int i=0;i<tmplist.size();i++) {
					if(tmplist.get(i)==father) {
						tmplist.remove(father);
						break;
					}
				}
				fatherChidren.addAll(tmplist);
				father.getFather().setChildList(fatherChidren);
				for(TNode x:fatherChidren) x.setFather(father.getFather());
			}
			else {
				newSon.setFather(father);
				father.setChildList(fatherChidren);
				
			}
		}
		else{
			returnNode=father;
		}
		return returnNode;
	
	}

	//trace up to find the same ancestor and record the trace in two stack 
	//list[0]: original path  ****     list[1]:to be merged path  
	public ArrayList<LinkedList<TNode>> trace(TNode originTNode, TNode mergedTNode){
		TNode oTNode=originTNode;
		TNode mTNode=mergedTNode;

		ArrayList<LinkedList<TNode>> list=new ArrayList<LinkedList<TNode>>();
		LinkedList<TNode> oList=new LinkedList<TNode>();
		LinkedList<TNode> mList=new LinkedList<TNode>();
		while(oTNode!=mTNode){
			if(oTNode.getCore()<mTNode.getCore()) {

				mList.addFirst(mTNode);
				mTNode=mTNode.getFather();
			}
			else if(oTNode.getCore()>mTNode.getCore()) {
				oList.addFirst(oTNode);
				oTNode=oTNode.getFather();
			}
			else if(oTNode.getCore()==mTNode.getCore()){
				mList.addFirst(mTNode);
				oList.addFirst(oTNode);
				mTNode=mTNode.getFather();
				oTNode=oTNode.getFather();
			}
		}
			if(oTNode==mTNode) {
				mList.addFirst(mTNode);
				oList.addFirst(oTNode);
			}
			list.add(oList);
			list.add(mList);
			return list;
	}
	
	
	//insert new tree node for merging
	public TNode merge(LinkedList<TNode> oList, LinkedList<TNode> mList){
		TNode root=oList.pop();//use root as cursor to build the merged tree here 
		mList.pop();
		TNode returnroot=root; 
		List<TNode>	tmp=root.getChildList();
		tmp.remove(oList.peek());
		tmp.remove(mList.peek());
		root.setChildList(tmp);

		//start to merge TNode 
		TNode oNode=null;
		TNode mNode=null;
		while(!oList.isEmpty()&&!mList.isEmpty()){
			Set<Integer> nodeSet=new HashSet<Integer>();
			List<TNode> sonChildrenList=new ArrayList<TNode>();
			List<TNode> fatherchildrenList=new ArrayList<TNode>();
			if(oList.peek().getCore()==mList.peek().getCore()){
				mNode=mList.pop();
				oNode=oList.pop();
				
				//create new node
				TNode newNode=new TNode(oNode.getCore());
				//**********************  Jan 13, 2017 CYK:  merge smaller set into bigger size one will be much better (speed up)
				ArrayList<Map<String, int []>> kwMapList=new ArrayList<Map<String,int[]>>();//store a kwMapList for mergeKw function 
				Map<String, int[]> baseMap=new HashMap<String, int[]>();
				if(oNode.getNodeSet().size()>=mNode.getNodeSet().size()){
					kwMapList.add(mNode.getKwMap());
					baseMap=oNode.getKwMap();
					nodeSet=oNode.getNodeSet();
					nodeSet.addAll(mNode.getNodeSet());
				}else{
					kwMapList.add(oNode.getKwMap());
					baseMap=mNode.getKwMap();
					nodeSet=mNode.getNodeSet();
					nodeSet.addAll(oNode.getNodeSet());
				}
				newNode.setNodeSet(nodeSet);
				newNode.setKwMap(mergeKw(kwMapList, baseMap));
				newNode.setFather(root);
				if(oNode.getChildList().size()>=mNode.getChildList().size()){
					sonChildrenList=oNode.getChildList();
					sonChildrenList.addAll(mNode.getChildList());
				}else{
					sonChildrenList=mNode.getChildList();
					sonChildrenList.addAll(oNode.getChildList());
				}
				if(!oList.isEmpty()) sonChildrenList.remove(oList.getFirst());
				if(!mList.isEmpty()) sonChildrenList.remove(mList.getFirst());
				newNode.setChildList(sonChildrenList);
				for(TNode x:sonChildrenList) x.setFather(newNode);
				
				//update father node
				fatherchildrenList=root.getChildList();
				fatherchildrenList.remove(oNode);
				fatherchildrenList.remove(mNode);
				fatherchildrenList.add(newNode);
				root.setChildList(fatherchildrenList);
				
				//link new node as root
				for (int x:newNode.getNodeSet()) invert[x]=newNode;
				root=newNode;
			}
			else if(oList.getFirst().getCore()>mList.getFirst().getCore()){
				mNode=mList.pop();
				//create new node
				TNode newNode=new TNode(mNode.getCore());
				nodeSet=mNode.getNodeSet();
				newNode.setNodeSet(nodeSet);
				newNode.setKwMap(mNode.getKwMap());
				newNode.setFather(root);
				sonChildrenList=mNode.getChildList();
				for(TNode x:sonChildrenList) x.setFather(newNode);
				newNode.setChildList(sonChildrenList);
				
				for (int x:newNode.getNodeSet()) invert[x]=newNode;
				
				//update father node 
				fatherchildrenList=root.getChildList();
				fatherchildrenList.remove(mNode);
				fatherchildrenList.add(newNode);
				root.setChildList(fatherchildrenList);
				
				//link new node as root
				root=newNode;
			}
			else if(oList.peek().getCore()<mList.peek().getCore()){
				oNode=oList.pop();
				
				//create new node
				TNode newNode=new TNode(oNode.getCore());
				nodeSet=oNode.getNodeSet();
				newNode.setNodeSet(nodeSet);
				newNode.setKwMap(oNode.getKwMap());
				newNode.setFather(root);
				sonChildrenList=oNode.getChildList();
				newNode.setChildList(sonChildrenList);
				for(TNode x:sonChildrenList) x.setFather(newNode);
				
				for (int x:newNode.getNodeSet()) invert[x]=newNode;
				
				//update father node
				fatherchildrenList=root.getChildList();
				fatherchildrenList.remove(oNode);
				fatherchildrenList.add(newNode);
				root.setChildList(fatherchildrenList);
		
				//link new node as root
				root=newNode;
			}
		}
		
		if(oList.isEmpty()	&& !mList.isEmpty()){
			//link mNode to the tree
			mNode=mList.pop();
			TNode newNode=new TNode(mNode.getCore());
			newNode.setNodeSet(mNode.getNodeSet());
			newNode.setKwMap(mNode.getKwMap());
			newNode.setChildList(oNode.getChildList());
			newNode.setFather(root);

			for (int x:newNode.getNodeSet()) invert[x]=newNode;
			
			
			//update father's children list
			List<TNode>	fatherchildren=root.getChildList();
			fatherchildren.remove(mNode);
			fatherchildren.add(newNode);
			root.setChildList(fatherchildren);
			
			root=newNode;
		}
		else if(mList.isEmpty() && !oList.isEmpty()){
			//link oNode to the tree
			oNode=oList.pop();
			TNode newNode=new TNode(oNode.getCore());
			newNode.setNodeSet(oNode.getNodeSet());
			newNode.setKwMap(oNode.getKwMap());
			newNode.setChildList(oNode.getChildList());
			newNode.setFather(root);	
			
			for (int x:newNode.getNodeSet()) invert[x]=newNode;

			//update father's children list
			List<TNode> fatherchildren=root.getChildList();
			fatherchildren.remove(oNode);
			fatherchildren.add(newNode);
			root.setChildList(fatherchildren);
			root=newNode;
		}
		return returnroot;
	}
	
	
	private Map<String, int[]> mergeKw(ArrayList<Map<String, int[]>> kwMapList, Map<String, int[]> baseKwMap){
		Map<String, Set<Integer>> spMap=new HashMap<String, Set<Integer>>();//support map:store same keyword and use set to store the node temporarily 
		for(Map<String, int[]> tmpMap:kwMapList){
			if(tmpMap!=baseKwMap){
				Iterator<String> iterator=tmpMap.keySet().iterator();
				while(iterator.hasNext()){
					String keyword=iterator.next();
					if(baseKwMap.containsKey(keyword)){
						if(spMap.containsKey(keyword)){
							int[] arr2=tmpMap.get(keyword);
							for(int x:arr2) spMap.get(keyword).add(x);
						}else{
							Set<Integer> set=new HashSet<Integer>();
							int[] arr1=baseKwMap.get(keyword);
							int[] arr2=tmpMap.get(keyword);
							for(int x:arr1) set.add(x);
							for(int x:arr2) set.add(x);
							spMap.put(keyword, set);
						}
					}else{
						baseKwMap.put(keyword,tmpMap.get(keyword));
					}
				}
			}
		}
		
		//update base keyword map using spMap
		Iterator<String> it=spMap.keySet().iterator();
		while(it.hasNext()){
			String keyword=it.next();
			Set<Integer> tmpSet=spMap.get(keyword);
			int[] newArr=new int[tmpSet.size()];
			Iterator<Integer> node=tmpSet.iterator();
			int i=0;
			while(node.hasNext())	newArr[ i++ ]=node.next();
			baseKwMap.put(keyword, newArr);
		}
		return baseKwMap;
	}
	
	private void addKeyword(Map<String, int[]> kwMap, Set<Integer> nodeSet){
		Iterator<Integer> iterator=nodeSet.iterator();
		while(iterator.hasNext()){
			int vertex=iterator.next();
			for(int j=1;j<node[vertex].length;j++){
				String keyword=node[vertex][j];
				if(kwMap.containsKey(keyword)){
					int[] arr=kwMap.get(keyword);
					int[] newArr=new int[arr.length+1];
					for(int i=0;i<arr.length;i++) newArr[i]=arr[i];				
					newArr[newArr.length-1]=vertex;
					kwMap.put(keyword, newArr);
				}
				else{
					int[] arr={vertex};
					kwMap.put(keyword, arr);
				}
			}
		}
	}
	
	private void deleteKeyword( Map<String, int[]> kwMap, Set<Integer> nodeSet){
		Iterator<Integer> iterator=nodeSet.iterator();
		while(iterator.hasNext()){
			int vertex=iterator.next();
			for(int j=1;j<node[vertex].length;j++){
				String keyword=node[vertex][j];
				if(kwMap.containsKey(keyword)){
					int[] arr=kwMap.get(keyword);
					if(arr.length>=2){
						int[] newArr=new int[arr.length-1];
						int i=0;
						for(int x:arr){
							if(x!=vertex&&i!=newArr.length){
								newArr[i]=x;
								i++;
							}
						}
						kwMap.put(keyword, newArr);
					}else{
						kwMap.remove(keyword);
					}
				}
			}
		}
	}
	
	//rebuild the invert list using root node 
	public TNode[] travseToInvert(TNode root){
		TNode[] invertArr=new TNode[this.graph.length];
		recursion(root, invertArr);
		return invertArr;
	}
	
	public void recursion(TNode root,TNode[] invertArr){
		Iterator<Integer> iter =root.getNodeSet().iterator();
		while(iter.hasNext()){
			int x=iter.next();
			invertArr[x]=root;
		}
		for(int i = 0;i < root.getChildList().size();i ++){
			TNode tnode = root.getChildList().get(i);
			recursion(tnode,invertArr);
		}
	}

	//traverse function
	public void traverse(TNode root){
		Iterator<Integer> iter = root.getNodeSet().iterator();
		System.out.println("**********");
		System.out.print("k=" + root.getCore()  + " nodes:");
		while(iter.hasNext())  {
			System.out.print(iter.next() + " ");
		}
		System.out.println();
		System.out.println("father is "+root.getFather().getNodeSet().toString());
		System.out.println("childlist size: "+root.getChildList().size());
		Iterator<String> iterator =root.getKwMap().keySet().iterator();
		while(iterator.hasNext()){
			String s=iterator.next();
			int[] arr=root.getKwMap().get(s);
			System.out.print(s+": ");
			for(int i=0;i<arr.length;i++) System.out.print(arr[i]+" ");
			System.out.println();
		}
		
		for(int i = 0;i < root.getChildList().size();i ++){
			TNode tnode = root.getChildList().get(i);
			traverse(tnode);
		}
	}

}
