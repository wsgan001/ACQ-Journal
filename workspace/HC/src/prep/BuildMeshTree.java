package prep;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import algorithm.ProfiledTree.PNode;
/**
 * 
 * @author chenyankai
 * @date Apr 26, 2017
 */

public class BuildMeshTree {
	private Map<String, MeshNode> map=null;// key:Node.code  value:meshNode
	private MeshNode root=null;
	private String treeFile=null;
	private Map<String, String> frstlvlMap=null;//store the first level taxa: A-Z
	private String outFileName=null;
	private String CPTreeFile=null;
	private boolean debug=true;
	private Map<Integer, PNode> cpTree=null;

	
	
	
	private int count=1;
	
	public BuildMeshTree(){
		this.map=new HashMap<String,MeshNode>();
		this.treeFile=ConfigPubmed.localPath+"mtreeSimple.txt";
		this.outFileName=ConfigPubmed.localPath+"MeshTree.txt";
		this.CPTreeFile=ConfigPubmed.localPath+"cptree.txt";
		this.cpTree=new HashMap<Integer,PNode>();
	}
	
	public Map<String, MeshNode> getMap(){return this.map;}

	
	public MeshNode buildMeshTree(){
		preBuildMap();
		this.root=new MeshNode("root","R",0);
		root.setCode("0");
		map.put("R", root);
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(treeFile));
			String line=null;
			int count=1;
			char letter='x';//initialize x as null 
			while((line=bReader.readLine())!=null){
				String[] contents=line.split(";");
				MeshNode node=null;
				if(letter!= contents[1].charAt(0)){
					//build the first level taxon ie,A-Z 16 taxa
					letter=contents[1].charAt(0);
					
					node=new MeshNode(frstlvlMap.get(letter+""),letter+"",count);
					map.get("R").getChildrenList().add(node);
					map.put(letter+"", node);
					count++;
				}
				//build the next level taxa
				String[] leaves=contents[1].split("\\.");// split \\. not . 
				//get last code of keyword such as 111 of A01.111; 01 of A01
				String leafCode= leaves.length> 1 ? leaves[leaves.length-1]: leaves[0].substring(1);
				String fatherCode=leaves.length> 1 ? contents[1].substring(0, contents[1].length()-leafCode.length()-1):contents[1].substring(0, contents[1].length()-leafCode.length());
				node=new MeshNode(contents[0],contents[1],count);
				map.get(fatherCode).getChildrenList().add(node);
				map.put(contents[1], node);
				count++;	
			}
			System.out.println("count number "+count);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in reading file!");
		}
		reCode(root);
		DFSCode(root);
		return root;
	}
	
	//build the first level taxa of the meshTree
	private void preBuildMap(){
		this.frstlvlMap=new HashMap<String,String>();
		frstlvlMap.put("A","Anatomy");
		frstlvlMap.put("B","Organisms");
		frstlvlMap.put("C","Diseases");
		frstlvlMap.put("D","Chemicals and Drugs");
		frstlvlMap.put("E","Analytical, Diagnostic and Therapeutic Techniques, and Equipment");
		frstlvlMap.put("F","Psychiatry and Psychology");
		frstlvlMap.put("G","Phenomena and Processes");
		frstlvlMap.put("H","Disciplines and Occupations");
		
//		frstlvlMap.put("I","Anthropology, Education, Sociology, and Social Phenomena");
//		frstlvlMap.put("J","Technology, Industry, and Agriculture");
//		frstlvlMap.put("K","Humanities");
//		frstlvlMap.put("L","Information Science");
//		frstlvlMap.put("M","Named Groups");
//		frstlvlMap.put("N","Health Care");
//		frstlvlMap.put("V","Publication Characteristics");
//		frstlvlMap.put("Z","Geographicals");
//		
	}
	
	public void printMap(){
		for(String str:map.keySet()){
			MeshNode node=map.get(str);
			System.out.print("str "+str+"	------	");
			System.out.println("name "+node.getName());
		}
	}
	

	
	public Map<String, Integer> getOldCodeDFSMap(){
		Map<String,Integer> map=new HashMap<String,Integer>();
		oldCodeDFS(root, map);
		return map;
	}
	
	private void oldCodeDFS(MeshNode root,Map<String, Integer> map){
//		System.out.println("old code "+root.getDFSNo());
		String oldCode=root.getoldCode();
		if(!map.containsKey(oldCode)) map.put(oldCode, root.getDFSNo());
		for(MeshNode node:root.getChildrenList()) oldCodeDFS(node, map);
	}
	
	
	public Map<Integer, PNode> getCPTree(){
		copyNode(root);
		return this.cpTree;
	}
	
	public void writeCPTree(){
		copyNode(root);
		PNode PTreeRoot=cpTree.get(1);
		try {
			FileWriter fWriter=new FileWriter(this.CPTreeFile);
			fWriter.write("");
			fWriter.close();
			fWriter=new FileWriter(this.CPTreeFile,true);
			fWriter.write(PTreeRoot.toString(""));
			fWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	private void copyNode(MeshNode root){
		int item=root.getDFSNo();
		PNode newPNode=null;
		if(!cpTree.containsKey(item)) {
			newPNode=new PNode(item);
			cpTree.put(item, newPNode);
		}
		else newPNode=cpTree.get(item);
	
		List<MeshNode> list=root.getChildrenList();
		for(MeshNode node:list){
			int nodeItem=node.getDFSNo();
			PNode childNode=null;
			if(!cpTree.containsKey(nodeItem)){
				childNode=new PNode(nodeItem);
				cpTree.put(nodeItem, childNode);
			}
			else childNode=cpTree.get(nodeItem);
			
			newPNode.addPNode(childNode);
			childNode.setFather(newPNode);
		}
		
		//recursively copy children nodes
		for(MeshNode node:list) copyNode(node);
	}
	
	
	//write the meshtree
	public void write(){
		try {
			//clear existing contents
			FileWriter fileWriter=new FileWriter(this.outFileName);
			fileWriter.write("");
			fileWriter.close();
			
			//rewrite the files 
			fileWriter=new FileWriter(this.outFileName,true);
			fileWriter.write(root.toString(""));
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		writeFile(root, this.outFileName);
	}
	
	//recursively write file; Not recommended
	private void recursiveLyWriteFile(MeshNode root,String file){
		try {
			FileWriter fileWriter=new FileWriter(file,true);
			BufferedWriter bWriter=new BufferedWriter(fileWriter);//continue to write
			bWriter.write(root.getName()+"	"+root.getoldCode()+"	"+root.getNewCode()+"	"+root.getDFSNo());
			bWriter.newLine();
			bWriter.flush();
			bWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		ArrayList<MeshNode> list =root.getChildrenList();
		for(MeshNode node:list) recursiveLyWriteFile(node,file);
	}

	//recode the new code
	private void reCode(MeshNode root){
		String code=root.getNewCode()+"";
		int BFScount=1;
		for(MeshNode node:root.getChildrenList()){
//			node.setNum(count);
			node.setCode(code+"."+BFScount);
			BFScount++;
		}
		for(MeshNode node:root.getChildrenList()){ reCode(node);}
	}
	
	//recode the DFS code of the MeshNode 
	private void DFSCode(MeshNode root){
		root.setDFSNo(count);
		count++;
		for(MeshNode node:root.getChildrenList()){
			DFSCode(node);
		}
	}
	
	public String toString() {
		String temp ="";
		// append child nodes
		temp += root.toString("");
		return temp;
	}
	
	
	
	public static void main(String[] args){
		BuildMeshTree bmTree=new BuildMeshTree();
		MeshNode root=bmTree.buildMeshTree();
//		System.out.println(bmTree);
//		bmTree.write();
		bmTree.getCPTree();
//		bmTree.writeCPTree(map.get(1));
		
	}
	
}
