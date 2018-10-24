package EXP;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import algorithm.CPTreeReader;
import algorithm.ProfiledTree.PNode;
import algorithm.basic.DFS;
import algorithm.basic.BFS;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.Query1_margin.Query1;
import algorithm.kwIndex.Query2.Query2_Inc;
import algorithm.kwIndex.Query2.query2_MP;
import algorithm.simpleKWIndex.simKWTree;
import algorithm.simpleKWIndex.query1.query1;
import config.Config;


public class indexBasedQueryEXP {

	public indexBasedQueryEXP(){
		
	}
	
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
	
	public void exp(String graphFile,String nodeFile,String CPtreeFile,String queryFile){
		
		Config.k = Integer.parseInt(queryFile.substring(queryFile.length()-5,queryFile.length()-4));
		System.out.println("K is: "+Config.k);
		
		CPTreeReader cpReader = new CPTreeReader(CPtreeFile);
		PNode root=cpReader.loadCPtreeRoot();
		
		KWTree kwTree1 = new KWTree(graphFile,nodeFile,root);
		kwTree1.build();
		Query1 query1 = new Query1(kwTree1.graph,kwTree1.getHeadList());
//		Query2_Inc query2_Inc= new Query2_Inc(kwTree1.graph, kwTree1.getHeadList());

//		kwTree1=null;
		
//		simKWTree kwTree2 = new simKWTree(graphFile, nodeFile, root);
//		kwTree2.build();
//		query1 simQuery2 = new query1(kwTree2.graph,kwTree2.getHeadList());
//		kwTree2=null;
		
//		BFS bfs= new BFS(graphFile, nodeFile,cpReader.loadCPTree(CPtreeFile));
		long time1 = 0;
		long time2 = 0; 
//		
		List<Integer> queryList = readQueryFile(queryFile);
//		List<Integer> list = new ArrayList<Integer>(); 
		StringBuffer std = new StringBuffer(); 
		int leavesCount = 0;
		int allCount = 0;
		int acqCount = 0;
		int PCSCount = 0;
		double size1=0.0;
		double size2=0.0;
		double count1=0.0;
		double count2=0.0;
		int[][] nodes = kwTree1.nodes;
		int[] groups = new int[5];
		List<Double> result = new ArrayList<Double>();
		for(int x:queryList){
			System.out.println("now query: "+x);
			long time11 = System.nanoTime();
//			query1.query(x);
			time1+=System.nanoTime()-time11;
//			getNum(query1.getMaximalPattern(),std);
//			int[] size= getleavesProportionofACQ(query1.getLeavesofMaximalPattern());
//			int[] size= getleavesProportionofPCS(query1.getLeavesofMaximalPattern());
//			int[] size = getAverageLeaves(query1.getLeavesofMaximalPattern());
//			leavesCount+=size[0];
//			allCount+=size[1];
//			acqCount+=size[2];
//			PCSCount+=size[3];
//			int size[] = getNumbersofItems(query1.getLeavesofMaximalPattern());
//			int size[] = getNum(query1.getMaximalPattern());
//			acqCount+=size[0];
//			PCSCount+=size[1];
			
//			list.add(query1.getoutputSize());
//			long time21=System.nanoTime();
////			simQuery2.query(x);
////			bfs.query(x);
////			query2_Inc.query(x);
//			time2+=System.nanoTime()-time21;
			
//			result.add( getweight(query1.getLeavesofMaximalPattern()));
//			result.add(getleavesProportion(query1.getLeavesofMaximalPattern()));
//			result.add(getitemsProportion(query1.getLeavesofMaximalPattern()));
//			double[] sss = CMF(query1.getPatternUsers(), x, nodes);
//			double[]sss = getitemsProportion(query1.getLeavesofMaximalPattern(), (double) nodes[x].length);
//			if(sss[0]!=0&&sss[1]!=0){
//				size1+=sss[0];
//				count1++;
//				size2+=sss[1];
//				count2++;
//			}
			int[] result1 =getweight(query1.getLeavesofMaximalPattern());
			for(int i=0;i<5;i++) groups[i]+=result1[i]; 
//			size1+=result[0];
//			size2+=result[1];
		}
		for(int x:groups)  System.out.println(x);
//		System.out.println("acq CMF:"+size1/count1+ " pcs CMF: "+size2/count2);
//		System.out.println("acq distinct item proportation: "+size1/count1+" pcs: "+size2/count2);
//		System.out.println("leaves: "+(double)leavesCount/acqCount+"  all items: "+(double) allCount/PCSCount);
//		System.out.println("acq community: "+ acqCount+ " pcs community: "+ PCSCount);
//		double sum = 0.0;
//		int count=0;
//		for(double x:result) {
//			sum+=x;
//			if(x!=0) count++;
//		}
//		System.out.println(result.toString());
//		System.out.println("sum of weight: "+(double)(sum/count));
//		long time11 = System.nanoTime();
//		query1.query(22901); // 22901 is good example for comparsion with ACQ:
//		time1+=System.nanoTime()-time11;
//		
//		System.out.println("hard index time1: "+time1/1000000+" simple index time2: "+time2/1000000+"time gap: "+(double)time2/time1 );
//		 for(int x:list){
//			 System.out.println("communities size: "+x);
//		 }
		
//		query2_MP.query(22901);
//		query2_MP.print();
		
//		subKwTree size 16
//		query.query(5473); 
		
		
//		subkwtree size 39
//		long time = System.nanoTime();
//		query1.query(5964); 
//		long time1= System.nanoTime()-time; 
//			
//		long time2 = System.nanoTime();
//		query2.query(5964);
//		long time3= System.nanoTime()-time2; 
//		System.out.println("simple Index: "+time3/1000000 +" hard index: "+time1/1000000);
//		
//		long time2 = System.nanoTime();
//		query1.query(37899);
//		bfs.query(37899);
//		long time3= System.nanoTime()-time2; 
//		System.out.println(time3/time1);
		
		
//		DFS dfs= new DFS(graphFile, nodeFile,cpReader.loadCPTree(CPtreeFile));
//		long time4 = System.nanoTime();
//		dfs.query(5964);
//		long time5 = System.nanoTime()-time4;
//		System.out.println(time5/time1);
		
		
		
		
//		query.query(933);
	
	}

