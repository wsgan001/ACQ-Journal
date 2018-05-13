package prep;

import java.io.*;
import java.util.Random;

import config.Config;
import algorithm.DataReader;

public class deleteEdges {

	String graphFile = null;
	String nodeFile = null;
 	
	public deleteEdges(String graphFile,String nodeFile){
		this.graphFile = graphFile;
		this.nodeFile = nodeFile;
	}
	
	public void deleteEdges(double proportion,String outFile){
		DataReader dataReader = new DataReader(graphFile,nodeFile);
		int[][] graph = dataReader.readGraph();
		int edgeNum = (int) (dataReader.getEdgeNum()- dataReader.getEdgeNum()*proportion);
		int count = 0;		
		Random random = new Random();
		System.out.println(edgeNum);
		while(count<edgeNum){
			int vertex = random.nextInt(graph.length-1)+1; 
			int[] list1 = graph[vertex];
			if(list1.length<=1) {
				continue;
			}
			int anotherVertex = list1[random.nextInt(list1.length)];
			int[] list2 = graph[anotherVertex];
			
			int[] newList1 = new int[list1.length-1];
			int[] newList2 = new int[list2.length-1];
			int idx = 0;
			
			for(int x:list1){
				if(x!=anotherVertex) {
					newList1[idx] = x;
					idx++;
				}
			} 
			
			idx=0;
			for(int x:list2){
				if(x!=vertex) {
					newList2[idx] = x;
					idx++;
				}
			} 
			graph[vertex] = newList1;
			graph[anotherVertex] = newList2;
			
			count++;
		}
		
		try {
			BufferedWriter std = new BufferedWriter(new FileWriter(outFile));
			for(int i=1;i<graph.length;i++){
				String line = "";
				for(int x:graph[i]) line+=" "+x;
				std.write(i+"\t"+line.trim());
				std.newLine();
			}
			std.flush();
			std.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
		
		
	public static void main(String[] args){
		
//		deleteEdges delete = new deleteEdges(Config.FlickrGraph,Config.FlickrNode1);
//		delete.deleteEdges(0.5,Config.FlickrDataWorkSpace+"newGraph.txt");
		DataReader datareader = new DataReader(Config.FlickrDataWorkSpace+"newGraph.txt",Config.FlickrNode1);
		datareader.readGraph();
		
	}
	
	
}
