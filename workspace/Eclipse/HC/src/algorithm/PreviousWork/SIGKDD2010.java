package algorithm.PreviousWork;

import algorithm.DataReader;
import algorithm.FindCKCore;
import algorithm.FindKCore;
import algorithm.KCoreDecom;
import config.Config;
import java.util.*;

/**
@author chenyankai
@Date	Jul 23, 2018
We implement the greedy algorithm proposed in the SIGKDD2010 paper
*/


public class SIGKDD2010 {
	private int graph[][] = null;
	private int queryId = -1;
	
	public SIGKDD2010(int graph[][]){
		this.graph = graph;
	}
	
	public Set<Integer> query(int queryId){
		this.queryId = queryId;
		if(graph[queryId].length < Config.k)   return new HashSet<Integer>();

		//step 1: find k-core first
		FindKCore fkc = new FindKCore(graph, Config.k);
		int subCore[] = fkc.decompose();
		
		//step 2: find ck-core
		FindCKCore finder = new FindCKCore();
		Set<Integer> rsSet = finder.findCKC(graph, subCore, queryId);
		
		if(rsSet.size() > 1){
//			System.out.println("SIGKDD: we find a community with size = " + rsArray.length);
			Set<Integer> set = new HashSet<Integer>();
			for(int id:rsSet)   set.add(id);
			return set;
		}else{
			return null;
		}
	}
	
	public static void main(String[] args) {
//		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
//		int graph[][] = dataReader.readGraph();
//		int[][] nodes = dataReader.readNodes();
//		
//		SIGKDD2010 sigkdd = new SIGKDD2010(graph);
//		sigkdd.query(246688);
	}
	
	
}
