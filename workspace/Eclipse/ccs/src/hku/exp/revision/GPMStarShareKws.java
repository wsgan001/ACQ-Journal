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
public class GPMStarShareKws {
	private int queryId = -1;
	private int graph[][] = null;
	private String nodes[][] = null;
	
	public GPMStarShareKws(int graph[][], String nodes[][]){
		this.graph = graph;
		this.nodes = nodes;
	}
	
	public void comp(int queryId){
		this.queryId = queryId;
		
		Set<String> set = new HashSet<String>();
//		set.add("transaction");
//		set.add("data");
//		set.add("management");
//		set.add("system");
//		set.add("research");
		
//		set.add("sloan");
//		set.add("digital");
//		set.add("sky");
//		set.add("data");
//		set.add("sdss");
		
//		set.add("analysis");
//		set.add("data");
//		set.add("information");
//		set.add("network");
		
		set.add("mine");
		set.add("data");
		set.add("pattern");
		set.add("database");
		
		findStark(set, 6);
		findStark(set, 8);
		findStark(set, 10);
	}
	
	private void findStark(Set<String> set, int k){
		Set<Integer> hop3KwSet = findHop3Keyword(set);
		System.out.println("k:" + k + " size:" + hop3KwSet.size());
		for(int id:hop3KwSet){
			String name = nodes[id][0];
			System.out.println(id + ":" + name);
		}
		System.out.println();
	}
	
	private Set<Integer> findHop3Keyword(Set<String> set){
		//step 1: initialize
		Set<Integer> selectSet = new HashSet<Integer>();
		
		//step 2: search
		for(int i = 1;i < graph[queryId].length;i ++){
			int firstNgh = graph[queryId][i];
			if(contain(firstNgh, set) && !selectSet.contains(firstNgh)){
				selectSet.add(firstNgh);
				System.out.println("select:" + firstNgh + "[" + nodes[firstNgh][0] + "]");
			}else{
				for(int j = 1;j < graph[firstNgh].length;j ++){
					int secondNgh = graph[firstNgh][j];
					if(secondNgh != queryId){
						if(contain(secondNgh, set) && !selectSet.contains(secondNgh)){
							selectSet.add(secondNgh);
							System.out.println("select:" + firstNgh + "[" + nodes[firstNgh][0] + "]" + " " 
							                             + secondNgh + "[" + nodes[secondNgh][0] + "]");
						}else{
							for(int k = 1;k < graph[secondNgh].length;k ++){
								int thirdNgh = graph[secondNgh][k];
								if(thirdNgh != queryId && thirdNgh != firstNgh){
									if(contain(thirdNgh, set) && !selectSet.contains(thirdNgh)){
										selectSet.add(thirdNgh);
										System.out.println("select:" + firstNgh + "[" + nodes[firstNgh][0] + "]" + " " 
										                             + secondNgh + "[" + nodes[secondNgh][0] + "] "
										                             + thirdNgh + "[" + nodes[thirdNgh][0] + "]");
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println();
		
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
	
	public static void main(String[] args) {
		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		GPMStarShareKws cmp = new GPMStarShareKws(graph, nodes);
		cmp.comp(15238);
//		cmp.comp(152532);
	}

}
