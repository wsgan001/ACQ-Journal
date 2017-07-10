package algorithm;

import java.util.*;
import config.Config;

/**
@author chenyankai
@Date	Jul 8, 2017

*/

public class FindCKCore {
	
	public int[] findCKC(int graph[][], int core[], int queryId){
		if(core[queryId]<Config.k){
			int[] tmp={queryId};
			return tmp;
		}
		
		Set<Integer> visited=new HashSet<Integer>();
		Queue<Integer> queue=new LinkedList<Integer>();
		
		//step1 initialzie
		queue.add(queryId);
		visited.add(queryId);
		
		//step2 findCKC 
		while(queue.size()!=0){
			int cur=queue.poll();
			for(int i=0;i<graph[cur].length;i++){
				int neighbor=graph[cur][i];
				if(!visited.contains(neighbor) && core[neighbor]>=Config.k){
					queue.add(neighbor);
					visited.add(neighbor);
				}
			}
		}
		
		//step3 transform and output
		int[] output=new int[visited.size()];
		Iterator<Integer> iter=visited.iterator();
		int index=0;
		while(iter.hasNext()){
			output[index]=iter.next();
		}
		return output;	
	}
	

}
