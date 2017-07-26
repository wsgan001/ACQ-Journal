package algorithm.basic;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntPredicate;

import javax.swing.ListModel;

import algorithm.DecomposeKCore;
import algorithm.FindCKCore;
import algorithm.LCS;
import config.Config;

/**
@author chenyankai
@Date	Jul 6, 2017
steps: (1) k-core; (2) maximal k+1 frequent subtree mining; (3) k-core. 
*/


public class BasicAlgorithm {
	private int graph[][]=null;//graph structure
	private int nodes[][]=null;//the tree nodes of each node
	private int core[]=null;
	private int queryId=-1;
	private int minSup=Config.k+1;
	private PTree pTree=null;
	private Map<Integer, PNode> pTreeMap=null;
	
	private boolean DEBUG=false;
	

	public BasicAlgorithm(int graph[][],int nodes[][]){
		this.graph=graph;
		this.nodes=nodes;
		DecomposeKCore kCore=new DecomposeKCore(graph);
		core=kCore.decompose();
		this.pTree=new PTree();
	}
	
	public void query(int queryId){
		this.queryId=queryId;
		if(core[queryId]<Config.k){
			System.out.println("No qualified connected k-core!");
			return;
		}
		
		//step 1:find the connected k-core containing queryId
		FindCKCore findCKCore=new FindCKCore();
		int[] CKC=findCKCore.findCKC(graph, core, queryId);
		for(int x:CKC) System.out.println(x);
		if(CKC.length<Config.k+1) return; 
		
		//step 2:lcs and filter those shared no subsequences
		LCS lcs=new LCS();
		Map<Integer, int[]> midRslt=new HashMap<Integer, int[]>();
		for(int vertex:CKC){
//			System.out.println(queryId+" __ "+vertex+" __ "+nodes[queryId]+"_____"+nodes[vertex]);
			if(vertex!=queryId){
				int[] tmp=lcs.lcs(nodes[queryId], nodes[vertex]);
				if(tmp.length!=0) midRslt.put(vertex, tmp);
			}else{
				pTree.loadPTree(nodes[vertex]);
				pTreeMap=pTree.getPtree();
			}
		}
		if(midRslt.size()<Config.k+1) return;
		
		Set<Integer> key=midRslt.keySet();
		for(int x:key) {
			int [] tmp=midRslt.get(x);
			System.out.println(x+": ");
			for(int y:tmp) System.out.print(y+"  ");
			System.out.println();
		}
		
		//step 3: mining all maximal common subsequences
	}
	
	
	public void mineMaxSubtree(){
		pTree=new PTree();
		pTreeMap=pTree.testLoadTree();
		int[]seq={1};
		enumerate(seq);
	}
	
	private void enumerate(int[] seq){
		List<int[]> nextSeqList= geneSubtree(seq);
		for(int[] nextSeq:nextSeqList){
			if(nextSeq.length>6) break;
			System.out.print("checking next seq:" );
			for(int x:nextSeq) System.out.print(x+"  ");
			System.out.println();
			enumerate(nextSeq);
		}

	}
	
	//generate a new subtree from a subtree by add an node in the right most path
	private List<int[]>  geneSubtree(final int[] seq){
		List<Integer> rightmostPath=getRightmostPath(seq);
		//------------------------DEBUG------------------------------
		if(DEBUG) System.out.println("rightmost path:"+rightmostPath.toString() );
		//----------------------END DEBUG----------------------------
		List<int[]> nextSeq=new ArrayList<int[]>();
		for(int i=0;i<rightmostPath.size()-1;i++){
			int father=rightmostPath.get(i);
			int child=rightmostPath.get(i+1);
			List<Integer> childSet=pTreeMap.get(father).getChildName();
			if(child<childSet.get(childSet.size()-1)){
				for(int x:childSet){
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
		List<Integer> childSet=pTreeMap.get(lastOne).getChildName();
			for(int x:childSet){
					int[] b=new int[seq.length+1];
					System.arraycopy(seq, 0, b, 0, seq.length);
					b[b.length-1]=x;
					nextSeq.add(b);
					//------------------------DEBUG------------------------------
					if(DEBUG){
						for(int q:b) {
							System.out.print(q+" ");
						}
						System.out.println();
					}
					//----------------------END DEBUG----------------------------
			}
	
		
		return nextSeq;
	}
	
	//get the rightmost path of one subtree pattern
	private List<Integer> getRightmostPath(final int[] seq){
		return pTree.tracePath(seq[seq.length-1]);
	}
	
	
	
	public static void main(String[] args){
		int graph[][] = new int[11][];
		int a1[] = {2, 3, 4, 5};graph[1] = a1;
		int a2[] = {1, 3, 4, 5};graph[2] = a2;
		int a3[] = {1, 2, 4};	graph[3] = a3;
		int a4[] = {1, 2, 3, 7};graph[4] = a4;
		int a5[] = {1, 2, 6};	graph[5] = a5;
		int a6[] = {5};			graph[6] = a6;
		int a7[] = {4};			graph[7] = a7;
		int a8[] = {9};			graph[8] = a8;
		int a9[] = {8};			graph[9] = a9;
		int a10[] = {};			graph[10] = a10;
		
		
		int nodes[][] = new int[11][];
		int k1[] = {1,2,3,4,6,7,9,12};nodes[1] = k1;
		int k2[] = {1,3,4,7,8,9,12};				nodes[2] = k2;
		int k3[] = {1,5,6,8};				nodes[3] = k3;
		int k4[] = {1,2,7,9,12};		nodes[4] = k4;
		int k5[] = {1,4,7,8,9,11};			nodes[5] = k5;
		int k6[] = {1,2,3,6,9,12};				nodes[6] = k6;
		int k7[] = {1,7,9};			nodes[7] = k7;
		int k8[] = {1,4,7,8,11};			nodes[8] = k8;
		int k9[] = {1,2,3,6,9,12};				nodes[9] = k9;
		int k10[] = {1,7,9,12};			nodes[10] = k10;
		
		BasicAlgorithm bAlgo=new BasicAlgorithm(graph, nodes);
		Config.k=2;
		
//		bAlgo.query(1);
		int[]seq={1};
		bAlgo.mineMaxSubtree();
		
	}
	

	
}
