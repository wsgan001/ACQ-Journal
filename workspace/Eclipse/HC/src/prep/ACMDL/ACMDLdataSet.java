package prep.ACMDL;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.corba.se.pept.transport.Connection;

import config.Config;
import config.Log;


/*
 * this class is to crawl all the data from acm 
 * Two inner class: (1) DynamicIP.class (); (2).crawler.class(extends Thread).
 */

public class ACMDLdataSet {
	private ConcurrentSkipListSet<String> community = null;
	private ConcurrentHashMap<String, Set<Integer>> usersItemsMap = null;
	Map<Integer, Integer> old2New = null;
	
	String IPFile = null;
	List<String> ipPort = null;
	int threadNum = 10000;
	ExecutorService pool = null;
	boolean isDebug = false;
	String communityFile = Config.acmccsDataWorkSpace+"users.txt";
	String nodesFile = Config.acmccsDataWorkSpace+"items.txt";
	int blockedIpCount = 0;
	int invalidIPCount = 0;
	
	public ACMDLdataSet(String ipFile){
		this.IPFile = Config.acmccsDataWorkSpace+ipFile; 
		this.community = new ConcurrentSkipListSet<String>();
		this.usersItemsMap = new ConcurrentHashMap<String,Set<Integer>>(2000,(float)0.75,1000);
		this.ipPort = new ArrayList<String>();
		pool = Executors.newFixedThreadPool(threadNum);

	} 
	
