package hku.prep.flickr;

import java.io.*;
import java.util.*;
/**
 * @author fangyixiang
 * @date Oct 5, 2015
 */
public class Test {
	public static Map<String, Integer> map = new HashMap<String, Integer>();
	
	public static void handle(){
		String path0 = "/home/fangyixiang/Desktop/CCS/flickr/yfcc100m_dataset-0";
		String path1 = "/home/fangyixiang/Desktop/CCS/flickr/yfcc100m_dataset-1";
		String path2 = "/home/fangyixiang/Desktop/CCS/flickr/yfcc100m_dataset-2";
		String path3 = "/home/fangyixiang/Desktop/CCS/flickr/yfcc100m_dataset-3";
		String path4 = "/home/fangyixiang/Desktop/CCS/flickr/yfcc100m_dataset-4";
		String path5 = "/home/fangyixiang/Desktop/CCS/flickr/yfcc100m_dataset-5";
		String path6 = "/home/fangyixiang/Desktop/CCS/flickr/yfcc100m_dataset-6";
		String path7 = "/home/fangyixiang/Desktop/CCS/flickr/yfcc100m_dataset-7";
		String path8 = "/home/fangyixiang/Desktop/CCS/flickr/yfcc100m_dataset-8";
		String path9 = "/home/fangyixiang/Desktop/CCS/flickr/yfcc100m_dataset-9";
		
		single(path0);
		single(path1);
		single(path2);
		single(path3);
		single(path4);
		single(path5);
		single(path6);
		single(path7);
		single(path8);
		single(path9);
		
		System.out.println("map.size:" + map.size());
		output();
	}

	public static void single(String path){
		System.out.println(path);
		try{
			BufferedReader stdin = new BufferedReader(new FileReader(path));
			
			int count = 0;
			String line = null;
			
			while((line = stdin.readLine()) != null){
				count += 1;
//				System.out.println(line);
				
				String s[] = line.split("\t");
				String userId = s[1];
				if(map.containsKey(userId)){
					map.put(userId, map.get(userId) + 1);
				}else{
					map.put(userId, 1);
				}
			}
			stdin.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void output(){
		try{
			String file = "/home/fangyixiang/Desktop/CCS/flickr/flickr-nodes";
			BufferedWriter stdout = new BufferedWriter(new FileWriter(file));
			int index = 0;
			for(Map.Entry<String, Integer> entry:map.entrySet()){
				stdout.write(index +" " + entry.getKey());
				stdout.newLine();
				index += 1;
			}
			stdout.flush();
			stdout.close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	public static void main(String[] args) {
		Test.handle();
	}

}
