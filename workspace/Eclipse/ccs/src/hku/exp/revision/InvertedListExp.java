package hku.exp.revision;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.query1.IncSRevision;
import hku.algo.query1.IncTRevision;
import hku.exp.util.QueryIdReader;
import hku.util.Log;

import java.util.List;

/**
 * @author fangyixiang
 * @date May 16, 2016
 */
public class InvertedListExp {
	
	public void exp(String graphFile, String nodeFile){
		Config.k = 4;
		singleExp(graphFile, nodeFile, graphFile + "-query=100");
		
		Config.k = 5;
		singleExp(graphFile, nodeFile, graphFile + "-query=100");
		
		Config.k = 6;
		singleExp(graphFile, nodeFile, graphFile + "-query=100");
		
		Config.k = 7;
		singleExp(graphFile, nodeFile, graphFile + "-query=100");
		
		Config.k = 8;
		singleExp(graphFile, nodeFile, graphFile + "-query=100");
	}
	
	private void singleExp(String graphFile, String nodeFile, String queryFile){
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		TNode root = index.build();
		int core[] = index.getCore();
		System.out.println("index construction finished !");
		
		QueryIdReader qReader = new QueryIdReader();
		List<Integer> queryIdList = qReader.read(queryFile);
		
		double q1 = 0, q2 = 0;
		int count = 0;
		for(int queryId:queryIdList){
			long time1 = System.nanoTime();
			IncSRevision query1 = new IncSRevision(graph, nodes, root, core, null);
			int size1 = query1.query(queryId);
			long time2 = System.nanoTime();
			q1 += time2 - time1;
			
			time1 = System.nanoTime();
			IncTRevision query2 = new IncTRevision(graph, nodes, root, core, null);
			int size2 = query2.query(queryId);
			time2 = System.nanoTime();
			q2 += time2 - time1;
			
			count += 1;
			if(count == 1){
				Log.log(graphFile + " Config.k=" + Config.k);
			}else if(count % 100 == 0){
				Log.log("count:" + count
						+ " IncSRevision:" + q1 / 1000000 / count
						+ " IncTRevision:" + q2 / 1000000 / count);
				if(count == queryIdList.size())   Log.log("\n");
			}
		}
	}
	
	public static void main(String[] args) {
		InvertedListExp ive = new InvertedListExp();
		ive.exp(Config.flickrGraph, Config.flickrNode);
		ive.exp(Config.dblpGraph, Config.dblpNode);
		ive.exp(Config.tencentGraph, Config.tencentNode);
		ive.exp(Config.dbpediaGraph, Config.dbpediaNode);
	}

}
