package prep.ACMDL;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.*;


public class DynamicIP implements Runnable{
	long sleepMs = 1000;
	int maxTime = 5;
	String order = null;
	int timeout;
	
	public DynamicIP(long sleepms,int maxTime,String orger,int timeout){
		// TODO Auto-generated constructor stub
		this.sleepMs = sleepms;
		this.maxTime = maxTime;
		this.order = orger;
		this.timeout = timeout;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int time = 1;
		while(time <= maxTime){
			try {
				URL url = new URL("ttp://api.ip.data5u.com/dynamic/get.html?order=" + order + "&ttl&random=true");
				
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			    connection.setConnectTimeout(3000);
			    connection = (HttpURLConnection)url.openConnection();
			    	
			    InputStream raw = connection.getInputStream();  
			    InputStream in = new BufferedInputStream(raw);  
			    byte[] data = new byte[in.available()];
			    int bytesRead = 0;  
			    int offset = 0;  
			    while(offset < data.length) {  
			         bytesRead = in.read(data, offset, data.length - offset);  
			         if(bytesRead == -1) {  
			        	 break;  
			           }  
			            offset += bytesRead;  
			     }  
			    in.close();  
			    raw.close();
			    String[] res = new String(data, "UTF-8").split("\n");
			    if(res.length==0) {
			    	time++;
				    System.out.println(">>>>>>>>>>>>>>IP retrieval failed.");
			    }
			    else{
			    	time = maxTime+1;
			    	System.out.println(">>>>>>>>>>>>>>current volumn of retrieved IP: " + res.length);
			    }
				
			    // run the crawler thread
			    for (String ip : res){
//			    	start to crawler the data
//					new Crawler(100, targetUrl, useJs, timeOut, ip, referer, https, outputHeaderInfo).start();
				}
			
				
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println(">>>>>>>>>>>>>>Error: " + e.getMessage());
			}
			try {
				Thread.sleep(sleepMs);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}	
	}
	
}
