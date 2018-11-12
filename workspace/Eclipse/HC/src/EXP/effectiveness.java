package EXP;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;


import algorithm.CPTreeReader;
import algorithm.DataReader;
import algorithm.PreviousWork.*;
import algorithm.ProfiledTree.PNode;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.Query1_margin.Query1;
import config.Config;
import config.Log;

public class effectiveness {
	private String localDataAddress = "/Users/chenyankai/Documents/HKU_research/PCS/dataset/";
	private String acmccsGraph = localDataAddress + "ACM_CCS/edges.txt";
	private String acmccsNode = localDataAddress + "ACM_CCS/nodes.txt";
	private String acmccsCPTree = localDataAddress + "ACM_CCS/CPTree.txt";
	private String acmccsQuery = localDataAddress + "ACM_CCS/queryDiversityFile4.txt";
	private String acmccsResult = localDataAddress + "ACM_CCS/result.txt";
			
	private String pubmedGraph = localDataAddress + "pubmed/edge-40.txt";
	private String pubmedNode = localDataAddress + "pubmed/node-40.txt";
	private String pubmedCPTree = localDataAddress + "pubmed/cptree.txt";
	private String pubmedQuery = localDataAddress + "pubmed/queryDiversityFile4.txt";
	private String pubmedResult = localDataAddress + "pubmed/result.txt";
	
//	private String dblpGraph = localDataAddress + "DBLP/dblp-pcs-graph";
//	private String dblpNode = localDataAddress + "DBLP/dblp-pcs-node-1.txt";
//	private String dblpQuery = localDataAddress + "DBLP/queryDiversityFile4.txt";
	
