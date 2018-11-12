package algorithm.kwIndex.Query1_margin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

import com.sun.org.apache.regexp.internal.recompile;

import algorithm.kwIndex.*;
import algorithm.DecomposeKCore;
import algorithm.FindCKCore;
import algorithm.FindCKSubG;
import config.Config;

/**
@author chenyankai
@Date	Sep 18, 2017
	This class is the index-based query algorithm.
	No-Apriori based (MAGRIN) 
	Details are derived from Query1_V1 and optimized.
		
	Difference between V1 and V2 is that V2 :
	(1). Time efficient.
	(2). BFS manner to expand all cuts.
*/
	
public class Query1 {

	private int[][] graph = null;
	private int queryId = -1;
	private int k = -1;
	private Map<Integer, List<KWNode>> headList = null;
	
	private List<Integer> leafList = null;
	private Map<Integer,KWNode> subKWTree = null;
	//key:pattern  value users
	private Map<Set<Integer>,Set<Integer>> lattice = null;
	private Set<Set<Integer>> maximalPattern = null;

	private Map<Set<Integer>, Set<Set<Integer>>> visited = null;
	
	private boolean debug = false;
	
	public Query1(int[][] graph,Map<Integer, List<KWNode>> headList){
		this.graph = graph;
		this.headList = headList;
		this.k=Config.k;
		
	}
	
	public void queryInc(int query){
		this.lattice = new HashMap<Set<Integer>, Set<Integer>>();
		this.maximalPattern = new HashSet<Set<Integer>>();
		this.visited = new HashMap<Set<Integer>, Set<Set<Integer>>>();
		this.queryId = query;
		boolean state = induceKWTree();
		if(!state) return;
		incInitCut();
	}
	
	
	public void queryDec(int query){
		this.lattice = new HashMap<Set<Integer>, Set<Integer>>();
		this.maximalPattern = new HashSet<Set<Integer>>();
		this.visited = new HashMap<Set<Integer>, Set<Set<Integer>>>();
		this.queryId = query;
		boolean state = induceKWTree();
		if(!state) return;
		decInitCut();
	}
	
	public void queryPath(int query){
		this.lattice = new HashMap<Set<Integer>, Set<Integer>>();
		this.maximalPattern = new HashSet<Set<Integer>>();
		this.visited = new HashMap<Set<Integer>, Set<Set<Integer>>>();
		this.queryId = query;
		boolean state = induceKWTree();
		if(!state) return;
		pathInitCut();
	}
	
	
	public void query(int query,int model){
		this.lattice = new HashMap<Set<Integer>, Set<Integer>>();
		this.maximalPattern = new HashSet<Set<Integer>>();
		this.visited = new HashMap<Set<Integer>, Set<Set<Integer>>>();
		this.queryId = query;
		boolean state = induceKWTree();
		if(!state) return;
//		printSubKWtree(Config.acmccsDataWorkSpace+"subKWTree.txt");
		System.out.println("induce done."+subKWTree.size());
		
//		long time = System.nanoTime(); 
		List<Set<Integer>> initCut = null;
		if(model==1) initCut= incInitCut();
		if(model==2) initCut =decInitCut();;
		if(model==3) initCut= pathInitCut();
//		List<Set<Integer>> initCut = decInitCut();
//		List<Set<Integer>> initCut = pathInitCut();
//		System.out.println("find init cut ");
		
		if(initCut.size()==1) {
//			print();
			return;
		}
		if(initCut.isEmpty()) {
//			System.out.println("No community!");
//			print();
			
			Set<Integer> clearPC = new HashSet<Integer>();
			FindCKCore findCKCore = new FindCKCore();
			DecomposeKCore decomposeKCore = new DecomposeKCore(graph);
			clearPC=findCKCore.findCKC(graph, decomposeKCore.decompose(), query);
			
			if(clearPC==null || clearPC.isEmpty()) return;
			else {
				Set<Integer> pattern = new HashSet<Integer>();
				pattern.add(1);
				maximalPattern.add(pattern);
				lattice.put(pattern,clearPC);
			}
			
		}
		
//		System.out.println("entering expandCut.");
		
		long time1 = System.nanoTime(); 
		if(initCut.size()!=2) return;
		iterativeExpandCut(initCut.get(0), initCut.get(1));
//		System.out.println("expand cut time: "+(System.nanoTime()-time1)/1000);
//		print();
	}
	
