package hku.variant.variant4;

import java.io.*;
import java.nio.Buffer;
import java.util.*;
import hku.Config;
import hku.algo.DataReader;
import hku.algo.FindCKCore;
import hku.algo.FindKCore;
import hku.algo.KCore;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.query2.Dec;
import hku.exp.util.QueryIdReader;


public class QuerySetGenerator {
	private String graphFile=null;
	private String nodeFile=null;
//	private String queryFile=null;
	private int[][] graph=null;
	private String[][]nodes=null;
	private int core[]=null;
	private TNode root=null;
	private static int MinimalLength=6;
	private static int fileLength=100;
	private List<List<Integer>> staticList=null;
	String outPath="/Users/chenyankai/Desktop/yankai_data/variant4/";
	
	
	/*
	public void handle(String graphFile,String nodeFile,String dataSet){
		Config.k=6;
		this.graphFile = graphFile;
		this.nodeFile = nodeFile;
		DataReader dataReader=new DataReader(graphFile, nodeFile);
		this.graph=dataReader.readGraph();
		this.nodes=dataReader.readNode();
		KCore kCore=new KCore(graph);
		this.core=kCore.decompose();
		
		AdvancedIndex adIndex=new AdvancedIndex(graph, nodes);
		this.root=adIndex.build();
		
			
		List<String> query2List=generateId(2);
		List<String> query3List=generateId(3);
		List<String> query4List=generateId(4);
		List<String> query5List=generateId(5);
		List<String> query6List=generateId(6);
		save(generatekw(query2List), outPath+dataSet+"-Qsize=2.txt");
		save(generatekw(query3List), outPath+dataSet+"-Qsize=3.txt");
		save(generatekw(query4List), outPath+dataSet+"-Qsize=4.txt");
		save(generatekw(query5List), outPath+dataSet+"-Qsize=5.txt");
		save(generatekw(query6List), outPath+dataSet+"-Qsize=6.txt");
	}
	
	private String[] intersect(String[] ar1,String[] ar2){
		List<String> list = new ArrayList<String>(Arrays.asList(ar1));
		list.retainAll(Arrays.asList(ar2));
		String [] out = new String[list.size()];
		for(int i = 0;i<out.length;i++){
			out[i] = list.get(i);
		}
		return out;
	}

	private List<String> generateId(int length){
		List<Set<Integer>> qList=new ArrayList<Set<Integer>>();
		List<String> wrtList=new ArrayList<String>();
		Dec dec=new Dec(graph, nodes, root);
		Random random=new Random();
		while(wrtList.size()<fileLength){
			int queryId=random.nextInt(graph.length-1)+1;
			qList=dec.query(queryId);
			if(qList!=null){	
				for(Set<Integer> singleSet:qList){
					String idString="";
					List<Integer> setInList=new ArrayList<Integer>();
					if(singleSet.size()>=MinimalLength){
						setInList.addAll(singleSet);
						for(int i=0;i<length;i++){
							idString=idString+setInList.get(random.nextInt(setInList.size()-1))+" ";
						}
						wrtList.add(idString);
					}
				}
			}
		}
		return wrtList;
	}
	
	private List<String> generatekw(List<String>idList){
		List<String> wrtList=new ArrayList<String>();
		for(int m=0;m<idList.size();m++){
			String wrtString=idList.get(m).toString();
			String id[]=idList.get(m).toString().split(" ");
			int id0=Integer.parseInt(id[0]);
			String kwString[]=new String[ nodes[id0].length ];
			for(int i=1;i<nodes[id0].length;i++)  kwString[i-1]=nodes[id0][i];
			
			for(String idString:id){
				int idInt=Integer.parseInt(idString);
				kwString=intersect(kwString, nodes[idInt]);
			}
			String outStr='\t'+"";
			int size=0;
			for(String s:kwString){
				if(size<10){
					outStr=outStr+s+" ";
					size++;
				}else break;
			}
			wrtString=wrtString+'\t'+outStr;
			wrtList.add(wrtString);
		}
		return wrtList;
	}
	*/
	
	public void handle(String graphFile,String nodeFile,String dataSet,int kwLength){
//        Config.k=6;
        this.graphFile = graphFile;
        this.nodeFile = nodeFile;
        DataReader dataReader=new DataReader(graphFile, nodeFile);
        this.graph=dataReader.readGraph();
        this.nodes=dataReader.readNode();
        System.out.println("data read completes.");
        KCore kCore=new KCore(graph);
        System.out.println("k core  completes.");
        this.core=kCore.decompose();
        
        AdvancedIndex adIndex=new AdvancedIndex(graph, nodes);
        this.root=adIndex.build();
        System.out.println("tree completes.");
        
            
//        List<String> query2List=generateId(2);
//        List<String> query3List=generateId(3);
//        List<String> query4List=generateId(4);
//        List<String> query5List=generateId(5);
//        List<String> query6List=generateId(6);
        generateId(6,kwLength);
        save(generatekw(2,kwLength), outPath+dataSet+"-Qsize=2.txt");
        save(generatekw(3,kwLength), outPath+dataSet+"-Qsize=3.txt");
        save(generatekw(4,kwLength), outPath+dataSet+"-Qsize=4.txt");
        save(generatekw(5,kwLength), outPath+dataSet+"-Qsize=5.txt");
        save(generatekw(6,kwLength), outPath+dataSet+"-Qsize=6.txt");
    }
    
