package algorithm.FPTreeAlgo;

import java.util.*;
import java.util.Map.Entry;
/**
@author chenyankai
@Date	Jul 11, 2017
old version of FPTree.java 
*/
public class FPGrowth {

	private Map<Integer, int[]> database=null;
	private int k=-1;
	private Map<Integer, FPNode> itemFPNodeMap=null;
	
	public FPGrowth(Map<Integer, int[]> DB,int k){
		this.database=DB;
		this.k=k-1;
		this.itemFPNodeMap=new HashMap<Integer,FPNode>();
	}
	
	//FP-tree growth algorithm
	public FPNode Construct(){
		//step 1: scan the DB can obtain frequent items 
		Map<Integer, Integer> countingMap=Scan();
		
		//step 2:initial root FP-tree
		FPNode root=new FPNode(-1, 0);
		Set<Integer> keySet=database.keySet();
		for(int index:keySet){
			int[] seq=database.get(index);
			LinkedList<Integer> list=select_sort(seq, countingMap);
//			for(int x:list) System.out.println((char)x);
			insert(list,root);
		}
		traverse(root);
		return root;
	}
	
	private void insert(LinkedList<Integer> list, FPNode father){
		if(!list.isEmpty()){
			int head=list.getFirst();
			list.removeFirst();
		
			FPNode child=father.hasChild(head);
			
			if(child!=null){
//				System.out.println("now "+(char)child.getItem()+"  "+(char)head+"  !null");
				child.IncCount();
				insert(list, child);
			}
			else{
//			System.out.println((char)head+"  null");
				FPNode node =new FPNode(head, 1);
				node.linkFather(father);
				father.linkChild(node);
				
				if(itemFPNodeMap.containsKey(head)){
					FPNode tmp=itemFPNodeMap.get(head);
					while(tmp.hasBro()) tmp=tmp.getBro();
					tmp.linkBrother(node);
				}else{
					itemFPNodeMap.put(head, node);
				}
				insert(list, node);
			}
		}
	}
	
	private Map<Integer,Integer> Scan(){
		//step1: counting tree nodes and its support
		Map<Integer, Integer> freSupMap=new HashMap<Integer,Integer>();
		Iterator<Integer> it=database.keySet().iterator();
		while(it.hasNext()){
			int index=it.next();
			for(int x:database.get(index)){
				if(freSupMap.containsKey(x)) freSupMap.put(x, freSupMap.get(x)+1);
				else freSupMap.put(x, 1);
			}
		}
		//step2: clear out those whose sup <=k
		Iterator<Integer> it1=freSupMap.keySet().iterator();
		while(it1.hasNext()){
			int index=it1.next();
			if(freSupMap.get(index)<=k) it1.remove();// note here should use iterator to delete items
		}
		
		//step3: sort frequent items with descending order sup
		List<Map.Entry<Integer, Integer>> tmpList=new LinkedList<Map.Entry<Integer,Integer>>(freSupMap.entrySet());
		Collections.sort(tmpList,new Comparator<Map.Entry<Integer, Integer>>() {
			@Override
			public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
				// TODO Auto-generated method stub
				//descending order
				return o1.getValue()>o2.getValue()?-1:1;
			}
		});
//		
//		for(Map.Entry<Integer, Integer> map:tmpList){
//			System.out.println(map.getKey()+" times "+map.getValue());
//		}
//		
		//step4: return list
		Map<Integer, Integer> countingMap=new HashMap<Integer,Integer>(); //Key-items, value-order
//		List<Integer> list=new ArrayList<Integer>();
		int order=0;
		for(Entry<Integer, Integer> entry:tmpList)  countingMap.put(entry.getKey(),order++);
		
		
		return countingMap;
	}

	//select and sort frequent items in above descending order 
	private LinkedList<Integer> select_sort(int[] seq,Map<Integer, Integer> countingMap){
		LinkedList<Integer> list=new LinkedList<Integer>();

		int[] tmp=new int[countingMap.size()];
		for(int x:seq){
			if(countingMap.containsKey(x)){
				tmp[countingMap.get(x)]=x;
			}
		}
		for(int x:tmp){
			if(x!=0) list.add(x);
		} 
		return  list;
	}	
	
	
//************************************************************************************************************************
// mining algorithm
	
	public void mining(FPNode root,int a){
		
		if(singlePath(root)){
		
		}
	}
	
	private boolean singlePath(FPNode root){
		boolean flag=true;
		if(root.getChild().size()==0) return false;
		
		while(root.getChild().size()!=0){
			if(root.getChild().size()!=1) {
				flag=false;
				break;
			}
			root=root.getChild().iterator().next();
		}
		return flag;
	}
	
	
	
	
	public void traverse(FPNode root){
		System.out.println("item "+(char)root.getItem()+" count "+root.getCount());
		Set<FPNode> nodeSet=root.getChild(); 
		for(FPNode node:nodeSet) traverse(node);
		
	}
	
	public void printMap(){
		Set<Integer> keySet=itemFPNodeMap.keySet();
		for(int x:keySet){
			System.out.println(x);
			FPNode node=itemFPNodeMap.get(x);
			System.out.println(node.hasBro());
			while(node.hasBro()){
				System.out.println((char)node.getItem()+" here times "+node.getCount());
				node=node.getBro();
			}
			System.out.println((char)node.getItem()+" here times "+node.getCount());
		}
		
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

		FPGrowth fpTree=new FPGrowth(map2, 3);
		FPNode root=fpTree.Construct();
//		fpTree.printMap();
//		System.out.println(fpTree.singlePath(root));	
		
		
		}
	
	
	
}
