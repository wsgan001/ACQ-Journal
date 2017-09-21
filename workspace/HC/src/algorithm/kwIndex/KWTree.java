package algorithm.kwIndex;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import EXP.test;
import algorithm.DataReader;
import algorithm.ProfiledTree.PNode;
import config.Config;

public class KWTree {
	public int[][]graph = null;
	private int[][]nodes = null;
	private int n = -1; 
	private KWNode root = null;
	private PNode pRoot = null;
	private Map<Integer, KWNode> itemMap = null;
//	private Map<Integer,Set<KWNode>> headMap = null;
	private Map<Integer,List<KWNode>> headList = null;
	
	private boolean debug = true; 
	private String indexFile= Config.workSpace+"indexFile.txt";
	
	
	public KWTree(int[][] graph,int[][]nodes,PNode pRoot){
		this.graph = graph;
		this.nodes = nodes;
		this.n = graph.length;
		this.pRoot = pRoot;
		this.itemMap = new HashMap<Integer,KWNode>();
		this.headList = new HashMap<Integer,List<KWNode>>();
	}
	
	
	public KWTree(String graphFile,String nodeFile,PNode pRoot){
		DataReader dReader = new DataReader(graphFile, nodeFile);
		this.graph = dReader.readGraph();
		this.nodes = dReader.readNodes();
		this.n = graph.length;
		this.pRoot = pRoot;
		this.itemMap = new HashMap<Integer,KWNode>();
		this.headList = new HashMap<Integer,List<KWNode>>();
		
	}
	
	
	public void build(){
//		long time=System.nanoTime();
		//step 1: initialize the index
		init(pRoot);
		
		//step 2: scan the database 
		scan();
		

		//step 3:compress the index
		refine();

		//step 4:build the K-tree for each kwnode
		buildKTree();
//		System.out.println((System.nanoTime()-time)/1000000);
		
	}
	
