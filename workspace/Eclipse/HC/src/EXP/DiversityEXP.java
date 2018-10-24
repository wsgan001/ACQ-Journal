package EXP;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import com.sun.corba.se.impl.orbutil.graph.Graph;

import algorithm.CPTreeReader;
import algorithm.PreviousWork.SIGKDD2010;
import algorithm.ProfiledTree.PNode;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.Query1_margin.Query1;
import config.Config;
import config.Log;

public class diversityEXP {
	

	private List<Integer> readQueryFile(String queryFile){
		List<Integer> queryList = new ArrayList<Integer>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(queryFile));
			String line = null;
			while((line=bReader.readLine())!=null){
				queryList.add(Integer.parseInt(line));
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}	
		return queryList;
	}
	
	private KWTree buildKWTree(String graphFile,String nodeFile,String CPtreeFile){
		CPTreeReader cpReader = new CPTreeReader(CPtreeFile);
		PNode root=cpReader.loadCPtreeRoot();
		
		KWTree kwTree1 = new KWTree(graphFile,nodeFile,root);
		kwTree1.build();
		return kwTree1;
	}
	
	private Map<Integer, Map<Set<Integer>,Set<Integer>>> queryK(KWTree kwTree1,String queryFile){
		Map<Integer, Map<Set<Integer>,Set<Integer>>> verticesMaximalPtreeMap = new HashMap<Integer,Map<Set<Integer>,Set<Integer>>>();
//		Config.k = 6;
		
		
		Query1 query1 = new Query1(kwTree1.graph,kwTree1.getHeadList());
		List<Integer> queryList = readQueryFile(queryFile);
		
		for(int x:queryList){
			System.out.println("now query: "+ x);
			query1.query(x,3);
//			Set<Set<Integer>> result = query1.getMaximalPattern();
//			getPatternUsers
			Map<Set<Integer>,Set<Integer>> result = query1.getPatternUsers();
			if(!result.isEmpty()) verticesMaximalPtreeMap.put(x,result);
		}	
		return verticesMaximalPtreeMap;
	}
	
	public void writeResult(Map<Integer, Map<Set<Integer>,Set<Integer>>> map,String outFile){
	
		try{
			BufferedWriter stdout = new BufferedWriter(new FileWriter(outFile));
			for(Iterator<Integer> iter = map.keySet().iterator();iter.hasNext(); ){
				int key = iter.next();
				Map<Set<Integer>,Set<Integer>> patternUsers = map.get(key);
				String line = key+"";
				Iterator<Entry<Set<Integer>,Set<Integer>>> entryIt = patternUsers.entrySet().iterator();
				while(entryIt.hasNext()){
					Entry<Set<Integer>,Set<Integer>> entry = entryIt.next();
					Set<Integer> pattern = entry.getKey();
					Set<Integer> users = entry.getValue();
					line += "\t"+pattern.toString().substring(1,pattern.toString().length()-1)+":"
					+users.toString().substring(1,users.toString().length()-1);
				}	
				stdout.write(line);
				stdout.newLine();
			}
			stdout.flush();
			stdout.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public Map<Integer, Map<Set<Integer>,Set<Integer>>> readResult(String inFile){
	Map<Integer, Map<Set<Integer>,Set<Integer>>> map = new HashMap<Integer, Map<Set<Integer>,Set<Integer>>>();
    	try {
			BufferedReader stdIn = new BufferedReader(new FileReader(inFile));
    		String line = null;
    		
    		while((line=stdIn.readLine())!=null){
    			String[] fragment = line.split("\t");
    			int id = Integer.parseInt(fragment[0]);
    			Map<Set<Integer>,Set<Integer>> internMap = new HashMap<Set<Integer>, Set<Integer>>();
    			for(int i=1; i<fragment.length; i++){
    				String patternUsers= fragment[i].trim();
    				String[] str = patternUsers.split(":");
    				Set<Integer> pattern = new HashSet<Integer>();
    				Set<Integer> users = new HashSet<Integer>();
    				for(String x: str[0].split(",")) pattern.add(Integer.parseInt(x.trim()));
    				for(String x: str[1].split(",")) users.add(Integer.parseInt(x.trim()));
    				internMap.put(pattern, users);
    			}
    			map.put(id, internMap);
    		}
    			stdIn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	return map;
    }


	private String countCommunity(Map<Integer, Map<Set<Integer>,Set<Integer>>>bigMap){
		Iterator<Map<Set<Integer>,Set<Integer>>> iter1 = bigMap.values().iterator();
		int totalCommunityNumber = 0;
		int acqCommunityNumber = 0;
		while(iter1.hasNext()){
		Map<Set<Integer>,Set<Integer>> map= iter1.next();
		
		
		int ACQMax = 0;
		for(Iterator<Set<Integer>> iter = map.keySet().iterator();iter.hasNext();){
			Set<Integer> one = iter.next();
			if(one.size() > ACQMax) ACQMax = one.size();
		}
		for(Iterator<Set<Integer>> iter = map.keySet().iterator();iter.hasNext();){
			Set<Integer> one = iter.next();
			totalCommunityNumber ++;		
			if(one.size() == ACQMax) acqCommunityNumber++;
		}
	}
		return  "PCS community size: "+totalCommunityNumber+" ACQ communities: "+acqCommunityNumber+"\n";	
	}
	

	
	
	//count the distinct keywords in each query vertex
	private String distinctKeywords(Map<Integer, Map<Set<Integer>,Set<Integer>>> bigMap){
		double totalACQWord = 0;
		double totalPCSWord = 0;

		Iterator<Map<Set<Integer>,Set<Integer>>> iter1 = bigMap.values().iterator();
	
		
		while(iter1.hasNext()){
		Map<Set<Integer>,Set<Integer>> map= iter1.next();
		Set<Integer> acqWordBag = new HashSet<Integer>();
		Set<Integer> pcsWordBag = new HashSet<Integer>();

		int ACQMax = 0;
		
		for(Iterator<Set<Integer>> iter = map.keySet().iterator();iter.hasNext();){
			Set<Integer> one = iter.next();
					
			if(one.size() > ACQMax) ACQMax = one.size();
		}
		for(Iterator<Set<Integer>> iter = map.keySet().iterator();iter.hasNext();){
			Set<Integer> one = iter.next();
			if(one.size() == ACQMax) {
				for(int x:one) acqWordBag.add(x);
			}
			for(int x:one) pcsWordBag.add(x);
		}
		if(pcsWordBag.size()!=acqWordBag.size()){
			totalACQWord += acqWordBag.size();
			totalPCSWord += pcsWordBag.size();
		}

	}

	return "ACQ average distinct words: "+totalACQWord+"  PCS average distinct words: "+totalPCSWord+ " rad: "+(double) totalPCSWord/totalACQWord ;
	
}



	public void expLevelGap(Map<Integer, Map<Set<Integer>,Set<Integer>>> bigMap,String CPTree){
		CPTreeReader cpTree = new CPTreeReader(CPTree);
		Map<Integer, PNode> cpTreeMap = cpTree.loadCPTree();
		int count=0;
		double all=0.0;
		
		Iterator<Map<Set<Integer>,Set<Integer>>> iter1 = bigMap.values().iterator();
		while(iter1.hasNext()){
			Map<Set<Integer>,Set<Integer>> map= iter1.next();
			Set<Integer> acqWordBag = new HashSet<Integer>();
			Set<Integer> pcsWordBag = new HashSet<Integer>();
			Set<Set<Integer>> PCSpattern = new HashSet<Set<Integer>>();
			Set<Set<Integer>> ACQpattern = new HashSet<Set<Integer>>();


			int ACQMax = 0;
			
			for(Iterator<Set<Integer>> iter = map.keySet().iterator();iter.hasNext();){
				Set<Integer> one = iter.next();
				if(one.size() > ACQMax) ACQMax = one.size();
			}
			
			for(Iterator<Set<Integer>> iter = map.keySet().iterator();iter.hasNext();){
				Set<Integer> one = iter.next();
				if(one.size() == ACQMax) {
					for(int x:one)  acqWordBag.add(x);	
					ACQpattern.add(one);
				}
					for(int x:one) pcsWordBag.add(x);
					PCSpattern.add(one);
			}
			
			
			if(acqWordBag.size()!=pcsWordBag.size()) {
				count++;
				double percentage = 0.0;
				Map<Integer, Integer> pcsCount = gapIneachLevel(cpTreeMap,PCSpattern);
				Map<Integer, Integer> acqCount = gapIneachLevel(cpTreeMap,ACQpattern);
		
				for(Iterator<Integer> it = pcsCount.keySet().iterator();it.hasNext();){
					int level = it.next();
					if(acqCount.containsKey(level)){
						percentage += (double) acqCount.get(level)/pcsCount.get(level);
					}
				} 
					
				all+=percentage/pcsCount.size();
//				Log.log("average percentage/level: "+percentage/pcsCount.size());
			}
		}
		Log.log("all percentage frac: "+all/count+"\n");
	}
	
	
	public Map<Integer, Integer> gapIneachLevel(Map<Integer, PNode> cpTreeMap,Set<Set<Integer>> patterns){
		Map<Integer,Integer> levelNum = new HashMap<Integer,Integer>();
		for(Set<Integer> set:patterns){
			for(int x:set){
				int level = getLevel(cpTreeMap, x);
				if(levelNum.containsKey(level)) {
					int count =levelNum.get(level);
					levelNum.put(level, count+1);
				}
				else levelNum.put(level, 1);
			}	
		}	
		return levelNum;
	}
	
	private int getLevel(Map<Integer, PNode> cpTreeMap,int x){
		PNode node = cpTreeMap.get(x);
		int level=1;
		while(node.getId()!=1){
			level++;
			node=node.father;
		}
		return level;
	}

	public void expDiversity(String CPTree,String resultFile){
		Map<Integer, Map<Set<Integer>,Set<Integer>>> map = readResult(resultFile);
		
		Log.log(resultFile);
		Log.log(countCommunity(map)+"\n");
		Log.log(distinctKeywords(map)+"\n");
		expLevelGap(map,CPTree);
	}


	
	public void exp(String graph,String node,String CPTree,String queryFile,String outFile){
		KWTree kwtree = buildKWTree(graph, node, CPTree);
		Map<Integer, Map<Set<Integer>,Set<Integer>>> verticesMaximalPtreeMap = queryK(kwtree, queryFile);
		String std = countCommunity(verticesMaximalPtreeMap);
//		Log.log(graph+"\n"+std);
		writeResult(verticesMaximalPtreeMap,outFile);
	}
	
	
	
	
	public static void main(String[] args){
		diversityEXP diversityEXP = new diversityEXP();
//		diversityEXP.exp(Config.pubMedGraph, Config.pubMedNode, Config.pubmedCPtree,Config.pubMedDataWorkSpace+"query1000.txt",Config.pubMedDataWorkSpace+"122result");
//		diversityEXP.exp(Config.FlickrGraph, Config.FlickrNode1,Config.FlickrCPTree,Config.FlickrDataWorkSpace+"query1000.txt",Config.FlickrDataWorkSpace+"122result");
//		Map<Integer, Map<Set<Integer>,Set<Integer>>> map = diversityEXP.readResult(Config.pubMedDataWorkSpace+"122result");
//		diversityEXP.exp(Config.acmccsDataWorkSpace+"edges_jimGray.txt",Config.acmccsDataWorkSpace+"nodes_jimGray.txt",Config.ACMDLCPtree,Config.acmccsDataWorkSpace+"query1",Config.acmccsDataWorkSpace+"resultjimGray");
		
		for(int i=5;i<9;i++){
			Config.k =i;
//			diversityEXP.exp(Config.ACMDLGraph, Config.ACMDLNode, Config.ACMDLCPtree,Config.acmccsDataWorkSpace+"query1000"+i,Config.acmccsDataWorkSpace+"result"+i);
//			diversityEXP.exp(Config.dblpGraph, Config.dblpNode1,Config.DBLPCPTree,Config.DBLPDataWorkSpace+"query1000"+i,Config.DBLPDataWorkSpace+"result"+i);
//			diversityEXP.expDiversity(Config.ACMDLCPtree, Config.acmccsDataWorkSpace+"result"+i);
//			diversityEXP.expDiversity(Config.DBLPCPTree, Config.DBLPDataWorkSpace+"result"+i);
		}
		
//		diversityEXP.expDiversity(Config.pubmedCPtree, Config.pubMedDataWorkSpace+"122result");
//		diversityEXP.expDiversity(Config.FlickrCPTree,Config.FlickrDataWorkSpace+"122result");
//		map=diversityEXP.readResult(Config.FlickrDataWorkSpace+"122result");
//		Log.log(diversityEXP.countCommunity(map)+"\n");
//		Log.log(diversityEXP.distinctKeywords(map)+"\n");
		
//		diversityEXP.exp(kwtree, Config.pubMedDiversityQueryFile4,Config.pubMedDataWorkSpace+"result4.txt");
//		diversityEXP.exp(kwtree, Config.pubMedDiversityQueryFile5,Config.pubMedDataWorkSpace+"result5.txt");
//		diversityEXP.exp(kwtree, Config.pubMedDiversityQueryFile6,Config.pubMedDataWorkSpace+"result6.txt");
//		diversityEXP.exp(kwtree, Config.pubMedDiversityQueryFile7,Config.pubMedDataWorkSpace+"result7.txt");
//		diversityEXP.exp(kwtree, Config.pubMedDiversityQueryFile8,Config.pubMedDataWorkSpace+"result8.txt");

//		kwtree = diversityEXP.buildKWTree(Config.ACMDLGraph, Config.ACMDLNode, Config.ACMDLCPtree);
//		diversityEXP.exp(kwtree, Config.ACMDLDiversityQueryFile4,Config.acmccsDataWorkSpace+"result4.txt");
//		diversityEXP.exp(kwtree, Config.ACMDLDiversityQueryFile5,Config.acmccsDataWorkSpace+"result5.txt");
//		diversityEXP.exp(kwtree, Config.ACMDLDiversityQueryFile6,Config.acmccsDataWorkSpace+"result6.txt");
//		diversityEXP.exp(kwtree, Config.ACMDLDiversityQueryFile7,Config.acmccsDataWorkSpace+"result7.txt");
//		diversityEXP.exp(kwtree, Config.ACMDLDiversityQueryFile8,Config.acmccsDataWorkSpace+"result8.txt");

//		kwtree = diversityEXP.buildKWTree(Config.dblpGraph, Config.dblpNode1, Config.DBLPCPTree);
//		diversityEXP.exp(kwtree, Config.dblpDiversityQueryFile4,Config.DBLPDataWorkSpace+"result4.txt");
//		diversityEXP.exp(kwtree, Config.dblpDiversityQueryFile5,Config.DBLPDataWorkSpace+"result5.txt");
//		diversityEXP.exp(kwtree, Config.dblpDiversityQueryFile6,Config.DBLPDataWorkSpace+"result6.txt");
//		diversityEXP.exp(kwtree, Config.dblpDiversityQueryFile7,Config.DBLPDataWorkSpace+"result7.txt");
//		diversityEXP.exp(kwtree, Config.dblpDiversityQueryFile8,Config.DBLPDataWorkSpace+"result8.txt");

//		kwtree = diversityEXP.buildKWTree(Config.FlickrGraph, Config.FlickrNode1, Config.FlickrCPTree);
//		diversityEXP.exp(kwtree, Config.flickrDiversityQueryFile4,Config.FlickrDataWorkSpace+"result4.txt");
//		diversityEXP.exp(kwtree, Config.flickrDiversityQueryFile5,Config.FlickrDataWorkSpace+"result5.txt");
//		diversityEXP.exp(kwtree, Config.flickrDiversityQueryFile6,Config.FlickrDataWorkSpace+"result6.txt");
//		diversityEXP.exp(kwtree, Config.flickrDiversityQueryFile7,Config.FlickrDataWorkSpace+"result7.txt");
//		diversityEXP.exp(kwtree, Config.flickrDiversityQueryFile8,Config.FlickrDataWorkSpace+"result8.txt");

		
		
	}
	
	
}
