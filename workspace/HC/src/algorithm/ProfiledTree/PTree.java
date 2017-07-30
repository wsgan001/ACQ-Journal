package algorithm.ProfiledTree;

import java.util.*;
/**
@author chenyankai
@Date	Jul 28, 2017

*/

public class PTree {
	//store the ptree of a user
	
	//complete P-tree map
	private Map<Integer, PNode> cpTreeMap=null;
	private PNode root=null;
	
	
	public PTree(){
		CPTree cpTree = new CPTree();
		this.cpTreeMap=cpTree.LoadTree();
	}
	
		
	//build p-tree from the complete P-tree
	public Map<Integer,PNode> buildPtree(int[] items){
	
		Map<Integer, PNode> queryPTree=new HashMap<Integer, PNode>();
		
		//build ptree in bottom-up manner 
		//**********************  Jul 30, 2017 CYK:  actually it can be build in top-down manner
		
		for(int i=items.length-1;i>0;i--){
			int item=items[i];
			int fatherItem=cpTreeMap.get(item).getFather().getId();
			PNode node=null;
			if(queryPTree.containsKey(item)) node=queryPTree.get(item);
			else {
				node=new PNode(item);
				queryPTree.put(item, node);
			}
			node.ascendOrder();

			if(queryPTree.containsKey(fatherItem)){
				PNode father=queryPTree.get(fatherItem);
				connectPNode(father, node);
			}else{
			
				PNode father=new PNode(fatherItem);
				connectPNode(father, node);
				queryPTree.put(fatherItem, father);
			}
		}
		root=queryPTree.get(items[0]);
		root.ascendOrder();
		return queryPTree;	
	}
	
	//for connect father node and child node 
	private void connectPNode(PNode father,PNode child){
		child.setFather(father);
		father.addPNode(child);
	}
	
	
	public List<Integer> tracePath(int x){
		List<Integer> path=new ArrayList<Integer>();
		while(cpTreeMap.get(x).getFather()!=null){
			path.add(x);
			x=cpTreeMap.get(x).getFather().getId();
		}
		path.add(x);
		Collections.reverse(path);
		return path;
	}
	
	
	public void testMap(Map<Integer, PNode> pTree){
		Iterator<Integer> it=pTree.keySet().iterator();
		while(it.hasNext()){
			int index=it.next();
			System.out.println("PNode "+pTree.get(index).getId()+" childList "+pTree.get(index).getChildlist().size());
			
		}
	}
	
	
	
	public void traverse(PNode root){
		System.out.println("now checking "+root.getId()+" childList size "+root.getChildlist().size());
		List<PNode> list=root.getChildlist();
		for(PNode x:list) traverse(x);
	}
	
	
	public String toString() {
		String temp ="";
		// append child nodes
		temp += root.toString("");
		return temp;
	}
	
	public static void main(String[] args){
		PTree pTree=new PTree();
		int[] items={1,2,3,7,8,10,11,12,13,15};
		pTree.buildPtree(items);
		System.out.println(pTree);
		
		
	}
	
}
