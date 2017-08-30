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
		Set<Integer> initPattern = new HashSet<Integer>();
		Set<Integer> initUsers = new HashSet<Integer>();
		initPattern.add(1);
		patternQueue.add(initPattern);
		userQueue.add(initUsers);
		
		while(!patternQueue.isEmpty()&&!userQueue.isEmpty()){
			Set<Integer> tocheck = patternQueue.poll();
			Set<Integer> users = userQueue.poll();
			
			int last = -1;
			for(Iterator<Integer> it=tocheck.iterator();it.hasNext();){
				last = it.next();
			}
			Set<Integer> RMPath = new HashSet<Integer>();//compressed rightmost path
			RMPath.add(1);
			if(last!=1) RMPath.add(last);
			
			//------------------------DEBUG------------------------------
			if(debug){
				System.out.println("checking pattern: "+tocheck.toString()
				+ "  users: "+users.toString());
				System.out.println("rightmost path "+last);
			}
			//----------------------END DEBUG----------------------------
			
			for(int x:RMPath){
				for(KWNode node:subKWTree.get(x).childList){
					int item = node.itemId;
					if(subKWTree.containsKey(item)&&!tocheck.contains(item)){
						Set<Integer> newPattern = new HashSet<Integer>();
						newPattern.add(item);
						if(x==1) newPattern.add(1);
						for(int y:tocheck){
							if(y!=x) newPattern.add(y);
						}
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
	
	
	
	
	
	private Set<Integer> obtainUser(int newItem,Set<Integer> preUsers){		
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
