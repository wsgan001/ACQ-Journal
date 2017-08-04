package prep;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.w3c.tidy.AttrCheckImpl.CheckClear;

/**
 * 
 * @author chenyankai
 * @date Mar 28, 2017
 */

public class MeSHPrep {
//	String xmlFile=null;
	String nodeFile=null;
	String edgeFile=null;
	int id1=-1;
//	Map<String, String> map=null;//store the MeshTree code key:name value:old code
	
	Map<String, Integer> oldCodeDFSMap=null;//store the mesh old code and the DFS code
	Map<String, Integer> nameVsDFSMap=null;//Store the name and DFS code
	
	Map<String, Set<Integer>> line = null;//for output the name-attributes
	Map<String, Set<String>> nameMap=null;//for output the name-edges
	Map<String,Integer> nameIdMap=null;
	
	public MeSHPrep(String nodeFile,String edgeFile,String treeFile){
//		this.xmlFile=Config.localPath+xmlfile;
		this.nodeFile=ConfigPubmed.localPath+nodeFile;
		this.edgeFile=ConfigPubmed.localPath+edgeFile;
		
		
		File edgefile=new File(this.edgeFile);
		if(edgefile.exists()) edgefile.delete();
		File nodefile=new File(this.nodeFile);
		if(nodefile.exists()) nodefile.delete();
		
		this.id1=1;
//		this.map=new HashMap<String,String>();
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
	
	public void writeFile(){
		try {
			BufferedWriter bfWriter = new BufferedWriter(new FileWriter(nodeFile,true));
			BufferedWriter bWriter1=new BufferedWriter(new FileWriter(edgeFile,true));//write edge 

			//print out all qualified edges and nodes
			//write into the file (attributes)
			for(String x:line.keySet()){
				String write=line.get(x).toString();	
					if(line.get(x).size()!=0){
						//**********************  Aug 5, 2017 CYK:  use nameIdMap to filter out those vertices without keywords
						nameIdMap.put(x, id1);
						//the format of writing files for debug 
//						bfWriter.write(x+" "+(id1++)+"\t"+write.substring(1, write.length()-1).replace(",", ""));
						bfWriter.write((id1++)+"\t"+write.substring(1, write.length()-1).replace(",", ""));
						bfWriter.newLine();	
					}	
			}
			bfWriter.flush();
			bfWriter.close();
			
			
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
					bWriter1.write(nameIdMap.get(x)+"\t"+string.substring(1, string.length()));
				}
				else{
					//the format of writing files for debug 
//					bWriter1.write(x+" "+nameIdMap.get(x)+"\t");
					bWriter1.write(nameIdMap.get(x)+"\t");
				}
				bWriter1.newLine();
				
			}
			bWriter1.flush();
			bWriter1.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//append the content at the end of the file
		
	}
	
	
	
	public static void main(String[] args){
		MeSHPrep meSHPrep=new MeSHPrep("nodeTest.txt","edgeTest.txt","oldCodeMeshTree.txt");
		meSHPrep.domMeSH("medsample1.xml");

		
	}
}
