package hku.algo.query2;

import java.util.*;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.KCore;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.query1.IncS;
import hku.algo.query1.IncT;
import hku.exp.util.QueryIdReader;
import hku.util.Log;

public class T {

	public static void main(String[] args) {
		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		TNode root = index.build();
		int core[] = index.getCore();
		System.out.println("index construction finished !");
		
		String node[] = {"data", "mine"};
		nodes[15238] = node;
		
		Config.k = 4;
		DecShare dec = new DecShare(graph, nodes, root, core, null);
		dec.query(15238);
	}
}
