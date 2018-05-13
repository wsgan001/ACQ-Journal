package hku.prep.twitter;

import java.util.*;
import java.io.*;

import hku.Config;
import hku.prep.*;
/**
 * This class is used only for counting the number of nodes and edges
 * @author fangyixiang
 * @date Oct 7, 2015
 * # of nodes:1058389
 * # of edges:161842645
 * degree is:152.91414120895058
 */
public class PrepTest {
	private Map<String, Integer> map = null;
	private List<PrepUser> userList = null;
	
	public PrepTest(){
		this.map = new HashMap<String, Integer>();
	}
	
	//generate the social graph
	public void prep2(){
		//step 1: fill map
		selectUser();
		
		//step 3: build the social network
		int edge = 0;
		try{
			String path = "/home/fangyixiang/Desktop/CCS/twitter/twitter.following.network";
			BufferedReader stdin = new BufferedReader(new FileReader(path));
			
			String line = null;
			while((line = stdin.readLine()) != null){
				String s[] = line.split("\t");
				
				if(map.containsKey(s[0]) && map.containsKey(s[1])){
					edge += 1;
				}
			}
			stdin.close();
		}catch(Exception e){e.printStackTrace();}
		
		System.out.println("# of nodes:" + map.size());
		System.out.println("# of edges:" + edge);
		System.out.println("degree is:" + (edge * 1.0 / map.size()));
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

	public static void main(String[] args) {
		PrepTest prep = new PrepTest();
		prep.prep2();
	}

}
