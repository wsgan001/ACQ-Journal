package prep;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import config.Config;

public class BuildMeshTree {
	private Map<String, MeshNode> map=null;
	private MeshNode root=null;
	private String treeFile=null;
	
	
	public BuildMeshTree(){
		this.map=new HashMap<String,MeshNode>();
		this.treeFile=Config.localPath+"mtrees2017.txt";
		
	}
	
	public MeshNode build(){
		this.root=new MeshNode("root", 0);
		build1stLvl();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(treeFile));
			String line=null;
			int count=0;
			while((line=bReader.readLine())!=null){
				String[] contents=line.split(";");
				char letter= contents[1].charAt(0);
				String[] leaves=contents[1].split("\\.");// split \\. not . 
				//get last code of keyword such as 111 of A01.111; 01 of A01
				String leafCode= leaves.length>1 ? leaves[leaves.length-1]: leaves[0].substring(1);
				String fatherCode=leaves.length>1 ? contents[1].substring(0, contents[1].length()-leafCode.length()-1):contents[1].substring(0, contents[1].length()-leafCode.length());
				System.out.println(fatherCode);
				MeshNode node=new MeshNode(contents[0],Integer.parseInt(leafCode));
				map.get(fatherCode).getChildrenList().add(node);
				map.put(contents[1], node);
				count++;
				if(count==20) break;
				
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
		list.add(new MeshNode("Anatomy",'A'));
		list.add(new MeshNode("Organisms",'B'));
		list.add(new MeshNode("Diseases",'C'));
		list.add(new MeshNode("Chemicals and Drugs",'D'));
		list.add(new MeshNode("Analytical, Diagnostic and Therapeutic Techniques, and Equipment",'E'));
		list.add(new MeshNode("Psychiatry and Psychology",'F'));
		list.add(new MeshNode("Phenomena and Processes",'G'));
		list.add(new MeshNode("Disciplines and Occupations",'H'));
		list.add(new MeshNode("Anthropology, Education, Sociology, and Social Phenomena",'I'));
		list.add(new MeshNode("Technology, Industry, and Agriculture",'J'));
		list.add(new MeshNode("Humanities",'K'));
		list.add(new MeshNode("Information Science",'L'));
		list.add(new MeshNode("Named Groups",'M'));
		list.add(new MeshNode("Health Care",'N'));
		list.add(new MeshNode("Publication Characteristics",'V'));
		list.add(new MeshNode("Geographicals",'Z'));
		root.setChild(list);
		map.put("root", root);
		for(MeshNode node:list) map.put((char)node.getCode()+"", node);
		return list;
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
