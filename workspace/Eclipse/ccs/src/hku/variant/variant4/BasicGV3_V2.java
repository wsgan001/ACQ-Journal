package hku.variant.variant4;

import java.util.*;



import hku.Config;
import hku.algo.AprioriPruner;
import hku.algo.DataReader;
import hku.algo.FindCC;
import hku.algo.FindCCS;
import hku.algo.FindCKCore;
import hku.algo.KCore;
import hku.prep.dblp.Finder;
import hku.util.Tool;

public class BasicGV3_V2 {
	private String nodes[][]=null;
	private int graph[][]=null;
	private int core[]=null;
	private int querySet[]=null;
	private AprioriPruner apruner = null;
	//output set
	private Set<String> maxKWSet= null;
	Set<Integer> ACNode=null;
	
public BasicGV3_V2(String[][] nodes,int graph[][]){
	this.nodes=nodes;
	this.graph=graph;
	KCore kcore=new KCore(graph);
	core=kcore.decompose();
}

public void testOutput(){
	if(!maxKWSet.isEmpty()&&!ACNode.isEmpty()){
		if(ACNode.size()>50){
			System.out.println("AC size is: "+ACNode.size());
		}else{
			System.out.println("AC nodes are: "+ACNode.toString());
		}
		System.out.println("shared keyword: "+maxKWSet.toString());
	}
	else{
		System.out.println("No such AC!");
	}
}


public void query(String[] kws,int[] vertices){
	this.querySet=vertices;
	ACNode=new HashSet<Integer>();
	maxKWSet=new HashSet<String>(); 
		//step1 : find the connected k-core containing q0
	int q0=querySet[0];
	if(core[q0]<Config.k) {
		ACNode.clear();
		maxKWSet.clear();
		return;  // Structure cohesiveness fail
	}
	
	Set<Integer> roughCCSet=new HashSet<Integer>();//to store the first step CCk
	FindCKCore findCKCore=new FindCKCore();
	int firstCKCoreNode[]=findCKCore.findCKCore(graph, core, q0);
	for(int i:firstCKCoreNode) {  roughCCSet.add(i);  }
	for(int i=1;i<querySet.length;i++){
		if(core[querySet[i]]<Config.k) {
			ACNode.clear();
			maxKWSet.clear();
			return;
			} 	// Structure cohesiveness fail
		if(!roughCCSet.contains(querySet[i])) {
			ACNode.clear();
			maxKWSet.clear();
			return; // Connectivity fail	
		}
	}
	//step2: do keyword filter and refind from Gq0
	//to calculate the interasection between KWS and Queryset 
	Set<String> kwSet=kwIntersection(kws);
	kwFilter(firstCKCoreNode, q0, kwSet);
	
	//step3: check qi from querySet is in this AC or not
	if(vertices.length>1){
		for(int qi=1;qi<querySet.length;qi++){
			if(!ACNode.contains(querySet[qi])){
				ACNode.clear();
				maxKWSet.clear();
				return;
			}
		}
	}else return;
}

private Set<String> kwIntersection(String kws[]){
	Set<String> kwSet=new HashSet<String>();
	kwSet.addAll(Arrays.asList(kws));
	for(int queryId:querySet){
		Set<String> tempSet= new HashSet<String>();
		for(int i=1;i<nodes[queryId].length;i++)  tempSet.add(nodes[queryId][i]);
		kwSet.retainAll(tempSet);
	}
	return kwSet;
}

private void kwFilter(int ckCoreNode[],int queryId,Set<String> kwSet){
	//step 1:initialize the candidate list
	List<String[]> canList=new ArrayList<String[]>();
	Iterator<String> iterator=kwSet.iterator();
	while(iterator.hasNext()){
		String[] kw={iterator.next()};
		canList.add(kw);
	}
	
	//generate keyword with length k
	for(int iteratorK =1;;iteratorK ++){
		//step 1: all the keyword combination
		List<String[]> validKwList=new ArrayList<String[]>();
		for(String kws[]:canList){
			//step 1.1 keyword filtering and copy subgraph
			List<Integer> curList=new ArrayList<Integer>();
			curList.add(-1);
			for(int i = 0;i < ckCoreNode.length; i ++){
				int curId = ckCoreNode[i];
				boolean isContained = true;
				for(int x = 0; x < kws.length;x ++){
					boolean isSingleContained = false;
					for(int y = 1;y < nodes[curId].length;y ++){
						if(nodes[curId][y].equals(kws[x])){
							isSingleContained = true;
							break;
						}
					}
					//if this single keyword is not contained, then skip
					if(isSingleContained == false){  isContained = false;  }
				}
				//if this node's keywords are contained, then we choose it
				if(isContained){  curList.add(curId);  }
			}
			if(curList.size()<=1) continue;
			
			//step 1.2:find a CC from the subgraph
			FindCCS finder= new FindCCS(graph, curList, queryId);
			Set<Integer> ccsSet=finder.findRobustCCS();
			if(ccsSet.size()>1){
				maxKWSet.clear();
				validKwList.add(kws);
				ACNode=ccsSet;
				for(int i=0;i<kws.length;i++)  maxKWSet.add(kws[i]);	
			}
		}
		if(validKwList.size()==0) break;
		
		//step 2:generate candidates
		Tool tool = new Tool();
		apruner = new AprioriPruner(validKwList);
		canList = new ArrayList<String[]>();
		for(int i = 0;i < validKwList.size() - 1;i ++){
			for(int j = i + 1;j < validKwList.size();j ++){
				if(iteratorK == 1){
					String kws1 = validKwList.get(i)[0];
					String kws2 = validKwList.get(j)[0];
					String newKws[] = {kws1, kws2};
					if(kws1.compareTo(kws2) > 0){
						newKws[0] = kws2;   newKws[1] = kws1;
					}
					if(!apruner.isPruned(newKws))   canList.add(newKws);
				}else{
					boolean isCand = true;
					String kws1[] = validKwList.get(i);
					String kws2[] = validKwList.get(j);
					for(int ij = 0;ij < iteratorK - 1;ij ++){
						if(kws1[ij].equals(kws2[ij]) == false){
							isCand = false;
							break;
						}
					}
					
					if(isCand){
						String newKws[] = new String[iteratorK + 1];
						for(int ij = 0;ij < iteratorK;ij ++)   newKws[ij] = kws1[ij];
						newKws[iteratorK] = kws2[iteratorK - 1];
						newKws = tool.sortKw(newKws); //sort the keywords
						if(!apruner.isPruned(newKws))   canList.add(newKws);
					}
				}
			}
		}
	}	
}	

}
