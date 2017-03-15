package hku.algo.index.edgeUpdate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;

public class GenerateInsrtEdgePrep {
	
	//outpath need to change if transplant to other machine !
	private String outpath="/Users/chenyankai/Desktop/yankai_data/insertEdge/";
	
	
	private boolean isContains(int[] x,int y){
		for(int a:x){
			if (a==y) return true;
		}
		return false;
	}
	
	//get all nodes with x core number 
	private void getCertainNode(TNode root,int coreNumber,List<TNode> list){
		if(root.getCore()==coreNumber) list.add(root);
		else if(root.getCore()<coreNumber){
			for(int i = 0;i < root.getChildList().size();i ++){
				TNode tnode = root.getChildList().get(i);
				getCertainNode(tnode,coreNumber,list);
			}
		}
	}
	
	private List<String> getXCoreEdge(List<TNode> TNodelist,int[][] graph,int[] core ){
		List<String> list=new ArrayList<String>();
		for(TNode node:TNodelist){
			Set<Integer> set=node.getNodeSet();
			for(int x:set){
				Boolean flag=false;
				while(!flag){
					Random random=new Random();
					int y=random.nextInt(graph.length-1)+1;
					if( (core[y]>=core[x])&& !isContains(graph[x],y) ){
						list.add(x+","+y );
						flag=true;
					}
				}
			}
			
		}
		return list;
	}
			
	private void writeXcoreEdge(int coreNumber,int fileSize,String dataSet,int[][] graph,String[][]node){
		AdvancedIndex ad=new AdvancedIndex(graph, node);
		TNode root=ad.build();
		List<TNode> list=new ArrayList<TNode>();
		getCertainNode(root, coreNumber, list);
		List<String> StringList=getXCoreEdge(list,graph,ad.getCore());
		
		try{
			String path=outpath+dataSet+"-"+coreNumber+".txt";
			File file=new File(path);
			BufferedWriter writer=new BufferedWriter(new FileWriter(file));
			for(int i=0;i<fileSize;i++){
				Random random=new Random();
				String string=StringList.get( random.nextInt(StringList.size()) );
				writer.write(string);
				writer.newLine();
			}
			writer.flush();
			writer.close();
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	private void generateEdge(String dataSet,String graphpath,String nodepath,int[] coreList){
		DataReader dataReader=new DataReader(graphpath,nodepath);
		int[][] graph=dataReader.readGraph();
		String[][] node=dataReader.readNode();
		for(int x:coreList) writeXcoreEdge(x, 100, dataSet,graph,node);
	}
	
	public void generate(){
		//dblp
		int[] coreList1={5,10,15,20,25};
		generateEdge("dblp",Config.dblpGraph,Config.dblpNode,coreList1);
		
		//flickr
		int[] coreList2={5,10,15,20,25};
		generateEdge("flickr", Config.flickrGraph, Config.flickrNode, coreList2);
		
		//tencent
//		int[] coreList3={5,10,15,20,25};
//		generateEdge("tencent", Config.tencentGraph, Config.tencentNode, coreList3);
		
		//
//	 	int[] coreList4={5,10,15,20,25};
//		generateEdge("dbpedia", Config.dbpediaGraph, Config.dbpediaNode, coreList4);
		
	}
	
	public static void main(String[] args){
		GenerateInsrtEdgePrep gePrep=new GenerateInsrtEdgePrep();
		gePrep.generate();
		
	}
	
	
	
	
	
}
