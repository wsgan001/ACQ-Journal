package hku.algo.index;

import hku.algo.DataReader;
import hku.algo.DataReaderPCS;
import hku.algo.KCore;
import hku.algo.TNode;
import hku.algo.index.unionFind.UNode;
import hku.algo.index.unionFind.UnionFind;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author fangyixiang
 * @date Sep 17, 2015
 * build the index using union-find data structure: correct, union later
 */
public class AdvancedIndex {
	public String nodes[][] = null;
	public int graph[][] = null;
	private int core[] = null;
	private int n = -1;
	private int coreReverseFang[] = null;
	private UNode unodeArr[] = null;
	private TNode invert[] = null;
	private Set<TNode> restNodeSet = null;
	private UnionFind uf = null;

	
	public AdvancedIndex(int graph[][], String nodes[][]){
		this.graph = graph;
		this.nodes = nodes;
	}
	
	public AdvancedIndex(String graphFile, String nodeFile){
		DataReaderPCS dataReader = new DataReaderPCS(graphFile, nodeFile);
		graph = dataReader.readGraph();
		nodes = dataReader.readNodes();
	}
	
	public TNode build(){
		//step 1: compute k-core
		//**********************  Dec 6, 2016 CYK:   here n-1 should be the actual number of the nodes 
		this.n = graph.length;//1 + actual node number 
		KCore kcore = new KCore(graph);
		core = kcore.decompose();
		int maxCore = kcore.obtainMaxCore();
		
		coreReverseFang = kcore.obtainReverseCoreArr();
		System.out.println("k-core decomposition finished (maxCore= " + maxCore +  ").");
		
		//step 2: initialize the union-find data structure
		restNodeSet = new HashSet<TNode>();//the remaining nodes without parents
		uf = new UnionFind();
		unodeArr = new UNode[n];
		for(int i = 1;i < n;i ++){ 
			UNode unode = new UNode(i);
			uf.makeSet(unode);
			unodeArr[i] = unode;
		}//**********************  Dec 6, 2016 CYK:  here n happens to be the vertices code 
		
		//step 3: build the tree in a bottom-up manner
		int startIdx = 1;
		invert = new TNode[n];//the invert index for graph nodes to their TNodes
		Set<Integer> core0Set = new HashSet<Integer>();//nodes with degree of 0
		for(int idx = 1;idx < n;idx ++){//idx is the index of array:coreReverseFang
			int id = coreReverseFang[idx];//current node,  an actual node ID
			int curCoreNum = core[id];
			
			if(curCoreNum > 0){
				int nextIdx = idx + 1;
				if(nextIdx < n){
					int nextId = coreReverseFang[nextIdx];
					
					if(core[nextId] < curCoreNum){//**********************  Dec 6, 2016 CYK:  traverse to find the location of the last vertex which core is curCoreNum,and then
												  //**********************  Dec 6, 2016 CYK:  -group them in a Tnode.
						handleALevel(startIdx, idx, curCoreNum); //generate nodes of tree index using nodes in [startIdx, idx]
						
						for(int reIdx = startIdx;reIdx <= idx;reIdx ++){
							int reId = coreReverseFang[reIdx];//current node,  an actual node ID
							UNode x = unodeArr[reId];
							for(int nghId:graph[reId]){//consider all the neighbors of id
								if(core[nghId] >= curCoreNum){
									UNode y = unodeArr[nghId];
									uf.union(x, y);
								
								}
							}
							UNode xParent = uf.find(x);
							int xRepresent = uf.find(x).represent;
							if(core[xRepresent] > core[reId])   xParent.represent = reId;//update x.parent's represent attribute
						}
						
						startIdx = nextIdx;//update the startIdx
					}
				}
				else if(nextIdx == n){//**********************  Dec 6, 2016 CYK:  the last one is root,and should be handled separately in step 4.
					handleALevel(startIdx, idx, curCoreNum); //generate nodes of tree index using nodes in [startIdx, idx]
				}
			}else{
				core0Set.add(id);
			}
		}
		
		//step 4: build the root node
		TNode root = new TNode(0);
		root.setNodeSet(core0Set);
		root.setChildList(new ArrayList<TNode>(restNodeSet));
//**********************  Jan 6, 2017 CYK: set root node as father of rootnode's childnodes
		for(TNode child:restNodeSet){
			child.setFather(root);
		}
//		System.out.println("after building the root:" + root.getChildList().size());
		
		//step 5: attach keywords
		AttachKw attacher = new AttachKw(nodes);
		root = attacher.attach(root);
	
//**********************  Dec 23, 2016 CYK: update rootTnode to invert
		//step 6:update root to invert array
		Iterator<Integer> iterator =root.getNodeSet().iterator();
		while(iterator.hasNext()) invert[iterator.next()]=root;
			
		return root;
	}
	
	
	//**********************  Dec 6, 2016 CYK: generate TNode with same coreNum and create its childSet.
	//old version: generate TNodes in the same level
	private void handleALevel(int startIdx, int endIdx, int curCoreNum){
		//step 1: build another temporary union-find data structure
		//**********************  Dec 9, 2016 CYK: in my opinion, here should be expressed as use union-find structure(just one tool) to find the CC
		Map<Integer, UNode> idUFMap = new HashMap<Integer, UNode>();//id -> union-find node
		for(int idx = startIdx;idx <= endIdx;idx ++){
			int id = coreReverseFang[idx];//a node's actual ID
			if(!idUFMap.containsKey(id)){
				UNode unode = new UNode(id);
				uf.makeSet(unode);
				idUFMap.put(id, unode);
			}
			for(int nghId:graph[id]){		 //**********************  Dec 6, 2016 CYK: you should find the CC which has at least k core. 
				if(core[nghId] >= core[id]){ //**********************  Dec 6, 2016 CYK: This action aims to get a representatives set 
					//**********************  Dec 6, 2016 CYK:  if core large than id, then find out the parent node to be the representatives. 
					if(core[nghId] > core[id])   {
						//System.out.println("id is "+id+"     nhgid: "+nghId+"!!!!");
						nghId = uf.find(unodeArr[nghId]).value;//**********************  Dec 7, 2016 CYK: you should understand the meaning of Union-find, just find out the parent of all the vertices,and put them in a virtual set 
						//System.out.println(nghId+"####");
					}//replaced by parent  
					if(!idUFMap.containsKey(nghId)){
						UNode unode = new UNode(nghId);
						uf.makeSet(unode);
						idUFMap.put(nghId, unode);
					}
					uf.union(idUFMap.get(id), idUFMap.get(nghId)); //*****************Dec 6, 2016 CYK: any ngdNode whose core equals to curCore should be union in. 
					//**********************  Dec 6, 2016 CYK: And for those having higher coreNum, find their parent and union them together.
				}
			}
		
		}
		
		//step 2: group nodes and find child nodes
		Map<UNode, Set<Integer>> ufGNodeMap = new HashMap<UNode, Set<Integer>>();//<parent, nodeSet>
		Map<UNode, Set<TNode>> ufTNodeMap = new HashMap<UNode, Set<TNode>>();//<parent, childNode>
		for(int reId:idUFMap.keySet()){//consider all the nodes, including out nodes
			UNode newParent = uf.find(idUFMap.get(reId));//in the new union-find
			//System.out.println(newParent.value);
			//**********************  Dec 6, 2016 CYK: group nodes with same coreNum which in another word, having the same parent. 
			//group nodes
			// System.out.println("core"+curCoreNum+"  id:  "+reId+"***((***"+newParent.value);
			if(core[reId] == curCoreNum){
				if(ufGNodeMap.containsKey(newParent)){
					ufGNodeMap.get(newParent).add(reId);
				}else{
					Set<Integer> set = new HashSet<Integer>();
					set.add(reId);
					ufGNodeMap.put(newParent, set);
				}
			}
			
			//find childList
			if(core[reId] > curCoreNum){
//**********************  Dec 9, 2016 CYK: differences between New parent and old parent:new one for same cores nodes and old one for child nodes! 
				
				//**********************  Dec 9, 2016 CYK: outer union-find operation will do the work using unodearr[].
				UNode oldParent = unodeArr[reId];//in the original union-find, reId is already an id of a parent node
				TNode tnode = invert[oldParent.represent];//**********************  Dec 6, 2016 CYK: to get the Anchor of the root and return its child tnode.
				//System.out.println(oldParent.represent);
				if(ufTNodeMap.containsKey(newParent)){
					ufTNodeMap.get(newParent).add(tnode);
				}else{
					Set<TNode> set = new HashSet<TNode>();
					set.add(tnode);
					ufTNodeMap.put(newParent, set);
				}
			}
		}
		
		//step 3: generate TNodes and build the connections
		for(Map.Entry<UNode, Set<Integer>> entry:ufGNodeMap.entrySet()){
			UNode parent = entry.getKey();
			Set<Integer> nodeSet = entry.getValue();
			Set<TNode> childSet = ufTNodeMap.get(parent);
			
			TNode tnode = new TNode(curCoreNum);
			tnode.setNodeSet(nodeSet);
			//**********************  Jan 6, 2017 CYK: set father node attribute 
			if(childSet != null) {
				tnode.setChildList(new ArrayList<TNode>(childSet));
				for(TNode child:childSet){
					child.setFather(tnode);
				}
				
			}
			
			restNodeSet.add(tnode);//record it as it has no parent
			for(int nodeId:tnode.getNodeSet())   invert[nodeId] = tnode;//update invert
			for(TNode subTNode:tnode.getChildList())   restNodeSet.remove(subTNode);//move some nodes
		}
	}
	
