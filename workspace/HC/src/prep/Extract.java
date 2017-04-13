package prep;

import java.io.IOException;

import config.*;

public class Extract {
	public static int pubMedSize=10;
	
	
	public void process(){
		DownloadFtp downFtp=new DownloadFtp(Config.pubMedHost,Config.pubMedUsr,Config.pubMedPswrd, Config.pubMedPort, Config.ftpPath, Config.localPath);
		for(int i=1;i<=pubMedSize;i++){
			String name=downFtp.GeneFileName(i);
//			downFtp.downloadFile(name);
			String command="gzip -d "+Config.localPath+name;
			String delete="rm -f "+Config.localPath+name.substring(0,name.length()-3);
//			/Users/chenyankai/GitDefault/workspace/HC/file/medline17n0001.xml.gz";
			try {
//				Runtime.getRuntime().exec(command);
//				process
				Runtime.getRuntime().exec(delete);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		
	}
	
	
	public static void main(String[] a){
		Extract extract=new Extract();
		extract.process();
		
	}
//		
	
}
