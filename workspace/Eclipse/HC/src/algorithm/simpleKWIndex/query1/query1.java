package algorithm.simpleKWIndex.query1;

import java.util.*;
import java.util.Map.Entry;
import algorithm.FindCKSubG;
import algorithm.simpleKWIndex.KNode;
import config.Config;
/**
@author chenyankai
@Date	Sep 3, 2017
	based on simplified KWtree index 
	search tree patterns in MARGIN-based(the first not-Apriori algo)
	
*/
public class query1{
	private int[][] graph = null;
	private int queryId = -1;
	private int k = Config.k;
	private Map<Integer, KNode> subKWTree = null;
	private Map<Integer, List<KNode>> headList = null;
	private List<Integer> leaves = null;
	//key:pattern  value users
	private Map<Set<Integer>,Set<Integer>> lattice = null;
	private Set<Set<Integer>> maximalPattern = null;
	private Map<Set<Integer>, Set<Set<Integer>>> visited = null;
	
	
	private boolean debug = false;
	
	public query1(int[][] graph, Map<Integer, List<KNode>> headList){
		this.graph = graph;
		this.headList = headList;
	}
	
	public void query(int queryId){
		this.queryId = queryId;
		this.maximalPattern = new HashSet<Set<Integer>>();
		this.subKWTree = new HashMap<Integer,KNode>();
		
		this.lattice = new HashMap<Set<Integer>,Set<Integer>>();
		this.leaves = new ArrayList<Integer>();	
		this.visited = new HashMap<Set<Integer>,Set<Set<Integer>>>();
		
		induceSubKWTree();
		System.out.println("induce done."+subKWTree.size());
		
		long time = System.nanoTime(); 
		List<Set<Integer>> initCut = incInitCut();	
//		List<Set<Integer>> initCut = decInitCut();
		System.out.println("find init cut time: "+(System.nanoTime()-time)/1000);
		
		if(initCut.size()==1) {
			print();
			return;
		}
		if(initCut.isEmpty()) {
			System.out.println("No community!");
			return;
		}
		
		System.out.println("entering expandCut.");
		
		long time1 = System.nanoTime(); 
		iterativeExpandCut(initCut.get(0), initCut.get(1));
		System.out.println("expand cut time: "+(System.nanoTime()-time1)/1000);
		print();
		
		
		
	}
	
	
	private void induceSubKWTree(){
		this.subKWTree = new HashMap<Integer, KNode>();
		Map<Integer, List<Integer>> childMap = new HashMap<Integer, List<Integer>>();
		boolean containRoot = false;
		for(KNode currentNode:headList.get(queryId)){
			leaves.add(currentNode.item);
			
			while(currentNode.item!=1){
				KNode node = new KNode(currentNode.item);
				node.vertexCore = currentNode.vertexCore;
				node.folder = currentNode.folder;
				subKWTree.put(node.item, node);
				int father = currentNode.father.item;
				if(childMap.containsKey(father)) childMap.get(father).add(node.item);
				else{
					List<Integer> list = new ArrayList<Integer>();
					list.add(node.item);
					childMap.put(father, list);
				}
				currentNode = currentNode.father;
			}
			if(!containRoot) {
				KNode root = new KNode(currentNode.item);
				subKWTree.put(1, root);
				containRoot = true; 
			}
		}
		
		Iterator<Integer> iter = childMap.keySet().iterator();
		while(iter.hasNext()){
			int father=iter.next();
			KNode fatherNode = subKWTree.get(father);
			for(int x:childMap.get(father)) {
				subKWTree.get(x).father = fatherNode;
				fatherNode.addChild(subKWTree.get(x));
			}
		}
	}
	
	
	