	private String flickrGraph = localDataAddress + "Flickr/flickr-pcs-graph.txt";
	private String flickrNode = localDataAddress + "Flickr/flickr-pcs-node-1.txt";
	private String flickrCPTree = localDataAddress + "Flickr/CPTree.txt";
	private String flickrQuery = localDataAddress + "Flickr/queryDiversityFile4.txt";
	private String flickrResult = localDataAddress + "Flickr/result.txt";
	

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
			System.out.println(queryList.size()+"  "+e.getMessage());
		}	
		return queryList;
	}
	
	
	private void compareCSsolutions(int[][] graph, String queryFile){
		List<Integer> queryList = readQueryFile(queryFile);
		
		int count1 = 0; 
		int count2 = 0;
		for(int queryId:queryList){
			SIGKDD2010 query1 = new SIGKDD2010(graph);
			Set<Integer> set1 = query1.query(queryId);
			
			if(set1.size()>=Config.k) count1++;
			
			SIGMOD2014 query2 = new SIGMOD2014(graph);
			Set<Integer> set2 = query2.query(queryId);
			
			if(set2.size()>=Config.k) count2++;
//			System.out.println(set1.size()+"   "+set2.size());
		}
		System.out.println("SIGKDD2010 community number: "+count1+"   SIGMOD2014 community number: "+count2);
	}
	
	private Map<Integer, Map<Set<Integer>,Set<Integer>>> queryK(KWTree kwTree1,String queryFile){
		Map<Integer, Map<Set<Integer>,Set<Integer>>> verticesMaximalPtreeMap = new HashMap<Integer,Map<Set<Integer>,Set<Integer>>>();
		
		
		Query1 query1 = new Query1(kwTree1.graph,kwTree1.getHeadList());
		List<Integer> queryList = readQueryFile(queryFile);
		
		for(int x:queryList){
			System.out.println("now query: "+ x);
			query1.query(x,3);
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
	
	
	private double[] getLocationInSearchSpace(Map<Integer, Map<Set<Integer>,Set<Integer>>> map,int[][] node){
		double[] list  = new double[5];
		double[] tmp = new double[5];
		Iterator<Integer> key = map.keySet().iterator();
		while(key.hasNext()){
			int queryId = key.next();
			double searchSpace = node[queryId].length;
			double averageSize = 0;
			int count = 0;
			Map<Set<Integer>,Set<Integer>> patterns = map.get(queryId);
			Iterator<Set<Integer>> iter = patterns.keySet().iterator();
			while(iter.hasNext()){
				averageSize += iter.next().size();
				count++;
			}
			averageSize = averageSize/count;

			double propotion = averageSize/searchSpace;
			System.out.println(averageSize+"     "+searchSpace+"   "+propotion);

			if(propotion<=0.2) tmp[0]++;
			if(propotion<=0.4 && propotion >0.2) tmp[1]++;
			if(propotion<=0.6 && propotion >0.4) tmp[2]++;
			if(propotion<=0.8 && propotion >0.6) tmp[3]++;
			if(propotion<=1 && propotion >0.8) tmp[4]++;
		}
		int total = 0;
		for(double x:tmp) total += x;
		for(int i = 0;i<tmp.length;i++) list[i] = tmp[i]/total;
		return list;
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

	private List<Set<Set<Integer>>> classifyACPC(Map<Set<Integer>,Set<Integer>> bigMap){
		List<Set<Set<Integer>>> ACPCs = new ArrayList<Set<Set<Integer>>>(); 
		Set<Set<Integer>> ACSet = new HashSet<Set<Integer>>(); 
		Set<Set<Integer>> PCSet = new HashSet<Set<Integer>>(); 
		Iterator<Set<Integer>>iter1 = bigMap.keySet().iterator();
		int ACQMax = 0;
		while(iter1.hasNext()){
			Set<Integer> pattern = iter1.next();
			if(pattern.size() > ACQMax) ACQMax = pattern.size();
			}
		
		iter1 = bigMap.keySet().iterator();
		while(iter1.hasNext()){
			Set<Integer> pattern = iter1.next();
			if(pattern.size() == ACQMax){
				ACSet.add(bigMap.get(pattern));
			}
			PCSet.add(bigMap.get(pattern));
		}

//		if(PCSet.size() != ACSet.size()){
			ACPCs.add(ACSet);
			ACPCs.add(PCSet);
//		}
//		if(PCSet.size() == ACSet.size()) ACPCs.add(new HashSet<Set<Integer>>());
		return ACPCs;
	}

	private KWTree buildKWTree(String graphFile,String nodeFile,String CPtreeFile){
		CPTreeReader cpReader = new CPTreeReader(CPtreeFile);
		PNode root=cpReader.loadCPtreeRoot();
		
		KWTree kwTree1 = new KWTree(graphFile,nodeFile,root);
		kwTree1.build();
		return kwTree1;
	}
	
	public void writeACQPCS(String graph,String node,String CPTree,String queryFile,String outFile){
		KWTree kwtree = buildKWTree(graph, node, CPTree);
		Map<Integer, Map<Set<Integer>,Set<Integer>>> verticesMaximalPtreeMap = queryK(kwtree, queryFile);
		writeResult(verticesMaximalPtreeMap,outFile);
	}
	
	public void readACQPCS(String resultFile){
		Map<Integer, Map<Set<Integer>,Set<Integer>>> map = readResult(resultFile);
		System.out.println(countCommunity(map));
	}

	public void expSearchSpace(String graphFile,String nodeFile,String resultFile){
		Map<Integer, Map<Set<Integer>,Set<Integer>>> map = readResult(resultFile);
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		int[][] node =  dataReader.readNodes();
		double[] list = getLocationInSearchSpace(map, node);
		for(double x:list) System.out.println(x);
	}
	
	
	public void exp1014(String graphFile, String nodeFile,String queryFile){
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		int[][] graph =  dataReader.readGraph();
		compareCSsolutions(graph, queryFile);	  
	}
	

	boolean contains(int t, int[] tree){
		boolean contains = false;
		for(int x:tree){
			if(t == x){
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	//compute the tree edit distance 
	public double TED(int[] tree1, int[] tree2){
		double distance = 0;
		for(int node1:tree1){
			boolean contain = false;
			if(contains(node1, tree2)){
				contain = true;
				continue;
			} 
			if(!contain) distance++;
		}
		
		for(int node1:tree2){
			boolean contain = false;
			if(contains(node1, tree1)){
				contain = true;
				continue;
			} 
			if(!contain) distance++;
		}	
		Set<Integer> uniqueNodes = new HashSet<Integer>();
		for(int x:tree1) uniqueNodes.add(x);
		for(int x:tree2) uniqueNodes.add(x);
		return distance/(uniqueNodes.size());
	}
	
	//compute the community pairwise similarity value 
	private double CPS(Set<Set<Integer>> community,int[][]nodes){
		int communityNO = 0; 
		double CPSValue = 0.0;
		int times = 1;
		for(Set<Integer> set1:community){
			if(set1.size()==0) continue;
			boolean flag = false;
			double tmpValue = 0.0;
			for(int j:set1){
				int[] Ptreej = nodes[j];
				for(int k:set1){
					System.out.println("now times.: "+times);
					if(times==10000) {
						flag = true;
					}
					int[] Ptreek = nodes[k];
					double TEDValue = TED(Ptreej, Ptreek);
					tmpValue += TEDValue;
					times += 1;
				}
				if(flag) break;
			}	
			
			communityNO++;
			if(flag) CPSValue += tmpValue/(times);
			CPSValue += tmpValue/(set1.size()*set1.size());
		}
		System.out.println("CPS value: "+CPSValue+"  community numbers: "+communityNO);
		if(communityNO==0) return CPSValue;
		return CPSValue/communityNO;
	}
	
	// compute the community p-tree frequency 
	//compute the community P-tree frequency value 
	private double CPF(int queryId, Set<Set<Integer>> communities,int[][]nodes){
		double value = 0.0;
		int communityNo = 0;
		int[] Ptree = nodes[queryId];
		for(Set<Integer> community:communities){
			if (community.size()==0) continue;
			for(int singleNode:Ptree){
				int fi = 0;
				for(int vertex:community){
					int[] node = nodes[vertex];
					if(contains(singleNode, node)) fi++;
				}
				value += fi/community.size();
			}
			communityNo++;
		}
		if(communityNo == 0) return 0;
		return value/Ptree.length/communityNo;
	}
	
	
	
	public List<Double> expCPS(String resultFile,String graphFile,String nodeFile){
		List<Double> ACPCValues = new ArrayList<Double>();
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		int[][] nodes =  dataReader.readNodes();
		
		 Map<Integer, Map<Set<Integer>,Set<Integer>>> result = readResult(resultFile);
		 Iterator<Map<Set<Integer>,Set<Integer>>> iter1 = result.values().iterator();
		 int count = 0;
		 double ACValue = 0.0;
		 double PCValue = 0.0;
		 while(iter1.hasNext()){
			Set<Set<Integer>> ACs = new HashSet<Set<Integer>>();
			Set<Set<Integer>> PCs = new HashSet<Set<Integer>>();
			Map<Set<Integer>,Set<Integer>> tmp = iter1.next();
			List<Set<Set<Integer>>> ACPCs = classifyACPC(tmp);
			if(ACPCs.size()==0) continue;
			count++;
			ACs = ACPCs.get(0);
			PCs = ACPCs.get(1);
			double ACCPS = CPS(ACs, nodes);
			double PCCPS = 0.0;
		
			if(ACPCs.size() == 2) {
				PCCPS = CPS(PCs, nodes);
			}else{
				PCCPS = ACCPS;
			}
			
			ACValue += ACCPS;
			PCValue += PCCPS;	
			System.out.println(count+"    ACCPS: "+ACCPS+"  PCCPS: "+PCCPS);

		 }
		 ACPCValues.add(1-ACValue/count);
		 ACPCValues.add(1-PCValue/count);
		 return ACPCValues;
	}
	
	
	private void CPS1014(int[][] graph, int[][]node, String queryFile){
		List<Integer> queryList = readQueryFile(queryFile);
		double CPSValue10 = 0.0;
		double CPSValue14 = 0.0;
		int count1 = 0; 
		int count2 = 0;
		for(int queryId:queryList){
			SIGKDD2010 query1 = new SIGKDD2010(graph);
			Set<Integer> set1 = query1.query(queryId);
			Set<Set<Integer>> tmpSet1 = new HashSet<Set<Integer>>();
			tmpSet1.add(set1);
			double CPS10 = CPS(tmpSet1, node);
			CPSValue10 += CPS10; 
			if(set1.size()>=Config.k) count1++;
			
			SIGMOD2014 query2 = new SIGMOD2014(graph);
			Set<Integer> set2 = query2.query(queryId);
			Set<Set<Integer>> tmpSet2 = new HashSet<Set<Integer>>();
			tmpSet2.add(set2);
			double CPS14 = CPS(tmpSet2, node);
			CPSValue14 += CPS14;
			
			if(set2.size()>=Config.k) count2++;
//			break;
		}
		System.out.println(CPSValue10+" "+count1+" ");
		System.out.println("SIGKDD2010 CPS: "+(1-CPSValue10/count1)+"   SIGMOD2014 CPS: "+(1-CPSValue14/count2));
	}
	
	public void expCPS1014(String graphFile,String nodeFile,String queryFile){
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		int[][] graph = dataReader.readGraph();
		int[][] nodes = dataReader.readNodes();
		CPS1014(graph, nodes, queryFile);
	}
	
	public List<Double> expCPF(String resultFile,String graphFile,String nodeFile){
		List<Double> ACPCValues = new ArrayList<Double>();
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		int[][] nodes =  dataReader.readNodes();
		 Map<Integer, Map<Set<Integer>,Set<Integer>>> result = readResult(resultFile);
		 Iterator<Integer> iter1 = result.keySet().iterator();
		 int count = 0;
		 double ACValue = 0.0;
		 double PCValue = 0.0;
		 
		 while(iter1.hasNext()){
			int queryId = iter1.next();
			Set<Set<Integer>> ACs = new HashSet<Set<Integer>>();
			Set<Set<Integer>> PCs = new HashSet<Set<Integer>>();
			Map<Set<Integer>,Set<Integer>> tmp = result.get(queryId);
			List<Set<Set<Integer>>> ACPCs = classifyACPC(tmp);
			if(ACPCs.size()==0) continue;
			count++;
			ACs = ACPCs.get(0);
			PCs = ACPCs.get(1);
				
			double ACCPF = CPF(queryId, ACs, nodes);
			double PCCPF = CPF(queryId ,PCs, nodes);
				
			ACValue += ACCPF;
			PCValue += PCCPF;	
			System.out.println(count+"    ACCPS: "+ACCPF+"  PCCPS: "+PCCPF);

		}
		ACPCValues.add(ACValue/count);
		ACPCValues.add(PCValue/count);
		return ACPCValues;
	 }
	
	private void CPF1014(int[][] graph, int[][]node, String queryFile){
		List<Integer> queryList = readQueryFile(queryFile);
		double CPFValue10 = 0.0;
		double CPFValue14 = 0.0;
		int count1 = 0; 
		int count2 = 0;
		int zeroCount = 0;
		for(int queryId:queryList){
			SIGKDD2010 query1 = new SIGKDD2010(graph);
			Set<Integer> set1 = query1.query(queryId);
			Set<Set<Integer>> tmpSet1 = new HashSet<Set<Integer>>();
			tmpSet1.add(set1);
			double CPF10 = CPF(queryId,tmpSet1, node);
			CPFValue10 += CPF10; 
			if(set1.size()>=Config.k) count1++;
			
			SIGMOD2014 query2 = new SIGMOD2014(graph);
			Set<Integer> set2 = query2.query(queryId);
			Set<Set<Integer>> tmpSet2 = new HashSet<Set<Integer>>();
			tmpSet2.add(set2);
			double CPF14 = CPF(queryId, tmpSet2, node);
			CPFValue14 += CPF14;
			System.out.println("CPF10: "+CPF10+" CPF14: "+CPF14);
			if(set2.size()>=Config.k) count2++;
			
		}
		System.out.println(CPFValue10+" "+count1+" ");
		System.out.println("SIGKDD2010 CPS: "+(CPFValue10/count1)+"   SIGMOD2014 CPS: "+(CPFValue14/count1));
	}
	
	public void expCPF1014(String graphFile,String nodeFile,String queryFile){
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		int[][] graph = dataReader.readGraph();
		int[][] nodes = dataReader.readNodes();
		CPF1014(graph, nodes, queryFile);
	}
	
	private Set<Integer> getGlobalMCSubtree(int method,int queryId, int[][] graph,int[][] node){
		Set<Integer> community = new HashSet<Integer>();
		if (method == 1 ){
			SIGKDD2010 sigkdd2010 = new SIGKDD2010(graph);
			community = sigkdd2010.query(queryId);
		}else{
			SIGMOD2014 sigmod2014 = new SIGMOD2014(graph);
			community = sigmod2014.query(queryId);
		}

		Set<Integer> MCSubtree = new HashSet<Integer>();
		for(int x:node[queryId]) MCSubtree.add(x);

		for(int vertex:community){
			Set<Integer> Ptree= new HashSet<Integer>();
			for(int x:node[vertex]) Ptree.add(x);
			MCSubtree.retainAll(Ptree);
		}
		return MCSubtree;
	}
	
	
	public void expLevelGapOtherMethod(int method, Map<Integer, Map<Set<Integer>,Set<Integer>>> bigMap,String CPTree,int[][] graph,int[][]node){
		CPTreeReader cpTree = new CPTreeReader(CPTree);
		Map<Integer, PNode> cpTreeMap = cpTree.loadCPTree();
		int count=0;
		double all=0.0;
		Iterator<Integer> key = bigMap.keySet().iterator();
		while(key.hasNext()){
			int queryId = key.next();
			Map<Set<Integer>,Set<Integer>> map = bigMap.get(queryId); 
			Set<Set<Integer>> methodPattern = new HashSet<Set<Integer>>();
			Set<Set<Integer>> PCSpattern = new HashSet<Set<Integer>>();
			
		
			for(Iterator<Set<Integer>> iter = map.keySet().iterator();iter.hasNext();){
				Set<Integer> one = iter.next();
					PCSpattern.add(one);
			}
			methodPattern.add(getGlobalMCSubtree(method, queryId, graph, node));
			
			count++;
			double percentage = 0.0;
			Map<Integer, Integer> pcsCount = gapIneachLevel(cpTreeMap,PCSpattern);
			Map<Integer, Integer> acqCount = gapIneachLevel(cpTreeMap,methodPattern);
		
			for(Iterator<Integer> it = pcsCount.keySet().iterator();it.hasNext();){
				int level = it.next();
				if(acqCount.containsKey(level)){
					percentage += (double) acqCount.get(level)/pcsCount.get(level);
				}
			} 
					
			all+=percentage/pcsCount.size();
//			Log.log("average percentage/level: "+percentage/pcsCount.size());
		}
		
		System.out.println("all percentage frac: "+all/count+"\n");
	}
	
	
	
	public void expLevelGapACQ(Map<Integer, Map<Set<Integer>,Set<Integer>>> bigMap,String CPTree){
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
	
	public void expLDR(int method, String graphFile, String nodeFile, String CPTreeFile,String resultFile){
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		int[][] graph = dataReader.readGraph();
		int[][] node = dataReader.readNodes();
		Map<Integer, Map<Set<Integer>,Set<Integer>>> bigMap = readResult(resultFile);
		expLevelGapOtherMethod(method, bigMap, CPTreeFile, graph, node);
	}
	
	
	
	public static void main(String[] args){
		effectiveness eff = new effectiveness();
//		eff.exp1014(eff.acmccsGraph, eff.acmccsNode, eff.acmccsQuery);
//		eff.exp1014(eff.flickrGraph, eff.flickrNode, eff.flickrQuery);
//		eff.exp1014(eff.pubmedGraph, eff.pubmedNode, eff.pubmedQuery);
//		eff.writeACQPCS(eff.acmccsGraph, eff.acmccsNode, eff.acmccsCPTree, eff.acmccsQuery, eff.acmccsResult);
//		eff.writeACQPCS(eff.flickrGraph, eff.flickrNode, eff.flickrCPTree, eff.flickrQuery, eff.flickrResult);
//		eff.writeACQPCS(eff.pubmedGraph, eff.pubmedNode, eff.pubmedCPTree, eff.pubmedQuery, eff.pubmedResult);
//		eff.readACQPCS(eff.acmccsResult);
//		eff.readACQPCS(eff.flickrResult);
//		eff.readACQPCS(eff.pubmedResult);
//		System.out.println(eff.countCommunity(eff.readResult(eff.acmccsResult)));
//		System.out.println(eff.countCommunity(eff.readResult(eff.flickrResult)));
//		System.out.println(eff.countCommunity(eff.readResult(eff.pubmedResult)));

//		List<Double> list = eff.expCPS(eff.acmccsResult,eff.acmccsGraph ,eff.acmccsNode);
//		List<Double> list = eff.expCPS(eff.flickrResult,eff.flickrGraph ,eff.flickrNode);
//		List<Double> list = eff.expCPS(eff.pubmedResult,eff.pubmedGraph ,eff.pubmedNode);
//		System.out.println("AC value: "+list.get(0)+" PC value: "+list.get(1));
//		int[] tree1 = {1,2,3,4,5,13};
//		int[] tree2 = {1,2,5,7,11,14,16};
//		System.out.println(eff.TED(tree1, tree2));
		
//		eff.expCPS1014(eff.acmccsGraph, eff.acmccsNode, eff.acmccsQuery);
//		eff.expCPS1014(eff.flickrGraph, eff.flickrNode, eff.flickrQuery);
//		eff.expCPS1014(eff.pubmedGraph, eff.pubmedNode,eff.pubmedQuery);
		
		
//		List<Double> list = eff.expCPF(eff.acmccsResult,eff.acmccsGraph ,eff.acmccsNode);
//		List<Double> list = eff.expCPF(eff.flickrResult,eff.flickrGraph ,eff.flickrNode);
//		List<Double> list = eff.expCPF(eff.pubmedResult,eff.pubmedGraph ,eff.pubmedNode);
//		eff.expCPF1014(eff.acmccsGraph, eff.acmccsNode, eff.acmccsQuery);
//		eff.expCPF1014(eff.flickrGraph, eff.flickrNode, eff.flickrQuery);
//		eff.expCPF1014(eff.pubmedGraph, eff.pubmedNode, eff.pubmedQuery);
//		System.out.println("AC value: "+list.get(0)+" PC value: "+list.get(1));
//		eff.expSearchSpace(eff.acmccsGraph, eff.acmccsNode, eff.acmccsResult);
//		eff.expSearchSpace(eff.pubmedGraph, eff.pubmedNode, eff.pubmedResult);
//		eff.expSearchSpace(eff.flickrGraph,eff.flickrNode,eff.flickrResult);
		
		eff.expLDR(2, eff.acmccsGraph, eff.acmccsNode, eff.acmccsCPTree, eff.acmccsResult);
		eff.expLDR(2, eff.pubmedGraph, eff.pubmedNode, eff.pubmedCPTree, eff.pubmedResult);
		eff.expLDR(2, eff.flickrGraph, eff.flickrNode, eff.flickrCPTree, eff.flickrResult);
		
	}
	
	
}
