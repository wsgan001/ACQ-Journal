package EXP;

import java.io.*;
import java.util.*;

import algorithm.CPTreeReader;
import algorithm.ProfiledTree.PNode;
import algorithm.basic.BFS;
import algorithm.basic.DFS;
import algorithm.kwIndex.KWTree;
import algorithm.kwIndex.Query1_margin.Query1;
import algorithm.kwIndex.Query2.Query2_Inc;
import config.Config;
import config.Log;

public class efficiencyEXP {
	
	public List<Integer> readQueryFile(String queryFile){
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
	
	//compare Inc with DFS
	public void expBFS(int k,String graphFile,String nodeFile,String CPtreeFile,String queryFile){
//		Config.k = Integer.parseInt(queryFile.substring(queryFile.length()-5,queryFile.length()-4));
		Config.k = k;
		CPTreeReader reader = new CPTreeReader(CPtreeFile);
		Log.log("now exp BFS..."+" k is: "+Config.k+"  graph is: "+graphFile+"\n");
		
				
		List<Integer> queryList = readQueryFile(queryFile);
		long time2 = 0;
		int count= 1;
		for(int q:queryList){
			long time1 = System.nanoTime();
			BFS bfs = new BFS(graphFile,nodeFile,reader.loadCPTree());
			bfs.query(q);
			time2+=System.nanoTime()-time1;
			count++;
			if(count%10==0) Log.log("log breaker current count: "+count+" bfs time cost: "+time2/1000000/count+"\n");
		}
		Log.log("now query count: "+count+" bfs time cost: "+time2/1000000/queryList.size()+"\n");
			
	}
	
	private KWTree buildKWtree(String graphFile,String nodeFile,String CPtreeFile){
//		Log.log("  graph is: "+graphFile+"\n");
		CPTreeReader reader = new CPTreeReader(CPtreeFile);
		CPTreeReader cpReader = new CPTreeReader(CPtreeFile);
		PNode root=cpReader.loadCPtreeRoot();
		KWTree kwTree = new KWTree(graphFile, nodeFile, root);
		kwTree.build();
		return kwTree;
	}
	
	public void expMargin(int k,KWTree kwTree,String queryFile){
		Config.k = k;
		Log.log("k is: "+Config.k+"\n");
		Query1 query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		
		List<Integer> queryList = readQueryFile(queryFile);
		long time4 = 0;
		for(int q:queryList){
			System.out.println("now query: "+q);
			long time3= System.nanoTime();
			query1.query(q,3);
			time4+=System.nanoTime()-time3;
		}
		Log.log("margin-based query time cost: "+time4/1000/queryList.size()+"\n");
		
	}
	
	
	public void expIncre(int k,KWTree kwTree,String queryFile){
		Config.k = k;
 		Query2_Inc query1 = new Query2_Inc(kwTree.graph,kwTree.getHeadList());
		
		List<Integer> queryList = readQueryFile(queryFile);
		long time4 = 0;
		int count=1;
		for(int q:queryList){
		
			System.out.println("now query: "+q);
			long time3= System.nanoTime();
			query1.query(q);
			time4+=System.nanoTime()-time3;
			if(count%10==0) Log.log("log breaker current count: "+count+" bfs time cost: "+time4/1000000/count+"\n");
			count++;

		}
		Log.log("Incre-based time cost: "+time4/1000000/queryList.size()+"\n");
//		Log.log("bfs time cost: "+time2/1000000+" margin-based time cost: "+time4/1000000);
	}

	
	public void expFIndInitialCut(int k,KWTree kwTree,String queryFile,int breaker){
		Config.k = k;
		Log.log("k is: "+Config.k+"\n");
		Query1 query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		
		List<Integer> queryList = readQueryFile(queryFile);
		long time1 = 0;
		long time2 = 0;
		long time3 = 0;
		int count=1;
		
		for(int q:queryList){
			if(count == breaker) return;
			System.out.println("now query: "+q);
			long time =System.nanoTime();
			query1.queryInc(q);
			time1+=System.nanoTime()-time;
			
			time =System.nanoTime();
			query1.queryDec(q);
			time2+=System.nanoTime()-time;
			
			time =System.nanoTime();
			query1.queryPath(q);
			time3+=System.nanoTime()-time;
			
			if(count%10==0) 
				Log.log("breaker query times: "+count+ "  I: "+time1/count+ "  D: "+time2/count+"  P: "+time3/count);
			
			count++;
		}
		int size= queryList.size();
		Log.log("I: "+time1/1000/size+ "  D: "+time2/1000/size+"  P: "+time3/1000/size);
	}
	
	
	public void expScalabVertice(String graphFile,String nodeFile,String CPtreeFile,String queryFile,int breaker){
		Config.k = 6;
		
		List<Integer> queryList = readQueryFile(queryFile+"-20");
//		List<Integer> queryList = readQueryFile(queryFile+"Scal");

		KWTree kwTree = buildKWtree(graphFile+"-20",nodeFile+"-20",CPtreeFile);
		Query1 query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		long time20 = 0;
		int count=1;
		for(int q:queryList){
			System.out.println("20 now query: "+q);
			long time =System.nanoTime();
			query1.query(q,3);
			time20+=System.nanoTime()-time;
			if(count == breaker) break;
			count++;
		}
		Log.log("20: "+time20/1000/queryList.size()+"\n");
		queryList = readQueryFile(queryFile+"-40");
//		queryList = readQueryFile(queryFile+"Scal");

		kwTree = buildKWtree(graphFile+"-40",nodeFile+"-40",CPtreeFile);
		query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		long time40 = 0;
		count=1;
		for(int q:queryList){
			System.out.println(" 40 now query: "+q);
			long time =System.nanoTime();
			query1.query(q,3);
			time40+=System.nanoTime()-time;
			if(count == breaker) break;
			count++;
		}
		Log.log("40: "+time40/1000/queryList.size()+"\n");

		queryList = readQueryFile(queryFile+"-60");
//		queryList = readQueryFile(queryFile+"Scal");

		kwTree = buildKWtree(graphFile+"-60",nodeFile+"-60",CPtreeFile);
		query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		long time60 = 0;
		count=1;
		for(int q:queryList){
			System.out.println(" 60 now query: "+q);
			long time =System.nanoTime();
			query1.query(q,3);
			time60+=System.nanoTime()-time;
			if(count == breaker) break;
			count++;
		}
		Log.log("60: "+time60/1000/queryList.size()+"\n");

		queryList = readQueryFile(queryFile+"-80");
//		queryList = readQueryFile(queryFile+"Scal");

		kwTree = buildKWtree(graphFile+"-80",nodeFile+"-80",CPtreeFile);
		query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		long time80 = 0;
		count=1;
		for(int q:queryList){
			System.out.println("80 now query: "+q);
			long time =System.nanoTime();
			query1.query(q,3);
			time80+=System.nanoTime()-time;
			if(count == breaker) break;
			count++;
		}
		Log.log("80: "+time80/1000/queryList.size()+"\n");

		queryList = readQueryFile(queryFile+"-100");
//		queryList = readQueryFile(queryFile+"Scal");
		kwTree = buildKWtree(graphFile,nodeFile,CPtreeFile);
		query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		long time100 = 0;
		count=1;
		for(int q:queryList){
			System.out.println("100 now query: "+q);
			long time =System.nanoTime();
			query1.query(q,3);
			time100+=System.nanoTime()-time;
			if(count == breaker) break;
			count++;
		}
		Log.log("100: "+time100/1000/queryList.size()+"\n");	
	}
	
	public void expScalabVerticeInc(String graphFile,String nodeFile,String CPtreeFile,String queryFile,int breaker){
		Config.k = 6;
		
		List<Integer> queryList = readQueryFile(queryFile+"-20");
//		List<Integer> queryList = readQueryFile(queryFile+"Scal");

		KWTree kwTree = buildKWtree(graphFile+"-20",nodeFile+"-20",CPtreeFile);
		Query2_Inc query1 = new Query2_Inc(kwTree.graph,kwTree.getHeadList());
		long time20 = 0;
		int count=1;
//		for(int q:queryList){
//			System.out.println("20 now query: "+q);
//			long time =System.nanoTime();
//			query1.query(q);
//			time20+=System.nanoTime()-time;
//			if(count == breaker) break;
//			count++;
//		}
//		Log.log("20: "+time20/1000/count+"\n");
//		queryList = readQueryFile(queryFile+"-40");
////		queryList = readQueryFile(queryFile+"Scal");
//
//		kwTree = buildKWtree(graphFile+"-40",nodeFile+"-40",CPtreeFile);
//		query1 = new Query2_Inc(kwTree.graph,kwTree.getHeadList());
//		long time40 = 0;
//		count=1;
//		for(int q:queryList){
//			System.out.println(" 40 now query: "+q);
//			long time =System.nanoTime();
//			query1.query(q);
//			time40+=System.nanoTime()-time;
//			if(count == breaker) break;
//			count++;
//		}
//		Log.log("40: "+time40/1000/count+"\n");
//
//		queryList = readQueryFile(queryFile+"-60");
////		queryList = readQueryFile(queryFile+"Scal");
//
//		kwTree = buildKWtree(graphFile+"-60",nodeFile+"-60",CPtreeFile);
//		query1 = new Query2_Inc(kwTree.graph,kwTree.getHeadList());
//		long time60 = 0;
//		count=1;
//		for(int q:queryList){
//			System.out.println(" 60 now query: "+q);
//			long time =System.nanoTime();
//			query1.query(q);
//			time60+=System.nanoTime()-time;
//			if(count == breaker) break;
//			if(count%10==0) Log.log("60: "+time60/1000/count+"\n");
//			count++;
//		}
//		Log.log("60: "+time60/1000/count+"\n");
//
//		queryList = readQueryFile(queryFile+"-80");
////		queryList = readQueryFile(queryFile+"Scal");
//
//		kwTree = buildKWtree(graphFile+"-80",nodeFile+"-80",CPtreeFile);
//		query1 = new Query2_Inc(kwTree.graph,kwTree.getHeadList());
//		long time80 = 0;
//		count=1;
//		for(int q:queryList){
//			System.out.println("80 now query: "+q);
//			long time =System.nanoTime();
//			query1.query(q);
//			time80+=System.nanoTime()-time;
//			if(count == breaker) break;
//			if(count%10==0) Log.log("80: "+time80/1000/count+"\n");
//			count++;
//		}
//		Log.log("80: "+time80/1000/count+"\n");

		queryList = readQueryFile(queryFile+"-100");
//		queryList = readQueryFile(queryFile+"Scal");
		kwTree = buildKWtree(graphFile,nodeFile,CPtreeFile);
		query1 = new Query2_Inc(kwTree.graph,kwTree.getHeadList());
		long time100 = 0;
		count=1;
		for(int q:queryList){
			System.out.println("100 now query: "+q);
			long time =System.nanoTime();
			query1.query(q);
			time100+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("100: "+time100/1000/count+"\n");

			count++;
		}
		Log.log("100: "+time100/1000/count+"\n");	
	}
	
	public void expScalaPTreeInc(String graphFile,String nodeFile,String CPtreeFile,String queryFile,int breaker){
		Config.k = 6;
		
		List<Integer> queryList = readQueryFile(queryFile);

		KWTree kwTree = buildKWtree(graphFile,nodeFile+"-only-20",CPtreeFile);
		Query2_Inc query1 = new Query2_Inc(kwTree.graph,kwTree.getHeadList());
		long time20 = 0;
		int count=1;
		for(int q:queryList){
			System.out.println("20 now query: "+q);
			long time =System.nanoTime();
			query1.query(q);
			time20+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("20: "+time20/1000/count+"\n");

			count++;
		}
		Log.log("20: "+time20/1000/count+"\n");
		

		kwTree = buildKWtree(graphFile,nodeFile+"-only-40",CPtreeFile);
		query1 = new Query2_Inc(kwTree.graph,kwTree.getHeadList());
		long time40 = 0;
		count=1;
		for(int q:queryList){
			System.out.println(" 40 now query: "+q);
			long time =System.nanoTime();
			query1.query(q);
			time40+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("40: "+time40/1000/count+"\n");

			count++;
		}
		Log.log("40: "+time40/1000/count+"\n");

		kwTree = buildKWtree(graphFile,nodeFile+"-only-60",CPtreeFile);
		query1 = new Query2_Inc(kwTree.graph,kwTree.getHeadList());
		long time60 = 0;
		count=1;
		for(int q:queryList){
			System.out.println(" 60 now query: "+q);
			long time =System.nanoTime();
			query1.query(q);
			time60+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("60: "+time60/1000/count+"\n");
			count++;
		}
		Log.log("60: "+time60/1000/count+"\n");


		kwTree = buildKWtree(graphFile,nodeFile+"-only-80",CPtreeFile);
		query1 = new Query2_Inc(kwTree.graph,kwTree.getHeadList());
		long time80 = 0;
		count=1;
		for(int q:queryList){
			System.out.println("80 now query: "+q);
			long time =System.nanoTime();
			query1.query(q);
			time80+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("80: "+time80/1000/count+"\n");
			count++;
		}
		Log.log("80: "+time80/1000/count+"\n");
		
		Log.log("20: "+time20/1000/count + "  40: "+time40/1000/count+ " 60: "+time60/1000/count+" 80: "+time80/1000/count );



	}
	
	public void expScalaPTree(int model,String graphFile,String nodeFile,String CPtreeFile,String queryFile,int breaker){
		Config.k = 6;
		
		List<Integer> queryList = readQueryFile(queryFile);

		KWTree kwTree = buildKWtree(graphFile,nodeFile+"-only-20",CPtreeFile);
		Query1 query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		long time20 = 0;
		int count=1;
		for(int q:queryList){
			System.out.println("20 now query: "+q);
			long time =System.nanoTime();
			query1.query(q,model);
			time20+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("20: "+time20/1000/count+"\n");

			count++;
		}
		Log.log("20: "+time20/1000/count+"\n");
		

		kwTree = buildKWtree(graphFile,nodeFile+"-only-40",CPtreeFile);
		query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		long time40 = 0;
		count=1;
		for(int q:queryList){
			System.out.println(" 40 now query: "+q);
			long time =System.nanoTime();
			query1.query(q,model);
			time40+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("40: "+time40/1000/count+"\n");

			count++;
		}
		Log.log("40: "+time40/1000/count+"\n");

		kwTree = buildKWtree(graphFile,nodeFile+"-only-60",CPtreeFile);
		query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		long time60 = 0;
		count=1;
		for(int q:queryList){
			System.out.println(" 60 now query: "+q);
			long time =System.nanoTime();
			query1.query(q,model);
			time60+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("60: "+time60/1000/count+"\n");
			count++;
		}
		Log.log("60: "+time60/1000/count+"\n");


		kwTree = buildKWtree(graphFile,nodeFile+"-only-80",CPtreeFile);
		query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		long time80 = 0;
		count=1;
		for(int q:queryList){
			System.out.println("80 now query: "+q);
			long time =System.nanoTime();
			query1.query(q,model);
			time80+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("80: "+time80/1000/count+"\n");
			count++;
		}
		Log.log("80: "+time80/1000/count+"\n");
	}
	
	public void expPTreeScala(String graphFile,String nodeFile,String CPtreeFile,String queryFile,int breaker){
		expScalaPTree(1,graphFile,nodeFile,CPtreeFile,queryFile,breaker);
		expScalaPTree(2,graphFile,nodeFile,CPtreeFile,queryFile,breaker);
		expScalaPTree(3,graphFile,nodeFile,CPtreeFile,queryFile,breaker);
		expScalaPTreeInc(graphFile,nodeFile,CPtreeFile,queryFile,breaker);	
	}

	public void expIndex(String graphFile,String nodeFile,String CPtreeFile,int times){
		 Log.log("  graph is: "+graphFile+"\n");
		long time1=0;
		long time2=0;
		long time3=0;
		long time4=0;
		long time5=0;
		long time6=0;
		long time7=0;
		long time8=0;
		long time9=0;
		for(int i = 0;i<times;i++){
			long time =System.nanoTime();
			buildKWtree(graphFile+"-20",nodeFile+"-20",CPtreeFile);
			time1+=System.nanoTime()-time;
			
			time=System.nanoTime();
			buildKWtree(graphFile+"-40",nodeFile+"-40",CPtreeFile);
			time2+=System.nanoTime()-time;
			
			time=System.nanoTime();
			buildKWtree(graphFile+"-60",nodeFile+"-60",CPtreeFile);
			time3+=System.nanoTime()-time;
			
			time=System.nanoTime();
			buildKWtree(graphFile+"-80",nodeFile+"-80",CPtreeFile);
			time4+=System.nanoTime()-time;
			
			time=System.nanoTime();
			buildKWtree(graphFile,nodeFile+"-only-20",CPtreeFile);
			time5+=System.nanoTime()-time;
			
			time=System.nanoTime();
			buildKWtree(graphFile,nodeFile+"-only-40",CPtreeFile);
			time6+=System.nanoTime()-time;
			
			time=System.nanoTime();
			buildKWtree(graphFile,nodeFile+"-only-60",CPtreeFile);
			time7+=System.nanoTime()-time;
			
			time=System.nanoTime();
			buildKWtree(graphFile,nodeFile+"-only-80",CPtreeFile);
			time8+=System.nanoTime()-time;

			time=System.nanoTime();
			buildKWtree(graphFile,nodeFile,CPtreeFile);
			time9+=System.nanoTime()-time;
		}
		
		Log.log("vertex scala. 20: "+time1/1000/times+" 40: "+time2/1000/times+" 60: "+time3/1000/times+" 80: "+time4/1000/times+"\n");
		Log.log("ptree scala. 20: "+time5/1000/times+" 40: "+time6/1000/times+" 60: "+time7/1000/times+" 80: "+time8/1000/times+"\n");
		Log.log("complete graph: "+time9/1000/times);
	}
	
	public void expCPtree(String graphFile,String nodeFile,String CPtreeFile,int times){
		Log.log("  graph is: "+graphFile+"\n");
		long time1=0;
		long time2=0;
		long time3=0;
		long time4=0;
		long time5=0;
		for(int i = 0;i<times;i++){
			long time =System.nanoTime();
			buildKWtree(graphFile,nodeFile,CPtreeFile+"-20");
			time1+=System.nanoTime()-time;
			
			time=System.nanoTime();
			buildKWtree(graphFile,nodeFile,CPtreeFile+"-40");
			time2+=System.nanoTime()-time;
			
			time=System.nanoTime();
			buildKWtree(graphFile,nodeFile,CPtreeFile+"-60");
			time3+=System.nanoTime()-time;
			
			time=System.nanoTime();
			buildKWtree(graphFile,nodeFile,CPtreeFile+"-80");
			time4+=System.nanoTime()-time;
			
			time=System.nanoTime();
			buildKWtree(graphFile,nodeFile,CPtreeFile);
			time5+=System.nanoTime()-time;
			
		}
		
		Log.log("cpTree frac. 20: "+time1/1000/times+" 40: "+time2/1000/times+" 60: "+time3/1000/times+" 80: "+time4/1000/times+"\n");
		Log.log("complete CPtree: "+time5/1000/times);
	}	
	
	
	public void Query(String graphFile,String nodeFile,String CPtreeFile,String queryFile,int breaker){
		Config.k = 6;
		List<Integer> queryList = readQueryFile(queryFile);

		KWTree kwTree = buildKWtree(graphFile,nodeFile,CPtreeFile);
		Query1 query1 = new Query1(kwTree.graph,kwTree.getHeadList());
		long time20 = 0;
		int count=1;
		for(int q:queryList){
			long time =System.nanoTime();
			query1.query(q,1);
			time20+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("ADV_I: "+time20/1000/count+"\n");
			count++;
		}
		

		long time40 = 0;
		count=1;
		for(int q:queryList){
			long time =System.nanoTime();
			query1.query(q,2);
			time40+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("ADV_D: "+time40/1000/count+"\n");
			count++;
		}
		
		
		long time60 = 0;
		count=1;
		for(int q:queryList){
			long time =System.nanoTime();
			query1.query(q,3);
			time60+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("ADV_P: "+time60/1000/count+"\n");
			count++;
		}

		Query2_Inc query2= new Query2_Inc(kwTree.graph,kwTree.getHeadList());
		long time80 = 0;
		count=1;
		for(int q:queryList){
			long time =System.nanoTime();
			query2.query(q);
			time80+=System.nanoTime()-time;
			if(count == breaker) break;
			if(count%10==0) Log.log("INC: "+time80/1000/count+"\n");
			count++;
		}
		
		Log.log("graph: "+graphFile+" CPtree frac:"+CPtreeFile+"\n");
		Log.log("adv_I: "+time20/1000/count+" adv_D: "+time40/1000/count+" adv_P: "+time60/1000/count+" Inc: "+time80/1000/count+"\n\n");
	}
	
	
	public void expCPTreeQuery(String graphFile,String nodeFile,String CPtreeFile,String queryFile,int breaker){
//		Query(graphFile,nodeFile,CPtreeFile+"-20",queryFile,breaker);
//		Query(graphFile,nodeFile,CPtreeFile+"-40",queryFile,breaker);
//		Query(graphFile,nodeFile,CPtreeFile+"-60",queryFile,breaker);
//		Query(graphFile,nodeFile,CPtreeFile+"-80",queryFile,breaker);
		Query(graphFile,nodeFile,CPtreeFile,queryFile,breaker);

	}
	
	public static void main(String[] args){
		
		efficiencyEXP exp = new efficiencyEXP();
		exp.expCPTreeQuery(Config.ACMDLGraph, Config.ACMDLNode, Config.ACMDLCPtree,Config.acmccsDataWorkSpace+"query100.txt",41);
		exp.expCPTreeQuery(Config.dblpGraph, Config.dblpNode1, Config.DBLPCPTree,Config.DBLPDataWorkSpace+"query100.txt", 41);
//		exp.expCPtree(Config.ACMDLGraph, Config.ACMDLNode, Config.ACMDLCPtree,10);
//		exp.expCPtree(Config.dblpGraph, Config.dblpNode1, Config.DBLPCPTree,2);
//		exp.expIndex(Config.ACMDLGraph, Config.ACMDLNode, Config.ACMDLCPtree,10);
//		exp.expIndex(Config.dblpGraph, Config.dblpNode1, Config.DBLPCPTree,2);
//		exp.expScalabVerticeInc(Config.ACMDLGraph, Config.ACMDLNode, Config.ACMDLCPtree,Config.acmccsDataWorkSpace+"query", 101);
//		exp.expScalabVerticeInc(Config.dblpGraph, Config.dblpNode1, Config.DBLPCPTree,Config.DBLPDataWorkSpace+"query", 51);
//		exp.expScalabVertice(Config.ACMDLGraph, Config.ACMDLNode, Config.ACMDLCPtree,Config.acmccsDataWorkSpace+"query", 101);
//		exp.expScalabVertice(Config.pubMedGraph,Config.pubMedNode,Config.pubmedCPtree,Config.pubMedDataWorkSpace+"query",101);
//		exp.expScalabVertice(Config.FlickrGraph, Config.FlickrNode1, Config.FlickrCPTree, Config.FlickrDataWorkSpace+"query", 101);

//		exp.expPTreeScala(Config.ACMDLGraph, Config.ACMDLNode, Config.ACMDLCPtree,Config.acmccsDataWorkSpace+"query100.txt", 51);
//		exp.expPTreeScala(Config.dblpGraph, Config.dblpNode1, Config.DBLPCPTree,Config.DBLPDataWorkSpace+"query100.txt",51);
		
//		KWTree kwtree =null;
//		kwtree = exp.buildKWtree(Config.ACMDLGraph,Config.ACMDLNode,Config.ACMDLCPtree);
////		exp.expFIndInitialCut(4,kwtree,Config.acmccsDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(5,kwtree,Config.acmccsDataWorkSpace+"query100.txt",30);
//		exp.expFIndInitialCut(6,kwtree,Config.acmccsDataWorkSpace+"query100.txt",30);
//		exp.expFIndInitialCut(7,kwtree,Config.acmccsDataWorkSpace+"query100.txt",30);
//		exp.expFIndInitialCut(8,kwtree,Config.acmccsDataWorkSpace+"query100.txt",30);
//		exp.expMargin(4,kwtree,Config.acmccsDataWorkSpace+"queryEfficiencyFile4.txt");
//		exp.expMargin(5,kwtree,Config.acmccsDataWorkSpace+"queryEfficiencyFile5.txt");
//		exp.expMargin(6,kwtree,Config.acmccsDataWorkSpace+"queryEfficiencyFile6.txt");
//		exp.expMargin(7,kwtree,Config.acmccsDataWorkSpace+"queryEfficiencyFile7.txt");
//		exp.expMargin(8,kwtree,Config.acmccsDataWorkSpace+"queryEfficiencyFile8.txt");
//		exp.expMargin(4,kwtree,Config.acmccsDataWorkSpace+"query100.txt");
//		exp.expMargin(5,kwtree,Config.acmccsDataWorkSpace+"query100.txt");
//		exp.expMargin(6,kwtree,Config.acmccsDataWorkSpace+"query100.txt");
//		exp.expMargin(7,kwtree,Config.acmccsDataWorkSpace+"query100.txt");
//		exp.expMargin(8,kwtree,Config.acmccsDataWorkSpace+"query100.txt");
//		exp.expIncre(4,kwtree,Config.acmccsDataWorkSpace+"query100.txt");
//		exp.expIncre(5,kwtree,Config.acmccsDataWorkSpace+"query100.txt");
//		exp.expIncre(6,kwtree,Config.acmccsDataWorkSpace+"query100.txt");
//		exp.expIncre(7,kwtree,Config.acmccsDataWorkSpace+"query100.txt");
//		exp.expIncre(8,kwtree,Config.acmccsDataWorkSpace+"query100.txt");
//		
//		kwtree = exp.buildKWtree(Config.FlickrGraph,Config.FlickrNode1,Config.FlickrCPTree);
////		exp.expMargin(4,kwtree,Config.FlickrDataWorkSpace+"queryEfficiencyFile4.txt");
////		exp.expMargin(5,kwtree,Config.FlickrDataWorkSpace+"queryEfficiencyFile5.txt");
////		exp.expMargin(6,kwtree,Config.FlickrDataWorkSpace+"queryEfficiencyFile6.txt");
////		exp.expMargin(7,kwtree,Config.FlickrDataWorkSpace+"queryEfficiencyFile7.txt");
////		exp.expMargin(8,kwtree,Config.FlickrDataWorkSpace+"queryEfficiencyFile8.txt");
//		exp.expMargin(4,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expMargin(5,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expMargin(6,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expMargin(7,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expMargin(8,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expIncre(4,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expIncre(5,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expIncre(6,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expIncre(7,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expIncre(8,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(4,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(5,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(6,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(7,kwtree,Config.FlickrDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(8,kwtree,Config.FlickrDataWorkSpace+"query100.txt");

		
		
//				
//		kwtree = exp.buildKWtree(Config.pubMedGraph,Config.pubMedNode,Config.pubmedCPtree);
////		exp.expMargin(4,kwtree,Config.pubMedDataWorkSpace+"queryEfficiencyFile4.txt");
////		exp.expMargin(5,kwtree,Config.pubMedDataWorkSpace+"queryEfficiencyFile5.txt");
////		exp.expMargin(6,kwtree,Config.pubMedDataWorkSpace+"queryEfficiencyFile6.txt");
////		exp.expMargin(7,kwtree,Config.pubMedDataWorkSpace+"queryEfficiencyFile7.txt");
////		exp.expMargin(8,kwtree,Config.pubMedDataWorkSpace+"queryEfficiencyFile8.txt");
//		exp.expMargin(4,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expMargin(5,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expMargin(6,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expMargin(7,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expMargin(8,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expIncre(4,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expIncre(5,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expIncre(6,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expIncre(7,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expIncre(8,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(4,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(5,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(6,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(7,kwtree,Config.pubMedDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(8,kwtree,Config.pubMedDataWorkSpace+"query100.txt");


//		
//		
//		kwtree = exp.buildKWtree(Config.dblpGraph,Config.dblpNode1,Config.DBLPCPTree);
////		exp.expMargin(4,kwtree,Config.DBLPDataWorkSpace+"queryEfficiencyFile4.txt");
////		exp.expMargin(5,kwtree,Config.DBLPDataWorkSpace+"queryEfficiencyFile5.txt");
////		exp.expMargin(6,kwtree,Config.DBLPDataWorkSpace+"queryEfficiencyFile6.txt");
////		exp.expMargin(7,kwtree,Config.DBLPDataWorkSpace+"queryEfficiencyFile7.txt");
////		exp.expMargin(8,kwtree,Config.DBLPDataWorkSpace+"queryEfficiencyFile8.txt");
//		exp.expMargin(4,kwtree,Config.DBLPDataWorkSpace+"query100.txt");
//		exp.expMargin(5,kwtree,Config.DBLPDataWorkSpace+"query100.txt");
//		exp.expMargin(6,kwtree,Config.DBLPDataWorkSpace+"query100.txt");
//		exp.expMargin(7,kwtree,Config.DBLPDataWorkSpace+"query100.txt");
//		exp.expMargin(8,kwtree,Config.DBLPDataWorkSpace+"query100.txt");
//		exp.expIncre(4,kwtree,Config.DBLPDataWorkSpace+"query100.txt");	
//		exp.expIncre(5,kwtree,Config.DBLPDataWorkSpace+"query100.txt");	
//		exp.expIncre(6,kwtree,Config.DBLPDataWorkSpace+"query100.txt");	
//		exp.expIncre(7,kwtree,Config.DBLPDataWorkSpace+"query100.txt");	
//		exp.expIncre(8,kwtree,Config.DBLPDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(4,kwtree,Config.DBLPDataWorkSpace+"query100.txt");
//		exp.expFIndInitialCut(5,kwtree,Config.DBLPDataWorkSpace+"query100.txt",30);
//		exp.expFIndInitialCut(6,kwtree,Config.DBLPDataWorkSpace+"query100.txt",30);
//		exp.expFIndInitialCut(7,kwtree,Config.DBLPDataWorkSpace+"query100.txt",30);
//		exp.expFIndInitialCut(8,kwtree,Config.DBLPDataWorkSpace+"query100.txt",30);

		

		
//			exp.expBFS(4,Config.FlickrGraph,Config.FlickrNode1,Config.FlickrCPTree,Config.FlickrDataWorkSpace+"query100.txt");
//			exp.expBFS(5,Config.FlickrGraph,Config.FlickrNode1,Config.FlickrCPTree,Config.FlickrDataWorkSpace+"query100.txt");
//			exp.expBFS(6,Config.FlickrGraph,Config.FlickrNode1,Config.FlickrCPTree,Config.FlickrDataWorkSpace+"query100.txt");
//			exp.expBFS(7,Config.FlickrGraph,Config.FlickrNode1,Config.FlickrCPTree,Config.FlickrDataWorkSpace+"query100.txt");
//			exp.expBFS(8,Config.FlickrGraph,Config.FlickrNode1,Config.FlickrCPTree,Config.FlickrDataWorkSpace+"query100.txt");
			
//			exp.expBFS(4,Config.pubMedGraph,Config.pubMedNode,Config.pubmedCPtree,Config.pubMedDataWorkSpace+"query100.txt");
//			exp.expBFS(5,Config.pubMedGraph,Config.pubMedNode,Config.pubmedCPtree,Config.pubMedDataWorkSpace+"query100.txt");
//			exp.expBFS(6,Config.pubMedGraph,Config.pubMedNode,Config.pubmedCPtree,Config.pubMedDataWorkSpace+"query100.txt");
//			exp.expBFS(7,Config.pubMedGraph,Config.pubMedNode,Config.pubmedCPtree,Config.pubMedDataWorkSpace+"query100.txt");
//			exp.expBFS(8,Config.pubMedGraph,Config.pubMedNode,Config.pubmedCPtree,Config.pubMedDataWorkSpace+"query100.txt");
			
//			exp.expBFS(5,Config.dblpGraph,Config.dblpNode1,Config.DBLPCPTree,Config.DBLPDataWorkSpace+"query100.txt");
//			exp.expBFS(6,Config.dblpGraph,Config.dblpNode1,Config.DBLPCPTree,Config.DBLPDataWorkSpace+"query100.txt");
//			exp.expBFS(7,Config.dblpGraph,Config.dblpNode1,Config.DBLPCPTree,Config.DBLPDataWorkSpace+"query100.txt");
//			exp.expBFS(8,Config.dblpGraph,Config.dblpNode1,Config.DBLPCPTree,Config.DBLPDataWorkSpace+"query100.txt");

			
		
	}
	
	
	
	
	
}