    private List<String> intersect(List<String> ar1,String[] ar2){
        ar1.retainAll(Arrays.asList(ar2));
        return ar1;
    }

    private void generateId(int length,int kwLength){
        List<Set<Integer>> qList=new ArrayList<Set<Integer>>();
        List<List<Integer>> list=new ArrayList<List<Integer>>();
        Dec dec=new Dec(graph, nodes, root);
        Set<Integer> tmpSet=new HashSet<Integer>();
        int aindex=0;
        while(list.size()<fileLength){
        	 Random random=new Random();
            int queryId=random.nextInt(graph.length-1)+1;
            qList=dec.query(queryId);
            if(qList!=null){    
                for(Set<Integer> singleSet:qList){
                    List<Integer> tmpList=new ArrayList<Integer>();
                    int size=0;
                    List<Integer> setInList=new ArrayList<Integer>();
                    if(singleSet.size()>=MinimalLength){
                    	Set<String> kwPool=new HashSet<String>();
                    	Iterator<Integer> iterator=singleSet.iterator();
                    	int id0=iterator.next();
                    	for(String x:nodes[id0]) kwPool.add(x);
                    	while(iterator.hasNext()){
                    		int id=iterator.next();
                    		Set<String> tmp=new HashSet<String>();
                    		for(String x:nodes[id]) tmp.add(x);
                    		kwPool.retainAll(tmp);
                    	}
                    	
                    	if(kwPool.size()>2&&kwPool.size()<=10){
                        setInList.addAll(singleSet);
                        for(int i=0;i<length;i++){
                            int x=setInList.get(random.nextInt(setInList.size()-1));
                            if(!tmpSet.contains(x)){
                                tmpSet.add(x);
//                                idString=idString+x+" ";
                                tmpList.add(x);
                                size++;
                            }
                        }
                        
                        if(size==length) {
//                            wrtList.add(idString);
                            list.add(tmpList);
                            aindex++;
                            System.out.println(aindex);
                        }
                    }
                    }
                }
                }
            
        }
//        return wrtList;
//        return list;
        System.out.println(list.size());
        staticList=list;
    }
    
    
    
    
    private List<String> generatekw(int length,int kwLength){
        List<String> wrtList=new ArrayList<String>();
       
        for(int m=0;m<staticList.size();m++){
            List<Integer> tmpList=staticList.get(m);
            int id0=tmpList.get(0);
          
            List<String> kwPool=new ArrayList<String>();
            for(int j=1;j<nodes[id0].length;j++) kwPool.add(nodes[id0][j]);
           
            for(int i=1;i<tmpList.size();i++){
            	int id=tmpList.get(i);
            	 kwPool=intersect(kwPool, nodes[id]);
            }
            String[] kwString=new String[kwPool.size()];
            for(int index=0;index<kwPool.size();index++){
            	kwString[index]=kwPool.get(index);
            }
            
//            for(int id:tmpList){
//                kwString=intersect(kwString, nodes[id]);
//            }
            String outStr="";
            int size=0;
            
            for(String s:kwString){
                if(size<kwLength){
                    outStr=outStr+s+" ";
                    size++;
                }else break;
            }
            String wrtString="";
            Set<Integer> tmpSet=new HashSet<Integer>();
            while(tmpSet.size()!=length){
                Random random=new Random();
//                int inputId=random.nextInt(tmpList.get(tmpList.size()));
                int inputId=tmpList.get(random.nextInt(tmpList.size()));
                if(!tmpSet.contains(inputId)){
                    tmpSet.add(inputId);
                    wrtString=wrtString+inputId+" ";
                }
            }
            wrtString=wrtString+'\t'+outStr;
            wrtList.add(wrtString);
        }
        return wrtList;
    }
    
	
	private void save(List<String> qList,String file){
		try {
			BufferedWriter stdout=new BufferedWriter(new FileWriter(file));
			for(int i=0;i<qList.size();i++){
				String str=qList.get(i);
				stdout.write(str);
				stdout.newLine();
			}
			stdout.flush();
			stdout.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void main(String[] args){
		QuerySetGenerator querySetGenerator=new QuerySetGenerator();
		querySetGenerator.handle(Config.dblpGraph, Config.dblpNode,"dblp",10);
		querySetGenerator.handle(Config.flickrGraph, Config.flickrNode,"flickr",10);
//		querySetGenerator.handle(Config.tencentGraph, Config.tencentGraph, "tencent");
//		querySetGenerator.handle(Config.dbpediaGraph, Config.dbpediaGraph, "dbpedia");
	}	
}