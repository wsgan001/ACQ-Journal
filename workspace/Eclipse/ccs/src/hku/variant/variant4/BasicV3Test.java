package hku.variant.variant4;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.online.BasicG;
import hku.algo.online.BasicW;
import hku.algo.query2.Dec;
import hku.algo.query2.DecRevision;
import hku.algo.query2.DecShare;
import hku.variant.variant4.*;



public class BasicV3Test {


public static void main(String []args){
	
//	DataReader dataReader=new DataReader(Config.flickrGraph, Config.flickrNode);
	DataReader dataReader=new DataReader(Config.dblpGraph, Config.dblpNode);
	int[][] g=dataReader.readGraph();
	String[][] n=dataReader.readNode();
	
	Config.k=4;
	int queryset[]={152532};//152532:jim gary 16704:Gerhard Weikum
	String[] kwStrings={"art","research","test","data" ,"management","system","transaction"};
		
	Long time1=System.nanoTime();
	BasicGV3_V2  basicGV3_V2=new BasicGV3_V2(n, g);
	basicGV3_V2.query(kwStrings, queryset);
	Long time2=System.nanoTime()-time1;
	System.out.println(time2/1000000);
	basicGV3_V2.testOutput();
	
//	BasicWV3_V2 basicWV3_V2=new BasicWV3_V2(n, g);
//	Long time3=System.nanoTime();
//	basicWV3_V2.query(queryset,kwStrings);
//	basicWV3_V2.testOutput();
//	Long time4=System.nanoTime()-time3;
//	System.out.println(time4/1000000);
//	
//	AdvancedIndex advancedIndex=new AdvancedIndex(g, n);
//	TNode root=advancedIndex.build();
//	SW_V2 s1=new SW_V2(n,g,root);
//	Long time5=System.nanoTime();
//	s1.query(queryset, kwStrings);
//	Long time6=System.nanoTime()-time5;
//	System.out.println(time6/1000000);
//	s1.testOutput();

	
	
	
	int graph[][] = new int[7][];
	int a1[] = {2, 3, 4, 5,6};graph[1] = a1;
	int a2[] = {1, 3, 4,5,6};graph[2] = a2;
	int a3[] = {1,2, 4};	graph[3] = a3;
	int a4[] = {1, 2, 3};graph[4] = a4;
	int a5[] = {1, 2, 6};	graph[5] = a5;
	int a6[] = {1,2,5};			graph[6] = a6;
//	int a7[] = {1,5,6};			graph[7] = a7;
//	int a8[] = {9};			graph[8] = a8;
//	int a9[] = {8};			graph[9] = a9;
	
	String nodes[][] = new String[7][];
	String k1[] = {"A", "x", "y"};nodes[1] = k1;
	String k2[] = {"B", "x", "y"};				nodes[2] = k2;
	String k3[] = {"C", "x"};			nodes[3] = k3;
	String k4[] = {"D", "x"};		nodes[4] = k4;
	String k5[] = {"E", "y"};			nodes[5] = k5;
	String k6[] = {"F", "y"};			nodes[6] = k6;
//	String k7[] = {"G", "y"};				nodes[7] = k7;
//	String k8[] = {"H", "x"};				nodes[8] = k8;
//	String k9[] = {"I", "y", "z"};			nodes[9] = k9;
	
	AdvancedIndex index=new AdvancedIndex(graph, nodes);
	TNode root1=index.build();
	SW_V2 sw_V2=new SW_V2(nodes, graph, root1);
	Config.k=3;
	int[] query={1,2};
	String[] kw={"x","y"};
	sw_V2.query(query,kw);
	sw_V2.testOutput();
}
}