package EXP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.*;

import algorithm.CPTreeReader;
import algorithm.DataReader;
import algorithm.PreviousWork.SIGKDD2010;
import algorithm.PreviousWork.SIGMOD2014;
import algorithm.ProfiledTree.PNode;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.Query1_margin.Query1;
import config.Config;
import sun.launcher.resources.launcher;

public class accuracy {
	int[][] graph = null;
	int[][] nodes = null;
	Map<Integer, Set<Integer>> groundTrueCommunities = null;
	
	public accuracy(String graphFile, String nodeFile){
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		graph = dataReader.readGraph();
		nodes = dataReader.readNodes();
		this.groundTrueCommunities = new HashMap<Integer, Set<Integer>>();
	}

	public double precision(Set<Integer> queryCommunity, Set<Integer> trueCommunity){
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(queryCommunity);
		set.retainAll(trueCommunity);
		double size1 = set.size();
		double size2 = queryCommunity.size();
		if(size2 == 0) return 0; 
		return size1/size2;
	}
	
	public double recall(Set<Integer> queryCommunity, Set<Integer> trueCommunity){
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(queryCommunity);
		set.retainAll(trueCommunity);
		double size1 = set.size();
		double size2 = trueCommunity.size();
		if(size2 == 0) return 0; 
		return size1/size2;
	}
	

	public double F1(double precision, double recall){
		if(precision==0 && recall==0) return 0;
		return 2*(precision*recall)/(precision+recall);
	}
	
	
	public Map<Integer, Set<Integer>> getGroundTrueCommunity(String file,int k){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = new String();
			while((line=reader.readLine())!=null){
				String[] str = line.split("\t");
				if(str.length >= k+1){
					Random random = new Random();
					int queryId = Integer.parseInt(str[random.nextInt(str.length-2) + 1]);
					
					if(!groundTrueCommunities.containsKey(queryId)){
						Set<Integer> commmunity = new HashSet<Integer>();
						for(int i = 1; i <str.length; i++ ){
							int vertex = Integer.parseInt(str[i]);
							commmunity.add(vertex);
						}
						groundTrueCommunities.put(queryId, commmunity);
					} 	
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return groundTrueCommunities;
	}
	
	
	private KWTree buildKWtree(String graphFile,String nodeFile,String CPtreeFile){
		
		CPTreeReader cpReader = new CPTreeReader(CPtreeFile);
		PNode root=cpReader.loadCPtreeRoot();
		KWTree kwTree = new KWTree(graphFile, nodeFile, root);
		kwTree.build();
		return kwTree;
	}
	
	
	public void expPCS(int k,KWTree kwTree){
		Config.k = 4;
		
		Iterator<Integer> iterator = groundTrueCommunities.keySet().iterator();
		int count = 0;
		double pre = 0.0;
		double rec = 0.0;
		double F1All = 0.0;
		while(iterator.hasNext()){
			count++;
			Query1 query1 = new Query1(graph, kwTree.getHeadList());

			int key = iterator.next();
			query1.query(key, 3);
			Map<Set<Integer>,Set<Integer>> patternUsers = query1.getPatternUsers();
			Set<Integer> set = new HashSet<Integer>();
			Iterator<Set<Integer>> communities = patternUsers.values().iterator();
			while(communities.hasNext()){
				set.addAll(communities.next());
				break;
			}
			Set<Integer> trueCommunity = groundTrueCommunities.get(key);
			double pre1 = precision(set, trueCommunity);
			pre += pre1;
			double rec1 = recall(set, trueCommunity);
			rec += rec1;
			double F1 = precision(set, trueCommunity);
			F1All += F1;
			System.out.println(" pre: "+pre1+" rec1: "+rec1+" F1: "+F1);
		}
		System.out.println(" pre: "+pre/count+" rec1: "+rec/count+" F1: "+F1All/count);

		
	}
	
	
	
	public void exp(){
		Iterator<Integer> iterator = groundTrueCommunities.keySet().iterator();
		int count11 = 0;
		int count21 = 0;
		int count12 = 0;
		int count22 = 0;
		int count13 = 0;
		int count23 = 0;
		double preGlobal = 0.0;
		double recGlobal = 0.0;
		double F1Global = 0.0;
		
		double preLocal = 0.0;
		double recLocal = 0.0;
		double F1Local = 0.0;
		int count = 0;
		while(iterator.hasNext()){
			count ++;
			int key = iterator.next();
			Set<Integer> trueCommunity = groundTrueCommunities.get(key);
			SIGKDD2010 method1 = new SIGKDD2010(graph);
			SIGMOD2014 method2 = new SIGMOD2014(graph);
			Set<Integer> set1 = method1.query(key);
			Set<Integer> set2 = method2.query(key);
			double pre1 = precision(set1, trueCommunity);
			double pre2 = precision(set2, trueCommunity);
			if(pre1!=0) count11++;
			if(pre2!=0) count21++;
			preGlobal += pre1;
			preLocal += pre2;
			
			double rec1 = recall(set1, trueCommunity);
			double rec2 = recall(set2, trueCommunity);
			if(rec1!=0) count12++;
			if(rec2!=0) count22++;
			recGlobal += rec1;
			recLocal += rec2;
			
			double F11 = F1(pre1, rec1);
			double F12 = F1(pre2,rec2);
			if(F11!=0) count13++;
			if(F12!=0) count23++;
			F1Global += F11;
			F1Local += F12;
		}
		
//		System.out.println("global pre: "+preGlobal/count11+" recall: "+recGlobal/count12+" F1: "+ F1Global/count13);
//		System.out.println("local pre: "+preLocal/count21+" recall: "+recLocal/count22+" F1: "+ F1Local/count23);

		System.out.println("global pre: "+preGlobal/count+" recall: "+recGlobal/count+" F1: "+ F1Global/count);
		System.out.println("local pre: "+preLocal/count+" recall: "+recLocal/count+" F1: "+ F1Local/count);

	}
	
	
	
	
	public static void main(String[] args){
		String graphFile= "/Users/chenyankai/Documents/HKU_research/PCS/dataset/facebook/edges.txt";
		String nodesFile = "/Users/chenyankai/Documents/HKU_research/PCS/dataset/facebook/node.txt";
		String CPTreeFile = "/Users/chenyankai/Documents/HKU_research/PCS/dataset/facebook/CPTree.txt";
		
		accuracy accuracy = new accuracy(graphFile, nodesFile);
		String file = "/Users/chenyankai/Downloads/facebook/0.circles";
//		Map<Integer, Set<Integer>> map = accuracy.getGroundTrueCommunity(file, 4);
//		for(Iterator<Integer> key = map.keySet().iterator(); key.hasNext();){
//			int queryId = key.next();
//			Set<Integer> community = map.get(queryId);
//			System.out.println("queryId: "+queryId+"  community :"+community.toString());
//		}
		
		KWTree kwTree = accuracy.buildKWtree(graphFile, nodesFile, CPTreeFile);
//		accuracy.test(4, kwTree);
		accuracy.getGroundTrueCommunity(file, 4);
//		accuracy.exp();
		accuracy.expPCS(4, kwTree);
	}
	
}