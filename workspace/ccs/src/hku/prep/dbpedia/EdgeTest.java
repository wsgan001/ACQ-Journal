package hku.prep.dbpedia;

import hku.Config;
import hku.algo.DataReader;

public class EdgeTest {

	public static void main(String[] args) {
		DataReader dataReader = new DataReader(Config.dbpediaGraph, Config.dbpediaNode);
//		String users[][] = dataReader.readNode();
		int graph[][] = dataReader.readGraph();
		
		for(int i = 1;i <= 10;i ++){
			for(int j = 0;j < graph[i].length;j ++){
				int neighbor = graph[i][j];
				
				int arr[] = graph[neighbor];
				boolean flag = false;
				for(int k = 0;k < arr.length;k ++){
					if(arr[k] == i){
						flag = true;
						break;
					}
				}
				if(flag == false){
					System.out.println("no symmetric i=" + i + " neighbor=" + neighbor);
				}
			}
		}
	}

}
