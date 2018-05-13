package hku.algo.query1;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;

import java.util.HashSet;
import java.util.Set;

/**
 * @author fangyixiang
 * @date Jun 2, 2016
 */
public class T {
	
	public static void main(String[] args) {
		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();

		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		TNode root = index.build();
		int core[] = index.getCore();
		
		Config.k = 4;
		IncSRevision incsr = new IncSRevision(graph, nodes, root, core, null);
		incsr.query(16720);
	}
}