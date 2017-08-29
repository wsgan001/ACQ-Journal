package algorithm.kwIndex;

import java.util.*;

public class KWNode {
	public int itemId = -1;
	public KWNode father = null;
//	List<KWNode> childList = null; 
	public Set<KWNode> childList = null;
	Set<Integer> vertex = null; 
	
//	public Map<Integer, KNode> KTree = null;
	public KTree ktree =null;
	KNode KtreeRoot = null;
	
	//8.25 add 
	Set<Integer> compressedId = null;
	
	
	public KWNode(int x){
		this.itemId = x;
		this.father = this;
//		this.childList = new ArrayList<KWNode>();
		this.childList = new HashSet<KWNode>();
		this.vertex = new HashSet<Integer>();
//		this.KTree = new HashMap<Integer,KNode>();
//		this.ktree = 
	}
	
//	public void setKTree(Map<Integer, KNode> map){
//		this.KTree = map;
//	}
	
	public void setktree(KTree kTree){
		this.ktree=kTree;
	}
	
	public void setVertex(Set<Integer> set){
		this.vertex = set; 
	}
	

	
	public void gc(){
		this.vertex = null;
	}
	
	public String toString(String indent){
		StringBuilder output = new StringBuilder();
//		output.append("item NO.: "+itemId+"vertex:"+ vertex.toString()+"\n");  clear the vertexSet 
		output.append("item NO.: "+itemId+"  father id: "+father.itemId);
		if(compressedId!=null) output.append("  compress ids: "+compressedId.toString());
		output.append("\n");
		
		output.append("  k-tree: "+KtreeRoot.toString("          "));
		String newIndent = indent+"  ";
		for(KWNode node:childList) output.append(newIndent+node.toString(newIndent));
		return output.toString();
	}

	
}