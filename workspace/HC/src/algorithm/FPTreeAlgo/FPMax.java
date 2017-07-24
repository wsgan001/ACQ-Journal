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
// private List<Integer> itemBuffer=null;
 private List<Integer> itemBuffer=null;
// private Map<Integer,Integer> headerFreMap=null;

 //record the global frequent items and corresponding support
 private Map<Integer,Integer> globalFreMap=null;
 //inverted list <item, users>
 private Map<Integer,Set<Integer>> itemUserMap=null;
 
 private Map<Integer, Set<Integer>> itemUserSetBuffer=null;
 private Set<Integer> usersBuffer=null;
 
// private Map<Integer,Integer> headerFreMap=null;
 
 
 private Boolean DEBUG=true;


 //define the Comparator the sort all sequence following the descending order of support
 Comparator<Integer> descendingOrder= new Comparator<Integer>() {
	@Override
	public int compare(Integer o1,Integer o2) {	
		// TODO Auto-generated method stub
		// compare the frequency
//		System.out.println(o1+"  "+o2);
//		System.out.println(globalFreMap.containsKey(o1)+"  "+globalFreMap.containsKey(o2));
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
//		this.itemBuffer=new ArrayList<Integer>();
		this.globalFreMap=new HashMap<Integer,Integer>();
		this.itemUserMap=new HashMap<Integer,Set<Integer>>();
		
		this.itemUserSetBuffer=new TreeMap<Integer,Set<Integer>>();
		this.usersBuffer=new HashSet<Integer>();
		this.itemBuffer=new ArrayList<Integer>();
//		this.headerFreMap=fpTree.getFreMap();
//		this.globalSupMap=new HashMap<Integer,Integer>();
		
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
			fpTree.insert(fixedSeq,index);		
		}

		System.out.println(fpTree);
//		fpTree.createHeaderList(globalFreMap);
//		
////		List<Integer> list=new ArrayList<Integer>();
//		Map<Integer, Set<Integer>> map=new HashMap<Integer,Set<Integer>>();
//		if(fpTree.getHeaderList().size()>0){
//			fpMax(fpTree, map, database.size(),globalFreMap,itemUserMap);
//		}
//		System.out.println(fpTree);
		fpTree.createHeaderList(globalFreMap);
//		for(int x:itemBuffer) System.out.print(x+ "   ");
		List<Integer> list=new ArrayList<Integer>();
		if(fpTree.getHeaderList().size()>0){
			fpMax(fpTree, list, database.size(), globalFreMap);
		}
	}
	
	
	//prefix:head+ i 

	public void fpMax(FPTree fpTree,List<Integer> prefix,int prefixSup,Map<Integer, Integer> mapSupport){
		System.out.println("*****************************************");
		System.out.println("prefix size: "+prefix.size());
		System.out.print("###### Prefix:   ");
		for(int k=0; k< prefix.size(); k++){
			System.out.print( prefix.get(k)+"  ");
		}
		System.out.println();
		System.out.println(fpTree);
		
		//get the result of single path property 
		//and get the support of the single path if there exists one
		FPNode root=fpTree.getRoot();
		int[] result=isSingleAndMiniSup(root,prefix.size());
		boolean singlePath=result[0]==1? true:false;
		int singlePathSup=result[1];
		int position=result[2];
//		for(int x:prefix) System.out.print(x+"  ");
//		System.out.println(result[0]+"      result   "+result[1]+"  next");

		//case 1:the FPtree contains a single path
		if(singlePath && singlePathSup>=minSup){
//			List<Integer> list=new LinkedList<Integer>(itemBuffer);
			Map<Integer, Set<Integer>> path=new TreeMap<Integer,Set<Integer>>(descendingOrder);
//			list.addAll(prefix);
			path.putAll(prefix);
			mfpTree.insert(path,singlePathSup);
			
//			Collections.sort(list, descendingOrder);
			
			//-----------------------DEBUG-------------------------------
			if(DEBUG){
				System.out.println("case 1");
				System.out.print("itemBuffer: ");
				for(int x:itemBuffer){
					if(x!=0) System.out.print(x+"  ");
				}
				System.out.println();
				System.out.print(" ##### SAVING : ");
				for(int i:path.keySet()) { System.out.print(i + "u serID: "+path.get(i).toString()+"    ");}
				System.out.println("\n");
			}
			//---------------------END DEBUG-----------------------------

			System.out.println("case 1");
			for(int x:itemBuffer){
				if(x!=0) System.out.print(x+"  ");
			}
			List<Integer> list=new LinkedList<Integer>(itemBuffer);
			list.addAll(prefix);
			Collections.sort(list, descendingOrder);

			
			System.out.print(" ##### SAVING : ");
			for(int i:list) { System.out.print(i + "  ");}
			System.out.println("\n");
			mfpTree.insert(list,position,singlePathSup);
		}
		//case 2: the fptree contains more than a single path 
		else{
			System.out.println("case 2");
			
			List<Integer> headerList=fpTree.getHeaderList();
		
//			Map<Integer, Integer> FreMap=fpTree.getFreMap();
			//for each frequent item in the header list in reverse order
			for(int i=headerList.size()-1;i>=0;i--){
				
				for(int x:itemBuffer){
					if(x!=0) System.out.print(x+"  ");
				}
				
				
				int item=headerList.get(i);
				int support=mapSupport.get(item);
				Set<Integer> userSet=itemUserMap.get(item);
				//Beta = ai U a (i U Head as described in paper); 
				//caculate the support of Beta
				prefix.put(item,userSet);
//				prefix.add(item);
				
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
				System.out.println("prefix on: "+item);
//				if(prefix.size()>0) prefix.remove(0);
//				
//				if(prefix.size()>prefix.size()) prefix.set(prefix.size()-1, item);
//				else {
//					prefix.add(item);
//				}
				prefix.add(item);
				System.out.println(" prefix now:"+prefix);


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
				
				for(int tmpItem:prefix.keySet()){
					headWithP.add(tmpItem);
//					headWithP.add(prefix.get(z));
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
				System.out.println(" CHECK2 : " + headWithP);
				
				// CHECK IF HEAD U P IS A SUBSET OF A MFP ACCORDING TO THE MF-TREE
				if(mfpTree.SubsetChecking(headWithP)) {
					System.out.println("    passed!");
					// (B) Construct beta's conditional FP-Tree using its prefix path
					// Create the tree.
					FPTree treeBeta = new FPTree(0);
					// Add each prefixpath in the FP-tree.
					for(List<FPNode> prefixPath : prefixPaths){
						treeBeta.addPrefixPath(prefixPath, BetaSupportMap, minSup); 
					}  
					// Mine recursively the Beta tree if it is not a single root
//					if(treeBeta.singlePath(treeBeta.getRoot()) ){
					if(treeBeta.getRoot().getChild().size()>0){
						// Create the header list.
						treeBeta.createHeaderList(globalFreMap); 	
						// recursive call
						fpMax(treeBeta, prefix,betaSupport, BetaSupportMap);
					}
					
					// ======= After that, we still need to check if beta is a maximal itemset ====
//					List<Integer> temp = new ArrayList<Integer>(mapSupportBeta.size() + prefixLength+1);
//					for(int z=0; z < prefixLength+1; z++) {
//						temp.add(prefix[z]);
//					}
//					Collections.sort(temp, comparatorOriginalOrder);
//					// if beta pass the test, we save it
//					if(mfiTree.passSubsetChecking(temp)) {
//						saveItemset(prefix, prefixLength+1, betaSupport);
//					}
					//===========================================================
				}else{System.out.println("    failed!");}	
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
				
				//record the item->users
				if(itemUserMap.containsKey(x)) itemUserMap.get(x).add(index);
				else {
					Set<Integer> tmp=new HashSet<Integer>();
					tmp.add(index);
					itemUserMap.put(x,tmp);
				}
			}
		}
		//step2: clear out those whose sup <minSup
		Iterator<Integer> it1=globalFreMap.keySet().iterator();
		while(it1.hasNext()){
			int index=it1.next();
			if(globalFreMap.get(index)<minSup) {
				it1.remove();// note here should use iterator to delete items
				itemUserMap.remove(index);
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
			itemUserSetBuffer.clear();
			usersBuffer.clear();
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
					itemUserSetBuffer.clear();
					usersBuffer.clear();
					break;
				}
				if(itemBuffer.size()>position) itemBuffer.set(position, root.getItem());
				else itemBuffer.add(root.getItem());
				position++;
				result[1]=root.getCount();
				if(root.getChild().size()==0){break;}
				root=root.getChild().iterator().next();
				result[2]=position;
			}
			
			return result;
}
	
	
	public void traverseMFP(MFPNode root){
		System.out.println("item "+(char)root.getItem()+" level "+root.getLevel());
		Set<MFPNode> nodeSet=root.getChild(); 
		for(MFPNode node:nodeSet) traverseMFP(node);
		
	}
	
	 public void traverseFP(FPNode root){
			System.out.println("item "+(char)root.getItem()+" count "+root.getCount());
			Set<FPNode> nodeSet=root.getChild(); 
			for(FPNode node:nodeSet) traverseFP(node);	
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
		Map<Integer, int[]> map2=new HashMap<Integer,int[]>();
		int[] b1={'f','a','c','d','g','i','m','p'}; map2.put(1, b1);
		int[] b2={'a','b','c','f','l','m','o'}; map2.put(2, b2);
		int[] b3={'b','f','h','j','o'}; map2.put(3, b3);
		int[] b4={'b','c','k','s','p'}; map2.put(4, b4);
		int[] b5={'a','f','c','e','l','p','m','n'}; map2.put(5, b5);
		
		
		int k=2;
		FPMax fpMax=new FPMax(k);
		fpMax.runAlgo(map2);

//		System.out.println(fpMax.mfpTree);
//		fpMax.testSortMap();

		
//		fpMax.traverseFP(fpMax.fpRoot);
		
	}
	
}
