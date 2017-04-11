package prep;
import java.io.*;

import org.apache.commons.net.ftp.*;
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
			ftpClient.connect("ftp://ftp.ncbi.nlm.nih.gov/pubmed/baseline/medline17n0001.xml.gz",port);//connect to ftp
//			ftpClient.login("", password);
			if(!FTPReply.isPositiveIntermediate(ftpClient.getReplyCode())){
				System.out.println("can not connect to ftp");
				ftpClient.disconnect();
			}else {
				System.out.println("connection succeeds!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in login");
		}
		return ftpClient;
	}
	
	
	public void downloadFile(String fileName){
		FTPClient ftpClient=null;
		try {
			ftpClient=getFTPClient();
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.changeWorkingDirectory(ftpPath);
			
			 File localFile = new File(localpath + File.separatorChar + fileName);  
	         OutputStream os = new FileOutputStream(localFile);  
	         ftpClient.retrieveFile(fileName, os);  
	         os.flush();
	         os.close();
	         ftpClient.logout();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error");
		}		
	}
	
	public static void main(String []args){
		DownloadFtp downFtp=new DownloadFtp(Config.urlPath,"", "",0, Config.urlPath, Config.localPath);
		downFtp.downloadFile("medline17n0001.xml.gz");
	}
	
}
