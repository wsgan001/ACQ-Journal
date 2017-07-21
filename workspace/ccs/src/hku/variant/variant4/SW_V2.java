package hku.variant.variant4;

import java.util.*;
import hku.Config;
import hku.algo.DataReader;
import hku.algo.FindCC;
import hku.algo.FindCCS;
import hku.algo.KCore;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.online.BasicW;
import hku.algo.query2.FPGrowthLocalFilter;

public class SW_V2 {
	private String nodes[][]=null;
	private int graph[][]=null;
	private TNode root;
	private int core[]=null;
	private int[] querySet=null;
	private Set<String> seedKwSet =null;
	
	//output set
	Set<Integer> ACNode=null;
	Map<Set<Integer>, Set<String>> outMap = null;
	
	public SW_V2(String[][] nodes,int graph[][],TNode root){
		this.nodes=nodes;
		this.graph=graph;
		this.root=root;
		KCore kcore=new KCore(graph);
		core=kcore.decompose();
	}

	public void testOutput(){
		if(outMap.isEmpty()){
			System.out.println("No such AC!");
		}else{
			if(ACNode.size()>50) System.out.println("AC size is: "+ACNode.size());
			else System.out.println(outMap.toString());
		}
	}
	
	public void query(int[] vertices ,String[] kws){
		ACNode=new HashSet<Integer>();
		outMap =new HashMap<Set<Integer>, Set<String>>();
		for(int i:vertices) {	if(core[i]<Config.k) return;}

		this.querySet=vertices;
		Map<Set<Integer>, Set<String>> ccsMap=null;
		int q0=querySet[0];
		
		//step1: mine neighbor information 
		List<Integer> nghList=new ArrayList<Integer>();
		for(int nghId:graph[q0]) if(core[nghId]>=Config.k) nghList.add(nghId);
		FPGrowthLocalFilter localFilter=new FPGrowthLocalFilter(graph, nodes,core, q0, nghList);
		//do keyword intersection 
		Set<String> kwSet=kwIntersection(kws);
		List<List<String[]>> canKwList=localFilter.mine(kwSet);		
		this.seedKwSet=localFilter.getSeedKwSet();
		if(this.seedKwSet.size()==0) return ;
		
		//step 2:location the node in the cck-core tree
		TNode tNode=locateAllCK(root,q0);
		
		//step 3:build the lengh->nodelist
		Map<Integer, List<String>> idShareWordMap=new HashMap<Integer, List<String>>();//id-wordlist
		fillLenMap(tNode, idShareWordMap);
		List<List<Integer>> allLenList=new ArrayList<List<Integer>>();//len:idList
		for(int i=0; i<=seedKwSet.size();i++){
			List<Integer> list=new ArrayList<Integer>();
			allLenList.add(list);
		}
		for(Map.Entry<Integer, List<String>> entry:idShareWordMap.entrySet()){
			int len= entry.getValue().size();
			allLenList.get(len).add(entry.getKey());
		}
		
		//step 4:initialize canNodeSet, which contains all the nodes sharing at least xx keywords
		Map<String, List<Integer>> invMap=new HashMap<String, List<Integer>>();
		for (int len = canKwList.size(); len < allLenList.size(); len++) {
			List<Integer> nodeList = allLenList.get(len);
			for(int id:nodeList){
				List<String> shareWordList = idShareWordMap.get(id);
				for(String word:shareWordList){
					if(invMap.containsKey(word)){
						invMap.get(word).add(id);
					}else{
						List<Integer> list = new ArrayList<Integer>();
						list.add(id);
						invMap.put(word, list);
					}
				}
			}
		}
		
		//step 5: search starting from long keyword combinations
		for (int len = canKwList.size() -1; len >= 1; len--){
			if(allLenList.size()>len){			
				List<Integer> nodeList = allLenList.get(len);
				for(int id:nodeList){
					List<String> shareWordList = idShareWordMap.get(id);
					for(String word:shareWordList){
						if(invMap.containsKey(word)){
							invMap.get(word).add(id);
						}else{
							List<Integer> list = new ArrayList<Integer>();
							list.add(id);
							invMap.put(word, list);
						}
					}
				}
				List<String[]> kwList = canKwList.get(len);
				ccsMap = findCCS(invMap, kwList,q0);
				if(ccsMap.size() > 0)	break;
			}
		}
		boolean isInSameCCK=true;
		
		//step 6: check others whether in this ACNode set if not clear and return
		for(Set<Integer> singleCCK:ccsMap.keySet()){
			boolean isSingleIn=false;
			for(int qi=1;qi<querySet.length;qi++){
				if(!singleCCK.contains(querySet[qi]))	isInSameCCK=false;
			}
			if(isInSameCCK==true) {
				ACNode.addAll(singleCCK);
				outMap.put(singleCCK, ccsMap.get(singleCCK));
//				break;
			}
		}
		
	}
	
