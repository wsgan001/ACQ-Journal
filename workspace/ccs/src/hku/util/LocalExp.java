package hku.util;
import hku.Config;

import java.util.*;
/**
 * @author fangyixiang
 * @date Sep 11, 2015
 */
public class LocalExp {

	public static void main(String[] args) {
		int graph[][] = new int[6][];
		int a1[] = {2, 3, 4};		graph[1] = a1;
		int a2[] = {1, 3, 4};	graph[2] = a2;
		int a3[] = {1, 2, 4, 5};graph[3] = a3;
		int a4[] = {1, 2, 3, 5};	graph[4] = a4;
		int a5[] = {3, 4};		graph[5] = a5;
		int queryId = 1;
		Set<Integer> candSet = new HashSet<Integer>();
		candSet.add(1);   candSet.add(2);
		candSet.add(3);   candSet.add(4);
		candSet.add(5);
		
		//the local expansion queue, which contains the connected candidates
		IndexMinPQ<Integer> ccsQ = new IndexMinPQ<Integer>(graph.length);
		IndexMaxPQ<Integer> expQ = new IndexMaxPQ<Integer>(graph.length);
		expQ.insert(queryId, 0);
		
		while(expQ.size() > 0){
			int degree = expQ.maxKey();
			int curId = expQ.delMax();
			System.out.println("curId:" + curId);
			
			//step 1: select a node and put it into ccsQ
			ccsQ.insert(curId, degree);
			
			//step 2: increase some keys in ccsQ and expQ
			for(int index:graph[curId]){
				if(ccsQ.contains(index)){
					int orgDegree = ccsQ.keyOf(index);
					ccsQ.increaseKey(index, orgDegree + 1);
				}else if(expQ.contains(index)){
					int orgDegree = expQ.keyOf(index);
					expQ.increaseKey(index, orgDegree + 1);
				}
			}
			
			//step 3: update expQ with local expansion
			for(int nghId:graph[curId]){
				if(candSet.contains(nghId) && ccsQ.contains(nghId) == false
						&& expQ.contains(nghId) == false){//keyword filtering
					int degCount = 0;
					for(int tmpId:graph[nghId]){
						if(ccsQ.contains(tmpId)){
							degCount += 1;
						}
					}
					expQ.insert(nghId, degCount);
				}
			}
			
			//a cc has been found
			if(ccsQ.minKey() >= Config.k)   break;
		}
		
		for(int i = 1;i <= 5;i ++){
			if(ccsQ.contains(i)){
				System.out.println("cssQ index=" + i + " deg=" + ccsQ.keyOf(i));
			}
		}
		System.out.println();
		
		for(int i = 1;i <= 5;i ++){
			if(expQ.contains(i)){
				System.out.println("expQ index=" + i + " deg=" + expQ.keyOf(i));
			}
		}
		System.out.println();
	}

}
