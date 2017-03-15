package hku.exp;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.exp.util.QueryIdReader;
import hku.util.Log;
import hku.variant.variant1.BasicGV1;
import hku.variant.variant1.BasicWV1;
import hku.variant.variant1.SW;
import hku.variant.variant2.BasicGV2;
import hku.variant.variant2.BasicWV2;
import hku.variant.variant2.SWT;
import hku.variant.variant3.BasicGV3;
import hku.variant.variant3.BasicWV3;
import hku.variant.variant3.DecV3;
import hku.variant.variant4.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

/**
 * @author fangyixiang
 * @date Nov 3, 2015
 */
public class VariantExp {
	private String graphFile = null;
	private int graph[][] = null;
	private String nodes[][] = null;
	private TNode root = null;
	private int core[] = null;
	
	public void exp(String graphFile, String nodeFile,String dataSet){
//		expV1(graphFile, nodeFile);
//		expV2(graphFile, nodeFile);
//		expV3(graphFile, nodeFile);
		expV4(graphFile, nodeFile,dataSet);
		
	}
	
	private void expV1(String graphFile, String nodeFile){
		this.graphFile = graphFile;
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		this.graph = dataReader.readGraph();
		this.nodes = dataReader.readNode();
		
//		String queryFile = graphFile + "-query=100";
		String queryFile ="/Users/chenyankai/Desktop/yankai_data/dblp/dblp-graph-query=80" ;
		QueryIdReader qReader = new QueryIdReader();
		List<Integer> queryIdList = qReader.read(queryFile);
		
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		this.root = index.build();
		this.core = index.getCore();
		System.out.println("index construction finished !");
		
		Config.k = 6;
		
		Set<Integer> set = new HashSet<Integer>();
		for(int queryId:queryIdList){
			if(nodes[queryId].length >= 10){
				set.add(queryId);
			}
		}
		
		for(int len = 1;len <= 9;len += 2){
			//step 1: select keywords
			List<String[]> queryKwsList = new ArrayList<String[]>();
			for(int i = 0;i < queryIdList.size();i ++){
				int queryId = queryIdList.get(i);
				if(set.contains(queryId)){
					String kws[] = new String[len];
					for(int j = 0;j < len;j ++){
						kws[j] = nodes[queryId][j + 1];
					}
					queryKwsList.add(kws);
				}else{
					queryKwsList.add(null);
				}
			}
			
			//step 2: test
			singleExpV1(queryIdList, queryKwsList, len);
		}
	}
	
	private void singleExpV1(List<Integer> queryIdList, List<String[]> queryKwsList, int len){
		double q1 = 0, q2 = 0, q3 = 0;
		int count = 0;
		for(int i = 0;i < queryIdList.size();i ++){
			int queryId = queryIdList.get(i);
			String kws[] = queryKwsList.get(i);
			
			if(kws != null){
				System.out.println("i:" + i + " queryId:" + queryId); 
				count += 1;
				
				long time1 = System.nanoTime();
				BasicGV1 bg = new BasicGV1(graph, nodes);
				bg.query(queryId, kws);
				long time2 = System.nanoTime();
				q1 += time2 - time1;
				System.out.println("BasicGV1:" + q1 / 1000000 / count);
				
				long time3 = System.nanoTime();
				BasicWV1 bw = new BasicWV1(graph, nodes);
				bw.query(queryId, kws);
				long time4 = System.nanoTime();
				q2 += time4 - time3;
//				System.out.println("BasicWV1:" + q2 / 1000000 / count);
				
				long time5 = System.nanoTime();
				SW sw = new SW(graph, nodes, root, core);
				sw.query(queryId, kws);
				long time6 = System.nanoTime();
				q3 += time6 - time5;
				System.out.println("SW:" + q3 / 1000000 / count);
			}

			
			if(i == 0){
				Log.log(graphFile + " variant1 Config.k=" + Config.k + " kws-len:" + len);
			}else if((i + 1) % 100 == 0){
				Log.log(graphFile + " count:" + count
						+ " BasicGV1:" + q1 / 1000000 / count
						+ " BasicWV1:" + q2 / 1000000 / count 
						+ " SW:" + q3 / 1000000 / count);
			}
			
			if(i == queryIdList.size() - 1)   Log.log("\n");
		}
		Log.log(graphFile + " count:" + count
				+ " BasicGV1:" + q1 / 1000000 / count
				+ " BasicWV1:" + q2 / 1000000 / count 
				+ " DecV1:" + q3 / 1000000 / count + "\n");
	}
	