	private int getSubkwtreeSize(){
		return this.subKWTree.size();
	}

	//for generate query Id
	public boolean generateQuery(int query,int min,int max){
		this.queryId = query;
		induceKWTree();
		System.out.println("induce done."+subKWTree.size());
		if(getSubkwtreeSize()>min && getSubkwtreeSize()<max) 
			return true;
		else 
			return false;	
	}
	
	private boolean induceKWTree(){
		this.subKWTree = new HashMap<Integer,KWNode>();
		this.leafList = new LinkedList<Integer>();
		Map<Integer, List<Integer>> childMap = new HashMap<Integer, List<Integer>>();
		KWNode root = new KWNode(1);
		subKWTree.put(1, root);
		int maxLength = 0;
		for(KWNode currentNode:headList.get(queryId)){
			//to mark the leaf item in the induce subKWtree
			int leaf = -1;
			int countLength = 0;
			boolean first = true;
			if(currentNode==null) continue;
			while(currentNode.itemId != 1){
				Set<Integer> vertexSet = currentNode.getCKCore(k, queryId);
//				if(vertexSet.isEmpty()) System.out.println("Not include id: "+currentNode.itemId);
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
			if(fatherNode==null) {
				System.out.println("father is null: "+father);
				continue;
//				return false;
			}
 			for(int child:childMap.get(father)){
				subKWTree.get(child).father = fatherNode;	
				fatherNode.childList.add(subKWTree.get(child));
			}
		}		
		return true;
	}
	
	
	
	
	//search a feasible solution in a incremental manner
	private List<Set<Integer>> incInitCut(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();

		//initialize patterns 
		for(KWNode node:subKWTree.get(1).childList){
			int item = node.itemId;
			
			Set<Integer> pattern = new HashSet<Integer>();
			pattern.add(1);//the root node
			pattern.add(item);
			Set<Integer> userSet = obtainUser(pattern);
			if(!userSet.isEmpty()){
				patternQueue.add(pattern);
			}
		}
		
		boolean tag=true;
		while(!patternQueue.isEmpty()&&tag){
//			System.out.println("patternQueue size: "+patternQueue.size());
			
			Set<Integer> tocheck = patternQueue.poll();
			Set<Integer> currentUser = lattice.get(tocheck);
			//if already reach the maximum pattern then return
			if(tocheck.size()==subKWTree.size()){
				maximalPattern.add(tocheck);
//				lattice.put(tocheck, currentUser);
				initCut.add(tocheck);
				break;
			}
			List<Integer> RMPath = getRightmostPath(tocheck);
			for(int x:RMPath){
				if(tag==false) break;
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
							 System.out.print("newUsers: "+users.size());
							 System.out.print(" current users: "+currentUser.size());
							 System.out.println();
						}
						//----------------------END DEBUG----------------------------
						
						 if(!currentUser.equals(users)){
							newUsers = intersect(users, currentUser);
							//compute CKC
							FindCKSubG findCKSG=new FindCKSubG(graph, newUsers, queryId);
							newUsers =findCKSG.findCKSG();
							if(newUsers==null){
								newUsers = new HashSet<Integer>(); 
								lattice.put(newPattern, newUsers);
//								maximalPattern.add(tocheck);
								 checkMax(tocheck);
								 System.out.println("break: "+newPattern.toString()+" users: "+newUsers.toString());
								initCut.add(newPattern);
								initCut.add(tocheck);
								tag=false;
								break;
							}else{
								lattice.put(newPattern, newUsers);
								patternQueue.add(newPattern);
								}
							}
						 else{
							 lattice.put(newPattern, users);
							patternQueue.add(newPattern);
						 }
					}
				}
			}
			
		}
		return initCut;
	}

	//search a feasible solution in a decremental manner
	private List<Set<Integer>> decInitCut(){
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
//							maximalPattern.add(nextpattern);
							checkMax(nextpattern);
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
	
	
	//function undone
	private List<Set<Integer>> pathInitCut(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		Queue<Set<Integer>> leafQueue = new LinkedList<Set<Integer>>();
		Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();
		
		//------------------------DEBUG------------------------------
		if(debug) 		System.out.println("leafList: "+leafList.toString());
		//----------------------END DEBUG----------------------------
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
			lattice.put(initPattern, initUsers);
			patternQueue.add(initPattern);
			leafQueue.add(initLeaf);

			//------------------------DEBUG------------------------------
			if(debug)	System.out.println("init pattern: "+initPattern.toString()+" init users: "+initUsers.toString());
			//----------------------END DEBUG----------------------------

		
		}
		
		//get the global right most leaf
		int globalRMLeaf = -1;
		for(int x:leafList) if(x>globalRMLeaf) globalRMLeaf=x;
		
		
		while(!patternQueue.isEmpty()&&!leafQueue.isEmpty()){
			//------------------------DEBUG------------------------------
			if(debug) 			System.out.println("pattern size: "+patternQueue.size());
			//----------------------END DEBUG----------------------------
			Set<Integer> checkLeaves = leafQueue.poll();
			Set<Integer> checkPattern = patternQueue.poll();
			Set<Integer> checkUsers = lattice.get(checkPattern);
			if(checkPattern.size()==subKWTree.size()){
//				maximalPattern.add(checkPattern);
				checkMax(checkPattern);
				initCut.add(checkPattern);
				break;
			}
			//------------------------DEBUG------------------------------
			if(debug){
				System.out.println();
				System.out.println(checkPattern.toString()+" users: "+checkUsers.toString()+"check leaves: "+checkLeaves.toString());
			}
			//----------------------END DEBUG----------------------------
			
			int rightMostLeaf = getRightMostLeaf(checkLeaves);
			if(rightMostLeaf==globalRMLeaf) {
				checkMax(checkPattern);
			}
				
				
			for(int newLeaf: leafList){
				if(newLeaf <= rightMostLeaf) continue;
				
				//firstly check the leaf item
				Set<Integer> users = getUsersInKTree(newLeaf);
				boolean isFeasible = false;
				if(checkUsers.equals(users)) isFeasible= true;
				
				else{
					Set<Integer> newUsers = getUsersofPatternNewItem(checkUsers, users);
					if(!newUsers.isEmpty()){//is feasible
						isFeasible = true;
						users = newUsers;
					}else{
						Set<Integer> newPattern = new HashSet<Integer>();
						newPattern.addAll(checkPattern);
						newPattern.add(newLeaf);
						lattice.put(newPattern, newUsers);
						
						int preItem = newLeaf;
						int currentItem = subKWTree.get(newLeaf).father.itemId;
//						int currentItem = newLeaf;
						 
						while(true){
							//if a leaf's father is in this pattern, and new pattern with this leaf is infre, find a initcut 
							if(checkPattern.contains(currentItem)) {
								Set<Integer> infrePattern = new HashSet<Integer>();
								infrePattern.addAll(checkPattern);
								infrePattern.add(preItem);
								initCut.add(infrePattern);
								initCut.add(checkPattern);
								break;
							}
							
							Set<Integer> currentItemUsers = getUsersInKTree(currentItem);
							Set<Integer> nextUsers = getUsersofPatternNewItem(checkUsers, currentItemUsers);
							if(nextUsers.isEmpty()){
								Set<Integer> nextPattern = new HashSet<Integer>();
								nextPattern.addAll(checkPattern);
								int tmpcurrentItem = currentItem;
								while(!nextPattern.contains(tmpcurrentItem)){
									nextPattern.add(tmpcurrentItem);
									tmpcurrentItem = subKWTree.get(tmpcurrentItem).father.itemId;
								}
								
								lattice.put(nextPattern, nextUsers);
								preItem = currentItem;
								currentItem = subKWTree.get(currentItem).father.itemId;
								
								
							}else {
								Set<Integer> nextPattern = new HashSet<Integer>();
								nextPattern.addAll(checkPattern);
								int tmpcurrentItem = currentItem;
								while(!nextPattern.contains(tmpcurrentItem)){
									nextPattern.add(tmpcurrentItem);
									tmpcurrentItem = subKWTree.get(tmpcurrentItem).father.itemId;
								}
								
								Set<Integer> prePattern = new HashSet<Integer>();
								prePattern.addAll(nextPattern);
								prePattern.add(preItem);
								
								lattice.put(nextPattern, nextUsers);
								initCut.add(prePattern);
								initCut.add(nextPattern);
								maximalPattern.add(nextPattern);
//								checkMax(nextPattern);
								break;
							}
							
						}
					}
				}
				//if this path is feasible, we do not need to compute attributes on this path
				if(isFeasible){
					Set<Integer> newLeaves = new HashSet<Integer>();
					Set<Integer> newPattern = new HashSet<Integer>();
					newLeaves.addAll(checkLeaves);
					newLeaves.add(newLeaf);
					newPattern.addAll(checkPattern);
					while(!newPattern.contains(newLeaf)){
						newPattern.add(newLeaf);
						newLeaf = subKWTree.get(newLeaf).father.itemId;
					}
					lattice.put(newPattern, users);
					patternQueue.add(newPattern);
					leafQueue.add(newLeaves);		
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
			//------------------------DEBUG------------------------------
			System.out.println("expand cut: "+infreQueue.size());
			//----------------------END DEBUG----------------------------
			Set<Set<Integer>> parentofSeqSet = parentSeq(infreToCheck);
			if(parentofSeqSet.isEmpty()) continue;
			for(Iterator<Set<Integer>> it=parentofSeqSet.iterator();it.hasNext();){
				Set<Integer> parentSeq = it.next();
				Set<Integer> userSet = obtainUser(parentSeq);
				
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
						Set<Integer> userOfElement = obtainUser(element);
						if(!userOfElement.isEmpty()){
							if(! lattice.get(parentSeq).isEmpty()) System.out.println("case 3");
							
							if(!hasVisited(parentSeq, element)){infreQueue.add(parentSeq);}
						}
					}		
				}	
			}		
		}
	}
	
	//expand a cross recursively 
	private void RecursiveExpandCut(Set<Integer> inFreSeq, Set<Integer> freSeq){
//		
		if(hasVisited(inFreSeq, freSeq)) return;
		
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
						RecursiveExpandCut(element, parentSeq);
					}else{
							
						Set<Integer> commonChild = commonChild(inFreSeq, element);
						//********************** 9.17 added **********************
						if(!lattice.containsKey(commonChild)) {
							Set<Integer> empty = new HashSet<Integer>();
							lattice.put(commonChild, empty);
						}
						//********************** 9.17 added **********************						
						RecursiveExpandCut(commonChild, element);
					}
				}
			}else{
				Set<Set<Integer>> parentOfParentSeq = parentSeq(parentSeq);
				for(Iterator<Set<Integer>> iter = parentOfParentSeq.iterator();iter.hasNext();){
					Set<Integer> element = iter.next();
					if(parentSeq.equals(element)) System.out.println("check heheheheh");
					Set<Integer> userOfElement = obtainUser(element);
					if(!userOfElement.isEmpty()){
						RecursiveExpandCut(parentSeq, element);
					}
				}
			}	
		}	
			return;
		}
	
	//obtain all the child pattern 
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
					
