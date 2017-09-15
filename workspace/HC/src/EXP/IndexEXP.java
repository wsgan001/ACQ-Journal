package EXP;

import algorithm.DataReader;
import algorithm.ProfiledTree.PNode;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.KTree;
import config.Config;
import prep.*;
import prep.PubMedPrep.BuildMeshTree;

public class IndexEXP {
	
	

	
	
	public void test(String graphFile,String nodeFile){
		DataReader dReader = new DataReader(graphFile, nodeFile);
		int[][] graph = dReader.readGraph();
		int[][] nodes = dReader.readNodes();
		
		BuildMeshTree bmTree=new BuildMeshTree();
		bmTree.buildMeshTree();
		PNode root=bmTree.getCPTree().get(1);
		KWTree kwTree = new KWTree(graph, nodes,root);
		kwTree.build();
		
	
	}
	
	
	
	public static void main(String[]  args){
		IndexEXP indexEXP = new IndexEXP();
		
//		indexEXP.test(Config.pubMedGraphTest,Config.pubMedNodeTest);
		indexEXP.test(Config.pubMedGraph50,Config.pubMedNode50);
		
	}
	
}
