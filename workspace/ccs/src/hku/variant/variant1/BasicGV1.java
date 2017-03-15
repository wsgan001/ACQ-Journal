package hku.variant.variant1;

import java.util.*;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.FindCCS;
import hku.algo.FindCKCore;
import hku.algo.KCore;

/**
 * @author fangyixiang
 * @date Nov 3, 2015
 * basic-g for variant1
 */
public class BasicGV1 {
	private String nodes[][] = null;
	private int graph[][] = null;
	private int core[] = null;
	private int queryId = -1;
	
	public BasicGV1(String graphFile, String nodeFile){
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		nodes = dataReader.readNode();
		graph = dataReader.readGraph();
		
		//compute k-core
		KCore kcore = new KCore(graph);
		core = kcore.decompose();
	}
	
	public BasicGV1(int graph[][], String nodes[][]){
		this.graph = graph;
		this.nodes = nodes;
		
		//compute k-core
		KCore kcore = new KCore(graph);
		core = kcore.decompose();
	}
	
	public void query(int queryId, String kws[]) {
		this.queryId = queryId;
		if(core[queryId] < Config.k)   return ;
//		for(int i:core) System.out.print(i+"**");
		System.out.println(" ");
		//step 1: find the ck-core
		FindCKCore ckcoreFinder = new FindCKCore();
		int cKCoreNode[] = ckcoreFinder.findCKCore(graph, core, queryId);
//		for(int i:cKCoreNode) System.out.print(i+"**");
//		System.out.println(" ");
		//step 2: do keyword filtering
		Set<String> set = new HashSet<String>();
		for(String kw:kws)   set.add(kw);
		List<Integer> curList = new ArrayList<Integer>();//this list serves as a map (newID -> original ID)
		curList.add(-1);//for consuming space purpose
		for(int nodeId:cKCoreNode){
			int count = 0;
			//**********************  Dec 7, 2016 CYK: i should start from 0
			//**********************  Dec 11, 2016 CYK: correspends to flickr data structure ,i should start from 1 
			for(int i = 1;i < nodes[nodeId].length;i ++){
//			for(int i = 0;i < nodes[nodeId].length;i ++){
				if(set.contains(nodes[nodeId][i])){
					count += 1;
				}
			}
			
			//if this node's keywords are contained, then we choose it
			if(count == set.size())   curList.add(nodeId);
		}
//		 System.out.print(curList.toString());
//		System.out.println(" ");
		
		//step 3: find CCS
		if(curList.size() > 1){
			FindCCS finder = new FindCCS(graph, curList, queryId);
			Set<Integer> ccsSet = finder.findRobustCCS();
			System.out.print(ccsSet.size());
			if(ccsSet.size() > 1)   System.out.println("BasicGV1 finds a LAC with size = " + ccsSet.toString());
		}else{
			System.out.print("no");
		}
		
	}
	
	public static void main(String[] args){
		int graph[][] = new int[11][];
		int a1[] = {2, 3, 4, 5};	graph[1] = a1;
		int a2[] = {1, 3, 4,5};		graph[2] = a2;
		int a3[] = {1, 2, 4};		graph[3] = a3;
		int a4[] = {1, 2, 3, 6};	graph[4] = a4;
		int a5[] = {1, 2, 7};		graph[5] = a5;
		int a6[] = {4};			graph[6] = a6;
		int a7[] = {5};			graph[7] = a7;
		int a8[] = {9};				graph[8] = a8;
		int a9[] = {8};	graph[9] = a9;
		int a10[] = {};	graph[10] = a10;
		
		
		String nodes[][] = new String[graph.length][];
		String b1[] = {"1","w", "x", "y"};	nodes[1] = b1;
		String b2[] = {"2","x"};	nodes[2] = b2;
		String b3[] = {"3","x", "y"};	nodes[3] = b3;
		String b4[] = {"4","z", "x", "y"};	nodes[4] = b4;
		String b5[] = {"5","y", "z"};	nodes[5] = b5;
		String b6[] = {"6","y"};	nodes[6] = b6;
		String b7[] = {"7","x", "y"};	nodes[7] = b7;
		String b8[] = {"8","y", "z"};	nodes[8] = b8;
		String b9[] = {"9","x"};	nodes[9] = b9;
		String b10[] = {"10","x"};	nodes[10] = b10;
		
		
		
		
		DataReader dataReader=new DataReader(Config.dblpGraph, Config.dblpNode);
		int[][] g=dataReader.readGraph();
		String[][] n=dataReader.readNode();
		
		
		Config.k=5;
		int queryset[]={152532,17891};//152532:jim gary 17891:Gerhard Weikum
		String[] kwStrings={"research","data","management","system","transaction"};
		
		BasicGV1 bgv1=new BasicGV1(g, n);
//		int[] Testq={1};


		bgv1.query(152532, kwStrings);
//		String[] kwrd={"x"};
//		BasicGV1 bgv2=new BasicGV1(graph, nodes);
//		bgv2.query(1,kwrd);
	}
	
	
}
