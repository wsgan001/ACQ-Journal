package hku.variant.variant1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.FindCCS;
import hku.algo.KCore;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.index.BasicIndex;

/**
 * @author fangyixiang
 * @date Nov 3, 2015
 */
public class SW {
	private String nodes[][] = null;
	private int graph[][] = null;
	private TNode root;// the built index
	private int core[] = null;
	private int queryId = -1;
	
	public SW(int graph[][], String nodes[][], TNode root, int core[]){
		this.graph = graph;
		this.nodes = nodes;
		this.root = root;
		this.core = core;
	}
	
	//**********************  Dec 7, 2016 CYK:  increase one constructor
	public SW(int graph[][],String nodes[][],TNode root){
		this.graph = graph;
		this.nodes = nodes;
		this.root=root;
		
		KCore kcrore=new KCore(graph);
		this.core=kcrore.decompose();
		
	}
	
	public void query(int queryId, String kws[]){
		this.queryId = queryId;
		if(core[queryId] < Config.k)   return ;
		
		Set<String> set = new HashSet<String>();
		for(String kw:kws)   set.add(kw);
		
		List<Integer> curList = new ArrayList<Integer>();//this list serves as a map (newID -> original ID)
		curList.add(-1);//for consuming space purpose
		
		TNode subRoot = locateAllCK(root);
		Queue<TNode> queue = new LinkedList<TNode>(); 
		queue.add(subRoot);
		while(queue.size() > 0){
			TNode curNode = queue.poll();
			
			//intersection on the inverted list
			Map<String, int[]> kwMap = curNode.getKwMap();
			Set<Integer> intersecSet = new HashSet<Integer>();
			boolean isFirst = true;
			for(int i = 0;i < kws.length;i ++){
				if(kwMap.containsKey(kws[i])){
					int invert[] = kwMap.get(kws[i]);
					if(isFirst){
						isFirst = false;
						for(int j = 0;j < invert.length;j ++)   intersecSet.add(invert[j]);
					}else{
						Set<Integer> tmpSet = new HashSet<Integer>();
						for(int j = 0;j < invert.length;j ++){
							if(intersecSet.contains(invert[j])){
								tmpSet.add(invert[j]);
							}
						}
						intersecSet = tmpSet;
					}
				}else{//this keyword is not contained. Skip all the nodes!!!
					intersecSet = null;
					break;
				}
			}
			if(intersecSet != null)   curList.addAll(intersecSet);//collect all the candidate nodes
			for(TNode tnode:curNode.getChildList())   queue.add(tnode);
		}
		
		if(curList.size() > 1){
			FindCCS finder = new FindCCS(graph, curList, queryId);
			Set<Integer> ccsSet = finder.findRobustCCS();
			if(ccsSet.size() > 1){
				System.out.println("SW finds a LAC with size = " + ccsSet.size());
				if(ccsSet.size() > 1)   System.out.println("BasicGV1 finds a LAC with size = " + ccsSet.toString());
			}else{
				System.out.println("No community");
			}
		}
	}
	
	// locate a list of tnodes, each of which has (1)coreness>=Config.k and (2) contains queryId
	private TNode locateAllCK(TNode root) {
		// step 1: find nodes with coreNumber=Config.k using BFS
		List<TNode> candRootList = new ArrayList<TNode>();
		Queue<TNode> queue = new LinkedList<TNode>();
		queue.add(root);

		while (queue.size() > 0) {
			TNode curNode = queue.poll();
			for (TNode tnode : curNode.getChildList()) {
				if (tnode.getCore() < Config.k) {
					queue.add(tnode);
				} else {// the candidate root node must has coreness at least Config.k
					candRootList.add(tnode);
				}
			}
		}
//		System.out.println("candRootList.size:" + candRootList.size());

		// step 2: locate a list of ck-cores
		for (TNode tnode : candRootList) {
			if (findCK(tnode)) {
				return tnode;
			}
		}

		return null;
	}
	
	// check whether a subtree rooted at "root" contains queryId or not
	private boolean findCK(TNode root) {
		if (root.getCore() <= core[queryId]) {
			boolean rs = false;
			if (root.getNodeSet().contains(queryId)) {
				rs = true;
			} else {
				for (TNode tnode : root.getChildList()) {
					if (findCK(tnode)) {
						rs = true;
						break;
					}
				}
			}
			return rs;
		} else {
			return false;
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
		String b1[] = {"w", "x", "y"};	nodes[1] = b1;
		String b2[] = {"x"};	nodes[2] = b2;
		String b3[] = {"x", "y"};	nodes[3] = b3;
		String b4[] = {"z", "x", "y"};	nodes[4] = b4;
		String b5[] = {"y", "z"};	nodes[5] = b5;
		String b6[] = {"y"};	nodes[6] = b6;
		String b7[] = {"x", "y"};	nodes[7] = b7;
		String b8[] = {"y", "z"};	nodes[8] = b8;
		String b9[] = {"x"};	nodes[9] = b9;
		String b10[] = {"x"};	nodes[10] = b10;
		
		AdvancedIndex adIndex=new AdvancedIndex(graph, nodes);
		TNode root=adIndex.build();
		SW sw=new SW(graph, nodes,adIndex.build());
		int[] Testq={1};
		String[] kwrd={"x"};
		Config.k=1;
		sw.query(1, kwrd);
	}
	
	
}
