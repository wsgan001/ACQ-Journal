package hku.exp.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author fangyixiang
 * @date Feb 20, 2016
 */
public class LinkFreq {
	private int graph[][] = null;
	
	public LinkFreq(int graph[][]){
		this.graph = graph;
	}
	
	//compute the MinLinkFreq for all the communities
	public double[] freq(List<Set<Integer>> ccsList){
		double sum[] = new double[10000];
		for(Set<Integer> set:ccsList){
			double arr[] = singleLinkFreq(set);
			for(int i = 0;i < arr.length;i ++){
				sum[i] += arr[i];
			}
		}
		
		for(int i = 0;i < sum.length;i ++){
			sum[i] = sum[i] / ccsList.size();
		}
		
		return sum;
	}
	
	//compute the MinLinkFreq for a single community
	public double[] singleLinkFreq(Set<Integer> set){
		double arr[] = new double[10000];
		int min = Integer.MAX_VALUE;
		List<Integer> list = new ArrayList<Integer>(set);
		for(int i = 0;i < list.size();i ++){
			int id1 = list.get(i);
			int count = 0;
			for(int j = 0;j < graph[id1].length;j ++){
				int id2 = graph[id1][j];
				if(set.contains(id2))   count += 1;
			}
			arr[count] += 1;
		}
		
		return arr;
	}
}
