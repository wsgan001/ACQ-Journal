/**
 * 
 */
package hku.prep.flickr.ip;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author fang
 * @date 2012-11-20
 */
public class IPXiCiExtractor extends Extractor {
	
	public IPXiCiExtractor(String foldPath, String htmlFileName){
		super(foldPath, htmlFileName);
	}

	//遍历寻找到的目标记录的根节点集合
	public void trave(Node root, List<Node> targetList){
		NodeList nodeList = root.getChildNodes();
		for(int i = 0;i < nodeList.getLength();i ++){
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {// 标签的识别留下了TagNode，
				String nodeName = node.getNodeName();
				
				if(nodeName.equalsIgnoreCase("tr")){
					NamedNodeMap map = node.getAttributes();
					if(map.getNamedItem("class") != null
							&& (map.getNamedItem("class").getNodeValue().equals("odd")
									|| map.getNamedItem("class").getNodeValue().equals(""))){
						targetList.add(node);
					}else{
						trave(node, targetList);
					}
				}else{
					trave(node, targetList);
				}
			}
		}
	}
	
	//从记录的根节点中抽取数据
	public Map<String, String> extractNode(Node root){
		Map<String, String> itemMap = new HashMap<String, String>();
		
		NodeList nodeList = root.getChildNodes();
		Node ipNode = nodeList.item(2);
		Node portNode = nodeList.item(3);
		
		String tmp = extractText(root);
//		System.out.println(tmp);
		
		if(ipNode != null && portNode != null){
			String ip = extractText(ipNode);
			String port = extractText(portNode);
			
//			System.out.println("IP:" + ip);
//			System.out.println("Port:" + port);
			
			if(ip != null && ip.indexOf('.') > 0){
				System.out.println("{\"" + ip + "\", \"" + port +"\"},");
				itemMap.put("ip", ip);
				itemMap.put("port", port);
			}
			
		}
		return itemMap;
	}
}
