package algorithm.simpleKWIndex;

import java.util.*;
import java.util.Map.Entry;

public class KNode implements Cloneable {
	public int item = -1; 
	public KNode father = null;
	//key: global core number value: vertex set of the specific core number
	public Map<Integer, Set<Integer>> vertices = null;
	public Set<Integer> folder = null;
	private List<KNode> childList = null;
	
//	private Set<Integer> inducedVertexSet = null;
	
	
	public KNode(int item){
		this.item = item;
		this.vertices = new HashMap<Integer,Set<Integer>>();
		this.childList =new ArrayList<KNode>();
		this.father = this;
	}
	
	public KNode clone(){
		KNode node = null;
		try {
			node = (KNode) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		node.childList = new ArrayList<KNode>();
		node.vertices = new HashMap<Integer,Set<Integer>>();
		return node;
	}
	
	
	
//	public void setInduceVertices(Set<Integer> set){
//		this.inducedVertexSet = set;
//	}
	
	
	public List<KNode> getChildList(){
		return this.childList;
	}
	
	public void setChildren(List<KNode> list){
		this.childList = list;
	}
	
	public void addChild(KNode child){
		this.childList.add(child);
	}
	
//	public void setFather(KNode father){
//		this.father = father;
//	}
	
	public boolean containsChild(int i){
		for(KNode child:childList){
			if(child.item==i) return true;
		}
		return false;
	}
	
	public Set<Integer> getKCore(int k){
		Set<Integer> set = new HashSet<Integer>();
		Iterator<Entry<Integer, Set<Integer>>> iter = vertices.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Integer, Set<Integer>> entry = iter.next();
			if(entry.getKey()>=k){
				set.addAll(entry.getValue());
			}
		}
		return set;
	}
	
	
	public String toString(String indent){
		StringBuilder output = new StringBuilder();
		output.append("item ID: "+item+" fatherId: "+father.item+"\n");
//		if(inducedVertexSet!=null) output.append(indent+"induced veritices: "+inducedVertexSet.toString()+"\n");
		if(folder!=null) output.append(indent+"  compressed item:"+folder.toString()+"\n");
		Iterator<Entry<Integer, Set<Integer>>> iter = vertices.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Integer, Set<Integer>> Entry = iter.next();
			output.append("		"+"core #: "+Entry.getKey()+" vertices: "+Entry.getValue().toString()+"\n");
		}
		String newIndent = indent+"  ";
		for(KNode node:childList) 
			output.append(newIndent+node.toString(newIndent));
	
		return output.toString();
	}
	
}
