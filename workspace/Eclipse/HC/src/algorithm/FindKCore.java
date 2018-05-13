
package algorithm;


/**
@author chenyankai
@Date	Jul 27, 2017
To find k-core( not necessarily connected)
*/
public class FindKCore {
	private int graph[][]=null;
	private int n=-1;
	private int deg[]= null;
	private int k=-1;
	
	public FindKCore(int[][] graph,int k){
		this.graph=graph;
		this.n=graph.length-1;
		this.k=k;
	}
	
	public int[] decompose(){
		
		deg=new int[n+1];
		
		//step 1: obtain the degree and maximum degree
		int md=-1;
		for(int i = 1;i < n+1;i ++){
			deg[i] = graph[i].length;
			if(deg[i] > md){
				md = deg[i];
			}
		}
		
		//step 2: fill the bin
		int bin[] = new int[md + 1];
		for(int i = 1;i < n+1;i ++){
			bin[deg[i]] += 1;
		}
		
		//step 3: update the bin
		int start = 1;// from 1?
		for(int d = 0; d <= md;d ++){
			int num = bin[d];
			bin[d] = start;
			start += num;	
		}
		
		//step 4: find the position
		int pos[] = new int[n + 1];
		int vert[] = new int[n + 1];
		for(int v = 1; v <= n;v ++){
			pos[v] = bin[deg[v]];
			vert[pos[v]] = v;
			bin[deg[v]] += 1;
		}
		
		for(int d = md; d >= 1; d--){
			bin[d] = bin[d - 1];
		}
		bin[0] = 1;
		
		//step 5: decompose
		for(int i = 1;i <= n;i ++){
			int v = vert[i];
			for(int j = 0;j < graph[v].length;j ++){
				int u = graph[v][j];
				if(deg[u] > deg[v]){
					int du = deg[u];   int pu = pos[u];
					int pw = bin[du];  int w = vert[pw];
					if(u != w){
						pos[u] = pw;   vert[pu] = w;
						pos[w] = pu;   vert[pw] = u;
					}
					bin[du] += 1;
					deg[u] -= 1;
				}
			}
//			System.out.println("deg[" + v + "]=" + deg[v]);
			if(deg[v] > k)   break;//a new line of code added on Oct 27, 2015
		}
		return deg;
	}
	
	public static void main(String[] args) {
		int graph[][] = new int[11][];
		int a1[] = {2, 3, 4, 6}; graph[1] = a1;
		int a2[] = {1, 3, 4, 6}; graph[2] = a2; 
		int a3[] = {1, 2, 4, 5}; graph[3] = a3;
		int a4[] = {1, 2, 3, 5}; graph[4] = a4;
		int a5[] = {3, 4};       graph[5] = a5;
		int a6[] = {1, 2, 7};    graph[6] = a6;
		int a7[] = {6};          graph[7] = a7;
		int a8[] = {9};          graph[8] = a8;
		int a9[] = {8};          graph[9] = a9;
		int a10[] = {};          graph[10] = a10;
		
		FindKCore kcore = new FindKCore(graph, 1);
		int core[] = kcore.decompose();
		for(int i = 1;i < core.length;i ++)   System.out.print("core[" + i + "]=" + core[i] + " ");
		System.out.println();
	}
	
	
}
