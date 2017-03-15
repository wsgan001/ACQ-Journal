package hku.algo.index.edgeUpdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.util.Log;

public class ExpInsertion {
	private int[][] graph=null;
	private String[][] node=null;
	private String inPath="/Users/chenyankai/Desktop/yankai_data/insertEdge/";
	
	
	private boolean edgeExist(int u,int v, int[][]graph){
		for(int neighbor:graph[u]){
			if(neighbor==v) return true;
		}
		return false;
	}
	private void checkEdge(int[] core,int u,int v){
		if(!edgeExist(u, v, graph)){
			System.out.println(core[u]+"   "+core[v]);
		}else{
			System.out.println("edge exists!");
		}
	}
	
	
	
	private double readAndTest(String dataSet,int coreNumber){
		double rebuildTime=0.0;
		double insertTime=0.0;
		try {
			String path=inPath+dataSet+"-"+coreNumber+".txt";
			File file=new File(path);
			BufferedReader bReader=new BufferedReader(new FileReader(file));
			String line=null;
			int i=1;
			while((line=bReader.readLine())!=null){
				String[] s=line.split(",");
				int u=Integer.parseInt( s[0] );
				int v=Integer.parseInt( s[1] );
				System.out.println("第 "+(i++)+" 次测试: "+u+ "  " +v);
			
				AdvancedIndex ac1=new AdvancedIndex(graph, node);
				TNode root=ac1.build();
				EdgeInsertion eInsertion=new EdgeInsertion(root, ac1.getInvert(), graph, node, ac1.getCore());
				long time1=System.nanoTime();
				eInsertion.insertEdge(u, v);
				long time2=System.nanoTime();
				insertTime+=time2-time1;
				
				long time3=System.nanoTime();
				eInsertion.rebuild();
				long time4=System.nanoTime();
				rebuildTime+=time4-time3;
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		double gap=rebuildTime/insertTime;
		return gap;
	}
	
	public void experiment(String dataSet,String graphFile, String nodeFile,int coreNumber){
		
		DataReader d1DataReader=new DataReader(graphFile , nodeFile); 
		int graph1[][]=d1DataReader.readGraph();
		String nodes1[][]=d1DataReader.readNode(); 
		this.graph=graph1;
		this.node=nodes1;
		double gap=readAndTest(dataSet, coreNumber);
		Log.log(dataSet+" insert:  Core number of edges: "+coreNumber+".  Time gap of the test: "+gap +" times");	
	}
	
	public static void main(String[] args){
		ExpInsertion eInsertion=new ExpInsertion();
		//dblp
//		int[] fileList={5,10,15,20,25};
//		for(int x:fileList) eInsertion.experiment("dblp",Config.dblpGraph, Config.dblpNode, x);
//		
//		//flickr
//		int[] fileList1={5,10,15,20,25};
//		for(int x:fileList1) eInsertion.experiment("flickr",Config.flickrGraph, Config.flickrNode, x);
//		
//		//tecent
//		int[] fileList2={5,10,15,20,25};
//		for(int x:fileList2) eInsertion.experiment("tecent",Config.tencentGraph, Config.tencentNode, x);
//		
//		//dbpedia 
//		int[] fileList3={5,10,15,20,25};
//		for(int x:fileList3) eInsertion.experiment("dbpedia",Config.dbpediaGraph, Config.dbpediaNode, x);
		
		eInsertion.experiment(dataSet, graphFile, nodeFile, coreNumber);
		
	}
	
}
