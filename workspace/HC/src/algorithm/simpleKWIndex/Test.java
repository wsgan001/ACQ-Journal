package algorithm.simpleKWIndex;

import java.util.HashSet;
import java.util.Set;

import algorithm.ProfiledTree.*;
import config.Config;
import prep.BuildMeshTree;

public class Test {

	public void test(){
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
		int k2[] = {1,2,3,4,7,8,9,12,13,15};	nodes[2] = k2;
		int k3[] = {1,3,4,5,6,10};			nodes[3] = k3;
		int k4[] = {1,2,3,7,10,12};			nodes[4] = k4;
		int k5[] = {1,3,4,7,8,9,10};		nodes[5] = k5;
		int k6[] = {1,2,3,4,10,12,13,14,15};nodes[6] = k6;
		int k7[] = {1,3,7,8,9};				nodes[7] = k7;
		int k8[] = {1,3,10,11,12,15};			nodes[8] = k8;
		int k9[] = {1,2,3,4};				nodes[9] = k9;
		int k10[] = {1,3,7,12};				nodes[10] = k10;
		CPTree cpTree = new CPTree();
		PNode root = cpTree.LoadTree().get(1);
		KWTree kwTree = new KWTree(graph, nodes, root);
		kwTree.build();
		kwTree.printTree();
		Config.k =2;
		query1 query1 = new query1(kwTree);
		query1.query(3);
//		query1.print();
 		
	}
	
	public void runRealData(String graphFile,String nodeFile){
		BuildMeshTree bmTree=new BuildMeshTree();
		bmTree.buildMeshTree();
		PNode root=bmTree.getCPTree().get(1);
		KWTree kwTree = new KWTree(graphFile, nodeFile, root);
		kwTree.build();
	}
	
	
	public void testInt(){
		int[] a = new int[2];
		a[0]=1;
		a[1]=2;
		Set<int[]> set = new HashSet<int[]>();set.add(a);
		a[0]=100;
		for(int[] x:set){
			for(int y:x) System.out.println(y);
		} 
//		
//		Set<Integer> set1 = new HashSet<Integer>();
//		set1.add(1);
//		set1.add(2);
//		
//		Set<Integer> set2 = new HashSet<Integer>();
//		set2.add(1);
//		set2.add(2);
//		System.out.println(set1.hashCode()+"   "+set2.hashCode());
//		
//		
//		Integer[] b = new Integer[2];
//		b[0]=1;
//		b[1]=2;
////		System.out.print(set.contains(a));
//		System.out.println(a.hashCode()+"   "+b.hashCode());
		
	}
	
	
	public static void main(String[] args){
		Test test = new Test();
		test.test();
//		test.runRealData(Config.pubMedGraph, Config.pubMedNode);
//		test.testInt();
	}
	
}
