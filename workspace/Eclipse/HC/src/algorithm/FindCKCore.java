package algorithm;

import java.util.*;
import config.Config;

/**
@author chenyankai
@Date	Jul 8, 2017
to find a connected component in a graph(k-core)

*/

public class FindCKCore {
	
	public Set<Integer> findCKC(int graph[][], int core[], int queryId){
		if(core[queryId]<Config.k){
			return null;
		}
		
		Set<Integer> visited=new HashSet<Integer>();
		Queue<Integer> queue=new LinkedList<Integer>();
		
		//step1 initialize
		queue.add(queryId);
		visited.add(queryId);
		
		//step2 find connected component
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
		Set<Integer> output=new HashSet<Integer>(visited.size());
		Iterator<Integer> iter=visited.iterator();
		while(iter.hasNext()){
			output.add(iter.next());
		}
		return output;	
	}
	
	
	
	

}
