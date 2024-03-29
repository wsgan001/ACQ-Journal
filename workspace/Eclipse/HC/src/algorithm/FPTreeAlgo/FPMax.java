package algorithm.FPTreeAlgo;

import java.util.*;
import java.util.Map.Entry;


/**
@author chenyankai
@Date	Jul 19, 2017

This is an implementation of the FPMax algorithm (Grahne et al., 2004).
High Performance Mining of Maximal Frequent Itemsets (with J. Zhu).
*/
public class FPMax {
//use core number of k-core as the minSup: miniSup=k+1
 private int minSup=-1;
 private MFPTree mfpTree=null;
 private FPNode fpRoot=null;
 //item buffer 
 private List<Integer> itemBuffer=null;
 //record the global frequent items and corresponding support
 private Map<Integer,Integer> globalFreMap=null;
//for debug use
 private Boolean DEBUG=true;


 //define the Comparator the sort all sequence following the descending order of support
 Comparator<Integer> descendingOrder= new Comparator<Integer>() {
	@Override
	public int compare(Integer o1,Integer o2) {	
		// TODO Auto-generated method stub
		int compare = globalFreMap.get(o2) - globalFreMap.get(o1);
		// if the same frequency, we check the lexical ordering!
		if(compare == 0){ 
			compare = (o1 - o2);
			return compare;
		}
			return compare;
	}
};

////define the Comparator the sort all sequence in Map<item,UserID> following the descending order of support
//Comparator<Entry<Integer,Set<Integer>>> descendingOrderMap= new Comparator<Entry<Integer,Set<Integer>>>() {
//	@Override
//	public int compare(Entry<Integer, Set<Integer>> o1,Entry<Integer, Set<Integer>> o2) {	
//		// TODO Auto-generated method stub
//		// compare the frequency
//		int compare = globalFreMap.get(o2.getKey()) - globalFreMap.get(o1.getKey());
//		// if the same frequency, we check the lexical ordering!
//		if(compare == 0){ 
//			compare = (o1.getKey() - o2.getKey());
//			return compare;
//		}
//			return compare;
//	}
//};



