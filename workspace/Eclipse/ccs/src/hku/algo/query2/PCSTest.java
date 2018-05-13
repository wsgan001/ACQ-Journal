package hku.algo.query2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hku.Config;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;
import hku.util.Log;

public class PCSTest {

	public void exp(String graph,String node,String queryFile){
		Config.k=6;
		double time1= 0.0;
		AdvancedIndex index = new AdvancedIndex(graph,node);
		TNode root = index.build();
		int core[] = index.getCore();
		List<Integer> queryList = readQueryFile(queryFile);
		DecRevision decr = new DecRevision(index.graph, index.nodes, root, core, null);
		int count = 0;
		for(int x:queryList){
			count++;
			long time= System.nanoTime();
			decr.query(x);
			time1+=System.nanoTime()-time;
			if(count%10==0) Log.log("acq query breaker: "+time1/1000/count+"\n");
			
		}
		Log.log("acq query time: "+time1/1000/queryList.size()+"\n");
	}
	
	
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
	
	
	public static void main(String[] args){
		PCSTest eTest = new PCSTest();
	
		
	}
	
}
