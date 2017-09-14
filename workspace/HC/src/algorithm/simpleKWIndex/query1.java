package algorithm.simpleKWIndex;

import java.util.*;
import java.util.Map.Entry;

import config.Config;
/**
@author chenyankai
@Date	Sep 3, 2017
	based on simplied KWtree index 
	search tree patterns in MARGIN-based(the first not-apriori algo)
	
*/
public class query1 {
	private int queryId = -1;
	private int k = Config.k;
	private KWTree kwTree = null; 
	private Map<Integer, KNode> subKWTree = null;
	//key:pattern  value users
	private Map<Set<Integer>,Set<Integer>> lattice = null;
	private Map<Set<Integer>, Set<Integer>> output=null;
	private Set<Set<Integer>> visited = null;
	
	
	private boolean debug = false;
	
	public query1(KWTree kwTree){
		this.kwTree = kwTree;
		this.subKWTree = new HashMap<Integer,KNode>();
		this.lattice = new HashMap<Set<Integer>,Set<Integer>>();
		this.output = new HashMap<Set<Integer>, Set<Integer>>();
		this.visited = new HashSet<Set<Integer>>();
	}
	
	public void query(int queryId){
		this.queryId = queryId;
//		induceIndex();
		newInduceIndex();
		
	}
	
	
	
	//induce a kwTree and store it in the map
	private void newInduceIndex(){
		KNode root = new KNode(1);
		subKWTree.put(1,root);
		Set<Integer> visited= new HashSet<Integer>();
		for(KNode currentNode:kwTree.getHeadList().get(queryId)){
			int leafItem = currentNode.item;
			if(kwTree.contains(leafItem)){
				KNode newNode = new KNode(leafItem);
				newNode.setInduceVertices(kwTree.induceUsers(k,leafItem));
				subKWTree.put(leafItem, newNode);
			
				int fatherItem = currentNode.father.item;
				while(fatherItem != 1){
					KNode newFather = subKWTree.get(fatherItem);
					if(newFather==null){
						newFather = new KNode(fatherItem);
						newFather.setInduceVertices(currentNode.father.getKCore(k));
						subKWTree.put(fatherItem, newFather);
					}

					if(visited.contains(currentNode.item)) break;
					subKWTree.get(currentNode.item).father = newFather;
					newFather.addChild(subKWTree.get(currentNode.item));
					visited.add(currentNode.item);
					
					//continue go up
					currentNode = currentNode.father;
					fatherItem = currentNode.father.item;
				}	

				if(visited.contains(currentNode.item)) continue;
				
				subKWTree.get(currentNode.item).father = root;
				root.addChild(subKWTree.get(currentNode.item));
				visited.add(currentNode.item);
			}
		}
		
		//------------------------DEBUG------------------------------
		if(debug) 		System.out.println(root.toString(""));
		//----------------------END DEBUG----------------------------
	}


	private List<Set<Integer>> decInitCut(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		return initCut;
	}
	
	
	private List<Set<Integer>> incInitCut(){
		List<Set<Integer>> initCut = new ArrayList<Set<Integer>>();
		Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>(); 
		Queue<Set<Integer>> userQueue = new LinkedList<Set<Integer>>();
		Queue<Integer> rightmostLeaf = new LinkedList<Integer>();
		
		for(KNode node:subKWTree.get(1).getChildList()){
			int item = node.item;
			if(!subKWTree.containsKey(item)) continue;
			
			Set<Integer> pattern = new HashSet<Integer>();
			pattern.add(1); pattern.add(item);
			Set<Integer> userSet = obtainUsers(item);
			if(!userSet.isEmpty()){
				patternQueue.add(pattern);
				userQueue.add(userSet);
			}
		}
		
		boolean tag = true;
		while(!patternQueue.isEmpty()&&!userQueue.isEmpty()&&tag){
			Set<Integer> check = patternQueue.poll();
			Set<Integer> currentUser = userQueue.poll();
			
			//if pattern already reach the maximum pattern then return
			if(check.size()==subKWTree.size()){
				output.put(check, currentUser);
				initCut.add(check);
				break;
			}
			
			List<Integer> RMPath = getRightmostPath(check);
			
			
			
			
		}
		
		
		
		return initCut;
	}
	
	
	
	
	
	//init users pattern.size() = 2.
	
	private Set<Integer> obtainUsers(int item){
		Queue<KNode> queue = new LinkedList<KNode>();
		Set<Integer> userSet = new HashSet<Integer>();
		KNode node = subKWTree.get(item);
		queue.add(node);
		
		while(!queue.isEmpty()){
			KNode check = queue.poll();
			if(leaf2Users.containsKey(check.item)){
				userSet.addAll(leaf2Users.get(check.item));
			}else{
				Iterator<Entry<Integer, Set<Integer>>> enIt = check.vertices.entrySet().iterator();
				while(enIt.hasNext()){
					Entry<Integer, Set<Integer>> entry = enIt.next();
					if(entry.getKey()>= k) userSet.addAll(entry.getValue());
				}
			}
			
			for(KNode child:check.getChildList()) {
				if(subKWTree.containsKey(child.item))
					queue.add(child);	
				}
		}
		
		return userSet;
	}
	
	
	
	//get the rightmost path of the current pattern
	private List<Integer> getRightmostPath(Set<Integer> seq){
		List<Integer> RMPath = new ArrayList<Integer>();
		int last = -1;
		for(Iterator<Integer> it=seq.iterator();it.hasNext();){
			last = it.next();
		} 
		while(subKWTree.get(last).father.item!=last){
			RMPath.add(last);
			last=subKWTree.get(last).father.item;
		}
		RMPath.add(1);
		return RMPath;
	}	
	
	
	

	
	

	
	private Set<Integer> intersect(Set<Integer>set1,Set<Integer>set2){
		Set<Integer> newSet = new HashSet<Integer>();
		if(set1.isEmpty()||set2.isEmpty()) return newSet;
		
		if(set1.size()>=set2.size()){
			Iterator<Integer> it = set2.iterator();
			while(it.hasNext()){
				int x= it.next();
				if(set1.contains(x)) newSet.add(x);
			}
		}
		else{
			Iterator<Integer> it = set1.iterator();
			while(it.hasNext()){
				int x= it.next();
				if(set2.contains(x)) newSet.add(x);
			}
		}
		return newSet;
	}
	
	
	
}