	public FPMax(int k){
		this.minSup=k+1;
		this.mfpTree=new MFPTree(k);
		this.globalFreMap=new HashMap<Integer,Integer>();
		this.itemBuffer=new ArrayList<Integer>();
	}
	
	
	//need to be done
	private void mineCommunity(){
		System.out.println("Mining the communities!");
	}
	
	
	public void runAlgo(Map<Integer,int[]> database){
		//first scan 
		Scan(database);
		FPTree fpTree=new FPTree(this.minSup-1);
		this.fpRoot=fpTree.getRoot();
		
		//second scan to build the FP-tree
		Iterator<Integer> it=database.keySet().iterator();
		while(it.hasNext()){
			int index=it.next();
			List<Integer> fixedSeq=SelectSort(database.get(index));
			fpTree.insert(fixedSeq);		
		}
		//------------------------DEBUG------------------------------
		if(DEBUG) System.out.println(fpTree);
		//----------------------END DEBUG----------------------------
		fpTree.createHeaderList(globalFreMap);
		List<Integer> list=new ArrayList<Integer>();
		if(fpTree.getHeaderList().size()>0){
			fpMax(fpTree,list, database.size(), globalFreMap);
		}
	}
	
	
	//prefix:head+ i 
	public void fpMax(FPTree fpTree,List<Integer> prefix,int prefixSup,Map<Integer, Integer> mapSupport){
		//------------------------DEBUG------------------------------
		if(DEBUG){
			System.out.println("*****************************************");
			System.out.println("prefix size: "+prefix.size());
			System.out.print("###### Prefix:   ");
			for(int k=0; k< prefix.size(); k++){
				System.out.print( prefix.get(k)+"  ");
			}
			System.out.println();
		}
		//----------------------END DEBUG----------------------------
		
		//get the result of single path property 
		//and get the support of the single path if there exists one
		FPNode root=fpTree.getRoot();
		int[] result=isSingleAndMiniSup(root);
		boolean singlePath=result[0]==1? true:false;
		int singlePathSup=result[1];
		
		//case 1:the FPtree contains a single path
		if(singlePath && singlePathSup>=minSup){
			List<Integer> list=new LinkedList<Integer>(itemBuffer);
			list.addAll(prefix);
			Collections.sort(list,descendingOrder);
			mfpTree.insert(list,singlePathSup);
						
			//-----------------------DEBUG-------------------------------
			if(DEBUG){
				System.out.println("case 1");
				System.out.print("itemBuffer: ");
				for(int x:itemBuffer){
					if(x!=0) System.out.print(x+"  ");
				}
				System.out.println();
				System.out.print(" ##### SAVING : ");
				for(int i:list) { System.out.print(i + "  ");}
				System.out.println("\n");
				System.out.println("case 1");
			}
			//---------------------END DEBUG-----------------------------
		}
		
		//case 2: the fptree contains more than a single path 
		else{
			//------------------------DEBUG------------------------------
			if(DEBUG){System.out.println("case 2");}
			//----------------------END DEBUG----------------------------
			
			List<Integer> headerList=fpTree.getHeaderList();
			
			//for each frequent item in the header list in reverse order
			for(int i=headerList.size()-1;i>=0;i--){
				int item=headerList.get(i);
				int support=mapSupport.get(item);
				//Beta = ai U a (i U Head as described in paper); 
				//caculate the support of Beta
				prefix.add(item);
				
				//-----------------------DEBUG-------------------------------
				if(DEBUG){
					System.out.print("itemBuffer: ");
					for(int x:itemBuffer){
						if(x!=0) System.out.print(x+"  ");
					}
					System.out.println();
					System.out.println("prefix on: "+item);
					System.out.println(" prefix now:"+prefix);
				}
				//---------------------END DEBUG-----------------------------
				

				int betaSupport=(support>prefixSup)?prefixSup:support;
				
				//Step 1: construct Beta's conditional pattern base
				List<List<FPNode>> prefixPaths= new ArrayList<List<FPNode>>();
				FPNode pathStart=fpTree.getItemNodeMap().get(item);
				
				// Map to count the support of items in the conditional prefix tree
				// Key: item   Value: support
				Map<Integer, Integer> BetaSupportMap= new HashMap<Integer, Integer>();
				
				while(pathStart != null){
					// if the path is not just the root node and itself 
					//still unconfirmed
					if(pathStart.getItem() != 0){
						// create the prefixpath
						List<FPNode> prefixPath = new ArrayList<FPNode>();
						// add this node.
						prefixPath.add(pathStart);   // NOTE: we add it just to keep its support,
						// actually it should not be part of the prefixPath
						int pathCount = pathStart.getCount();
						
						//Recursively add all the parents of this node.
						FPNode father = pathStart.getFather();
						while(father.getItem() != 0){
							prefixPath.add(father);
							
							// FOR EACH PATTERN WE ALSO UPDATE THE ITEM SUPPORT AT THE SAME TIME
							// if the first time we see that node id
							if(!BetaSupportMap.containsKey(father.getItem())){
								// just add the path count
								BetaSupportMap.put(father.getItem(), pathCount);
							}else{
								// otherwise, make the sum with the value already stored
								BetaSupportMap.put(father.getItem(), BetaSupportMap.get(father.getItem()) + pathCount);
							}
							father = father.getFather();
						}
					// add the path to the list of prefixpaths
					prefixPaths.add(prefixPath);
				}	
					// check the next prefixpath
					pathStart = pathStart.getBro();
				}
				
				// ===== FPMAX ======
				// 1: Get tail={frequent items in base}
				// 2: concatenate Beta(head) with all the frequent itemsets in the pattern base
				// 3: headWithP= Head U tail
				List<Integer> headWithP = new ArrayList<Integer>(BetaSupportMap.size() + prefix.size());
				Set<Integer> tmpCommunity=new HashSet<Integer>();
				// concatenate the prefix

				for(int z=0; z < prefix.size(); z++) {
					headWithP.add(prefix.get(z));	
				}
				//-----------------------DEBUG-------------------------------
				if(DEBUG) System.out.println(" CHECK1 : " + headWithP);
				//---------------------END DEBUG-----------------------------

				// concatenate the other FREQUENT items in the pattern base
				// for each item
				for(Entry<Integer,Integer> entry: BetaSupportMap.entrySet()) {
					// if the item is frequent
					if(entry.getValue() >= minSup){
						headWithP.add(entry.getKey());
					}
				}

				// Sort Beta(head) U tail according to the original header list total order on items
				// sort item in the transaction by descending order of support
				Collections.sort(headWithP, descendingOrder);
				
				//------------------------DEBUG------------------------------
				if(DEBUG) System.out.println(" CHECK2 : " + headWithP);
				//----------------------END DEBUG----------------------------
				
				// CHECK IF HEAD U P IS A SUBSET OF A MFP ACCORDING TO THE MF-TREE
				if(mfpTree.SubsetChecking(headWithP)) {
					//------------------------DEBUG------------------------------
					 if(DEBUG) System.out.println("    passed!");
					//----------------------END DEBUG----------------------------
					// (B) Construct beta's conditional FP-Tree using its prefix path
					// Create the tree.
					FPTree treeBeta = new FPTree(0);
					// Add each prefixpath in the FP-tree.
					for(List<FPNode> prefixPath : prefixPaths){
						treeBeta.addPrefixPath(prefixPath, BetaSupportMap, minSup); 
					}  

					// Mine recursively the Beta tree if the root has child(s)
					if(treeBeta.getRoot().getChild().size()>0){
						// Create the header list.
						treeBeta.createHeaderList(globalFreMap); 
						// recursive call
						fpMax(treeBeta, prefix,betaSupport, BetaSupportMap);

					}
					
				}
				//------------------------DEBUG------------------------------
				else if(DEBUG){ System.out.println("    failed!");}	
				//----------------------END DEBUG----------------------------
				prefix.remove(prefix.size()-1);
			}	
		}	
	}
	
	
//	 scan the database and create headerList
	private void Scan(Map<Integer, int[]> database){
		//step1: counting tree nodes and its support
		Iterator<Integer> it=database.keySet().iterator();
		while(it.hasNext()){
			int index=it.next();
			for(int x:database.get(index)){
				//record the item->support
				if(globalFreMap.containsKey(x)) globalFreMap.put(x, globalFreMap.get(x)+1);
				else globalFreMap.put(x, 1);
			}
		}
		//step2: clear out those whose sup <minSup
		Iterator<Integer> it1=globalFreMap.keySet().iterator();
		while(it1.hasNext()){
			int index=it1.next();
			if(globalFreMap.get(index)<minSup) {
				it1.remove();// note here should use iterator to delete items
			}
		}		
	}
	
