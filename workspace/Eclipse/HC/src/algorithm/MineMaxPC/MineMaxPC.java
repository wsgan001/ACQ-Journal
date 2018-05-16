package algorithm.MineMaxPC;
import java.util.*;
import java.util.Map.Entry;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import algorithm.ProfiledTree.PTree;

/**
@author chenyankai
@Date	Jul 24, 2017
For mining profiled community FP-tree based

adapted algorithm for adding users is not finishded. 
Still exist bugs. For example: testing map2 in main function: g in MFPtree should be shared by 4 5 9 10 
and d should be shared by 4 8 

reason: same item with different users should be treated as different prefix.(see line 180: prefix.add(pathStart);) 
pathStart differs with different userSet but sharing same item. remained fixing....


Note that its query method is a little bit similar to index-based query algorithm. Maybe it's can be handle by that time.

*/

public class MineMaxPC {
	//use core number of k-core as the minSup: miniSup=k+1
	 private int minSup=-1;
	 private MFPTree mfpTree=null;
	 private FPNode fpRoot=null;
	 //item buffer 
	 private List<FPNode> itemBuffer=null;
	 //record the global frequent items and corresponding support
	 private Map<Integer,Set<Integer>> globalFreMap=null;
	 private PTree pTree=null;
	 
	//for debug use
	 private Boolean DEBUG=true;
	 


	 //define the Comparator the sort all sequence following the descending order of support
	 Comparator<Integer> descendingOrder= new Comparator<Integer>() {
		@Override
		public int compare(Integer o1,Integer o2) {	
			// TODO Auto-generated method stub
			int compare = globalFreMap.get(o2).size() - globalFreMap.get(o1).size();
			// if the same frequency, we check the lexical ordering!
			if(compare == 0){ 
				compare = (o1 - o2);
				return compare;
			}
				return compare;
		}
	};

	
	//define the Comparator the sort all FPNode following the descending order of support
		 Comparator<FPNode> descendingOrderFPNode= new Comparator<FPNode>() {
			@Override
			public int compare(FPNode o1,FPNode o2) {	
				// TODO Auto-generated method stub
				int compare = globalFreMap.get(o2.getItem()) .size()- globalFreMap.get(o1.getItem()).size();
				// if the same frequency, we check the lexical ordering!
				if(compare == 0){ 
					compare = (o1.getItem() - o2.getItem());
					return compare;
				}
					return compare;
			}
		};



		public MineMaxPC(int k){
			this.minSup=k+1;
			this.mfpTree=new MFPTree(k);
			this.globalFreMap=new HashMap<Integer,Set<Integer>>();
			this.itemBuffer=new ArrayList<FPNode>();
			this.pTree=new PTree();
		}
		
		
		//need to be done
		private void mineCommunity(){
			System.out.println();
			
			
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
			//------------------------DEBUG------------------------------
			if(DEBUG) System.out.println(fpTree);
			//----------------------END DEBUG----------------------------
			fpTree.createHeaderList(globalFreMap);
			List<FPNode> list=new ArrayList<FPNode>();
			if(fpTree.getHeaderList().size()>0){
				fpMax(fpTree,list, database.size(), globalFreMap);
			}
			System.out.println(mfpTree);
		}
		
		
		
