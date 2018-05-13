package hku.prep.dbpedia;

import hku.Config;
import hku.algo.DataReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * @author fangyixiang
 * @date Nov 6, 2015
 * Due to the memory limitation, we have to cut some keywords
 */
class Pair{
	public String word = null;
	public int value = 0;
}
public class FreqKwsCut {
	
	public static void main(String args[]){
		String graphFile = Config.dbpediaGraph;
		String nodeFile = Config.dbpediaNode + "-org";
		DataReader dataReader = new DataReader(graphFile, nodeFile);
		String nodes[][] = dataReader.readNode();
		
		Map<String, Integer> freqMap = new HashMap<String, Integer>();
		for(int i = 1;i < nodes.length;i ++){
			if(nodes[i] == null)   System.out.println("i:" + i);
			for(int j = 1;j < nodes[i].length;j ++){
				String word = nodes[i][j];
				if(freqMap.containsKey(word)){
					freqMap.put(word, freqMap.get(word) + 1);
				}else{
					freqMap.put(word, 1);
				}
			}
		}
		
		Comparator<Pair> OrderIsdn =  new Comparator<Pair>(){  
            public int compare(Pair o1, Pair o2) {  
                if(o2.value > o1.value){
                	return 1;
                }else if(o2.value < o1.value){
                	return -1;
                }else{
                	return 0;
                }
            }  
        };  
        Queue<Pair> priorityQueue =  new PriorityQueue<Pair>(11, OrderIsdn);
		
        for(Map.Entry<String, Integer> entry:freqMap.entrySet()){
        	Pair p = new Pair();
        	p.word = entry.getKey();
        	p.value = entry.getValue();
        	priorityQueue.add(p);
        }
        
        int threshold = (int)(freqMap.size() * 0.5);
        Set<String> wordSet = new HashSet<String>();
        while(priorityQueue.size() > 0){
        	Pair p = priorityQueue.poll();
        	wordSet.add(p.word);
        	if(wordSet.size() >= threshold)   break;
        }
        System.out.println("map.size:" + freqMap.size() + " wordSet.size:" + wordSet.size());
        
        try{
        	BufferedWriter stdout = new BufferedWriter(new FileWriter(Config.dbpediaNode));
        	int countAll = 0;
        	for(int i = 1;i < nodes.length;i ++){
        		stdout.write(i + "\t" + nodes[i][0]);
        		for(int j = 1;j < nodes[i].length;j ++){
        			String word = nodes[i][j];
        			if(wordSet.contains(word)){
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
