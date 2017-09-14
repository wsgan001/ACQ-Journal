package algorithm.kwIndex.Query1;

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
	
	Difference between V1 and V2 is that V2 :
	(1) dynamic computing userSets in obtain childSeq function.
*/
public class Query1_V1 {
	private int[][] graph = null;
	private int queryId = -1;
	private int k = -1;
	private Map<Integer, List<KWNode>> headList = null;
	private List<KWNode> leafList = null;
	private Map<Integer,KWNode> subKWTree = null;
	//key:pattern  value users
	private Map<Set<Integer>,Set<Integer>> lattice = null;
	private Map<Set<Integer>, Set<Integer>> output=null;
	private Set<Set<Integer>> visited = null;
	
	private boolean debug = true;
	
	public Query1_V1(int[][] graph,Map<Integer, List<KWNode>> headList){
		this.graph = graph;
		this.leafList = new LinkedList<KWNode>();
		this.headList = headList;
		this.subKWTree = new HashMap<Integer,KWNode>();
		this.lattice = new HashMap<Set<Integer>, Set<Integer>>();
		this.output = new HashMap<Set<Integer>, Set<Integer>>();
		this.visited=new HashSet<Set<Integer>>();
		this.k=Config.k;
	}
	
	//main function
	public void query(int queryId){
		this.queryId = queryId;
		induceSubKWTree();
		System.out.println("step1 getsubtree finished");
		
		List<Set<Integer>> initCut = decInitCross();
//		List<Set<Integer>> initCut = incInitCross();
		jumpInitCross();
		if(initCut.isEmpty()) {
			System.out.println("No community!");
			return;
		}
		if(initCut.size()==1) return;
		
		//expandCross
		expandCross(initCut.get(0),initCut.get(1));
				
	}
	
