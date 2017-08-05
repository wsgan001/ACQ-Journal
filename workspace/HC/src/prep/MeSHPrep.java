package prep;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.ListModel;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import algorithm.ProfiledTree.PNode;

/**
 * 
 * @author chenyankai
 * @date Mar 28, 2017
 */

public class MeSHPrep {
	//	aggregated edges and nodes file.No vertices without items in P-tree are included.
	private String nodeFile=null;
	private String edgeFile=null;
	
	boolean type=true;
	
	int id1=-1;
//	Map<String, String> map=null;//store the MeshTree code key:name value:old code
	
	private Map<String, Integer> oldCodeDFSMap=null;//store the mesh old code and the DFS code
	private Map<String, Integer> nameVsDFSMap=null;//Store the name and DFS code
	
	private Map<String, Set<Integer>> line = null;//for output the name-attributes
	private Map<String, Set<String>> nameMap=null;//for output the name-edges
	private Map<String,Integer> nameIdMap=null;
	private Map<Integer,PNode> cpTree=null;
	
	
	//type 1:aggregated data type0: full data 
	public MeSHPrep(String nodeFile,String edgeFile,String treeFile){
		
			this.edgeFile=ConfigPubmed.localPath+edgeFile;
			this.nodeFile=ConfigPubmed.localPath+nodeFile;
			
			File edgefile=new File(this.edgeFile);
			if(edgefile.exists()) edgefile.delete();
			File nodefile=new File(this.nodeFile);
			if(nodefile.exists()) nodefile.delete();
		
		
		this.id1=1;
		this.oldCodeDFSMap=new HashMap<String,Integer>();
		this.nameVsDFSMap=new HashMap<String, Integer>();
		
		this.line=new HashMap<String, Set<Integer>>();
		this.nameMap=new HashMap<String,Set<String>>();
		this.nameIdMap=new HashMap<String,Integer>();
		readMeshTree(treeFile);
	}
	
	public int getnameMapSize(){
		return this.nameMap.size();
	}
	//to get the map key:old code value: DFS code
	private void loadPtree(){
		BuildMeshTree bTree=new BuildMeshTree();
		bTree.buildMeshTree();
		this.oldCodeDFSMap=bTree.getOldCodeDFSMap();
		this.cpTree=bTree.getCPTree();
	}
	
	
	
	
	
