package algorithm.kwIndex;

import java.util.*;

import javax.security.auth.Subject;

import algorithm.DecomposeKCore;
import algorithm.ProfiledTree.CPTree;
import algorithm.ProfiledTree.PNode;
import algorithm.kwIndex.Query1.Query1_V1;
import config.Config;
import prep.BuildMeshTree;
import prep.MeshNode;

public class Test {
	private int[][] graph = null;
	private int[][] nodes = null;
	
private void runningExample(){
	int[][] graph = new int[29][]; 
	int[] a1={2,3}; 						graph[1]=a1;
	int[] a2={1,3,4,9}; 					graph[2]=a2;
	int[] a3={1,2,4,5,6,7}; 				graph[3]=a3;
	int[] a4={2,3,5,6,7}; 					graph[4]=a4;
	int[] a5={3,4,6,7,8};					graph[5]=a5;
	int[] a6={3,4,5,7,8}; 					graph[6]=a6;
	int[] a7={3,4,5,6,9}; 					graph[7]=a7;
	int[] a8={5,6}; 						graph[8]=a8;
	int[] a9={2,7,10}; 						graph[9]=a9;
	int[] a10={9,11,18}; 					graph[10]=a10;
	int[] a11={10,12,13}; 					graph[11]=a11;
	int[] a12={11,13}; 						graph[12]=a12;
	int[] a13={11,12,14,17}; 				graph[13]=a13;
	int[] a14={13,15,16,17};				graph[14]=a14;
	int[] a15={14,16,17}; 					graph[15]=a15;
	int[] a16={14,15,17,18}; 				graph[16]=a16;
	int[] a17={13,14,15,16,18}; 			graph[17]=a17;
	int[] a18={10,16,17}; 					graph[18]=a18;
	int[] a19={20,21,22,27}; 				graph[19]=a19;
	int[] a20={19,21,22,}; 					graph[20]=a20;
	int[] a21={19,20,22,23,24,25,26}; 		graph[21]=a21;
	int[] a22={19,20,21,23,24,25,26}; 		graph[22]=a22;
	int[] a23={21,22,24,25,26}; 			graph[23]=a23;
	int[] a24={21,22,23,25,26}; 			graph[24]=a24;
	int[] a25={21,22,23,24,26}; 			graph[25]=a25;
	int[] a26={21,22,23,24,25}; 			graph[26]=a26;
	int[] a27={19}; 						graph[27]=a27;
	int[] a28={}; 							graph[28]=a28;
	
	int[][] nodes = new int[29][]; 
	int k1[] = {1,2,3,4,6,7,10,12,13,14,15};	nodes[1] = k1;
	int k2[] = {1,2,3,4,7,8,9,10,11,12,13,14,15};		nodes[2] = k2;
	int k3[] = {1,3,4,5,6,10};				nodes[3] = k3;
	int k4[] = {1,2,3,7,10};				nodes[4] = k4;
	int k5[] = {1,3,7,8,9,10};				nodes[5] = k5;
	int k6[] = {1,2,3,4,7,10,12,13,14,15};	nodes[6] = k6;
	int k7[] = {1,3,7,8,9};					nodes[7] = k7;
	int k8[] = {1,10,11,12,13,14,15};				nodes[8] = k8;
	int k9[] = {1,2,3,4,10};				nodes[9] = k9;
	int k10[] = {1,3,7};					nodes[10] = k10;
	int k11[] = {1,2,3,4,6,7,12,13,14,15};	nodes[11] = k11;
	int k12[] = {1,2,3,7,8,9,12,13,14,15};		nodes[12] = k12;
	int k13[] = {1,3,4,5,6,10};				nodes[13] = k13;
	int k14[] = {1,2,3,7,10};			nodes[14] = k14;
	int k15[] = {1,3,7,8,9,10};				nodes[15] = k15;
	int k16[] = {1,2,3,4,10,12,13,14,15};	nodes[16] = k16;
	int k17[] = {1,3,7,8,9};				nodes[17] = k17;
	int k18[] = {1,10,11,12,13,14,15};			nodes[18] = k18;
	int k19[] = {1,2,3,4,10};				nodes[19] = k19;
	int k20[] = {1,3,7};					nodes[20] = k20;
	int k21[] = {1,2,3,4,6,7,12,13,14,15};	nodes[21] = k21;
	int k22[] = {1,2,3,7,8,9,12,13,14,15};		nodes[22] = k22;
	int k23[] = {1,3,4,5,6,10};				nodes[23] = k23;
	int k24[] = {1,2,3,7,10};			nodes[24] = k24;
	int k25[] = {1,3,7,8,9,10};				nodes[25] = k25;
	int k26[] = {1,2,3,4,10,12,13,14,15};	nodes[26] = k26;
	int k27[] = {1,3,7,8,9};				nodes[27] = k27;
	int k28[] = {1,10,11,12,13,14,15};			nodes[28] = k28;
	
	this.graph = graph;
	this.nodes = nodes;
}


private void test(){
	
	runningExample();
//	int[] core=kCore.decompose();
//	for(int i=1;i<core.length;i++) System.out.println("a"+i+":"+core[i]);
//	
////	
//	KTree kTree=new KTree(subGraph);
//	kTree.build();
//	System.out.println(kTree.getRoot().toString(""));
	
	
//	BuildMeshTree bmTree=new BuildMeshTree();
//	bmTree.buildMeshTree();
//	PNode root=bmTree.getCPTree().get(1);
	
	CPTree cpTree = new CPTree();
	PNode root = cpTree.LoadTree().get(1);
//	
	KWTree kwTree = new KWTree(graph, nodes,root);
//	int[] test={1,3,5,6,7,9,10,12,17,18,14,16,19,20,21,22,24,26,28};
//	Set<Integer> vertex =new HashSet<Integer>();
//	for(int x:test) vertex.add(x);
//	int[][] subGraph = kwTree.getsubGraph(vertex);
//	DecomposeKCore kCore = new  DeKWNode currentNode:headList.get(queryId)KWNode currentNode:headList.get(queryId)composeKCore(subGraph);
//	int core[] = kCore.decompose(); 
//	int[] old = subGraph[0];
//	for(int i=1;i<old.length;i++){
//		System.out.println(old[i]+" core number is: "+core[i]);
//	}

	kwTree.build();
	kwTree.printTree();
//	Config.k = 4;
//	Query1_V1 query1 = new Query1_V1(kwTree);
//	query1.query(4);
 	
//	Map<Integer,Set<KWNode>> map=kwTree.getHeadMap();
//	int index=0;
//	while(index<map.size()){
//		Set<KWNode> set = map.get(index);
//		if(set==null) {
//			index++;
//			continue;
//		}
//		for(KWNode node:set){
//			System.out.println("id "+index+"    "+node.KTree.get(index).getVertices().toString());
//		}
//		index++;
//	}
//	
}
	
	


private List<List<Integer>> getSubgraph(List<Integer> vertex){
	List<List<Integer>> subGraph = new ArrayList<List<Integer>>();
	List<Integer> list =new ArrayList<>();
	//vertex id starts from 1, thus here we add 0 to align the index and its values for easy computing afterwards
	list.add(0);
	list.addAll(vertex);
	subGraph.add(list);

	Map<Integer, Integer> old2New = new HashMap<Integer,Integer>();
	Iterator<Integer> iter = vertex.iterator();
	System.out.println(iter.next());
	int newId=1;
	// reorganize the id of subgraph
	while(iter.hasNext()) {
		int oldId=iter.next();
		old2New.put(oldId, newId++);
	}
	
	//construct the adjacency matrix of the subgraph
	iter = vertex.iterator();
	iter.next();
	while(iter.hasNext()) {
		int oldId=iter.next();
		int[] nghbor = graph[oldId];
		List<Integer> newNghbr = new ArrayList<Integer>();
		for(int x:nghbor){
			if (vertex.contains(x)) 
				newNghbr.add(old2New.get(x));
		}
		subGraph.add(newNghbr);
	}
	return subGraph;
}




	
	public static void main(String[] args ){
		Test test=new Test();
		test.test();
		

		
	}
	
}
