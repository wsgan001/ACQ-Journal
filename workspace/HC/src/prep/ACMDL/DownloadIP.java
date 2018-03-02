package prep.ACMDL;

import java.io.*;
import java.net.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import config.Config;

public class DownloadIP {

	String api = "http://tvp.daxiangdaili.com/ip/?tid=558882767541188&num=1&category=2&protocol=https";
	final int ipTopNumbers = 5;
	public DownloadIP(){}
	
	
	public void download(){
		URL url;
		String IPFile = Config.acmccsDataWorkSpace+"ips-"+System.currentTimeMillis()+".txt";
		int count = 0;
		try {
			BufferedWriter std = new BufferedWriter(new FileWriter(IPFile));
			
			while(count < ipTopNumbers){
				url = new URL(api);
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
			    
			    String[] res = new String(data, "UTF-8").split("\n");
			    for(String str:res){
			    	count++;
			    	std.write(str);
			    	std.newLine();
			    }
			    
			 Thread.sleep(1100);
			 
			}
			
			
		    std.flush();
		    std.close();
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public List<String> API(int size){
		List<String> IPPort = new ArrayList<String>(); 
		int count = 0;
		String dynamicAPI = "http://tvp.daxiangdaili.com/ip/?tid=559429124398742&num=1000&category=2&protocol=https";
		try {
			while(count < size){
				URL url = new URL(dynamicAPI);
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
			    
			    String[] res = new String(data, "UTF-8").split("\n");
			    for(String str:res){
			    	count++;
			    	IPPort.add(str);
			    }
			    
			 Thread.sleep(1100); 
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("IP download finished.");
		return IPPort;
	}
	
	
	public static void main(String[] args){
		DownloadIP downloadIP = new DownloadIP();
		downloadIP.download();
	}
	
	
	
}
