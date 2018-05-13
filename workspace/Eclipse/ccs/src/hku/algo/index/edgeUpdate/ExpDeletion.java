package hku.algo.index.edgeUpdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.util.Log;

public class ExpDeletion {
	private int graph[][]=null;
	private String node[][]=null;
	private TNode[] invert=null;
	private TNode root=null;
	private int[] core=null;
	private String inPath="/Users/chenyankai/Desktop/yankai_data/deleteEdge/";

	

	private boolean compare(TNode[] invert1, TNode[] invert2){
		for(int i=1;i<invert1.length;i++) {
			if(invert1[i].getNodeSet().size()!=invert2[i].getNodeSet().size() || invert1[i].getChildList().size()!=invert2[i].getChildList().size()){
				return false;
				}
		}
		return true;
	}


	
	private double readAndTest(String dataSet,int coreNumber){
		double rebuildTime=0.0;
		double insertTime=0.0;
		int i=1;
		try{
			String path=inPath+dataSet+"-"+coreNumber+".txt";
			File file=new File(path);
			BufferedReader bReader=new BufferedReader(new FileReader(file));
			String line=null;
			long time3=System.nanoTime();
			AdvancedIndex ac1=new AdvancedIndex(graph, node);
			root=ac1.build();
			invert=ac1.getInvert();
			core=ac1.getCore();
			long time4=System.nanoTime();
			rebuildTime=time4-time3;
			while((line=bReader.readLine())!=null){
				String[] s=line.split(",");
				int u=Integer.parseInt( s[0] );
				int v=Integer.parseInt( s[1] );
				System.out.println("第 "+(i++)+" 次测试: "+u+ "  " +v);
				
				EdgeDeletion edgeDeletion=new EdgeDeletion(root, invert, graph, node,core);
				long time1=System.nanoTime();
				edgeDeletion.deleteEdge(u, v);
				long time2=System.nanoTime();
				insertTime+=time2-time1;
				invert=edgeDeletion.travseT(root);
				
//				edgeDeletion.rebuild();
				
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		double gap=rebuildTime*i/insertTime;
		return gap;
	}
	
	
	
	public void experiment(String dataSet,String graphFile, String nodeFile,int coreNumber){
	
		DataReader d1DataReader=new DataReader(graphFile , nodeFile); 
		int graph1[][]=d1DataReader.readGraph();
		String nodes1[][]=d1DataReader.readNode(); 
		this.graph=graph1;
		this.node=nodes1;
		double gap=readAndTest(dataSet, coreNumber);
		Log.log(dataSet+" delete:  Core number of edges: "+coreNumber+".  Time gap of the test: "+gap +" times");
	}

	 public static void main(String[] args){
		 ExpDeletion deletionTest=new ExpDeletion();
		 int[] fileList1={5,10,15,20,15};
		 for(int x:fileList1) deletionTest.experiment("dblp",Config.dblpGraph, Config.dblpNode, x);
	
		 int[] fileList2={5,10,15,20,15};
		 for(int x:fileList2) deletionTest.experiment("flickr",Config.flickrGraph, Config.flickrNode, x);

		 //		 deletionTest.experiment(Config.dblpGraph, Config.dblpNode, 20);
//		 deletionTest.experiment(Config.dblpGraph, Config.dblpNode, 25);
	}	 
}
