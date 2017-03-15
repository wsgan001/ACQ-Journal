package hku.util;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.BasicIndex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * @author fangyixiang
 * @date Aug 18, 2015
 */
public class IndexT {

	public void test(){
		DataReader dataReader = new DataReader(Config.caseGraph, Config.caseNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		BasicIndex index = new BasicIndex(graph, nodes);
		TNode root = index.build();
		int core[] = index.getCore();
		
		int k = 1;
		Set<Integer> allNodeSet = new HashSet<Integer>();
		for(int i = 1;i < core.length;i ++){
			if(core[i] >= k)   allNodeSet.add(i);
		}
		
//		findCC(graph, core, allNodeSet, k);
		
		checkEmpty(root);
	}
	
	private void findCC(int graph[][], int core[], Set<Integer> allNodeSet, int k){
		List<Set<Integer>> rsList = new ArrayList<Set<Integer>>();
		
		//labling visited nodes
		int arrNum = graph.length; //arrNum = nodeNum + 1; 
		boolean visit[] = new boolean[arrNum];
		
		while(allNodeSet.size() > 0){
			Set<Integer> ccSet = new HashSet<Integer>(); //this is a connected component
			Queue<Integer> queue = new LinkedList<Integer>(); 
			
			Iterator<Integer> iter = allNodeSet.iterator();
			int seedID = iter.next();
			
			//initialize
			queue.add(seedID);
			allNodeSet.remove(seedID);
			ccSet.add(seedID);
			visit[seedID] = true;
			
			//search
			while(queue.size() > 0){
				int current = queue.poll();
				for(int i = 0;i < graph[current].length;i ++){
					int neighbor = graph[current][i];
					if(visit[neighbor] == false && core[neighbor] >= k && allNodeSet.contains(neighbor)){
						queue.add(neighbor);
						allNodeSet.remove(neighbor);
						ccSet.add(neighbor);
						visit[neighbor] = true;
					}
				}
			}
			
			rsList.add(ccSet);
		}
		
		System.out.println("# of connected components:" + rsList.size());
		
		int nonEmptyCount = 0;
		for(Set<Integer> set:rsList){
			boolean isContain2 = false;
			for(Integer nodeId:set){
				if(core[nodeId] == Config.k){
					isContain2 = true;
					break;
				}
			}
			
			if(isContain2 == false){
				System.out.println("nonEmptyCount:" + nonEmptyCount);
				nonEmptyCount += 1;
			}
		}
	}
	
	int eptyCount = 0;
	int nonEptyCount = 0;
	private void checkEmpty(TNode root){
		if(root.getNodeSet().size() == 0){
			System.out.println("Oh, fuck! " + eptyCount);
			eptyCount += 1;
		}else{
			nonEptyCount += 1;
		}
		
		for(TNode child:root.getChildList()){
			checkEmpty(child);
		}
	}
	
	public void print(){System.out.println("eptyCount:" + eptyCount + " nonEptyCount:" + nonEptyCount);}
	
	public static void main(String[] args) {
		IndexT index = new IndexT();
		index.test();
		index.print();
	}

}
