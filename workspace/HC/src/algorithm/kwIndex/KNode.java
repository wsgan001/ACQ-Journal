package algorithm.kwIndex;

import java.util.*;

public class KNode {
	public int k=-1;
	private Set<Integer> vertexSet = null;
 	private List<KNode> childList = null; 
	
	
	public KNode(int k){
		this.k = k;
		this.vertexSet = new HashSet<Integer>();
		this.childList = new LinkedList<KNode>();
	}
	
	public void setChildList(List<KNode> list){
		this.childList = list;
	}
	
	public void setVertices(Set<Integer> set){
		this.vertexSet = set;
	}
	

	public List<KNode> getChildList(){
		return this.childList;
	}
	
	
	public Set<Integer> getVertices(){
		return this.vertexSet;
	}
	
	public void traverse(KNode root,Set<Integer> set){
		if(!root.vertexSet.isEmpty()) {
			set.addAll(root.vertexSet);
		}
		for(KNode node:root.childList) traverse(node,set);
	}
	
	public String toString(String indent){
		StringBuilder output = new StringBuilder();
		output.append(k+"-hatcore: "+vertexSet.toString()+"\n");
		String newIndent = indent+"  ";
		for(KNode child:this.childList) output.append(newIndent+child.toString(newIndent));
		return output.toString();
	}
	
}
