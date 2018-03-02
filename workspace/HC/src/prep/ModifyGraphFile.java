package prep;

import java.util.*;
import java.io.*;

import config.Config;

public class ModifyGraphFile {
	
	public void modifyGraph(String inFile,String outFile){
		List<String> newFile = new ArrayList<String>();
		try {
			BufferedReader stdIn = new BufferedReader(new FileReader(inFile));
			String line = null;
			while((line=stdIn.readLine())!=null){
				
				String newLine = line.replaceFirst(" ", "\t");
				newFile.add(newLine);
			}
			stdIn.close();
			
			BufferedWriter stdOut = new BufferedWriter(new FileWriter(outFile));
			for(String x:newFile){
				stdOut.write(x);
				stdOut.newLine();
			}
			stdOut.flush();
			stdOut.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public void modifyNodes(String inFile,String outFile){
		List<String> newFile = new ArrayList<String>();
		try {
			BufferedReader stdIn = new BufferedReader(new FileReader(inFile));
			String line = null;
			while((line=stdIn.readLine())!=null){
				
				String[] newLine = line.split("\t");
				if(newLine.length>=2) newFile.add(line);
				else{
					String str= newLine[0]+"\t"+"1";
					newFile.add(str);
				}
			}
			stdIn.close();
			
			BufferedWriter stdOut = new BufferedWriter(new FileWriter(outFile));
			for(String x:newFile){
				stdOut.write(x);
				stdOut.newLine();
			}
			stdOut.flush();
			stdOut.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void modifyFlickrDBLPNodes(String inFile,String outFile){
		List<String> newFile = new ArrayList<String>();
		try {
			BufferedReader stdIn = new BufferedReader(new FileReader(inFile));
			String line = null;
			while((line=stdIn.readLine())!=null){
				String[] newLine = line.split("\t");
				if(newLine.length>=3){
					String shortLine = newLine[0]+"\t"+newLine[2].trim();
					newFile.add(shortLine);
				}else{
					String shortLine = newLine[0]+"\t";
					newFile.add(shortLine);
				}
			
			}
			
			stdIn.close();
			
			BufferedWriter stdOut = new BufferedWriter(new FileWriter(outFile));
			for(String x:newFile){
				stdOut.write(x);
				stdOut.newLine();
			}
			stdOut.flush();
			stdOut.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		ModifyGraphFile instance = new ModifyGraphFile();
//		instance.modifyGraph(Config.FlickrGraph, Config.FlickrGraph);
//		instance.modifyGraph(Config.dblpGraph, Config.dblpGraph);
		instance.modifyFlickrDBLPNodes("/home/fangyixiang/Desktop/HC/dataspace/Flickr/flickr-node.txt", "/home/fangyixiang/Desktop/HC/dataspace/Flickr/flickr-node.txt");
		instance.modifyFlickrDBLPNodes("/home/fangyixiang/Desktop/HC/dataspace/DBLP/dblp-node", "/home/fangyixiang/Desktop/HC/dataspace/DBLP/dblp-node");
	}

	
	
	
}
