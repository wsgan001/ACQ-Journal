package algorithm.kwIndex;

import java.util.*;
import java.util.Map.Entry;
import algorithm.KCoreDecom;
import algorithm.kwIndex.AUF.*;

/**
@author chenyankai
@Date	Aug 10, 2017
the class for building kTree for every KWNode
*/

public class KTree {
	//start from index 1
	private List<Integer> vertexList = null;
	private List<List<Integer>> subGraph = null;
	
	private int n = -1; 
	private int[] core = null; 
	private int[] reverseCore = null;
	private UNode[] unodeArr = null;
	private UnionFind uf = null;
	private Set<KNode> restNodeSet = null;
	private Map<Integer, KNode> vertexMap = null;

	private KNode root=null;
	
	
	boolean debug = false;
	
	public KTree(List<List<Integer>> subGraph){
		this.vertexList = subGraph.get(0);
		this.subGraph = subGraph;
		this.n = subGraph.size();
		this.vertexMap = new HashMap<Integer,KNode>();
		this.core = new int[subGraph.size()];
		this.uf = new UnionFind();
	}
		
	public void build(){
		//step 1:compute the core number
		//k-core decompose the subgraph
		KCoreDecom dCore = new KCoreDecom(subGraph);
		core= dCore.decompose();
		reverseCore = dCore.getReverseCore();
		//------------------------DEBUG------------------------------
		if(debug) {
			System.out.println("vertexList: "+vertexList.toString());
		
			for(int i=1;i<core.length;i++) System.out.println("core["+i+"]: "+core[i]+"  ");
			System.out.println();
			for(int i = 1;i < reverseCore.length;i ++)   System.out.println("cor[" + i + "]=" + reverseCore[i] + " ");
			System.out.println();
			System.out.println("k-core decomposition finished (maxCore= " + dCore.obtainMaxCore()+  ").");
		}
		//----------------------END DEBUG----------------------------
		
		//step 2:initialize the union-find data structure
		restNodeSet = new HashSet<KNode>();
		uf = new UnionFind();
		unodeArr = new UNode[n];
		for(int i=1; i<n;i++){
//			int trueId = vertexList.get(i);
			UNode node = new UNode(i);
			uf.makeSet(node);
			unodeArr[i] = node;
		}
		
		
		//step 3:build the tree in a bottom-up manner
		int startIdx = 1;
		Set<Integer> core0Set = new HashSet<Integer>();
		for(int idx = 1;idx < n; idx ++){
			int id = reverseCore[idx];
			int curCoreNum = core[id];
			
			if(curCoreNum > 0){
				int nextIdx = idx + 1;
				if(nextIdx < n){
					int nextId = reverseCore[nextIdx];
					
					if(core[nextId] < curCoreNum){
						handle(startIdx, idx, curCoreNum);
						
						//?
						for(int reIdx = startIdx; reIdx <= idx; reIdx++){
							int reId = reverseCore[reIdx];
							UNode xNode = unodeArr[reId];
							for(int nghId:subGraph.get(reId)){
								if(core[nghId]>= curCoreNum){
									UNode yNode = unodeArr[nghId];
									uf.union(xNode, yNode);
								}
							}
							UNode xFather = uf.find(xNode);
							int xAnchor = xFather.anchor;
							if(core[xAnchor] > core[reId]) xFather.anchor = reId; 	
						}
						startIdx = nextIdx;
					}
				}
				else if(nextIdx == n){
					handle(startIdx, idx, curCoreNum);
				}
				
			}else{
				//vertex.get(x) is the real vertex id
//				System.out.println(id);
				core0Set.add(vertexList.get(id));
			}
			
		}
		
		//step 4:build the root node and updated the vertexMap
		root = new KNode(0);
		root.setVertices(core0Set);
		root.setChildList(new ArrayList<KNode>(restNodeSet));
		for(int x:core0Set) vertexMap.put(x, root);
		return;
	} 
	
	
	private void handle(int startIdx, int endIdx, int curCoreNum){
		//step 1:build 
		Map<Integer, UNode> idUFMap = new HashMap<Integer,UNode>();
		for(int idx = startIdx; idx <= endIdx; idx ++){
			int id = reverseCore[idx];
			if(!idUFMap.containsKey(id)){
				UNode unode=new UNode(id);
				uf.makeSet(unode);
				idUFMap.put(id, unode);
			}
			for(int nghId: subGraph.get(id)){
				if(core[nghId] >= core[id]){
					if(core[nghId] > core[id]){
//						System.out.println("visiting here: "+nghId+" ID: "+id);
						nghId = uf.find(unodeArr[nghId]).value;
//						System.out.println("after find: "+nghId+" ID: "+id);
					}
					if(!idUFMap.containsKey(nghId)){
						UNode uNode = new UNode(nghId);
						uf.makeSet(uNode);
						idUFMap.put(nghId, uNode);
					}
					uf.union(idUFMap.get(id), idUFMap.get(nghId));
				}	
			}
		}
		
		//step 2: group nodes and find child nodes
		//key:father value:vertexSet
		Map<UNode, Set<Integer>> ufNodeMap = new HashMap<UNode,Set<Integer>>();
		//key:father value:childNode
		Map<UNode, Set<KNode>> ufKNodeMap = new HashMap<UNode, Set<KNode>>();
		
		Iterator<Entry<Integer, UNode>> entryIter = idUFMap.entrySet().iterator();
		while(entryIter.hasNext()){
			Entry<Integer, UNode> entry = entryIter.next();
			int id = entry.getKey();
			UNode idNode = entry.getValue();
			UNode newFather = uf.find(idNode);
			
			if(core[id] == curCoreNum){
				if(ufNodeMap.containsKey(newFather)){
					ufNodeMap.get(newFather).add(vertexList.get(id));
				}else{
					Set<Integer> set = new HashSet<Integer>();
					set.add(vertexList.get(id));
					ufNodeMap.put(newFather, set);
				}	
			}
			
			//childList
			else if(core[id] > curCoreNum){
				UNode oldFather = unodeArr[id];
				KNode kNode = vertexMap.get(vertexList.get(oldFather.anchor));
				if(ufKNodeMap.containsKey(newFather)){
					ufKNodeMap.get(newFather).add(kNode);
				}else{
					Set<KNode> set = new HashSet<KNode>();
					set.add(kNode);
					ufKNodeMap.put(newFather, set);
				}
			}
		}
		
		
		//step 3:
		for(Map.Entry<UNode, Set<Integer>> entry:ufNodeMap.entrySet()){
			UNode parent = entry.getKey();
			Set<Integer> nodeSet = entry.getValue();
			Set<KNode> childSet = ufKNodeMap.get(parent);
			
			KNode kNode = new KNode(curCoreNum);
			kNode.setVertices(nodeSet);
			//**********************  Jan 6, 2017 CYK: set father node attribute 
			if(childSet != null) {
				List<KNode> children = new ArrayList<KNode>();
				children.addAll(childSet);
				kNode.setChildList(children);
//				//set father
//				for(KNode child:childSet){
//					child.setFather(kNode);
//				}
			}
			
			restNodeSet.add(kNode);//record it as it has no parent
			for(int nodeId:kNode.getVertices())   vertexMap.put(nodeId, kNode);//update invert
			for(KNode subTNode:kNode.getChildList())   restNodeSet.remove(subTNode);//move some nodes
		}	
	}

	
	public Map<Integer, KNode> getVertexMap(){
		return this.vertexMap;
	}
	
	//return a ck-core containing queryId 
	public Set<Integer> getKQueryId(int k,int queryId){
		Set<Integer> set=new HashSet<Integer>();
		KNode node = vertexMap.get(queryId);
		if(node==null||node.k< k) return set;
		
		node.traverse(node,set);
		return set;
	}
	
	
	
	
	public KNode getRoot(){
		return this.root;
	}
	
	
	
}
