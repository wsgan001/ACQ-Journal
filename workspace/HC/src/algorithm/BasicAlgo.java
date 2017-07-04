package algorithm;

/**
@author chenyankai
@Date	Jul 4, 2017
steps: (1) k-core; (2) maximal k+1 frequent subtree mining; (3) k-core. 
*/


public class BasicAlgo {
	private int graph[][]=null;//graph structure
	private int nodes[][]=null;//the tree nodes of each node
	private int core[]=null;

	public BasicAlgo(int graph[][],int nodes[][]){
		this.graph=graph;
		this.nodes=nodes;
		KCore kCore=new KCore(graph);
		core=kCore.decompose();
	}
	
	public void query(int queryId){
		
	}

	
}
