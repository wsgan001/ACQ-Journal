package algorithm.ProfiledTree;

import java.util.*;

/**
@author chenyankai
@Date	Jul 28, 2017
This is a class to read and build the complete profiled tree
*/
public class CPTree {
	private Map<Integer, PNode> cpTreeMap=null;	
	
	public CPTree(){
		//this map records the complete profiled tree
		this.cpTreeMap=new HashMap<Integer,PNode>();
	}


	//load the complete P-tree
	//top down manner
	public Map<Integer, PNode> LoadTree(){
		// the complete P-tree 
		//test data file: 
		Map<Integer,int[]> map=new HashMap<Integer,int[]>();
		int[]a1={2,3,10,12}; map.put(1, a1);
		int[]a2={};map.put(2, a2);
		int[]a3={4,7};map.put(3, a3);
		int[]a4={5,6};map.put(4, a4);
		int[]a5={};map.put(5, a5);
		int[]a6={};map.put(6, a6);
		int[]a7={8};map.put(7, a7);
		int[]a8={9};map.put(8, a8);
		int[]a9={};map.put(9, a9);
		int[]a10={11};map.put(10, a10);
		int[]a11={};map.put(11, a11);
		int[]a12={13,15};map.put(12, a12);
		int[]a13={14};map.put(13, a13);
		int[]a14={};map.put(14, a14);
		int[]a15={};map.put(15, a15);
		
		
		
		Iterator<Integer> it=map.keySet().iterator();
		while(it.hasNext()){
			int index=it.next();
			if(!cpTreeMap.containsKey(index)){
				PNode node=new PNode(index);
				for(int x:map.get(index)){
				PNode xNode=new PNode(x);
				xNode.setFather(node);
				node.addPNode(xNode);
				cpTreeMap.put(x, xNode);
				}
			cpTreeMap.put(index, node);
			}else{
				PNode node = cpTreeMap.get(index);
				for(int x:map.get(index)){
					PNode xNode=new PNode(x);
					xNode.setFather(node);
					node.addPNode(xNode);
					cpTreeMap.put(x, xNode);
				}
			}
		}
		return cpTreeMap;
	}
	
	
	
	
	
	
	
	
	
}
