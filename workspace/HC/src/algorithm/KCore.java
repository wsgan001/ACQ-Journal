package algorithm;
/**
@author chenyankai
@Date	Jul 4, 2017
*vertex IDs range from 1 to n;
*/

public class KCore {
	private int[][] graph=null;
	private int n=-1; //(size-1) of the vertices in graph
	private int[] degree=null;
	private int[] coreReverse=null;
	
	public KCore(int[][] graph){
		this.graph=graph;
		this.n=graph.length-1;
		this.coreReverse=new int[n+1];
	}
	
	public int[] decompose(){
		degree=new int[n+1];
		
		//step 1:get the degree and the maximum degree
		int maxD=-1;
		for(int i=1;i<n+1;i++){
			degree[i]=graph[i].length;
			maxD= (degree[i]>maxD)?degree[i]:maxD;
		}
	
		//step 2:fill the bin
		int[] bin=new int[maxD+1];
		for(int i=1;i<n+1;i++){
			bin[degree[i]]+=1;
		}
		
		//step 3:update the bin
		int start=1;
		for(int d=0;d<maxD+1;d++){
		int num=bin[d];
		bin[d]=start;
		start+=num;
		}
		
		//step 4:find the position
		int pos[]=new int[n+1];
		int ver[]=new int[n+1];
		for(int v=1;v<n+1;v++){
			pos[v] = bin[degree[v]];
			ver[pos[v]]=v;
			bin[degree[v]]+=1;
		}
		
		for(int d=maxD;d>=1;d--){
			bin[d]=bin[d-1];
		}
		
		bin[0]=1;
		
		//step 5:decompose
		for(int i=1;i<n+1;i++){
			int v=ver[i];
			for(int j = 0;j < graph[v].length;j ++){
				int u = graph[v][j];
				if(degree[u] > degree[v]){
					int du = degree[u];   int pu = pos[u];
					int pw = bin[du];  int w = ver[pw];
					if(u != w){
						pos[u] = pw;   ver[pu] = w;
						pos[w] = pu;   ver[pw] = u;
					}
					bin[du] += 1;
					degree[u] -= 1;
				}
			}
			coreReverse[n-i+1]=v;
		}
		return degree;
	}
	
	public int obtainMaxCore(){
		int max=-1;
		for(int i=1;i<degree.length;i++){
			max=degree[i]>max?degree[i]:max;
		}
		return max;
	}
	
	public int[] getReverseCore(){
		return coreReverse;
	}
	
	public static void main(String[] args) {
		int graph[][] = new int[11][];
		int a1[] = {2, 3, 4, 5};	graph[1] = a1;
		int a2[] = {1, 3, 4,5};		graph[2] = a2;
		int a3[] = {1, 2, 4};		graph[3] = a3;
		int a4[] = {1, 2, 3, 6};	graph[4] = a4;
		int a5[] = {1, 2, 7};		graph[5] = a5;
		int a6[] = {4};			graph[6] = a6;
		int a7[] = {5};			graph[7] = a7;
		int a8[] = {9};				graph[8] = a8;
		int a9[] = {8};	graph[9] = a9;
		int a10[] = {};	graph[10] = a10;
		
		
		
		KCore kcore = new KCore(graph);
		kcore.decompose();
		int reversecoreArr[] = kcore.getReverseCore();
		int maxCore = kcore.obtainMaxCore();
		for(int i = 1;i < reversecoreArr.length;i ++)   System.out.print("cor[" + i + "]=" + reversecoreArr[i] + " ");
		System.out.println();
		for(int i=1;i < kcore.degree.length;i++)
			System.out.print("degree["+i+"]="+kcore.degree[i]+" ");
		//System.out.println("maxCore:" + maxCore);
	}
	
	
}