	//generate the query URL for ACM DL
	private String generateQueryURL(){
		String url = "https://dl.acm.org/author_page.cfm?id=81100";
		Random random = new Random();
		for(int i=0;i<6;i++){
			url+=random.nextInt(10);
		}
		return url;
	}
	
	
	//output the users and items 
	private void output(){
		try {
			BufferedWriter std = new BufferedWriter(new FileWriter(communityFile,true));
			for(Iterator<String>it = community.iterator();it.hasNext();){
				String line = it.next();
				std.write(line.trim());
				std.newLine();
			}
			std.flush();
			std.close();
				
		} catch (Exception e) {
			// TODO: handle exception
		}
			
		try {
			BufferedWriter std = new BufferedWriter(new FileWriter(nodesFile,true));
			for(Iterator<String>it = usersItemsMap.keySet().iterator();it.hasNext();){
				String name = it.next(); 
				std.write(name+"	"+usersItemsMap.get(name).toString().trim());
				std.newLine();
			}
			std.flush();
			std.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
		
	public void loadIP(){
		try {
			BufferedReader std = new BufferedReader(new FileReader(IPFile));
			String line = null;
			while((line=std.readLine())!=null){
				ipPort.add(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void run(){
//		loadIP();
		DownloadIP download = new DownloadIP();
		ipPort = download.API(threadNum);
//		String acmurl = "https://dl.acm.org/author_page.cfm?id=81100010683";
		Iterator<String> iter = ipPort.iterator();
		while(iter.hasNext()){
			String acmurl = generateQueryURL();
			String ipPortStr = iter.next().trim();
			pool.execute(new Crawler(ipPortStr, acmurl));
		}
		pool.shutdown();
	}

	public boolean isTerminated(){
		if(pool.isTerminated()) return true;
		else return false;
	}
		
	
	//class crawer 
	public class Crawler implements Runnable{
		long sleepMs = 500; 
		String ipPort = null;
		int timeOut = 10000;
		String startUrl = null; 
		
		
		public Crawler(String ipport, String url) {
			// TODO Auto-generated constructor stub
			this.ipPort = ipport;
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
			client.getOptions().setAppletEnabled(false);
			client.getOptions().setGeolocationEnabled(false);
			client.getOptions().setRedirectEnabled(true);
			client.getOptions().setUseInsecureSSL(true);// allow connection to the https.
			
			//set the proxy using the dynamic IP and port
			if (ipPort != null) {
				ProxyConfig proxyConfig = new ProxyConfig((ipPort.split(",")[0]).split(":")[0], Integer.parseInt((ipPort.split(",")[0]).split(":")[1]));
				client.getOptions().setProxyConfig(proxyConfig);
			}else {
				//------------------------DEBUG------------------------------
				if(isDebug)  System.out.print("loadHTML ipPort is empty.");
				//----------------------END DEBUG----------------------------
				return "";
			}
			
			try {
				Page page = client.getPage(url);
				if(page.isHtmlPage()){
					html = ((HtmlPage)page).asXml();
				}else{
					//------------------------DEBUG------------------------------
					if(isDebug) System.out.println(url+" current url content format is not html.");
					//----------------------END DEBUG----------------------------
				}
				
				if(html.isEmpty()){
					//------------------------DEBUG------------------------------
					if(isDebug) {
						System.out.println("html file is empty.");
					}
					//----------------------END DEBUG----------------------------
				}
				
				
			} catch (Exception e) {
				if(e.getMessage().contains("failed: Connection refused (Connection refused)")){
//					System.out.println("Connection refused");
					invalidIPCount++;
				}
					
				if(e.getMessage().equals("HTTP error fetching URL")){
					System.out.println("current IP is blocked.");
					blockedIpCount++;
				}
				// TODO: handle exception
				//------------------------DEBUG------------------------------
				if(isDebug){
					if(e.getMessage().equals("HTTP error fetching URL")){
						System.out.println("current ip is blocked!");
						System.out.println(e.toString());
					}
					e.printStackTrace();
				}
				//----------------------END DEBUG----------------------------	
			}
			return html;
		}
		
		private void collectPages(String url){
			//get all page links
			//------------------------DEBUG------------------------------
			if(isDebug) System.out.println(url);
			//----------------------END DEBUG----------------------------
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
			} catch (Exception e){
				// TODO Auto-generated catch block
				//------------------------DEBUG------------------------------
				if(isDebug) System.out.println(e.getMessage());	
				//----------------------END DEBUG----------------------------
			}
				
			while(!queue.isEmpty()){
				String url1 = queue.poll();
				//------------------------DEBUG------------------------------
				if(isDebug) System.out.println("reading single pages: "+url1);
				//----------------------END DEBUG----------------------------
				readSinglePage(url1);
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

				if(html.isEmpty()) {
					//------------------------DEBUG------------------------------
					if(isDebug) System.out.println("html is empty.");
					//----------------------END DEBUG----------------------------
					return;
				}
				
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
				System.out.println("error!");
				e.printStackTrace();
			}	
		}
		
		
		//fill the community
		private void fillCommunity(String[] author){
			String line = new String();
			for(String str:author) 	line+=str+",";
			community.add(line.substring(0,line.length()-1));
			System.out.println("after filling, community size: "+community.size());
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
				aut = aut.trim();
				Set<Integer> set = usersItemsMap.get(aut);
				if(set==null) usersItemsMap.put(aut, items);
				else set.addAll(items);
			}
			
			System.out.println("after obtiaining the data,  size: "+usersItemsMap.size());
		}	

	}

	
//	//class dynamicIP
//	public class DynamicIP implements Runnable{
//		long sleepMs = 1000;
//		int maxTime = 5;
//		String api = null;
//		int timeout;
//		ExecutorService pool = null;
//		
//		public DynamicIP(long sleepms,int maxTime,String api,int timeout,int executorPoolSize){
//			// TODO Auto-generated constructor stub
//			this.sleepMs = sleepms;
//			this.api = api;
//			this.maxTime = maxTime;
//			this.timeout = timeout;
//			this.pool = Executors.newFixedThreadPool(executorPoolSize);
//		}
//		
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			int time = 1;
//			
//			String IPFile = Config.acmccsDataWorkSpace+"ips.txt";
//			BufferedWriter std = null;
//			try {
//				std = new BufferedWriter(new FileWriter(IPFile));
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//
//			
//			
//			
//			
//			while(time <= crawlerTimes){
//				System.out.println("now times: "+time+" all time: "+crawlerTimes);
//				try {
//					URL url = new URL(api);
//					
//					HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//				    connection.setConnectTimeout(3000);
//				    connection = (HttpURLConnection)url.openConnection();
//				    	
//				    InputStream raw = connection.getInputStream();  
//				    InputStream in = new BufferedInputStream(raw);  
//				    byte[] data = new byte[in.available()];
//				    int bytesRead = 0;  
//				    int offset = 0;  
//				    while(offset < data.length) {  
//				         bytesRead = in.read(data, offset, data.length - offset);  
//				         if(bytesRead == -1) {  
//				        	 break;  
//				           }  
//				            offset += bytesRead;  
//				     }  
//				    in.close();  
//				    raw.close();
//				    String[] res = new String(data, "UTF-8").split("\n");
//				    if(res.length==0) {
////				    	time++;
//					    System.out.println(">>>>>>>>>>>>>>IP retrieval failed.");
//				    }
//				    else{
//				    	time ++;
//				    	System.out.println(">>>>>>>>>>>>>>current volumn of retrieved IP: " + res.length);
//				    }
//					
//				    // run the crawler thread
//				    for (String ip : res){
//				    	//start to crawler the data
//				    	String acmURL = generateQueryURL();
//				    	System.out.println(ip);
//				    	
//						std.write(ip.trim());
//						std.newLine();
//	
//							
//						
//				    	pool.execute(new Crawler(sleepMs, ip, acmURL));
//				    	
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//					System.err.println(">>>>>>>>>>>>>>Error: " + e.getMessage());
//				}
//				try {
//					Thread.sleep(sleepMs);
//				} catch (InterruptedException e) {
//					System.out.println("????????");
//					e.printStackTrace();
//				}	
//			}	
//			
//			pool.shutdown();
//			try {
//				std.flush();
//				std.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
//	//check if the executor is terminated	
//	public boolean isTerminated(){
//		if(pool.isTerminated()) return true;
//		else return false;
//	}		
//}

	
	
	
	public static void main(String[] args) throws Exception {
			
		// request delay time, the default timeout is 5000ms
//		int timeOut = 10000;
//		int maxAttempt = 5;
//		boolean over = false;
//		int crawlerTimes = 10;
//		int executorSize = 100;
//		String api= "http://tvp.daxiangdaili.com/ip/?tid=559668919475897&num=100&delay=5&category=2&protocol=https";
//		String acmurl = "https://dl.acm.org/author_page.cfm?id=81100010683";
//		String acmurl = "https://dl.acm.org/author_page.cfm?id=81100403088";
		
//		Crawler crawler = acmdLdataSet.new Crawler("95.182.97.70:53281", acmurl);
//		Thread thread1=new Thread(crawler);
//		thread1.start();
//		DynamicIP dynamicIP = acmdLdataSet.new DynamicIP(sleepMs,maxAttempt, api, timeOut,executorSize);
//		new Thread(dynamicIP).start();
	
//		while(!over){
//			try {
//				Thread.sleep(2000);
//				System.out.println(thread1.getState());
//				if(thread1.getState().toString().equals("TERMINATED")){
//					acmdLdataSet.output();
//					break;
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
		
		int count = 0;
		Log log = new Log();
		while(count<20){
			log.log("looper: "+(count+1)+"\n");
			count++;
			ACMDLdataSet acmdLdataSet = new ACMDLdataSet("ips-1510028062514.txt");
			acmdLdataSet.run();
			while(true){
				Thread.sleep(10000);
				System.out.println("isterminated: "+acmdLdataSet.isTerminated());
				if(acmdLdataSet.isTerminated()){
					acmdLdataSet.output();
					break;
					
				}
			}
			System.out.println("blocked IP count: "+acmdLdataSet.blockedIpCount);
			System.out.println("invalid IP count: "+acmdLdataSet.invalidIPCount);	

		}
	
	}
	
	
	
	
	
	
}