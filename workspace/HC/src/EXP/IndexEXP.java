package EXP;

import algorithm.CPTreeReader;
import algorithm.DataReader;
import algorithm.ProfiledTree.PNode;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.KTree;
import config.Config;
import prep.*;
import prep.PubMedPrep.BuildMeshTree;
import prep.PubMedPrep.ConfigPubmed;

public class IndexEXP {

	

	
	
	public void test(String graphFile,String nodeFile,String cptreeFile){
		DataReader dReader = new DataReader(graphFile, nodeFile);
		int[][] graph = dReader.readGraph();
		int[][] nodes = dReader.readNodes();
		CPTreeReader cpReader = new CPTreeReader();
		PNode root = cpReader.loadCPtreeRoot(cptreeFile);
		KWTree kwTree = new KWTree(graph, nodes,root);
		kwTree.build();
	}
	
	
	
	public static void main(String[]  args){
		IndexEXP indexEXP = new IndexEXP();
		
//		indexEXP.test(Config.pubMedGraphTest,Config.pubMedNodeTest);
		indexEXP.test(Config.pubMedGraph120,Config.pubMedNode120,Config.pubmedCPtree120);
		
	}
	
}
