package hku.variant.variant4;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.algo.online.BasicG;
import hku.algo.online.BasicW;
import hku.algo.query2.Dec;
import hku.algo.query2.DecRevision;
import hku.algo.query2.DecShare;
import hku.variant.variant4.*;



public class BasicV3Test {


public static void main(String []args){
	
//	DataReader dataReader=new DataReader(Config.flickrGraph, Config.flickrNode);
	DataReader dataReader=new DataReader(Config.dblpGraph, Config.dblpNode);
	int[][] g=dataReader.readGraph();
	String[][] n=dataReader.readNode();
	
	Config.k=4;
	int queryset[]={152532};//152532:jim gary 16704:Gerhard Weikum
	String[] kwStrings={"art","research","test","data" ,"management","system","transaction"};
		
	Long time1=System.nanoTime();
	BasicGV3_V2  basicGV3_V2=new BasicGV3_V2(n, g);
	basicGV3_V2.query(kwStrings, queryset);
	Long time2=System.nanoTime()-time1;
	System.out.println(time2/1000000);
	basicGV3_V2.testOutput();
	
//	BasicWV3_V2 basicWV3_V2=new BasicWV3_V2(n, g);
//	Long time3=System.nanoTime();
//	basicWV3_V2.query(queryset,kwStrings);
//	basicWV3_V2.testOutput();
//	Long time4=System.nanoTime()-time3;
//	System.out.println(time4/1000000);
//	
//	AdvancedIndex advancedIndex=new AdvancedIndex(g, n);
//	TNode root=advancedIndex.build();
//	SW_V2 s1=new SW_V2(n,g,root);
//	Long time5=System.nanoTime();
//	s1.query(queryset, kwStrings);
//	Long time6=System.nanoTime()-time5;
//	System.out.println(time6/1000000);
//	s1.testOutput();
}
}