	private void readMeshTree(String fileName){
		loadPtree();
		
		try {
			BufferedReader bfReader=new BufferedReader(new FileReader(ConfigPubmed.localPath+fileName));
			if(fileName=="mtrees2017.txt"){
				String line=null;
				while((line=bfReader.readLine())!=null){
					String[] s=line.split(";");
					nameVsDFSMap.put(s[0], oldCodeDFSMap.get(s[1]));
				}
			}else{
				String line=null;
				while((line=bfReader.readLine())!=null){
					String[] s=line.split("\t");
					if(s.length>=2) nameVsDFSMap.put(s[0], Integer.parseInt(s[3]));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	//extract one tag and create profiled attributes and edges
	public boolean domMeSH(String xmlFile){
		boolean state=true;
		try {
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder=factory.newDocumentBuilder();
			Document doc=builder.parse(new File(ConfigPubmed.localPath+xmlFile));
			Element element=doc.getDocumentElement();
//			BufferedWriter bfWriter=new BufferedWriter(new FileWriter(Config.localPath+outFile,true));//append the content at the end of the file
			
//			Map<String, Set<String>> line = new HashMap<String, Set<String>>();//for output the name-attributes
			

			NodeList nodes=element.getElementsByTagName("PubmedArticle");
			System.out.println("number of citatios: "+nodes.getLength());
			
			//step 1:read all vertices and nodes
			for(int i=0;i<nodes.getLength();i++){
				Element Article=(Element) nodes.item(i);
				Element MedlineCitation= (Element) Article.getElementsByTagName("MedlineCitation").item(0);
				Element authorListNode = (Element) MedlineCitation.getElementsByTagName("AuthorList").item(0);
				Element MeshKeyListNode= (Element) MedlineCitation.getElementsByTagName("MeshHeadingList").item(0);
				boolean flag=false;//mark if there are authors in one citation
				ArrayList<String> nameList=null;
				//find all the author
				if(authorListNode!=null){
					flag=true;
					NodeList autherList= authorListNode.getElementsByTagName("Author");
					nameList=new ArrayList<String>();
					for(int j=0;j<autherList.getLength();j++){
						Element author=(Element) autherList.item(j);
							NodeList childList=author.getChildNodes();
							String name=null;
							for(int x=0;x<childList.getLength();x++){
								Node node=childList.item(x);
								String tag=node.getNodeName();
								if(tag.equals("LastName")){
									name=node.getFirstChild().getNodeValue();
								} 
								if(tag.equals("Initials")){
									name=node.getFirstChild().getNodeValue()+","+name;
								}
							}		
							if(name!=null){
//								if(name.equals("JC,Chen"))System.out.println(name);
								nameList.add(name);
								Set<Integer> set=new HashSet<Integer>();
								if(!line.containsKey(name))line.put(name,set);			
							}
					}
				}
				
				//create edges 
				 createEdge(nameList);
				
				//find all the meshCode with mesh
				if(flag==true){
					Set<Integer> set=new HashSet<Integer>();
					
					if(MeshKeyListNode!=null){ //if one man has at least one keywords
						NodeList meshList=MeshKeyListNode.getElementsByTagName("DescriptorName");					
						for(int k=0;k<meshList.getLength();k++){
							Element mesh=(Element) meshList.item(k);
//							String key=null;
							int key=-1;
							String meshName=mesh.getFirstChild().getNodeValue();
//							if(map.containsKey(meshValue)){
							if(nameVsDFSMap.containsKey(meshName)){
								key = nameVsDFSMap.get(meshName);
							}
//							if(key!=null){
							if(key!=-1){
								set.add(key);
							}
						}
			
						for(String x:nameList){
						if(line.containsKey(x)){
								line.get(x).addAll(set);
							}else{
								line.put(x,set);								
							}
						}
						
					}
				}
			}
		
			
		} catch (Exception e) {
			// TODO: handle exception
			state=false;
			e.printStackTrace();
		}
		return state;
	}
	
	private void createEdge( ArrayList<String> set){
		if(set==null) return;
		
		for(String x:set){
			if(nameMap.containsKey(x)){
				for(String y:set){
					if(!x.equals(y)){nameMap.get(x).add(y);}
				}
			}else{
				Set<String> tmp=new HashSet<String>();
				for(String y:set){
					if(!x.equals(y)){tmp.add(y);}
				}
				nameMap.put(x, tmp);
			}
		}
		
	}
	
	
	public void writeAllFile(){
		try{
			BufferedWriter nodeWriter = new BufferedWriter(new FileWriter(nodeFile,true));
			BufferedWriter edgeWriter = new BufferedWriter(new FileWriter(edgeFile,true));
			//print out all qualified edges and nodes
			//write into the file (attributes)
			
			for(String x:line.keySet()){
				String write=line.get(x).toString();	
				//**********************  Aug 5, 2017 CYK:  use nameIdMap to filter out those vertices without keywords
				nameIdMap.put(x, id1);
				write = write.substring(1, write.length()-1);
	
				//the format of writing files for debug 
//				 nodeWriter.write(x+" "+(id1++)+"\t"+ fill(write));
				 nodeWriter.write((id1++)+"\t"+ fill(write));
				 nodeWriter.newLine();	
						
			}
			 nodeWriter.flush();
			 nodeWriter.close();
			
			//edge 
				for(String x:nameMap.keySet()){
					if(!nameIdMap.containsKey(x)) continue;
					String string="";
					for(String y:nameMap.get(x)){
						if(nameIdMap.containsKey(y))
							string+=" "+nameIdMap.get(y);
					}
					if(string.length()>2){
						//the format of writing files for debug 
//						edgeWriter.write(x+" "+nameIdMap.get(x)+"\t"+string.substring(1, string.length()));
						edgeWriter.write(nameIdMap.get(x)+"\t"+string.substring(1, string.length()));

					}
					else{
						//the format of writing files for debug 
//						edgeWriter.write(x+" "+nameIdMap.get(x)+"\t");
						edgeWriter.write(nameIdMap.get(x)+"\t");

					}
					edgeWriter.newLine();
					
				}
				edgeWriter.flush();
				edgeWriter.close();
			
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	//write the vertices and nodes and skip the vertices without any items in P-tree
	public void writeFile(){
		try {
			BufferedWriter nodeWriter = new BufferedWriter(new FileWriter(nodeFile,true));//write node
			BufferedWriter edgeWriter = new BufferedWriter(new FileWriter(edgeFile,true));//write edge 
			

			//print out all qualified edges and nodes
			//write into the file (attributes)
			for(String x:line.keySet()){
					
					if(line.get(x).size()!=0){
						
						String write=line.get(x).toString();
						write=write.substring(1, write.length()-1);
				
						//**********************  Aug 5, 2017 CYK:  use nameIdMap to filter out those vertices without keywords
						nameIdMap.put(x, id1);
						//the format of writing files for debug 
//						nodeWriter.write(x+" "+(id1++)+"\t"+fill(write));
						nodeWriter.write((id1++)+"\t"+fill(write));
						nodeWriter.newLine();	
					}	
			}
			nodeWriter.flush();
			nodeWriter.close();
			
			
			//edge 
			for(String x:nameMap.keySet()){
				if(!nameIdMap.containsKey(x)) continue;
				String string="";
				for(String y:nameMap.get(x)){
					if(nameIdMap.containsKey(y))
						string+=" "+nameIdMap.get(y);
				}
				if(string.length()>2){
					//the format of writing files for debug 
//					bWriter1.write(x+" "+nameIdMap.get(x)+"\t"+string.substring(1, string.length()));
					edgeWriter.write(nameIdMap.get(x)+"\t"+string.substring(1, string.length()));
				}
				else{
					//the format of writing files for debug 
//					bWriter1.write(x+" "+nameIdMap.get(x)+"\t");
					edgeWriter.write(nameIdMap.get(x)+"\t");
				}
				edgeWriter.newLine();
				
			}
			edgeWriter.flush();
			edgeWriter.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//append the content at the end of the file
		
	}
	
	
	//use leaf nodes of p-tree to fill the its p-tree
		private String fill(String write){
			String output="";
			String[] items=write.split(",");
			List<Integer> list=new LinkedList<Integer>();
			for(String item:items) tracePath(Integer.parseInt(item.trim()), list);
			list.add(1);
			Collections.sort(list);
			for(int i:list) output +=i+" ";
			return output;
		}
		// trace one leaf node and find its path to the root
		private void tracePath(int leaf,List<Integer> list){
			PNode node=cpTree.get(leaf);
			while(leaf!=1){
				if(!list.contains(leaf))	list.add(leaf);
				node=node.getFather();
				leaf=node.getId();
			}
		}
	
	
	public static void main(String[] args){
		MeSHPrep meSHPrep=new MeSHPrep("nodeTest.txt","edgeTest.txt","oldCodeMeshTree.txt");
		meSHPrep.domMeSH("medsample1.xml");
		meSHPrep.writeFile();
		
	}
}
