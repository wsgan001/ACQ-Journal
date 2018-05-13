package hku.exp.revision;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.query1.IncSRevisionACQSize;

/**
 * @author fangyixiang
 * @date Jun 3, 2016
 */
public class IncSRevisionACQSizeTest {

	public static void main(String[] args) {
		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		TNode root = index.build();
		int core[] = index.getCore();
		System.out.println("index construction finished !");
		
		Config.k = 4;
		IncSRevisionACQSize incs = new IncSRevisionACQSize(graph, nodes, root, core, null);
//		incs.query(15238);//jiawei han
		incs.query(152532);//jim gray
	}

}
/*
|S|:1 Size:8421.277777777777
|S|:2 Size:492.1136363636364
|S|:3 Size:52.63333333333333
|S|:4 Size:13.222222222222221
|S|:5 Size:5.0
*/