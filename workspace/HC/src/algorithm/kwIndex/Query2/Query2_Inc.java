package algorithm.kwIndex.Query2;

import java.util.*;

import algorithm.FindCKSubG;
import algorithm.kwIndex.*;

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
	private int queryId = -1;
	private int k = -1;
	private KWTree kwTree = null;
	private Map<Integer,KWNode> subKWTree = null;
	//key:pattern  value users
	private Map<Set<Integer>, Set<Integer>> output=null;
	
	private boolean debug = true;
	
	public Query2_Inc(KWTree kwTree){
		this.kwTree = kwTree;
		this.subKWTree = new HashMap<Integer,KWNode>();
		this.output = new HashMap<Set<Integer>, Set<Integer>>();
	}
	
	//main function
	public void query(int queryId){
		this.queryId = queryId;
		getSubKWTree();
		
		BFSMine();
		
		
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
	
	
	private void BFSMine(){
		Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();
		Queue<Set<Integer>> userQueue = new LinkedList<Set<Integer>>();
		
		for(KWNode node:subKWTree.get(1).childList){
			int item = node.itemId;
			if(!subKWTree.containsKey(item)) continue;
			
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
			Set<Integer> tocheck = patternQueue.poll();
			Set<Integer> users = userQueue.poll();
			
			List<Integer> RMPath = getRightmostPath(tocheck);
			
			//------------------------DEBUG------------------------------
			if(debug){
				System.out.println("checking pattern: "+tocheck.toString()
				+ "  users: "+users.toString());
				System.out.println("rightmost path "+RMPath.toString());
			}
			//----------------------END DEBUG----------------------------
			
			for(int x:RMPath){
				for(KWNode node:subKWTree.get(x).childList){
					int item = node.itemId;
					if(subKWTree.containsKey(item)&&!tocheck.contains(item)){
						Set<Integer> newPattern = new HashSet<Integer>();
						newPattern.add(item);
						for(int y:tocheck) newPattern.add(y);
						
						Set<Integer> newUsers = obtainUser(item, users);
						if(!newUsers.isEmpty()){
							patternQueue.add(newPattern);
							userQueue.add(newUsers);
						}else {
							checkMax(tocheck,users);
						}
					}				
				}				
			}		
		}	
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
	
	
	//get the Connected k-core from the every kTree of specific item
	private Set<Integer> getUsersInKTree(int item){
		return subKWTree.get(item).ktree.getKQueryId(k, queryId);
	}
	
	
	private Set<Integer> obtainUser(int newItem,Set<Integer> preUsers){		
		 Set<Integer> userSet = getUsersInKTree(newItem);
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
			Set<Integer> user = output.get(pattern);
			System.out.println("pattern: "+pattern.toString()+" users: "+user);
		}
	}
	
}
