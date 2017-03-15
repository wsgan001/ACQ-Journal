package hku.exp.sim;

import java.util.*;
/**
 * @author fangyixiang
 * @date Oct 29, 2015
 * compute the AQJSim
 */
public class AQJSim {
	private String nodes[][] = null;
	
	public AQJSim(String nodes[][]){
		this.nodes = nodes;
	}
	
	//compute the APJSim for all the communities
	public double sim(List<Set<Integer>> ccsList, int queryId){
		double sum = 0.0;
		for(Set<Integer> set:ccsList){
			sum += singleSim(set, queryId);
		}
		return sum / ccsList.size();
	}
	
	//compute the APJSim for a single community
	public double singleSim(Set<Integer> set, int queryId){
		double simSum = 0.0;
		int count = 0;
		for(int nodeA: set){
			double simValue = jaccardSim(nodes[nodeA], nodes[queryId]);
			simSum += simValue;
			count += 1;
		}
		
		return simSum / count;
	}
	
	//compute the Jaccard similarity
	private double jaccardSim(String a[], String b[]){
		Set<String> set = new HashSet<String>();
        for(int i = 1;i < a.length;i ++)   set.add(a[i]);
       
        double share = 0.0;
        for(int i = 1;i < b.length;i ++){
            if(set.contains(b[i])){
                share += 1;
            }else{
            	set.add(b[i]);
            }
        }
       
        return share / set.size();
	}
}
