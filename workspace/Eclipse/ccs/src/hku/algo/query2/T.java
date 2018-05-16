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

public class T {

	public static void main(String[] args) {

//		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
//		int graph[][] = dataReader.readGraph();
//		String nodes[][] = dataReader.readNode();
//		
//		AdvancedIndex index = new AdvancedIndex(graph, nodes);
//		TNode root = index.build();
//		int core[] = index.getCore();
//		System.out.println("index construction finished !");
//		
//		String node[] = {"data", "mine"};
//		nodes[15238] = node;
//		
////		Config.k = 4;
//		DecShare dec = new DecShare(graph, nodes, root, core, null);
//		dec.query(15238);
		
		
		int graph[][] = new int[10][];
		int a1[] = {2, 3, 4, 5,6};graph[1] = a1;
		int a2[] = {1, 3, 4,5,6};graph[2] = a2;
		int a3[] = {1,2, 4};	graph[3] = a3;
		int a4[] = {1, 2, 3};graph[4] = a4;
		int a5[] = {1, 2, 6};	graph[5] = a5;
		int a6[] = {1,2,5};			graph[6] = a6;
		int a7[] = {1,5,6};			graph[7] = a7;
		int a8[] = {9};			graph[8] = a8;
		int a9[] = {8};			graph[9] = a9;
		
		String nodes[][] = new String[10][];
		String k1[] = {"A", "x", "y"};nodes[1] = k1;
		String k2[] = {"B", "x"};				nodes[2] = k2;
		String k3[] = {"C", "x"};			nodes[3] = k3;
		String k4[] = {"D", "x"};		nodes[4] = k4;
		String k5[] = {"E", "y"};			nodes[5] = k5;
		String k6[] = {"F", "y"};			nodes[6] = k6;
		String k7[] = {"G", "y"};				nodes[7] = k7;
		String k8[] = {"H", "x"};				nodes[8] = k8;
		String k9[] = {"I", "y", "z"};			nodes[9] = k9;

		AdvancedIndex index1 = new AdvancedIndex(graph, nodes);
		TNode root1 = index1.build();
		Config.k = 3;
		Dec dec2=new Dec(graph, nodes, root1);
		dec2.query(1);

//		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
//		int graph[][] = dataReader.readGraph();
//		String nodes[][] = dataReader.readNode();

		
//		AdvancedIndex index = new AdvancedIndex(graph, nodes);
//		TNode root = index.build();
//		int core[] = index.getCore();
//		System.out.println("index construction finished !");
//		
//		String node[] = {"data", "mine"};
//		nodes[15238] = node;
//		
//		Config.k = 4;
//		DecShare dec = new DecShare(graph, nodes, root, core, null);
//		dec.query(15238);
	}
}