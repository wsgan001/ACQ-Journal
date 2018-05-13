package hku.exp.revision;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.query1.IncSRevisionACQSize;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author fangyixiang
 * @date May 18, 2016
 */
public class ACQSize {

	public static void expAuthor(int graph[][], String nodes[][], TNode root, int core[], int queryId){
		IncSRevisionACQSize query = new IncSRevisionACQSize(graph, nodes, root, core, null);
		query.query(queryId);
	}
	
	private static String[] findKws(String nodes[][], int queryId, int len){
		Set<String> set = new HashSet<String>();
		while(set.size() < len){
			Random random = new Random();
			int rand = random.nextInt(len);
			String word = nodes[queryId][rand];
			set.add(word);
		}
		
		String kws[] = new String[len];
		int i = 0;
		for(String word:set){
			kws[i] = word;
			i += 1;
		}
		
		return kws;
	}
	
	public static void main(String[] args) {
		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		
		AdvancedIndex index = new AdvancedIndex(graph, nodes);
		TNode root = index.build();
		int core[] = index.getCore();
		System.out.println("index construction finished !");
		
		Config.k = 4;
//		expAuthor(graph, nodes, root, core, 15238);//jiawei han
		expAuthor(graph, nodes, root, core, 152532);//jim gray
	}

}
