package prep.Facebook;

import java.io.*;
import java.util.*;

public class createEdges {
	Map<Integer, List<Integer>> map = null;
	
	public createEdges(){
		this.map = new HashMap<Integer, List<Integer>>();
	}
	
	public void createEdge(String edgeFile){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(edgeFile));
			String string  = "";
			//read file
			while((string=reader.readLine())!=null){
				String[] str = string.split(" ");
				int a1 = Integer.parseInt(str[0])+1;
				int a2 = Integer.parseInt(str[1])+1;
				if(map.containsKey(a1)) {
					map.get(a1).add(a2);
				}
				else{
					List<Integer> list = new ArrayList<Integer>();
					list.add(a2);
					map.put(a1,list);
				}				
			}	
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	private void modify(int end){
		Iterator<Integer> iterator = map.keySet().iterator();
		Map<Integer, List<Integer>> newMap = new HashMap<Integer, List<Integer>>();
		int count =1;
		while(iterator.hasNext()){
			iterator.next();
			System.out.println(count);
			if(count<end){
				List<Integer> edges = map.get(count);
				if(edges!=null){
					List<Integer> newEdges = new ArrayList<Integer>();
					for(int x:edges){
						if(x<end) newEdges.add(x);
					}
				 	newMap.put(count, newEdges);
				} else{
					List<Integer> newEdges = new ArrayList<Integer>();
				 	newMap.put(count, newEdges);

				}
				
			}
			count++;
		}
		System.out.println(map.size()+"    new map: "+newMap.size());
		map = newMap;
		System.out.println(map.size()+"    new map: "+newMap.size());

	}
	
	private void writeEdges(String outFile){
		modify(3663);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
			Iterator<Integer> key = map.keySet().iterator();
			int count = 1;
			while(key.hasNext()){
				int now = key.next();
				
				List<Integer> list = map.get(count);
				String line = count + "	";
				if(list!=null){
					for(int x:list) {
							line +=x+" ";
					}
					line = line.substring(0, line.length()-1);
				}
				writer.write(line);
				writer.newLine();
				count++;
			}
			writer.flush();
			writer.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	

	public void createAllEgdes(){
		int[] fileList = {0, 107, 348, 414, 686, 698, 1684, 1912, 3437};
		for(int x:fileList){
			String fileName = "/Users/chenyankai/Downloads/facebook/" + x + ".edges";
			createEdge(fileName);
		}	
		String outFile = "/Users/chenyankai/Downloads/facebook/edges.txt";

		writeEdges(outFile);
	}
	
	
	
	public static void main(String[] args){
		createEdges method = new createEdges();
//		String edgeFile = "/Users/chenyankai/Downloads/facebook_combined.txt";
//		String outFile ="/Users/chenyankai/Downloads/edges.txt";
//		method.createEdge(edgeFile, outFile);
		method.createAllEgdes();
	}
}