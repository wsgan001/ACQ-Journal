package EXP.otherDefinition;


import java.util.*;
import java.util.Map.Entry;


import algorithm.DataReader;
import algorithm.DecomposeKCore;
import algorithm.FindCKCore;
import algorithm.FindCKSubG;
import algorithm.ProfiledTree.PNode;
import algorithm.ProfiledTree.PTree;
import config.Config;

public class TreeSimilarity {
	private int[][] graph = null;
	private int[][] nodes = null;
	private int core[] = null;
	private int queryId= -1;
	private PTree pTree = null;
	private Map<Integer, PNode> pTreeMap=null;
	
	private Set<Set<Integer>> cache = null;
	private double globalTreeSim =  0.0;
	private Set<Integer> CKC = null;
	private boolean DEBUG = false;
	
	
	public TreeSimilarity(int[][] graph, int[][] nodes){
		this.graph=graph;
		this.nodes=nodes;
		DecomposeKCore kCore=new DecomposeKCore(this.graph);
		core=kCore.decompose();
		this.pTree=new PTree();	
	}
	
	
	public TreeSimilarity(String graphFile, String nodeFile,Map<Integer, PNode> CPTreeMap){
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		this.graph = dataReader.readGraph();
		this.nodes = dataReader.readNodes();
		DecomposeKCore kCore=new DecomposeKCore(this.graph);
		core=kCore.decompose();
		this.pTree= new PTree(CPTreeMap);
	}
	
	
	public Set<Set<Integer>> query(int queryId){
		Set<Set<Integer>> output = new HashSet<Set<Integer>>();
		this.queryId = queryId;
		if(core[queryId]<Config.k){
			System.out.println("No qualified connected k-core!");
			return output;
		}
		this.cache = new HashSet<Set<Integer>>();

		//step 1:find the connected k-core containing queryId
		FindCKCore findCKCore=new FindCKCore();
		CKC = new HashSet<Integer>();
		CKC=findCKCore.findCKC(graph, core, queryId);
		if(CKC.size()<Config.k+1) return output; 
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
		if(nodes[queryId].length<2) return output;
		pTreeMap=pTree.buildPtree(nodes[queryId]);

		BFSMinePattern();
		 
		output = getqualifiedCommunity();
		
//		print(output);
	
		return output;
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
				Set<Integer> users = obtainNewUsers(patternToCheck);
				checkTreeSim(users);
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
							Set <Integer> users = new HashSet<Integer>(); 
							checkTreeSim(users);
						}
						
					}
					
				}
			}	
		}
	}

	
	//compute the tree similarity using the tree edit distance 
	public double treeSim(int[] tree1, int[] tree2){
		double distance = 0;
		for(int node1:tree1){
			boolean contain = false;
			if(contains(node1, tree2)){
				contain = true;
				continue;
			} 
			if(!contain) distance++;
		}
			
		for(int node1:tree2){
			boolean contain = false;
			if(contains(node1, tree1)){
				contain = true;
				continue;
			} 
			if(!contain) distance++;
		}	
		Set<Integer> uniqueNodes = new HashSet<Integer>();
		for(int x:tree1) uniqueNodes.add(x);
		for(int x:tree2) uniqueNodes.add(x);
		return distance/(uniqueNodes.size());
	}
			
	boolean contains(int t, int[] tree){
		boolean contains = false;
		for(int x:tree){
			if(t == x){
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	
	private void checkTreeSim(Set<Integer> users){
		double simValues = 0.0;
		int times = 1;
		boolean flag = false;
		for(int i:users){
			int[] ptreei = nodes[i];
			for(int j:users){
				if(i!=j){
					if(times ==10000){
						flag = true;
					}
					int []ptreej = nodes[j];
					simValues += treeSim(ptreei, ptreej);
					times++;

				}
			}
			if(flag) break;
		}
		simValues = simValues/times;
		
		if(simValues > globalTreeSim){
			globalTreeSim = simValues;
			cache = new HashSet<Set<Integer>>();
			cache.add(users);
		}else{
			cache.add(users);
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
	

	private Set<Set<Integer>> getqualifiedCommunity(){
		Set<Set<Integer>> community = new HashSet<Set<Integer>>();
		Iterator<Set<Integer>> iterator=cache.iterator();
		while(iterator.hasNext()){
			
			Set<Integer> users = iterator.next();
			community.add(users);
		}
		return community;
	}
	
}
	

	
	
	

