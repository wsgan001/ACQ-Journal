package algorithm.FPTreeAlgo;

import java.util.*;
import java.util.Map.Entry;

import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.FPNode;
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.FPTree;

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
 private List<Integer> itemBuffer=null;
 private Map<Integer,Integer> headerFreMap=null;
 
 
 //define the Comparator the sort all sequence following the descending order of support
 Comparator<Integer> descendingOrder= new Comparator<Integer>() {
	
	@Override
	public int compare(Integer o1,Integer o2) {	
		// TODO Auto-generated method stub
		// compare the frequency
		int compare = headerFreMap.get(o2) - headerFreMap.get(o1);
		// if the same frequency, we check the lexical ordering!
		if(compare == 0){ 
			compare = (o1 - o2);
			return compare;
		}
			return compare;
	}
	
};
	
	public FPMax(int k,FPTree fpTree){
		this.minSup=k+1;
		this.mfpTree=new MFPTree(k);
		this.itemBuffer=new ArrayList<Integer>();
		this.headerFreMap=fpTree.getFreMap();
		
	}
	
	
	
	public void fpMax(FPTree fpTree,ArrayList<Integer> prefix,int prefixSup){
		//get the result of single path property 
		//and get the support of the single path if there exists one
		FPNode root=fpTree.getRoot();
		int[] result=isSingleAndMiniSup(root);
		boolean singlePath=result[0]==1? true:false;
		int singlePathSup=result[1];
		
		
		//case 1:the FPtree contains a single path
		if(singlePath && singlePathSup>=minSup){
			mfpTree.insert(itemBuffer, singlePathSup);
		}
		//case 2: the fptree contains more than a single path 
		else{
			List<Integer> headerList=fpTree.getHeaderList();
			Map<Integer, Integer> FreMap=fpTree.getFreMap();
			//for each frequent item in the header list in reverse order
			for(int i=headerList.size()-1;i>=0;i--){
				int item=headerList.get(i);
				int support=FreMap.get(item);
				
				//create Beta by ai U a; caculate the support of Beta
				prefix.add(item);
				int BetaSupport=support>prefixSup?prefixSup:support;
				
				//Step 1: construct Beta's conditional pattern base
				List<List<FPNode>> prefixPaths= new ArrayList<List<FPNode>>();
				FPNode pathStart=fpTree.getItemNodeMap().get(item);
				
				// Map to count the support of items in the conditional prefix tree
				// Key: item   Value: support
				Map<Integer, Integer> BetaSupportMap= new HashMap<Integer, Integer>();
				
				while(pathStart != null){
					// if the path is not just the root node
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
				// concatenate Beta with all the frequent itemsets in the pattern base
				// to get Beta(head) U tail
				List<Integer> headWithP = new ArrayList<Integer>(BetaSupportMap.size() + prefix.size());
				// concatenate the prefix
				for(int z=0; z < prefix.size(); z++) {
					headWithP.add(prefix.get(z));
				}
				// concatenate the other FREQUENT items in the pattern base
				// for each item
				for(Entry<Integer,Integer> entry: BetaSupportMap.entrySet()) {
					// if the item is frequent
					if(entry.getValue() >= minSup) {
						headWithP.add(entry.getKey());
					}
				}
				
				// Sort Beta(head) U tail according to the original header list total order on items
				// sort item in the transaction by descending order of support
				Collections.sort(headWithP, descendingOrder);
				
				// CHECK IF HEAD U P IS A SUBSET OF A MFI ACCORDING TO THE MFI-TREE
				if(mfiTree.passSubsetChecking(headWithP)) {
					
					// (B) Construct beta's conditional FP-Tree using its prefix path
					// Create the tree.
					FPTree treeBeta = new FPTree();
					// Add each prefixpath in the FP-tree.
					for(List<FPNode> prefixPath : prefixPaths){
						treeBeta.addPrefixPath(prefixPath, mapSupportBeta, minSupportRelative); 
					}  
					// Mine recursively the Beta tree if the root has child(s)
					if(treeBeta.root.childs.size() > 0){

						// Create the header list.
						treeBeta.createHeaderList(originalMapSupport); 
						
						// recursive call
						fpMax(treeBeta, prefix, prefixLength+1, betaSupport, mapSupportBeta);
					}
					
					// ======= After that, we still need to check if beta is a maximal itemset ====
					List<Integer> temp = new ArrayList<Integer>(mapSupportBeta.size() + prefixLength+1);
					for(int z=0; z < prefixLength+1; z++) {
						temp.add(prefix[z]);
					}
					Collections.sort(temp, comparatorOriginalOrder);
					// if beta pass the test, we save it
					if(mfiTree.passSubsetChecking(temp)) {
						saveItemset(prefix, prefixLength+1, betaSupport);
					}
					//===========================================================
				}
				
			
			}	
		}	
	}
	
	
//index 0:mark the single property: 1 means single; 
//index 1: record the support of the single path if there exists one
//in the meanwhile, use itemBuffer to store the items in the single path
  private int[] isSingleAndMiniSup(FPNode root){
		 	int[] result=new int[2];
		 	result[0]=1;
			if(root.getChild().size()==0) {
				result[0]=0;
				//clear the buffer
				itemBuffer.clear();
				return result;
			}
			
			while(root.getChild().size()!=0){
				if(root.getChild().size()!=1) {
					result[0]=0;
					//clear the buffer
					itemBuffer.clear();
					break;
				}
				itemBuffer.add(root.getItem());
				root=root.getChild().iterator().next();
				result[1]=root.getCount();
			}
			return result;
}
	
	
	public static void main(String[] atgs){
		
	}
	
}
