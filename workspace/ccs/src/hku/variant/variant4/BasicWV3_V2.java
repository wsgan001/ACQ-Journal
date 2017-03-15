package hku.variant.variant4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import hku.algo.AprioriPruner;
import hku.algo.FindCCS;
import hku.algo.KCore;
import hku.util.Tool;

public class BasicWV3_V2 {
	private String nodes[][]=null;
	private int graph[][]=null;
	private int core[]=null;
	private int querySet[]=null;
	private AprioriPruner apruner = null;
	//output set
	private Set<String> maxKWSet= null;
	Set<Integer> ACNode=null;
	
	public BasicWV3_V2(String[][] nodes,int graph[][]){
		this.nodes=nodes;
		this.graph=graph;
		KCore kcore=new KCore(graph);
		this.core=kcore.decompose();
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
		
	
	public void query(int[] vertices,String[] kws){
		this.querySet=vertices;
		ACNode=new HashSet<Integer>();
		maxKWSet=new HashSet<String>();
		//step1: do keyword filter and find ccs from Gq0
		int q0=querySet[0];
		long time=System.nanoTime();
		Set<String> kwSet=kwIntersection(kws);
		long time1=System.nanoTime();
		kwFilter(q0, kwSet);
		//step2: check qi from querySet is in this AC or not
		for(int qi=1;qi<querySet.length;qi++){
			if(!ACNode.contains(querySet[qi])){
				ACNode.clear();
				maxKWSet.clear();
				return;
			}
		}
	}
	
	private Set<String> kwIntersection(String kws[]){
		Set<String> kwSet=new HashSet<String>();
		kwSet.addAll(Arrays.asList(kws));
		for(int queryId:querySet){
			Set<String> tempSet= new HashSet<String>();
			for(int i=1;i<nodes[queryId].length;i++) tempSet.add(nodes[queryId][i]);
			kwSet.retainAll(tempSet);
		}
//		System.out.println(kwSet.toString());
		return kwSet;
	}
	
	private void kwFilter(int queryId,Set<String> kwSet){
		//step1:initialize the candidate list
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
				
				for(int i=1; i<graph.length;i++){
					int curId = i;
					
//					Set<String> set1=new HashSet<String>();
//					Set<String> set2=new HashSet<String>();
//					set1.addAll(Arrays.asList(kws));
//					set2.addAll(Arrays.asList(nodes[curId]));
//					if(set2.containsAll(set1)) curList.add(curId);
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
						if(isSingleContained == false){
							isContained = false;
							break;
						}
					}
					
					//if this node's keywords are contained, then we choose it
					if(isContained){
						curList.add(curId);
					}
				}
//				if(curList.size()<=1) continue;
				
				//step 1.2:find a CC from the subgraph
				FindCCS finder= new FindCCS(graph, curList, queryId);
				Set<Integer> ccsSet=finder.findRobustCCS();
				if(ccsSet.size()>1){
					maxKWSet.clear();
					validKwList.add(kws);
					ACNode=ccsSet;
					for(int i=0;i<kws.length;i++)	maxKWSet.add(kws[i]);
//					System.out.println("there is a cc " + ccsSet.size());
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
