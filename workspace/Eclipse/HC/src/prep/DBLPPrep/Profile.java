package prep.DBLPPrep;

import java.io.*;
import java.util.*;



import algorithm.CPTreeReader;
import algorithm.ProfiledTree.PNode;

public class Profile {
	
	boolean isdebug = false;
	
	String itemFile = "/Users/chenyankai/Documents/HKU_research/PCS/dataset/facebook/itemName.txt";
	String cpTreeFile = null;
	String[] ccsList = null;
	int CPTreeItemSize = 1908;
	final double D = 0.0000001;
	final int ccsItemSize = 1908;
	Map<Integer,Set<Integer>> map = null;
	Map<String, Set<Integer>> buffer = null;
	String nodeFile = null;
	
	
	
	public Profile(String nodefile,String cptreeFile){
		this.ccsList = new String[ccsItemSize+1];// size of ACM ccs items; we leave the 0 as empty
		this.map = new HashMap<Integer,Set<Integer>>();
		this.nodeFile = nodefile;
		this.buffer = new HashMap<String, Set<Integer>>();
		this.cpTreeFile = cptreeFile;
	}
	
	private void loadCCS(){
		try {
			BufferedReader stdIn = new BufferedReader(new java.io.FileReader(itemFile));
			String line = null;
			while((line=stdIn.readLine())!=null){
				String[] str = line.split(";");
				int index =Integer.parseInt(str[1]);
				System.out.println(index+"   now ");
				ccsList[index] = str[2];
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	private boolean bigger(double a,double b){
		if((a-b) > D) return true;
		else return false;
	}
	
	//1 is the closest     
	private Set<Integer> TopK1Closest(double[] ranks,int topK,double lowBound){
		Set<Integer> indexSet = new HashSet<Integer>();
		while(indexSet.size()!=topK){
			double max = 0;
			int index = 1;
			for(int i=1;i<ranks.length;i++){
				double cur = ranks[i];
				if(cur >= max && !indexSet.contains(i)) {
					index = i;
					max = cur;	
				}
			}
			if(lowBound==0) if(max<lowBound) break;
			indexSet.add(index);
		}	
		return indexSet;
	}
	
	//for 0 is the closest
	private Set<Integer> TopK0Closest(double[] ranks,int topK){
		Set<Integer> indexSet = new HashSet<Integer>();
		while(indexSet.size()!=topK){
			double min = 10000000;
			int index = 1;
			for(int i=1;i<ranks.length;i++){
				double cur = ranks[i];
				if(cur <= min && !indexSet.contains(i)) {
					index = i;
					min = cur;	
				}
			}
			indexSet.add(index);
		}	
		return indexSet;
	}
	
	//topk means return top k keywords in each distance function; 
	//overlapSize constrains the keywords return by this function should show at least overlapSize times
	public Set<Integer> computeDistance(String str,int topk,int overlapSize,int returnSize){
		
		Distance distance = new Distance();

		//edit distance
		double[] editDisRanks = new double[ccsList.length];
		double lowBound = 0.05;
		for(int index = 1;index<ccsList.length;index++){
			
			String ccsItem = ccsList[index];
			if(ccsItem==null) System.out.println("ccsItem null here index: "+ index);
			if(str==null) System.out.println("str null");
			editDisRanks[index]= distance.editDis(str, ccsItem);
		}
		Set<Integer> set1=TopK1Closest(editDisRanks, topk,lowBound);
		//------------------------DEBUG------------------------------
		if(isdebug){
			for(int x:set1) System.out.println(ccsList[x]+" "+editDisRanks[x]);
			System.out.println();
		}
		//----------------------END DEBUG----------------------------
		

		
		//cosine distance 
		double[] cosineDisRanks = new double[ccsList.length];
		for(int index = 1;index<ccsList.length;index++){
			String ccsItem = ccsList[index];
			cosineDisRanks[index]= distance.cosineDis(str, ccsItem);
		}
		Set<Integer> set2=TopK1Closest(cosineDisRanks,topk,lowBound);
		//------------------------DEBUG------------------------------
		if(isdebug){
			for(int x:set2) System.out.println(ccsList[x]+" "+cosineDisRanks[x]);
			System.out.println();
		}
		//----------------------END DEBUG----------------------------
		
		
		//euclidean distance
		double[] euclideanDisRanks = new double[ccsList.length];
		for(int index = 1;index<ccsList.length;index++){
			String ccsItem = ccsList[index];
			euclideanDisRanks[index]= distance.EuclideanDis(str, ccsItem);
		}
		Set<Integer> set3=TopK0Closest(euclideanDisRanks,topk);
		//------------------------DEBUG------------------------------
		if(isdebug){
			for(int x:set3) System.out.println(ccsList[x]+" "+euclideanDisRanks[x]);
			System.out.println();
		}
		//----------------------END DEBUG----------------------------
		
		Map<Integer, Integer> countMap = new HashMap<Integer,Integer>();
		
		for(int x:set1){
			if(!countMap.containsKey(x)) countMap.put(x, 1);
			else countMap.put(x, (countMap.get(x)+1));
		}
		for(int x:set2){
			if(!countMap.containsKey(x)) countMap.put(x, 1);
			else countMap.put(x, (countMap.get(x)+1));
		}
		for(int x:set3){
			if(!countMap.containsKey(x)) countMap.put(x, 1);
			else countMap.put(x, (countMap.get(x)+1));
		}
		
		Set<Integer> resultIndex = new HashSet<Integer>();
		for(int x:countMap.keySet()){
			if(countMap.get(x)>=overlapSize) resultIndex.add(x);
		}
		//------------------------DEBUG------------------------------
		if(isdebug){
			for(int x:resultIndex) System.out.println(ccsList[x]);
		}
		//----------------------END DEBUG----------------------------
//		for(int x:resultIndex) System.out.println(ccsList[x]);
		Set<Integer> returnSet = new HashSet<Integer>(returnSize);
		if(resultIndex.size()>returnSize){
			Random random = new Random();
			while(returnSet.size()!=returnSize){
				int index = random.nextInt(resultIndex.size());
				int count=0;
				for(Iterator<Integer> iter = resultIndex.iterator();iter.hasNext();){
					int x=iter.next();
					if(count==index){
						returnSet.add(x);
						break;
					}
					count++;
				}
			}
		}else{
			while(returnSet.size()!=returnSize){
				Random random = new Random();
				returnSet.add(random.nextInt(CPTreeItemSize-1)+1);
			}
		}
	return returnSet;
	}
	
	
	private void reloadKeywords(int topk,int overlapSize,int returnSize){
		CPTreeReader reader = new CPTreeReader(cpTreeFile);
		Map<Integer, PNode> cptree = reader.loadCPTree();
		Map<String, Integer> wordCount = wordCount();
		try {
			BufferedReader std = new BufferedReader(new FileReader(nodeFile));
			String line = null;
			while((line=std.readLine())!=null){
				String[] str = line.split("	");
				int No = Integer.parseInt(str[0]);
				System.out.println(No);
				
				
				if(str.length < 2){
					Set<Integer> set = new HashSet<Integer>();
					set.add(1);
					map.put(No, set);
					continue;
					
				}
				
				String keywords = str[1].trim();
				Set<Integer> ptrees = new HashSet<Integer>(); 
				String[] singleKeyword = keywords.split(" ");
				for(String s:singleKeyword){
//					if(wordCount.get(s)<6) continue;
				
					Set<Integer> set = buffer.get(s);
					//if the keyword is the first time to compute, store it in the buffer
					if(set == null){
						set = new HashSet<Integer>();
						set.addAll(computeDistance(s, topk, overlapSize,returnSize));
						buffer.put(s, set);
					}
					ptrees.addAll(set);
					System.out.println("NO: "+No+"  size: "+ptrees.size());
				}
				if(ptrees.isEmpty()){
					ptrees.add(1);
					map.put(No, ptrees);
				}
				else{
					Set<Integer> completePtrees = reader.completeCPTree(ptrees,cptree);
					map.put(No, completePtrees);
				}
				
			}
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	public Map<String, Integer> wordCount(){
		Map<String, Integer> wordCount = new HashMap<String,Integer>();
		
		try {
			BufferedReader std = new BufferedReader(new FileReader(nodeFile));
			String line = null;
			while((line=std.readLine())!=null){
				String[] str = line.split("\t");
				if(str.length < 3) continue;
				
				String keywords = str[2];
				String[] singleKeyword = keywords.split(" ");
				for(String s:singleKeyword){
					if(wordCount.containsKey(s.trim())) wordCount.put(s.trim(), wordCount.get(s.trim())+1);
					else wordCount.put(s.trim(), 1);
				}
			}
//			int max = 0;
//			Map<Integer, Integer> count = new HashMap<Integer,Integer>();
//			for(Iterator<Integer> iter = wordCount.values().iterator();iter.hasNext();){
//				int times = iter.next();
//				if(max < times) max = times;
//				if(count.containsKey(times)){
//					count.put(times, count.get(times)+1); 
//				}else count.put(times,1);
//			}
//			
//			BufferedWriter stdout = new BufferedWriter(new FileWriter("/Users/chenyankai/Documents/DBLP/wordCount.txt"));
//			int totalKeyword = 0;
//			for(int i= 1;i<max;i++){
//				if(count.containsKey(i)){
//					totalKeyword+= count.get(i);
//					stdout.write(i+" different keywords: " + count.get(i));
//					stdout.newLine();
//				}
//			}
//			stdout.write("totalkeyword:" + totalKeyword);
//			stdout.flush();
//			stdout.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
	}
		
		return wordCount;
	}
	
	
	private void writeFile(String outFile){
		try {
			BufferedWriter stdout = new BufferedWriter(new FileWriter(outFile));
			int lineNum = 1;
			while(lineNum<map.size()+1){
				Set<Integer> set = map.get(lineNum);
				List<Integer> list = new ArrayList<Integer>(set);
				Collections.sort(list);	
				String write = lineNum+"\t";
				for(int x:list) write+=x+" ";
				stdout.write(write.trim());
				stdout.newLine();
				lineNum++;
			}
			stdout.flush();
			stdout.close();		
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}
	
	
	public void run(String outFile,int times){
		loadCCS();
		reloadKeywords(1, 3, times);
		writeFile(outFile);
	}
	
	
	
	
	
	public static void main(String[] args){
//		String nodefile = "/home/fangyixiang/Desktop/HC/dataspace/DBLP/dblp-node";
//		String CPtreeFile = "/home/fangyixiang/Desktop/HC/dataspace/DBLP/CPTree";
//		String out1 ="/home/fangyixiang/Desktop/HC/dataspace/DBLP/dblp-pcs-node-1.txt";
//		String out2 ="/home/fangyixiang/Desktop/HC/dataspace/DBLP/dblp-pcs-node-2.txt";

//		
//		String nodefile = "/home/fangyixiang/Desktop/HC/dataspace/Flickr/flickr-node.txt";
//		String CPtreeFile = "/home/fangyixiang/Desktop/HC/dataspace/Flickr/CPTree.txt";
//		String out1 ="/home/fangyixiang/Desktop/HC/dataspace/Flickr/flickr-pcs-node-1.txt";
//		String out ="/home/fangyixiang/Desktop/HC/dataspace/Flickr/flickr-pcs-node-test.txt";

//		String out2 ="/home/fangyixiang/Desktop/HC/dataspace/Flickr/flickr-pcs-node-2.txt";
//		String out3 ="/home/fangyixiang/Desktop/HC/dataspace/Flickr/flickr-node-3.txt";

		String nodefile = "/Users/chenyankai/Documents/HKU_research/PCS/dataset/facebook/items.txt";
		String CPtreeFile ="/Users/chenyankai/Documents/HKU_research/PCS/dataset/facebook/CPTree.txt";
		String out = "/Users/chenyankai/Documents/HKU_research/PCS/dataset/facebook/node.txt";
		Profile profile = new Profile(nodefile,CPtreeFile);
		profile.run(out,1);
//		profile = new Profile(nodefile,CPtreeFile);
//		profile.run(out2,2);
		
		
//		profile.wordCount();
	}
	
}
