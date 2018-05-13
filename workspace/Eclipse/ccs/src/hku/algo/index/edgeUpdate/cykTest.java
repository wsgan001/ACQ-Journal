package hku.algo.index.edgeUpdate;

import java.util.*;


import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;

public class cykTest {
	
	public boolean compare(TNode[] invert1, TNode[] invert2){
		for(int i=1;i<invert1.length;i++) {
//			System.out.println(i);
			if(invert1[i].getNodeSet().size()!=invert2[i].getNodeSet().size() || invert1[i].getChildList().size()!=invert2[i].getChildList().size()){
				System.out.println("compare");
				System.out.println(invert1[i].getNodeSet().size());
				System.out.println(invert2[i].getNodeSet().size());
				System.out.println(invert1[i].getChildList().size());
				System.out.println(invert2[i].getChildList().size());
				System.out.println(invert1[i].getCore()+"  "+invert2[i].getCore());
				return false;
				}
		}
		return true;
	}
	
	private boolean edgeExist(int u,int v, int[][]graph){
		for(int neighbor:graph[u]){
			if(neighbor==v) return true;
		}
		return false;
	}
	
	public void test500(int[][] graph1,String[][] nodes1){
		int count=0;
		int validTst=0;
		double rebuildTime=0.0;
		double insertTime=0.0;
		for(int i=1;i<51;i++){
			Random random=new Random();
			int u=random.nextInt(graph1.length);
			int v=random.nextInt(graph1.length);
			System.out.println("第 "+i+" 次测试: "+u+ "  " +v);
			if(!edgeExist(u, v,graph1)){
				validTst++;
				AdvancedIndex ac1=new AdvancedIndex(graph1, nodes1);
				TNode root312=ac1.build();
				
				EdgeInsertion edgeUpdate2=new EdgeInsertion(root312, ac1.getInvert(), graph1, nodes1,ac1.getCore());
				
				long time1=System.nanoTime();
				TNode  newroot=edgeUpdate2.insertEdge(u,v);
				long time2=System.nanoTime();
				insertTime+=time2-time1;
				
				
				long time3=System.nanoTime();
				TNode[] array3=edgeUpdate2.rebuild();
				long time4=System.nanoTime();
				rebuildTime+=time4-time3;
				System.out.println("重建时间需要："+(time4-time3)/1000000+ " 动态修改时间： "+(time2-time1)/1000);
				TNode[] array4=edgeUpdate2.travseToInvert(newroot);
				boolean result=compare(array3, array4);
				if(result) count++;
				System.out.println("______"+result);
			}else {
				System.out.println("edge has already exist!");
			}
		 }
//		System.out.println("有效测试次数 "+validTst+"  正确次数 "+count);
		System.out.println("正确次数 "+count);
		System.out.println("时间效率对比："+rebuildTime/insertTime);
	}
	

