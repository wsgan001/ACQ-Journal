package prep.PubMedPrep;
import java.io.*;

import org.apache.commons.net.ftp.*;
import org.omg.PortableInterceptor.SUCCESSFUL;
/**
 * 
 * @author chenyankai
 * @date Apr 26, 2017
 */

public class DownloadFtp {
	private String ftpHost=null;
	private String userName=null;
	private String password=null;
	private int port=-1;
	private String ftpPath=null;
	private String localpath=null;
	private String ftpState=null;
	
	public DownloadFtp(String host, String usrname, String password, int port, String ftpPath, String localPath){
		this.ftpHost=host;
		this.userName=usrname;
		this.password=password;
		this.port=port;
		this.ftpPath=ftpPath;
		this.localpath=localPath;
	}
	
	public String getFtpState(){return this.ftpState;}
	
	private FTPClient getFTPClient(){
		FTPClient ftpClient=null;
		try {
			ftpClient=new FTPClient();
//			System.out.println(1);
			ftpClient.connect(ftpHost,21);//connect to ftp
//			System.out.println(2);
			ftpClient.login(userName, password);
//			System.out.println(3);
			
			int replyCode = ftpClient.getReplyCode();
		    if(!FTPReply.isPositiveCompletion(replyCode)){
//		    	 System.out.println("failed ");
		    	ftpState="ftpreply failed.";
		      }
		    else{ 
//		    	  System.out.println(ftpClient.getReplyString());
//		    	  System.out.println("succeed in login");	
		    	  ftpState="succeed in login____________"+ftpClient.getReplyString();
		    	  }
			
		} catch (Exception e) {
			// TODO: handle exception
//			System.out.println("error in login");
			ftpState="error in login.";
		}
		return ftpClient;
	}
	
	public String GeneFileName(int i){
		String id="000"+i;
		if(id.length()>4) id=id.substring(id.length()-4, id.length());
		return "medline17n"+id+".xml.gz";
	}
	
	public boolean downloadFile(String fileName){
		FTPClient ftpClient=null;
		ftpState=fileName+"	state:	"+ftpState;
		try {
			ftpClient=getFTPClient();
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.changeWorkingDirectory(ftpPath);
		      
			File localFile = new File(localpath + fileName);  
	        OutputStream os = new FileOutputStream(localFile);  
	        boolean flag=ftpClient.retrieveFile(fileName, os);  
	        os.close();
	        ftpClient.logout();
			return flag;
		} catch (Exception e) {
			// TODO: handle exception
//			System.out.println("error");
			return false;
		}		
	}
	
	
	
	
	
//	public static void main(String []args){
//		DownloadFtp downFtp=new DownloadFtp(Config.pubMedHost,Config.pubMedUsr,Config.pubMedPswrd, Config.pubMedPort, Config.ftpPath, Config.localPath);
//		
//		
//	}
	
}