	private List<Set<Integer>> decInitCut(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		return initCut;
	}
	
	
	private List<Set<Integer>> incInitCut(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>(); 
		
		for(KNode node:subKWTree.get(1).childList){
			int item = node.item;
			if(!subKWTree.containsKey(item)|| !node.isContains(queryId, k)) continue;
			
			Set<Integer> pattern = new HashSet<Integer>();
			pattern.add(1); pattern.add(item);
			Set<Integer> userSet = obtainUsersInSingleNode(item);
			if(!userSet.isEmpty()){
				patternQueue.add(pattern);
				lattice.put(pattern, userSet);
			}	
		}
		
		boolean tag = true;
		while(!patternQueue.isEmpty()&&tag){
			System.out.println("patternQueue size: "+patternQueue.size());
			
			Set<Integer> checkPattern = patternQueue.poll();
			Set<Integer> checkUsers = lattice.get(checkPattern);
			if(checkPattern.size()==subKWTree.size()){
				maximalPattern.add(checkPattern);
				initCut.add(checkPattern);
				break;
			}
			List<Integer> RMPath = getRightmostPath(checkPattern);
			for(int x:RMPath){
				if(tag==false) break;
				for(KNode node:subKWTree.get(x).childList){
					int item = node.item;
					if(!checkPattern.contains(item)){
						Set<Integer> newPattern = new HashSet<Integer>();
						Set<Integer> newUsers = new HashSet<Integer>();
						newPattern.add(item);
						for(int y:checkPattern) newPattern.add(y);
						Set<Integer> CKCofNewItem = obtainUsersInSingleNode(item);
						if(!checkUsers.equals(CKCofNewItem)){
							newUsers = intersect(checkUsers, CKCofNewItem);
							FindCKSubG findCKSubG = new FindCKSubG(graph, newUsers, queryId);
							newUsers = findCKSubG.findCKSG();
							if(newUsers==null){
								newUsers = new HashSet<Integer>();
								lattice.put(newPattern, newUsers);
								maximalPattern.add(checkPattern);
								initCut.add(newPattern);
								initCut.add(checkPattern);
								tag = false;
								break;
							}else{
								lattice.put(newPattern, newUsers);
								patternQueue.add(newPattern);
							}
						}
						else {
							lattice.put(newPattern, CKCofNewItem);
							patternQueue.add(newPattern);
						}
					}
				}
			}
		}		
		return initCut;
	}
	
	

