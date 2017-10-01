package EXP;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algorithm.CPTreeReader;
import algorithm.ProfiledTree.PNode;
import algorithm.basic.DFS;
import algorithm.basic.BFS;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.Query1_margin.Query1;
import algorithm.kwIndex.Query2.Query2_Inc;
import algorithm.kwIndex.Query2.query2_MP;
import algorithm.simpleKWIndex.simKWTree;
import algorithm.simpleKWIndex.query1.query1;
import config.Config;


public class IndexBasedQueryEXP {
	
	public IndexBasedQueryEXP(){
		
	}
	
	private List<Integer> readQueryFile(String queryFile){
		List<Integer> queryList = new ArrayList<Integer>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(queryFile));
			String line = null;
			while((line=bReader.readLine())!=null){
				queryList.add(Integer.parseInt(line));
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		
		return queryList;
	}
	
	public void exp(String graphFile,String nodeFile,String CPtreeFile,String queryFile){
		
		CPTreeReader cpReader = new CPTreeReader();
		PNode root=cpReader.loadCPtreeRoot(CPtreeFile);
		
		KWTree kwTree1 = new KWTree(graphFile,nodeFile,root);
		kwTree1.build();
		Query1 query1 = new Query1(kwTree1.graph,kwTree1.getHeadList());
		Query2_Inc query2_Inc= new Query2_Inc(kwTree1.graph, kwTree1.getHeadList());

		kwTree1=null;
		
//		simKWTree kwTree2 = new simKWTree(graphFile, nodeFile, root);
//		kwTree2.build();
//		query1 simQuery2 = new query1(kwTree2.graph,kwTree2.getHeadList());
//		kwTree2=null;
		
		
		BFS bfs= new BFS(graphFile, nodeFile,cpReader.loadCPTree(CPtreeFile));
		long time1 = 0;
		long time2 = 0; 
//		
		List<Integer> queryList = readQueryFile(queryFile);
//		for(int x:queryList){
//			System.out.println("now query: "+x);
//			long time11 = System.nanoTime();
//			query1.query(x);
//			time1+=System.nanoTime()-time11;
//			
//			long time21=System.nanoTime();
////			simQuery2.query(x);
////			bfs.query(x);
////			query2_Inc.query(x);
//			time2+=System.nanoTime()-time21;
//		}
//		long time11 = System.nanoTime();
//		query1.query(22901); // 22901 is good example for comparsion with ACQ:
//		time1+=System.nanoTime()-time11;
//		
		System.out.println("hard index time1: "+time1/1000000+" simple index time2: "+time2/1000000+"time gap: "+(double)time2/time1 );
		
		
//		query2_MP.query(22901);
//		query2_MP.print();
		
//		subKwTree size 16
//		query.query(5473); 
		
		
//		subkwtree size 39
//		long time = System.nanoTime();
//		query1.query(5964); 
//		long time1= System.nanoTime()-time; 
//			
//		long time2 = System.nanoTime();
//		query2.query(5964);
//		long time3= System.nanoTime()-time2; 
//		System.out.println("simple Index: "+time3/1000000 +" hard index: "+time1/1000000);
//		
//		long time2 = System.nanoTime();
//		query1.query(37899);
//		bfs.query(37899);
//		long time3= System.nanoTime()-time2; 
//		System.out.println(time3/time1);
		
		
//		DFS dfs= new DFS(graphFile, nodeFile,cpReader.loadCPTree(CPtreeFile));
//		long time4 = System.nanoTime();
//		dfs.query(5964);
//		long time5 = System.nanoTime()-time4;
//		System.out.println(time5/time1);
		
		
		
		
//		query.query(933);
 
		

		
	}
	
	public static void main(String[] args){
		IndexBasedQueryEXP exp = new IndexBasedQueryEXP();
		exp.exp(Config.pubMedGraph10,Config.pubMedNode10,Config.pubmedCPtree10,Config.pubMedQueryFile);
	}
	
	
}
