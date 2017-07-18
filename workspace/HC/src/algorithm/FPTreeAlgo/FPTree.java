package algorithm.FPTreeAlgo;

import java.util.*;
import java.util.Map.Entry;

/**
@author chenyankai
@Date	Jul 18, 2017

construction of FP-tree
*/

public class FPTree {
 private FPNode root=null;
 
 //use core number of k-core as the minSup
 private int k=-1;
 
 //list of items in the header table with descending order
 private Map<Integer,Integer> headerMap=null;
 
 //map of Entry<item, FPNode> of the header table 
 private Map<Integer, FPNode> itemNodeMap=null;
 
 //map of Entry<item, Last_FPNode> of the header table 
 //for fast locate the last FPNode of the header table
 private Map<Integer, FPNode> itemLastNodeMap=null;
 
 
 public FPTree(int k){
	 this.root = new FPNode(-1, 0);
	 this.k=k-1;
	 headerMap = new HashMap<Integer,Integer>();
	 this.itemNodeMap = new HashMap<Integer,FPNode>();
	 this.itemLastNodeMap = new HashMap<Integer,FPNode>();
	  
 }
 
 
 
 public FPNode construct(Map<Integer, int[]> database){
	//step 1: scan the DB can obtain frequent items 
			Scan(database);
	//step 2:construct fp-tree
			Set<Integer> keySet=database.keySet();
			for(int index:keySet){
				int[] seq=database.get(index);
				List<Integer> list=selectSort(seq);
//				for(int x:list) System.out.println((char)x);
				insert(list);
			}
			return root;
	 
 }
 
 //scan the database and create headerList
 private void Scan(Map<Integer, int[]> database){
		//step1: counting tree nodes and its support
			Map<Integer, Integer> SupMap=new HashMap<Integer,Integer>();
			Iterator<Integer> it=database.keySet().iterator();
			while(it.hasNext()){
				int index=it.next();
				for(int x:database.get(index)){
					if(SupMap.containsKey(x)) SupMap.put(x, SupMap.get(x)+1);
					else SupMap.put(x, 1);
				}
			}
				//step2: clear out those whose sup <=k
			Iterator<Integer> it1=SupMap.keySet().iterator();
			while(it1.hasNext()){
				int index=it1.next();
				if(SupMap.get(index)<=k) it1.remove();// note here should use iterator to delete items
			}
				
			//step3: sort frequent items with descending order sup and return 
			List<Map.Entry<Integer, Integer>> tmpList=new LinkedList<Map.Entry<Integer,Integer>>(SupMap.entrySet());
			Collections.sort(tmpList,new Comparator<Map.Entry<Integer, Integer>>() {
				@Override
				public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
					// TODO Auto-generated method stub
					//descending order
					return o1.getValue()>o2.getValue()?-1:1;
				}
			});
			
			//step 4: return 
			int order=0;
			for(Entry<Integer, Integer> entry:tmpList)  headerMap.put(entry.getKey(),order++);
			
	 }
  
 private List<Integer> selectSort(int[] seq){
		
		int[] tmp=new int[headerMap.size()];
		for(int x:seq){
			if(headerMap.containsKey(x)){
				tmp[headerMap.get(x)]=x;
			}
		}
		List<Integer> list=new LinkedList<Integer>();
		for(int x:tmp){
			if(x!=0) list.add(x);
		} 
		return  list;
	}
 
 //insert one transaction in the FP-tree
 private void insert(List<Integer> transaction){
	 FPNode currentNode = root;
	 for(int item : transaction){
		 FPNode child = currentNode.hasChild(item);
		 if(child == null ){
//			 System.out.println((char)item+"  null");
				FPNode node =new FPNode(item, 1);	
				node.linkFather(currentNode);
				currentNode.linkChild(node);
				UpdateNodeLinks(item, node);
				
				currentNode=node;
		 }
		 else{
				child.IncCount();
				currentNode=child;
		 }
		 
	 } 
 }
 
 //maintain two Map:itemNodeMap and itemLastNodeMap
 private void UpdateNodeLinks(int item, FPNode newNode){
	 if(itemLastNodeMap.containsKey(item)){
		 FPNode lastNode=itemLastNodeMap.get(item);
		 lastNode.linkBrother(newNode);
	 }
	 //update the itemLastMap with newNode
	itemLastNodeMap.put(item, newNode);
	
	//put <item,newNode> itmeNodeMap if it does not contains item
	FPNode headNode= itemNodeMap.get(item);
	if(headNode == null){ itemNodeMap.put(item, newNode); }
 }
	
 public void traverse(FPNode root){
		System.out.println("item "+(char)root.getItem()+" count "+root.getCount());
		Set<FPNode> nodeSet=root.getChild(); 
		for(FPNode node:nodeSet) traverse(node);
		
	}
 
 public static void main(String[] args){
		Map<Integer, int[]> map=new HashMap<Integer,int[]>();
		int[] a1={1,2,3,4,5,7}; map.put(1,a1);
		int[] a2={1,3,5,7,9}; map.put(2,a2);
		int[] a3={1,2,3,5,8}; map.put(3,a3);
		int[] a4={1,2,4,5,7}; map.put(4,a4);
		int[] a5={1,2,3}; map.put(5,a5);
		int[] a6={1,2,3,4,5,7,10,11}; map.put(6,a6);
		
		Map<Integer, int[]> map2=new HashMap<Integer,int[]>();
		int[] b1={'f','a','c','d','g','i','m','p'}; map2.put(1, b1);
		int[] b2={'a','b','c','f','l','m','o'}; map2.put(2, b2);
		int[] b3={'b','f','h','j','o'}; map2.put(3, b3);
		int[] b4={'b','c','k','s','p'}; map2.put(4, b4);
		int[] b5={'a','f','c','e','l','p','m','n'}; map2.put(5, b5);

		FPTree fpTree=new FPTree(3);
		fpTree.construct(map2);
//		fpTree.printMap();
//		System.out.println(fpTree.singlePath(root));	
		
		}
	
 
}
