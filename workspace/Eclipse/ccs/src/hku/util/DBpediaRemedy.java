package hku.util;

import hku.Config;
import hku.algo.DataReader;

import java.util.*;
import java.io.*;
/**
 * @author fangyixiang
 * @date Oct 19, 2015
 */
public class DBpediaRemedy {

	public static void main(String[] args) {
		Set<String> nameSet = new HashSet<String>();
		try{
			String path = Config.tencentGraph + "-query-20";
			BufferedReader stdin = new BufferedReader(new FileReader(path));
			String line = null;
			while((line = stdin.readLine())!= null){
				String s[] = line.trim().split(" ");
				nameSet.add(s[1]);
			}
			stdin.close();
		}catch(Exception e){e.printStackTrace();}
		
		DataReader dataReader = null;
		dataReader = new DataReader(Config.tencentGraph + "-80", Config.tencentNode + "-80");
		String nodes[][] = dataReader.readNode();
		
		Set<Integer> idSet = new HashSet<Integer>();
		for(int i = 1;i < nodes.length;i ++){
			if(nameSet.contains(nodes[i][0])){
				idSet.add(i);
			}
		}
		System.out.println("idSet.size:" + idSet.size());
	}

}
