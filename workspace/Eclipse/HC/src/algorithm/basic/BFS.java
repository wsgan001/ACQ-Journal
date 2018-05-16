package algorithm.basic;

import java.util.*;
import java.util.Map.Entry;


import algorithm.DataReader;
import algorithm.DecomposeKCore;
import algorithm.FindCKCore;
import algorithm.FindCKSubG;
import algorithm.ProfiledTree.PNode;
import algorithm.ProfiledTree.PTree;
import config.Config;

public class BFS {
	private int[][] graph = null;
	private int[][] nodes = null;
	private int core[] = null;
	private int queryId= -1;
	private PTree pTree = null;
	private Map<Integer, PNode> pTreeMap=null;
	
	private Set<Set<Integer>> maximalPattern = null;
	private Set<Integer> CKC = null;

	private boolean DEBUG = false;
	
	
	public BFS(int[][] graph, int[][] nodes){
		this.graph=graph;
		this.nodes=nodes;
		DecomposeKCore kCore=new DecomposeKCore(this.graph);
		core=kCore.decompose();
		this.pTree=new PTree();
		
	}
	

	
	public BFS(String graphFile, String nodeFile,Map<Integer, PNode> CPTreeMap){
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		this.graph = dataReader.readGraph();
		this.nodes = dataReader.readNodes();
		DecomposeKCore kCore=new DecomposeKCore(this.graph);
		core=kCore.decompose();
		this.pTree= new PTree(CPTreeMap);
	}
	
	
	public void query(int queryId){
		this.queryId = queryId;
		if(core[queryId]<Config.k){
			System.out.println("No qualified connected k-core!");
			return;
		}
		this.maximalPattern = new HashSet<Set<Integer>>();

		//step 1:find the connected k-core containing queryId
		FindCKCore findCKCore=new FindCKCore();
		CKC = new HashSet<Integer>();
		CKC=findCKCore.findCKC(graph, core, queryId);
		if(CKC.size()<Config.k+1) return; 
		System.out.println("CKC: "+CKC.size());

		
		//------------------------DEBUG------------------------------
				if(DEBUG){
					for(int x:CKC) {
						String append="CKC users: "+x+" shared items: ";
						for(int y:nodes[x]) append+=y+" ";
						System.out.println(append);
					}
				}
		//----------------------END DEBUG----------------------------
	
		//step 2: mining all maximal common subsequences
		if(nodes[queryId].length<2) return;
		pTreeMap=pTree.buildPtree(nodes[queryId]);

		BFSMinePattern();
		 
		Map<Set<Integer>, Set<Integer>> output = getqualifiedCommunity();
		
		print(output);
	
	}	
	
	private void BFSMinePattern(){
		Queue<Set<Integer>> patternQueue = new LinkedList<Set<Integer>>();
		Set<Integer> startPattern = new HashSet<Integer>();
		startPattern.add(1);
		patternQueue.add(startPattern);
		
	
		while(!patternQueue.isEmpty()){
//			System.out.println(patternQueue.size());
			Set<Integer> patternToCheck = patternQueue.poll();
			
			//if already reach the maximum pattern then return
			if(patternToCheck.size()==nodes[queryId].length){
				maximalPattern.add(patternToCheck);
				continue;
			}
			
			List<Integer> RMPath = getRMPath(patternToCheck);
			for(int x:RMPath){
				for(PNode node:pTreeMap.get(x).getChildlist()){
					int newItem = node.getId();
					if(!patternToCheck.contains(newItem)){
						Set<Integer> newPattern = new HashSet<Integer>();
						newPattern.addAll(patternToCheck);
						newPattern.add(newItem);
						
						Set<Integer> newUsers = obtainNewUsers(newPattern);
						if(!newUsers.isEmpty()){
							patternQueue.add(newPattern);
						}else{
							checkMax(patternToCheck);
						}
						
					}
					
				}
			}	
		}
	}
	
	
	private List<Integer> getRMPath(Set<Integer> seq){
		List<Integer> RMPath = new ArrayList<Integer>();
		int last = -1;
		for(Iterator<Integer> it=seq.iterator();it.hasNext();){
			int current = it.next();
			if(last < current) last = current;
		} 
		PNode lastNode = pTreeMap.get(last);
		while(lastNode.father!=lastNode){
			RMPath.add(lastNode.getId());
			lastNode= lastNode.father;
		}
		RMPath.add(1);
		return RMPath;
	}
	
	
	private Set<Integer> obtainNewUsers(Set<Integer> newPattern){
		Set<Integer> newUsers = new HashSet<Integer>();
		
		for(Iterator<Integer> iter=CKC.iterator();iter.hasNext(); ){
			int user = iter.next();
			int[] seqOfUser =  nodes[user];
			if(isContains(newPattern, seqOfUser)){
				newUsers.add(user);
			}
		}
		
		if(!CKC.equals(newUsers)){
			FindCKSubG findCKSG=new FindCKSubG(graph, newUsers, queryId);
			newUsers = findCKSG.findCKSG();
			if(newUsers==null) newUsers = new HashSet<Integer>();
		}
		return newUsers;
	}
	
	
	private boolean isContains(Set<Integer> pattern,int[]seq){
		if (seq.length==0 || pattern.size()>seq.length ) return false;
		
		
		boolean isAllContains = true; 
		for(int x:pattern){
			boolean isContains = false;
			for(int y:seq){
				if(y==x){
					isContains = true; 
					break;
				}	
			}
			if(!isContains){
				isAllContains = false;
			}
		}
		
		return isAllContains;
	}
	
	
	private void checkMax(Set<Integer> pattern){
		if(maximalPattern.isEmpty()) {
			maximalPattern.add(pattern);
			return;
		}
		if(maximalPattern.contains(pattern)) return;
		
		boolean flag = true;
		Iterator<Set<Integer>> iter = maximalPattern.iterator();
		while(iter.hasNext()){
			Set<Integer> key = iter.next();
			if(key.size()>pattern.size()){
				if(key.containsAll(pattern)){
					flag=false;
					break;
				}
			}else if(key.size()<pattern.size()) {
				if(pattern.containsAll(key)){
					iter.remove();
				}
			}
		}
		if(flag==true) maximalPattern.add(pattern);
	}
	
	
	private Map<Set<Integer>, Set<Integer>> getqualifiedCommunity(){
		Map<Set<Integer>, Set<Integer>> patternToCommunity = new HashMap<Set<Integer>,Set<Integer>>();
		Iterator<Set<Integer>> iterator=maximalPattern.iterator();
		while(iterator.hasNext()){
			Set<Integer> pattern = iterator.next();
			Set<Integer> users = obtainNewUsers(pattern);
			patternToCommunity.put(pattern, users);
		}
		return patternToCommunity;
	}

	
	
	//print all qualified patterns
	public void print(Map<Set<Integer>, Set<Integer>> map){
		System.out.println("Now printing outputs of the BFS algorithm!");
		System.out.println("output size:"+maximalPattern.size());
		Iterator<Entry<Set<Integer>, Set<Integer>>> entryIter = map.entrySet().iterator();
		String string = new String();
		while(entryIter.hasNext()){
			Entry<Set<Integer>, Set<Integer>> entry = entryIter.next();
			if(entry.getValue().size()>20)
				string += "Patten:  "+entry.getKey().toString()+",  Users size: "+entry.getValue().size()+"\n";	
			else {
				string += "Patten:  "+entry.getKey().toString()+",  Users: "+entry.getValue().toString()+"\n";	
			}
		
		}
		System.out.println(string);
	}
	
}
	

	
	
	
