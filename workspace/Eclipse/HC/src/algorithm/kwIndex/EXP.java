package algorithm.kwIndex;


import algorithm.ProfiledTree.CPTree;
import algorithm.ProfiledTree.PNode;
import algorithm.basic.BFS;
import algorithm.basic.DFS;
import algorithm.basic.BFS;
import algorithm.kwIndex.Query1_margin.Query1;
import algorithm.kwIndex.Query2.Query2_Inc;
import algorithm.kwIndex.Query2.query2_MP;
import algorithm.simpleKWIndex.simKWTree;
import algorithm.simpleKWIndex.query1.query1;
import config.Config;

public class EXP {
public EXP(){}
	
	public void Test(){
		int graph[][] = new int[11][];
		int a1[] = {2, 3, 4, 5};	graph[1] = a1;
		int a2[] = {1, 3, 4, 5};	graph[2] = a2;
		int a3[] = {1, 2, 4, 5};		graph[3] = a3;
		int a4[] = {1, 2, 3,5,7};	graph[4] = a4;
		int a5[] = {1, 2,3,4, 6};		graph[5] = a5;
		int a6[] = {5};				graph[6] = a6;
		int a7[] = {4};				graph[7] = a7;
		int a8[] = {9};				graph[8] = a8;
		int a9[] = {8};				graph[9] = a9;
		int a10[] = {};				graph[10] = a10;
		
		
		int nodes[][] = new int[11][];
		int k1[] = {1,2,3,4,6,7,12,13,14,15};	nodes[1] = k1;
		int k2[] = {1,2,3,4,7,8,9,12,13,14,15};	nodes[2] = k2;
		int k3[] = {1,3,4,5,6,10};			nodes[3] = k3;
		int k4[] = {1,2,3,7,10,12};			nodes[4] = k4;
		int k5[] = {1,3,4,7,8,9,10};		nodes[5] = k5;
		int k6[] = {1,2,3,4,10,12,13,14,15};nodes[6] = k6;
		int k7[] = {1,3,7,8,9};				nodes[7] = k7;
		int k8[] = {1,10,11,12,15};			nodes[8] = k8;
		int k9[] = {1,2,3,4};				nodes[9] = k9;
		int k10[] = {1,3,7,12};				nodes[10] = k10;
		
		CPTree cpTree = new CPTree();
		PNode root = cpTree.LoadTree().get(1);
		
		KWTree kwTree = new KWTree(graph, nodes, root);
		kwTree.build();
//		kwTree.printTree();
		Config.k = 2;
		Query1 query = new Query1(graph,kwTree.getHeadList());
		query2_MP query2_MP = new query2_MP(graph, kwTree.getHeadList());
//		Query2_Inc query2_Inc= new Query2_Inc(graph, kwTree.getHeadList());
//		Query1_V2 query = new Query1_V2(graph,kwTree.getHeadList());
//		simKWTree kwTree = new simKWTree(graph, nodes, root);
//		kwTree.build();
//		query1 query1 = new query1(graph, kwTree.getHeadList());
//		Query2_Inc query = new Query2_Inc(kwTree); 
//		BFS bfs = new BFS(graph, nodes);
//		DFS dfs = new DFS(graph, nodes);
//		long time = System.nanoTime();
//		bfs.query(1);
//		dfs.query(1);
//		System.out.println((System.nanoTime()-time)/1000);
		
		long time1 = System.nanoTime();
//		query.query(1);
//		query.query(1);
		query2_MP.query(1);
		query2_MP.print();
//		query2_Inc.query(2);
//		query2_Inc.print();
		System.out.println((System.nanoTime()-time1)/1000);
	
		
	}
		
	public static void main(String[] args){
		EXP exp = new EXP();
		exp.Test();
	}
	
}