	private void iterativeExpandCut(Set<Integer> infreSeq, Set<Integer> freSeq){
		Queue<Set<Integer>> infreQueue = new LinkedList<Set<Integer>>();   
		infreQueue.add(infreSeq);
		
		while(!infreQueue.isEmpty()){
			Set<Integer> infreToCheck = infreQueue.poll();
			System.out.println("expand cut: "+infreQueue.size());
			Set<Set<Integer>> parentofSeqSet = parentSeq(infreToCheck);
			if(parentofSeqSet.isEmpty()) continue;
			for(Iterator<Set<Integer>> it=parentofSeqSet.iterator();it.hasNext();){
				Set<Integer> parentSeq = it.next();
				Set<Integer> userSet = obtainUsersofPattern(parentSeq);
				if(userSet==null) System.out.println(parentSeq.toString());
				if(!userSet.isEmpty()){
					checkMax(parentSeq);
					
					Set<Set<Integer>> childOfParentSeq = childSeq(parentSeq);
					if(childOfParentSeq.isEmpty()) continue;
					
					for(Iterator<Set<Integer>> iter = childOfParentSeq.iterator();iter.hasNext();){
						Set<Integer> element = iter.next();
						Set<Integer> userOfElement = lattice.get(element);
						if(userOfElement.isEmpty()){
							
							if(!hasVisited(element, parentSeq)){infreQueue.add(element);}
						
						}else{
							Set<Integer> commonChild = commonChild(infreToCheck, element);
							if(!lattice.containsKey(commonChild)){
								Set<Integer> empty = new HashSet<Integer>();
								lattice.put(commonChild, empty);
							}
							
							if(!hasVisited(commonChild, element)){infreQueue.add(commonChild);}
						}	
					}	
				}else{
					Set<Set<Integer>> parentOfParentSeq = parentSeq(parentSeq) ;
					for(Iterator<Set<Integer>> iter = parentOfParentSeq.iterator();iter.hasNext();){
						Set<Integer> element = iter.next();
						Set<Integer> userOfElement = obtainUsersofPattern(element);
						if(!userOfElement.isEmpty()){
							if(! lattice.get(parentSeq).isEmpty()) System.out.println("case 3");
							
							if(!hasVisited(parentSeq, element)){infreQueue.add(parentSeq);}
						}
					}		
				}	
			}		
		}
	}
	
	
	
	
	//obtain all the child pattern 
	private Set<Set<Integer>> childSeq(Set<Integer> seq){
		Set<Set<Integer>> childSeq = new HashSet<Set<Integer>>();
		if(seq.size()==subKWTree.size()) return childSeq;
			
		for(int x:seq){
			KNode node = subKWTree.get(x);
			for(KNode child:node.childList){
				int item = child.item;
				if(!seq.contains(item)){
					Set<Integer> nextSeq = new HashSet<Integer>(seq.size()+1);
					nextSeq.add(item);
					for(int y:seq) nextSeq.add(y);
					childSeq.add(nextSeq);
					
//					//optimized 
					if(lattice.containsKey(nextSeq)) continue;
				
					Set<Integer> userSet = lattice.get(seq);
					Set<Integer> users = obtainUsersInSingleNode(item);
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
	
	
	private Set<Set<Integer>> parentSeq(Set<Integer> seq){
		Set<Set<Integer>> parentSeq = new HashSet<Set<Integer>>(); 
		if(seq.size()==2) return parentSeq;
			
		Set<Integer> leaves = getLeaves(seq); 
		for(Iterator<Integer> it = leaves.iterator();it.hasNext(); ){
			int leaf = it.next();
			Set<Integer> nextSeq = new HashSet<Integer>(seq.size()-1);
			for(int x:seq){
				if(x != leaf) nextSeq.add(x);
			}
			parentSeq.add(nextSeq);
		}
		return parentSeq;
	}
		
		
	private Set<Integer> commonChild(Set<Integer> seq1,Set<Integer> seq2){		
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(seq1);
		
		for(Iterator<Integer> iter = seq2.iterator();iter.hasNext();){
			int newItem = iter.next();
			if(!set.contains(newItem)) set.add(newItem);
		}	
		return set;
	}		
		
	private Set<Integer> obtainUsersInSingleNode(int item){		
		Set<Integer> CKC = new HashSet<Integer>();
		if(!subKWTree.containsKey(item)) System.out.println("not contains! "+item );
		Map<Integer, Integer> vertexCore = subKWTree.get(item).vertexCore;
		
		if(vertexCore.get(queryId)<k) return CKC;
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(queryId);
		CKC.add(queryId);
		
		while(!queue.isEmpty()){
			int current = queue.poll();
			for(int i=0;i<graph[current].length;i++){
				int nghbr = graph[current][i];
				if(!CKC.contains(nghbr)&& vertexCore.containsKey(nghbr) &&vertexCore.get(nghbr)>=k){
					queue.add(nghbr);
					CKC.add(nghbr);	
				}
			}
		}
		return CKC;
	} 
	

	private Set<Integer> obtainUsersofPattern(Set<Integer> pattern){
		Set<Integer> userSet = lattice.get(pattern);
		if(userSet==null){
			Set<Integer> leaves = getLeaves(pattern);
			Set<Integer> users = null;
			Iterator<Integer> iter = leaves.iterator();
			while(iter.hasNext()){
				int leaf = iter.next();
				if(users == null){
					users = obtainUsersInSingleNode(leaf);
				}
				else{
					users = IntersectofKNode(users, subKWTree.get(leaf));
				}
				if(users.isEmpty()) break;
			}
			
			//compute CKC
			if(!users.isEmpty()){
				FindCKSubG findCKSG=new FindCKSubG(graph, users, queryId);
				userSet = findCKSG.findCKSG();
				if(userSet==null) System.out.println("case 1");
				if(userSet==null) userSet = new HashSet<Integer>();
			}else userSet = users;
			lattice.put(pattern, userSet);
		}
		return userSet;
	}

	
	
	
	
	
	//get leaves of current pattern
	private Set<Integer> getLeaves(Set<Integer> pattern){
		Set<Integer> leaves = new HashSet<Integer>();
		leaves.addAll(pattern);
		
		Iterator<Integer> it = pattern.iterator();
		while(it.hasNext()){
			int item = it.next();
			int itemFather = subKWTree.get(item).father.item;
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
		
		while(subKWTree.get(last).father.item!=last){
			RMPath.add(last);
			last=subKWTree.get(last).father.item;
		}
		RMPath.add(1);
		return RMPath;
	}
	

	
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
	
	
	private Set<Integer> IntersectofKNode(Set<Integer> set,KNode node){
		Set<Integer> newUsers = new HashSet<Integer>();
		Map<Integer, Integer> vertexCore = node.vertexCore;
		if(set.isEmpty()||vertexCore.isEmpty()||!vertexCore.containsKey(queryId)
				||(vertexCore.containsKey(queryId)&&vertexCore.get(queryId)<k))
			return newUsers;
		
		if(set.size()>vertexCore.size()){
			Iterator<Entry<Integer, Integer>> enit = vertexCore.entrySet().iterator();
			while(enit.hasNext()){
				Entry<Integer, Integer> entry = enit.next();
				if(set.contains(entry.getKey())&&entry.getValue()>=k) 
					newUsers.add(entry.getKey());
			}	
		}else {
			Iterator<Integer> iter = set.iterator();
			while(iter.hasNext()){
				int x = iter.next();
				if(vertexCore.containsKey(x)&&vertexCore.get(x)>=k)
					newUsers.add(x);
			}
		}
		return newUsers;
	}
	
	
	//check one pattern is a maximal pattern whether or not in result set  
	private void checkMax(Set<Integer> seq){
		if(maximalPattern.isEmpty()) {
			maximalPattern.add(seq);
			return;
		}
		if(maximalPattern.contains(seq)) return;
		
		boolean flag = true;
		Iterator<Set<Integer>> iter = maximalPattern.iterator();
		while(iter.hasNext()){
			Set<Integer> key = iter.next();
			if(key.size()>seq.size()){
				if(key.containsAll(seq)){
					flag=false;
					break;
				}
			}else if(key.size()<seq.size()) {
				if(seq.containsAll(key)){
					iter.remove();
				}
			}
		}
		if(flag==true) maximalPattern.add(seq);
	}
	
	
	private boolean hasVisited(Set<Integer> infre,Set<Integer> fre){
		Set<Set<Integer>> cuts = visited.get(infre);
		if(cuts==null){
			cuts = new HashSet<Set<Integer>>();
			cuts.add(fre);
			visited.put(infre, cuts);
			return false;
		}else{
			if(cuts.contains(fre)) return true;
			else {
				cuts.add(fre); 
				return false;
			}		
		}
	}
	
	
	//print all PCs
	public void print(){
		System.out.println("output size: "+maximalPattern.size());
		Iterator<Set<Integer>> iter = maximalPattern.iterator();
		while(iter.hasNext()){
			Set<Integer> pattern = iter.next();
			Set<Integer> user = lattice.get(pattern);
			if(user.size()>20)
				System.out.println("pattern: "+pattern.toString()+" users: "+user.size());
			else 
				System.out.println("pattern: "+pattern.toString()+" users: "+user.toString());
			
		}
	}
}
