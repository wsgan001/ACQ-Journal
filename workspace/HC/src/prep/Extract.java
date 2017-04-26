package prep;

import java.io.*;
import config.*;

/**
 * 
 * @author chenyankai
 * @date Apr 26, 2017
 */

public class Extract {
	public static int pubMedSize=10;
	
	private void Upzip(String fileName){
		
	}
	
	
	
	public void process(){
		DownloadFtp downFtp=new DownloadFtp(Config.pubMedHost,Config.pubMedUsr,Config.pubMedPswrd, Config.pubMedPort, Config.ftpPath, Config.localPath);
		MeSHPrep meSHPrep=null;
		for(int i=1;i<=pubMedSize;i++){
			String name=downFtp.GeneFileName(i);
//			downFtp.downloadFile(name);
			
			String command="gzip -d "+Config.localPath+name;
			String delete="rm -f "+Config.localPath+name.substring(0,name.length()-3);
//			/Users/chenyankai/GitDefault/workspace/HC/file/medline17n0001.xml.gz";
			try {
				System.out.println(command);
				Runtime.getRuntime().exec(command);
//				meSHPrep=new MeSHPrep(name);
//				meSHPrep.domMeSH(i+".txt");
				Runtime.getRuntime().exec(delete);
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("error in processing");
			}	
		}
		
	}
	
	public static void main(String[] a){
		Extract extract=new Extract();
		extract.process();
		
	}
//		
	
}
