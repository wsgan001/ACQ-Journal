package algorithm.kwIndex.Query1;

import java.util.*;
import algorithm.FindCKSubG;
import algorithm.kwIndex.KWNode;
import algorithm.kwIndex.KWTree;

/**
@author chenyankai
@Date	Aug 29, 2017
	This class is the index-based query algorithm.
	Details are derived from Query1_V1 and optimized.
	
	Difference between V1 and V2 is that V2 :
	(1) dynamic computing userSets in obtain childSeq function.
*/
public class Query1_V2 {
	private int queryId = -1;
	private int k = -1;
	private KWTree kwTree = null;
	private Map<Integer,KWNode> subKWTree = null;
	//key:pattern  value users
	private Map<Set<Integer>,Set<Integer>> lattice = null;
	private Map<Set<Integer>, Set<Integer>> output=null;
	private Set<Set<Integer>> visited = null;
	
	private boolean debug = true;
	
	public Query1_V2(KWTree kwTree){
		this.kwTree = kwTree;
		this.subKWTree = new HashMap<Integer,KWNode>();
		this.lattice = new HashMap<Set<Integer>, Set<Integer>>();
		this.output = new HashMap<Set<Integer>, Set<Integer>>();
		this.visited=new HashSet<Set<Integer>>();
		
	}
	
	//main function
	public void query(int queryId){
		this.queryId = queryId;
		getSubKWTree();
		
//		List<Set<Integer>> initCut = decInitCross();
		List<Set<Integer>> initCut = incInitCross();
		if(initCut.size()==1) return;
		
		//expandCross
		expandCross(initCut.get(0),initCut.get(1));
				
	}
	
	//induce a KW-tree subtree and store it in a map
	private void getSubKWTree(){
		KWNode root = null;
		for(KWNode node: kwTree.getHeadMap().get(queryId)){			
			while(node.father!=node){
				subKWTree.put(node.itemId,node);	
				node = node.father;
				root = node;
			}
		}
		subKWTree.put(root.itemId, root);
	}
			
