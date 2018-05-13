package prep;

import java.io.BufferedWriter;
import java.io.FileWriter;

import config.Config;
import algorithm.DataReader;

public class SubPtreeData {
	private String graphFile = null;
	private String nodeFile = null;
	private int[][] nodes;
	
	public SubPtreeData(String graphFile, String nodeFile){
		this.graphFile = graphFile;
		this.nodeFile = nodeFile;
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		this.nodes = dataReader.readNodes();
	}
	
	public void createAll(){
		create(20);
		create(40);
		create(60);
		create(80);
		
	}
	
	
	private void create(int percentage){
		//create node file
		try{
			BufferedWriter stdout = new BufferedWriter(new FileWriter(nodeFile + "-only-" + percentage));
			for(int i = 1;i < nodes.length;i ++){
				int kws[] = nodes[i];
				int kwLen = kws.length - 1;
				stdout.write(i + "\t");
				
				int topLen = (int)(kwLen * percentage * 0.01);
				for(int j = 0;j <= topLen;j ++){
					stdout.write(nodes[i][j] + " ");
				}
				stdout.newLine();
			}
			stdout.flush();
			stdout.close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	
	public static void main(String[] args) {
		SubPtreeData data = new SubPtreeData(Config.pubMedGraph, Config.pubMedNode);
		data.createAll();
		
		data = new SubPtreeData(Config.ACMDLGraph, Config.ACMDLNode);
		data.createAll();
		
		data = new SubPtreeData(Config.FlickrGraph, Config.FlickrNode1);
		data.createAll();
		
		data = new SubPtreeData(Config.dblpGraph, Config.dblpNode1);
		data.createAll();
		
		
	}
	
}