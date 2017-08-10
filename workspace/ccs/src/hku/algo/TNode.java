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
	 
	
	
}



