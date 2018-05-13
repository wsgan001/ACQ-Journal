package algorithm.FPTreeAlgo;

import java.util.List;

/**
@author chenyankai
@Date	Jul 18, 2017
this class represents an itemset 
implement as an array of integers with a variable to store the support number 

*/
public class ItemSet {
	private int[] itemSet=null;
	private int support=0;
	
	public int[] getItems(){ return itemSet; }
	public int getSupport(){ return support; }

	
	public ItemSet(){ itemSet=new int[]{};}
	
	public ItemSet(int item){ itemSet=new int[]{item}; }
	
	public ItemSet(int[] items){ this.itemSet=items; }
	
	public ItemSet(List<Integer> list, int support){
		this.itemSet = new int[list.size()];
		int i = 0;
		for(int x:list) this.itemSet[i++]=x;
		this.support=support;
	}
	
	public int size(){ return itemSet.length;}
	
	public int get(int index){ return itemSet[index]; }
	
	public void setSupport(int sup){ this.support=sup; }
	
	public void increaseCount(){ this.support++;}

	
	
	
	
	
}
