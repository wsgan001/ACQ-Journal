package hku.exp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.query2.Dec;

public class CommunitySizeExp {
private int[][] graph=null;
private String[][] nodes=null;
private TNode root=null;
private int[] core=null;


	
public void exp(String gFile,String nFile,String file){
	DataReader dataReader=new DataReader(gFile, nFile);
	this.graph=dataReader.readGraph();
	this.nodes=dataReader.readNode();
	
	int size50=0;
	int size100=0;
	int size200=0;
	int size400=0;
	int sizeOver=0;
	Set<Integer> set1=new HashSet<Integer>();
	
	Config.k=6;
	
	AdvancedIndex index=new AdvancedIndex(graph, nodes);
	TNode root=index.build();
	this.core=index.getCore();
	System.out.println("index construction finished !");

	Dec dec=new Dec(graph, nodes, root);
	List<Integer> list=read(file);
//	dec.query(82713);
	System.out.println(list.size());
	int i=0;
	for(int x:list){
		
		List<Set<Integer>> communities=dec.query(x);
		if(communities==null) System.out.println(i+"       asdasdasdasdf   ");
		if(communities!=null){
			int size=0;
			int count=0;
			for(Set<Integer> set:communities){	
				size+=set.size();
				count++;
			}			
			
				if( count!=0) {
					System.out.println(i++);

					int aver=size/count;//treat as int
					if(aver>=0&& aver<=50) size50++;
					else if(aver>50&&aver<=100) size100++;
					else if(aver>100&& aver<=200) size200++;
					else if(aver>200&& aver<=400) size400++;
					else sizeOver++;
				}
			
		}
	}
	
	System.out.println("community size 50:"+ size50+" 50-100:"+size100+" 200: "+size200+
			" 200-400: "+size400+" over 400: "+sizeOver);
//	System.out.println("?? "+set1.toString());
	
}

private List<Integer> read(String file){
	List<Integer> list=new ArrayList<Integer>();
	try{
		BufferedReader reader=new BufferedReader(new FileReader(file));
		String line=null;
		while((line=reader.readLine())!=null){
			String[] strings=line.split(" ");
			int id=Integer.parseInt(strings[0]);
			list.add(id);
		}
		reader.close();
	}catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	return list;
}


public static void main(String[] a){
	CommunitySizeExp exp=new CommunitySizeExp();
	exp.exp(Config.dblpGraph, Config.dblpNode,Config.dblpquery);

//	exp.exp(Config.flickrGraph, Config.flickrNode,Config.flickrquery);
	
	
}
}
