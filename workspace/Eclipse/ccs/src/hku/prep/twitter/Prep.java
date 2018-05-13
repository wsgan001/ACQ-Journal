package hku.prep.twitter;

import java.util.*;
import java.io.*;

import hku.Config;
import hku.prep.*;
/**
 * @author fangyixiang
 * @date Oct 7, 2015
 * handle the dataset with average degree of 300
 */
public class Prep {
	private Map<String, Integer> map = null;
	private List<PrepUser> userList = null;
	
	public Prep(){
		this.map = new HashMap<String, Integer>();
//		this.userList = new ArrayList<PrepUser>();
	}
	
	//generate the social graph
	public void prep2(){
		//step 1: fill map
		selectUser();
		System.out.println("finish selecting users, map.size=" + map.size());
		
		//step 2:
		List<Set<Integer>> edgeList = new ArrayList<Set<Integer>>();
		edgeList.add(null);//take the space
		for(int i = 1;i <= map.size();i ++){
			Set<Integer> set = new HashSet<Integer>();
			edgeList.add(set);
		}
		
		//step 3: build the social network
		int edge = 0;
		try{
			String path = "/home/fangyixiang/Desktop/CCS/twitter/twitter.following.network";
			BufferedReader stdin = new BufferedReader(new FileReader(path));
			
			String line = null;
			int c = 0;
			while((line = stdin.readLine()) != null){
				String s[] = line.split("\t");
				
				if(map.containsKey(s[0]) && map.containsKey(s[1])){
					int id1 = map.get(s[0]);
					int id2 = map.get(s[1]);
					
					if(id1 > 500000){
						edgeList.get(id1).add(id2);
						edge += 2;
					}
					
					if(id2 > 500000){
						edgeList.get(id2).add(id1);
					}
				}
			}
			stdin.close();
		}catch(Exception e){e.printStackTrace();}
		System.out.println("we have finished reading social networks, edges:" + edge);
		
		try{
			BufferedWriter stdout = new BufferedWriter(new FileWriter(Config.twitterGraph));
//			for(int i = 1;i < edgeList.size();i ++){
//			for(int i = 1;i <= 500000;i ++){
			for(int i = 500001;i < edgeList.size();i ++){
				String line = i + "";
				stdout.write(line);
				
				Set<Integer> set = edgeList.get(i);
				if(i % 100000 == 0)   System.out.println("set.size=" + set.size());
				
				Iterator<Integer> iter = set.iterator();
				while(iter.hasNext()){
			    	int neighbor = iter.next();
			    	if(neighbor != i ){//She/He appears in the original file
			    		String tmp = " " + neighbor;
			    		stdout.write(tmp);
			    	}
			    }
				stdout.newLine();
			}
			stdout.flush();
			stdout.close();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void selectUser(){
		//step 1: read tags
		List<PrepUser> allList = new ArrayList<PrepUser>();
		Map<String, Integer> tmpMap = new HashMap<String, Integer>();//idStr->index
		try{
			String path = "/home/fangyixiang/Desktop/CCS/twitter/twitter.tagging.network";
			BufferedReader stdin = new BufferedReader(new FileReader(path));
			
			String line = null;
			while((line = stdin.readLine()) != null){
				String s[] = line.split("\t");
				String idStr = s[1];//a user who has been tagged
				String tag = s[3].trim();

				if(tmpMap.containsKey(idStr)){
					int idx = tmpMap.get(idStr);
					allList.get(idx).getKeySet().add(tag);
				}else{
					tmpMap.put(idStr, allList.size());
					
					PrepUser user = new PrepUser(idStr, "-");
					user.getKeySet().add(tag);
					allList.add(user);
				}
			}
			stdin.close();
		}catch(Exception e){e.printStackTrace();}
		System.out.println("we have finished reading tags. The # of total users:" + allList.size());
		
		//step 2: select users
		int index = 1;
		for(PrepUser user:allList){
			if(user.getKeySet().size() >= 3){
				String oldIdStr = user.getUserIDStr();
				map.put(oldIdStr, index);
				index += 1;
			}
		}
		allList = null;
		System.out.println("total # of selected users:" + map.size());
	}
	
	//generate node first, due to the memory issue
	public void prep1(){
		//step 1: read tags
		List<PrepUser> allList = new ArrayList<PrepUser>();
		Map<String, Integer> tmpMap = new HashMap<String, Integer>();//idStr->index
		try{
			String path = "/home/fangyixiang/Desktop/CCS/twitter/twitter.tagging.network";
			BufferedReader stdin = new BufferedReader(new FileReader(path));
			
			String line = null;
			while((line = stdin.readLine()) != null){
				String s[] = line.split("\t");
				String idStr = s[1];//a user who has been tagged
				String tag = s[3].trim();

				if(tmpMap.containsKey(idStr)){
					int idx = tmpMap.get(idStr);
					allList.get(idx).getKeySet().add(tag);
				}else{
					tmpMap.put(idStr, allList.size());
					
					PrepUser user = new PrepUser(idStr, "-");
					user.getKeySet().add(tag);
					allList.add(user);
				}
			}
			stdin.close();
		}catch(Exception e){e.printStackTrace();}
		System.out.println("we have finished reading tags. The # of total users:" + allList.size());
		
		//step 2: select users
		int index = 1;
		for(PrepUser user:allList){
			if(user.getKeySet().size() >= 3){
				String oldIdStr = user.getUserIDStr();
				map.put(oldIdStr, index);
				
				user.setUserID(index); //update
				userList.add(user);
				index += 1;
			}
		}
		allList = null;
		System.out.println("total # of selected users:" + userList.size());
		
		int kwLen = 0;
		try{
			BufferedWriter stdout = new BufferedWriter(new FileWriter(Config.twitterNode));
			for(int i = 0;i < userList.size();i ++){
				PrepUser user = userList.get(i);
				String line = user.getUserID() + "\t-\t";
				stdout.write(line);
				
				Iterator<String> iter = user.getKeySet().iterator();
			    while(iter.hasNext()){
			    	String word = iter.next();
			    	line = word + " ";
			    	stdout.write(line);
			    	kwLen += 1;
			    }
			    
			    stdout.newLine();
			}
			stdout.flush();
			stdout.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Average # of keywords:" + (kwLen * 1.0 / userList.size()));
	}

	public static void main(String[] args) {
		Prep prep = new Prep();
		prep.prep2();
	}

}