	private void expV2(String graphFile, String nodeFile){
		this.graphFile = graphFile;
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		this.graph = dataReader.readGraph();
		this.nodes = dataReader.readNode();
		
		String queryFile = graphFile + "-query=100";
		QueryIdReader qReader = new QueryIdReader();
		List<Integer> queryIdList = qReader.read(queryFile);
		
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		this.root = index.build();
		this.core = index.getCore();
		System.out.println("index construction finished !");
		
		Config.k = 6;
		
		for(int percentage = 20;percentage <= 100;percentage += 20){
			singleExpV2(queryIdList, percentage * 0.01);
		}
	}
	
	private void singleExpV2(List<Integer> queryIdList,  double percentage){
		double q1 = 0, q2 = 0, q3 = 0;
		int count = 0;
		for(int i = 0;i < queryIdList.size();i ++){
			int queryId = queryIdList.get(i);
			String allKws[] = new String[nodes[queryId].length - 1];
			String kws[] = null;
			if(allKws.length > 10){
				kws = new String[10];
				for(int j = 0;j < 10;j ++)   kws[j] = nodes[queryId][j + 1]; //skip username
			}else{
				kws = new String[allKws.length];
				for(int j = 0;j < allKws.length;j ++)   kws[j] = nodes[queryId][j + 1]; //skip username
			}
			
			long time1 = System.nanoTime();
			BasicGV2 bg = new BasicGV2(graph, nodes);
			bg.query(queryId, kws, percentage);
			long time2 = System.nanoTime();
			q1 += time2 - time1;
//			System.out.println("BasicGV1:" + q1 / 1000000 / count);
			
			long time3 = System.nanoTime();
			BasicWV2 bw = new BasicWV2(graph, nodes);
			bw.query(queryId, kws, percentage);
			long time4 = System.nanoTime();
			q2 += time4 - time3;
//			System.out.println("BasicWV1:" + q2 / 1000000 / count);
			
			long time5 = System.nanoTime();
			SWT swt = new SWT(graph, nodes, root, core);
			swt.query(queryId, kws, percentage);
			long time6 = System.nanoTime();
			q3 += time6 - time5;
//			System.out.println("SW:" + q3 / 1000000 / count);
			
			count += 1;
			if(i == 0){
				Log.log(graphFile + " variant2 Config.k=" + Config.k + " kws-threshold:" + percentage);
			}else if((i + 1) % 100 == 0){
				Log.log(graphFile + " count:" + count
						+ " BasicGV2:" + q1 / 1000000 / count
						+ " BasicWV2:" + q2 / 1000000 / count 
						+ " SWT:" + q3 / 1000000 / count);
			}

			if(i == queryIdList.size() - 1)   Log.log("\n");
			
			
			System.out.println("i:" + i + " queryId:" + queryId); 
		}
	}
	
	private void expV3(String graphFile, String nodeFile){
		this.graphFile = graphFile;
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		this.graph = dataReader.readGraph();
		this.nodes = dataReader.readNode();
		
		String queryFile = graphFile + "-query=100";
		QueryIdReader qReader = new QueryIdReader();
		List<Integer> queryIdList = qReader.read(queryFile);
		
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		this.root = index.build();
		this.core = index.getCore();
		System.out.println("index construction finished !");
		
		Config.k = 6;
		
		Set<Integer> set = new HashSet<Integer>();
		for(int queryId:queryIdList){
			if(nodes[queryId].length >= 10){
				set.add(queryId);
			}
		}
		
		for(int len = 1;len <= 9;len += 2){
			//step 1: select keywords
			List<String[]> queryKwsList = new ArrayList<String[]>();
			for(int i = 0;i < queryIdList.size();i ++){
				int queryId = queryIdList.get(i);
				if(set.contains(queryId)){
					String kws[] = new String[len];
					for(int j = 0;j < len;j ++){
						kws[j] = nodes[queryId][j + 1];
					}
					queryKwsList.add(kws);
				}else{
					queryKwsList.add(null);
				}
			}
			
			//step 2: test
			singleExpV3(queryIdList, queryKwsList, len);
		}
	}
	
