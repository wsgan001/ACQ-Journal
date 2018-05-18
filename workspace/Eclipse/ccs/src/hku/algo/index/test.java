package hku.algo.index;



import java.sql.Time;

import hku.Config;
import hku.algo.index.BasicIndex;
import hku.algo.index.AttachKw;
import hku.algo.DataReader;
import hku.algo.TNode;


public class test {
	//2016年12月5日 CYK:  you should set the virtual memory to 4096M max, otherwise the IDE can't calculate it. 
	//DataReader d1DataReader=new DataReader(Config.flickrGraph , Config.flickrNode); 
	//int graph[][]=d1DataReader.readGraph();
	//String nodes[][]=d1DataReader.readNode();  
	//BasicIndex basicIndex=new BasicIndex(graph, nodes);
	//TNode root=basicIndex.build();
	
	
	public static void main (String[] args){
		int graph[][] = new int[11][];
		int a1[] = {2, 3, 4, 5};	graph[1] = a1;
		int a2[] = {1, 3, 4,5};		graph[2] = a2;
		int a3[] = {1, 2, 4};		graph[3] = a3;
		int a4[] = {1, 2, 3, 6};	graph[4] = a4;
		int a5[] = {1, 2, 7};		graph[5] = a5;
		int a6[] = {4};			graph[6] = a6;
		int a7[] = {5};			graph[7] = a7;
		int a8[] = {9};				graph[8] = a8;
		int a9[] = {8};	graph[9] = a9;
		int a10[] = {};	graph[10] = a10;
		
		
		String nodes[][] = new String[graph.length][];
		String b1[] = {"w", "x", "y"};	nodes[1] = b1;
		String b2[] = {"x"};	nodes[2] = b2;
		String b3[] = {"x", "y"};	nodes[3] = b3;
		String b4[] = {"z", "x", "y"};	nodes[4] = b4;
		String b5[] = {"y", "z"};	nodes[5] = b5;
		String b6[] = {"y"};	nodes[6] = b6;
		String b7[] = {"x", "y"};	nodes[7] = b7;
		String b8[] = {"y", "z"};	nodes[8] = b8;
		String b9[] = {"x"};	nodes[9] = b9;
		String b10[] = {"x"};	nodes[10] = b10;
//		DataReader d1DataReader=new DataReader(Config.flickrGraph , Config.flickrNode); 
//		int graph1[][]=d1DataReader.readGraph();
//		String nodes1[][]=d1DataReader.readNode();  
//		BasicIndex basicIndex=new BasicIndex(graph, nodes);
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		TNode root =index.build();
		index.traverse(root);
	
//	 	ac.traverse(root);
		
	
		
	}
}
