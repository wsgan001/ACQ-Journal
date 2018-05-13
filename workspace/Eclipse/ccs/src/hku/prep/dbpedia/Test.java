package hku.prep.dbpedia;

import java.io.*;
import java.util.*;

public class Test {

	public static void main(String[] args) {
		try{
			String path = "/home/fangyixiang/Desktop/CCS/dbpedia/nidKeywordsListMapDBpediaVB.txt";
			BufferedReader stdin = new BufferedReader(new FileReader(path));
			
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;
			String line = stdin.readLine();
			boolean flag[] = new boolean[100000000];
			while((line = stdin.readLine()) != null){
				String s[]= line.split(":");
				String ss[] = s[1].trim().split(",");
				
				for(String term:ss){
					int index = Integer.parseInt(term);
					if(index > max)   max = index;
					if(index < min)   min = index;
				}
			}
			stdin.close();
			System.out.println("min=" + min + " max=" + max);
		}catch(Exception e){e.printStackTrace();}
		
//		try{
//			String path = "/home/fangyixiang/Desktop/CCS/dbpedia/edgeDBpediaVB.txt";
//			BufferedReader stdin = new BufferedReader(new FileReader(path));
//			
//			String line = stdin.readLine();
//			boolean flag[] = new boolean[100000000];
//			while((line = stdin.readLine()) != null){
//				String s[]= line.split(":");
//				int index = Integer.parseInt(s[0]);
//				flag[index] = true;
//				
//				String ss[] = s[1].trim().split(",");
//				for(String term:ss){
//					index = Integer.parseInt(term);
//					flag[index] = true;
//				}
//			}
//			stdin.close();
//			
//			for(int i= 0;i < flag.length;i ++){
//				if(flag[i] == false){
//					System.out.println("node " + i + " is missed.");
//					break;
//				}
//			}
//		}catch(Exception e){e.printStackTrace();}
	}

}
/*
statistic about the original datasets
nodes: 0 ~ 8,099,954
words: 8,099,955 ~ 11,026,980
*/