package hku.algo.index.edgeUpdate;

import java.util.*;
import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.index.unionFind.UNode;
import hku.algo.index.unionFind.UnionFind;

public class EdgeDeletion {
	private TNode root=null;
	private TNode[] invert=null;
	private String[][]node=null;
	private int graph[][] = null;
	private int core[] = null;
	private TKDEalgoDelete tkdEalgo=null;
//	private Map<TNode, TNode> childFatherMap=null;
	
	public EdgeDeletion(TNode root,TNode[]invert, int[][]graph,String[][]node,int[]core){
		this.root=root;
		this.invert=invert;
		this.graph=graph;
		this.node=node;
		this.core=core;
//		this.childFatherMap=new HashMap<TNode, TNode>();
		this.tkdEalgo=new TKDEalgoDelete(this.graph, this.core);
	}
	
	public TNode[] rebuild(){
		AdvancedIndex ac=new AdvancedIndex(this.graph,this.node);
		ac.build();
		TNode[] invert=ac.getInvert();
		return invert;
	}
	
	public TNode duibi(){
		AdvancedIndex ac=new AdvancedIndex(this.graph,this.node);
		return ac.build();
	}
	
	
	
	
	public TNode deleteEdge(int nodeu,int nodev){
		TNode u=invert[nodeu];
		TNode v=invert[nodev];
		this.graph=tkdEalgo.getGraph();
		this.core=tkdEalgo.getCore();
		Set<Integer>set= tkdEalgo.deleteEdge(nodeu, nodev, u, v);
		if(u.getCore()>v.getCore()){	
			System.out.println(v.getNodeSet().size());
			System.out.println(1);
			TNode newV=split(v,nodev);
			System.out.println(2);
			upAlevel(set, newV);
			System.out.println(3);
			
			}
		else if(u.getCore()<v.getCore()){	
			
			TNode newU=split(u,nodeu);
			upAlevel(set, newU);
			
		}else if(u.getCore()==v.getCore()){
			
			List<TNode> list=sepecialSplit(u,nodeu, nodev);
			if(list.size()==1){
				upAlevel(set, list.get(0));
			}else if(list.size()==2){
				if(list.get(0)!=list.get(1)){
					upAlevel(set, list.get(0));
					upAlevel(set, list.get(1));
				}else {
					upAlevel(set, list.get(0));
				}
			}
			
		}
		return root;
	}
	
	
	
	//when core number of certain nodes has changed,they should go up a level 
	private TNode upAlevel(Set<Integer> nodeSet,TNode son){	
		if(nodeSet.size()!=0&&son.getCore()!=0){
			//step1: if son node is not leaf node ,makeSet all children node
			List<TNode> childrenList=son.getChildList();
			int neverVisited=childrenList.size();// if some tree nodes are never visited, then it means should go up a level as upper node's child
			Set<Integer> splitSet=son.getNodeSet();
			splitSet.removeAll(nodeSet);
			TNode newSon=son;
			newSon.setNodeSet(splitSet);
			for(int x:splitSet) invert[x]=newSon;
			newSon.setKwMap(son.getKwMap());
			deleteKeyword(newSon.getKwMap(), nodeSet);
			List<TNode> newTnodeList=reOrganize(newSon);
			
			
			//split and create new nodes
			List<TNode> newAddList=new ArrayList<TNode>();
			for(TNode x:newTnodeList){
				neverVisited=neverVisited-x.getChildList().size();
				newAddList.add(x);
			}
			
			// if son is just leaf node,which means childrenlist size is 0.And process above will not be processed 
			//if leafNode is empty remove son node;if leafNode is not empty leave them in the son node
			//if splitSetsize>0 then remove all the nodes which has been visited and find CC in those not been visited as the split leaf nodes
			if(neverVisited!=0){
				List<TNode> upList=childrenList;
				for(TNode x:newTnodeList){
					upList.removeAll(x.getChildList());
				}
					newAddList.addAll(upList);
			}
			
			//create or update father node and up down relationship
			if(son.getFather().getCore()<son.getCore()-1){//in this case,we should create a new node 
				TNode addNode=new TNode(son.getCore()-1);
				addNode.setNodeSet(nodeSet);
				addKeyword(addNode.getKwMap(), nodeSet);
				addNode.setFather(son.getFather());
				son.getFather().getChildList().add(addNode);
				son.getFather().getChildList().remove(son);
				addNode.setChildList(newAddList);
				for(TNode x:newAddList) x.setFather(addNode);
				son.setFather(addNode);
				for(int x:nodeSet) invert[x]=addNode;
				
			}else{
				TNode father =son.getFather();
				father.getNodeSet().addAll(nodeSet);
				addKeyword(father.getKwMap(), nodeSet);
				father.getChildList().remove(son);
				father.getChildList().addAll(newAddList);
				for(TNode x:newAddList) x.setFather(father);
				for(int x:nodeSet) invert[x]=father;
			}

		}
		return root;
	}


	
	private void mapFunction(TNode grandfather,Map<TNode, TNode> childFatherMap){
		List<TNode> childrenList=grandfather.getChildList();
		for(TNode x:childrenList){
			childFatherMap.put(x, x);
			for(TNode y:x.getChildList()) recursion(y, x,childFatherMap);
		}
	}
	private void recursion(TNode x,TNode father,Map<TNode, TNode> childFatherMap){
		childFatherMap.put(x, father);
		for(TNode y:x.getChildList()) recursion(y,father,childFatherMap);
	}
	
//	private void mapFunction(TNode grandfather){
//		List<TNode> childrenList=grandfather.getChildList();
//		for(TNode x:childrenList){
//			childFatherMap.put(x, x);
//			for(TNode y:x.getChildList()) recursion(y, x);
//		}
//	}
//	private void recursion(TNode x,TNode father){
//		childFatherMap.put(x, father);
//		for(TNode y:x.getChildList()) recursion(y,father);
//	}
	
