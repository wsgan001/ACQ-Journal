package hku.algo.index.edgeUpdate;

import java.util.*;
import hku.algo.TNode;

//**********************  Jan 7, 2017 CYK: TKDE algorithm for updating core number
public class TKDEalgoInsert {
	private  int[][] graph=null;
	private int[] core=null;
	private Map<Integer,Boolean> markMap=null;
	
	public TKDEalgoInsert(int[][] graph,int[] core){
		this.graph=graph;
		this.core=core;
		markMap=new HashMap<Integer, Boolean>();
	}
	
	//**********************  Dec 31, 2016 CYK: 这里只复制需要修改的边，同时引用未修改的；
	private void updateGraph(int u,int v){
				int[] newa=new int[graph[u].length+1];
				for(int j=0;j<newa.length-1;j++) newa[j]=graph[u][j];
					newa[newa.length-1]=v;
					this.graph[u]=newa;
			
				int[] newb=new int[graph[v].length+1];
				for(int j=0;j<newb.length-1;j++) newb[j]=graph[v][j];
					newb[newb.length-1]=u;
					this.graph[v]=newb;
	}
	
	//basic algorithm 
	private void markNode(int k,int idToUpdate){
		Queue<Integer> queue=new LinkedList<Integer>();
		queue.add(idToUpdate);
		markMap.put(idToUpdate,true);
		while(queue.size()>0){
			int curId=queue.poll();	
			for(int neighbor:graph[curId]){
				if(core[neighbor]==k){
					if(!markMap.containsKey(neighbor)){
						markMap.put(neighbor,true);
						queue.add(neighbor);
					}
				}
			}	
		}
	}
	
	
	private boolean computeX(int k,int queryId){
		int count=0;
		for(int neighbor:graph[queryId]){
			if(core[ neighbor ]>=k) count++;
		}
		if(count>k) return true;
		else return false;
	}
	
	//**********************  Jan 14, 2017 CYK: X pruning strategies 
	//using the lemma:Xu is the upper bound of Cu. Xu is the number of u's neighbor whose core number is >= Cu
	private void markNodeX(int k,int queryId){
		Queue<Integer> queue=new LinkedList<Integer>();
		queue.add(queryId);
		markMap.put(queryId,true);
		while(queue.size()>0){
			int curId=queue.poll();	
			if(computeX(k, curId)){// prune more than basic algorithm 
				for(int neighbor:graph[curId]){
					if(core[neighbor]==k){
						if(!markMap.containsKey(neighbor)){
							markMap.put(neighbor,true);
							queue.add(neighbor);
						}
					}
				}		
			}
		}
	}
	
	private boolean computeY(int k,int queryId){
		int count=0;
		for(int neighbor:graph[queryId]){
			if(core[ neighbor ]>k) count++;
		}
		if(count<k) return true;
		else return false;
	}
	
	private void markNodeY(int k,int queryId){
		Queue<Integer> queue=new LinkedList<Integer>();
		queue.add(queryId);
		markMap.put(queryId,true);
		while(queue.size()>0){
			int curId=queue.poll();	
			if(computeY(k, curId)){// prune more than basic algorithm 
				for(int neighbor:graph[curId]){
					if(core[neighbor]==k){
						if(!markMap.containsKey(neighbor)){
							markMap.put(neighbor,true);
							queue.add(neighbor);
						}
					}
				}		
			}
		}
	}
	
	
	private void reMarkNode(TNode x){
		int flag=0;
		int k=x.getCore();
		Iterator<Integer> iterator=markMap.keySet().iterator();
		while(iterator.hasNext()){
			int node=iterator.next();
				if(markMap.get(node)==true){
					int count=0;
					for(int neighbor:graph[node]){
						if(( markMap.containsKey(neighbor)&&(markMap.get(neighbor)==true) )|| core[neighbor]>k) count++;
					}
					if(count<=k){
						markMap.put(node, false);
						flag=1;
					}
				}
		}
		if(flag==1)  reMarkNode(x);
		
	}
	
	
	private Set<Integer> updateKcore(){
		Set<Integer> set=new HashSet<Integer>();
		Iterator<Integer> iterator=markMap.keySet().iterator();
		while(iterator.hasNext()){
			int node=iterator.next();
			if(markMap.get(node)==true) {
				core[node]++;
				set.add(node);
			}
		}
		return set;
	}
	
	// the main function 
	public Set<Integer> updateCore(TNode x,int nodeU,int nodeV,int idToUpdate){
		Set<Integer> set=new HashSet<Integer>();
		if(!edgeExist(nodeU, nodeV)){
			updateGraph(nodeU, nodeV);
//			markNode(x.getCore(),idToUpdate);
			markNodeX(x.getCore(),idToUpdate);
//			markNodeY(x.getCore(),idToUpdate);
			reMarkNode(x);
			set=updateKcore();
//			testMap(markMap);	
		}
		else {
			System.out.println("这里说明边已经存在");
		}
		return set;
	}
	
	
	private boolean edgeExist(int u,int v){
		for(int i=1;i<graph[u].length;i++){
			if(graph[u][i]==v) return true;
		}
		return false;
	}
	
	 private void testMap(){
		 int count=0;
		 Iterator<Integer> it=markMap.keySet().iterator();
			while(it.hasNext()){
				int node=it.next();
				if(markMap.get(node)==true) {
					count++;
					System.out.println("node: "+node+" value: "+markMap.get(node)+" core number: "+core[node]);
				}
			}
			System.out.println("nodeSet size: "+count);
	 }
	 
	public int[][] getGragh(){
		return graph; 
	}
	
	public int[] getCore(){
		return core;
	}
	
}
