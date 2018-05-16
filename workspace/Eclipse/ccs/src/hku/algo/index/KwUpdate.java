package hku.algo.index;

import java.util.Map;
import java.util.Random;

import javax.xml.crypto.Data;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;

//**********************  Dec 23, 2016 CYK:  online update keyword class
public class KwUpdate {
//	private TNode root=null;
	private TNode invert[] = null;
	
	public KwUpdate(TNode[] invert){
//		this.root=root;
		this.invert=invert;
		}
	
	public void addKw(int node, String keyword){
		TNode tNode=invert[node];
		Map<String, int[]> kwMap=null;
		int[] nodearr=null;
		kwMap=tNode.getKwMap();
		if(kwMap!=null){
			if(kwMap.containsKey(keyword)){
				Boolean vertexHaskwrd=false;
				nodearr=kwMap.get(keyword);
				for(int i=0;i<nodearr.length;i++){
					if(nodearr[i]==node) {
						System.out.println("node and new keyword has already exists.");
						 vertexHaskwrd=true;
						break;
					}
				}
				if(!vertexHaskwrd){
						int[] newarr=new int[nodearr.length+1];
						for(int i=0;i<nodearr.length;i++) newarr[i]=nodearr[i];
						newarr[newarr.length-1]=node;
						kwMap.put(keyword, newarr);
				}		
			}else{
				
				int[] newarr={node};
				kwMap.put(keyword, newarr);
			}
		}else	System.out.println("the node containing this vertex have no keyword!");
		
	}
	
	public void deleteKw(int node, String keyword){
		TNode tNode=invert[node];
		Map<String, int[]> kwMap=null;
		int[] nodearr=null;
		kwMap=tNode.getKwMap();
		if(kwMap!=null){
			if(kwMap.containsKey(keyword)){
				Boolean vertexHaskwrd=false;
				nodearr=kwMap.get(keyword);
				int index=-1; // record the location of vertex which will be delete keyword
				for(int i=0;i<nodearr.length;i++){
					if(nodearr[i]==node) {
						 vertexHaskwrd=true;
						 index=i;
						break;
					}
				}
				if(vertexHaskwrd){
					if(nodearr.length==1){
						kwMap.remove(keyword);
					} 
					else{
						int[] newarr=new int[nodearr.length-1];
						for(int i=0;i<index;i++) 			   	 newarr[i]=nodearr[i];
						for(int i=index+1;i<nodearr.length;i++)  newarr[i-1]=nodearr[i];
						kwMap.put(keyword, newarr);
						}
				}
				else	System.out.println("vertex "+node+" does not has this keyword ");
			}else	System.out.println("vertex "+node+" does not has this keyword ");
		}else	System.out.println("the node containing this vertex have no keyword!");
	}
	
	public double test(int[][] graph,String[][] nodes,int num,String add,boolean isAdd){
		long time1=System.nanoTime();
		if(isAdd){
			for(int i=0;i<num;i++){
				Random random=new Random();
				int x=random.nextInt(graph.length-1)+1;
				addKw(x, add);
			}
		}else{
			for(int i=0;i<num;i++){
				Random random=new Random();
				int x=random.nextInt(graph.length-1)+1;
				int y=random.nextInt(nodes[x].length-1)+1;
				deleteKw(x,nodes[x][y]);
			}
			
		}
		double kwupdate=System.nanoTime()-time1;
		return kwupdate/num;
	}
	
	
	public static void main(String[] args){
//		int graph[][] = new int[11][];
//		int a1[] = {2, 3, 4, 5};	graph[1] = a1;
//		int a2[] = {1, 3, 4,5};		graph[2] = a2;
//		int a3[] = {1, 2, 4};		graph[3] = a3;
//		int a4[] = {1, 2, 3, 6};	graph[4] = a4;
//		int a5[] = {1, 2, 7};		graph[5] = a5;
//		int a6[] = {4};			graph[6] = a6;
//		int a7[] = {5};			graph[7] = a7;
//		int a8[] = {9};				graph[8] = a8;
//		int a9[] = {8};	graph[9] = a9;
//		int a10[] = {};	graph[10] = a10;
//	
//		String nodes[][] = new String[graph.length][];
//		String b1[] = {"w", "x", "y"};	nodes[1] = b1;
//		String b2[] = {"x"};	nodes[2] = b2;
//		String b3[] = {"x", "y"};	nodes[3] = b3;
//		String b4[] = {"z", "x", "y"};	nodes[4] = b4;
//		String b5[] = {"y", "z"};	nodes[5] = b5;
//		String b6[] = {"y"};	nodes[6] = b6;
//		String b7[] = {"x", "y"};	nodes[7] = b7;
//		String b8[] = {"y", "z"};	nodes[8] = b8;
//		String b9[] = {"x"};	nodes[9] = b9;
//		String b10[] = {"x"};	nodes[10] = b10;
//		AdvancedIndex ac=new AdvancedIndex(graph, nodes);
//		TNode root=ac.build();
//		
//		KwUpdate kwUpdate=new KwUpdate(ac.getInvert());
//		kwUpdate.addKw(1,"bjbh");
//		kwUpdate.deleteKw(1,"x");
//		ac.traverse(root);
		
		DataReader dReader=new DataReader(Config.flickrGraph, Config.flickrNode);
		int[][] graph=dReader.readGraph();
		String[][] nodes=dReader.readNode();
		long time=System.nanoTime();
		AdvancedIndex ad=new AdvancedIndex(graph, nodes);
		ad.build();
		long rebuild=System.nanoTime()-time;
		
		KwUpdate kUpdate=new KwUpdate(ad.getInvert());
		
		double kwupdate=kUpdate.test(graph, nodes, 1000, "cyk", false);
		
		System.out.println(rebuild/kwupdate);
		
	}
}