package hku.exp.sim;

import java.util.*;
/**
 * @author fangyixiang
 * @date Oct 29, 2015
 * compute the average word frequency
 */
public class AWFreq {
	private String nodes[][] = null;
	
	public AWFreq(String nodes[][]){
		this.nodes = nodes;
	}
	
	//compute the AWFreq for all the communities
	public double sim(List<Set<Integer>> ccsList, int queryId){
		double sum = 0.0;
		for(Set<Integer> set:ccsList){
			sum += singleSim(set, queryId);
		}
		return sum / ccsList.size();
	}
	
	//compute the APJSim for a single community
	public double singleSim(Set<Integer> set, int queryId){
		Set<String> qSet = new HashSet<String>();
		for(int i = 1;i < nodes[queryId].length;i ++){
			qSet.add(nodes[queryId][i]);
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		double count = 0.0;
		for(int id:set){
			for(int i = 1;i < nodes[id].length;i ++){
				String word = nodes[id][i];
				if(qSet.contains(word)){//only consider words appearing in q's keyword set
					if(map.containsKey(word)){
						map.put(word, map.get(word) + 1);
					}else{
						map.put(word, 1);
					}
				}
				count += 1;
			}
		}
		
		double sum = 0.0;
		for(Map.Entry<String, Integer> entry:map.entrySet()){
			sum += entry.getValue() / count;
		}
		
		return sum / map.size();
	}
}
