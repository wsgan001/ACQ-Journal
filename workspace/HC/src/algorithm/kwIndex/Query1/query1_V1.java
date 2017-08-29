package algorithm.kwIndex.Query1;
import java.util.*;


import algorithm.FindCKSubG;
import algorithm.kwIndex.KTree;
import algorithm.kwIndex.KWNode;
import algorithm.kwIndex.KWTree;
/**
@author chenyankai
@Date	Aug 24, 2017
	Note that all patterns in this class are represented by the sequence of the whole P-tree
	compare to the Query1_v2 that patterns are denoted by the leaf nodes
*/
public class query1_V1 {
	private int queryId = -1;
	private int k = -1;
	private KWTree kwTree = null;
	//key:pattern  value users
	private Map<Set<Integer>,Set<Integer>> lattice = null;

	private Map<Integer,KWNode> subKWTree = null;
	
	private Map<Set<Integer>, Set<Integer>> output=null;
	
	private boolean debug = true;
	
	private Set<Set<Integer>> visited = null;
	
	
	
	public query1_V1(KWTree kwTree){
		this.kwTree = kwTree;
		this.subKWTree = new HashMap<Integer,KWNode>();
		this.lattice = new HashMap<Set<Integer>, Set<Integer>>();
		this.output = new HashMap<Set<Integer>, Set<Integer>>();
		
		this.visited=new HashSet<Set<Integer>>();
	}
	
	public void print(){
		Iterator<Set<Integer>> iter = output.keySet().iterator();
		while(iter.hasNext()){
			Set<Integer> pattern = iter.next();
			Set<Integer> user = output.get(pattern);
			System.out.println("pattern: "+pattern.toString()+" users: "+user);
		}
	}
	
	public void query(int queryId){
		this.queryId = queryId;
		
		getSubTree();
		List<Set<Integer>> initCut = decInitCross();
		if(initCut.size()==1) return;
		
		expandCross(initCut.get(0),initCut.get(1));
				
	}
	
	
	
	//induce a KW-tree subtree and store it in a map
	private void getSubTree(){
		KWNode root = null;
		int count=0;
		for(KWNode node: kwTree.getHeadMap().get(queryId)){			
			//------------------------DEBUG------------------------------
//			if(debug) System.out.println("leaf node: "+node.itemId);
			//----------------------END DEBUG----------------------------
			while(node.father!=node){
				subKWTree.put(node.itemId,node);	
				node = node.father;
				root = node;
			}
		}
		subKWTree.put(root.itemId, root);
	}
	
	private void printMap(){
		Iterator<Integer> iter = subKWTree.keySet().iterator();
		while(iter.hasNext()){
			KWNode node = subKWTree.get(iter.next());
			System.out.println("item id: "+node.itemId);
		}
		
	}
	
	//search a feasible solution in a decremental manner
	private List<Set<Integer>> decInitCross(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		Set<Integer> pattern= new HashSet<Integer>();
		for(KWNode node:kwTree.getHeadMap().get(queryId)) pattern.add(node.itemId);
		Set<Integer> users = miniObtainUser(pattern);	
		if(!users.isEmpty()){
			output.put(pattern, users);
			initCut.add(pattern);
		}
		
		else{
			Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();
			patternQueue.add(pattern);
			boolean tag = true;
			while(!patternQueue.isEmpty()&&tag){
				Set<Integer> tocheck = patternQueue.poll();
				for(int leaf:tocheck){
					KWNode leafNode = subKWTree.get(leaf);
					if(leafNode.father.itemId!=1) {//not the root node
						Set<Integer> parentPattern = new HashSet<Integer>();
						parentPattern.add(leafNode.father.itemId);
						for(int x:tocheck) if(x!=leaf) parentPattern.add(x);
						Set<Integer> newUser = miniObtainUser(parentPattern);
						if(!newUser.isEmpty()){
							output.put(parentPattern, newUser);
							initCut.add(tocheck);
							initCut.add(parentPattern);
							tag = false; 
							break;
						}
						patternQueue.add(parentPattern);
					}

				}			
			}			
		}
		return initCut;
	}
	
	
	//obtain all users according to the leaf items instead of the whole P-tree
	private Set<Integer> miniObtainUser(Set<Integer> leafPattern){
		Set<Integer> users = new HashSet<Integer>();
		
		Iterator<Integer> iter=leafPattern.iterator();
		int first = iter.next();
//		if(first == 1) return users;
		users=subKWTree.get(first).ktree.getKQueryId(k, queryId);
		while(iter.hasNext()){
			int item = iter.next();
			users.retainAll(subKWTree.get(item).ktree.getKQueryId(k, queryId));
		}
		//compute CKC
		FindCKSubG findCKSG=new FindCKSubG(kwTree.graph, users, queryId);
		users =findCKSG.findCKSG();
		if(users==null) return new HashSet<Integer>();
		return users;
		 
	}
	
	
	
