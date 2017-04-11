package prep;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import config.Config;

public class DownloadURL {
	String URL=null;
	String localPath=null;
	
	
	public DownloadURL(String url,String local){
		this.URL=url;
		this.localPath=local;
	}
	
	private void downloadFile(String fileName){
		try {
			URL url=new URL(URL+fileName);
			System.out.println(url);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(10*1000);
//			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"); 
			int code=connection.getResponseCode();
			 if (code != HttpURLConnection.HTTP_OK) {
				  throw new Exception("文件读取失败");
			        }
			
			InputStream inStream=connection.getInputStream();
			byte[] data=readInputStream(inStream);
			
			System.out.println(fileName);
			String filePath=localPath+fileName;
			File saveFile=new File(filePath);
			FileOutputStream fos = new FileOutputStream(saveFile);
			fos.write(data);
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
		String fileName="medline17n000"+number+".xml.gz";
		System.out.println(fileName);
		downloadFile(fileName);
	}
	
public static void main(String[]a){
	DownloadURL dUrl=new DownloadURL(Config.urlPath, Config.localPath);
	dUrl.batchDown(1);
}
	
}
