package algorithm;

import java.io.*;

/**
@author chenyankai
@Date	Aug 4, 2017
read the edges and nodes
*/
public class DataReader {
	private String graphFile=null;
	private String nodeFile=null;
	private int userNum=-1;
	private int edgeNum=-1;
	
	
	public DataReader(String graph,String nodes){
		this.graphFile=graph;
		this.nodeFile=nodes;
		try {
			File file=new File(nodes);
			Long fileLength=file.length();
			LineNumberReader rf=new LineNumberReader(new FileReader(file));
			if(rf!=null){
				rf.skip(fileLength);
				userNum=rf.getLineNumber();
			}
			rf.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println(nodes);
		System.out.println("# of the nodes in G: "+userNum);
		
	}
	
	public int[][] readGraph(){
		int edges=0;
		int[][] graph=new int[userNum+1][];
		
		try {
			BufferedReader bReader=new BufferedReader(new FileReader(graphFile));
			
			String line=null;
			while((line=bReader.readLine())!=null){
				String[] s=line.split("\t");
				int userId=Integer.parseInt(s[0]);
				
				if(s.length>1){
					String[] strings=s[1].split(" ");
					graph[userId]=new int[strings.length];
					for(int i=0;i<strings.length;i++){
						graph[userId][i]=Integer.parseInt(strings[i]);
					}
					edges+=graph[userId].length;
				}else{
					graph[userId]=new int[0];
				}
			}
			bReader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("# of edges in G: "+edges);
		System.out.println("the average degree:" + (edges * 1.0 / userNum));
		
		this.edgeNum=edges/2;
		
		return graph;
	}
	
	
	public int[][] readNodes(){
		double len=0.0;
		int[][] nodes=new int[userNum+1][];
		String line=null;
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(nodeFile));
			while((line=bReader.readLine())!=null){
				String userString = line.substring(0, line.indexOf('\t'));
				int userId = Integer.parseInt(userString);
				
				line=line.substring(line.indexOf('\t')+1);
				if(line.length()>0){
					String[] kw = line.trim().split(" ");
					len +=kw.length;
					nodes[userId] = new int[kw.length];
//					nodes[userId][0] = userId;
					
					for(int i=0;i<kw.length;i++){
						nodes[userId][i]=Integer.parseInt(kw[i]);
					}
				}
			}	
			bReader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		System.out.println("the avg # of keywords in each node:" + (len / userNum));
		return nodes;
	}
	
	
}
