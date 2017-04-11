package prep;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import config.Config;

/**
 * 
 * @author chenyankai
 * @date Mar 28, 2017
 */

public class MeSHPrep {
	String xmlFile=null;
	
	public MeSHPrep(String xmlfile){
		this.xmlFile=xmlfile;
	}
	
	//extract one tag
	public void domMeSH(){
		try {
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder=factory.newDocumentBuilder();
			Document doc=builder.parse(new File(xmlFile));
			Element element=doc.getDocumentElement();
	
			NodeList nodes=element.getElementsByTagName("PubmedArticle");
			System.out.println(nodes.getLength()+" 1 ");
			for(int i=0;i<nodes.getLength();i++){
				Element Article=(Element) nodes.item(i);
				Element MedlineCitation=(Element) Article.getElementsByTagName("MedlineCitation").item(0);
				Element autherListNode=  (Element) MedlineCitation.getElementsByTagName("AuthorList").item(0);
				if(autherListNode!=null){
					NodeList autherList= autherListNode.getElementsByTagName("Author");
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
							if(name!=null) System.out.println("name "+name);
					}
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	public static void main(String[] a){
		String file=Config.home+"medsample1.xml";
		MeSHPrep meSHPrep=new MeSHPrep(file);
		meSHPrep.domMeSH();
	}
}