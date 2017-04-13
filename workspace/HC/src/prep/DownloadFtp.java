package prep;
import java.io.*;

import org.apache.commons.net.ftp.*;
import org.omg.PortableInterceptor.SUCCESSFUL;

import config.Config;

public class DownloadFtp {
	private String ftpHost=null;
	private String userName=null;
	private String password=null;
	private int port=-1;
	private String ftpPath=null;
	private String localpath=null;
	
	public DownloadFtp(String host, String usrname, String password, int port, String ftpPath, String localPath){
		this.ftpHost=host;
		this.userName=usrname;
		this.password=password;
		this.port=port;
		this.ftpPath=ftpPath;
		this.localpath=localPath;
	}
	
	
	public FTPClient getFTPClient(){
		FTPClient ftpClient=null;
		try {
			ftpClient=new FTPClient();
			System.out.println(1);
			ftpClient.connect(ftpHost,21);//connect to ftp
			System.out.println(2);
			ftpClient.login(userName, password);
			System.out.println(3);
			
			int replyCode = ftpClient.getReplyCode();
		    if(!FTPReply.isPositiveCompletion(replyCode)){
		    	 System.out.println("failed ");
		      }
		    else{ 
		    	  System.out.println(ftpClient.getReplyString());
		    	  System.out.println("succeed in login");	
		    	  }
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in login");
		}
		return ftpClient;
	}
	
	private String GeneFileName(int i){
		String id="000"+i;
		if(id.length()>4) id=id.substring(id.length()-4, id.length());
		return "medline17n"+id+".xml.gz";
	}
	
	public void downloadFile(String fileName){
		FTPClient ftpClient=null;
		try {
			ftpClient=getFTPClient();
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.changeWorkingDirectory(ftpPath);
		      
			File localFile = new File(localpath + fileName);  
	        OutputStream os = new FileOutputStream(localFile);  
	        ftpClient.retrieveFile(fileName, os);  
	        os.close();
	        ftpClient.logout();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error");
		}		
	}
	
	public static void main(String []args){
		DownloadFtp downFtp=new DownloadFtp(Config.pubMedHost,Config.pubMedUsr,Config.pubMedPswrd, Config.pubMedPort, Config.ftpPath, Config.localPath);
		downFtp.downloadFile("medline17n0001.xml.gz");
		
	}
	
}