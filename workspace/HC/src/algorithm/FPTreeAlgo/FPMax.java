package algorithm.FPTreeAlgo;

import java.util.*;

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
	
	public FPMax(int k){
		this.minSup=k+1;
		this.mfpTree=new MFPTree(k);
		this.itemBuffer=new ArrayList<Integer>();
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
				
				//construct Beta's conditional pattern base
				
				
			
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
