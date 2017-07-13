package algorithm.FPTreeAlgo;

import java.util.*;

/**
@author chenyankai
@Date	Jul 11, 2017

*/


public class FPNode {
	private int item=-1;
	private int count=-1;
	private FPNode father=null;
	private Set<FPNode> childSet=null;
	private FPNode brother=null;
	
	
	public FPNode(int item,int count){
		this.item=item;
		this.count=count;
		this.childSet=new HashSet<FPNode>();
	}
	
	public void linkFather(FPNode node){
		this.father=node;
	}
	
	public void linkChild(FPNode node){
		this.childSet.add(node);
	}

	public void linkBrother(FPNode node){
		this.brother=node;
	}
	
	public FPNode hasChild(int item){
			for(FPNode node:childSet){
				if(node.getItem()==item) return node;
			}
		FPNode node=null;
		return node;
	}
	
	public Set<FPNode> getChild(){
		return childSet;
	}
	
	public boolean hasBro(){
		return brother!=null;
	}
	
	public FPNode getBro(){
		return brother;
	}
	
	public int getItem(){
		return item;
	}
	
		
	public int getCount(){
		return count;
	}

	public void IncCount(){
		 count++;
	}
	
}
