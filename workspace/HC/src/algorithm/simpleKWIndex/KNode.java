package algorithm.simpleKWIndex;

import java.util.*;
import java.util.Map.Entry;

import algorithm.DecomposeKCore;

public class KNode{
	public int item = -1; 
	public KNode father = null;
	//key: global core number value: vertex set of the specific core number
//	key:vertex value:core number 
	public Map<Integer,Integer> vertexCore = null;
	public Set<Integer> folder = null;
	public List<KNode> childList = null;
	public Set<Integer> tmpUsers = null;
	
	
	
	public KNode(int item){
		this.item = item;
		this.vertexCore = new HashMap<Integer,Integer>();
		this.childList =new ArrayList<KNode>();
		this.father = this;
		this.tmpUsers = new HashSet<Integer>();
	}
	
	

	
	public void computeCore(int[][] subgraph){
		DecomposeKCore decopose = new DecomposeKCore(subgraph);
		int[] core = decopose.decompose();
		int[] vertexList = subgraph[0];
		
		for(int i=0;i<vertexList.length;i++){
			vertexCore.put(vertexList[i], core[i]);
		}
		gc();
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
	
	public void gc(){
		this.tmpUsers=null;
	}
	
	public boolean containsChild(int i){
		for(KNode child:childList){
			if(child.item==i) return true;
		}
		return false;
	}
	
	
	public  boolean isContains(int id,int k){
		return vertexCore.get(id)>=k?true:false;
	}
	
	
	public Set<Integer> findCKC(int k,int queryId){
		Set<Integer> users = new HashSet<Integer>();
		
		
		
		return users;
	}
	
	
	
	
	public String toString(String indent){
		StringBuilder output = new StringBuilder();
		output.append("item ID: "+item+" fatherId: "+father.item+"\n");
//		if(inducedVertexSet!=null) output.append(indent+"induced veritices: "+inducedVertexSet.toString()+"\n");
		if(folder!=null) output.append(indent+"  compressed item:"+folder.toString()+"\n");
		output.append(indent+"   users: "+vertexCore.size()+"\n");
		String newIndent = indent+"  ";
		for(KNode node:childList) 
			output.append(newIndent+node.toString(newIndent));
	
		return output.toString();
	}
	
}
