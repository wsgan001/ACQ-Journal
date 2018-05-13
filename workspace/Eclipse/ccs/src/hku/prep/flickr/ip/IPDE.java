/**
 * 
 */
package hku.prep.flickr.ip;

import hku.prep.flickr.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yxfang
 */
public class IPDE {

	public List<Map<String, String>> downExtUpdateIPProxy(){
		List<Map<String, String>> allItemList = new ArrayList<Map<String, String>>();
		for(int i = 0;i < 10;i ++){
//			String url = "http://www.goodips.com/?pageid=" + (i + 1);
//			String fileName = "ip" + i + ".html";
//			System.out.println("url:" + url);
			
			//http://www.xici.net.co/nt/1
			String url = "http://www.xici.net.co/nt/" + (i + 1);			
			String fileName = "ip" + i + ".html";
			System.out.println("url-:" + url);
			
			IPDownloader download = new IPDownloader(url, Cfg.ipPath, fileName);
			int state = download.download();
			
			if(state == 1){
				String htmlFileName = fileName;
//				IPGoodExtractor extractor = new IPGoodExtractor(Config.ipPath, htmlFileName);
				IPXiCiExtractor extractor = new IPXiCiExtractor(Cfg.ipPath, htmlFileName);
				List<Map<String, String>> itemList = extractor.extract();
				
				for(Map<String, String> map:itemList){
					allItemList.add(map);
					
//					System.out.println("IP:" + map.get("ip"));
//					System.out.println("Port:" + map.get("port"));
				}
			}
		}
		
//		System.out.println("allItemList.size:" + allItemList.size());
		
		//更新IP代理
		if(allItemList.size() > 0){
			allItemList.remove(0);//第一行是无效的中文字符
			int size = allItemList.size();
			Cfg.ip = new String[size][2];
			for(int i = 0;i < allItemList.size();i ++){
				Map<String, String> map = allItemList.get(i);
				String ip = map.get("ip");
				String port = map.get("port");
				
				Cfg.ip[i][0] = ip;
				Cfg.ip[i][1] = port;
			}
		}
		return allItemList;
	}
	
	public static void main(String[] args) {
		Cfg.setValue();
		IPDE ipde = new IPDE();
		ipde.downExtUpdateIPProxy();
	}

}
