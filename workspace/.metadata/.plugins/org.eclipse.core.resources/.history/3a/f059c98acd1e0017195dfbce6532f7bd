package prep;
import org.apache.commons.net.ftp.*;

public class DownFtp {
	private String ftpHost=null;
	private String userName=null;
	private String password=null;
	private int port=-1;
	private String ftpPath=null;
	private String localpath=null;
	
	public DownFtp(String host, String usrname, String password, int port, String ftpPath,String localPath, String localPath){
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
			ftpClient.connect(ftpHost,port);//connect to ftp
			ftpClient.login(userName, password);
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
	
	
	public static void downloadFile(String fileNname){
		
	}
	
}
