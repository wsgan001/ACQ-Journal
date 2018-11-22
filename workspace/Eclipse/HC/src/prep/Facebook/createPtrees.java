package prep.Facebook;

import java.io.*;
import java.util.*;

import prep.DBLPPrep.Profile;




public class createPtrees {
	Map<Integer, Set<Integer>> map = null;
	
	public createPtrees(){
		map = new HashMap<Integer,Set<Integer>>();
	}

	
	
	private int singleCreateNodes(String nodeFile){
		int max = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(nodeFile));
			String line = new String();
			while((line=reader.readLine())!=null){
				String[] str = line.split(" ");
				Set<Integer> setHere = new HashSet<Integer>();
				for(int i=1;i<str.length;i++){
					if(Integer.parseInt(str[i])==1) setHere.add(i);
				}
				int vertex = Integer.parseInt(str[0])+1;
				max = vertex;
				if(map.containsKey(vertex)){
					map.get(vertex).addAll(setHere);
				}
				else{
					map.put(vertex, setHere);
				}
			}		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error!");
		}
		return max;
	}
	
	private void singleCreateNodesForEgo(String nodeFile,int vertex){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(nodeFile));
			String line = new String();
			while((line=reader.readLine())!=null){
				String[] str = line.split(" ");
				Set<Integer> setHere = new HashSet<Integer>();
				for(int i=0;i<str.length;i++){
					if(Integer.parseInt(str[i])==1) setHere.add(i);
				}
				vertex = vertex+1;
				if(map.containsKey(vertex)){
					map.get(vertex).addAll(setHere);
				}
				else{
					map.put(vertex, setHere);
				}
			}		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error!");
		}
	}
	
	
	
	private void writeNodes(String outFile,int max){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
			int count = 1;
			while(count!= max){
				String line = new String();
				line = count+"	";
				if(map.containsKey(count)){
					Set<Integer> set = map.get(count);
					List<Integer> list = new ArrayList<Integer>();
					list.addAll(set);
					Collections.sort(list);
					for(int x:list) {
//						x++;
						line += x+" ";
					}
				}
				line = line.substring(0,line.length()-1);
				writer.write(line);
				writer.newLine();
				count++;
			}
			writer.flush();
			writer.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error");
		}
		
	}

	
	public void createNodes(){
		int[] fileList = {0, 107, 348, 414, 686, 698, 1684, 1912, 3437, 3980};
		for(int x:fileList){
			String fileName = "/Users/chenyankai/Downloads/facebook/" + x + ".feat";
			int max = singleCreateNodes(fileName);
			
			fileName = "/Users/chenyankai/Downloads/facebook/" + x + ".egofeat";
			singleCreateNodesForEgo(fileName,x);
			
			String outFile = "/Users/chenyankai/Downloads/facebook/" + x+"items.txt";
			writeNodes(outFile,max);
		}	
	}
	
	public void createPTree(){
		int[] fileList = {0, 107, 348, 414, 686, 698, 1684, 1912, 3437, 3980};
		String CPtreeFile ="/Users/chenyankai/Documents/HKU_research/PCS/dataset/facebook/CPTree.txt";
		for(int x:fileList){
			String nodefile = "/Users/chenyankai/Downloads/facebook/" + x+"items.txt";
			String out = "/Users/chenyankai/Documents/HKU_research/PCS/dataset/facebook/"+x+"node.txt";
			Profile profile = new Profile(nodefile,CPtreeFile);
			profile.run(out,1);
		}
	}
	
	
	
	
	public static void main(String[] args){
		createPtrees create = new createPtrees();
//		String outFile ="/Users/chenyankai/Downloads/items.txt";
//		create.createNodes();
		create.createPTree();
	}
	
	
	
}