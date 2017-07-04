package prep;

import java.io.*;
import java.util.*;
import config.Config;
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
	
	public BuildMeshTree(){
		this.map=new HashMap<String,MeshNode>();
		this.treeFile=Config.localPath+"mtreeSimple.txt";
		
	}
	
	public Map<String, MeshNode> getMap(){return this.map;}

	
	public MeshNode build(){
		preBuildMap();
		this.root=new MeshNode("root","R",0);
		root.setCode("0");
		map.put("R", root);
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
				String leafCode= leaves.length>1 ? leaves[leaves.length-1]: leaves[0].substring(1);
				String fatherCode=leaves.length>1 ? contents[1].substring(0, contents[1].length()-leafCode.length()-1):contents[1].substring(0, contents[1].length()-leafCode.length());
				System.out.println(fatherCode);
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
	
	
	
	public void traverse(MeshNode root,String fileName){
//		System.out.print("name: "+root.getName()+"	----	");
//		System.out.println("code: "+root.getCode());
		try {
			BufferedWriter bWriter=new BufferedWriter(new FileWriter(Config.localPath+fileName+".txt",true));//continue to write
			bWriter.write(root.getName()+"	"+root.getoldCode()+"	"+root.getNewCode());
			bWriter.newLine();
			bWriter.flush();
			bWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("name "+root.getName()+" number "+root.getNum());
		
		ArrayList<MeshNode> list =root.getChildrenList();
		for(MeshNode node:list) traverse(node,fileName);
	}
	
	
	public void reCode(MeshNode root){
		String code=root.getNewCode()+"";
		int count=1;
		for(MeshNode node:root.getChildrenList()){
//			node.setNum(count);
			node.setCode(code+"."+count);
			count++;
		}
		for(MeshNode node:root.getChildrenList()){ reCode(node);}
	}
	
	
	
	
	
	public static void main(String[] args){
		BuildMeshTree bmTree=new BuildMeshTree();
		MeshNode root=bmTree.build();
		bmTree.reCode(root);
		bmTree.traverse(root,"MeshTree");
//		bmTree.printMap();
		
	}
	
}