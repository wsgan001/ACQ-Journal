package algorithm.kwIndex.Query1;

import java.util.*;

import org.apache.commons.net.ftp.FTP;

import algorithm.FindCKSubG;
import algorithm.kwIndex.*;

/**
@author chenyankai
@Date	Aug 29, 2017
	This class is the index-based query algorithm.
	No-Apriori based (MAGRIN) 
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

						for(int y:tocheck) newPattern.add(y);
						
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
	
	
	//not fixed
	private List<Set<Integer>> incInitCross1(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();
		Queue<Set<Integer>> userQueue = new LinkedList<Set<Integer>>();
		Set<Integer> initPattern = new HashSet<Integer>();
		Set<Integer> initUsers = new HashSet<Integer>();
		initPattern.add(1);
		patternQueue.add(initPattern);
		userQueue.add(initUsers);
		
	
		while(!patternQueue.isEmpty()&&!userQueue.isEmpty()){
			//check whether a pattern has already reach the complete structure
//			boolean isComplete = true;
			
			Set<Integer> tocheck = patternQueue.poll();
			Set<Integer> users = userQueue.poll();
			
			
			int last = -1;
			for(Iterator<Integer> it=tocheck.iterator();it.hasNext();){
				last = it.next();
			}
			
			Set<Integer> RMPath = new HashSet<Integer>();//compressed rightmost path
			RMPath.add(1);
			if(last!=1) RMPath.add(1);
			//------------------------DEBUG------------------------------
			if(debug){
				System.out.println("checking pattern: "+tocheck.toString()
				+ "  users: "+users.toString());
				System.out.println("rightmost path "+last);
			}
			//----------------------END DEBUG----------------------------
			boolean outBreak = false;
			
			for(int x:RMPath){
				for(KWNode node:subKWTree.get(x).childList){
					int item = node.itemId;
					if(subKWTree.containsKey(item)&&!tocheck.contains(item)){
//						isComplete = false;
						Set<Integer> newPattern = new HashSet<Integer>();
						newPattern.add(item);
						if(x==1) newPattern.add(1);
						for(int y:tocheck){
							if(y!=x) newPattern.add(y);
						}
						Set<Integer> newUsers = fastObtainUser(item, users);
						
						if(!newUsers.isEmpty()){
							patternQueue.add(newPattern);
							userQueue.add(newUsers);
						}else {
							initCut.add(newPattern);
							initCut.add(tocheck);
							outBreak = true;
							break;
						}
					}				
				}				
			}
//			//means pattern-tocheck is the maximum pattern alrady
//			if(isComplete) {
//				initCut.add(tocheck);
//				output.put(tocheck, users);
//				break;
//			}
			if(outBreak) break;
		}	
		
		
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
			
			int previous = iter.next();
			if(pattern.size()==2){
				users = subKWTree.get(iter.next()).ktree.getKQueryId(k, queryId);
			}
			
			else{
				int current = -1;
				boolean first = true; 
				while(iter.hasNext()){
					current = iter.next();
					if(subKWTree.get(current).father.itemId != previous){
						if(first){
							users = subKWTree.get(previous).ktree.getKQueryId(k, queryId);
							first = false;
						}
						users.retainAll(subKWTree.get(previous).ktree.getKQueryId(k, queryId));
					}
					previous = current;
				}
				users.retainAll(subKWTree.get(previous).ktree.getKQueryId(k, queryId));
			}
			
			
			//compute CKC
			FindCKSubG findCKSG=new FindCKSubG(kwTree.graph, users, queryId);
			userSet =findCKSG.findCKSG();
			if(userSet==null) userSet = new HashSet<Integer>();
			lattice.put(pattern, userSet);
		}
		return userSet;
	}
		
	
	//obtain users using former userSet
	//using anti-monotonicity
	private Set<Integer> fastObtainUser(int newItem,Set<Integer> preUsers){		
		 Set<Integer> userSet = subKWTree.get(newItem).ktree.getKQueryId(k, queryId);
		//note that there is only one condition that preUsers are empty 
		//and still go into this function is that the root node
		 if(userSet.equals(preUsers) || preUsers.isEmpty()) return userSet;
		
		//compute CKC
		userSet.retainAll(preUsers);
		FindCKSubG findCKSG=new FindCKSubG(kwTree.graph, userSet, queryId);
		userSet =findCKSG.findCKSG();
		if(userSet==null) userSet = new HashSet<Integer>();
		
		return userSet;
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
					Set<Integer> set = newPattern(previous, seq);
					parentSeq.add(set);
				}
				previous = current;
			}
			
			//the last item must be the leaf node
			Set<Integer> set = newPattern(previous, seq);
			parentSeq.add(set);
		}
		return parentSeq;
	}
		
	
	//given a leaf item, generate a new pattern from the current pattern
	private Set<Integer> newPattern(int leafItem,Set<Integer> seq){
		Set<Integer> newPattern = new HashSet<Integer>();
		newPattern.add(1);
		for(int y:seq){
			if(y!=leafItem) newPattern.add(y);
		}
		KWNode leafNode = subKWTree.get(leafItem);
		if(leafNode.father.itemId!=1){
			newPattern.add(leafNode.father.itemId);
		}	
		return newPattern;
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