	//search a feasible solution in a decremental manner
	private List<Set<Integer>> decInitCross(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		Set<Integer> pattern= new HashSet<Integer>();
		pattern.add(1);//root node
		for(KWNode node:kwTree.getHeadMap().get(queryId)) pattern.add(node.itemId);
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
				for(int leaf:tocheck){
					if(leaf==1) continue;
					Set<Integer> parentPattern = new HashSet<Integer>();
					for(int x:tocheck) {
						
						if(x!=leaf) parentPattern.add(x);
					}
					KWNode leafNode = subKWTree.get(leaf);
					if(leafNode.father.itemId!=1)
						parentPattern.add(leafNode.father.itemId);
					
					Set<Integer> newUser = obtainUser(parentPattern);
					if(!newUser.isEmpty()){
						output.put(parentPattern, newUser);
						lattice.put(parentPattern, newUser);
						initCut.add(tocheck);
						initCut.add(parentPattern);
						tag = false; 
						break;
					}
					patternQueue.add(parentPattern);
				}			
			}			
		}
		return initCut;
	}
		
	
	//search a feasible solution in an incremental manner (BFS based)
	private List<Set<Integer>> incInitCross(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();

		for(KWNode node:subKWTree.get(1).childList){
			int item = node.itemId;
			if(!subKWTree.containsKey(item)) continue;
			
			Set<Integer> pattern = new HashSet<Integer>();
			pattern.add(1);//the root node
			pattern.add(item);
			Set<Integer> userSet = obtainUser(pattern);
			if(!userSet.isEmpty()) {
				patternQueue.add(pattern);
			}
		}
		
		boolean tag=true;
		while(!patternQueue.isEmpty()&&tag){
			Set<Integer> tocheck = patternQueue.poll();
			
			//if already reach the maximum pattern then return
			if(tocheck.size()==subKWTree.size()) {
				Set<Integer> userSet = obtainUser(tocheck);
				output.put(tocheck, userSet);
				lattice.put(tocheck, userSet);
				initCut.add(tocheck);
				break;
			}
			
			for(Iterator<Integer> it=tocheck.iterator();it.hasNext()&&tag;){
				int x = it.next();
				for(KWNode node:subKWTree.get(x).childList){
					int item = node.itemId;
					if(subKWTree.containsKey(item)&&!tocheck.contains(item)){
						Set<Integer> newPattern= new HashSet<Integer>();
						newPattern.add(item);
						//if x==1 add it without replacing it
						if(x==1) newPattern.add(1);
						
						for(int y:tocheck){
							if(y!=x) newPattern.add(y);
						}
						Set<Integer> UserSet = obtainUser(tocheck);
						Set<Integer> users = subKWTree.get(item).ktree.getKQueryId(k, queryId);
						if(!users.equals(UserSet)){
							users.retainAll(UserSet);
							//compute CKC
							FindCKSubG findCKSG=new FindCKSubG(kwTree.graph, users, queryId);
							UserSet =findCKSG.findCKSG();
							if(UserSet==null) {
								UserSet = new HashSet<Integer>();
								lattice.put(newPattern, UserSet);
								initCut.add(newPattern);
								initCut.add(tocheck);
								tag=false;
								break;
							}else{
								lattice.put(newPattern, UserSet);
								patternQueue.add(newPattern);
							}	
						}
					}
				}
			}
			
		}
		
		return initCut;
	}
	
	
	
	//obtain all users according to the leaf items instead of the whole P-tree
	//if a seq corresponds to no community 		------>  userSet.size=0
	//if lattice does not contain this pattern 	------>  userSet=null
	private Set<Integer> obtainUser(Set<Integer> leafPattern){
		Set<Integer> userSet = lattice.get(leafPattern);
		if(userSet == null){
			userSet = new HashSet<Integer>();
			Iterator<Integer> iter=leafPattern.iterator();
			iter.next();//skip the root node
			
			int first = iter.next();
			Set<Integer> users = subKWTree.get(first).ktree.getKQueryId(k, queryId);
			while(iter.hasNext()){
				int item = iter.next();
				users.retainAll(subKWTree.get(item).ktree.getKQueryId(k, queryId));
			}
			//compute CKC
			FindCKSubG findCKSG=new FindCKSubG(kwTree.graph, users, queryId);
			userSet =findCKSG.findCKSG();
			if(userSet==null) userSet = new HashSet<Integer>();
			lattice.put(leafPattern, userSet);
		}
		return userSet;
	}
		
			
	//expand a cross 
	private void expandCross(Set<Integer> inFreSeq, Set<Integer> freSeq){
		if(visited.contains(inFreSeq)&&visited.contains(freSeq)) return;
		visited.add(inFreSeq); visited.add(freSeq);
	
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
		
		
	//obtain all child patterns of the current simplified pattern
	private Set<Set<Integer>> childSeq(Set<Integer> seq){
		Set<Set<Integer>> childSeq = new HashSet<Set<Integer>>();
		for(int x:seq){
			KWNode node = subKWTree.get(x);
	 		for(KWNode child:node.childList){
	 			int item = child.itemId;
				if(!seq.contains(item)&&subKWTree.containsKey(item)){
					Set<Integer> nextSeq = new HashSet<Integer>();
					nextSeq.add(item);
					
					//if x==1 add it without replacing it
					if(x==1) nextSeq.add(1);
					
					for(int y:seq){
						 
						if(y!=x) nextSeq.add(y);
					}
					childSeq.add(nextSeq);
					
					//optimized 
					Set<Integer> userSet = lattice.get(seq);
					Set<Integer> users = subKWTree.get(item).ktree.getKQueryId(k, queryId);
					if(!users.equals(userSet)){
						users.retainAll(userSet);
						//compute CKC
						FindCKSubG findCKSG=new FindCKSubG(kwTree.graph, users, queryId);
						userSet =findCKSG.findCKSG();
						if(userSet==null) userSet = new HashSet<Integer>();	
					}
					lattice.put(nextSeq, userSet);
				}	
			}
		}
		return childSeq;	
	}

	
	//obtain all parent patterns of the current simplified pattern
	private Set<Set<Integer>> parentSeq(Set<Integer> seq){
		Set<Set<Integer>> parentSeq = new HashSet<Set<Integer>>();
		for(int x:seq){
			if(x==1) continue;
			
			Set<Integer> set=new HashSet<Integer>();
			for(int y:seq){
				if(y!=x) set.add(y);
			}
			
			KWNode leafNode = subKWTree.get(x);
			if(leafNode.father.itemId!=1){
				set.add(leafNode.father.itemId);
			}	
			
			parentSeq.add(set);
		}
		return parentSeq;
	}
		
		
	private Set<Integer> findCommon(Set<Integer> seq1,Set<Integer> seq2){
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(seq1);
		set.addAll(seq2);
		return set;
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
