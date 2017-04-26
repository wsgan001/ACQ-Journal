package prep;

import java.awt.geom.Area;
import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import config.Config;

/**
 * 
 * @author chenyankai
 * @date Mar 28, 2017
 */

public class MeSHPrep {
	String xmlFile=null;
	Map<String, String> map=null;//store the MeshTree code key:name value:code
	
	public MeSHPrep(String xmlfile){
		this.xmlFile=Config.localPath+xmlfile;
		this.map=new HashMap<String,String>();
		
	}
	
	private void readMeshTree(String fileName){
		try {
			BufferedReader bfReader=new BufferedReader(new FileReader(Config.localPath+fileName));
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
	
	//extract one tag
	public void domMeSH(String outFile){
		try {
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder=factory.newDocumentBuilder();
			Document doc=builder.parse(new File(xmlFile));
			Element element=doc.getDocumentElement();
			BufferedWriter bfWriter=new BufferedWriter(new FileWriter(outFile));
			Map<String, Set<String>> line=new HashMap<String, Set<String>>();

			NodeList nodes=element.getElementsByTagName("PubmedArticle");
			System.out.println(nodes.getLength()+" 1 ");
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
						if(key!=null) {
							set.add(key);
						}
					}
					}else{//one man has no keywords
						set.add("0");
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
			
			for(String x:line.keySet()){
				String write=line.get(x).toString();
					if(write.length()>2){
					bfWriter.write(x+"\t"+write.substring(1, write.length()-1));
					}else{
						bfWriter.write(x+"\t");
					}
					bfWriter.newLine();
			}
			bfWriter.flush();
			bfWriter.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error!!!");
		}
		
	}
	

	
	
	public static void main(String[] a){
		
		MeSHPrep meSHPrep=new MeSHPrep("medsample1.xml");
		meSHPrep.domMeSH("test.txt");
	}
}
