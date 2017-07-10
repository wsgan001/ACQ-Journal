package algorithm;

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
		
		//step 2:
		
	}
	
	
	

	
}