	public int[] getCore(){
		return core;
	}
	
	public TNode[] getInvert() {
		return invert;
	}

	//traverse the tree
	public void traverse(TNode root){
		Iterator<Integer> iter = root.getNodeSet().iterator();
		System.out.println("father is "+root.getFather().getNodeSet().toString());
		System.out.print("k=" + root.getCore() + " size=" + root.getNodeSet().size() + " nodes:");
		while(iter.hasNext())  {
			System.out.print(iter.next() + " ");
			
		}
		System.out.println();
		
	//**********************  Dec 23, 2016 CYK: test output keyword
		Iterator<String> iterator =root.getKwMap().keySet().iterator();
		while(iterator.hasNext()){
			String s=iterator.next();
			int[] arr=root.getKwMap().get(s);
			System.out.print(s+": ");
			for(int i=0;i<arr.length;i++) System.out.print(arr[i]+" ");
			System.out.println();
		}
		System.out.println("**********");
		
		for(int i = 0;i < root.getChildList().size();i ++){
			TNode tnode = root.getChildList().get(i);
			traverse(tnode);
		}
	}
	
	//**********************  Dec 23, 2016 CYK: offline keyword-update function
	public void addKw(int node, String keyword){
		TNode tNode=invert[node];
		Map<String, int[]> kwMap=null;
		int[] nodearr=null;
		kwMap=tNode.getKwMap();
		if(kwMap!=null){
			if(kwMap.containsKey(keyword)){
				Boolean vertexHaskwrd=false;
				nodearr=kwMap.get(keyword);
				for(int i=0;i<nodearr.length;i++){
					if(nodearr[i]==node) {
						System.out.println("node and new keyword has already exists.");
						 vertexHaskwrd=true;
						break;
					}
				}
				if(!vertexHaskwrd){
						int[] newarr=new int[nodearr.length+1];
						for(int i=0;i<nodearr.length;i++) newarr[i]=nodearr[i];
						newarr[newarr.length-1]=node;
						kwMap.put(keyword, newarr);
				}		
			}else{
				
				int[] newarr={node};
				kwMap.put(keyword, newarr);
			}
		}else	System.out.println("the node containing this vertex have no keyword!");
		
	}
	
	
	//**********************  Dec 23, 2016 CYK: offline delete a certain keyword of the vertex
	public void deleteKw(int node, String keyword){
		TNode tNode=invert[node];
		Map<String, int[]> kwMap=null;
		int[] nodearr=null;
		kwMap=tNode.getKwMap();
		if(kwMap!=null){
			if(kwMap.containsKey(keyword)){
				Boolean vertexHaskwrd=false;
				nodearr=kwMap.get(keyword);
				int index=-1; // record the location of vertex which will be delete keyword
				for(int i=0;i<nodearr.length;i++){
					if(nodearr[i]==node) {
						 vertexHaskwrd=true;
						 index=i;
						break;
					}
				}
				if(vertexHaskwrd){
					if(nodearr.length==1){
						kwMap.remove(keyword);
					} 
					else{
						int[] newarr=new int[nodearr.length-1];
						for(int i=0;i<index;i++) 			   	 newarr[i]=nodearr[i];
						for(int i=index+1;i<nodearr.length;i++)  newarr[i-1]=nodearr[i];
						kwMap.put(keyword, newarr);
						}
				}
				else	System.out.println("vertex "+node+" does not has this keyword ");
			}else	System.out.println("vertex "+node+" does not has this keyword ");
		}else	System.out.println("the node containing this vertex have no keyword!");
	}
	
	
	public TNode[] travseToInvert(TNode root){
		TNode[] invertArr=new TNode[this.graph.length];
		recursion(root, invertArr);
		return invertArr;
	}
	
	public void recursion(TNode root,TNode[] invertArr){
	
		Iterator<Integer> iter =root.getNodeSet().iterator();
		while(iter.hasNext()){
			int x=iter.next();
			System.out.println(x+"    "+root.getNodeSet().toString());
			invertArr[x]=root;
		}
		
		for(int i = 0;i < root.getChildList().size();i ++){
			TNode tnode = root.getChildList().get(i);
			recursion(tnode,invertArr);
		}
	}
	
	
}