	public static void main(String[] args){
		 
		int graph[][] = new int[29][];
		 int a1[] = {2,3,4};	graph[1] = a1;
		 int a2[] = {1,3,4};	graph[2] = a2;
		 int a3[] = {1,2,4};	graph[3] = a3;
		 int a4[] = {1,2,3,5};	graph[4] = a4;
		 int a5[] = {4,6};	graph[5] = a5;
		 int a6[] = {5,7,8};	graph[6] = a6;
		 int a7[] = {6,8,9,13};	graph[7] = a7;
		 int a8[] = {6,7,9,10,11,12};	graph[8] = a8;
		 int a9[] = {7,8,10,11,12};	graph[9] = a9;
		 int a10[] = {8,9,11,12,14};	graph[10] = a10;
		 int a11[] = {8,9,10,12,13};	graph[11] = a11;
		 int a12[] = {8,9,10,11,14};	graph[12] = a12;
		 int a13[] = {7,11,15};	graph[13] = a13;
		 int a14[] = {10,12};	graph[14] = a14;
		 int a15[] = {13,16};	graph[15] = a15;
		 int a16[] = {15,17,21,28};	graph[16] = a16;
		 int a17[] = {16,18,19,20};	graph[17] = a17;
		 int a18[] = {17,19,20};	graph[18] = a18;
		 int a19[] = {17,18,20};	graph[19] = a19;
		 int a20[] = {17,18,19};	graph[20] = a20;
		 int a21[] = {16,22,23};	graph[21] = a21;
		 int a22[] = {21,23};	graph[22] = a22;
		 int a23[] = {21,22,24,25};	graph[23] = a23;
		 int a24[] = {23,28,25,26,27};	graph[24] = a24;
		 int a25[] = {23,24,26,27};	graph[25] = a25;
		 int a26[] = {24,25,27};	graph[26] = a26;
		 int a27[] = {24,28,25,26};	graph[27] = a27;
		 int a28[] = {24,27,16};	graph[28] = a28;
		 
		 
		 String nodes[][] = new String[graph.length][];
		 String b1[] = {"w"};	nodes[1] = b1;
		 String b2[] = {"x"};	nodes[2] = b2;
		 String b3[] = {"x", "y"};	nodes[3] = b3;
		 String b4[] = {"z", "x", "y"};	nodes[4] = b4;
		 String b5[] = {"y", "z"};	nodes[5] = b5;
		 String b6[] = {"y"};	nodes[6] = b6;
		 String b7[] = {"x", "y"};	nodes[7] = b7;
		 String b8[] = {"y", "z"};	nodes[8] = b8;
		 String b9[] = {"x"};	nodes[9] = b9;
		 String b10[] = {"x"};	nodes[10] = b10;
		 String b11[] = {"w"};	nodes[11] = b11;
		 String b12[] = {"w"};	nodes[12] = b12;
		 String b13[] = {"w"};	nodes[13] = b13;
		 String b14[] = {"w"};	nodes[14] = b14;
		 String b15[] = {"w"};	nodes[15] = b15;
		 String b16[] = {"w"};	nodes[16] = b16;
		 String b17[] = {"w"};	nodes[17] = b17;
		 String b18[] = {"w"};	nodes[18] = b18;
		 String b19[] = {"x"};	nodes[19] = b19;
		 String b20[] = {"x"};	nodes[20] = b20;
		 String b21[] = {"x"};	nodes[21] = b21;
		 String b22[] = {"x"};	nodes[22] = b22;
		 String b23[] = {"x"};	nodes[23] = b23;
		 String b24[] = {"x"};	nodes[24] = b24;
		 String b25[] = {"x"};	nodes[25] = b25;
		 String b26[] = {"x"};	nodes[26] = b26;
		 String b27[] = {"x"};	nodes[27] = b27;
		 String b28[] = {"x"};	nodes[28] = b28;
		 
		 
//		AdvancedIndex ac=new AdvancedIndex(graph, nodes);
//		 TNode root=ac.build();
//		 ac.traverse(root);
//////////		 TNode[] in=ac.getInvert();
////////		 
////		  ac.traverse(root);
//		 EdgeInsertion ed=new EdgeInsertion(root, ac.getInvert(), graph,nodes, ac.getCore());
//		 TNode newRoot= ed.insertEdge(14,16);	
////////////		 edgeUpdate.duibiTree();
////////////		 System.out.println("##########################");
//////////		 TNode[] array1=edgeUpdate.rebuild();
//////////		 TNode[] array2=edgeUpdate.travseToInvert(newRoot);
//		 ed.traverse(newRoot);
//		 cykTest cTest=new cykTest();
//		 System.out.println(cTest.compare(array1, array2));

		 
		 
//		 System.out.println("……………………………………………………………………………………………………");
//
////		 
		DataReader d1DataReader=new DataReader(Config.dblpGraph , Config.dblpNode); 
		int graph1[][]=d1DataReader.readGraph();
		String nodes1[][]=d1DataReader.readNode();  
//////		long time=System.nanoTime();
		AdvancedIndex ac=new AdvancedIndex(graph1, nodes1);
		TNode root=ac.build();
////		
//		EdgeInsertion edgeUpdate=new EdgeInsertion(root, ac.getInvert(), graph1,nodes1, ac.getCore());
//		long time=System.nanoTime();
//		edgeUpdate.insertEdge(651867 , 308915);
//		root312, ac1.getInvert(), graph1, nodes1,ac1.getCore());
//		
	 	 EdgeDeletion eDeletion=new EdgeDeletion(root, ac.getInvert(), graph1, nodes1, ac.getCore());
	 	 eDeletion.deleteEdge(39125, 83829);
	}
	
}