	//initialize the index
	private void init(PNode pRoot){
		root = new KWNode(pRoot.getId());
		itemMap.put(root.itemId, root);
		loadCPTree(pRoot);
		//------------------------DEBUG------------------------------
		if(debug) 		System.out.println("Index initialization finished!");
		//----------------------END DEBUG----------------------------
	}
	
	
	private void loadCPTree(PNode pNode){
		KWNode root = itemMap.get(pNode.getId());
		for(PNode child:pNode.getChildlist()) {
			int childItem = child.getId();
			KWNode itemNode = new KWNode(childItem);
			itemNode.father = root;
			root.childList.add(itemNode);
			itemMap.put(childItem, itemNode);
		}
		for(PNode child:pNode.getChildlist()) loadCPTree(child);	
	}
	
	
	private void scan(){
		int idx = 1;
		while(idx<n){
			List<KWNode> list = headList.get(idx);

			
			for(int i=1; i< nodes[idx].length;i++){
				int previousItem = nodes[idx][i-1];
				int item = nodes[idx][i];
				itemMap.get(item).tmpVertexSet.add(idx);
				
				//update the headMap key:userId value:leaf KWNode containing userId
				KWNode itemNode = itemMap.get(item);
				KWNode previousItemNode = itemMap.get(previousItem);
				if(itemNode.father != previousItemNode){
	
					if(list==null){
						list = new LinkedList<KWNode>();
						list.add(previousItemNode);
						headList.put(idx, list);
					}else{
						list.add(previousItemNode);
					}
				}
			}
			//the last one must be the leaf KWNode
			int lastOne = nodes[idx][nodes[idx].length-1];
			KWNode lastNode = itemMap.get(lastOne);
			if(list==null){
				list=new LinkedList<KWNode>();
				list.add(lastNode);
				headList.put(idx, list);
			}else{
				list.add(lastNode);
			}
			idx++;
		}
		
		gc();
		//------------------------DEBUG------------------------------
		if(debug) 		System.out.println("Scan database finished!");
		//----------------------END DEBUG----------------------------
	}
	
		
	//compress the index. Delete nodes with same vertices 
	//LEMMA_anti-monotonicity: if fre of fatheritem should be no less than that of childItem. 
	//which means if # is equal, then their vertices must be the same.
	private void refine(){		
		
		//------------------------DEBUG------------------------------
		int count = 0;
		//----------------------END DEBUG----------------------------
		
		Iterator<Entry<Integer, KWNode>> entryIter = itemMap.entrySet().iterator();
		entryIter.next();//skip the root node
		while(entryIter.hasNext()){
			KWNode node = entryIter.next().getValue();
			
			count += node.tmpVertexSet.size();
			
			if(node.tmpVertexSet.size() == 0){
				
				KWNode father = node.father;
				for(KWNode child:node.childList){
					child.father = father;
					father.childList.add(child);
					
					//8.25 update the compress set
					Set<Integer> compressedSet=child.compressedId;
					if(compressedSet==null){
						compressedSet=new HashSet<Integer>();
					}
					compressedSet.add(node.itemId);
					child.compressedId=compressedSet;
					if(node.compressedId!=null) compressedSet.addAll(node.compressedId);
				
				}
				father.childList.remove(node);
//				node.refined = true;
				node = null;
				entryIter.remove();
			}
//			else{
//
//				if(node.childList.isEmpty()) continue;
//				int size=node.tmpVertexSet.size();
//				
//				count += size;
//				
//				boolean flag = true;
//				for(KWNode child:node.childList){
//					//9.16 debug: if child.tmpVertexSet.isempty, we do not refine the current node
//					if(child.tmpVertexSet.isEmpty()  ||    (!child.tmpVertexSet.isEmpty() &&child.tmpVertexSet.size() != size)){	
//						flag = false;
//						break;
//					}
//				}
//				if(flag){
//					for(KWNode child:node.childList){
//						child.father = node.father;
//						node.father.childList.add(child);
//						
//						//8.25 update the compress set
//						Set<Integer> compressedSet=child.compressedId;
//						if(compressedSet==null){
//							compressedSet=new HashSet<Integer>();
//						}
//						compressedSet.add(node.itemId);
//						child.compressedId=compressedSet;
//						if(node.compressedId!=null) compressedSet.addAll(node.compressedId);
//					
//					}
//					node.father.childList.remove(node);
////					node.refined = true;
//					entryIter.remove();
//					node = null;
//				}
//			}
		}
		
		//------------------------DEBUG------------------------------
		if(debug) {
			System.out.println("Refinement finished!"+" total vertices: "+count);
			
		}
	
		//----------------------END DEBUG----------------------------
	}
	
	
	private void buildKTree(){
		Iterator<KWNode> iter = itemMap.values().iterator();
		iter.next();//skip the root
		long time1 = 0;
		long time2 = 0;
		long time3 = 0;
		long time4 = 0;
		
 		while(iter.hasNext()){
			KWNode node = iter.next();
			Set<Integer> vertexSet = node.tmpVertexSet;	
	
			time1 = System.nanoTime();
			int[][] subGraph = getsubGraph(vertexSet);
			time2 += System.nanoTime()-time1;
			
			time3 = System.nanoTime();
			KTree kTree = new KTree(subGraph);
			kTree.build();
			time4 += System.nanoTime()-time3;
			node.setvertexMap(kTree.getVertexMap());
			
			//------------------------DEBUG------------------------------
			if(debug) node.KtreeRoot = kTree.getRoot();
			//----------------------END DEBUG----------------------------
			node.gc();
			
		}
 		
 		//------------------------DEBUG------------------------------
 		if(debug) {
 			System.out.println("build internal k-tree finished!");
 			System.out.println("first getsubgraph time cost: "+time2/1000000+"    "
 					+ "ktree index build time: "+time4/1000000+" total:"+(time2+time4)/1000000);
 			
 		}
 		
 		//----------------------END DEBUG----------------------------
	}
	
	
	//release the nodes[][] 
	private void gc(){
		this.nodes= null;
	}
	
	

	public int[][] getsubGraph(Set<Integer> vertexSet){
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
			fileWriter.write(root.toString(""));
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<Integer,List<KWNode>> getHeadList(){
		return this.headList;
	}

	public boolean containsItem(int item){
		return itemMap.containsKey(item);
	}
	
	
}
