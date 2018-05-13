package hku.prep.flickr.ip;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import hku.prep.flickr.*;

/**
 * @author fangyixiang
 * @date 2012-11-8
 * @function 输入URL，下载网页到指定文件
 */
public class IPDownloader {
	private String url;
	private String foldPath;
	private String fileName;
	private int maxCount = -1;

	//构�?方法
	public IPDownloader(String url, String foldPath, String fileName){
		this.url = url;
		this.foldPath = foldPath;
		this.fileName = fileName;
	}
	
	//下载方法
	public int download(){
		File pathFile = new File(foldPath);
		if(pathFile.exists() == false){
			pathFile.mkdirs();
		}
		
		int state = 0;
		//0:下载不成�?
		//1:下载成功
		//2:因为被禁下载失败
		
		int maxCount = 5;
		while(maxCount > 0){
			try {
				URL _url = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
				conn.setConnectTimeout(Cfg.connectTime);
				conn.setReadTimeout(Cfg.connectTime);
				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)");
			
				conn.connect(); //可以在conn建立以后
				
				BufferedReader stdin = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			    
			    System.out.println("download step1...");
			    File outFile = new File(foldPath + "\\" + fileName);
			    FileWriter fw = new FileWriter(outFile); 
			    BufferedWriter stdout = new BufferedWriter(fw);
			    
			    String line = null;
			    int count = 0;
			    while((line = stdin.readLine()) != null){
			    	stdout.write(line);
			    	count = count + line.length();
			    	stdout.newLine();
			    }
			    
			    System.out.println("download step2...");
			  
			    //写尾�?
			    stdout.flush();
			    stdout.close();
			    fw.close();

			    break;
			}catch(Exception e){
				maxCount -- ;
				e.printStackTrace();
				System.out.println("download fails ...");
			}
		}
		return 1;
	}
	
	public static void main(String args[]){
		String url = "https://twitter.com/g";
		String foldPath = "E://twitter2015/";
		String fileName = "fenfen";
		
		IPDownloader downloader = new IPDownloader(url, foldPath, fileName);
		downloader.download();
	}
}
