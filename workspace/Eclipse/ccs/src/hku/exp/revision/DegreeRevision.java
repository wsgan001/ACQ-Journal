package hku.exp.revision;

import hku.Config;
import hku.algo.DataReader;

import java.util.HashSet;
import java.util.Set;

public class DegreeRevision {

	public void handle(){
		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		Set<Integer> set = new HashSet<Integer>();
		set.add(15238);//jiawei han
		
		set.add(15386);//jian pei
//		set.add(1778);//jeffrey xu yu
//		set.add(17539);//guozhu dong
		set.add(18115);//jae-gil lee
//		set.add(11276);//jianyong wang
		set.add(16357);//jiong yang
//		set.add(17139);//wei wang
		
		set.add(15765);//[jiajun bu]
		set.add(15793);//chengqi zhang
		
		set.add(15418);//wen-chih peng
		set.add(19244);//suh-yin lee
		
		set.add(15743);//[dik lun lee]
		set.add(15908);//[wilfred ng]
		
		int sum = 0;
		for(int id:set){
			for(int i = 0;i < graph[id].length;i ++){
				int nghId = graph[id][i];
				if(set.contains(nghId)){
					System.out.println(nodes[id][0] + "-" + nodes[nghId][0]);
					sum += 1;
				}
			}
		}
		System.out.println("average degree: " + sum * 1.0 / set.size());
	}
	
	public static void main(String args[]){
		DegreeRevision dr = new DegreeRevision();
		dr.handle();
	}
}
