package prep;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.omg.CORBA.PRIVATE_MEMBER;

import config.Config;

public class BuildMeshTree {
	private Map<String, MeshNode> map=null;
	private MeshNode root=null;
	private String treeFile=null;
	private Map<String, String> frstlvlMap=null;
	
	public BuildMeshTree(){
		this.map=new HashMap<String,MeshNode>();
		this.treeFile=Config.localPath+"mtrees2017.txt";
		
	}
	
	public MeshNode build(){
		preBuildMap();
		this.root=new MeshNode("root","R",0);
		map.put("root", root);
//		build1stLvl();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(treeFile));
			String line=null;
			int count=1;
			char letter='x';//initialize x as null 
			while((line=bReader.readLine())!=null){
				String[] contents=line.split(";");
				MeshNode node=null;
				if(letter!= contents[1].charAt(0)){
					letter=contents[1].charAt(0);
					node=new MeshNode(frstlvlMap.get(letter),letter+"",count);
					map.get("root").getChildrenList().add(node);
					map.put(contents[1], node);
				}else{
					String[] leaves=contents[1].split("\\.");// split \\. not . 
					//get last code of keyword such as 111 of A01.111; 01 of A01
					String leafCode= leaves.length>1 ? leaves[leaves.length-1]: leaves[0].substring(1);
					String fatherCode=leaves.length>1 ? contents[1].substring(0, contents[1].length()-leafCode.length()-1):contents[1].substring(0, contents[1].length()-leafCode.length());
					System.out.println(fatherCode);
					node=new MeshNode(contents[0],leafCode,count);
					map.get(fatherCode).getChildrenList().add(node);
					map.put(contents[1], node);
				}
				count++;
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in reading file!");
		}
		
		
		
		return root;
	}
	
	//build the first level taxa of the meshTree
	public ArrayList<MeshNode> build1stLvl(){
		ArrayList<MeshNode> list=new ArrayList<MeshNode>();
//		list.add(new MeshNode("Anatomy","A",1));
//		list.add(new MeshNode("Organisms","B",2));
//		list.add(new MeshNode("Diseases","C",3));
//		list.add(new MeshNode("Chemicals and Drugs","D",4));
//		list.add(new MeshNode("Analytical, Diagnostic and Therapeutic Techniques, and Equipment","E",5));
//		list.add(new MeshNode("Psychiatry and Psychology","F",6));
//		list.add(new MeshNode("Phenomena and Processes","G",7));
//		list.add(new MeshNode("Disciplines and Occupations","H",7));
//		list.add(new MeshNode("Anthropology, Education, Sociology, and Social Phenomena","I",8));
//		list.add(new MeshNode(,9));
//		list.add(new MeshNode("Humanities","K",10));
//		list.add(new MeshNode("Information Science","L",11));
//		list.add(new MeshNode(,12));
//		list.add(new MeshNode(,13));
//		list.add(new MeshNode("Publication Characteristics","V",14));
//		list.add(new MeshNode(,15));
		root.setChild(list);
		map.put("root", root);
		for(MeshNode node:list) map.put(node.getCode(), node);
		return list;
	}
	
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
		frstlvlMap.put("I","Anthropology, Education, Sociology, and Social Phenomena");
		frstlvlMap.put("J","Technology, Industry, and Agriculture");
		frstlvlMap.put("K","Humanities");
		frstlvlMap.put("L","Information Science");
		frstlvlMap.put("M","Named Groups");
		frstlvlMap.put("N","Health Care");
		frstlvlMap.put("V","Publication Characteristics");
		frstlvlMap.put("Z","Geographicals");
	}
	
	public void printMap(){
		for(String str:map.keySet()){
			MeshNode node=map.get(str);
			System.out.print("str "+str+"	------	");
			System.out.println("name "+node.getName());
		}
	}
	
	
	
	public void traverse(MeshNode root){
		System.out.print("name: "+root.getName()+"	----	");
		System.out.println("code: "+root.getCode());

		ArrayList<MeshNode> list =root.getChildrenList();
		for(MeshNode node:list) traverse(node);
	}
	
	public static void main(String[] args){
		BuildMeshTree bmTree=new BuildMeshTree();
		MeshNode root=bmTree.build();
		bmTree.traverse(root);
//		bmTree.printMap();
		
	}
	
}
