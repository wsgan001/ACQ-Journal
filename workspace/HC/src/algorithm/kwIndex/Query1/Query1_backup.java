package algorithm.kwIndex.Query1;

import java.io.*;
import java.util.*;

import algorithm.FindCKSubG;
import algorithm.kwIndex.*;
import config.Config;

/**
@author chenyankai
@Date	Aug 29, 2017
	This class is the index-based query algorithm.
	No-Apriori based (MAGRIN) 
	Details are derived from Query1_V1 and optimized.
	
	Difference between V1 and V2 is that V1 :
	(1). Space efficient.
	(2). DFS manner to expand all cuts. 
*/
public class Query1_backup {
	private int[][] graph = null;
	private int queryId = -1;
	private int k = -1;
	private Map<Integer, List<KWNode>> headList = null;
	private List<KWNode> leafList = null;
	private Map<Integer,KWNode> subKWTree = null;
	//key:pattern  value users
	private Map<Set<Integer>,Set<Integer>> lattice = null;
	private Set<Set<Integer>> maximalPattern = null;	
	Set<List<Set<Integer>>> visited = null;
	

	private boolean debug = false;
	
	public Query1_backup(int[][] graph,Map<Integer, List<KWNode>> headList){
		this.graph = graph;
		this.leafList = new LinkedList<KWNode>();
		this.headList = headList;
		this.k=Config.k;
		
		
	}
	
	//main function
	public void query(int queryId){
		this.lattice = new HashMap<Set<Integer>, Set<Integer>>();
		this.maximalPattern = new HashSet<Set<Integer>>();	
		this.visited = new HashSet<List<Set<Integer>>>(); 
		
		this.queryId = queryId;
		induceSubKWTree();
		System.out.println("step1 getsubtree finished, subkwtree size: "+subKWTree.size());
	
		List<Set<Integer>> initCut = decInitCross();
//		List<Set<Integer>> initCut = incInitCross();
		
		
		if(initCut.size()==1) return;
		if(initCut.isEmpty()){
			System.out.println("No community!");
			return;
		}
		
		//expandCross
		System.out.println("entering expandCut.");
		expandCross(initCut.get(0),initCut.get(1));

		print();
				
	}
	
