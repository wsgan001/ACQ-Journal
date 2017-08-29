package algorithm;
/**
@author chenyankai
@Date	Jul 25, 2017
to find a connected k-core in a subgraph
*/
import java.util.*;

import config.Config;

public class FindCKSubG {
	private int[][] graph=null;
	private Set<Integer> curSet=null;
	private int queryId;
	
	public FindCKSubG(int[][]graph,Set<Integer> curList,int queryId){
		this.graph=graph;
		this.curSet=curList;
		this.queryId=queryId;
	}
	
	public Set<Integer> findCKSG(){
		Set<Integer> CKSG=null;
		if(curSet.size()<=Config.k) return CKSG;
		
		//step 1: build a subgraph with curList
		Map<Integer, Integer> oldToNewMap=new HashMap<Integer,Integer>();
		Map<Integer, Integer> newToOldMap=new HashMap<Integer,Integer>();
		
		Iterator<Integer> iterator=curSet.iterator();
		int index=1; //subgraph start from node number 1
		while(iterator.hasNext()){
			int old=iterator.next();
			oldToNewMap.put(old, index);
			newToOldMap.put(index++,old);
		}
		
		
		int subgraph[][]=new int[curSet.size()+1][];
		iterator=curSet.iterator();
		index=1;
		while(iterator.hasNext()){
			int old=iterator.next();
			int arr[]=graph[old];
			
			int nghCount=0;
			boolean[] check = new boolean[arr.length];//check its original neighbour
			for(int j=0;j<graph[old].length;j++){
				int oldNeighbour=graph[old][j];
				
				if(curSet.contains(oldNeighbour)){
					check[j]=true;
					nghCount++;
				}
			}
			
			int newNeighborArr[]=new int[nghCount];
			int idx=0;
			for(int j=0;j<check.length;j++){
				if(check[j]){
					int newNeighbor=oldToNewMap.get(graph[old][j]);
					newNeighborArr[idx++]=newNeighbor;
				}
			}
			subgraph[index++]=newNeighborArr;
		}
		
		//step 2: find a connected k-core
		FindKCore fkCore=new FindKCore(subgraph, Config.k);
		int[] subCore=fkCore.decompose();
	
		FindCKCore finder=new FindCKCore();
		Set<Integer> rsSet=finder.findCKC(subgraph, subCore, oldToNewMap.get(queryId));
		
		if(rsSet !=null){
			CKSG=new HashSet<Integer>();
			for(int x:rsSet) {
				CKSG.add(newToOldMap.get(x));
			}
		}
		return CKSG;
	}
	
	
	

	
	
	
	
}
