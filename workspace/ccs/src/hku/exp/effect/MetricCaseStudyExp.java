package hku.exp.effect;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.previous.SIGKDD2010;
import hku.algo.previous.SIGMOD2014;
import hku.algo.query2.Dec;
import hku.exp.sim.AMFreq;
import hku.exp.sim.APJSim;
import hku.exp.sim.AQJSim;
import hku.exp.sim.AWFreq;
import hku.exp.util.IncSNumber;
import hku.exp.util.QueryIdReader;
import hku.util.Log;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author fangyixiang
 * @date Oct 29, 2015
 */
public class MetricCaseStudyExp {
	
	public void exp(String graphFile, String nodeFile){
//		expMax(graphFile, nodeFile);
		expAll(graphFile, nodeFile);
	}
	
	//consider all the keywords
	public void expMax(String graphFile, String nodeFile){
		Config.k = 4;
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		TNode root = index.build();
		int core[] = index.getCore();
		System.out.println("index construction finished !");
		
		Config.k = 4;
		int queryId = 152532;//jim gray
//		int queryId = 15238;//jiawei han
		
		AQJSim aqj = new AQJSim(nodes);
		APJSim apj = new APJSim(nodes);
		AWFreq awf = new AWFreq(nodes);
		AMFreq adf = new AMFreq(nodes);
		
		Dec query3 = new Dec(graph, nodes, root, core, null);
		List<Set<Integer>> ccsList = query3.query(queryId);
		
		SIGKDD2010 sigkdd = new SIGKDD2010(graph);
		Set<Integer> sigkddSet = sigkdd.query(queryId);
		
		SIGMOD2014 sigmod = new SIGMOD2014(graph, core);
		Set<Integer> sigmodSet = sigmod.query(queryId);
		
		double aqj1 = aqj.singleSim(sigkddSet, queryId);
		double aqj2 = aqj.singleSim(sigmodSet, queryId);
		double aqj3 = aqj.sim(ccsList, queryId);
		
		double apj1 = apj.singleSim(sigkddSet);
		double apj2 = apj.singleSim(sigmodSet);
		double apj3 = apj.sim(ccsList);
		
		double awf1 = awf.singleSim(sigkddSet, queryId);
		double awf2 = awf.singleSim(sigmodSet, queryId);
		double awf3 = awf.sim(ccsList, queryId);
		
		double adf1 = adf.singleSim(sigkddSet, queryId);
		double adf2 = adf.singleSim(sigmodSet, queryId);
		double adf3 = adf.sim(ccsList, queryId);
		
		System.out.println("AQJ:" + aqj1 + " " + aqj2 + " " + aqj3);
		System.out.println("APJ:" + apj1 + " " + apj2 + " " + apj3);
		System.out.println("AWF:" + awf1 + " " + awf2 + " " + awf3);
		System.out.println("AMF:" + adf1 + " " + adf2 + " " + adf3);
	}
	
	//consider part of the keywords
	public void expAll(String graphFile, String nodeFile){
		Config.k = 4;
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		TNode root = index.build();
		int core[] = index.getCore();
		System.out.println("index construction finished !");
		
		String queryFile = graphFile + "-query=100";
		QueryIdReader qReader = new QueryIdReader();
		List<Integer> queryIdList = qReader.read(queryFile);
		
		int fangLen = 5;
		int count = 0;
		double cmfSum[] = new double [100];
		double cwfSum[] = new double [100];
	    double cpjSum[] = new double [100];
	    double cqjSum[] = new double [100];
		
		for(int i = 0;i < queryIdList.size();i ++){
			int queryId = queryIdList.get(i);
			if(i % 5 == 0)   System.out.println("considering " + i + "-th query vertex ...");
			
			IncSNumber query = new IncSNumber(graph, nodes, root, core, null);
			List<Map<Integer, Double>> fangList = query.query(queryId, fangLen);
			
			if(fangList != null){
				Map<Integer, Double> cmfMap = fangList.get(0);
				for(Map.Entry<Integer, Double> entry:cmfMap.entrySet()){
					cmfSum[entry.getKey()] += entry.getValue();
				}
				
				Map<Integer, Double> cwfMap = fangList.get(1);
				for(Map.Entry<Integer, Double> entry:cwfMap.entrySet()){
					cwfSum[entry.getKey()] += entry.getValue();
				}
				
				Map<Integer, Double> cpjMap = fangList.get(2);
				for(Map.Entry<Integer, Double> entry:cpjMap.entrySet()){
					cpjSum[entry.getKey()] += entry.getValue();
				}
				
				Map<Integer, Double> cqjMap = fangList.get(3);
				for(Map.Entry<Integer, Double> entry:cqjMap.entrySet()){
					cqjSum[entry.getKey()] += entry.getValue();
				}
				
				count += 1;
			}
		}
		
		for(int shareLen = 1;shareLen <= fangLen;shareLen ++){
			Log.log(graphFile + " len:" + shareLen + " cmf:" + cmfSum[shareLen] / count
					+ " cwf:" + cwfSum[shareLen] / count
					+ " cpj:" + cpjSum[shareLen] / count
					+ " cqj:" + cqjSum[shareLen] / count
					+ " count:" + count);
		}
	}
	
	public static void main(String args[]){
		MetricCaseStudyExp exp = new MetricCaseStudyExp();
		exp.exp(Config.flickrGraph, Config.flickrNode);
		exp.exp(Config.tencentGraph, Config.tencentNode);
		exp.exp(Config.dblpGraph, Config.dblpNode);
	}
}
