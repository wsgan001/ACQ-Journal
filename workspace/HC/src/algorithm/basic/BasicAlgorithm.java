package algorithm.basic;

import java.util.*;
import algorithm.*;
import algorithm.ProfiledTree.PNode;
import algorithm.ProfiledTree.PTree;
import config.Config;

/**
@author chenyankai
@Date	Jul 6, 2017
		
		DFS based search algorithm:
steps:	(1) k-core to narrow down the search space; 
	   	(2) generate subtrees of query vertex's tree; 
	   	(3) checking (both k-core and maximal subtree). 
*/


public class BasicAlgorithm {
	private int graph[][]=null;//graph structure;  starting from 1
	private int nodes[][]=null;//the tree nodes of each node; starting from 1

	
	private int core[]=null;
	private int queryId=-1;
	private PTree pTree=null;
	private Map<Integer, PNode> pTreeMap=null;
	
	//record the outputs
	private Map<Set<Integer>, int[]> output=null;
	
	private boolean DEBUG=true;
	

	public BasicAlgorithm(){
		this.pTree=new PTree();
		this.output=new HashMap<Set<Integer>, int[]>();
	}
	
	public BasicAlgorithm(int graph[][],int nodes[][]){
		this.graph=graph;
		this.nodes=nodes;
		DecomposeKCore kCore=new DecomposeKCore(this.graph);
		core=kCore.decompose();
		this.pTree=new PTree();
		this.output=new HashMap<Set<Integer>, int[]>();

	}
	
	
	
	public void query(int queryId){
		this.queryId=queryId;
		if(core[queryId]<Config.k){
			System.out.println("No qualified connected k-core!");
			return;
		}
		
		//step 1:find the connected k-core containing queryId
		FindCKCore findCKCore=new FindCKCore();
		Set<Integer> CKC=findCKCore.findCKC(graph, core, queryId);
		if(CKC.size()<Config.k+1) return; 
		
		
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
		pTreeMap=pTree.buildPtree(nodes[queryId]);
		int[] seqStart={nodes[queryId][0]};
		mine(seqStart, CKC);
		printOutput();
	}
	

	
	private void mine(int[] seq,Set<Integer> users){
		//------------------------DEBUG------------------------------
		if(DEBUG){
			System.out.println("------------------------------");
			System.out.print("cksg: ");
			for(int x:users) System.out.print(x+" ");
			System.out.println();
		}
		//----------------------END DEBUG----------------------------
		if(users.size()<Config.k+1) return;
		
		
		List<int[]> nextSeqList= geneSubtree(seq);
		//if it has reach the maixmal pattern
		if(nextSeqList.size()==0){
//			if(isNewItems(seq)){
				output.put(users, seq);
				//------------------------DEBUG------------------------------
				if(DEBUG) System.out.println("saved");
				//----------------------END DEBUG----------------------------
//			}
			return;
		}
		
		//otherwise recursively mine
		boolean finished=true;
		for(int[] nextSeq:nextSeqList){
			Set<Integer> newUsers=check(nextSeq, users);
			//------------------------DEBUG------------------------------
			if(DEBUG){
				String append="checking next seq: ";
				for(int x:nextSeq) append+=x+"  ";
				System.out.println(append);
				
				append="now the users are: ";
				for(int x:newUsers)append+= x+" ";
				System.out.println(append);
			}
			//----------------------END DEBUG----------------------------
			if(newUsers.size()>=Config.k+1){
				finished=false;
				mine(nextSeq,newUsers);
			}
		}
		if(finished && isNewItems(seq)){
			//------------------------DEBUG------------------------------

			//----------------------END DEBUG----------------------------
			if(DEBUG){
				System.out.print("finished recursion and saving: "+users.toString()+" items ");
				for(int a:seq) 	System.out.print(a+"  ");
				System.out.println();
			}
			output.put(users,seq);
		}
	}
	
	
		//generate a new subtree from a subtree by add an node in the right most path
	private List<int[]>  geneSubtree(final int[] seq){
		List<Integer> rightmostPath=getRightmostPath(seq);
		//------------------------DEBUG------------------------------
		if(DEBUG) System.out.println("now rightmost path:"+rightmostPath.toString() );
		//----------------------END DEBUG----------------------------
		
		List<int[]> nextSeq=new ArrayList<int[]>();
		for(int i=0;i<rightmostPath.size()-1;i++){
			int father=rightmostPath.get(i);
			int child=rightmostPath.get(i+1);
			List<PNode> childSet=pTreeMap.get(father).getChildlist();
			if(child<childSet.get(childSet.size()-1).getId()){
				for(PNode node:childSet){
					int x=node.getId();
					if(x>child){
						int[] b=new int[seq.length+1];
						System.arraycopy(seq, 0, b, 0, seq.length);
						b[b.length-1]=x;
						nextSeq.add(b);
						//------------------------DEBUG------------------------------
						if(DEBUG){
							System.out.print("new Array: ");
							for(int q:b) {
								System.out.print(q+" ");
							}
							System.out.println();
						}
						//----------------------END DEBUG----------------------------
					}
				}
			}
		}
		//last node of in the right most path
		int lastOne=rightmostPath.get(rightmostPath.size()-1);
		List<PNode> childSet=pTreeMap.get(lastOne).getChildlist();
			for(PNode node:childSet){
				int x=node.getId();
					int[] b=new int[seq.length+1];
					System.arraycopy(seq, 0, b, 0, seq.length);
					b[b.length-1]=x;
					nextSeq.add(b);
					//------------------------DEBUG------------------------------
					if(DEBUG){
						System.out.println("poteintial new seq: ");
						for(int q:b) {
							System.out.print(q+" ");
						}
						System.out.println();
					}
					//----------------------END DEBUG----------------------------
			}
			
		//------------------------DEBUG------------------------------
		if(DEBUG) System.out.println("next rightmost path number:"+nextSeq.size());
		//----------------------END DEBUG----------------------------	
		return nextSeq;
	}
	
