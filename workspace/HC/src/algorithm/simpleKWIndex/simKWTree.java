package algorithm.simpleKWIndex;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import algorithm.DataReader;
import algorithm.DecomposeKCore;
import algorithm.ProfiledTree.PNode;
import algorithm.kwIndex.KWNode;
import config.Config;

/**
@author chenyankai
@Date	Sep 2, 2017
	(1). Original KW-tree index is not very efficient 
	(2). The proposed simplified KWtree index is not to build the KTree for each vertex
	(3). We use the global core number as the upper bound
	
*/ 
public class simKWTree {
	private int[][] graph = null;
	private int[][] nodes = null;
	private int n = -1;
	private int[] core = null;
	private PNode pRoot = null;
	private Map<Integer,KNode> itemMap = null;
	private Map<Integer, List<KNode>> headList = null;
	
	private boolean debug = true;
	private String indexFile= Config.pubMedDataWorkSpace+"simpileKWindexFile.txt";

	
	public simKWTree(int[][] graph,int[][] nodes,PNode root){
		this.graph = graph;
		this.nodes = nodes;
		this.n = graph.length;
		this.core = new int[n+1];
		this.pRoot = root;
		this.itemMap = new HashMap<Integer,KNode>();
		this.headList = new HashMap<Integer,List<KNode>>();   
	}
	
	public simKWTree(String graphFile,String nodesFile,PNode root){
		DataReader reader = new DataReader(graphFile, nodesFile);
		this.graph = reader.readGraph();
		this.nodes = reader.readNodes();
		this.n = graph.length;
		this.core = new int[n+1];
		this.pRoot = root;
		this.itemMap = new HashMap<Integer,KNode>();
		this.headList = new HashMap<Integer,List<KNode>>();   
	}
	
	public void build(){
		//step 1: initialize the index
		KNode root = new KNode(1);
		itemMap.put(1, root);
		init(pRoot);
		System.out.println("init finished.");
		
		//step 2:compute the k-cores
		DecomposeKCore kCore = new DecomposeKCore(graph);
		core = kCore.decompose();
		System.out.println("k core decompose finished.");
		
		//step 3: scan the database 
		scan();		
		System.out.println("scan finished.");
		
//		count();
		gc();
		
		//step 4:compress the index
		refine();
		System.out.println("refine finished.");
	}
	
	//recursively initialize the KWIndex using known CP-tree
	private void init(PNode node){
		KNode kNode = itemMap.get(node.getId());
		for(PNode child:node.getChildlist()){
			int childItem = child.getId();
			KNode itemNode = new KNode(childItem);
			itemNode.father = kNode;
			kNode.addChild(itemNode);
			itemMap.put(childItem, itemNode);
		}	
		for(PNode child:node.getChildlist()) init(child);
		
	}
	
	
	//scan the database and fill the index
	private void scan(){
		int idx = 1;
		while(idx<n){
			int previousItem = nodes[idx][0];
			KNode previousNode = null; 
			int coreNum = core[idx];
			
			List<KNode> list = headList.get(idx);
			if(list==null) list = new LinkedList<KNode>();

			for(int i=1;i<nodes[idx].length;i++){//skip the root
				int currentItem = nodes[idx][i];
				KNode currentNode = itemMap.get(currentItem);
				if(currentNode.father.item!=previousItem){
					Set<Integer> userSet=previousNode.vertices.get(coreNum);
					if(userSet==null){
						userSet = new HashSet<Integer>();
						userSet.add(idx);
						previousNode.vertices.put(coreNum, userSet);
					}else {
						previousNode.vertices.get(coreNum).add(idx);
					}
					
					//update the headList
					list.add(previousNode);			
				}
				previousItem = currentItem; 
				previousNode = currentNode;
			}
			
			//the last item must be the leaf item
			Set<Integer> userSet=previousNode.vertices.get(coreNum);
			if(userSet==null){
				userSet = new HashSet<Integer>();
				userSet.add(idx);
				previousNode.vertices.put(coreNum, userSet);
			}else {
				previousNode.vertices.get(coreNum).add(idx);
			}
				
			//update the headList
			list.add(previousNode);
			
			headList.put(idx, list);
			idx++;
		}	
	}
	
	private void gc(){
		this.nodes = null;
	}
	
	private void refine(){	
		int totalCount = 0; 
		Iterator<Entry<Integer, KNode>> enIt = itemMap.entrySet().iterator();
		enIt.next();//skip the root node
		while(enIt.hasNext()){
			int count = 0;
			Entry<Integer, KNode> entry = enIt.next();
			KNode node = entry.getValue();
			
			//get the total # of the users in one KNode
			Iterator<Set<Integer>> iter=node.vertices.values().iterator();
			while(iter.hasNext()){
				count += iter.next().size();
			}
			totalCount += count;
			
			if(count==0){
				KNode father = node.father;
				for(KNode child:node.getChildList()){
					child.father = father;
					father.addChild(child);
					
					//store the compress information to child nodes
					Set<Integer> folder = child.folder;
					if(folder == null) folder = new HashSet<Integer>();
					folder.add(node.item);
					if(node.folder!=null) folder.addAll(node.folder);
					child.folder = folder;
				}
			father.getChildList().remove(node);
			enIt.remove();	
			}
			

		}	
	//------------------------DEBUG------------------------------
	if(debug)	System.out.println("total vertices stored in index: "+totalCount);
	//----------------------END DEBUG----------------------------	
	
	}
	
	
	
	
	//induce all users in one specific item in index

	public Set<Integer> induceUsers(int k,int item){
		Queue<KNode> queue = new LinkedList<KNode>();
		Set<Integer> set = new HashSet<Integer>();
		KNode node = itemMap.get(item);
		queue.add(node);
		while(!queue.isEmpty()){
			KNode check = queue.poll();
			Iterator<Entry<Integer, Set<Integer>>> enIt = check.vertices.entrySet().iterator();
			while(enIt.hasNext()){
				Entry<Integer, Set<Integer>> entry = enIt.next();
				if(entry.getKey()>=k){
					set.addAll(entry.getValue());
				}
			}
			for(KNode child:check.getChildList()) queue.add(child);			
		}	
	return set;
	}
	
	public int[] getCore(){
		return this.core;
	}
	
	private void count(){
		int count = 0;
		Iterator<KNode> It = itemMap.values().iterator();
		while(It.hasNext()){
			KNode node = It.next();
			Iterator<Set<Integer>> it = node.vertices.values().iterator();
			while(it.hasNext()) count+=it.next().size();
		}
		System.out.println("first count:" +count);
	}
	
	
	public void printTree(){		
		File file = new File(indexFile);
		if(file.exists()) file.delete();
		try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
			fileWriter.write(itemMap.get(1).toString(""));
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<Integer, List<KNode>> getHeadList(){
		return this.headList;
	}
	
	public boolean contains(int item){
		return itemMap.containsKey(item);
	}
	
	public KNode getRoot(){
		return itemMap.get(1);
	}
	
	
	
}
