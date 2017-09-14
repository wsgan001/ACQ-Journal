package algorithm.kwIndex.Cloneable;

import java.util.*;




/**
@author chenyankai
@Date	Aug 31, 2017
	Cloneable KNode class
*/
public class KNode implements Cloneable{
	public int k=-1;
	private Set<Integer> vertexSet = null;
//	private KNode father = null;
	private List<KNode> childList = null; 
	
	public KNode(int k){
		this.k = k;
		this.vertexSet = new HashSet<Integer>();
		this.childList = new LinkedList<KNode>();
	}
	
	
	//fast clone the KNode class
	public KNode clone(){
		KNode o = null;
		try {
			o = (KNode) super.clone();
			
		} catch (CloneNotSupportedException e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		return o;
	}
	
	
	
//	//clone the KNode class manually-------> slow
//	public KNode clone(){	
//		KNode o = new KNode(k);
//		o.vertexSet = vertexSet;
//		Iterator<KNode> it = childList.iterator();
//		while(it.hasNext()){
//			KNode node = it.next();
//			o.childList.add(node.clone());
//		}
//		return o;
//	}
	
	
	public void setChildList(List<KNode> list){
		this.childList = list;
	}
	
	public void setVertices(Set<Integer> set){
		this.vertexSet = set;
	}
	
//	public void setFather(KNode father){
//		this.father = father;
//	}
	
	
	public List<KNode> getChildList(){
		return this.childList;
	}
	
	
	public Set<Integer> getVertices(){
		return this.vertexSet;
	}
	
	public void traverse(Set<Integer> set){
		set.addAll(vertexSet);
		for(KNode node:childList) traverse(set);
	}
	
	public String toString(String indent){
		StringBuilder output = new StringBuilder();
		output.append(k+"-hatcore: "+vertexSet.toString()+"\n");
		String newIndent = indent+"  ";
		for(KNode child:this.childList) output.append(newIndent+child.toString(newIndent));
		return output.toString();
	}
	
}

