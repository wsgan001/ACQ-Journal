package EXP;


import algorithm.basic.BFS;
import config.Config;
import prep.BuildMeshTree;


public class BasicQueryEXP {

	public void test(String graphFile,String nodeFile){
		BuildMeshTree bmTree=new BuildMeshTree();
		bmTree.buildMeshTree();
		BFS bfs = new BFS(graphFile,nodeFile,bmTree.getCPTree());
//		for(int i= 0;i<400;i++){
//			System.out.println(i);
			bfs.query(13);
//		}	
		
	}
	
	public static void main(String[] args){
		BasicQueryEXP basic = new BasicQueryEXP();
		Config.k =6;
		basic.test(Config.pubMedGraph,Config.pubMedNode);
	}
	
}
