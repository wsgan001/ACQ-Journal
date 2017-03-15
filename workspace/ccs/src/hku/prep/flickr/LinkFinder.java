package hku.prep.flickr;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import hku.prep.flickr.ip.*;
/**
 * @author yxfang
 *
 * @date 2015-10-6
 */
public class LinkFinder {
	private Map<String, Integer>  map = new HashMap<String, Integer>();
	private String outFileName = "./info/flickr/flickr-edge";
	private List<String> friendList = null;
	private boolean pageFlag = false;
	
	public LinkFinder(){
		outFileName += System.currentTimeMillis();
	}
	
	public void find(int startIdx){
		//step 1: read users' identifiers
		List<String> idList = readId();
		
		//step 2: initialize a queue of ip proxy
		Queue<Map<String, String>> queue = new LinkedList<Map<String, String>>();  
		
		//step 3: start to download
		for(int i = 0;i < idList.size();i ++){
			if(i < startIdx){
				continue;
			}else{
				this.pageFlag = true;
				this.friendList = new ArrayList<String>();
				String userId = idList.get(i);
				for(int j = 1;;j ++){
					int pageId = j;
					boolean rs = download(userId, pageId);
					
//					try{
//						Thread.sleep(100);
//					}catch(Exception e){e.printStackTrace();}
					
					if(!pageFlag || !rs){
						break;
					}
				}
				
				System.out.print("index:" + i);
				System.out.print(" userId:" + userId);
				System.out.println(" friendCount:" + friendList.size());
				
				save(userId, friendList);
			}
		}
	}

	private List<String> readId(){
        List<String> list = new ArrayList<String>();
        try{
            String path = "./info/flickr/flickr-nodes";
            BufferedReader stdin = new BufferedReader(new FileReader(path));
            
            String line = null;
            while((line = stdin.readLine()) != null){
            	String s[] = line.trim().split(" ");
                list.add(s[1]);
            }
            stdin.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
	
	private boolean verfiyOne(){
		int okay = 0;
		try {
			for(int i = 0;i < 5;i ++){
				URL _url = new URL("https://twitter.com/");
				HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setReadTimeout(3000);
//				conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
				conn.connect(); //可以在conn建立以后
				
			    BufferedReader stdin = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			    
			    int count = 0;
			    String line = null;
			    while((line = stdin.readLine()) != null){
			    	count = count + line.length();
			    }
			    
			    System.out.println("count:" + count);
			    if(count > 0)   okay ++;
			    
			    stdin.close();
			}

		    if(okay > 3){
		    	return true;
		    }else {
		    	return false;
		    }
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean download(String userId, int pageId){
		String url = "https://www.flickr.com/people/" + userId + "/contacts/?filter=&page=" + pageId;
//		System.out.println(url);
		
		int failCount = 0;
		while(failCount < 3){
			try {
				URL _url = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
				conn.setConnectTimeout(Cfg.connectTime);
				conn.setReadTimeout(Cfg.connectTime);
//				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
			
				conn.connect(); //可以在conn建立以后
				BufferedReader stdin = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			    
				int friendCount = 0;
			    String line = null;
			    while((line = stdin.readLine()) != null){
			    	if(line.length() >= 50){
			    		if(line.contains("<td class=\"Icon\"><a href=\"/photos/")){
			    			int atIndex = line.indexOf('@');
			    			String s1 = line.substring(0, atIndex);
			    			int start = 0;
			    			for(int k = s1.length() - 1;k >=0;k --){
			    				if(s1.charAt(k) < '0' || s1.charAt(k) > '9'){
			    					start = k + 1;
			    					break;
			    				}
			    			}
			    			String part1 = s1.substring(start);
			    			String part2 = line.substring(atIndex, atIndex + 4);
			    			String friendId = part1 + part2;
			    			friendCount += 1;
			    			
//			    			System.out.println(friendId);
			    			this.friendList.add(friendId);
			    		}
			    	}
			    }
			    
			    if(friendCount == 0)   this.pageFlag = false;
			    
			    stdin.close();
			    return true;
			}catch(Exception e){
				failCount += 1;
				e.printStackTrace();
				System.out.println("download fails ...");
			}
		}
		return false;
	}

	
	private void save(String userId, List<String> friendList){
		try{
			BufferedWriter stdout = new BufferedWriter(new FileWriter(outFileName, true));
			stdout.write(userId);
			for(String friendId:friendList){
				stdout.write(" " + friendId);
			}
			stdout.newLine();
			stdout.flush();
			stdout.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		LinkFinder finder = new LinkFinder();
//		finder.find(0);
//		finder.find(159);
//		finder.find(238);
		finder.find(50000);
	}

}
