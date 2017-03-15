package hku.prep.flickr;

import hku.Config;
import hku.util.STEMExt;
import hku.util.StopFilter;

import java.io.*;
import java.util.*;

/**
 * @author fangyixiang
 * @date Oct 30, 2015
 */
public class FlickrKeyword {

	public void handle(){
		STEMExt stem = new STEMExt();
		StopFilter stop = new StopFilter();
		Map<String, Integer> map = readId();
		
		try{
		    List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
            for(int i = 0;i < map.size();i ++){
            	Map<String, Integer> tmpMap = new HashMap<String, Integer>();
            	list.add(tmpMap);
            }
            
            for(int fileNum = 0;fileNum < 10;fileNum ++){
            	String path = "/home/fangyixiang/Desktop/CCS/flickr/yfcc100m_dataset-" + fileNum;
                BufferedReader stdin = new BufferedReader(new FileReader(path));
                System.out.println(path);
                
                String line = null;
                while((line = stdin.readLine()) != null){
                	String s[] = line.trim().split("\t");
                	if(s.length > 8){
                		String userId = s[1];
                		int index = map.get(userId);
                		Map<String, Integer> kwMap = list.get(index);
                		
                		String tagPart = s[8];
                		String tags[] = tagPart.split(",");
                		
                		for(String tag:tags){
//                			System.out.println(tag);
                			String words[] = tag.trim().split("\\+");
                			for(String word:words){
                				word = word.toLowerCase();
            					word = filerSpec(word); //filter special symbols
            					word = stem.extSTEM(word); //extract its stem
            					
            					if(word != null && word.length() > 0){
            						//only consider words that are stems and not stop words
            						if(stop.contains(word) == false){
            							if(kwMap.containsKey(word)){
            								int freq = kwMap.get(word);
            								kwMap.put(word, freq + 1);
            							}else{
            								kwMap.put(word, 1);
            							}
            						}
            					}
                			}
                		}
                	}
                }
                stdin.close();
            }
            
            int count0 = 0;
            int countAll = 0;
            String outPath = "/home/fangyixiang/Desktop/CCS/flickr/flickr-node";
            BufferedWriter stdout = new BufferedWriter(new FileWriter(outPath));
            for(int i = 0;i < list.size();i ++){
            	stdout.write((i + 1) + "\t" + "flickr" + (i + 1) + "\t");
            	Map<String, Integer> kwMap = list.get(i);
            	if(kwMap.size() == 0)   count0 += 1;
            	
            	int num = 0;
//            	while(num < kwMap.size() && num < Config.topKw){
            	while(num < kwMap.size() && num < 30){//20 is a little bit small
            		int max = -1;
    		    	String word = "";
    		    	for(Map.Entry<String, Integer> entry:kwMap.entrySet()){
    		    		if(entry.getValue() > max){
    		    			max = entry.getValue();
    		    			word = entry.getKey();
    		    		}
    		    	}
    		    	
    		    	num += 1;
    		    	kwMap.remove(word);
    		    	stdout.write(" " + word);
            	}
            	countAll += num;
            	stdout.newLine();
            }
            stdout.flush();
            stdout.close();
            
            System.out.println("Flickr dataset, average keywords:" + countAll * 1.0 / map.size() 
            		+ " count0:" + count0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private Map<String, Integer> readId(){
		Map<String, Integer> map = new HashMap<String, Integer>();
        try{
            String path = "/home/fangyixiang/Desktop/CCS/flickr/flickr-nodes";
            BufferedReader stdin = new BufferedReader(new FileReader(path));
            
            String line = null;
            while((line = stdin.readLine()) != null){
            	String s[] = line.trim().split(" ");
            	int nodeId = Integer.parseInt(s[0]);
            	String userId = s[1];
                map.put(userId, nodeId);
            }
            stdin.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return map;
    }
	
	//filter special characters
	private String filerSpec(String word){
		String rs = "";
		for(int i = 0;i < word.length();i ++){
			char c = word.charAt(i);
			if(c >= 'a' && c <= 'z'){
				rs += c;
			}
			if(c >= 0 && c <= 9){
				rs += c;
			}
		}
		return rs;
	}
	
	public static void main(String[] args) {
		FlickrKeyword fg = new FlickrKeyword();
		fg.handle();
	}

}
//Flickr dataset, average keywords:7.509605075899287 count0:165562
