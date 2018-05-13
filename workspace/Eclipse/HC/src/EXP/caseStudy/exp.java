package EXP.caseStudy;

import java.util.HashSet;
import java.util.Set;

import algorithm.CPTreeReader;
import algorithm.FindCKSubG;
import algorithm.ProfiledTree.PNode;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.Query1_margin.Query1;
import config.Config;

public class exp {

	private String graphFile = Config.acmccsDataWorkSpace+"edges.txt";
	private String nodesFile = Config.acmccsDataWorkSpace+"nodes.txt";
	private String ccsFile = Config.acmccsDataWorkSpace+"CPTree.txt";
	
	public void run(){
		CPTreeReader cpReader = new CPTreeReader(ccsFile);
		PNode root=cpReader.loadCPtreeRoot();
		
		KWTree kwTree1 = new KWTree(graphFile,nodesFile,root);
//		kwTree1.test();
		kwTree1.build();
		
				
//		kwTree1.printTree(Config.acmccsDataWorkSpace+"indexFile.txt");
				
		
//		Config.k = 9;
//		Query1 query1 = new Query1(kwTree1.graph,kwTree1.getHeadList());
//		query1.query(20);
	
	}
	
	public void controlItem(int[] list,int queryId){
		CPTreeReader cpReader = new CPTreeReader(ccsFile);
		PNode root=cpReader.loadCPtreeRoot();
		
		KWTree kwTree1 = new KWTree(graphFile,nodesFile,root);
		kwTree1.build();
		Set<Integer> users = new HashSet<Integer>();
		for(int item:list){
			 users.addAll(kwTree1. getKcoreofitems(item, Config.k, queryId));
		}
		
		FindCKSubG findCKSG=new FindCKSubG(kwTree1.graph, users, queryId);
		users =findCKSG.findCKSG();
		System.out.println(users.size());
	}
	
	
	public static void main(String[] args){
		exp exp = new exp();
//		exp.run();
		Config.k = 8; 
		int[] list = {511,597,1181}; 
		exp.controlItem(list, 113);
	}
	
}
