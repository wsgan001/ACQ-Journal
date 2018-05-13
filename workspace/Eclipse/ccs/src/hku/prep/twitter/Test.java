package hku.prep.twitter;

import java.io.*;
import java.util.*;

/**
 * @author fangyixiang
 * @date Oct 6, 2015
 * seed: 1,000,000
 * link: 8,804,017
 * tag:  7,021,966
 */
public class Test {

	public static void main(String[] args) {
		try{
//			String path = "/home/fangyixiang/Desktop/CCS/twitter/twitter.seed.users";
//			String path = "/home/fangyixiang/Desktop/CCS/twitter/twitter.following.network";
			String path = "/home/fangyixiang/Desktop/CCS/twitter/twitter.tagging.network";
			BufferedReader stdin = new BufferedReader(new FileReader(path));
			
			int lineNum = 0;
			String line = null;
//			Set<String> set = new HashSet<String>();
			Map<String, Integer> map = new HashMap<String, Integer>();
			while((line = stdin.readLine()) != null){
				String s[] = line.split("\t");
//				set.add(s[0]);
//				set.add(s[1]);
				
				if(map.containsKey(s[1]) == false){
					map.put(s[1], 1);
				}else{
					map.put(s[1], map.get(s[1]) + 1);
				}
				
//				if(map.containsKey(s[0]) == false){
//					map.put(s[0], 1);
//				}else{
//					map.put(s[0], map.get(s[0]) + 1);
//				}
				
				lineNum += 1;
			}
			stdin.close();
//			System.out.println("The # of users is " + set.size());
			
			int count = 0;
			int countAll = 0;
			for(Map.Entry<String, Integer> entry:map.entrySet()){
				if(entry.getValue() >= 3){
					count += 1;
					countAll += entry.getValue();
				}
			}
			
			System.out.println("total tagged users:" + map.size());
			System.out.println("top3:" + count);
			System.out.println("lineNum:" + lineNum);
			System.out.println("avg keyword:" + countAll * 1.0 / count);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
/*
total tagged users:2376506
top3:2376506
lineNum:20240219
 
*/