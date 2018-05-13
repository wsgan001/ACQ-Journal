package algorithm.simpleKWIndex;

import algorithm.CPTreeReader;
import algorithm.ProfiledTree.PNode;
import config.Config;

public class EXP {

	public void exp(String graphFile,String nodeFile,String CPtreeFile){
		CPTreeReader cpReader = new CPTreeReader(CPtreeFile);
		PNode root=cpReader.loadCPtreeRoot();
		simKWTree kwTree = new simKWTree(graphFile, nodeFile, root);
		long time= System.nanoTime();
		
		kwTree.build();
		System.out.println((System.nanoTime()-time)/1000000);
	}
	
	
	public static void main(String[] args){
		EXP exp= new EXP();
//		exp.exp(Config.pubMedGraph100,Config.pubMedNode100,Config.pubmedCPtree100);
		
	}
	
}
