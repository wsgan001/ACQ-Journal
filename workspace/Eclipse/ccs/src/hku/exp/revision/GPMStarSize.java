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
public class GPMStarSize {
	private int queryId = -1;
	private int graph[][] = null;
	private String nodes[][] = null;
	
	public GPMStarSize(int graph[][], String nodes[][]){
		this.graph = graph;
		this.nodes = nodes;
	}
	
	public void comp(int queryId){
		this.queryId = queryId;
		
		Random rand = new Random();
		List<List<Set<String>>> list = new ArrayList<List<Set<String>>>();
		for(int i = 0;i < 5;i ++){
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
		
		
		for(int i = 0;i < 5;i ++){
			List<Set<String>> tmpList = list.get(i);
			
			int sum = 0, count = 0, max = 0;
			for(int j = 0;j < tmpList.size();j ++){
				set1 = new HashSet<Integer>();
				set2 = new HashSet<Integer>();
				set3 = new HashSet<Integer>();
				
				Set<String> set = tmpList.get(j);
				findHop3(set);
				int total = set1.size() + set2.size() + set3.size();
				
				if(total >= 6){
					sum += obtainSize(set1, set2, set3, 6);
					max += set3.size();
					count += 1;
				}
			}
			System.out.println("|S|=" + (i + 1) + " size:" + sum * 1.0 / count + " max:" + max * 1.0 / count);
		}

	}
	
	Set<Integer> set1 = null;
	Set<Integer> set2 = null;
	Set<Integer> set3 = null;
	private void findHop3(Set<String> set){
		for(int i = 1;i < graph[queryId].length;i ++){
			int firstNgh = graph[queryId][i];
			if(contain(firstNgh, set) && !set1.contains(firstNgh)){
				set1.add(firstNgh);
//				System.out.println("select:" + firstNgh + "[" + nodes[firstNgh][0] + "]");
			}else{
				for(int j = 1;j < graph[firstNgh].length;j ++){
					int secondNgh = graph[firstNgh][j];
					if(secondNgh != queryId && !set1.contains(secondNgh)){
						if(contain(secondNgh, set) && !set2.contains(secondNgh)){
							set2.add(secondNgh);
//							System.out.println("select:" + firstNgh + "[" + nodes[firstNgh][0] + "]" + " " 
//							                             + secondNgh + "[" + nodes[secondNgh][0] + "]");
						}else{
							for(int k = 1;k < graph[secondNgh].length;k ++){
								int thirdNgh = graph[secondNgh][k];
								if(thirdNgh != queryId && !set1.contains(thirdNgh) && !set2.contains(thirdNgh)){
									if(contain(thirdNgh, set) && !set3.contains(thirdNgh)){
										set3.add(thirdNgh);
//										System.out.println("select:" + firstNgh + "[" + nodes[firstNgh][0] + "]" + " " 
//										                             + secondNgh + "[" + nodes[secondNgh][0] + "] "
//										                             + thirdNgh + "[" + nodes[thirdNgh][0] + "]");
									}
								}
							}
						}
					}
				}
			}
		}
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
	
	private double obtainSize(Set<Integer> set1, Set<Integer> set2, Set<Integer> set3, int k){
		int total = set1.size() + set2.size() + set3.size();
		double p1 = set1.size() * 1.0 / total;
		double p2 = set2.size() * 1.0 / total;
		double p3 = set3.size() * 1.0 / total;
		return 6 * p1 * 1 + 6 * p2 * 2 + 6 * p3 * 3;
	}
	
	public static void main(String[] args) {
		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		GPMStarSize cmp = new GPMStarSize(graph, nodes);
//		cmp.comp(15238);//jiawei han
		cmp.comp(152532);
	}

}
