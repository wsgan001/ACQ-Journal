package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import algorithm.ProfiledTree.PNode;

public class CPTreeReader {
	String cptreeFile = null;
	
	public CPTreeReader(String file){
		this.cptreeFile = file;
	}
	

	public Map<Integer, PNode> loadCPTree(){
		Map<Integer, PNode> cpTree = new HashMap<Integer,PNode>();
		try {
			BufferedReader breader = new BufferedReader(new FileReader(cptreeFile));
			String line = null;
			while((line=breader.readLine())!=null){
				String[] str = line.split(",");
				if(str[0].equals(str[1])){
					int rootItem = Integer.parseInt(str[0]);
					PNode root = new PNode(Integer.parseInt(str[0]));
					cpTree.put(rootItem, root);
				}else{
					int item = Integer.parseInt(str[0]);
					int fatherItem = Integer.parseInt(str[1]);
					PNode node = cpTree.get(item);
					if(node==null){
						node = new PNode(item);
						cpTree.put(item, node);
					}
					
					PNode fatherNode = cpTree.get(fatherItem);
					if(fatherNode==null) {
						fatherNode = new PNode(fatherItem);
						cpTree.put(fatherItem, fatherNode);
					}
					node.setFather(fatherNode);
					fatherNode.addPNode(node);
				}
			}	
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("aaa");
			e.printStackTrace();
		}				
		return cpTree;
	}
 	
	
	public Set<Integer> completeCPTree(Set<Integer> itemSet, Map<Integer, PNode> cptree){
		Set<Integer> PTree = new HashSet<Integer>();
		for(Iterator<Integer> iter = itemSet.iterator();iter.hasNext();){
			int item = iter.next();
			while(true){
				PTree.add(item);
				item = cptree.get(item).father.getId();
				if(item==1||PTree.contains(item)) break;
			}
			PTree.add(1);
		}
		return PTree;
	}
	
	
	public PNode loadCPtreeRoot(){
		return loadCPTree().get(1);
	}
	
	
}
