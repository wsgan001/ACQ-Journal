package hku.util;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.KCore;
import hku.algo.index.BasicIndex;

import java.util.*;
/**
 * @author fangyixiang
 * @date Sep 9, 2015
 */
public class UserKwsFinder {
	private DataReader dataReader = null;
	private int graph[][] = null;
	private String nodes[][] = null;
	private int core[] = null;

	public UserKwsFinder(int nodeId){
		dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
		graph = dataReader.readGraph();
		nodes = dataReader.readNode();
		
		for(int i = 0;i < nodes[nodeId].length;i ++){
			System.out.println(nodes[nodeId][i]);
		}
	}

	public static void main(String[] args) {
//		int nodeId = 15238;//jiawei han
//		int nodeId = 152532;//jim gray
//		int nodeId = 15857;//reynold cheng
		int nodeId = 16665;//yizhou sun
		
		UserKwsFinder finder = new UserKwsFinder(nodeId);
	}
}
