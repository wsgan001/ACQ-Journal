package prep;

import java.io.*;
import java.util.zip.GZIPInputStream;

import javax.management.loading.PrivateClassLoader;

import config.*;

/**
 * 
 * @author chenyankai
 * @date Apr 26, 2017
 */

public class Extract {
	private int pubMedSize=-1;
	private String nodeFile=null;
	private String edgeFile=null;
	
	
	
	public Extract(int x,String nodeFile,String edgeNode){
		this.pubMedSize=x;
		this.nodeFile=nodeFile;
		this.edgeFile=edgeNode;
		
	}
	
	//unzip .gz file 
	private boolean Unzip(String fileName){
		String outName=null;
		try {
			GZIPInputStream gzInput=new GZIPInputStream(new FileInputStream(ConfigPubmed.localPath+fileName));
			
			outName = fileName.substring(0,fileName.length()-3);
			FileOutputStream fout=new FileOutputStream(ConfigPubmed.localPath+outName);
			
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
		File file=new File(ConfigPubmed.localPath+fileName);
		if(file.exists()&&file.isFile()){
			return file.delete();
		}else{
			return false;
		}
	}
	
	
	public void process(){
		DownloadFtp downFtp=new DownloadFtp(ConfigPubmed.pubMedHost,ConfigPubmed.pubMedUsr,ConfigPubmed.pubMedPswrd, ConfigPubmed.pubMedPort, ConfigPubmed.ftpPath, ConfigPubmed.localPath);
		MeSHPrep meSHPrep=new MeSHPrep(nodeFile,edgeFile,"oldCodeMeshTree.txt");

		Log log=new Log();
		
		for(int i=1;i<=pubMedSize;i++){
			String msg=null;
			String name=downFtp.GeneFileName(i);
			boolean flag1=downFtp.downloadFile(name);
			boolean flag2=Unzip(name);
			boolean flag3=deleteFile(name);
			boolean flag4=meSHPrep.domMeSH(name.substring(0,name.length()-3));
			deleteFile(name.substring(0,name.length()-3));
			System.out.println(meSHPrep.getnameMapSize());
			msg=downFtp.getFtpState()+"\t"+"download status: "+flag1+"\t"+"unzip state: "+flag2+"\t"+"doMesh state: "+flag4+"\t"
					+"delete gz file and unzip file states: "+flag3+"\t"+flag4;
			log.log(msg);		
		}
		meSHPrep.writeFile();
//		meSHPrep.writeAllFile();
	}
	
	
	public void testRandomAccess(String file,int targetLine){
		int currentLine=0;
		String line=null;
		try {
			RandomAccessFile rAccessFile=new RandomAccessFile(file, "rw");
			while((line=rAccessFile.readLine())!=null){
			if(currentLine==targetLine){
				break;
			}
			currentLine++;
			}
			rAccessFile.writeBytes("testetetetettetete   ");
			rAccessFile.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("file not found!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Io exception!");
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void main(String[] a){
		Extract extract=new Extract(30,"node.txt","edge.txt");
		extract.process();
//		extract.testRandomAccess(Config.localPath+"test.txt",1 );
		
	}
//		
	
}