	//induce a KW-tree subtree and store it in a map
	private void induceSubKWTree(){
		this.subKWTree = new HashMap<Integer,KWNode>();
		Map<Integer, List<Integer>> childMap = new HashMap<Integer,List<Integer>>();
		KWNode root =new KWNode(1);
		subKWTree.put(1, root);
		//------------------------DEBUG------------------------------
		if(debug){
			System.out.println("headList size: "+headList.get(queryId).size());
			for(KWNode currentNode:headList.get(queryId)){ System.out.println("detail: "+currentNode.itemId);}
		}
		//----------------------END DEBUG----------------------------
		
		for(KWNode currentNode:headList.get(queryId)){
			//to mark the leaf item in the induce subKWtree
			int leaf = -1;
			boolean first = true;
//			if(currentNode.refined) {
//				System.out.println(currentNode.itemId);
//				continue;
//			}
			while(currentNode.itemId != 1){
				
				Set<Integer> vertexSet = currentNode.getCKCore(k, queryId);
//				Set<Integer> vertexSet = currentNode.tmpVertexSet;
				if(!vertexSet.isEmpty()){
//					System.out.println("inside: "+currentNode.itemId);
					int currenItem = currentNode.itemId;
					if(!subKWTree.containsKey(currenItem)){
						KWNode newNode = new KWNode(currenItem);
						newNode.tmpVertexSet = vertexSet;
						subKWTree.put(currenItem, newNode);
						
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
			if(subKWTree.containsKey(leaf)) leafList.add(subKWTree.get(leaf));
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
		
		//------------------------DEBUG-----------------------------
		if(debug) 	{
			System.out.println(root.toString(""));
			for(KWNode leaf:leafList) System.out.println("leafList: "+leaf.itemId);
		}
		//----------------------END DEBUG---------------------------	
	}
	
	
	//get the leaf item in the subtree which has the long path from bottom up
	private int longestPath(){
		int longestleaf = -1;
		int maxLen = 0;
		for(KWNode node:leafList){
			int leaf = node.itemId;
			KWNode currentNode = subKWTree.get(leaf);
			if(currentNode==null) continue;
			int count = 0;
			while(currentNode.itemId!=1){
				currentNode =currentNode.father;
				count++;
			}
			if(count>=maxLen) {
				longestleaf = leaf;
				maxLen = count;
			}
		}
		return longestleaf;
	}

	
	
	//search a feasible solution in a decremental manner
	private List<Set<Integer>> decInitCross(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		Set<Integer> pattern= new HashSet<Integer>();
		for(Iterator<Integer> it = subKWTree.keySet().iterator();it.hasNext();) 
				pattern.add(it.next());

		Set<Integer> users = obtainUser(pattern);
		if(!users.isEmpty()){
			maximalPattern.add(pattern);
			lattice.put(pattern, users);
			initCut.add(pattern);
		}
		else{
			Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();
			patternQueue.add(pattern);
			boolean tag = true;
			
			while(!patternQueue.isEmpty() && tag){
				System.out.println("pattern queue length: "+patternQueue.size());
				Set<Integer> tocheck = patternQueue.poll();
				if(tocheck.size()==2) return initCut;// means there is no qualified patterns 
				
				else{
					//looking for the leaf item and delete it
					Set<Integer> leaves = getLeaves(tocheck);
					for(int leaf:leaves){
						Set<Integer> nextpattern = new HashSet<Integer>();
						Set<Integer> nextUsers = null;
						for(int x:tocheck){
							if(x==leaf) continue;
							nextpattern.add(x);
						}
						nextUsers = obtainUser(nextpattern);
						if(nextUsers.isEmpty()){
							patternQueue.add(nextpattern);
						}else {
							maximalPattern.add(nextpattern);
							initCut.add(tocheck);
							initCut.add(nextpattern);
							tag = false;
							break;
						}
					}
				}			
			}			
		}
		return initCut;
	}
		
	
	//search a feasible solution in an incremental manner (BFS based)
	private List<Set<Integer>> incInitCross(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();
		Queue<Set<Integer>> userQueue = new LinkedList<Set<Integer>>();

		//initialize patterns 
		for(KWNode node:subKWTree.get(1).childList){
			int item = node.itemId;
//			if(!subKWTree.containsKey(item)) continue;
			
			Set<Integer> pattern = new HashSet<Integer>();
			pattern.add(1);//the root node
			pattern.add(item);
			Set<Integer> userSet = obtainUser(pattern);
			if(!userSet.isEmpty()) {
				patternQueue.add(pattern);
				userQueue.add(userSet);
				
			}
		}
		
		boolean tag=true;
		while(!patternQueue.isEmpty()&&!userQueue.isEmpty()&&tag){
			System.out.println("patternQueue size: "+patternQueue.size());
			
			Set<Integer> tocheck = patternQueue.poll();
			Set<Integer> currentUser = userQueue.poll();
			//if already reach the maximum pattern then return
			if(tocheck.size()==subKWTree.size()) {
				maximalPattern.add(tocheck);
				lattice.put(tocheck, currentUser);
				initCut.add(tocheck);
				break;
			}
			List<Integer> RMPath = getRightmostPath(tocheck);
			for(int x:RMPath){
				for(KWNode node:subKWTree.get(x).childList){
					int item = node.itemId;
					if(!tocheck.contains(item)){
						Set<Integer> newPattern= new HashSet<Integer>();
						Set<Integer> newUsers = new HashSet<Integer>();
						newPattern.add(item);
						for(int y:tocheck) newPattern.add(y);
						 Set<Integer> users = getUsersInKTree(item);
						 //------------------------DEBUG------------------------------
						 if(debug){
							 System.out.println("newPattern: "+newPattern.toString());
							 System.out.print("newUsers: "+users.toString());
							 System.out.print("current users: "+currentUser.toString());
						 }
						//----------------------END DEBUG----------------------------
						
						 if(!currentUser.equals(users)){
							newUsers = intersect(users, currentUser);
//							users.retainAll(currentUser);
							//compute CKC
							FindCKSubG findCKSG=new FindCKSubG(graph, newUsers, queryId);
							newUsers =findCKSG.findCKSG();
							if(newUsers==null){
								newUsers = new HashSet<Integer>(); 
								lattice.put(newPattern, newUsers);
								initCut.add(newPattern);
								initCut.add(tocheck);
								tag=false;
								break;
							}else{
								lattice.put(newPattern, newUsers);
								patternQueue.add(newPattern);
								userQueue.add(newUsers);
								}
							}
						 else{
							 lattice.put(newPattern, users);
							patternQueue.add(newPattern);
							userQueue.add(users);
						 }
					}
				}
			}
			
		}
		return initCut;
	}
	
	
	
	private List<Set<Integer>> jumpInitCross(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		int longestLeaf = longestPath();
		System.out.println("longest path leaf: "+longestLeaf);	
		return initCut;
	}


	
	//obtain all users according to the leaf items instead of the whole P-tree
	//if a seq corresponds to no community 		------>  userSet.size=0
	//if lattice does not contain this pattern 	------>  userSet=null
	private Set<Integer> obtainUser(Set<Integer> pattern){
		Set<Integer> userSet = lattice.get(pattern);
		
		if(userSet == null){
			Set<Integer> leaves = getLeaves(pattern);
			Set<Integer> users = null;
			Iterator<Integer> iter = leaves.iterator();
			while(iter.hasNext()){
				int leaf = iter.next();
				if(users==null){
					users = new HashSet<Integer>();
					users.addAll(getUsersInKTree(leaf));
 				}
				else users = intersect(users, getUsersInKTree(leaf));	
			}
			
			//compute CKC
			FindCKSubG findCKSG=new FindCKSubG(graph, users, queryId);
			userSet =findCKSG.findCKSG();
			if(userSet==null) userSet = new HashSet<Integer>();
			lattice.put(pattern, userSet);
		}
		return userSet;
	}
	
		
	//get the Connected k-core from the every kTree of specific item
	private Set<Integer> getUsersInKTree(int item){
		return subKWTree.get(item).tmpVertexSet;
	}
	
	//expand a cross recursively
	private void expandCross(Set<Integer> inFreSeq, Set<Integer> freSeq){
//		System.out.println("expandCut");
		List<Set<Integer>> list = new ArrayList<Set<Integer>>(2);
		list.add(inFreSeq); list.add(freSeq);
		if(visited.contains(list)) return;
		visited.add(list);	
	
		//------------------------DEBUG------------------------------
		if(debug){
			System.out.println("fre seq: "+freSeq.toString()+" infre: "+inFreSeq.toString());
		}
		//----------------------END DEBUG----------------------------
		
		Set<Set<Integer>> parentSeqSet=parentSeq(inFreSeq);
		if(parentSeqSet.isEmpty()) return;
			
		for(Iterator<Set<Integer>> it=parentSeqSet.iterator();it.hasNext();){
			Set<Integer> parentSeq = it.next();
			Set<Integer> userSet = obtainUser(parentSeq);
			
			if(!userSet.isEmpty()){
				checkMax(parentSeq);
				
				Set<Set<Integer>> childOfParentSeq = childSeq(parentSeq);
				//9.17 debug 
				if(childOfParentSeq.isEmpty()) return;
				
				for(Iterator<Set<Integer>> iter=childOfParentSeq.iterator();iter.hasNext();){
					Set<Integer> element = iter.next();
					Set<Integer> userOfElement = lattice.get(element);

					if(userOfElement.isEmpty()){
						expandCross(element, parentSeq);
					}else{
						
						Set<Integer> commonChild = findCommon(inFreSeq, element);
						//********************** 9.17 added **********************
						if(!lattice.containsKey(commonChild)) {
							Set<Integer> empty = new HashSet<Integer>();
							lattice.put(commonChild, empty);
						}
						//********************** 9.17 added **********************						
						expandCross(commonChild, element);
					}
				}
			}else{
				Set<Set<Integer>> parentOfParentSeq = parentSeq(parentSeq);
				for(Iterator<Set<Integer>> iter = parentOfParentSeq.iterator();iter.hasNext();){
					Set<Integer> element = iter.next();
					if(parentSeq.equals(element)) System.out.println("check heheheheh");
					Set<Integer> userOfElement = obtainUser(element);
					if(!userOfElement.isEmpty()){
						expandCross(parentSeq, element);
					}
				}
			}	
		}	
		return;
	}

			
	//obtain all child patterns of the current pattern
	private Set<Set<Integer>> childSeq(Set<Integer> seq){
		Set<Set<Integer>> childSeq = new HashSet<Set<Integer>>();
		if(seq.size()==subKWTree.size()) return childSeq;

		for(int x:seq){
			KWNode node = subKWTree.get(x);
	 		for(KWNode child:node.childList){
	 			int item = child.itemId;
				if(!seq.contains(item)){
					Set<Integer> nextSeq = new HashSet<Integer>(seq.size()+1);
					nextSeq.add(item);
					for(int y:seq) nextSeq.add(y);
					childSeq.add(nextSeq);
					if(seq.size()==nextSeq.size()) System.out.println("impossible???!!  "); 
					

					if(lattice.containsKey(nextSeq)) continue;
					//optimized 
					Set<Integer> userSet = lattice.get(seq);
					Set<Integer> users = getUsersInKTree(item);
					if (users==null)  System.out.println(" case 3 ");
					if(!userSet.equals(users)){
						Set<Integer> newUsers = intersect(users, userSet);
						//compute CKC
						FindCKSubG findCKSG	= new FindCKSubG(graph, newUsers, queryId);
						newUsers = findCKSG.findCKSG();
						
						if(newUsers == null) newUsers = new HashSet<Integer>();	
						lattice.put(nextSeq, newUsers);
					}else {
						lattice.put(nextSeq, users);
					}
				}	
			}
		}
		return childSeq;	
	}

	
	//obtain all parent patterns of the current pattern from the leaf nodes
	//O(m) time complexity to obtain parentSeq without computing all leaf nodes previously
	private Set<Set<Integer>> parentSeq(Set<Integer> seq){
		Set<Set<Integer>> parentSeq = new HashSet<Set<Integer>>();
		if(seq.size()==2){
			return parentSeq;
		}
		
		Set<Integer> leaves = getLeaves(seq);
		for(Iterator<Integer> it = leaves.iterator();it.hasNext();){
			int leaf = it.next();
			Set<Integer> nextSeq = new HashSet<Integer>(seq.size()-1);
			for(int x:seq) {
				if(x != leaf) nextSeq.add(x);
			}
			parentSeq.add(nextSeq);
		}
		return parentSeq;
	}
			
	
	//get leaves of current pattern
	private Set<Integer> getLeaves(Set<Integer> pattern){
		Set<Integer> leaves = new HashSet<Integer>();
		leaves.addAll(pattern);
		Iterator<Integer> it = pattern.iterator();
		while(it.hasNext()){
			int item = it.next();
			int itemFather = subKWTree.get(item).father.itemId;
			if(pattern.contains(itemFather)) leaves.remove(itemFather);
		}
		return leaves;
	}
	
	
	//get the rightmost path of the current pattern
	private List<Integer> getRightmostPath(Set<Integer> seq){
		List<Integer> RMPath = new ArrayList<Integer>();
		int last = -1;
		for(Iterator<Integer> it=seq.iterator();it.hasNext();){
			int current = it.next();
			if(last < current) last = current;
		} 
		while(subKWTree.get(last).father.itemId!=last){
			RMPath.add(last);
			last=subKWTree.get(last).father.itemId;
		}
		RMPath.add(1);
		return RMPath;
	}
	
	
	private Set<Integer> findCommon(Set<Integer> seq1,Set<Integer> seq2){
		
		if(seq1.equals(seq2)) {
			System.out.println(lattice.get(seq1).size()+"   fre: "+lattice.get(seq2).size());
			System.out.println(seq1.toString());
			System.out.println("checking hteee! ");
		}
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(seq1);
		
		for(Iterator<Integer> it =seq2.iterator(); it.hasNext();){
			int newItem = it.next();
			if(!set.contains(newItem))
				set.add(newItem);
		}
		

		return set;
	}
		
	
	private Set<Integer> intersect(Set<Integer> set1,Set<Integer>set2){
		Set<Integer> newSet = new HashSet<Integer>();
		if(set1.isEmpty() || set2.isEmpty()) return newSet;
		
		if(set1.size()>=set2.size()){
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
	
	
	//check one pattern is a maximal pattern whether or not in result set  
	private void checkMax(Set<Integer> seq){
		if(maximalPattern.isEmpty()) {
			maximalPattern.add(seq);
			return;
		}
		if(maximalPattern.contains(seq)) return;
	
		boolean flag = true;
		Iterator<Set<Integer>> keyIter = maximalPattern.iterator();
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
		if(flag==true) maximalPattern.add(seq);
	}
	
	public int getoutputSize(){
		return maximalPattern.size();
	}
	
	//print all PCs
	public void print(){
		Iterator<Set<Integer>> iter = maximalPattern.iterator();
		while(iter.hasNext()){
			Set<Integer> pattern = iter.next();
			Set<Integer> user = lattice.get(pattern);
			if(user.size()>20)
				System.out.println("pattern: "+pattern.toString()+" users size: "+user.size());
			else {
				System.out.println("pattern: "+pattern.toString()+" users: "+user.toString());
			}
		}
	}
	
	
}