	public int[] getNum(Set<Set<Integer>> maximalPattern){
		int size = -1;
		int[] ACQPCS = new int[2];
		Set<Set<Integer>> ACQPattern = new HashSet<Set<Integer>>();
		for(Set<Integer> pattern:maximalPattern){
			if(pattern.size()>size) size = pattern.size();
		}
		for(Set<Integer> pattern:maximalPattern){
			if(pattern.size()==size) ACQPattern.add(pattern);
		}
		ACQPCS[0] = ACQPattern.size();
		ACQPCS[1] = maximalPattern.size();
//		std.append( "ACQ size: "+ACQPattern.size()+"   others in pcs: "+(maximalPattern.size()-ACQPattern.size())+"\n");
		return ACQPCS;
	}
	
	
	//acq size
	public double getleavesProportion(Map<Set<Integer>, Set<Integer>> patternLeaves){
		int size = -1;

		Set<Integer> acqDistinctLeaves = new HashSet<Integer>(); 
		Set<Integer> PCSDistinctLeaves = new HashSet<Integer>(); 
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(pattern.size()>size) size = pattern.size();
		}
		Set<Set<Integer>> ACQPattern = new HashSet<Set<Integer>>();
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(pattern.size()==size) ACQPattern.add(pattern);
		}
		
		for(Set<Integer> set:patternLeaves.keySet()){
			if(ACQPattern.contains(set)){
				acqDistinctLeaves.addAll(patternLeaves.get(set));
			}
				PCSDistinctLeaves.addAll(patternLeaves.get(set));
		}

		double size1 = PCSDistinctLeaves.size();
		double size2 = acqDistinctLeaves.size(); 
		return size1/size2;
	}
	
	public double[] getitemsProportion(Map<Set<Integer>, Set<Integer>> patternLeaves,double queryNodeSize){
		int size = -1;
		double size1 = 0.0;
		double size2 = 0.0;
		Set<Integer> acqDistinctLeaves = new HashSet<Integer>(); 
		Set<Integer> PCSDistinctLeaves = new HashSet<Integer>(); 
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(pattern.size()>size) size = pattern.size();
		}
		Set<Set<Integer>> ACQPattern = new HashSet<Set<Integer>>();
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(pattern.size()==size) ACQPattern.add(pattern);
		}
		
		for(Set<Integer> set:patternLeaves.keySet()){
			if(ACQPattern.contains(set)){
				acqDistinctLeaves.addAll(set);
			}
				PCSDistinctLeaves.addAll(set);
		}
		if(ACQPattern.size()!=patternLeaves.size()){
			size1 = acqDistinctLeaves.size()/queryNodeSize;
			size2 = PCSDistinctLeaves.size()/queryNodeSize; 
		}
		double[] result ={size1,size2};