	//select frequent item and sort them with descending order 
	private List<Integer> SelectSort(int[] seq){
		List<Integer> fixedSeq=new LinkedList<Integer>();
		//select frequent items in seq
		for(int x:seq){
			if(globalFreMap.containsKey(x)) fixedSeq.add(x);
		}
		//sort with descending order
		Collections.sort(fixedSeq,descendingOrder);
		return fixedSeq;
	}
	
	
//index 0:mark the single property: 1 means single; 
//index 1: record the support of the single path if there exists one
//in the meanwhile, use itemBuffer to store the items in the single path

	private int[] isSingleAndMiniSup(FPNode root){
			itemBuffer.clear();
		 	int[] result=new int[2];
		 	//check root node 
			if(root.getChild().size()==0||root.getChild().size()>1) {
				//clear the buffer
				return result;
			}
			else{
				result[0]=1;
				root=root.getChild().iterator().next();
			}
			
			//recursively check its child 
			while(true){
				if(root.getChild().size()>1) {
					result[0]=0;
					//clear the buffer
					itemBuffer.clear();
					break;
				}
				else itemBuffer.add(root.getItem());
				result[1]=root.getCount();
				if(root.getChild().size()==0){break;}
				root=root.getChild().iterator().next();
			
			}
			return result;
}
	
	 
	 public void testSortMap(){
		 globalFreMap=new HashMap<Integer,Integer>();
		 globalFreMap.put(1, 100);
		 globalFreMap.put(3, 5);
		 globalFreMap.put(-1, 11);
		 globalFreMap.put(2, 1000);
		Map<Integer,Integer> map=new TreeMap<Integer,Integer>(descendingOrder);
		map.put(1, 9);
		map.put(3, 8);
		map.put(-1, 7);
		map.put(2, 6);
		map.remove(2);
		
		Iterator<Integer> it=map.keySet().iterator();
		while(it.hasNext()) {
			int index=it.next();
			System.out.println(index+"   "+  map.get(index).toString());}
	 }
  
	public static void main(String[] atgs){
		
		
		Map<Integer, int[]> map1=new HashMap<Integer,int[]>();
		int[] a1={'a','b','c','e','f','o'}; map1.put(1, a1);
		int[] a2={'a','c','g'}; map1.put(2, a2);
		int[] a3={'e','i'}; map1.put(3, a3);
		int[] a4={'a','c','d','e','g'}; map1.put(4, a4);
		int[] a5={'a','c','e','g','l'}; map1.put(5, a5);
		int[] a6={'e','j'}; map1.put(6, a6);
		int[] a7={'a','b','c','e','f','p'}; map1.put(7, a7);
		int[] a8={'a','c','d'}; map1.put(8, a8);
		int[] a9={'a','c','e','g','m'}; map1.put(9, a9);
		int[] a10={'a','c','e','g','n'}; map1.put(10, a10);
		

		
		Map<Integer, int[]> map2=new HashMap<Integer,int[]>();
		int[] b1={'f','a','c','d','g','i','m','p'}; map2.put(1, b1);
		int[] b2={'a','b','c','f','l','m','o'}; map2.put(2, b2);
		int[] b3={'b','f','h','j','o'}; map2.put(3, b3);
		int[] b4={'b','c','k','s','p'}; map2.put(4, b4);
		int[] b5={'a','f','c','e','l','p','m','n'}; map2.put(5, b5);
		
		
		int k=1;
		FPMax fpMax=new FPMax(k);
		fpMax.runAlgo(map1);
		System.out.println(fpMax.mfpTree);

		
	}
	
}
