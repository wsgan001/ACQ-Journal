package hku.exp;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.KCore;

public class FractionExp {
private int k=-1;
private int[][] graph=null;
//private String[][] nodes=null;
private int[] core=null;

public FractionExp(int k){
	this.k=k;
}

	public void exp(String graphFile,String nodeFile){
		DataReader dataReader=new DataReader(graphFile, nodeFile);
		this.graph=dataReader.readGraph();
		
		KCore kCore=new KCore(graph);
		this.core=kCore.decompose();
		int count=0;
		for(int x:core){
			if(x>=k) count++;
		}
		
		System.out.println("# of "+"graph:" +graphFile+" k>="+k+": "+count+"; total #: "+(graph.length-1));
		System.out.println("fraction of  is: "+(double)count/(graph.length-1));
		System.out.println();
	}
	
	
	public static void main(String[] a){
		FractionExp fractionExp=new FractionExp(6);
		fractionExp.exp(Config.dblpGraph, Config.dblpNode);
		fractionExp.exp(Config.flickrGraph, Config.flickrNode);
		fractionExp.exp(Config.DflickrGraph, Config.DflickrNode);
		fractionExp.exp(Config.youtubeGraph, Config.youtubeNode);
		
//		fractionExp.exp(Config.dbpediaGraph, Config.dbpediaNode);
//		fractionExp.exp(Config.twitterGraph, Config.twitterNode);
		
	}
	
}
