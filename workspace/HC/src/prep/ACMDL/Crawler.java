package prep.ACMDL;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.*;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class Crawler extends Thread{
	long sleepMs = 500; 
	String ipPort = null;
	int timeOut = 5000;
	ConcurrentLinkedQueue<String> communitiesMap = null;
	Map<String, Set<Integer>> usersItemsMap = null;
	Map<Integer, Integer> old2New = null;
	String startUrl = null; 
	
	
	public Crawler(long sleepMs, int timeOut, String ipport,ConcurrentLinkedQueue<String> communitiesMap,Map<String, Set<Integer>> usersItemsMap,String url) {
		// TODO Auto-generated constructor stub
		this.sleepMs = sleepMs;
		this.timeOut = timeOut;
		this.ipPort = ipport;
		this.communitiesMap = communitiesMap;
		this.usersItemsMap = usersItemsMap;
		this.startUrl = url;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		collectPages(startUrl);
	}
	
	
	//use proxy to open one url connection and store the content as the string 
	//enclosure the connection operation in this function 
	private String loadHTML(String url){
		String html = "";
		BrowserVersion[] versions = { BrowserVersion.CHROME, BrowserVersion.FIREFOX_45, BrowserVersion.INTERNET_EXPLORER, BrowserVersion.BEST_SUPPORTED};
		WebClient client = new WebClient(versions[(int)(versions.length * Math.random())]);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setJavaScriptEnabled(false);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.getOptions().setTimeout(timeOut);
		client.getOptions().setAppletEnabled(true);
		client.getOptions().setGeolocationEnabled(true);
		client.getOptions().setRedirectEnabled(true);
		client.getOptions().setUseInsecureSSL(true);// allow connection to the https.
		
		//set the proxy using the dynamic IP and port
		if (ipPort != null) {
			ProxyConfig proxyConfig = new ProxyConfig((ipPort.split(",")[0]).split(":")[0], Integer.parseInt((ipPort.split(",")[0]).split(":")[1]));
			client.getOptions().setProxyConfig(proxyConfig);
		}else {
			System.out.print("loadHTML ipPort is empty.");
			return "";
		}
		
		try {
			Page page = client.getPage(url);
			if(page.isHtmlPage()){
				html = ((HtmlPage)page).asXml();
			}else{
				System.out.println(url+" current url content format is not html.");
			}
			
			if(html.isEmpty()){
				System.out.println("html file is empty.");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			if(e.getMessage().equals("HTTP error fetching URL")){
				System.out.println("current ip is blocked!");
				System.out.println(e.toString());
			}
			e.printStackTrace();
		}
		return html;
	}
	
	private void collectPages(String url){
		//get all page links
		System.out.println(url);
		Queue<String> queue = new LinkedList<String>();
		try {
			String html = loadHTML(url);
			if(html.isEmpty()) return;
			Document doc = Jsoup.parse(html);
				
			queue.add(url);
			Element element= doc.getElementById("pagelogic");
				
			if(element!=null){
				Elements links = element.getElementsByTag("a");
				for(Element link:links){
					queue.add("https://dl.acm.org/"+link.attr("href"));
				}
			}
		} catch (Exception e) {
				// TODO Auto-generated catch block
			System.out.println(e.getMessage());
				
//			e.printStackTrace();
		}
			
		while(!queue.isEmpty()){
			readSinglePage(queue.poll());
			try {
				Thread.sleep(sleepMs);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	
	private void readSinglePage(String url){
		try {
			String html = loadHTML(url);
			if(html.isEmpty()) return;
			Document doc = Jsoup.parse(html);
			Elements elements = doc.getElementsByClass("details");
			for(Element element:elements) {
				Elements links = element.getElementsByTag("a");
				String href = links.attr("href");
				Elements authors = element.getElementsByClass("authors");
				for(Element authorElement:authors ){
					String authorString = authorElement.text();
					String[] author = authorString.split(",");
					
					for(String str:author) str = str.trim();
					
					fillCommunity(author);
					
					obtainData("https://dl.acm.org/"+href,author);
					
					Thread.sleep(sleepMs);
				}
			
			} 
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
	}
	
	
	//fill the community
	private void fillCommunity(String[] author){
		String line = new String();
		for(String str:author) 	line+=str+",";
		communitiesMap.add(line.substring(0,line.length()-1));
	}
	
	
	//get the detailed ACM CCS items 
	private void obtainData(String url,String[] author){
		Set<Integer> items = new HashSet<Integer>();
		try{
			String html = loadHTML(url);
			if(html.isEmpty()) return;
			Document doc = Jsoup.parse(html);
			
			String content = doc.toString().trim();
			//the regular expression of the CCS temple. It match the text in the format of [{v: .... }]
			String temple = "(\\[\\{v:.+\\])"; 
			Pattern pattern = Pattern.compile(temple);
			Matcher matcher = pattern.matcher(content) ;
			while(matcher.find()){
				String string = matcher.group();
				Pattern pattern2 = Pattern.compile("('\\d+')");// match 'numbers'
				Matcher matcher2 = pattern2.matcher(string) ;
				while(matcher2.find()){
					String num = matcher2.group();
					items.add(Integer.parseInt(num.substring(1, num.length()-1)));
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//fill all items for each users 
		for(String aut:author){
			Set<Integer> set = usersItemsMap.get(aut);
			if(set==null) usersItemsMap.put(aut, items);
			else set.addAll(items);
		}
	}	
	
	
	

}
