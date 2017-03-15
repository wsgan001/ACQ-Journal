package hku.util;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.KCore;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author fangyixiang
 * @date Jul 27, 2015
 */
public class KwChecker {
	private String nodes[][] = null;
	private int graph[][] = null;
	private int core[] = null;
	private int queryId = -1;
	private CCSSaver saver = null;
	
	public KwChecker(String nodeFile, String graphFile, String ccsFile) {
		DataReader dataReader = new DataReader(nodeFile, graphFile);
		nodes = dataReader.readNode();
		graph = dataReader.readGraph();
	
		//compute k-core
		KCore kcore = new KCore(graph);
		core = kcore.decompose();
		System.out.println("k-core decomposition finished.");
		
		//CCS saver
		saver = new CCSSaver(ccsFile);
	}

	public void query(int queryId) {
		this.queryId = queryId;
		
		// step 1: find the connected k-core containing nodeId
		int cKCoreNode[] = findCKCore(graph, core, queryId);
		if(core[queryId] < Config.k){
			System.out.println("No answer! Cannot find k-core");
			return ;
		}
		
		int count = 0;
		for(int i = 0;i < cKCoreNode.length;i ++){
			int nodeId = cKCoreNode[i];
			boolean rs1 = isContained(nodes[nodeId], "heterogeneous");
			boolean rs2 = isContained(nodes[nodeId], "network");
			
			if(rs1 && rs2){
				count += 1;
			}
		}
		System.out.println("count:" + count);
	}

	private int[] findCKCore(int graph[][], int core[], int qId) {
		//step 1: initialize
		int arrNum = graph.length; //arrNum = nodeNum + 1; 
		boolean visit[] = new boolean[arrNum];
		visit[qId] = true;
		Queue<Integer> queue = new LinkedList<Integer>(); 
		for(int i = 0;i < graph[qId].length;i ++){
			int neighbor = graph[qId][i];
			if(core[neighbor] >= Config.k){
				queue.add(neighbor);
			}
		}
		
		//step 2: search
		while(queue.size() > 0){
			int current = queue.poll();
			visit[current] = true;
			for(int i = 0;i < graph[current].length;i ++){
				int neighbor = graph[current][i];
				if(visit[neighbor] == false && core[neighbor] >= Config.k){
					queue.add(neighbor);
				}
			}
		}
		
		//count the number of nodes in the k-core
		int count = 0;
		for(int i = 0;i < visit.length;i ++)   if(visit[i])   count ++;
		
		//put all the nodes in an array
		int index = 0;
		int rsNode[] = new int[count];
		for(int i = 1;i < visit.length;i ++){
			if(visit[i]){
				rsNode[index] = i;
				index += 1;
			}
		}
		
		return rsNode;
	}
	
	private boolean isContained(String s[], String str){
		boolean rs = false;
		for(int i = 1;i < s.length;i ++){
			if(s[i].equals(str)){
				rs = true;
				break;
			}
		}
		return rs;
	}
	
	public static void main(String[] args) {
		KwChecker checker = new KwChecker(Config.caseGraph, Config.caseNode, Config.caseCCS);
		checker.query(7305);
	}

}
