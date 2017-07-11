package algorithm;

import java.util.HashMap;
import java.util.Map;

import config.Config;

/**
@author chenyankai
@Date	Jul 6, 2017
steps: (1) k-core; (2) maximal k+1 frequent subtree mining; (3) k-core. 
*/


public class BasicAlgo {
	private int graph[][]=null;//graph structure
	private int nodes[][]=null;//the tree nodes of each node
	private int core[]=null;
	private int queryId=-1;
	

	public BasicAlgo(int graph[][],int nodes[][]){
		this.graph=graph;
		this.nodes=nodes;
		DecomposeKCore kCore=new DecomposeKCore(graph);
		core=kCore.decompose();
	}
	
	public void query(int queryId){
		this.queryId=queryId;
		if(core[queryId]<Config.k){
			System.out.println("No qualified connected k-core!");
			return;
		}
		
		//step 1:find the connected k-core containing queryId
		FindCKCore findCKCore=new FindCKCore();
		int[] CKC=findCKCore.findCKC(graph, core, queryId);
		if(CKC.length<Config.k+1) return; 
		
		//step 2:lcs and filter those shared no subsequences
		LCS lcs=new LCS();
		Map<Integer, int[]> midRslt=new HashMap<Integer, int[]>();
		for(int vertex:CKC){
			int[] tmp=lcs.lcs(nodes[queryId], nodes[vertex]);
			if(tmp.length!=0) midRslt.put(vertex, tmp);
		}
		if(midRslt.size()<Config.k+1) return;
		
		//step 3: mining all maximal common subsequences
		
	}
	
	public static void main(String[] args){
		int graph[][] = new int[11][];
		int a1[] = {2, 3, 4, 5};graph[1] = a1;
		int a2[] = {1, 3, 4, 5};graph[2] = a2;
		int a3[] = {1, 2, 4};	graph[3] = a3;
		int a4[] = {1, 2, 3, 7};graph[4] = a4;
		int a5[] = {1, 2, 6};	graph[5] = a5;
		int a6[] = {5};			graph[6] = a6;
		int a7[] = {4};			graph[7] = a7;
		int a8[] = {9};			graph[8] = a8;
		int a9[] = {8};			graph[9] = a9;
		int a10[] = {};			graph[10] = a10;
		
		
		int nodes[][] = new int[11][];
		int k1[] = {1,2,3,4};nodes[1] = k1;
		int k2[] = {};				nodes[2] = k2;
		int k3[] = {};				nodes[3] = k3;
		int k4[] = {};		nodes[4] = k4;
		int k5[] = {};			nodes[5] = k5;
		int k6[] = {};				nodes[6] = k6;
		int k7[] = {};			nodes[7] = k7;
		
		
		
	}
	

	
}
