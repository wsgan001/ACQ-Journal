package algorithm.FPTreeAlgo;


import java.util.*;

/**
@author chenyankai
@Date	Jul 19, 2017

*/

public class MFPNode {
	private int item=-1;
//	private int count=-1;
	private int level=-1;//the level that the mfp node appears 
	private MFPNode father=null;
	private Set<MFPNode> childSet=null;
	private MFPNode brother=null;
	//store the users' ID 
	private Set<Integer> userSet=null;
	
	
	public MFPNode(int item,int level){
		this.item=item;
		this.level=level;
		this.childSet=new HashSet<MFPNode>();
		this.userSet= new HashSet<Integer>();
	}
	
	public void linkFather(MFPNode node){
		this.father=node;
	}
	
	public void linkChild(MFPNode node){
		this.childSet.add(node);
	}

	public void linkBrother(MFPNode node){
		this.brother=node;
	}
	
	public void addUser(int i){this.userSet.add(i);}
	
	public void addUserSet(Set<Integer> set){
		if(this.userSet.size()>set.size()){
			this.userSet.addAll(set);
		}else{
			set.addAll(this.userSet);
			this.userSet=set;
		}
	}
	
	
	public Set<Integer> getUsers(){return this.userSet;}
	
	// get the specific child, if there is no such child, return null.
	public MFPNode hasChild(int item){
			for(MFPNode node:childSet){
				if(node.getItem()==item) return node;
			}
		return null;
	}
	
	public MFPNode getFather(){
		return father;
	}
	
	public Set<MFPNode> getChild(){
		return childSet;
	}
	
	public boolean hasBro(){
		return brother!=null;
	}
	
	public MFPNode getBro(){
		return brother;
	}
	
	public int getItem(){
		return item;
	}
	
		
	public int getLevel(){
		return level;
	}

	public void IncLevel(){
		this.level++;
	}
	
}
