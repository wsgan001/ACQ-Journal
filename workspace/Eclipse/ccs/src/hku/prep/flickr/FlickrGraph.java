package hku.prep.flickr;

import java.io.*;
import java.util.*;

/**
 * @author fangyixiang
 * @date Oct 30, 2015
 */
public class FlickrGraph {

	public void handle(){
		Map<String, Integer> map = readId();
		try{
			String path = "/home/fangyixiang/Desktop/CCS/flickr/flickr-edges";
            BufferedReader stdin = new BufferedReader(new FileReader(path));
            
            List<Set<Integer>> edgeList = new ArrayList<Set<Integer>>();
            for(int i = 0;i < map.size();i ++){
            	Set<Integer> set = new HashSet<Integer>();
            	edgeList.add(set);
            }
            
            String line = null;
            while((line = stdin.readLine()) != null){
            	String s[] = line.trim().split(" ");
            	String userId = s[0];
            	int newUserId = map.get(userId);
            	for(int i = 1;i < s.length;i ++){
            		String neighborId = s[i];
            		if(map.containsKey(neighborId)){
            			int newNeighborId = map.get(neighborId);
                		
                		edgeList.get(newUserId).add(newNeighborId);
                		edgeList.get(newNeighborId).add(newUserId);
            		}
            	}
            }
            stdin.close();
            
            
            int count0 = 0;
            int edgeCount = 0;
            String outPath = "/home/fangyixiang/Desktop/CCS/flickr/flickr-graph";
            BufferedWriter stdout = new BufferedWriter(new FileWriter(outPath));
            for(int i = 0;i < edgeList.size();i ++){
            	stdout.write((i + 1) + ""); //id + 1
            	Set<Integer> set = edgeList.get(i);
            	for(int tmpId:set){
            		stdout.write(" " + (tmpId + 1)); //id + 1
            		edgeCount += 1;
            	}
            	if(set.size() == 0) count0 += 1;
            	stdout.newLine();
            }
            stdout.flush();
            stdout.close();
            
            System.out.println("Flickr dataset, nodes:" + map.size() + " edges:" + edgeCount + " count0:" + count0);
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
	
	public static void main(String[] args) {
		FlickrGraph fg = new FlickrGraph();
		fg.handle();
	}

}
//Flickr dataset, nodes:581099 edges:9944548 count0206109
