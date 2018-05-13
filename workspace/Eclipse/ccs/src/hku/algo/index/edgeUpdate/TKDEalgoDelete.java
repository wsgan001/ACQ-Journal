package hku.algo.index.edgeUpdate;
import java.util.*;
import hku.algo.TNode;

public class TKDEalgoDelete {

	private  int[][] graph=null;
	private int[] core=null;
	private Map<Integer,Boolean> markMap=null;
	
	public TKDEalgoDelete(int[][] graph,int[] core){
		this.graph=graph;
		this.core=core;
		markMap=new HashMap<Integer, Boolean>();
	}
	
	private void updateGraph(int u,int v){
		int[] newa=new int[graph[u].length-1];
		int i=0;
		for(int x:graph[u]){
			if(x!=v)  newa[i++]=x;
		}
		this.graph[u]=newa;
	
		int[] newb=new int[graph[v].length-1];
		int j=0;
		for(int x:graph[v]){
			if( x!=u) newb[j++]=x;
		}
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
	
	//x pruning strategy 
	private boolean computeX(int k,int queryId){
		int count=0;
		for(int neighbor:graph[queryId]){
			if(core[ neighbor ]>=k) count++;
		}
		if(count<k) return true;
		else return false;
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
					if(count<k){
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
			if(markMap.get(node)==false) {
				core[node]--;
				set.add(node);
			}
		}
		return set;
	}
	
	//the main function 
	public Set<Integer> deleteEdge(int nodeU,int nodeV,TNode u,TNode v){
		Set<Integer> set=new HashSet<Integer>();
		if(edgeExist(nodeU, nodeV)){
			updateGraph(nodeU, nodeV);
			if(u.getCore()>v.getCore()){
				if(computeX(v.getCore(), nodeV)){
					markNode(v.getCore(), nodeV);
					reMarkNode(v);
					set=updateKcore();
				}
				
			}else if(u.getCore()<v.getCore()){
				if(computeX(u.getCore(), nodeU)){
					markNode(u.getCore(), nodeU);
					reMarkNode(u);
					set=updateKcore();
				}
			}else{
				markNode(u.getCore(), nodeU);
				if(!markMap.containsKey(v)){
					if(computeX(u.getCore(), nodeU)){
						reMarkNode(u);
					}
					if(computeX(v.getCore(), nodeV)){
						markNode(v.getCore(),nodeV);
						reMarkNode(v);
					}
					set=updateKcore();
				}else{
					if(computeX(u.getCore(), nodeU)){
						reMarkNode(u);
						set=updateKcore();
					}
				}
			}
		}else{
			System.out.println("edge does not exist!");
		}
		return set;
	}
	
	private boolean edgeExist(int u,int v){
		for(int i=0;i<graph[u].length;i++){
			if(graph[u][i]==v) return true;
		}
		return false;
	}
	
	public void output(){
		Iterator<Integer> set=markMap.keySet().iterator();
		while(set.hasNext()){
			int x=set.next();
			System.out.println(x+" "+markMap.get(x));
		}
		
		System.out.println("test output: ");
		System.out.println();
		for(int i=1;i<graph.length;i++){
			int[] g=graph[i];
			for(int x:g) System.out.print(x+" ");
			System.out.println();
		}
	}
	
	public void coreout(){
		for(int i=0;i<core.length;i++) System.out.println(i+" "+core[i]);
	}
	
	public int[][] getGraph(){return this.graph;}
	
	public int[] getCore(){return this.core;}
	
}
