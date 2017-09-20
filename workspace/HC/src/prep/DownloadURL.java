package prep;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.net.PrintCommandListener;

import config.Config;
import prep.PubMedPrep.ConfigPubmed;
/**
 * 
 * 
 * @author chenyankai
 * @date Apr 26, 2017
 */
public class DownloadURL {
	String URL=null;
	String localPath=null;
	
	
	public DownloadURL(String url,String local){
		this.URL=url;
		this.localPath=local;
	}
	
	private void downloadFile(String fileName){
		try {
			
			
//			URL url=new URL(URL);
			URL url=new URL("http://akka.io");
			System.out.println(url);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(10*1000);
			System.out.println(1);
//			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"); 
			int code=connection.getResponseCode();
			System.out.println(code);
			 if (code != HttpURLConnection.HTTP_OK) {
				  throw new Exception("文件读取失败");
			        }
			 System.out.println(2);
			 
			InputStream inStream=connection.getInputStream();
			byte[] data=readInputStream(inStream);
			System.out.println(3);
			System.out.println(fileName);
			String filePath=localPath+fileName;
			File saveFile=new File(filePath);
			System.out.println(4);
			FileOutputStream fos = new FileOutputStream(saveFile);
			fos.write(data);
			System.out.println(5);
			if(fos!=null){fos.close(); }
			if(inStream!=null) {inStream.close();}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error!");
		}
		
	}
	
	private  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }    
	
	public void batchDown(int number){
//		ftp://ftp.ncbi.nlm.nih.gov/pubmed/baseline/medline17n0001.xml.gz
//		String fileName="medline17n000"+number+".xml.gz";
		String  fileName="medline17n0001.xml.gz";
		System.out.println(fileName);
		downloadFile(fileName);
	}
	
public static void main(String[]a){
	String url="ftp://ftp.ncbi.nlm.nih.gov/pubmed/baseline/medline17n0001.xml.gz";
	DownloadURL dUrl=new DownloadURL(url, Config.pubMedDataWorkSpace);
//	dUrl.batchDown(1);
	dUrl.downloadFile("aa.txt");
}
	
}
