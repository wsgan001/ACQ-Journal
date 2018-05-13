package hku.algo.query2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import hku.Config;
import hku.util.CCSSaver;
import hku.algo.*;

import java.util.*;

public class DecRevision {
	private int graph[][];// graph structure
	private String nodes[][];// the keywords of each node
	private TNode root;// the built index
	private int core[];
	private TNode invert[];
	private String ccsFile = null;
//	private CCSSaver saver = null;
	private int queryId = -1;
	private AprioriPruner apruner = null;
	private Set<String> seedKwSet = null;
	private long startT = 0;

	public DecRevision(int graph[][], String nodes[][], TNode root, int core[], String ccsFile) {
		this.graph = graph;
		this.nodes = nodes;
		this.root = root;
		this.core = core;
		this.ccsFile = ccsFile;
	}
	public DecRevision(int graph[][], String nodes[][], TNode root){
		this.graph = graph;
		this.nodes = nodes;
		this.root = root;
		
		KCore k1=new KCore(graph);
		core=k1.decompose();
	}

	public Set<Integer> query(int queryId){
		this.queryId = queryId;
		TNode tnode = locateAllCK(root);
		
		if(tnode != null){
			Set<Integer> lacSet = new HashSet<Integer>();
			findCKcore(tnode, lacSet);
			return lacSet;
		}else{
			return null;
		}
	}

	private void findCKcore(TNode root, Set<Integer> set){
		for(int id:root.getNodeSet())   set.add(id);
		for(TNode tnode:root.getChildList())   findCKcore(tnode, set);
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
}
