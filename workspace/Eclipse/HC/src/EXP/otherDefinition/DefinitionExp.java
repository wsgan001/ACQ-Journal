package EXP.otherDefinition;

import java.io.*;
import java.util.*;

public class DefinitionExp {
	private int graph[][]=null;//graph structure;  starting from 1
	private int nodes[][]=null;//the tree nodes of each node; starting from 1

	
	
	public DefinitionExp(int graph[][],int nodes[][]){
		this.graph = graph;
		this.nodes = nodes;
		
	}
	
	
	private void compareDefinitions(String queryFile){
		List<Integer> queryList = readQueryFile(queryFile);
		
		for(int queryId:queryList){
			NumPaths numPaths = new NumPaths(graph, nodes);
			Map<Set<Integer>, Set<Integer>> output1 = numPaths.query(queryId);
			
			TreeSimilarity tSimilarity = new TreeSimilarity(graph, nodes);
			Set<Set<Integer>> output2 = tSimilarity.query(queryId);
		}
		
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
			System.out.println(queryList.size()+"  "+e.getMessage());
		}	
		return queryList;
	}
	
	
	
	
	
	
}