//		System.out.println(size1+"  "+size2);
		return result;
	}
	
	
	
	public int[] getAverageLeaves(Map<Set<Integer>, Set<Integer>> patternLeaves){
		int size = -1;
		int acqLeaves = 0;
		int PCSLeaves = 0;
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(pattern.size()>size) size = pattern.size();
		}
		Set<Set<Integer>> ACQPattern = new HashSet<Set<Integer>>();
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(pattern.size()==size) ACQPattern.add(pattern);
		}
		
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(ACQPattern.contains(pattern)) {
				acqLeaves+=patternLeaves.get(pattern).size();
				PCSLeaves+=patternLeaves.get(pattern).size();
			}else{
				PCSLeaves+=patternLeaves.get(pattern).size();
			}
		}
		int aver[]= new int[4];
		aver[0] =  acqLeaves;
		aver[1] =  PCSLeaves;
		aver[2] = ACQPattern.size();
		aver[3] = patternLeaves.size();
		return aver;
		
	}
	
	public int[] getNumbersofItems(Map<Set<Integer>, Set<Integer>> patternLeaves){
		int size = -1;
		int[] itemsNum= new int[2]; 
		int acq = 0;
		int pcs = 0;
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(pattern.size()>size) size = pattern.size();
		}
		Set<Set<Integer>> ACQPattern = new HashSet<Set<Integer>>();
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(pattern.size()==size) ACQPattern.add(pattern);
		}
		
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(ACQPattern.contains(pattern)){
				acq+=pattern.size();
			}
			pcs+=pattern.size();
		}
		itemsNum[0] = acq;
		itemsNum[1] = pcs;
		return itemsNum;
	}
	
	public int[] getweight(Map<Set<Integer>, Set<Integer>> patternLeaves){
		int [] groups = new int[5];
		double size = -1;
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(pattern.size()>size) size = (double) pattern.size();
		}
		Set<Set<Integer>> ACQPattern = new HashSet<Set<Integer>>();
		for(Set<Integer> pattern:patternLeaves.keySet()){
			if(pattern.size()==size) ACQPattern.add(pattern);
		}
		
		for(Set<Integer> pattern:patternLeaves.keySet()){
			double weight = (double)(pattern.size())/size;
			if(weight>0&&weight<=0.2) groups[0]++;
			else if(weight>0.2&&weight<=0.4) groups[1]++;
			else if(weight>0.4&&weight<=0.6) groups[2]++;
			else if(weight>0.6&&weight<=0.8) groups[3]++;
			else if(weight>0.8) groups[4]++;
		}	
		return groups;
	}
	
	public double[] CMF(Map<Set<Integer>, Set<Integer>> patternUsers,int queryId,int[][] nodes){
		double acqSum = 0.0;
		double pcsSum = 0.0;
		int size = 0;
		for(Set<Integer> pattern:patternUsers.keySet()){
			if(pattern.size()>size) size = pattern.size();
		}
		Set<Set<Integer>> ACQPattern = new HashSet<Set<Integer>>();
		for(Set<Integer> pattern:patternUsers.keySet()){
			if(pattern.size()==size) ACQPattern.add(pattern);
		}
		
		for(Set<Integer> pattern:patternUsers.keySet()){
			if(ACQPattern.contains(pattern)){
				Set<Integer> users = patternUsers.get(pattern);
				double count = 0.0;
				double userSize = users.size();
				double sum = 0.0;
				for(int node:nodes[queryId]){
					for(int user:users){
						if(isContains(nodes[user], node)) count++;
					}
				}
				sum += count/userSize;
				acqSum+=sum;
				
			}else{
				Set<Integer> users = patternUsers.get(pattern);
				double count = 0.0;
				double userSize = users.size();
				double sum = 0.0;
				for(int node:nodes[queryId]){
					for(int user:users){
						if(isContains(nodes[user], node)) count++;
					}
				}
				sum += count/userSize;
				pcsSum+=sum;
			}
		}	
		double wqsize = nodes[queryId].length;
		acqSum = (acqSum/ACQPattern.size())/wqsize;
		if((patternUsers.size()-ACQPattern.size())==0.0) pcsSum=0.0;
		else 
			pcsSum = (pcsSum/(patternUsers.size()-ACQPattern.size()))/wqsize;
		double[] result={acqSum,pcsSum};
		return result;
	}
	
	private boolean isContains(int[]nodes, int x){
		boolean isOk = false;
		for(int node:nodes){
			if(node==x) return true;
		}
		return isOk;
	}
	
	
	
	public static void main(String[] args){
		indexBasedQueryEXP exp = new indexBasedQueryEXP();
//		exp.exp(Config.pubMedGraph10,Config.pubMedNode10,Config.pubmedCPtree10,Config.pubMedQueryFile4);
//		exp.exp(Config.pubMedGraph10,Config.pubMedNode10,Config.pubmedCPtree10,Config.pubMedQueryFile5);
//		exp.exp(Config.pubMedGraph10,Config.pubMedNode10,Config.pubmedCPtree10,Config.pubMedQueryFile6);
//		exp.exp(Config.pubMedGraph10,Config.pubMedNode10,Config.pubmedCPtree10,Config.pubMedQueryFile7);
//		exp.exp(Config.pubMedGraph10,Config.pubMedNode10,Config.pubmedCPtree10,Config.pubMedQueryFile8);


		
		
	}
	
	
}
