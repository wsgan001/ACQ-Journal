package hku.util;

import hku.Config;
import hku.algo.DataReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author fangyixiang
 * @date Jul 21, 2015
 */
public class Baseline0 {
	private int userNum = -1;
	private String users[][] = null;
	private int graph[][] = null;

	public Baseline0(String nodeFile, String graphFile) {
		DataReader dataReader = new DataReader(nodeFile, graphFile);
		userNum = dataReader.getUserNum();
		users = dataReader.readNode();
		graph = dataReader.readGraph();
	}

	public void query(int nodeId) {
		// step 1: find a k-core
		findKCore();
//		track(nodeId);

		// step 2: keyword search
		kwFilter();
		System.out.println(graph[7304].length);

		// step 3: find the k-core
		findKCore();
		System.out.println(graph[7304].length);
//		track(nodeId);
		
		// step 4: find the sub-graph containing nodeId
		findMCC(nodeId);
	}

	private void findKCore() {
		// we remove a batch of nodes, whose degrees are less than k
		while (true) {
			boolean flag = false;// no node has degree bigger than k
			for (int i = 0; i < userNum; i++) {
				if (graph[i] != null && graph[i].length < Config.k) {
					flag = true;
					// step 1: remove edges
					for (int j = 0; j < graph[i].length; j++) {
						int neighbor = graph[i][j];

						// find the neighbor
						int pos = -1;
						for (int k = 0; k < graph[neighbor].length; k++) {
							if (graph[neighbor][k] == i) {
								pos = k;
								break;
							}
						}

						// update its edge list
						int len = graph[neighbor].length;
						int edge[] = new int[len - 1];
						for (int k = 0; k < graph[neighbor].length; k++) {
							if (k < pos) {
								edge[k] = graph[neighbor][k];
							} else if (k > pos) {
								edge[k - 1] = graph[neighbor][k];
							}
						}
						graph[neighbor] = edge;
					}

					// step 2: remove nodes
					graph[i] = null;
				}
			}
			if (flag == false)
				break;
		}
	}

	private void kwFilter() {
//		String keyword = "cluster"; //"classification"; //jiawei han
//		String keywords[] = "frequent pattern mine".split(" ");
//		String keywords[] = "stream classification".split(" ");
		String keywords[] = "heterogeneous network".split(" ");
		for (int i = 0; i < users.length; i++) {
			if(graph[i] != null){
				boolean isContained = true; //It is true, if all the keywords are contained in another set
				for(int j = 0;j < keywords.length; j ++){
					boolean tmp = false; //It is true, if this keyword is contained
					for (int k = 1; k < users[i].length; k++) {
						if(users[i][k].equals(keywords[j])){
							tmp = true;
							break;
						}
					}
					if(tmp == false){
						isContained = false;
						break;
					}
				}
				if(i == 7304)	System.out.println("isContained:" + isContained);
				
				// update sub-graph, if the input keywords are not well contained
				if (isContained == false) {
					// step 1: remove edges
					for (int j = 0; j < graph[i].length; j++) {
						int neighbor = graph[i][j];

						// find the neighbor
						int pos = -1;
						for (int k = 0; k < graph[neighbor].length; k++) {
							if (graph[neighbor][k] == i) {
								pos = k;
								break;
							}
						}

						// update its edge list
						int len = graph[neighbor].length;
						int edge[] = new int[len - 1];
						for (int k = 0; k < graph[neighbor].length; k++) {
							if (k < pos) {
								edge[k] = graph[neighbor][k];
							} else if (k > pos) {
								edge[k - 1] = graph[neighbor][k];
							}
						}
						graph[neighbor] = edge;
					}

					// step 2: remove nodes
					graph[i] = null;
				}
			}
		}
	}

	private List<String> findMCC(int nodeId){
		List<String> friendList = new ArrayList<String>();
		
		//step 1: initialize
		boolean visit[] = new boolean[userNum];
		visit[nodeId] = true;
		Queue<Integer> queue = new LinkedList<Integer>(); 
		for(int i = 0;i < graph[nodeId].length;i ++){
			int neighbor = graph[nodeId][i];
			queue.add(neighbor);
		}
		
		//step 2: search
		while(queue.size() > 0){
			int current = queue.poll();
			visit[current] = true;
			
			for(int i = 0;i < graph[current].length;i ++){
				int neighbor = graph[current][i];
				if(visit[neighbor] == false){
					queue.add(neighbor);
				}
			}
		}
		
		//step 3: collect
		for(int i = 0;i < userNum;i ++){
			if(visit[i]){
				friendList.add(users[i][0]);
				
				System.out.print("User:" + users[i][0]);
				for(int j = 0;j < graph[i].length;j ++){
					int neighbor = graph[i][j];
					System.out.print(" \"" + users[neighbor][0] + "\"");
				}
				System.out.println();
			}
		}
		System.out.println("community size:" + friendList.size());
		
		return friendList;
	}
		
	public void track(int nodeId) {
		// test codes
		int count = 0;
		for (int i = 0; i < userNum; i++) {
			if (graph[i] != null) {
				count += 1;
				if (graph[i].length < Config.k) {
					System.out.println("fuck! A mistake has been made");
				}
				for (int j = 0; j < graph[i].length; j++) {
					int neighbor = graph[i][j];
					if (graph[neighbor] == null) {
						System.out.println("fuck! Another mistake as been made");
					}
				}
			}
		}
//		for (int i = 0; i < graph[5224].length; i++) {
//			int neighbor = graph[5224][i];
//			System.out.println(users[neighbor][0]);
//		}
		System.out.println("The number of nodes in k-core is " + count);
		if(graph[nodeId] == null){
			System.out.println(users[nodeId][0] + " disappears");
		}else{
			System.out.println(users[nodeId][0] + " exists");
		}
	}

	public static void main(String[] args) {
		long startT = System.currentTimeMillis();
		Baseline0 base = new Baseline0(Config.caseNode, Config.caseGraph);
		base.query(7304);
		long endT = System.currentTimeMillis();
		System.out.println("time cost:" + (endT - startT));
	}

}
