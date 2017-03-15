package hku.prep.dbpedia;

import hku.Config;
import hku.algo.DataReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

/**
 * @author fangyixiang
 * @date Nov 6, 2015
 * Due to the memory limitation, we have to cut some keywords
 */
public class RandKwsCut {
	
	public static void main(String args[]){
		String graphFile = Config.dbpediaGraph;
		String nodeFile = Config.dbpediaNode + "-org";
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		String nodes[][] = dataReader.readNode();
		
		
        try{
        	BufferedWriter stdout = new BufferedWriter(new FileWriter(Config.dbpediaNode));
        	int countAll = 0;
        	for(int i = 1;i < nodes.length;i ++){
        		stdout.write(i + "\t" + nodes[i][0]);
        		for(int j = 1;j < nodes[i].length;j ++){
        			String word = nodes[i][j];
        			Random rand = new Random();
        			int value = rand.nextInt(100);
        			if(value < 75){
        				stdout.write(" " + word);
        				countAll += 1;
        			}
        		}
        		stdout.newLine();
        	}
        	stdout.flush();
        	stdout.close();
        	System.out.println(countAll * 1.0 / (nodes.length - 1));
        }catch(Exception e){e.printStackTrace();}
	}
}
