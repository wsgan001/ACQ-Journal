package hku.exp.revision;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.query2.DecShare;
import hku.exp.sim.AMFreq;
import hku.exp.sim.APJSim;
import hku.exp.sim.AQJSim;
import hku.exp.sim.AWFreq;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author fangyixiang
 * @date May 24, 2016
 */
public class GPMRandom {
	private int queryId = -1;
	private int graph[][] = null;
	private String nodes[][] = null;
	
	public GPMRandom(int graph[][], String nodes[][]){
		this.graph = graph;
		this.nodes = nodes;
	}
	
	public void comp(int queryId){
		this.queryId = queryId;
		
		for(int s = 1; s <= 6;s ++){
			Random rand = new Random();
			List<List<Set<String>>> list = new ArrayList<List<Set<String>>>();
			for(int i = 0;i < 100;i ++){
				List<Set<String>> tmpList = new ArrayList<Set<String>>();
				while(tmpList.size() < 10){
					Set<String> set = new HashSet<String>();
					int kwsLen = s;
					while(set.size() < kwsLen){
						int id = rand.nextInt(nodes[queryId].length - 1) + 1;
						String word = nodes[queryId][id];
						set.add(word);
					}
					tmpList.add(set);
				}
				list.add(tmpList);
	 		}
			
			System.out.println("|S|=" + s);
			findStark(list, 6);
			findStark(list, 8);
			findStark(list, 10);
			System.out.println();
		}
	}
	
	private void findStark(List<List<Set<String>>> list, int k){
		int count = 0;
		double cpjSum = 0;
		double cmfSum = 0;
		
		
		for(int i = 0;i < list.size();i ++){
			List<Set<String>> tmpList = list.get(i);
			
			//consider a star
			Set<Integer> selectedSet = new HashSet<Integer>();
			selectedSet.add(queryId);
			
			int ct = 0;
			Set<Integer> cmSet = new HashSet<Integer>();
			List<Set<String>> kwsList = tmpList.subList(0, k);
			
			for(int j = 0;j < kwsList.size();j ++){
				Set<String> set = kwsList.get(j);
//				System.out.println("|set|:" + set.size());
				
				Set<Integer> tmpSet = new HashSet<Integer>();
				Set<Integer> pathSet = findHop3Keyword(set, tmpSet);
//				Set<Integer> pathSet = findHop3Keyword(set, cmSet);
				
				if(pathSet.size() > 0){
					ct += 1;
					cmSet.addAll(pathSet);
				}
			}
			
			if(ct == k){
				APJSim apj = new APJSim(nodes);
				AMFreq adf = new AMFreq(nodes);
				
				
				double apjValue = apj.singleSim(cmSet);
				double adfValue = adf.singleSim(cmSet, queryId);
				
				count += 1;
				cmfSum += adfValue;
				cpjSum += apjValue;
			}else{
//				System.out.print("No result:");
//				for(Set<String> set:tmpList){
//					for(String word:set){
//						System.out.print(word + " ");
//					}
//				}
//				System.out.println();
			}
		}
		System.out.println("star-" + k + " cmf:" + (cmfSum / count) + " cpj:" + (cpjSum / count) + " count:" + count);
	}
	
	private Set<Integer> findHop3Keyword(Set<String> set, Set<Integer> visitSet){
		boolean flag = false;
		Set<Integer> selectSet = new HashSet<Integer>();
		
		for(int i = 1;i < graph[queryId].length;i ++){
			int firstNgh = graph[queryId][i];
			if(contain(firstNgh, set) && !visitSet.contains(firstNgh)){
				selectSet.add(firstNgh);
				flag = true;
			}else{
				for(int j = 1;j < graph[firstNgh].length;j ++){
					int secondNgh = graph[firstNgh][j];
					if(contain(secondNgh, set) && !visitSet.contains(secondNgh)){
						selectSet.add(firstNgh);
						selectSet.add(secondNgh);
						flag = true;
					}else{
						for(int k = 1;k < graph[secondNgh].length;k ++){
							int thirdNgh = graph[secondNgh][k];
							if(contain(thirdNgh, set) && !visitSet.contains(thirdNgh)){
								selectSet.add(firstNgh);
								selectSet.add(secondNgh);
								
								flag = true;
								break;
							}
						}
					}
					
					if(flag)   break;
				}
			}
			
			if(flag)   break;
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
	
	public static void main(String[] args) {
		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		GPMRandom cmp = new GPMRandom(graph, nodes);
//		cmp.comp(15238);
		cmp.comp(152532);
	}

}