	private List<TNode> reOrganize(TNode tNode){		
		List<TNode> childrenList=tNode.getChildList();
		Map<Integer, Set<TNode> > vertexTNodeMap=new HashMap<Integer,  Set<TNode> >();
		Map<TNode,Set<Integer>> TNodeSetMap=new HashMap<TNode, Set<Integer>>();
		UnionFind uf=new UnionFind();
		Map<Integer, UNode> unodeMap=new HashMap<Integer, UNode>();
		Iterator<Integer> it=tNode.getNodeSet().iterator();
		while(it.hasNext()){
			int x=it.next();
			UNode uNode=new UNode(x);
			uf.makeSet(uNode);
			unodeMap.put(x, uNode);
		}
		Map<TNode, TNode> childFatherMap=new HashMap<TNode, TNode>();
		mapFunction(tNode,childFatherMap);
		int count=0;
		Iterator<Integer> iterator=tNode.getNodeSet().iterator();
		while(iterator.hasNext()){
			int vertex=iterator.next();
			for(int x:graph[vertex]){
				count++;
				if(core[x]>tNode.getCore()){
					TNode xNode=invert[x];
					TNode node=childFatherMap.get(xNode);
					if(childrenList.contains(node)){
						if(vertexTNodeMap.containsKey(vertex)) 	vertexTNodeMap.get(vertex).add(node);
						else{	
							Set<TNode> tmpSet=new HashSet<TNode>();
							tmpSet.add(node);
							vertexTNodeMap.put(vertex, tmpSet);
						}
				
						if(TNodeSetMap.containsKey(node)) TNodeSetMap.get(node).add(vertex);	
						else{		
							Set<Integer> tmpSet=new HashSet<Integer>();
							tmpSet.add(vertex);
							TNodeSetMap.put(node, tmpSet);
						}
					}
				}
				//while neighbor in the same set,union them
				else if(tNode.getNodeSet().contains(x)){
					uf.union(unodeMap.get(vertex), unodeMap.get(x));
				}
			}
		}		
		System.out.println("Unodemap size "+unodeMap.size());
		//union those who are not neighbors but share common children tree Nodes
		Iterator<TNode> it3=TNodeSetMap.keySet().iterator();
		while(it3.hasNext()){
			TNode tmpNode=it3.next();
			if(TNodeSetMap.get(tmpNode).size()>1){// at least 2 vertex
				Set<Integer> set=TNodeSetMap.get(tmpNode);
				 Iterator<Integer> iterator4=set.iterator();
				 UNode firstNode=unodeMap.get(iterator4.next());
				 while(iterator4.hasNext()){
					 uf.union(firstNode, unodeMap.get(iterator4.next()));
				 }
			}
		}
		
		//get the disjoint set with the same root in Union find structure 
		Map<UNode, Set<Integer>> UNodeSetMap=new HashMap<UNode, Set<Integer>>();
		for(Iterator<Integer> tmpIt=tNode.getNodeSet().iterator();tmpIt.hasNext();){
			int x=tmpIt.next();
			UNode rootUnode=uf.find( unodeMap.get(x) );
		
			if(UNodeSetMap.containsKey(rootUnode)) 	UNodeSetMap.get(rootUnode).add(x);
			else{
				Set<Integer> tmpSet=new HashSet<Integer>();
				tmpSet.add(x);
				UNodeSetMap.put(rootUnode, tmpSet);
			}
		}
		System.out.println("unodeset map size "+UNodeSetMap.size());
		
		Map<Set<Integer>, List<TNode>> outputMap=new HashMap<Set<Integer>, List<TNode>>();
		Set<Integer> biggestSet=new HashSet<Integer>();//to record biggest set and treat it as basic keyword map
		
		for(Iterator<UNode> itUnode=UNodeSetMap.keySet().iterator(); itUnode.hasNext();){
			Set<Integer> set=UNodeSetMap.get(itUnode.next());
			if(set.size()>biggestSet.size()) biggestSet=set;
			List<TNode> list=new ArrayList<TNode>();
			for(Iterator<Integer> setIt=set.iterator();	setIt.hasNext();	){
				int x=setIt.next();
				if(vertexTNodeMap.containsKey(x)) {
					for(TNode y:vertexTNodeMap.get(x)) {
						if( !list.contains(y) )  list.add(y);
					}
				}
			}
			
			outputMap.put(set, list);
		}
		
		//output 
		System.out.println(tNode.getNodeSet().size()+"   ~!~!!~");
		int qq=0;
		for(TNode x:tNode.getChildList()){qq=qq+x.getChildList().size();}
		System.out.println("qq size"+qq);
		
		List<TNode> outTNodeList=new ArrayList<TNode>();
		int bgstIndex=-1;
		Iterator<Set<Integer>> iterator1=outputMap.keySet().iterator();
		while(iterator1.hasNext()){
			Set<Integer> set=iterator1.next();
			TNode node=new TNode(tNode.getCore());
			
			node.setNodeSet(set);
			node.setChildList(outputMap.get(set));
			for(TNode x:node.getChildList()) x.setFather(node);
			for(int x:set) invert[x]=node;
			node.setFather(tNode.getFather());
			if(set==biggestSet) {
				node.setKwMap(tNode.getKwMap());
				bgstIndex=outTNodeList.size();
				//split keyword 
			}else addKeyword(node.getKwMap(), set);
			
			outTNodeList.add(node);
		}
		
		
		
			if(outTNodeList.size()>1){
				for(int i=0;i<outTNodeList.size();i++){
					if(i!=bgstIndex) {
//						System.out.println(i+"   "+bgstIndex);
						deleteKeyword(outTNodeList.get(bgstIndex).getKwMap(), outTNodeList.get(i).getNodeSet());
					}
				}
			}
			
		return outTNodeList;
	}
	
	
	public TNode split(TNode start,int u){
		TNode cursor=start;
		int newSize=-1;
		int originalSize=-1;
		int count=-1;
		TNode returnroot=null;
		
		do{	
			if(cursor.getCore()!=0){
				originalSize=cursor.getChildList().size();
				System.out.println("core number is "+cursor.getCore());
				List<TNode> newTnodeList=reOrganize(cursor);
				System.out.println(newTnodeList.size()+"size");
				if(newTnodeList.size()!=-1){
					for(TNode x:newTnodeList) System.out.println("here "+x.getNodeSet().size()+" "+x.getCore()+ " "+x.getChildList().size());
				}
				int num=newTnodeList.size();				
				if(num==2){//num==2 cursor node needs to be split into two nodes
					TNode node1=newTnodeList.get(0);
					TNode node2=newTnodeList.get(1);
					
					if(node1.getNodeSet().contains(u) && count==-1) returnroot=node1;//record return root in the first loop
					if(node2.getNodeSet().contains(u) && count==-1) returnroot=node2;
					
					TNode father=cursor.getFather();
					father.getChildList().remove(cursor);
					father.getChildList().add(node1);
					father.getChildList().add(node2);
					count++;
					cursor=father;
			
				}else if(num==1){	//num==1 cursor node needs to be linked 
					List<TNode> childList=newTnodeList.get(0).getChildList();
					newSize=childList.size();
					TNode father= cursor.getFather();

					if(originalSize>newSize){//need to update father's children nodes as for recursion 
						List<TNode> newChildList=new ArrayList<TNode>();
						newChildList.addAll(cursor.getChildList());
						newChildList.removeAll(childList);
						for(TNode x:newChildList) x.setFather(father);
						father.getChildList().addAll(newChildList);	
					}
					if(count==-1) returnroot=cursor;
					count++;
					cursor=father;
				}else originalSize=newSize;
			}
		}while(originalSize!=newSize&&cursor.getCore()!=0);
		return returnroot;
	}
	
