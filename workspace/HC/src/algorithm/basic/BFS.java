package algorithm.basic;

import java.util.*;

import algorithm.DecomposeKCore;
import algorithm.FindCKCore;
import algorithm.FindCKSubG;
import algorithm.ProfiledTree.*;
import config.Config;

/**
@author chenyankai
@Date	Aug 24, 2017

		BFS based search algorithm
steps:	(1) k-core to narrow down the search space; 
	   	(2) generate subtrees of query vertex's tree; 
	   	(3) checking (both k-core and maximal subtree).
*/
public class BFS {
	private int[][] graph = null;
	private int[][] nodes = null;
	private int core[] = null;
	private int queryId= -1;
	private PTree pTree = null;
	private Map<Integer, PNode> pTreeMap=null;

	private Map<Set<Integer>, int[]> output=null;

	private boolean DEBUG = true;
	
	
	public BFS(int[][] graph, int[][] nodes){
		this.graph=graph;
		this.nodes=nodes;
		DecomposeKCore kCore=new DecomposeKCore(this.graph);
		core=kCore.decompose();
		this.pTree=new PTree();
		this.output=new HashMap<Set<Integer>, int[]>();
	}
	
	public void query(int queryId){
		this.queryId = queryId;
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
		List<int[]> currentPattern = new ArrayList<int[]>();
		List<Set<Integer>> currentUsers = new ArrayList<Set<Integer>>();
		currentPattern.add(seqStart);
		currentUsers.add(CKC);
		BFSMine(currentPattern, currentUsers);
		printOutput();
	}
	
	private void BFSMine(List<int[]> currentPattern,List<Set<Integer>> currentUsers){
		while(!currentPattern.isEmpty()){
			List<int[]> nextPattern = new ArrayList<int[]>();
			List<Set<Integer>> nextUsers = new ArrayList<Set<Integer>>();
			for(int index=0;index<currentPattern.size();index++){
				int[] pattern =currentPattern.get(index);
				Set<Integer> users = currentUsers.get(index);
				
				//generate new patterns
				List<int[]> nextSeqList = geneSubtree(pattern);
				//reach the leaf pattern 
				if(nextSeqList.size()==0 && isNewItems(pattern)){
					output.put(users, pattern);
				}else{
					boolean flag = true;
					for(int[] nextSeq : nextSeqList){
						Set<Integer> newUsers=check(nextSeq, users);
						if(newUsers!=null){
							flag = false;
							nextPattern.add(nextSeq);
							nextUsers.add(newUsers);
						}
					}
					if(flag && isNewItems(pattern)) 
						output.put(users, pattern);
				}
			}
			currentPattern=nextPattern;
			currentUsers=nextUsers;
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
		if(!isKCore(users)){
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
	private boolean isKCore(Set<Integer> set){
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
		System.out.println("Now testing BFS algorithm!");
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
	

}
