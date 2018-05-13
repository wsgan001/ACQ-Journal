package hku.exp.revision;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.query2.DecRevision;
import hku.exp.util.QueryIdReader;
import hku.util.Log;

import java.util.List;

/**
 * @author fangyixiang
 * @date May 16, 2016
 */
public class LocalDecExp {
	
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
		
		double q1 = 0;
		int count = 0;
		for(int queryId:queryIdList){
			long time1 = System.nanoTime();
			DecRevision query1 = new DecRevision(graph, nodes, root, core, null);
			query1.query(queryId);
			long time2 = System.nanoTime();
			q1 += time2 - time1;
			
			count += 1;
			if(count == 1){
				Log.log(graphFile + " Config.k=" + Config.k);
			}else if(count % 100 == 0){
				Log.log("count:" + count + " DecRevision:" + q1 / 1000000 /count);
				if(count == queryIdList.size())   Log.log("\n");
			}
		}
	}
	
	public static void main(String[] args) {
		LocalDecExp lde = new LocalDecExp();
		lde.exp(Config.flickrGraph, Config.flickrNode);
		lde.exp(Config.dblpGraph, Config.dblpNode);
		lde.exp(Config.tencentGraph, Config.tencentNode);
		lde.exp(Config.dbpediaGraph, Config.dbpediaNode);
	}

}
