package hku.algo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import javax.swing.ListModel;
import javax.xml.crypto.NodeSetData;

/**
 * @author fangyixiang
 * @date Aug 11, 2015
 * A tree node in the cck-core tree index
 */
public class TNode implements Cloneable {
	private int core = - 1;
	private Set<Integer> nodeSet = null;
	private List<TNode> childList = null;
	private Map<String, int[]> kwMap = null;
	//**********************  Jan 6, 2017 CYK: add a father attribute
	private TNode father=this;
	
	public TNode(int core){
		this.core = core;
		this.nodeSet = new HashSet();
		this.childList = new ArrayList<TNode>();
		this.kwMap = new HashMap<String, int[]>();
	}
	
	public int getCore() {
		return core;
	}
	public void setCore(int core) {
		this.core = core;
	}
	
	//**********************  Jan 6, 2017 CYK: add relevant function of father attribute
	public TNode getFather(){
		return father;
	}
	public void setFather(TNode father){
		this.father=father;
	}
	
	public Set<Integer> getNodeSet() {
		return nodeSet;
	}

	public void setNodeSet(Set<Integer> nodeSet) {
		this.nodeSet = nodeSet;
	}



	public Map<String, int[]> getKwMap() {
		return kwMap;
	}

	public void setKwMap(Map<String, int[]> kwMap) {
		this.kwMap = kwMap;
	}

	public List<TNode> getChildList() {
		return childList;
	}

	public void setChildList(List<TNode> childList) {
		this.childList = childList;
	}
	 
	protected TNode clone() throws CloneNotSupportedException{
		TNode newnode=new TNode(this.core);
		Set<Integer> set=new HashSet<Integer>();
//		for(int x:nodeSet) set.add(x);
		set=(Set<Integer>) ((HashSet<Integer>) nodeSet).clone();
		newnode.setNodeSet(set);
		List<TNode> newList=new ArrayList<TNode>();
		for(TNode x:childList) newList.add(x.clone());
		
//		newList=(List<TNode>) ((ArrayList<TNode>) this.childList).clone();
		newnode.setChildList(newList);
		for(TNode x:newnode.getChildList()) x.setFather(newnode);
		
		newnode.setKwMap(this.kwMap);
		return newnode;
		
	}
	
	public static void main(String[] args) throws CloneNotSupportedException {
		TNode aNode=new TNode(1);
		Set<Integer> set=new HashSet<Integer>();
		for(int i=0;i<5;i++) set.add(i);
		
		aNode.setNodeSet(set);
		for(int i=0;i<5;i++){
			TNode newNode=new TNode(i);
			aNode.getChildList().add(newNode);
		}
		aNode.setFather(aNode);
		TNode bNode=(TNode) aNode.clone();
		bNode.getNodeSet().add(10);
//		TNode father=new TNode(11);
		
		System.out.println(aNode.getNodeSet());
		System.out.println(bNode.getNodeSet());
		
		
		
		
	}
	
}



