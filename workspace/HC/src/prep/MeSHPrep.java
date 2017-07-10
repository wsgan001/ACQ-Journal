package prep;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

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
	Map<String, String> map=null;//store the MeshTree code key:name value:code
	
	
	public MeSHPrep(String nodeFile,String edgeFile,String treeFile){
//		this.xmlFile=Config.localPath+xmlfile;
		this.nodeFile=ConfigPubmed.localPath+nodeFile;
		this.edgeFile=ConfigPubmed.localPath+edgeFile;
		this.id1=1;
		this.map=new HashMap<String,String>();
		readMeshTree(treeFile);
	}
	
	
	private void readMeshTree(String fileName){
		try {
			BufferedReader bfReader=new BufferedReader(new FileReader(ConfigPubmed.localPath+fileName));
			if(fileName=="mtrees2017.txt"){
				String line=null;
				while((line=bfReader.readLine())!=null){
					String[] s=line.split(";");
					map.put(s[0], s[1]);
				}
			}else{
				String line=null;
				while((line=bfReader.readLine())!=null){
					String[] s=line.split("\t");
					map.put(s[0], s[2]);
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
			BufferedWriter bfWriter=new BufferedWriter(new FileWriter(nodeFile,true));//append the content at the end of the file

			Map<String, Set<String>> line = new HashMap<String, Set<String>>();//for output the name-attributes
			Map<String, Set<String>> nameMap=new HashMap<String,Set<String>>();//for output the name-edges
			
			NodeList nodes=element.getElementsByTagName("PubmedArticle");
			System.out.println("number of citatios: "+nodes.getLength());
			for(int i=0;i<nodes.getLength();i++){
				Element Article=(Element) nodes.item(i);
				Element MedlineCitation=(Element) Article.getElementsByTagName("MedlineCitation").item(0);
				Element authorListNode=  (Element) MedlineCitation.getElementsByTagName("AuthorList").item(0);
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
								}else if(tag.equals("Initials")){
									name=node.getFirstChild().getNodeValue()+","+name;
								}
							}		
							if(name!=null) {
								nameList.add(name);
								Set<String> set=new HashSet<String>();
								line.put(name,set);			
							}
					}
				}
				
				//find all the meshCode with mesh
				if(flag==true){
					Set<String> set=new HashSet<String>();	
					if(MeshKeyListNode!=null){ //if one man has at least one keywords
						NodeList meshList=MeshKeyListNode.getElementsByTagName("DescriptorName");					
						for(int k=0;k<meshList.getLength();k++){
							Element mesh=(Element) meshList.item(k);
							String key=null;
							String meshValue=mesh.getFirstChild().getNodeValue();
							if(map.containsKey(meshValue)){
								key = map.get(meshValue);
							}
							if(key!=null){
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
						//create edges 
						createEdge(nameMap, nameList);
						
					}
					//if one man has no keyword.We do not record it.
					else{
						for(String x:nameList){
							line.remove(x);
							nameMap.remove(x);
						}
					}	
				}
			
			}
			Map<String,Integer> nameIdMap=new HashMap<String,Integer>();
			//write into the file (attributes)
			
			for(String x:line.keySet()){
				String write=line.get(x).toString();	
					nameIdMap.put(x, id1);
//					System.out.println(id1+" "+write.substring(1, write.length()-1));
//					bfWriter.write((id1++)+"\t"+x+"\t"+write.substring(1, write.length()-1).replace(",", ""));
					bfWriter.write((id1++)+"\t"+write.substring(1, write.length()-1).replace(",", ""));
					bfWriter.newLine();	
			}
			bfWriter.flush();
			bfWriter.close();
			
			//edge 
//			BufferedWriter bWriter1=new BufferedWriter(new FileWriter(Config.localPath+"edge.txt",true));
			BufferedWriter bWriter1=new BufferedWriter(new FileWriter(edgeFile,true));

			for(String x:nameMap.keySet()){
				String string="";
				for(String y:nameMap.get(x)){
					string+=" "+nameIdMap.get(y);
				}
				if(string.length()>2){
//					bWriter1.write(x+"\t"+nameIdMap.get(x)+"\t"+string.substring(1, string.length()));
					bWriter1.write(nameIdMap.get(x)+"\t"+string.substring(1, string.length()));
				}else{
//					bWriter1.write(x+"\t"+nameIdMap.get(x)+"\t");
					bWriter1.write(nameIdMap.get(x)+"\t");
				}
				bWriter1.newLine();
				
			}
			bWriter1.flush();
			bWriter1.close();
			
//			//test name map
//			BufferedWriter b1=new BufferedWriter(new FileWriter(Config.localPath+"name.txt"));
//			for(String x:nameMap.keySet()){
//				b1.write("id "+nameMap.get(x)+"\t"+x);
//				b1.newLine();
//			}
//			b1.flush();
//			b1.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			state=false;
			System.out.println("error!!!");
		}
		return state;
	}
	
	private void createEdge(Map<String, Set<String>> map, ArrayList<String> set){
		for(String x:set){
			if(map.containsKey(x)){
				for(String y:set){
					if(x!=y){map.get(x).add(y);}
				}
			}else{
				Set<String> tmp=new HashSet<String>();
				for(String y:set){
					if(x!=y){tmp.add(y);}
				}
				map.put(x, tmp);
			}
		}
		
	}
	
	
	public static void main(String[] args){
		MeSHPrep meSHPrep=new MeSHPrep("nodeTest.txt","edgeTest.txt","MeshTree.txt");
		meSHPrep.domMeSH("medsample1.xml");
//		meSHPrep.domMeSH("medline17n0001.xml");
//		String string=" abc";
//		System.out.println(string.substring(1,string.length()));
		
	}
}
