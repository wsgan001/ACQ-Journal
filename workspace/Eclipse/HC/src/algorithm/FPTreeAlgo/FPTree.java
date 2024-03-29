package algorithm.FPTreeAlgo;

import java.util.*;


/**
@author chenyankai
@Date	Jul 18, 2017

construction of FP-tree
*/

public class FPTree {
// o mark the root node
 private FPNode root=null;
 
 //use core number of k-core as the minSup
 private int minSup=-1;
  
 private List<Integer> headerList= null;
  
 //map of Entry<item, FPNode> of the header table 
 private Map<Integer, FPNode> itemNodeMap=null;
 
 //map of Entry<item, Last_FPNode> of the header table 
 //for fast locate the last FPNode of the header table
 private Map<Integer, FPNode> itemLastNodeMap=null;
 
 
 public FPTree(int k){
	 this.root = new FPNode(0, 0);
	 this.minSup=k+1;
//	 headerFreMap=new HashMap<Integer,Integer>();
	 this.itemNodeMap = new HashMap<Integer,FPNode>();
	 this.itemLastNodeMap = new HashMap<Integer,FPNode>();
	  
 }
  
 public FPNode getRoot(){ return root; }
 
// public Map<Integer, Integer> getFreMap() { return headerFreMap;}
 
 public List<Integer> getHeaderList(){return headerList;}
 
 public Map<Integer, FPNode> getItemNodeMap(){return itemNodeMap;}
 
 public Map<Integer, FPNode> getItemLastNodeMap(){return itemLastNodeMap;}

// public List<Integer> getHeaderList(){
//	List<Integer> headerList=new ArrayList<Integer>(headerFreMap.keySet());
//	Collections.sort(headerList,new Comparator<Integer>() {
//		@Override
//		public int compare(Integer o1,Integer o2) {	
//		// TODO Auto-generated method stub
//		// compare the frequency
//			int compare = headerFreMap.get(o2) - headerFreMap.get(o1);
//		// if the same frequency, we check the lexical ordering!
//			if(compare == 0){ 
//			compare = (o1 - o2);
//			return compare;
//			}
//			return compare;
//		}
//	});
//	
//	return headerList; 
//}
 
 //better implement it in FP-growth Algo
// public FPNode construct(Map<Integer, int[]> database){
//	//step 1: scan the DB can obtain frequent items 
//			Scan(database);
//	//step 2:construct fp-tree
//			Set<Integer> keySet=database.keySet();
//			for(int index:keySet){
//				int[] seq=database.get(index);
//				List<Integer> list=selectSort(seq);
//				for(int x:list) System.out.println((char)x);
//				insert(list);
//			}
//			return root; 
// }
 

 
 //insert one transaction in the FP-tree
 public void insert(List<Integer> transaction){
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
	
 // add a prefixpath to a fp-tree.
 void addPrefixPath(List<FPNode> prefixPath, Map<Integer, Integer> mapSupportBeta, int relativeMinsup){
	// the first element of the prefix path contains the path support
			int pathCount = prefixPath.get(0).getCount();  
			
			FPNode currentNode = root;
			// For each item in the transaction  (in backward order)
			// (and we ignore the first element of the prefix path)
			for(int i = prefixPath.size() -1; i >=1; i--){ 
				FPNode pathItem = prefixPath.get(i);
				// if the item is not frequent we skip it
				if(mapSupportBeta.get(pathItem.getItem()) >= relativeMinsup){
					// check if there is a node already in the FP-Tree
					FPNode child = currentNode.hasChild(pathItem.getItem());
					if(child == null){ 
						// there is no node, we create a new one
						FPNode newNode = new FPNode(pathItem.getItem(),pathCount);
						newNode.linkFather(currentNode);
						currentNode.linkChild(newNode);
						
						currentNode = newNode;
						// update the header table.
						// and the node links
						UpdateNodeLinks(pathItem.getItem(), newNode);

					}else{ 
						// there is a node already, we update it
						child.IncCount();
						currentNode = child;
					}
				}
			} 
 }
 
 
 //check is single path or not 
 public boolean singlePath(FPNode root){
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
 
 //create headerList according to mapsupport's (key,value).
 public void createHeaderList(final Map<Integer, Integer> mapSupport){
		// create an array to store the header list with
		// all the items stored in the map received as parameter
		headerList =  new ArrayList<Integer>(itemNodeMap.keySet());
		
		// sort the header table by decreasing order of support
		Collections.sort(headerList, new Comparator<Integer>(){
			public int compare(Integer id1, Integer id2){
				// compare the support
				int compare = mapSupport.get(id2) - mapSupport.get(id1);
				// if the same frequency, we check the lexical ordering!
				// otherwise we use the support
				return (compare == 0) ? (id1 - id2) : compare;
			}
		});
	}
 
 
 //index 0:mark the single property: 1 means single; 
 //index 1: record the support of the single path if there exists one 
 private int[] isSingleAndMiniSup(FPNode root){
	 	int[] result=new int[2];
	 	result[0]=1;
		if(root.getChild().size()==0) {
			result[0]=0;
			return result;
		}
		
		while(root.getChild().size()!=0){
			if(root.getChild().size()!=1) {
				result[0]=0;
				break;
			}
			root=root.getChild().iterator().next();
			result[1]=root.getCount();
		}
		return result;
	}
 
 public void traverse(FPNode root){
		System.out.println("item "+(char)root.getItem()+" count "+root.getCount());
		Set<FPNode> nodeSet=root.getChild(); 
		for(FPNode node:nodeSet) traverse(node);	
	}
 
 
  // overwrite for string output of the tree
	public String toString() {
		String temp = "F";
		// append header list
		temp += " HeaderList: "+ headerList + "\n";
		// append child nodes
		temp += root.toString("");
		return temp;
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

		FPTree fpTree=new FPTree(1);
		
//		fpTree.traverse(fpTree.getRoot());
//		int a[]= fpTree.getHeaderList();
//		for(int x:a) System.out.println((char)x);
//		fpTree.printMap();
//		System.out.println(fpTree.singlePath(root));	
		
		
		
		
		}


 
}
