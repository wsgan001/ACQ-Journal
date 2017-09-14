package config;

import java.io.*;
import java.util.Date;

import prep.ConfigPubmed;


/**
 * 
 * @author chenyankai
 * @date Apr 27, 2017
 */

public class Log {
	private String logFile=Config.logFileName;
	
	public Log(){
		
	} 
	
	public void log(String msg){
		try {
			BufferedWriter bWriter=new BufferedWriter(new FileWriter(logFile,true));//continue to write instead of overwriting 
			Date date=new Date();
			bWriter.write(date.toString());
			bWriter.write("\t");
			bWriter.write(msg);
			bWriter.newLine();
			
			bWriter.flush();
			bWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Log log=new Log();
		log.log("test");
	}
	
	
}

