package algorithm.kwIndex;

import java.util.*;

public class KWNode {
	public int itemId = -1;
	public KWNode father = null;
	public Set<KWNode> childList = null;
	Set<Integer> tmpVertexSet = null; 
	
	KNode KtreeRoot = null;
	
	private Map<Integer, KNode> vertexMap = null;
	
	
	//8.25 add 
	Set<Integer> compressedId = null;
	
	
	public KWNode(int x){
		this.itemId = x;
		this.father = this;
		this.childList = new HashSet<KWNode>();
		this.tmpVertexSet = new HashSet<Integer>();

	}
	
	public void setvertexMap(Map<Integer, KNode> vertexMap){
		this.vertexMap = vertexMap;
		gc();
	}
	
	public void gc(){
		this.tmpVertexSet = null;
	}
	
	public String toString(String indent){
		StringBuilder output = new StringBuilder();
//		output.append("item NO.: "+itemId+"vertex:"+ vertex.toString()+"\n");  clear the vertexSet 
		output.append("item NO.: "+itemId+"  father id: "+father.itemId);
		if(compressedId!=null) output.append("  compress ids: "+compressedId.toString());
		output.append("\n");
		if(KtreeRoot!=null) output.append(indent+KtreeRoot.toString(indent));
		String newIndent = indent+"  ";
		for(KWNode node:childList) output.append(newIndent+node.toString(newIndent));
		return output.toString();
	}

	
}