	//expand a cross 
	private void expandCross(Set<Integer> inFreSeq, Set<Integer> freSeq){
		if(visited.contains(inFreSeq)&&visited.contains(freSeq)) return;
		visited.add(inFreSeq); visited.add(freSeq);
		
		Set<Set<Integer>> parentSeqSet=parentSeq(inFreSeq);
		if(parentSeqSet.isEmpty()) return;
		
		for(Iterator<Set<Integer>> it=parentSeqSet.iterator();it.hasNext();){
			Set<Integer> parentSeq = it.next();
			Set<Integer> userSet = obtainUserSet(parentSeq);
			if(!userSet.isEmpty()){
				checkMax(parentSeq);
				Set<Set<Integer>> childOfParentSeq = childSeq(parentSeq);
				
				for(Iterator<Set<Integer>> iter=childOfParentSeq.iterator();iter.hasNext();){
					Set<Integer> element = iter.next();
					Set<Integer> userOfElement = obtainUserSet(element);
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
					Set<Integer> userOfElement = obtainUserSet(element);
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
			if(node==null) continue;
 			for(KWNode child:node.childList){
				int item = child.itemId;
				if(!seq.contains(item)&&subKWTree.containsKey(item)){
					Set<Integer> nextSeq = new HashSet<Integer>();
					nextSeq.addAll(seq);
					nextSeq.add(item);
					childSeq.add(nextSeq);
				}	
			}
		}
		return childSeq;
	}
	
	//obtain all parent patterns of the current pattern
	private Set<Set<Integer>> parentSeq(Set<Integer> seq){
		Set<Set<Integer>> parentSeq = new HashSet<Set<Integer>>();
		if(seq.size()==1) return parentSeq;
		Set<Integer> leaf = getLeaves(seq);
		for(int x:leaf){
			Set<Integer> set=new HashSet<Integer>();
			for(int y:seq){
				if(y!=x) set.add(y);
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
	
	

	
	
	
	//obtain a userSet with a specific seq
	//if a seq corresponds to no community 		------>  userSet.size=0
	//if lattice does not contain this pattern 	------>  userSet=null
	private Set<Integer> obtainUserSet(Set<Integer> seq){
		Set<Integer> userSet =lattice.get(seq);
		System.out.println("now checking seq: "+seq.toString());
		if(userSet==null){
			userSet=new HashSet<Integer>();
			Set<Integer> leaf = getLeaves(seq);
			
			Iterator<Integer> iter=leaf.iterator();
			int x =iter.next();
			if(x==1) return userSet;
			Set<Integer> users=subKWTree.get(x).ktree.getKQueryId(k, queryId);
			
			//------------------------DEBUG------------------------------
			if(debug) System.out.println("initial users: "+users.toString() );
			//----------------------END DEBUG----------------------------
			
			while(iter.hasNext()){
				int item = iter.next();
				users.retainAll(subKWTree.get(item).ktree.getKQueryId(k, queryId));
				//------------------------DEBUG------------------------------
				if(debug) System.out.println("now users: "+users.toString() );
				//----------------------END DEBUG----------------------------
			}
				//compute CKC
				FindCKSubG findCKSG=new FindCKSubG(kwTree.graph, users, queryId);
				userSet = findCKSG.findCKSG();
				if(userSet==null) userSet = new HashSet<Integer>();
				lattice.put(seq, userSet);
				
				//------------------------DEBUG------------------------------
				if(debug) System.out.println("final CKC: "+userSet.toString() );
				//----------------------END DEBUG----------------------------
			
		}
		return userSet;
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
	
	
	
	//O(n) time complexity to obtain all leaf items
	private Set<Integer> getLeaves(Set<Integer> seq){
		Set<Integer> leaves = new HashSet<Integer>();
		if(seq.size()==1) {
			leaves.add(1);//root item
			return leaves;
		}
		
		Iterator<Integer> iter = seq.iterator();
		int prevOne = iter.next();
		while(iter.hasNext()){
			int curOne = iter.next();
			if(subKWTree.get(curOne).father.itemId!=prevOne)
				leaves.add(prevOne);
			prevOne=curOne;
		}
		leaves.add(prevOne);//add the last item
		return leaves;
	}
	
}