	// ucore = vcore 
	public List<TNode> sepecialSplit(TNode start,int u,int v){
		TNode cursor=start;
		int newSize=-1;
		int originalSize=-1;
		int count=-1;
		List<TNode> returnroot=new ArrayList<TNode>();
		do{	
			if(cursor.getCore()!=0){
				originalSize=cursor.getChildList().size();
				List<TNode> newTnodeList=reOrganize(cursor);
				int num=newTnodeList.size();
				if(num==2){//num==2 cursor node needs to be split into two nodes
					TNode node1=newTnodeList.get(0);
					TNode node2=newTnodeList.get(1);
//					for(int x:node1.getNodeSet()) invert[x]=node1;
//					for(int x:node2.getNodeSet()) invert[x]=node2;
					if(count==-1){//record return root in the first loop
						if(node1.getNodeSet().contains(u)||node1.getNodeSet().contains(v)) returnroot.add(node1);
						if(node2.getNodeSet().contains(u)||node2.getNodeSet().contains(v)) returnroot.add(node2);
					}
					TNode father=cursor.getFather();
					father.getChildList().remove(cursor);
					father.getChildList().add(node1);
					father.getChildList().add(node2);
					count++;
					cursor=father;
			
				}else if(num==1){//num==1 cursor node needs to be linked 
					List<TNode> childList=newTnodeList.get(0).getChildList();
					newSize=childList.size();
					TNode father= cursor.getFather();

				
				if(originalSize>newSize){//need to update father's children nodes as for recursion 
					List<TNode> newChildList=new ArrayList<TNode>();
					newChildList.addAll(cursor.getChildList());
					newChildList.removeAll(childList);
					for(TNode x:newChildList) x.setFather(father);
					father.getChildList().addAll(newChildList);	
				}
				if(count==-1) returnroot.add(cursor);
				count++;
				cursor=father;
			}
		}
		}while(originalSize!=newSize&&cursor.getCore()!=0);
	
		return returnroot;
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
	
	public TNode[]  travseT(TNode root){
		recursion(root);
		return this.invert;
	}
	
	public void recursion(TNode root){
		Iterator<Integer> iter =root.getNodeSet().iterator();
		while(iter.hasNext()){
			int x=iter.next();
			invert[x]=root;
		}
		for(int i = 0;i < root.getChildList().size();i ++){
			TNode tnode = root.getChildList().get(i);
			recursion(tnode,invert);
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
	
	public boolean compare(TNode[] invert1, TNode[] invert2){
		for(int i=1;i<invert1.length;i++) {
//			System.out.println(i);
			if(invert1[i].getNodeSet().size()!=invert2[i].getNodeSet().size() || invert1[i].getChildList().size()!=invert2[i].getChildList().size()){
//				System.out.println("compare");
//				System.out.println(invert1[i].getNodeSet().size());
//				System.out.println(invert2[i].getNodeSet().size());
//				System.out.println(invert1[i].getChildList().size());
//				System.out.println(invert2[i].getChildList().size());
//				System.out.println(invert1[i].getCore()+"  "+invert2[i].getCore());
				return false;
				}
		}
		return true;
	}


}