	//do keyword intersection 
	private Set<String> kwIntersection(String kws[]){
		Set<String> kwSet=new HashSet<String>();
		for(String word:kws)   kwSet.add(word);
		for(int queryId:querySet){
			Set<String> tempSet= new HashSet<String>();
			for(int i = 1;i < nodes[queryId].length;i ++)   tempSet.add(nodes[queryId][i]);
			kwSet.retainAll(tempSet);
		}
		return kwSet;
	}
		
	
	private TNode locateAllCK(TNode rootNode,int queryId){
		//step 1: locate nodes with corenumber=config.k using BFS
		List<TNode> canList=new ArrayList<TNode>();
		Queue<TNode> queue=new LinkedList<TNode>();
		queue.add(rootNode);
	
		while(queue.size()>0){
			TNode curNode=queue.poll();
			for(TNode node:curNode.getChildList()){
				if(node.getCore()<Config.k) queue.add(node);
				else canList.add(node);// core number of this node must be at least k 
			}
		}
		
		//step2: locate a list of CKCore
		for(TNode node:canList){
			if(findCK(node,queryId)) return node;
		}
		return null;
	}
	
	private boolean findCK(TNode node,int queryId){
		if(node.getCore()<=core[queryId]){
			boolean rs=false;
			if(node.getNodeSet().contains(queryId)){
				rs=true;
			}else {
				for(TNode tnode:node.getChildList()){
					if(findCK(tnode, queryId)){
						rs=true;
//						break;
					}
				}
			}
			return rs;
		}
		else{
			return false;
		}
	}
	
	private void fillLenMap(TNode root, Map<Integer, List<String>> idshareWordMap ){
		//step 1:compute the number of shared keywords
		Map<String,int[]> kwMap=root.getKwMap();
		for(String kw:seedKwSet){//consider each candidates
			if(kwMap.containsKey(kw)){
				for(int id:kwMap.get(kw)){
					if(idshareWordMap.containsKey(id)){
						idshareWordMap.get(id).add(kw);
					}else {
						List<String> list=new ArrayList<String>();
						list.add(kw);
						idshareWordMap.put(id, list);
						
					}
				}
			}
		}
		//step2: traverse the sub-tree
		for(int i=0;i< root.getChildList().size();i++){
			TNode tNode=root.getChildList().get(i);
			fillLenMap(tNode, idshareWordMap);
		}
	}
	
	private Map<Set<Integer>,Set<String>> findCCS(Map<String, List<Integer>> invMap, List<String[]> kwList,int queryId) {
		Map<Set<Integer>,Set<String>> rsList = new HashMap<Set<Integer>,Set<String>>();
			
		// step 1: verify each keyword combination
		for (String kws[] : kwList) {
			if(invMap.get(kws[0])!=null){
				Set<Integer> nodeSet = new HashSet<Integer>(invMap.get(kws[0]));
				Set<String> tmpkwset=new HashSet<String>();
				//for(int i = 1;i < kws.length;i ++){// here should start from 0
				for(int i = 0;i < kws.length;i ++){
					Set<Integer> tmpSet = new HashSet<Integer>();
					if(invMap.get(kws[i])!=null){
						for(int id:invMap.get(kws[i])){
							if(nodeSet.contains(id)){
								tmpSet.add(id);
								tmpkwset.add(kws[i]);
							}
						}
					}
					nodeSet = tmpSet;
				}
		
				// count the number of nodes and edges
				FindCC findCC = new FindCC(graph, nodeSet, queryId);
				Set<Integer> ccNodeSet = findCC.findCC();
				int nodeNum = ccNodeSet.size();
				int edgeNum = findCC.getEdge();
			
				//find the ccs
				Set<Integer> ccsSet = new HashSet<Integer>();
				if(edgeNum - nodeNum >= (Config.k * Config.k - Config.k) / 2 - 1){
					List<Integer> curList = new ArrayList<Integer>();//this list serves as a map (newID -> original ID)
					curList.add(-1);//for consuming space purpose
					curList.addAll(ccNodeSet);
				
					FindCCS finder = new FindCCS(graph, curList, queryId);
					ccsSet = finder.findRobustCCS();
					if(ccsSet.size() > 1)	rsList.put(ccsSet,tmpkwset);
				}
			}
		}
		return rsList;
	}
	
}
