package prep;

import java.io.*;
import java.util.zip.GZIPInputStream;

import config.*;

/**
 * 
 * @author chenyankai
 * @date Apr 26, 2017
 */

public class Extract {
	private int pubMedSize=-1;
	
	
	public Extract(int x){
		this.pubMedSize=x;
		
	}
	
	//unzip .gz file 
	private boolean Unzip(String fileName){
		String outName=null;
		try {
			GZIPInputStream gzInput=new GZIPInputStream(new FileInputStream(Config.localPath+fileName));
			
			outName = fileName.substring(0,fileName.length()-3);
			FileOutputStream fout=new FileOutputStream(Config.localPath+outName);
			
			int num;
			byte[] buf=new byte[1024];
			while ((num = gzInput.read(buf,0,buf.length)) != -1)
			{   
			    fout.write(buf,0,num);   
			}
			gzInput.close();   
		    fout.close();   
		    return true;
//		    System.out.println();
	      
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			System.out.println("Error in output step!");
			e.printStackTrace();
			return false;
			
		}	
	}
	
	//delete a file 
	private boolean deleteFile(String fileName){
		File file=new File(Config.localPath+fileName);
		if(file.exists()&&file.isFile()){
			file.delete();
			
//			System.out.println("d");
			return true;
		}else{
//			System.out.println("File does not exist!");
			return false;
		}
	}
	
	
	public void process(){
		DownloadFtp downFtp=new DownloadFtp(Config.pubMedHost,Config.pubMedUsr,Config.pubMedPswrd, Config.pubMedPort, Config.ftpPath, Config.localPath);
		MeSHPrep meSHPrep=null;
		Log log=new Log();
		for(int i=1;i<=pubMedSize;i++){
			String msg=null;
			String name=downFtp.GeneFileName(i);
			boolean flag1=downFtp.downloadFile(name);
			boolean flag2=Unzip(name);
			boolean flag3=deleteFile(name);
			meSHPrep=new MeSHPrep(name.substring(0,name.length()-3),"MeshTree.txt");
			boolean flag4=meSHPrep.domMeSH(i+".txt");
			boolean flag5=deleteFile(name.substring(0,name.length()-3));
			
			msg=downFtp.getFtpState()+"\t"+"download status: "+flag1+"\t"+"unzip state: "+flag2+"\t"+"doMesh state: "+flag4+"\t"
					+"delete gz file and unzip file states: "+flag3+"\t"+flag4;
			log.log(msg);
//				
		}
		
	}
	
	public static void main(String[] a){
		Extract extract=new Extract(2);
		extract.process();
//		extract.deleteFile(fileName);
		
	}
//		
	
}
