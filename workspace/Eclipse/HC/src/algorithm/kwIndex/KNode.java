package algorithm.kwIndex;

import java.util.*;

public class KNode{
	public int k=-1;
	public Set<Integer> vertexSet = null;
 	public List<KNode> childList = null; 
 	public KNode father = null;
	
	
	public KNode(int k){
		this.k = k;
		this.vertexSet = new HashSet<Integer>();
		this.childList = new LinkedList<KNode>();
		this.father = this;
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
	
	
	public String toString(String indent){
		StringBuilder output = new StringBuilder();
		output.append(k+"-hatcore: "+vertexSet.toString()+"\n");
		String newIndent = indent+"  ";
		for(KNode child:this.childList) output.append(newIndent+child.toString(newIndent));
		return output.toString();
	}
	
}
