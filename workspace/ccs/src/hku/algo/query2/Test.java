package hku.algo.query2;

import java.util.*;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.KCore;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.query1.IncS;
import hku.algo.query1.IncT;
import hku.exp.util.QueryIdReader;
import hku.util.Log;

public class Test {

	public static void main(String[] args) {
		int graph[][] = new int[10][];
		int a1[] = {2, 3, 4, 5};graph[1] = a1;
		int a2[] = {1, 3, 4, 5};graph[2] = a2;
		int a3[] = {1, 2, 4};	graph[3] = a3;
		int a4[] = {1, 2, 3, 7};graph[4] = a4;
		int a5[] = {1, 2, 6};	graph[5] = a5;
		int a6[] = {5};			graph[6] = a6;
		int a7[] = {4};			graph[7] = a7;
		int a8[] = {9};			graph[8] = a8;
		int a9[] = {8};			graph[9] = a9;
		
		String nodes[][] = new String[10][];
		String k1[] = {"A", "v", "w", "x", "y"};nodes[1] = k1;
		String k2[] = {"B", "x"};				nodes[2] = k2;
		String k3[] = {"C", "x", "y"};			nodes[3] = k3;
		String k4[] = {"D", "x", "y", "z"};		nodes[4] = k4;
		String k5[] = {"E", "w", "y"};			nodes[5] = k5;
		String k6[] = {"F", "x", "y"};			nodes[6] = k6;
		String k7[] = {"G", "z"};				nodes[7] = k7;
		String k8[] = {"H", "x"};				nodes[8] = k8;
		String k9[] = {"I", "y", "z"};			nodes[9] = k9;
		
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		TNode root = index.build();
		int core[] = index.getCore();
//		for(int i = 1;i < core.length;i ++)   System.out.println("core i=" + i + " " + core[i]);
		
		Config.k = 3;
		DecRevision decr = new DecRevision(graph, nodes, root, core, null);
		Set<Integer> set = decr.query(2);
		if(set == null)   System.out.println("null");
		else          for(int id:set)   System.out.print(id + " ");
		System.out.println();
	}
}