		//prefix:head+ i 
		public void fpMax(FPTree fpTree,List<FPNode> prefix,int prefixSup,Map<Integer, Set<Integer>> mapSupport){
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
				List<FPNode> list=new LinkedList<FPNode>(itemBuffer);
				list.addAll(prefix);
				Collections.sort(list,descendingOrderFPNode);
				System.out.println(list.toString());
				mfpTree.insertFPNodes(list,singlePathSup);
				mineCommunity();
				//-----------------------DEBUG-------------------------------
				if(DEBUG){
					System.out.println("case 1");
					System.out.print("itemBuffer: ");
					for(FPNode x:itemBuffer){
						if(x.getItem()!=0) System.out.print(x.getItem()+"  ");
					}
					System.out.println();
					System.out.print(" ##### SAVING : ");
					for(FPNode i:list) { System.out.print(i.getItem() + "  ");}
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
					int support=mapSupport.get(item).size();
					
					
					int betaSupport=(support>prefixSup)?prefixSup:support;
					
					//Step 1:Beta = ai U a (i U Head as described in paper); 
					//caculate the support of Beta
					//construct Beta's conditional pattern base
					List<List<FPNode>> prefixPaths= new ArrayList<List<FPNode>>();
					FPNode pathStart=fpTree.getItemNodeMap().get(item);
					prefix.add(pathStart);

					// Map to count the support of items in the conditional prefix tree
					// Key: item   Value: users
					Map<Integer, Set<Integer>> betaSupportMap= new HashMap<Integer,Set<Integer>>();
					//map to record the item and its users 
					//key:item value:users 
//					Map<Integer, Set<Integer>> betaUsersMap=new HashMap<Integer, Set<Integer>>();
					
					while(pathStart != null){
						//-----------------------DEBUG-------------------------------
						if(DEBUG){
							System.out.print("itemBuffer: ");
							for(FPNode x:itemBuffer){
								if(x.getItem()!=0) System.out.print(x.getItem()+"  ");
							}
							System.out.println();
							System.out.println("prefix on: "+pathStart.getItem());
							System.out.println(" prefix now:"+prefix);
						}
						//---------------------END DEBUG-----------------------------
						// if the path is not just the root node and itself 
						//still unconfirmed
						if(pathStart.getItem() != 0){
							// create the prefixpath
							List<FPNode> prefixPath = new ArrayList<FPNode>();
							// add this node.
							prefixPath.add(pathStart);   // NOTE: we add it just to keep its support,
							// actually it should not be part of the prefixPath
//							int pathCount = pathStart.getCount();
							System.out.println((char)pathStart.getItem()+" users "+pathStart.getUser().toString());
							//Recursively add all the parents of this node.
							FPNode father = pathStart.getFather();
							while(father.getItem() != 0){
								prefixPath.add(father);
								
								// FOR EACH PATTERN WE ALSO UPDATE THE ITEM SUPPORT AT THE SAME TIME
								// if the first time we see that node id
								if(!betaSupportMap.containsKey(father.getItem())){
									// just clone the users set
									Set<Integer> userSet=new HashSet<Integer>();
									for(int x:pathStart.getUser()) userSet.add(x);
									betaSupportMap.put(father.getItem(),userSet);
								}else{
									// otherwise, make the sum with the value already stored
									betaSupportMap.get(father.getItem()).addAll(pathStart.getUser());
								}
								father = father.getFather();
							}
						// add the path to the list of prefixpaths
						prefixPaths.add(prefixPath);
						for(FPNode x:prefixPath){
							System.out.println("prefixPath: "+x.getItem()+" users "+x.getUser().toString());
						}
					}	
						// check the next prefixpath
						pathStart = pathStart.getBro();
					}
					
					// ===== FPMAX ======
					// 1: Get tail={frequent items in base}
					// 2: concatenate Beta(head) with all the frequent itemsets in the pattern base
					// 3: headWithP= Head U tail
					List<FPNode> headWithP = new ArrayList<FPNode>(betaSupportMap.size() + prefix.size());
					// concatenate the prefix

					for(FPNode x:prefix){
							System.out.println("prefix: "+x.getItem()+" users "+x.getUser().toString());
					}
					
					for(int z=0; z < prefix.size(); z++) {
						
						headWithP.add(prefix.get(z));	
					}
					//-----------------------DEBUG-------------------------------
					if(DEBUG) System.out.println(" CHECK1 : " + headWithP);
					//---------------------END DEBUG-----------------------------

					// concatenate the other FREQUENT items in the pattern base
					// for each item
					for(Entry<Integer, Set<Integer>> entry: betaSupportMap.entrySet()){
	
						// if the item is frequent
						if(entry.getValue().size() >= minSup){
							FPNode node=new FPNode(entry.getKey(), entry.getValue().size());
							node.addUserSet(entry.getValue());
							headWithP.add(node);
						}
					}
				
					for(FPNode x:headWithP) System.out.println("item: "+x.getItem()+" check here: "+x.getUser().toString());

					// Sort Beta(head) U tail according to the original header list total order on items
					// sort item in the transaction by descending order of support
					Collections.sort(headWithP, descendingOrderFPNode);
					
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
							treeBeta.addPrefixPath(prefixPath, betaSupportMap, minSup); 
						}  

						// Mine recursively the Beta tree if the root has child(s)
						if(treeBeta.getRoot().getChild().size()>0){
							// Create the header list.
							treeBeta.createHeaderList(globalFreMap); 
							// recursive call
							fpMax(treeBeta, prefix,betaSupport, betaSupportMap);

						}
						
					}
					//------------------------DEBUG------------------------------
					else if(DEBUG){ System.out.println("    failed!");}	
					//----------------------END DEBUG----------------------------
					prefix.remove(prefix.size()-1);
				}	
			}	
		}
		
		
//		 scan the database and create headerList
		private void Scan(Map<Integer, int[]> database){
			//step1: counting tree nodes and its support
			Iterator<Integer> it=database.keySet().iterator();
			while(it.hasNext()){
				int index=it.next();
				for(int x:database.get(index)){
					//record the item->support
					if(globalFreMap.containsKey(x)) {
						globalFreMap.get(x).add(index);
						}
					else {
						Set<Integer> set= new HashSet<Integer>();
						set.add(index);
						globalFreMap.put(x, set);
					}
					
					
				}
			}
			//step2: clear out those whose sup <minSup
			Iterator<Integer> it1=globalFreMap.keySet().iterator();
			while(it1.hasNext()){
				int index=it1.next();
				if(globalFreMap.get(index).size()<minSup) {
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
//					else itemBuffer.add(root.getItem());
					else itemBuffer.add(root);
					result[1]=root.getCount();
					if(root.getChild().size()==0){break;}
					root=root.getChild().iterator().next();
				
				}
				return result;
	}
		
		
	
	
	public static void main(String[] args){

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
		MineMaxPC mineMaxPC=new MineMaxPC(k);
		mineMaxPC.runAlgo(map1);
	}
		
}

