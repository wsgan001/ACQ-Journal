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
	public int[][] graph = null;
	private int[][] nodes = null;
	private int n = -1;
	private PNode pRoot = null;
	private Map<Integer,KNode> itemMap = null;
	private Map<Integer, List<KNode>> headList = null;
	
	private boolean debug = true;
	private String indexFile= Config.pubMedDataWorkSpace+"simpileKWindexFile.txt";

	
	public simKWTree(int[][] graph,int[][] nodes,PNode root){
		this.graph = graph;
		this.nodes = nodes;
		this.n = graph.length;
		this.pRoot = root;
		this.itemMap = new HashMap<Integer,KNode>();
		this.headList = new HashMap<Integer,List<KNode>>();   
	}
	
	public simKWTree(String graphFile,String nodesFile,PNode root){
		DataReader reader = new DataReader(graphFile, nodesFile);
		this.graph = reader.readGraph();
		this.nodes = reader.readNodes();
		this.n = graph.length;
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
		
		
		//step 2: scan the database 
		scan();		
		System.out.println("scan finished.");
		gc();
		
		//step 3:compress the index
		refine();
		System.out.println("refine finished.");
		
		long time = System.nanoTime();
		//step 4:compute the core number 
		computeCoreNumber();
		System.out.println("core number decomposition finished and its time cost: "+(System.nanoTime()-time)/1000000);
	
		printTree();
		
		
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
			
			List<KNode> list = headList.get(idx);
			if(list==null) list = new LinkedList<KNode>();

			for(int i=1;i<nodes[idx].length;i++){//skip the root
				int currentItem = nodes[idx][i];
				KNode currentNode = itemMap.get(currentItem);
				currentNode.tmpUsers.add(idx);
				
				if(currentNode.father.item!=previousItem){
					//update the headList
					list.add(previousNode);			
				}
				previousItem = currentItem; previousNode = currentNode;
			}
			//the last item must be the leaf item
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
		Iterator<Entry<Integer, KNode>> entryIter = itemMap.entrySet().iterator();
		int count=0;
		while(entryIter.hasNext()){
			
			KNode node = entryIter.next().getValue();
			if(node.item==1) continue;
			 count += node.tmpUsers.size();
			//get the total # of the users in one KNode
			
			if(node.tmpUsers.size()==0){
				KNode father = node.father;
				for(KNode child:node.childList){
					child.father = father;
					father.addChild(child);
					
					//store the compress information to child nodes
					Set<Integer> folder = child.folder;
					if(folder == null) folder = new HashSet<Integer>();
					folder.add(node.item);
					if(node.folder!=null) folder.addAll(node.folder);
					child.folder = folder;
				}
			father.childList.remove(node);
			entryIter.remove();	
			}
		
		}	
	//------------------------DEBUG------------------------------
	if(debug)	System.out.println("total vertices stored in index: "+count);
	//----------------------END DEBUG----------------------------	
	
	}
	
	
	private void computeCoreNumber(){
		Iterator<KNode> it = itemMap.values().iterator();
		while(it.hasNext()){
			KNode node = it.next();
			if(node.item==1) continue;
			int[][] subgraph = getsubGraph(node.tmpUsers);
			node.computeCore(subgraph);
			node.gc();
		}
		
	}
	
	private int[][] getsubGraph(Set<Integer> vertexSet){
		//the first element of the subgraph keeps the original vertex 
		int[] originalId = new int[vertexSet.size()+1];
		//step 1: build the subgraph in the matrix
		Map<Integer, Integer> old2New = new HashMap<Integer,Integer>();
		originalId[0]=0;
		int idx = 1;
		Iterator<Integer> iter = vertexSet.iterator();
		while(iter.hasNext()){
			int old = iter.next();
			originalId[idx] = old;
			old2New.put(old, idx++);
		}
		
		int[][] subgraph = new int[vertexSet.size()+1][];
		subgraph[0] = originalId;
		
		iter = vertexSet.iterator();
		idx = 1;
		while(iter.hasNext()){
			int old = iter.next();
			int arr[] = graph[old];
			
			int nghCount = 0;
			boolean[] check = new boolean[arr.length];
			for(int i=0;i<arr.length;i++){
				if(vertexSet.contains(arr[i])){
					check[i] = true;
					nghCount++;
				}
			}
			
			int newNeighborArr[] = new int[nghCount];
			int index = 0;
			for(int i=0;i<check.length;i++){
				if(check[i]){
					newNeighborArr[index++] = old2New.get(arr[i]);
				}
			}
			subgraph[idx++] = newNeighborArr;
		}
		
		return subgraph;
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
