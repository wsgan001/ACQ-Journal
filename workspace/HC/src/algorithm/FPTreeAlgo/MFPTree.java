package algorithm.FPTreeAlgo;

import java.util.*;



/**
@author chenyankai
@Date	Jul 19, 2017
This is an implementation of a MFITree as used by the FPMax algorithm.
*/

public class MFPTree {
private MFPNode root=null;
//use core number of k-core as the minSup
private int k=-1;
	 
//list of items in the header table with descending order
private Map<Integer,Integer> headerMap=null;
	 
//map of Entry<item, FPNode> of the header table 
private Map<Integer, MFPNode> itemNodeMap=null;
	 
//map of Entry<item, Last_FPNode> of the header table 
//for fast locate the last FPNode of the header table
private Map<Integer, MFPNode> itemLastNodeMap=null;

// last added itemset
MFPNode lastAddedItemsetNode = null;
	
public MFPTree(int k){
	 this.root = new MFPNode(-1,0);
	 this.k=k-1;
//	 headerMap = new HashMap<Integer,Integer>();
	 this.itemNodeMap = new HashMap<Integer,MFPNode>();
	 this.itemLastNodeMap = new HashMap<Integer,MFPNode>(); 
}

public MFPNode getRoot(){ return root;}

////insert one possible MFP in the MFP-tree
//public void insert(List<Integer> itemset,int support){
//	 MFPNode currentNode = root;
//	
//	 for(int i=0;i<itemset.size();i++){
//		 int item=itemset.get(i);
//		 //check if there is a node already in the MFP-tree
//		 MFPNode child = currentNode.hasChild(item);
//		 if(child == null ){
////			 System.out.println((char)item+"  null");
//				MFPNode node =new MFPNode(item, i+1);	
//				node.linkFather(currentNode);
//				currentNode.linkChild(node);
//				UpdateNodeLinks(item, node);
//				
//				currentNode=node;
//		 }
//		 else{
//				currentNode=child;
//		 } 
//	 } 
//}

//insert one possible MFP in the MFP-tree
public void insert(Map<Integer,Set<Integer>> itemUser,int support){
	 MFPNode currentNode = root;
	Iterator<Integer> iterator=itemUser.keySet().iterator();
//	 for(int i=0;i<itemUser.size();i++){
	int level=1;
	while(iterator.hasNext()){
		int item=iterator.next();
		 //check if there is a node already in the MFP-tree
		 MFPNode child = currentNode.hasChild(item);
		 if(child == null ){
//			 System.out.println((char)item+"  null");
				MFPNode node =new MFPNode(item, level++);	
				node.addUserSet(itemUser.get(item));
				node.linkFather(currentNode);
				currentNode.linkChild(node);
				UpdateNodeLinks(item, node);
				
				currentNode=node;
		 }
		 else{
			 	child.addUserSet(itemUser.get(item));
				currentNode=child;
		 } 
	 } 
}
	 
//maintain two Map:itemNodeMap and itemLastNodeMap
private void UpdateNodeLinks(int item, MFPNode newNode){
	 if(itemLastNodeMap.containsKey(item)){
		 MFPNode lastNode=itemLastNodeMap.get(item);
		 lastNode.linkBrother(newNode);
	 }
	 //update the itemLastMap with newNode
	itemLastNodeMap.put(item, newNode);
	
	//put <item,newNode> itmeNodeMap if it does not contains item
	MFPNode headNode= itemNodeMap.get(item);
	if(headNode == null){ itemNodeMap.put(item, newNode); }
}

//subset test to check if an itemset is a subset of an already found MFP
//param: headwithP thew itemset to be tested 
public boolean SubsetChecking(List<Integer> headWithP) {

	// Find the node list for the last item of the itemset
	int lastItem = headWithP.get(headWithP.size()-1);
	
	// OPTIMIZATION:
	// We first check against the last added itemset
	if(lastAddedItemsetNode != null) {
		boolean isSubset = isASubsetOfPath(headWithP, lastAddedItemsetNode);
		// if the itemset is a subset of the last added itemset, we do not need to check further
		if(isSubset) {
			return false;
		}
	}
	
	// OTHERWISE, WE NEED TO COMPARE "headwithP" with all the patterns in the MFP-tree.
	MFPNode node = itemNodeMap.get(lastItem);
	// if that last item is not yet in the MFP-tree, it means that "itemset" is not a subset 
	// of some itemset already in the tree
	if(node == null) { return true;}
	
	// else we will loop over each node by following node links
	do {
		// for a node, we will check if "headwithP" is a subset of the path ending at node
		boolean isSubset = isASubsetOfPath(headWithP, node);
		// if it is a subset, then "headWithP" is in the MFP-tree, we return false
		if(isSubset) {   
			return false;
		}
		// go to the next itemset to check
		node = node.getBro();
	}while(node != null);

	// the itemset is not in the MFP-tree.
	return true;
}

// to check headWithP is a subset of the path bottom-up from the node
private boolean isASubsetOfPath(List<Integer> headWithP, MFPNode node) {
	// optimization proposed in the fpmax* paper: if there is less than itemset node in that branch,
	// we don't need to check it
	if(node.getLevel() >= headWithP.size()) {
		// check if "itemset" is contained in the prefix path ending at "node"
		// We will start comparing from the parent of "node" in the prefix path since
		// the last item of itemset is "node".
		MFPNode nodeToCheck = node;
		int positionInHead = headWithP.size()-1;
		int itemToLookFor = headWithP.get(positionInHead);
		
		// for each item in itemset
		do {
			if(nodeToCheck.getItem() == itemToLookFor) {
				positionInHead--;
				// we found the itemset completely, so the subset check test is failed
				if(positionInHead < 0) { return true;}
				itemToLookFor = headWithP.get(positionInHead);
			}
			nodeToCheck = nodeToCheck.getFather();
		}while(nodeToCheck != null);
	}
	return false;
}

//check is single path or not
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

public void traverse(MFPNode root){
	System.out.print("item ");
	System.out.print((char)root.getItem()+"  ");
	
	Set<MFPNode> nodeSet=root.getChild(); 
	for(MFPNode node:nodeSet) {
		System.out.println("%%%%%%%%%%");
		traverse(node);
	}
}

@Override
/**
 * Method for getting a string representation of the CP-tree 
 * (to be used for debugging purposes).
 * @return a string
 */
public String toString() {
	return "M"+root.toString("");
}

public static void main(String[] args){

	
	
}
}