package algorithm.basic;

import java.util.*;

public class PTree {
	private Map<Integer, PNode> pTree=null;
	private PNode root=null;
	
	public PTree(){
		this.pTree=new HashMap<Integer,PNode>();
	}
	
	public void add(int id,PNode node){
		pTree.put(id,node);
	}
	
	public Map<Integer, PNode> getPtree(){
		return this.pTree;
	} 
	
	public void loadPTree(int[] seq){
		System.out.println("loading");
	}
	
	public List<Integer> tracePath(int x){
		List<Integer> path=new ArrayList<Integer>();
		while(pTree.get(x).getFather()!=null){
			path.add(x);
			x=pTree.get(x).getFather().getId();
		}
		path.add(x);
		Collections.reverse(path);
		return path;
	}
	
	
	public Map<Integer, PNode> testLoadTree(){
		Map<Integer,int[]> map=new HashMap<Integer,int[]>();
		int[]a1={2,3,10,12}; map.put(1, a1);
		int[]a2={};map.put(2, a2);
		int[]a3={4,7};map.put(3, a3);
		int[]a4={5,6};map.put(4, a4);
		int[]a5={};map.put(5, a5);
		int[]a6={};map.put(6, a6);
		int[]a7={8};map.put(7, a7);
		int[]a8={9};map.put(8, a8);
		int[]a9={};map.put(9, a9);
		int[]a10={11};map.put(10, a10);
		int[]a11={};map.put(11, a11);
		int[]a12={13,15};map.put(12, a12);
		int[]a13={14};map.put(13, a13);
		int[]a14={};map.put(14, a14);
		int[]a15={};map.put(15, a15);
		
		Iterator<Integer> it=map.keySet().iterator();
		while(it.hasNext()){
			int index=it.next();
			if(!pTree.containsKey(index)){
				PNode node=new PNode(index);
				for(int x:map.get(index)){
					PNode xnode=new PNode(x);
					xnode.setFather(node);
					node.addPNode(xnode);
					pTree.put(x,xnode);
				}
				pTree.put(index,node);
			}
			else{
				PNode node=pTree.get(index);
				for(int x:map.get(index)){
					PNode xnode=new PNode(x);
					xnode.setFather(node);
					node.addPNode(xnode);
					pTree.put(x,xnode);
				}
			}
		}
		root=pTree.get(1);
		return pTree;
	}
	
	public void testMap(){
		Iterator<Integer> it=pTree.keySet().iterator();
		while(it.hasNext()){
			int index=it.next();
			System.out.println("ID: "+index+"  PNode "+pTree.get(index).getId());
			
		}
	}
	
	public String toString() {
		String temp ="";
		// append child nodes
		temp += root.toString("");
		return temp;
	}
	
	public static void main(String[] args){
		PTree pTree=new PTree();
		pTree.testLoadTree();
//		pTree.testMap();
		List<Integer> list=pTree.tracePath(7);
		System.out.println(list.toString());
	}
	
}
