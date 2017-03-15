package hku.exp;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.index.AdvancedIndex;

/**
 * @author fangyixiang
 * @date Oct 8, 2015
 * Obtain the statistic of each dataset
 */

public class FillTableExp {

	public static void main(String[] args) {
//		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
//		DataReader dataReader = new DataReader(Config.twitterGraph, Config.twitterNode);
		DataReader dataReader = new DataReader(Config.tencentGraph, Config.tencentNode);
//		DataReader dataReader = new DataReader(Config.dbpediaGraph, Config.dbpediaNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		index.build();
	}

}
