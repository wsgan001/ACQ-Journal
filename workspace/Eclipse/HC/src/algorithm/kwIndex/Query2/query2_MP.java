package algorithm.kwIndex.Query2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import algorithm.FindCKSubG;
import algorithm.kwIndex.KWNode;
import config.Config;

/**
@author chenyankai
@Date	Sep 30, 2017
	This query algorithm:
	(1). Index-based 
	(2). merge paths of the sub Ptree from bottom up to mine all maximal P-trees
	(3). using the rightmost leaf to generate each pattern once 
	
	10.1 conclusion after debug:
	merge path can find one feasible solution (can be used in MARGIN)
	but can not find all maximal patterns 
	
	
*/
public class query2_MP {
	private int[][] graph = null;
	private int queryId = -1;
	private int k = -1;
	private Map<Integer, List<KWNode>> headList = null;
	private Map<Integer,KWNode> subKWTree = null;
	private List<Integer> leafList = null;
	//key:pattern  value users
	private Map<Set<Integer>, Set<Integer>> output=null;
	
	private boolean debug = false;
	
	
	public query2_MP(int[][] graph, Map<Integer, List<KWNode>> headList){
		this.graph = graph;
		this.headList = headList;
		this.k = Config.k;	
	}
	
	public void query(int queryID){
		this.queryId = queryID;
		this.output = new HashMap<Set<Integer>, Set<Integer>>();
		induceKWTree();
		mergePath();
	}

	
	//induce a KW-tree subtree and store it in a map
	private void induceKWTree(){
		this.subKWTree = new HashMap<Integer,KWNode>();
		this.leafList = new LinkedList<Integer>();
		Map<Integer, List<Integer>> childMap = new HashMap<Integer, List<Integer>>();
		KWNode root = new KWNode(1);
		subKWTree.put(1, root);
		int maxLength = 0;
//		System.out.println(headList.get(queryId).size()+"   size ");
		for(KWNode currentNode:headList.get(queryId)){
			//to mark the leaf item in the induce subKWtree
			int leaf = -1;
			int countLength = 0;
			boolean first = true;
			while(currentNode.itemId != 1){

				Set<Integer> vertexSet = currentNode.getCKCore(k, queryId);
				if(!vertexSet.isEmpty()){
					int currenItem = currentNode.itemId;
					if(!subKWTree.containsKey(currenItem)){
						KWNode newNode = new KWNode(currenItem);
						newNode.tmpVertexSet = vertexSet;
						subKWTree.put(currenItem, newNode);
						//computing the length of the current path
						countLength++;
						
						if(first){
							leaf = currenItem;
							first = false;
						}
					}
					
					List<Integer> child = childMap.get(currentNode.father.itemId);
					if(child==null){
						child = new ArrayList<Integer>();
						child.add(currenItem);
						childMap.put(currentNode.father.itemId, child);
					}else{
						child.add(currenItem);
					}
				}
				currentNode = currentNode.father;
			}
			if(countLength > maxLength) {
				maxLength = countLength;
			}
			if(subKWTree.containsKey(leaf)) leafList.add(leaf);
		}
		
		Iterator<Integer> iter = childMap.keySet().iterator();
		while(iter.hasNext()){
			int father = iter.next();
			KWNode fatherNode = subKWTree.get(father);
			for(int child:childMap.get(father)){
				subKWTree.get(child).father = fatherNode;
				fatherNode.childList.add(subKWTree.get(child));
			}
		}	
		
		System.out.println(subKWTree.get(1).toString(""));
		
	}
	
	
	private void mergePath(){
		Queue<Set<Integer>> leafQueue = new LinkedList<Set<Integer>>();
		Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();
		Queue<Set<Integer>> userQueue = new LinkedList<Set<Integer>>();
		//initialize the pattern using a path 
		for(int leaf: leafList) {
			Set<Integer> initPattern = new HashSet<Integer>();
			Set<Integer> initLeaf = new HashSet<Integer>();
			KWNode leafNode = subKWTree.get(leaf);
			Set<Integer> initUsers = getUsersInKTree(leaf);
			while(leafNode.itemId!=1){
				initPattern.add(leafNode.itemId);
				leafNode = leafNode.father;
			}
			initPattern.add(1);
			
			initLeaf.add(leaf);
			
			patternQueue.add(initPattern);
			leafQueue.add(initLeaf);
			userQueue.add(initUsers);
			System.out.println("initial pattern: "+initPattern.toString());
			//------------------------DEBUG------------------------------
			if(debug)	System.out.println("init pattern: "+initPattern.toString()+" init users: "+initUsers.toString());
			//----------------------END DEBUG----------------------------
			int max = leaf; 
			while(true){
				int father = subKWTree.get(max).father.itemId;
				if(father==1) break;
				Set<Integer> subPattern = new HashSet<Integer>();
				for(int x:initPattern){
					if(x <= father) subPattern.add(x);
				}
				Set<Integer> users = getUsersInKTree(father);
				
				patternQueue.add(subPattern);
				leafQueue.add(initLeaf);
				userQueue.add(users);
				System.out.println("sub pattern: "+subPattern.toString()+"usrs size: "+users.size());

				max = father;
			}
			
		}
		
		
			//get the global right most leaf
			int globalRMLeaf = -1;
			for(int x:leafList) if(x>globalRMLeaf) globalRMLeaf = x;
			BufferedWriter std;
			try {
				std = new BufferedWriter(new FileWriter(Config.pubMedDataWorkSpace+"buffer.txt"));
			
			

			//start to mine all possible paths
			while(!patternQueue.isEmpty()&&!leafQueue.isEmpty()){
				System.out.println("patternQueue size: "+patternQueue.size());
				Set<Integer> checkLeaves = leafQueue.poll();
				Set<Integer> checkPattern = patternQueue.poll();
				Set<Integer> checkUsers = userQueue.poll();
				if(checkPattern.size()==subKWTree.size()){
					System.out.println("very special case");
					checkMax(checkPattern, checkUsers);
					break;
				}
					List<Integer> list1 =new ArrayList<Integer>(checkPattern);
					Collections.sort(list1);
					std.write("checking patterns: "+list1.toString()+" users: "+checkUsers.size()+"  check leaves: "+checkLeaves.toString());
					std.newLine();
				
				//------------------------DEBUG------------------------------
				if(debug){
					System.out.println();
					System.out.println(checkPattern.toString()+" users: "+checkUsers.toString()+"check leaves: "+checkLeaves.toString());
				}
				//----------------------END DEBUG----------------------------
			
				int rightMostLeaf = getRightMostLeaf(checkLeaves);
				if(rightMostLeaf == globalRMLeaf){
					List<Integer> list =new ArrayList<Integer>(checkPattern);
					Collections.sort(list);
					std.write("case1: "+"now a tmp maximal pattern: "+list.toString()+" users: "+checkUsers.size());
					std.newLine();
					std.newLine();
					checkMax(checkPattern,checkUsers);
					continue;
				}
					
				boolean isTmpMaximal = true;
				for(int newLeaf: leafList){
					if(newLeaf <= rightMostLeaf) continue;
					
					//firstly check the leaf item
					Set<Integer> users = getUsersInKTree(newLeaf);
					boolean isFeasible = false;
					if(checkUsers.equals(users)) {
						isFeasible= true;
					}
					
					else{
						Set<Integer> newUsers = getUsersofPatternNewItem(checkUsers, users);
						if(!newUsers.isEmpty()){//is feasible
							isFeasible = true;
							users = newUsers;
						}else{
							
							int currentItem = subKWTree.get(newLeaf).father.itemId;
							 
							while(true){
								//if a leaf's father is in this pattern, and new pattern with this leaf is infre, find a initcut 
								if(checkPattern.contains(currentItem)){
									break;
								}
								Set<Integer> currentItemUsers = getUsersInKTree(currentItem);
								Set<Integer> nextUsers = getUsersofPatternNewItem(checkUsers, currentItemUsers);
								
								if(nextUsers.isEmpty()){
									currentItem = subKWTree.get(currentItem).father.itemId;
							
								}else {
									Set<Integer> nextleaves = new HashSet<Integer>();
									Set<Integer> nextPattern = new HashSet<Integer>();
									nextPattern.addAll(checkPattern);
									int tmpcurrentItem = currentItem;
									while(!nextPattern.contains(tmpcurrentItem)){
										nextPattern.add(tmpcurrentItem);
										tmpcurrentItem = subKWTree.get(tmpcurrentItem).father.itemId;
									}
									nextleaves.addAll(checkLeaves);
									nextleaves.add(newLeaf);
									
									patternQueue.add(nextPattern);
									userQueue.add(nextUsers);
									leafQueue.add(nextleaves);
									isTmpMaximal = false;
									break;
								}		
							}
						}
					}
					//if this path is feasible, we do not need to compute attributes on this path
					if(isFeasible){
						isTmpMaximal = false;
						Set<Integer> newLeaves = new HashSet<Integer>();
						Set<Integer> newPattern = new HashSet<Integer>();
						newLeaves.addAll(checkLeaves);
						newLeaves.add(newLeaf);
						newPattern.addAll(checkPattern);
						int item = newLeaf;
						while(!newPattern.contains(item)){
							newPattern.add(item);
							item = subKWTree.get(item).father.itemId;
						}
						userQueue.add(users);
						patternQueue.add(newPattern);
						leafQueue.add(newLeaves);		
					}			
				}
				
				if(isTmpMaximal) {
					List<Integer> list =new ArrayList<Integer>(checkPattern);
					Collections.sort(list);
					std.write("case 2:   "+"now a tmp maximal pattern: "+list.toString()+" users: "+checkUsers.size());
					std.newLine();
					std.newLine();
					checkMax(checkPattern, checkUsers);
				}

			}		
			
			std.flush();
			std.close();
			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	
	
	//get all the connected k-core from the each ktree of sepcific item
	private Set<Integer> getUsersInKTree(int item){
		return subKWTree.get(item).tmpVertexSet;
	}
		
	
	private Set<Integer> getUsersofPatternNewItem(Set<Integer> currentUsers, Set<Integer> itemUsers){
		Set<Integer> newUsers = new HashSet<Integer>();
		if(currentUsers.equals(itemUsers)){
			newUsers.addAll(itemUsers);
		}else {
			newUsers = intersect(currentUsers, itemUsers);
			FindCKSubG findCKSG=new FindCKSubG(graph, newUsers, queryId);
			newUsers =findCKSG.findCKSG();
			if(newUsers==null) newUsers = new HashSet<Integer>();
		}
		return newUsers;
	}
	
	
	//get the intersect of two sets
	private Set<Integer> intersect(Set<Integer> set1, Set<Integer> set2){
		Set<Integer> newSet = new HashSet<Integer>();
		if(set1.isEmpty()||set2.isEmpty()) return newSet;
			
		if(set1.size()>set2.size()){
			Iterator<Integer> iter = set2.iterator();
			while(iter.hasNext()){
				int x = iter.next();
				if(set1.contains(x)) newSet.add(x);
			}
		}else{
			Iterator<Integer> iter = set1.iterator();
			while(iter.hasNext()){
				int x = iter.next();
				if(set2.contains(x)) newSet.add(x);
			}
		}
		return newSet;
	}

	private int getRightMostLeaf(Set<Integer> leaves){
		int max = -1;
		for(int x:leaves){
			if(x>max) max = x;
		}
		return max;
	}
		
		
	//check one pattern is a maximal pattern whether or not in result set  
	private void checkMax(Set<Integer> seq, Set<Integer>users){
		if(output.isEmpty()) {
			output.put(seq, users);
			return;
		}
		if(output.containsKey(seq)) return;
			
		boolean flag = true;
		Iterator<Set<Integer>> keyIter = output.keySet().iterator();
		while(keyIter.hasNext()){
			Set<Integer> key = keyIter.next();
			if(key.size()>seq.size()){
				if(key.containsAll(seq)){
					flag=false;
					break;
				}
			}else if(key.size()<seq.size()) {
				if(seq.containsAll(key)){
					keyIter.remove();
				}
			}
		}
		if(flag==true) output.put(seq, users);
	}
	
	//print all PCs
		public void print(){
			Iterator<Set<Integer>> iter = output.keySet().iterator();
			while(iter.hasNext()){
				Set<Integer> pattern = iter.next();
				List<Integer> list = new ArrayList<Integer>(pattern);
				Collections.sort(list);
				Set<Integer> user = output.get(pattern);
				if(user.size()>20)
					System.out.println("pattern: "+list.toString()+" users: "+user.size());
				else 
					System.out.println("pattern: "+list.toString()+" users: "+user.toString());
			}
		}
	
	
}


