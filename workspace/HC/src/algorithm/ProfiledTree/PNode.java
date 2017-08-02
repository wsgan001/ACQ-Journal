package algorithm.ProfiledTree;

import java.util.*;

/**
@author chenyankai
@Date	Jul 24, 2017
this is the data structure for profile tree node
*/
public class PNode {
//	private String Attribute=null;
	private int ID=-1;
	private PNode father=null;
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
	
	
	public String toString(String indent){
		StringBuilder output = new StringBuilder();
		output.append("ID:  "+this.ID+"\n");
		String newIndent = indent + "   ";
		for (PNode child : childPNode) {
			output.append(newIndent+ child.toString(newIndent));
		}
		return output.toString();
	}

	
	
	
	
}