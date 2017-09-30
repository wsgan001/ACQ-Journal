package algorithm.kwIndex.Query2;

import java.util.*;

import algorithm.FindCKSubG;
import algorithm.kwIndex.*;
import config.Config;

/**
@author chenyankai
@Date	Aug 30, 2017
	This query algorithm:
	 (1) Index-based 
	 (2) an incremental pattern generation 
	 (3) increase in rightmost path
	 (4) BFS-based search model
*/
public class Query2_Inc {
	private int[][] graph = null;
	private int queryId = -1;
	private int k = -1;
	private Map<Integer, List<KWNode>> headList = null;
	private Map<Integer,KWNode> subKWTree = null;

	//key:pattern  value users
	private Map<Set<Integer>, Set<Integer>> output=null;
	
	private boolean debug = false;
	
	public Query2_Inc(int[][] graph, Map<Integer, List<KWNode>> headList){
		this.graph = graph;
		this.headList = headList;
		this.k = Config.k;
	}
	
	//main function
	public void query(int queryId){
		this.queryId = queryId;
		this.output = new HashMap<Set<Integer>, Set<Integer>>();

		induceSubKWTree();
		
		BFSMine();
		
		
	}
	
	//induce a KW-tree subtree and store it in a map
		private void induceSubKWTree(){
			this.subKWTree = new HashMap<Integer,KWNode>();
			Map<Integer, List<Integer>> childMap = new HashMap<Integer, List<Integer>>();
			KWNode root = new KWNode(1);
			subKWTree.put(1, root);
			System.out.println(headList.get(queryId).size()+"   size ");
			for(KWNode currentNode:headList.get(queryId)){
				//to mark the leaf item in the induce subKWtree
				while(currentNode.itemId != 1){
					Set<Integer> vertexSet = currentNode.getCKCore(k, queryId);
					if(!vertexSet.isEmpty()){
						int currenItem = currentNode.itemId;
						if(!subKWTree.containsKey(currenItem)){
							KWNode newNode = new KWNode(currenItem);
							newNode.tmpVertexSet = vertexSet;
							subKWTree.put(currenItem, newNode);
							//computing the length of the current path
							
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
		}
	
	
	private void BFSMine(){
		Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();
		Queue<Set<Integer>> userQueue = new LinkedList<Set<Integer>>();
		
		for(KWNode node:subKWTree.get(1).childList){
			int item = node.itemId;
			
			Set<Integer> pattern = new HashSet<Integer>();
			pattern.add(1);//the root node
			pattern.add(item);
			Set<Integer> userSet = getUsersInKTree(item);
			if(!userSet.isEmpty()) {
				patternQueue.add(pattern);
				userQueue.add(userSet);
			}
		}
		
		
		while(!patternQueue.isEmpty()&&!userQueue.isEmpty()){
			Set<Integer> checkPattern = patternQueue.poll();
			Set<Integer>  checkUsers = userQueue.poll();
			System.out.println("query2_inc pattern size: "+patternQueue.size());
			if(checkPattern.size()==subKWTree.size()){
				output.put(checkPattern, checkUsers);
				break;
			}
			
			boolean PatternisTerminated = true;
			List<Integer> RMPath = getRightmostPath(checkPattern);
			
			for(int x:RMPath){
				for(KWNode node:subKWTree.get(x).childList){
					int item = node.itemId;
					if(!checkPattern.contains(item)){
						PatternisTerminated = true;
						Set<Integer> newPattern = new HashSet<Integer>();
						newPattern.add(item);
						
						for(int y:checkPattern) newPattern.add(y);
				
						Set<Integer> users = getUsersInKTree(item);
						//------------------------DEBUG------------------------------
						if(debug){
							 System.out.println("oldPattern: "+checkPattern.toString()+" newPattern: "+newPattern.toString());
							 System.out.println(" current users: "+checkUsers.toString()+" newUsers: "+users.toString());
							
							 System.out.println();
						}
						//----------------------END DEBUG----------------------------
						
						
						if(!checkUsers.equals(users)){
							Set<Integer> newUsers = intersect(users, checkUsers);
							FindCKSubG findCKSG=new FindCKSubG(graph, newUsers, queryId);
							newUsers =findCKSG.findCKSG();
							if(newUsers==null){
								checkMax(checkPattern, checkUsers);
							}else{
								patternQueue.add(newPattern);
								userQueue.add(newUsers);
							}
						}else{
							patternQueue.add(newPattern);
							userQueue.add(users);
						}
					}				
				}				
			}	
			
			if(PatternisTerminated){
				checkMax(checkPattern, checkUsers);
			}
			
			
		}	
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
	
	
	//get the Connected k-core from the every kTree of specific item
	private Set<Integer> getUsersInKTree(int item){
		return subKWTree.get(item).tmpVertexSet;
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
