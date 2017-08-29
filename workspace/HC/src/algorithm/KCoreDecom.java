package algorithm;
/**
@author chenyankai
@Date	Aug 9, 2017
k core decomposition 
Use list<list<>> to store the adjacency matrix 
*/
import java.util.*;

public class KCoreDecom {
	private List<List<Integer>> graph = null;
	private int n = -1;
	private int[] degree = null;
	private int[] reverseCore =null;
	
	public KCoreDecom(List<List<Integer>> g){
		this.graph=g;
		this.n=graph.size()-1;
		this.reverseCore = new int[n+1]; 
		
	}
	
	public int[] decompose(){
		degree = new int[n+1]; 
		//step 1:get the degree and the maximum degree
		int maxD=-1;
		for(int i=1;i<=n;i++){
			degree[i]=graph.get(i).size();
			maxD= (degree[i]>maxD)?degree[i]:maxD;
		}
		
		//step 2:fill the bin
		int[] bin= new int[maxD+1];
		for(int i=1;i<n+1;i++){
			bin[degree[i]] += 1;
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
			for(int j = 0;j < graph.get(v).size();j ++){
				int u = graph.get(v).get(j);
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
			reverseCore[n-i+1]=v;
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
		return reverseCore;
	}
	
	public static void main(String[] args){
		List<List<Integer>> graph= new ArrayList<List<Integer>>();
		List<Integer> list=new ArrayList<Integer>();
		graph.add(list);
		
		List<Integer> list1=Arrays.asList(2, 3, 4, 5); graph.add(list1);
		List<Integer> list2=Arrays.asList(1, 3, 4, 5); graph.add(list2);
		List<Integer> list3=Arrays.asList(1, 2, 4); graph.add(list3);
		List<Integer> list4=Arrays.asList(1, 2, 3, 6); graph.add(list4);
		List<Integer> list5=Arrays.asList(1, 2, 7); graph.add(list5);
		List<Integer> list6=Arrays.asList(4); graph.add(list6);
		List<Integer> list7=Arrays.asList(5); graph.add(list7);
		List<Integer> list8=Arrays.asList(9); graph.add(list8);
		List<Integer> list9=Arrays.asList(8); graph.add(list9);
		List<Integer> list10=Arrays.asList(); graph.add(list10);
		
		KCoreDecom kDecom=new KCoreDecom(graph);
//		int[] degree=kDecom.decompose();
//		for(int x:degree) System.out.println(" >>  "+x);
//		int reversecoreArr[] = kDecom.getReverseCore();
//		for(int i = 1;i < reversecoreArr.length;i ++)   System.out.print("cor[" + i + "]=" + reversecoreArr[i] + " ");

		
	}
	
	
}
