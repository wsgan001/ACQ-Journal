package hku.prep.twitter.tmp;

import hku.util.STEMExt;
import hku.util.StopFilter;

import java.io.*;
/**
 * @author fangyixiang
 * @date Oct 6, 2015
 */
public class TextView {

	public void handle(){
		STEMExt stem = new STEMExt();
		StopFilter stop = new StopFilter();
		
		String path = "/home/fangyixiang/Desktop/CCS/twitter/twitterDataset/";
		File folder = new File(path);
		File files[] = folder.listFiles();
		
		try{
			for(int i = 0;i < files.length;i ++){
				File file = files[i];
				
				BufferedReader stdin = new BufferedReader(new FileReader(file));
					
				String line = null;
				while((line = stdin.readLine()) != null){
					String s[] = line.split("\t");
						
					System.out.println(line);
				}
				stdin.close();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		TextView filter = new TextView();
		filter.handle();
	}

}
