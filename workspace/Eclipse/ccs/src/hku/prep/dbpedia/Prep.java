package hku.prep.dbpedia;

import hku.Config;

import java.io.*;
import java.util.*;

/**
 * @author fangyixiang
 * @date Oct 8, 2015
 */
public class Prep {

	public void prep(){
		try{
			int node[][] = new int[8099956][];
			String path = "/home/fangyixiang/Desktop/CCS/dbpedia/nidKeywordsListMapDBpediaVB.txt";
			BufferedReader stdin = new BufferedReader(new FileReader(path));
			
			String line = stdin.readLine();
			while((line = stdin.readLine()) != null){
				String s[]= line.split(":");
				String ss[] = s[1].trim().split(",");
				
				int id = Integer.parseInt(s[0]) + 1; //update node id
				node[id] = new int[ss.length];
				for(int i = 0;i < ss.length;i ++){
					String term = ss[i];
					int word = Integer.parseInt(term) - 8099955; //update keyword id
					node[id][i] = word;
				}
			}
			stdin.close();
			
			BufferedWriter stdout = new BufferedWriter(new FileWriter(Config.dbpediaNode));
			for(int i = 1;i < node.length;i ++){
				stdout.write(i + "\tdbp" + i + "\t");
				if(node[i] != null){
					for(int j = 0;j < node[i].length;j ++){
						stdout.write(node[i][j] + " ");
					}
				}
				stdout.newLine();
			}
			stdout.flush();
			stdout.close();
		}catch(Exception e){e.printStackTrace();}
		
//		try{
//			List<Set<Integer>> list = new ArrayList<Set<Integer>>();
//			list.add(null);
//			for(int i = 1;i <= 8099955;i ++){
//				Set<Integer> set = new HashSet<Integer>();
//				list.add(set);
//			}
//			
//			String path = "/home/fangyixiang/Desktop/CCS/dbpedia/edgeDBpediaVB.txt";
//			BufferedReader stdin = new BufferedReader(new FileReader(path));
//			
//			String line = stdin.readLine();
//			while((line = stdin.readLine()) != null){
//				String s[]= line.split(":");
//				String ss[] = s[1].trim().split(",");
//				
//				int id = Integer.parseInt(s[0]) + 1; //update node id
//				for(String term:ss){
//					int neighbor = Integer.parseInt(term) + 1; //update node id
//					
//					list.get(id).add(neighbor);
//					list.get(neighbor).add(id);
//				}
//			}
//			
//			stdin.close();
//			
//			BufferedWriter stdout = new BufferedWriter(new FileWriter(Config.dbpediaGraph));
//			for(int i = 1;i < list.size();i ++){
//				stdout.write(i + "");
//				Set<Integer> set = list.get(i);
//				Iterator<Integer> iter = set.iterator();
//				while(iter.hasNext()){
//			    	int neighbor = iter.next();
//			    	if(neighbor != i ){//She/He appears in the original file
//			    		String tmp = " " + neighbor;
//			    		stdout.write(tmp);
//			    	}
//			    }
//				stdout.newLine();
//			}
//			stdout.flush();
//			stdout.close();
//		}catch(Exception e){e.printStackTrace();}
	}
	
	public static void main(String[] args) {
		Prep prep = new Prep();
		prep.prep();
	}

}