	//calculate the right most path of one tree
	//get the rightmost path of one subtree pattern
	private List<Integer> getRightmostPath(final int[] seq){
		return pTree.tracePath(seq[seq.length-1]);
	}
	

	//check users to get new users who share new subtrees 
	private Set<Integer> check(int[] newSeq, final Set<Integer> users){
		//------------------------DEBUG------------------------------
		if(!isCKCore(users)){
			if(DEBUG)  System.out.println("users is not kcore");
			return null;
		}
		//----------------------END DEBUG----------------------------
		Set<Integer> newUsers=new HashSet<Integer>();
		Iterator<Integer> iterator=users.iterator();
		while(iterator.hasNext()){
			int user=iterator.next();
			boolean isContained=true;
			
			Set<Integer> userItemSet=new HashSet<Integer>();
			for(int x:nodes[user]) userItemSet.add(x);

			for(int item:newSeq){
				if(!userItemSet.contains(item)){
					isContained=false;
					break;
				}
			}
			if(isContained){
				newUsers.add(user);
			}
		}
		//------------------------DEBUG------------------------------
		if(DEBUG){
			String append="Before k-core check and newItem check________\n k core:  "+newUsers.toString()+"  new items: ";
			for(int x:newSeq) append +=x+" ";
			System.out.println(append);
		}
		//----------------------END DEBUG----------------------------
		FindCKSubG findCKSG=new FindCKSubG(graph, newUsers, queryId); 
		return findCKSG.findCKSG();
	}	
	

	//not finished
	//checking whether satisfy connected and k-core 
	private boolean isCKCore(Set<Integer> set){
		if(set.size()>=Config.k+1) return true;
		else return false;
	}

	
	//checking one pattern is one sub-pattern of the qualified pattern; true: new pattern flase: is not new pattern
	private boolean isNewItems(int[]seq){
		if(output.size()==0) return true;
		
		Iterator<int[]> iterator=output.values().iterator();		
		while(iterator.hasNext()){
			int[] validatedSeq=iterator.next();
		//------------------------DEBUG------------------------------
			if(DEBUG) {
				System.out.print("subseq checking: ");
				for(int x:seq) System.out.print(x+"  ");
				System.out.println();
				
				System.out.print("validatedSeq checking: ");
				for(int x:validatedSeq) System.out.print(x+"  ");
				System.out.println();
			}
		//----------------------END DEBUG----------------------------
			
			if(seq.length<=validatedSeq.length){
				int x=0; int y=0;
				while(x<seq.length && y<validatedSeq.length){
					if(seq[x]==validatedSeq[y]){ x++; y++;}
					else if(seq[x]>validatedSeq[y]) {y++;}
					else break;// seq is not contained in this validatedSeq, continue loop
				}
				//compare to the end then seq is contained in validatedSeq: false
				if (x==seq.length)  return false;
			}
		}
		return true;	
	}


	//print all qualified community and its corresponding maximal subtrees
		private void printOutput(){
			System.out.println("output size"+output.size());
			Iterator<Set<Integer>> iterator=output.keySet().iterator();
			while(iterator.hasNext()){
				Set<Integer> set=iterator.next();
				String append="users: "+set.toString()+"  maximal seq: ";
				int[]a=output.get(set);
				for(int x:a) append+=x+"  ";
				System.out.println(append);
			}
		}
	
	
	public static void main(String[] args){
		int graph[][] = new int[11][];
		int a1[] = {2, 3, 4, 5};	graph[1] = a1;
		int a2[] = {1, 3, 4, 5};	graph[2] = a2;
		int a3[] = {1, 2, 4, 5};		graph[3] = a3;
		int a4[] = {1, 2, 3,5,7};	graph[4] = a4;
		int a5[] = {1, 2,3,4, 6};		graph[5] = a5;
		int a6[] = {5};				graph[6] = a6;
		int a7[] = {4};				graph[7] = a7;
		int a8[] = {9};				graph[8] = a8;
		int a9[] = {8};				graph[9] = a9;
		int a10[] = {};				graph[10] = a10;
		
		
		int nodes[][] = new int[11][];
		int k1[] = {1,2,3,4,6,7,12,13,14,15};	nodes[1] = k1;
		int k2[] = {1,2,3,4,7,8,9,12,13,15};	nodes[2] = k2;
		int k3[] = {1,3,4,5,6,10};			nodes[3] = k3;
		int k4[] = {1,2,3,7,10,12};			nodes[4] = k4;
		int k5[] = {1,3,4,7,8,9,10};		nodes[5] = k5;
		int k6[] = {1,2,3,4,10,12,13,14,15};nodes[6] = k6;
		int k7[] = {1,3,7,8,9};				nodes[7] = k7;
		int k8[] = {1,10,11,12,15};			nodes[8] = k8;
		int k9[] = {1,2,3,4};				nodes[9] = k9;
		int k10[] = {1,3,7,12};				nodes[10] = k10;
		
		BasicAlgorithm bAlgo=new BasicAlgorithm(graph,nodes);
		Config.k=2;
	
		bAlgo.query(1);
		
		
		
		
	}
	

	
}
