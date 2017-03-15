package hku.algo.index;

import java.util.HashMap;
import java.util.Map;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.util.Tool;

/**
 * @author fangyixiang
 * @date Sep 18, 2015
 */
public class T {

	public static void main(String[] args) {
		int graph[][] = new int[15][];
		int a1[] = {2, 3, 4, 8};	graph[1] = a1;
		int a2[] = {1, 3, 4};		graph[2] = a2;
		int a3[] = {1, 2, 4};		graph[3] = a3;
		int a4[] = {1, 2, 3, 5};	graph[4] = a4;
		int a5[] = {4, 6, 7};		graph[5] = a5;
		int a6[] = {5, 7};			graph[6] = a6;
		int a7[] = {5, 6};			graph[7] = a7;
		int a8[] = {1};				graph[8] = a8;
		int a9[] = {10, 11, 12};	graph[9] = a9;
		int a10[] = {9, 11, 12};	graph[10] = a10;
		int a11[] = {9, 10, 12};	graph[11] = a11;
		int a12[] = {9, 10, 11, 13};graph[12] = a12;
		int a13[] = {12};			graph[13] = a13;
		int a14[] = {};				graph[14] = a14;
		
//		int graph[][] = new int[11][];
//		int a1[] = {2, 3, 4, 5};graph[1] = a1;
//		int a2[] = {1, 3, 4, 5};graph[2] = a2;
//		int a3[] = {1, 2, 3};	graph[3] = a3;
//		int a4[] = {1, 2, 3, 7};graph[4] = a4;
//		int a5[] = {1, 2, 7};	graph[5] = a5;
//		int a6[] = {4};			graph[6] = a6;
//		int a7[] = {5};			graph[7] = a7;
//		int a8[] = {9};			graph[8] = a8;
//		int a9[] = {8};			graph[9] = a9;
//		int a10[] = {};			graph[10] = a10;
		
		String nodes[][] = new String[graph.length][];
		
//		AdvancedIndex idx = new AdvancedIndex(graph, nodes);
		AdvancedIndex idx = new AdvancedIndex(graph, nodes);
		TNode root = idx.build();
		idx.traverse(root);
		
		Tool tool = new Tool();
//		int all = tool.count(root);
//		System.out.println("total nodes:" + all);
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		tool.countNodeFreq(root, map);
		for(Map.Entry<Integer, Integer> entry:map.entrySet()){
			if(entry.getValue() > 1){
				System.out.println("key:" + entry.getKey());
				System.out.println("value:" + entry.getValue());
			}
		}
		System.out.println("nodes:" + map.size());
	}
}
