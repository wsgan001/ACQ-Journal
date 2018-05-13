package hku.exp.revision;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.query2.DecShare;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author fangyixiang
 * @date May 24, 2016
 */
public class GPMStar {
	private int queryId = -1;
	private int graph[][] = null;
	private String nodes[][] = null;
	
	public GPMStar(int graph[][], String nodes[][]){
		this.graph = graph;
		this.nodes = nodes;
	}
	
	public void comp(int queryId){
		this.queryId = queryId;
		
		Random rand = new Random();
		List<List<Set<String>>> list = new ArrayList<List<Set<String>>>();
		for(int i = 0;i < 1;i ++){
			List<Set<String>> tmpList = new ArrayList<Set<String>>();
			while(tmpList.size() < 100){
				Set<String> set = new HashSet<String>();
				while(set.size() < i + 1){
					int id = rand.nextInt(nodes[queryId].length);
					String word = nodes[queryId][id];
					set.add(word);
				}
				tmpList.add(set);
			}
			list.add(tmpList);
 		}
		
		for(int i = 0;i < 1;i ++){
			for(int k = 6;k <= 10; k += 2){
				int ok[] = findStark(list.get(i), k);
				System.out.println("|S|:" + (i + 1) + " star-" + k + " %:" + ok[k] / 100.0);
			}
			System.out.println();
		}
		
//		testACQ(list);
	}
	
	private int[] findStark(List<Set<String>> list, int k){
		int ok[] = new int[100];
		for(Set<String> set:list){
			Set<Integer> hop3KwSet = findHop3Keyword(set);
			int size = hop3KwSet.size();
			for(int i = 5;i <= size && i < 100;i ++)   ok[i] += 1;
		}
		return ok;
	}
	
	private Set<Integer> findHop3(){
		//step 1: initialize
		Set<Integer> selectSet = new HashSet<Integer>();
		
		//step 2: search
		for(int i = 1;i < graph[queryId].length;i ++){
			int firstNgh = graph[queryId][i];
			selectSet.add(firstNgh);
			for(int j = 1;j < graph[firstNgh].length;j ++){
				int secondNgh = graph[firstNgh][j];
				selectSet.add(secondNgh);
				for(int k = 1;k < graph[secondNgh].length;k ++){
					int thirdNgh = graph[secondNgh][k];
					selectSet.add(thirdNgh);
				}
			}
		}
		
		return selectSet;
	}
	
	private Set<Integer> findHop3Keyword(Set<String> set){
		//step 1: initialize
		Set<Integer> selectSet = new HashSet<Integer>();
		
		//step 2: search
		for(int i = 1;i < graph[queryId].length;i ++){
			int firstNgh = graph[queryId][i];
			if(contain(firstNgh, set)){
				selectSet.add(firstNgh);
			}else{
				for(int j = 1;j < graph[firstNgh].length;j ++){
					int secondNgh = graph[firstNgh][j];
					if(contain(secondNgh, set)){
						selectSet.add(secondNgh);
					}else{
						for(int k = 1;k < graph[secondNgh].length;k ++){
							int thirdNgh = graph[secondNgh][k];
							if(contain(thirdNgh, set))   selectSet.add(thirdNgh);
						}
					}
				}
			}
		}
		
		return selectSet;
	}
	
	private boolean contain(int id, Set<String> set){
		int count = 0;
		for(int i = 1;i < nodes[id].length;i ++){
			String word = nodes[id][i];
			if(set.contains(word))   count += 1;
		}
		if(count == set.size())   return true;
		else                      return false;
	}
	
	private void testACQ(List<List<Set<String>>> list){
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		TNode root = index.build();
		int core[] = index.getCore();
		System.out.println("index construction finished !");
		
		for(int i = 0;i < list.size();i ++){
			List<Set<String>> tmpList = list.get(i);
			int ok = 0;
			for(int j = 0;j < tmpList.size();j ++){
				Set<String> set = tmpList.get(j);
				String node[] = new String[set.size() + 1];
				nodes[queryId][0] = "jiawei";
				int idx = 1;
				for(String id:set){
					node[idx] = id;
					idx += 1;
				}
				nodes[queryId] = node;
				
				Config.k = 4;
				DecShare dec = new DecShare(graph, nodes, root, core, null);
//				int size = dec.query(queryId);
//				if(size >= 1)   ok += 1;
//				System.out.println("nodes[queryId][1]:" + size);
			}
			System.out.println("ACQ |S|=" + (i + 1) + " %:" + ok / 100.0);
		}
	}
	
	public static void main(String[] args) {
		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		GPMStar cmp = new GPMStar(graph, nodes);
//		cmp.comp(15238);
		cmp.comp(152532);//jiawei han
	}

}
