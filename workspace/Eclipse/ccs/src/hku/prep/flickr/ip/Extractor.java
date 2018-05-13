/**
 * 
 */
package hku.prep.flickr.ip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

/**
 * @author fang
 * @date 2012-11-17
 */
public class Extractor {
	protected String outFilePath = "./transfer.html";
	protected String configFileName = "./jtidy.txt";
	protected String errorFileName = "./error.txt";
	protected String foldPath = null;
	protected String htmlFileName = null;
	protected String content = "";
	
	public Extractor(String foldPath, String htmlFileName){
		this.foldPath = foldPath;
		this.htmlFileName = htmlFileName;
	}
	
	//提取DOM树的根节点
	public Node extractDOM(){
		Document document = null;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(foldPath + "//" + htmlFileName));
			OutputStream out = new BufferedOutputStream(new FileOutputStream(outFilePath));
			Tidy tidy = new Tidy();

			tidy.setConfigurationFromFile(configFileName);// JTidy的配置文件必须要的，否则提取文本的时候会出现不可意料的换行
//			tidy.setErrout(new PrintWriter(new FileWriter(errorFileName),true));
			tidy.setForceOutput(true);


//			document = tidy.parseDOM(in, null);
			document = tidy.parseDOM(in, out);
			out.flush();
			out.close();
			in.close();

			NodeList nodeList = document.getChildNodes();// 初始化这个全局变量
			for (int i = 0; i < nodeList.getLength(); i++) {// 删除 DOCTYPE 元素
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.DOCUMENT_TYPE_NODE) {// 查找类型定义节点
					node.getParentNode().removeChild(node);
				}
			}
//			NodeList scriptList = document.getElementsByTagName("script"); // 删掉所有的script结点
//			while (true) {
//				for (int i = 0; i < scriptList.getLength(); i++) {
//					Node node = scriptList.item(i);
//					if (node != null)
//						node.getParentNode().removeChild(node);
//				}
//				nodeList = document.getElementsByTagName("script");
//				if (nodeList == null || nodeList.getLength() == 0)
//					break;
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	//抽取特定目标节点下的全部文字信息
	public String extractText(Node root){
		String text = "";
		if(root != null && root.getChildNodes() != null){
			NodeList nodeList = root.getChildNodes();
			
			for(int i = 0;i < nodeList.getLength();i ++){
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.TEXT_NODE){
					String tmpText = node.getNodeValue();
					if(tmpText != null){
						text += tmpText.trim() + " ";
//						System.out.println("text:" + tmpText);
					}else{
						text += "";
					}
				}else{
					text += extractText(node) + " ";
				}
			}
		}
		
		text = text.trim();
		
		return text;
	}

	//遍历寻找到的目标记录的根节点集合
	public void trave(Node root, List<Node> targetList){

	}
	
	//抽取入口
	public List<Map<String, String>> extract(){
		Node root = extractDOM();
		
		List<Map<String, String>> ideaList = new ArrayList<Map<String, String>>();
		if(root != null){
			List<Node> targetList = new ArrayList<Node>();
			trave(root, targetList);			
			
			for(Node node:targetList){
				Map<String, String> map = extractNode(node);
				if(map != null){
					ideaList.add(map);
				}
			}
		}
		
		return ideaList;
	}
	
	//从记录的根节点中抽取数据
	public Map<String, String> extractNode(Node root){
		return null;
	}

}
