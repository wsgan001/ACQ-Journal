package prep.ACMDL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import algorithm.CPTreeReader;
import algorithm.ProfiledTree.PNode;
import config.Config;

public class ExtractRawFile {
	Map<String, Set<Integer>> usersItemsMap = null;
	Map<Integer,String> usersMap = null;
	String userItemFile = null;
	String usersFile = null;
	Map<Integer, Integer> old2New = null; 
	
	
	
public ExtractRawFile(String itemsFile,String usersFile){
	this.usersItemsMap = new HashMap<String,Set<Integer>>();
	this.usersMap = new HashMap<Integer,String>();
	this.old2New = new HashMap<Integer,Integer>();
	this.userItemFile = itemsFile;
	this.usersFile = usersFile;	
	
}


public void run(){
	readFile();
	outputadjacncyMatrix();
	
}

private void reCode(){
	String ItemNameFile = Config.acmccsDataWorkSpace+"itemName.txt";
	try {
		BufferedReader std = new BufferedReader(new FileReader(ItemNameFile));
		String line = null;
		while((line=std.readLine())!=null){
			String[] str = line.split(";");
			int old = Integer.parseInt(str[0]);
			int newId = Integer.parseInt(str[1]);
//			System.out.println(old+"  "+newId);
			old2New.put(old, newId);
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
}
	
private void readFile(){
	reCode();
	String communityFile = usersFile;
	try {
		BufferedReader std = new BufferedReader(new FileReader(communityFile));
		String line = null;
		int lineNum = 1;
		while((line=std.readLine())!=null){
			usersMap.put(lineNum++,line);
		}
		std.close();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	System.out.println(old2New.size());
	
	String nodesFile = userItemFile;
	try {
		BufferedReader std = new BufferedReader(new FileReader(nodesFile));
		String line = null;
		int count=0;
		while((line=std.readLine())!=null){
			String[] s = line.trim().split("	");
			String name = s[0];
//			if(name.equals("J. Gray")||name.equals("J. N. Gray")||name.equals("James N. Gray")||name.equals("James Gray")) name="Jim Gray";
			Set<Integer> set = new HashSet<Integer>();
			count++;
			if(s.length==1) continue;
			if(s[1].length()==2){
				set.add(1);
			}
			else{
				String items = s[1].substring(1, s[1].length()-1);
				String[] numItems = items.split(",");
				for(String x:numItems) {
					int old = Integer.parseInt(x.trim());
					if(old==0) old=1;
					
					if(old2New.containsKey(old)){
						int newId= old2New.get(old);
						set.add(newId);
					}
				}	
			}
			
			if(usersItemsMap.containsKey(name)){
				usersItemsMap.get(name).addAll(set);
			}
			else 
				usersItemsMap.put(name, set);
		}
		std.close();
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	
	CPTreeReader reader = new CPTreeReader(Config.acmccsDataWorkSpace+"CPTree.txt");
	Map<Integer, PNode> cptree = reader.loadCPTree();
	for(Iterator<String> iter=usersItemsMap.keySet().iterator();iter.hasNext();){
		String name = iter.next();
		Set<Integer> set = usersItemsMap.get(name);
		Set<Integer> completePtrees = reader.completeCPTree(set,cptree);
		usersItemsMap.put(name, completePtrees);
		
	}
	
	
	
}




private void outputadjacncyMatrix(){
	Set<String> names = usersItemsMap.keySet();
	int count=1;
	Map<String, Integer> idMap = new HashMap<String,Integer>();
	Map<Integer,String> nameMap = new HashMap<Integer,String>();
	for(String name:names) {
		idMap.put(name, count);
		nameMap.put(count++, name);
	}
	Map<Integer,Set<Integer>> edges = new HashMap<Integer,Set<Integer>>();
	for(Iterator<Integer>it = usersMap.keySet().iterator();it.hasNext();){
		int cur = it.next();
		String[] author = usersMap.get(cur).split(",");
		for(String name:author){
			name = name.trim();
//			if(name.equals("J. Gray")||name.equals("J. N. Gray")||name.equals("James N. Gray")||name.equals("James Gray")) name="Jim Gray";
			if(!idMap.containsKey(name)) continue;
			int id = idMap.get(name);
			Set<Integer> set = new HashSet<Integer>();
			for(String other:author){
				other = other.trim();
//				if(other.equals("J. Gray")||other.equals("J. N. Gray")||other.equals("James N. Gray")||other.equals("James Gray")) other="Jim Gray";
				if(!idMap.containsKey(other)) continue;
				int num = idMap.get(other);
				if(num!=id) set.add(num);
			}
			if(edges.containsKey(id)){
				edges.get(id).addAll(set);
			}else{
				edges.put(id, set);
			}			
		}
	}
	
	//write the edges 
	String communityFile = Config.acmccsDataWorkSpace+"edges.txt";
	try {
		BufferedWriter std = new BufferedWriter(new FileWriter(communityFile));
		int lineNum = 1;
		while(lineNum<=edges.size()+1){
			String str = new String();
			if(!edges.containsKey(lineNum)) {
				System.out.println("not contains: "+lineNum);
				std.write(lineNum+"\t");
				std.newLine();
				lineNum++;
				continue;
			}
			for(int x:edges.get(lineNum)) str += x+" ";
			if(str.length()==0) {
//				System.out.println(edges.containsKey(lineNum));
//				System.out.println("line: "+lineNum+edges.get(lineNum).toString()+" Name: "+nameMap.get(lineNum));
				std.write(lineNum+"	");
				std.newLine();
				lineNum++;
			}else{
				std.write(lineNum+"	"+str.substring(0,str.length()-1));
				std.newLine();
				lineNum++;
			}
			
		}
		std.flush();
		std.close();		
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	
	//write the nodes
	String nodesFile = Config.acmccsDataWorkSpace+"nodes.txt";
	try {
		BufferedWriter std = new BufferedWriter(new FileWriter(nodesFile));
		int lineNum = 1;
		while(lineNum <= idMap.size()){
			String line = lineNum+"	";
			String name = nameMap.get(lineNum);
			List<Integer> list = new ArrayList<Integer>(usersItemsMap.get(name));
			Collections.sort(list);
			for(int x:list){
				if(x==0) line+=1+" ";
				else line+=x+" ";
			}
			
			std.write(line.substring(0, line.length()-1).trim());
			std.newLine();
			lineNum++;
		}
		
		std.flush();
		std.close();
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
}
	
public static void main(String[] args){
	ExtractRawFile instance = new ExtractRawFile(Config.acmccsDataWorkSpace+"items.txt", Config.acmccsDataWorkSpace+"users.txt");
	instance.run();
}
	
	
}
