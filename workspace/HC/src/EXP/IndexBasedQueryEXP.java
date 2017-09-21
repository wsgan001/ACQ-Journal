package EXP;


import algorithm.CPTreeReader;
import algorithm.ProfiledTree.PNode;
import algorithm.basic.DFS;
import algorithm.basic.BFS;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.Query1.Query1_backup;
import algorithm.kwIndex.Query1.Query1;
import config.Config;


public class IndexBasedQueryEXP {
	
	public IndexBasedQueryEXP(){
		
	}
	
	public void exp(String graphFile,String nodeFile,String CPtreeFile){
		
		CPTreeReader cpReader = new CPTreeReader();
		PNode root=cpReader.loadCPtreeRoot(CPtreeFile);
		
		
		KWTree kwTree = new KWTree(graphFile,nodeFile,root);
		kwTree.build();
		Query1 query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		kwTree=null;
		
//		while(true){
//			Random random = new Random();
//			int queryID = random.nextInt(80000);
//			System.out.println("now query: "+queryID);
////			query2.induceKWTree();
////			if(query2.getSubkwtreeSize()<30){
//				query2.query(queryID);
////			}
//		}
		
//		 subKwTree size 76
//		query.query(11427);
		
//		subKwTree size 16
//		query.query(5473); 
		
		
//		subkwtree size 39
		long time = System.nanoTime();
		query1.query(5964); 
		long time1= System.nanoTime()-time; 
		System.out.println(time1/1000000 );
//		
		BFS bfs= new BFS(graphFile, nodeFile,cpReader.loadCPTree(CPtreeFile));
		long time2 = System.nanoTime();
		bfs.query(5964);
		long time3= System.nanoTime()-time2; 
		System.out.println(time3/time1);
//		
		
//		
//		long time3 = System.nanoTime()-time2;
//		System.out.println(time3/1000000);
//		System.out.println(time3/time22);
		
		
//		DFS dfs= new DFS(graphFile, nodeFile,cpReader.loadCPTree(CPtreeFile));
//		long time4 = System.nanoTime();
//		dfs.query(5964);
//////		bfs.print();
//		long time5 = System.nanoTime()-time4;
//		System.out.println(time5/time1);
		
		
		
		
//		query.query(933);
 
		

		
	}
	
	public static void main(String[] args){
		IndexBasedQueryEXP exp = new IndexBasedQueryEXP();
		exp.exp(Config.pubMedGraph10,Config.pubMedNode10,Config.pubmedCPtree10);
	}
	
	
}