	//induce a KW-tree subtree and store it in a map
	private void induceSubKWTree(){
		Map<Integer, List<Integer>> childMap = new HashMap<Integer,List<Integer>>();
		KWNode root =new KWNode(1);
		subKWTree.put(1, root);
		for(KWNode currentNode:headList.get(queryId)){
			while(currentNode.itemId != 1){
//				System.out.println("checking: "+currentNode.itemId);
				Set<Integer> vertexSet = currentNode.getCKCore(k, queryId);
				if(!vertexSet.isEmpty()){
//					System.out.println("inside: "+currentNode.itemId);
					int currenItem = currentNode.itemId;
					if(!subKWTree.containsKey(currenItem)){
						KWNode newNode = new KWNode(currenItem);
						newNode.tmpVertexSet = vertexSet;
						subKWTree.put(currenItem, newNode);
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
		if(debug) 		System.out.println(root.toString(""));
		//----------------------END DEBUG---------------------------	
	}
	
	
	//get the leaf item in the subtree which has the long path from bottom up
	private int longestPath(){
		int longestleaf = -1;
		int maxLen = 0;
		for(KWNode node:headList.get(queryId)){
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
			output.put(pattern, users);
			lattice.put(pattern, users);
			initCut.add(pattern);
		}
		else{
			Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();
			patternQueue.add(pattern);
			boolean tag = true;
			
			while(!patternQueue.isEmpty() && tag){
				Set<Integer> tocheck = patternQueue.poll();
				Iterator<Integer> iter = tocheck.iterator();
				int previous = iter.next(); 
//				System.out.println(tocheck.size());
				if(tocheck.size()==2) return initCut;
				else{
					int current = -1;
					//looking for the leaf item and delete it
					while(iter.hasNext()){
						current = iter.next();
						if(subKWTree.get(current).father.itemId != previous){
							Set<Integer> set = new HashSet<Integer>();
							
							for(int y:tocheck){
								if(y!= previous) set.add(y);
							}
							//obtain users
							Set<Integer> newUser = obtainUser(set);
							if(!newUser.isEmpty()){
								output.put(set, newUser);
								lattice.put(set, newUser);
								initCut.add(tocheck);
								initCut.add(set);
								tag = false; 
								break;
							}
							patternQueue.add(set);
						}
						previous = current;
					}
					
					//the last item must be a leaf item
					Set<Integer> set = new HashSet<Integer>();	
					for(int y:tocheck){
						if(y!= previous) set.add(y);
					}
					//obtain users
					Set<Integer> newUser = obtainUser(set);
					if(!newUser.isEmpty()){
						output.put(set, newUser);
						lattice.put(set, newUser);
						initCut.add(tocheck);
						initCut.add(set);
						tag = false; 
						break;
					}
					patternQueue.add(set);
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

		for(KWNode node:subKWTree.get(1).childList){
			int item = node.itemId;
			if(!subKWTree.containsKey(item)) continue;
			
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
			Set<Integer> tocheck = patternQueue.poll();
			Set<Integer> currentUser = userQueue.poll();
			//if already reach the maximum pattern then return
			if(tocheck.size()==subKWTree.size()) {
				output.put(tocheck, currentUser);
				lattice.put(tocheck, currentUser);
				initCut.add(tocheck);
				break;
			}
			List<Integer> RMPath = getRightmostPath(tocheck);
			for(int x:RMPath){
				for(KWNode node:subKWTree.get(x).childList){
					int item = node.itemId;
					if(subKWTree.containsKey(item)&&!tocheck.contains(item)){
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
								lattice.put(newPattern, newUsers);
								initCut.add(newPattern);
								initCut.add(tocheck);
								tag=false;
								break;
							}else{
								newUsers = new HashSet<Integer>();
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
			Set<Integer> users = new HashSet<Integer>();
			Iterator<Integer> iter=pattern.iterator();
			
			int previous = iter.next();//the root item 
			if(pattern.size()==2){
				users.addAll(getUsersInKTree(iter.next()));
			}
			
			else{
				int current = -1;
				boolean first = true; 
				while(iter.hasNext()){
					current = iter.next();
					if(subKWTree.get(current).father.itemId != previous&&previous!=1){
						//initialize the users using the first item
						if(first){	
							users.addAll(getUsersInKTree(previous));
							first = false;
						}
						else{
							users= intersect(users, getUsersInKTree(previous));
						}
					}
					previous = current;
				}
				users= intersect(users, getUsersInKTree(previous));
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
	
	
	//expand a cross 
	private void expandCross(Set<Integer> inFreSeq, Set<Integer> freSeq){
		if(visited.contains(inFreSeq)&&visited.contains(freSeq)) return;
		visited.add(inFreSeq); visited.add(freSeq);
		
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
				
				for(Iterator<Set<Integer>> iter=childOfParentSeq.iterator();iter.hasNext();){
					Set<Integer> element = iter.next();
					Set<Integer> userOfElement = obtainUser(element);
					if(userOfElement.isEmpty()){
						expandCross(element, parentSeq);
					}else{
						Set<Integer> commonChild = findCommon(inFreSeq, element);
						expandCross(commonChild, element);
					}
				}
			}else{
				Set<Set<Integer>> parentOfParentSeq = parentSeq(parentSeq);
				for(Iterator<Set<Integer>> iter = parentOfParentSeq.iterator();iter.hasNext();){
					Set<Integer> element = iter.next();
					Set<Integer> userOfElement = obtainUser(element);
					if(!userOfElement.isEmpty()){
						expandCross(parentSeq, element);
					}
				}
			}	
		}		
	}
		
		
	//obtain all child patterns of the current pattern
	private Set<Set<Integer>> childSeq(Set<Integer> seq){
		Set<Set<Integer>> childSeq = new HashSet<Set<Integer>>();
		for(int x:seq){
			KWNode node = subKWTree.get(x);
	 		for(KWNode child:node.childList){
	 			int item = child.itemId;
				if(!seq.contains(item)&&subKWTree.containsKey(item)){
					Set<Integer> nextSeq = new HashSet<Integer>();
					nextSeq.add(item);
					for(int y:seq) nextSeq.add(y);
					childSeq.add(nextSeq);
					
					
					//optimized 
					Set<Integer> userSet = lattice.get(seq);
					Set<Integer> users = getUsersInKTree(item);
					if(!userSet.equals(users)){
						Set<Integer> newUsers = new HashSet<Integer>();
						newUsers=intersect(users, userSet);
						//compute CKC
						FindCKSubG findCKSG=new FindCKSubG(graph, newUsers, queryId);
						newUsers =findCKSG.findCKSG();
						if(newUsers==null) newUsers = new HashSet<Integer>();	
						lattice.put(nextSeq, newUsers);
					}else {
						lattice.put(nextSeq, userSet);
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
		Iterator<Integer> iter = seq.iterator();
		int previous = iter.next();
		if(seq.size()==2){
			return parentSeq;
		}
		else{
			int current = -1;
			while(iter.hasNext()){
				current = iter.next();
				if(subKWTree.get(current).father.itemId != previous){
					//then previous must be a leaf item
					Set<Integer> set = new HashSet<Integer>();
					for(int y:seq){
						if(y != previous) set.add(y);
					}
					parentSeq.add(set);
				}
				previous = current;
			}
			
			//the last item must be the leaf node
			Set<Integer> set = new HashSet<Integer>();

			for(int y:seq){
				if(y != previous) set.add(y);
			}
			parentSeq.add(set);
		}
		return parentSeq;
	}
			
	
	//get the rightmost path of the current pattern
	private List<Integer> getRightmostPath(Set<Integer> seq){
		List<Integer> RMPath = new ArrayList<Integer>();
		int last = -1;
		for(Iterator<Integer> it=seq.iterator();it.hasNext();){
			last = it.next();
		} 
		while(subKWTree.get(last).father.itemId!=last){
			RMPath.add(last);
			last=subKWTree.get(last).father.itemId;
		}
		RMPath.add(1);
		return RMPath;
	}
	
	
	private Set<Integer> findCommon(Set<Integer> seq1,Set<Integer> seq2){
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
		if(output.isEmpty()) {
			output.put(seq, lattice.get(seq));
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
		if(flag==true) output.put(seq, lattice.get(seq));
	}
	

	//print all PCs
	public void print(){
		Iterator<Set<Integer>> iter = output.keySet().iterator();
		while(iter.hasNext()){
			Set<Integer> pattern = iter.next();
			Set<Integer> user = output.get(pattern);
			System.out.println("pattern: "+pattern.toString()+" users: "+user);
		}
	}
	
	
}