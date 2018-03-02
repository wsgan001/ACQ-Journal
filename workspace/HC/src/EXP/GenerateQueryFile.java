package EXP;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algorithm.CPTreeReader;
import algorithm.DataReader;
import algorithm.DecomposeKCore;
import algorithm.ProfiledTree.PNode;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.Query1_margin.Query1;
import config.Config;

public class GenerateQueryFile {

	private int querySize = 100;

	
	private KWTree buildKWTree(String CPtreeFile, String graphFile, String nodeFile){
		CPTreeReader cpReader = new CPTreeReader(CPtreeFile);
		PNode root=cpReader.loadCPtreeRoot();
		KWTree kwTree = new KWTree(graphFile, nodeFile, root);
		kwTree.build();
		
		return kwTree;
	}
	
	//this function generate query vertices which has communities 
	//for checking diversity part

	public void generateForDiversity(int k,KWTree kwTree,String dataSpace){
		String queryFile =dataSpace+"queryDiversityFile"+k+".txt";
		DecomposeKCore decomposeKCore = new DecomposeKCore(kwTree.graph);
		int core[] = decomposeKCore.decompose();
		Query1 query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		int idx=0;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(queryFile));
			while(idx<querySize){
				Random random = new Random();
				int queryId = random.nextInt(kwTree.graph.length-2)+1;
				boolean isGood = query1.generateQuery(queryId,4,25);
				if(isGood){
					if(core[queryId]>=k){
						idx++;
						writer.write(queryId+"");
						writer.newLine();
						}
				}
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString()+"**************");
		}
	}
	
	
	//this function generates random query vertices for checking the efficiency 
	public void generateForEfficiency(int k,KWTree kwTree,String dataSpace){
		String queryFile =dataSpace+"queryEfficiencyFile"+k+".txt";
		
		Query1 query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		int idx=0;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(queryFile));
			while(idx<querySize){
				Random random = new Random();
				int queryId = random.nextInt(kwTree.graph.length-2)+1;
				boolean isGood = query1.generateQuery(queryId,0,30);
				if(isGood){
						idx++;
						writer.write(queryId+"");
						writer.newLine();
				}
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString()+"**************");
		}
	}
	
	
		
	public void generateQueryRandom(String graphFile,String nodeFile,String outFile,int size,int lowBound,int upBound){
		DataReader reader = new DataReader(graphFile,nodeFile);
		int[][] nodes = reader.readNodes();
		
		List<Integer> list = new ArrayList<Integer>();
		Random random = new Random();
		while(list.size()<size){
			int q = random.nextInt(nodes.length-1)+1;
			if(nodes[q].length> lowBound && nodes[q].length<upBound) {
				list.add(q);
			}
		}
		
		try{
			BufferedWriter stdOut = new BufferedWriter(new FileWriter(outFile));
			for(int q:list){
				stdOut.write(q+"");
				stdOut.newLine();
			}
			stdOut.flush();
			stdOut.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
		
	
	
	
	
	
	
	
	
	
	public static void main(String[] args){
		GenerateQueryFile run = new GenerateQueryFile();
//		int k = 6;
//		Config.k=k;
//		run.generateQueryRandom(Config.ACMDLGraph, Config.ACMDLNode, Config.acmccsDataWorkSpace+"query100.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.ACMDLGraph, Config.ACMDLNode, Config.acmccsDataWorkSpace+"query2.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.ACMDLGraph, Config.ACMDLNode, Config.acmccsDataWorkSpace+"query3.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.ACMDLGraph, Config.ACMDLNode, Config.acmccsDataWorkSpace+"query4.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.ACMDLGraph, Config.ACMDLNode, Config.acmccsDataWorkSpace+"query5.txt", 100, 0, 40);

		
//		run.generateQueryRandom(Config.pubMedGraph, Config.pubMedNode, Config.pubMedDataWorkSpace+"query100.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.pubMedGraph, Config.pubMedNode, Config.pubMedDataWorkSpace+"query2.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.pubMedGraph, Config.pubMedNode, Config.pubMedDataWorkSpace+"query3.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.pubMedGraph, Config.pubMedNode, Config.pubMedDataWorkSpace+"query4.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.pubMedGraph, Config.pubMedNode, Config.pubMedDataWorkSpace+"query5.txt", 100, 0, 40);

		
//		run.generateQueryRandom(Config.dblpGraph, Config.dblpNode1, Config.DBLPDataWorkSpace+"query100.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.dblpGraph, Config.dblpNode1, Config.DBLPDataWorkSpace+"query2.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.dblpGraph, Config.dblpNode1, Config.DBLPDataWorkSpace+"query3.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.dblpGraph, Config.dblpNode1, Config.DBLPDataWorkSpace+"query4.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.dblpGraph, Config.dblpNode1, Config.DBLPDataWorkSpace+"query5.txt", 100, 0, 40);

		
//		run.generateQueryRandom(Config.FlickrGraph, Config.FlickrNode1, Config.FlickrDataWorkSpace+"query100.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.FlickrGraph, Config.FlickrNode1, Config.FlickrDataWorkSpace+"query2.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.FlickrGraph, Config.FlickrNode1, Config.FlickrDataWorkSpace+"query3.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.FlickrGraph, Config.FlickrNode1, Config.FlickrDataWorkSpace+"query4.txt", 100, 0, 40);
//		run.generateQueryRandom(Config.FlickrGraph, Config.FlickrNode1, Config.FlickrDataWorkSpace+"query5.txt", 100, 0, 40);

//		run.generateQueryRandom(Config.dblpGraph+"-20", Config.dblpNode1+"-20", Config.DBLPDataWorkSpace+"query100-20.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.dblpGraph+"-40", Config.dblpNode1+"-40", Config.DBLPDataWorkSpace+"query100-40.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.dblpGraph+"-60", Config.dblpNode1+"-60", Config.DBLPDataWorkSpace+"query100-60.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.dblpGraph+"-80", Config.dblpNode1+"-80", Config.DBLPDataWorkSpace+"query100-80.txt", 100, 0, 30);

//		for(int i=5;i<9;i++){
//			Config.k= i;
//			run.generateQueryRandom(Config.dblpGraph, Config.dblpNode1, Config.DBLPDataWorkSpace+"query1000"+i, 1000, 0, 30);
//			run.generateQueryRandom(Config.ACMDLGraph, Config.ACMDLNode, Config.acmccsDataWorkSpace+"query1000"+i, 1000, 0, 30);
//
//		}

		
//		run.generateQueryRandom(Config.ACMDLGraph+"-20", Config.ACMDLNode+"-20", Config.acmccsDataWorkSpace+"query100-20.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.ACMDLGraph+"-40", Config.ACMDLNode+"-40", Config.acmccsDataWorkSpace+"query100-40.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.ACMDLGraph+"-60", Config.ACMDLNode+"-60", Config.acmccsDataWorkSpace+"query100-60.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.ACMDLGraph+"-80", Config.ACMDLNode+"-80", Config.acmccsDataWorkSpace+"query100-80.txt", 100, 0, 30);

//		run.generateQueryRandom(Config.pubMedGraph+"-20", Config.pubMedNode+"-20", Config.pubMedDataWorkSpace+"query100-20.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.pubMedGraph+"-40", Config.pubMedNode+"-40", Config.pubMedDataWorkSpace+"query100-40.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.pubMedGraph+"-60", Config.pubMedNode+"-60", Config.pubMedDataWorkSpace+"query100-60.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.pubMedGraph+"-80", Config.pubMedNode+"-80", Config.pubMedDataWorkSpace+"query100-80.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.pubMedGraph, Config.pubMedNode, Config.pubMedDataWorkSpace+"query100-100.txt", 100, 0, 30);

		
//		run.generateQueryRandom(Config.FlickrGraph+"-20", Config.FlickrNode1+"-20", Config.FlickrDataWorkSpace+"query100-20.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.FlickrGraph+"-40", Config.FlickrNode1+"-40", Config.FlickrDataWorkSpace+"query100-40.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.FlickrGraph+"-60", Config.FlickrNode1+"-60", Config.FlickrDataWorkSpace+"query100-60.txt", 100, 0, 30);
//		run.generateQueryRandom(Config.pubMedGraph+"-80", Config.pubMedNode+"-80", Config.pubMedDataWorkSpace+"query100-80.txt", 100, 0, 30);

		

		
		
//		KWTree kwTree = run.buildKWTree(Config.pubmedCPtree, Config.pubMedGraph, Config.pubMedNode);
//		for(int k=4;k<=8;k++){
//			Config.k = k;
//////		run.generateForDiversity(k,kwTree,Config.pubMedDataWorkSpace);
//			run.generateForEfficiency(k, kwTree, Config.pubMedDataWorkSpace);
//		}
////		
//////
//		kwTree = run.buildKWTree(Config.DBLPCPTree, Config.dblpGraph, Config.dblpNode1);
//		for(int k=4;k<=8;k++){
//			Config.k = k; 
//////		run.generateForDiversity(k,kwTree,Config.DBLPDataWorkSpace);
//			run.generateForEfficiency(k, kwTree, Config.DBLPDataWorkSpace);
//		}
//		
//		
//		kwTree = run.buildKWTree(Config.FlickrCPTree, Config.FlickrGraph, Config.FlickrNode1);
//		for(int k=4;k<=8;k++){
//			Config.k = k;
////			run.generateForDiv/ersity(k,kwTree,Config.FlickrDataWorkSpace);
//			run.generateForEfficiency(k, kwTree, Config.FlickrDataWorkSpace);
//		}
//		
//		
//		kwTree = run.buildKWTree(Config.ACMDLCPtree, Config.ACMDLGraph, Config.ACMDLNode);
//		for(int k=4;k<=8;k++){
//			Config.k=k;
//////	run.generateForDiversity(k,kwTree,Config.acmccsDataWorkSpace);
//		run.generateForEfficiency(k, kwTree, Config.acmccsDataWorkSpace);
//
//		}
		
		
			
//			

		
	}
	
	
}