//					//optimized 
					if(lattice.containsKey(nextSeq)) continue;
				
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
				if(users == null){
					users = getUsersInKTree(leaf);
	 			}
				else users = intersect(users, getUsersInKTree(leaf));	
			}
				
			//compute CKC
			if(users==null){
				users= new HashSet<Integer>();;
				lattice.put(pattern, users);
				return users;
			}
			FindCKSubG findCKSG=new FindCKSubG(graph, users, queryId);
			userSet = findCKSG.findCKSG();
			if(userSet==null) userSet = new HashSet<Integer>();
			lattice.put(pattern, userSet);
			return userSet;
		}
		else {
			return userSet;
		}
		
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
	
	//get all the connected k-core from the each ktree of sepcific item
	private Set<Integer> getUsersInKTree(int item){
		return subKWTree.get(item).tmpVertexSet;
	}
	
	
	private Set<Integer> getUsersofPatternNewItem(Set<Integer> currentUsers, Set<Integer> itemUsers){
		Set<Integer> newUsers = new HashSet<Integer>();
		if(currentUsers.equals(itemUsers)){
			newUsers = itemUsers;
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
	
	
	private int getRightMostLeaf(Set<Integer> leaves){
		int max = -1;
		for(int x:leaves){
			if(x>max) max = x;
		}
		return max;
	}
	
	//check one pattern is a maximal pattern whether or not in result set  
	private void checkMax(Set<Integer> seq){
		
//		System.out.println("before checkking max: ");
//		if(!maximalPattern.isEmpty()){
//			Iterator<Set<Integer>> iter = maximalPattern.iterator();
//			while(iter.hasNext()){
//				System.out.println("existing pattern: "+iter.next().toString());
//			}
//		}
		
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
		if(flag==true) {
//			System.out.println("new added pattern: "+seq.toString());
			maximalPattern.add(seq);
		}
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
	
	
	public void printSubKWtree(String file){
		try {
			BufferedWriter std = new BufferedWriter(new FileWriter(file));
			std.write(subKWTree.get(1).toString(""));
			std.close();
			std.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public int getoutputSize(){
		return maximalPattern.size();
	}
	
	public Set<Set<Integer>> getMaximalPattern(){
		Set<Set<Integer>> set = new HashSet<Set<Integer>>();
		for(Set<Integer> pattern:maximalPattern){
			Set<Integer> newSet = new HashSet<Integer>();
			newSet.addAll(pattern);
			set.add(newSet);
		}
		return set;
	}
	
	public Map<Set<Integer>,Set<Integer>> getLeavesofMaximalPattern(){
		 Map<Set<Integer>,Set<Integer>> patternLeaves = new HashMap<Set<Integer>,Set<Integer>>();
		 for(Set<Integer> pattern:maximalPattern){
			 Set<Integer> leaves = getLeaves(pattern);
			 patternLeaves.put(pattern, leaves);
		 }
		 
		return patternLeaves;
	}
	
	public Map<Set<Integer>,Set<Integer>> getPatternUsers(){
		Map<Set<Integer>,Set<Integer>> patternLeaves = new HashMap<Set<Integer>,Set<Integer>>();
		 for(Set<Integer> pattern:maximalPattern){
			 Set<Integer> user = lattice.get(pattern);
			 patternLeaves.put(pattern, user);
		 }
		 return patternLeaves;
	}
	
	
	//print all PCs
	public void print(){
		System.out.println("output size: "+maximalPattern.size());
		Iterator<Set<Integer>> iter = maximalPattern.iterator();
		while(iter.hasNext()){
			Set<Integer> pattern = iter.next();
			List<Integer> list = new ArrayList<Integer>(pattern);
			Collections.sort(list);
			Set<Integer> user = lattice.get(pattern);
			if(user.size()>20)
				System.out.println("pattern: "+list.toString()+" users: "+user.size());
			else 
				System.out.println("pattern: "+list.toString()+" users: "+user.toString());
			
		}
	}
	
	
	}
