package algorithm.kwIndex;

import java.util.*;

public class KWNode {
	public int itemId = -1;
	public KWNode father = null;
	public Set<KWNode> childList = null;
	public Set<Integer> tmpVertexSet = null; 
	
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
	
	//return a ck-core containing queryId 
	public Set<Integer> getCKCore(int k,int queryId){
		Set<Integer> set=new HashSet<Integer>();
		KNode node = vertexMap.get(queryId);
		if(node==null||node.k < k) return set;
		traverse(node,set);
		return set;
	}
	
	private void traverse(KNode root,Set<Integer> set){
		if(!root.vertexSet.isEmpty()) {
			set.addAll(root.vertexSet);
		}
		for(KNode node:root.childList) traverse(node,set);
	}
	
	public String toString(String indent){
		StringBuilder output = new StringBuilder();
//		output.append("item NO.: "+itemId+"vertex:"+ vertex.toString()+"\n");  clear the vertexSet 
		output.append("item NO.: "+itemId+"  father id: "+father.itemId);
		if(compressedId!=null) output.append("  compress ids: "+compressedId.toString());
		output.append("\n");
		if(KtreeRoot!=null) output.append(indent+KtreeRoot.toString(indent));
		if(tmpVertexSet!=null) output.append("CKC: "+tmpVertexSet.toString()+"\n");
//		if(vertexMap!=null){
//			Iterator<Integer> iter = vertexMap.keySet().iterator();
//			while(iter.hasNext()){
//				int vertex = iter.next();
//				output.append("vertex: "+vertex+" its kcore: "+vertexMap.get(vertex).getVertices().toString()+"\n");
//			}
//		} 
		
		
		String newIndent = indent+"  ";
		for(KWNode node:childList) output.append(newIndent+node.toString(newIndent));
		return output.toString();
	}

	
}