	private void singleExpV3(List<Integer> queryIdList, List<String[]> queryKwsList, int len){
		double q1 = 0, q2 = 0, q3 = 0;
		int count = 0;
		for(int i = 0;i < queryIdList.size();i ++){
			int queryId = queryIdList.get(i);
			String kws[] = queryKwsList.get(i);
			
			if(kws != null){
				System.out.println("i:" + i + " queryId:" + queryId); 
				count += 1;
				
				long time1 = System.nanoTime();
				BasicGV3 bg = new BasicGV3(graph, nodes);
				bg.query(queryId, kws);
				long time2 = System.nanoTime();
				q1 += time2 - time1;
//				System.out.println("BasicGV3:" + q1 / 1000000 / count);
				
				long time3 = System.nanoTime();
				BasicWV3 bw = new BasicWV3(graph, nodes);
				bw.query(queryId, kws);
				long time4 = System.nanoTime();
				q2 += time4 - time3;
//				System.out.println("BasicWV3:" + q2 / 1000000 / count);
				
				long time5 = System.nanoTime();
				DecV3 dv3 = new DecV3(graph, nodes, root, core);
				dv3.query(queryId, kws);
				long time6 = System.nanoTime();
				q3 += time6 - time5;
//				System.out.println("SW:" + q3 / 1000000 / count);
			}

			
			if(i == 0){
				Log.log(graphFile + " variant3 Config.k=" + Config.k + " kws-len:" + len);
			}else if((i + 1) % 100 == 0){
				Log.log(graphFile + " count:" + count
						+ " BasicGV3:" + q1 / 1000000 / count
						+ " BasicWV3:" + q2 / 1000000 / count 
						+ " DecV3:" + q3 / 1000000 / count);
			}

			if(i == queryIdList.size() - 1)   Log.log("\n");
		}
		
		Log.log(graphFile + " count:" + count
				+ " BasicGV3:" + q1 / 1000000 / count
				+ " BasicWV3:" + q2 / 1000000 / count 
				+ " DecV3:" + q3 / 1000000 / count + "\n");
	}
	
	//**********************  Dec 13, 2016 CYK: variant4 exp 
	public void expV4(String graphFile, String nodeFile,String dataSet){
		this.graphFile = graphFile;
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		this.graph = dataReader.readGraph();
		this.nodes = dataReader.readNode();
		
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		this.root = index.build();
		this.core = index.getCore();
		System.out.println("index construction finished !");	
		
		int testFileSize=1;
		for(int i=2;i<=testFileSize+1;i++){
			String queryFile ="/Users/chenyankai/Desktop/yankai_data/variant4/"+dataSet+"-Qsize=";
			queryFile=queryFile+i+".txt";
			System.out.println(queryFile);
			QueryV3Reader qReader = new QueryV3Reader();
			List<int[]> queryIdList = qReader.readIdArr(queryFile);
			List<String[]> queryKwList=qReader.readKwSet(queryFile);
			singleExpV4(queryIdList, queryKwList);
		}
	}	
	
	private void singleExpV4(List<int[]> querySetList, List<String[]> queryKwsList){
		double q1 = 0, q2 = 0, q3 = 0;
		int count = 0;
		for(int i = 0;i < querySetList.size();i ++){
			int[] queryArr = querySetList.get(i);
			String[] kwsArr = queryKwsList.get(i);
			
			if(kwsArr != null){
				System.out.println("i:" + i ); 
				count += 1;
				
//				long time1 = System.nanoTime();
//				BasicGV3_V2 bg = new BasicGV3_V2(nodes, graph);
//				bg.query(kwsArr, queryArr);
//				long time2 = System.nanoTime();
//				q1 += time2 - time1;
//				
//				long time3 = System.nanoTime();
//				BasicWV3_V2 bw = new BasicWV3_V2(nodes, graph);
//				bw.query(queryArr, kwsArr);
//				long time4 = System.nanoTime();
//				q2 += time4 - time3;
				
				long time5 = System.nanoTime();
				SW_V2 sw_V2=new SW_V2(nodes,graph,root);
				sw_V2.query(queryArr, kwsArr);
				long time6 = System.nanoTime();
				q3 += time6 - time5;
//				sw_V2.testOutput();
			}

			if(i == 0){
				Log.log(graphFile + " variant4 Config.k=" + Config.k  );
			}else if((i + 1) % 50 == 0){
				Log.log(graphFile + " count:" + count
						+ " BasicGV4:" + q1 / 1000000 / count
						+ " BasicWV4:" + q2 / 1000000 / count 
						+ " SW_V4:" + q3 / 1000000 / count);
			}

			if(i == querySetList.size() - 1)   Log.log("\n");
		}
		
		Log.log(graphFile + " count:" + count
				+ " BasicGV4:" + q1 / 1000000 / count
				+ " BasicWV4:" + q2 / 1000000 / count 
				+ " SW_V4:" + q3 / 1000000 / count + "\n");	
	}
	
	
	public static void main(String args[]){
		//should be deleted in final test!!
		Config.qIdNum=100;
				
		VariantExp exp = new VariantExp();
//		exp.exp(Config.dblpGraph, Config.dblpNode,"dblp");
		exp.exp(Config.flickrGraph, Config.flickrNode,"flickr");
		
	}
}
