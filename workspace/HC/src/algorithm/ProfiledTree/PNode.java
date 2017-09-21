package algorithm.ProfiledTree;

import java.util.*;

/**
@author chenyankai
@Date	Jul 24, 2017
this is the data structure for profile tree node
*/
public class PNode {
//	private String Attribute=null;
	private int ID=-100;
	public PNode father=null;
	private List<PNode> childPNode=null;
	private int childSize=-1;
	
	 //define the Comparator the sort all PNode following the ascending order of support
	 Comparator<PNode> ascendingOrder= new Comparator<PNode>(){
		@Override
		public int compare(PNode o1,PNode o2){	
			// TODO Auto-generated method stub
			return o1.getId()-o2.getId();
		}
	};
	

	public PNode(int id){
		this.ID=id;
		this.childPNode=new ArrayList<PNode>();
		this.father = this; 
	}

	public void setFather(PNode node){
		this.father=node;
	}
	
	public int getId(){
		return this.ID;
	}
	
	public PNode getFather(){
		return this.father;
	}
	
	public void ascendOrder(){
		Collections.reverse(childPNode);
	}
	
	public List<PNode> getChildlist(){
		return this.childPNode;
	}
	

	
	public void addPNode(PNode node){
		this.childPNode.add(node);
//		this.childName.add(node.getId());
		childSize=childPNode.size();
	}
	
	public String toString(){
		StringBuilder output = new StringBuilder();
		if(father!=null){
			output.append(this.ID+","+father.ID +"\n");
			 
		}
		else output.append(this.ID+"\n");
//			output.append("ID:  "+this.ID+"\n");
			
		for (PNode child : childPNode) {
			output.append(child.toString());
		}
		return output.toString();
	}
	
	
	public String toString(String indent){
		StringBuilder output = new StringBuilder();
		if(father!=this){
			output.append("ID:  "+this.ID+" father: "+father.ID +"\n");
			 
		}
		else output.append("ID:  "+this.ID+"\n");
//			output.append("ID:  "+this.ID+"\n");
			
		String newIndent = indent + "   ";
		for (PNode child : childPNode) {
			output.append(newIndent+ child.toString(newIndent));
		}
		return output.toString();
	}

	
	
	
	
}
