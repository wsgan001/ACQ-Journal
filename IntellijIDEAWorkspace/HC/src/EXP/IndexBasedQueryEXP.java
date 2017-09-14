package EXP;

import algorithm.DataReader;
import algorithm.ProfiledTree.PNode;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.Query1.Query1_V1;
import config.Config;
import prep.BuildMeshTree;

public class IndexBasedQueryEXP {
	
	public IndexBasedQueryEXP(){
		
	}
	
	public void exp(String graphFile,String nodeFile){
		BuildMeshTree bmTree=new BuildMeshTree();
		bmTree.buildMeshTree();
		PNode root=bmTree.getCPTree().get(1);
		KWTree kwTree = new KWTree(graphFile,nodeFile,root);
		kwTree.build();
		Config.k = 10;
		Query1_V1 query = new Query1_V1(kwTree);
		
		query.query(13);
		query.print();
		
	}
	
	public static void main(String[] args){
		IndexBasedQueryEXP exp = new IndexBasedQueryEXP();
		exp.exp(Config.pubMedGraph,Config.pubMedNode);
	}
	
	
}
