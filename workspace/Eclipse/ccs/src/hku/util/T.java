package hku.util;

import hku.algo.KCore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
/**
 * @author fangyixiang
 * @date Sep 8, 2015
 */
class Pair{
	public int id;
	public double dist;
	public Pair(int id, double dist){   this.id = id;   this.dist = dist;}
}
public class T {

	public static void main1(String[] args) {
		int graph[][] = new int[8][];
		int a1[] = {4};		graph[1] = a1;
		int a2[] = {4};			graph[2] = a2;
		int a3[] = {4};			graph[3] = a3;
		int a4[] = {1, 2, 3};	graph[4] = a4;
		int a5[] = {6, 7};		graph[5] = a5;
		int a6[] = {5, 7};			graph[6] = a6;
		int a7[] = {5, 6};			graph[7] = a7;
		
		
		KCore kcore = new KCore(graph);
		int core[] = kcore.decompose();
		int reverse[] = kcore.obtainReverseCoreArr();
		for(int i = 1;i < graph.length;i ++){
			System.out.println(reverse[i] + " " + core[reverse[i]]);
		}
	}

	public static void main2(String args[]){
		Comparator<Pair> OrderIsdn =  new Comparator<Pair>(){  
            public int compare(Pair o1, Pair o2) {  
                // TODO Auto-generated method stub
            	if(o1.dist <= o2.dist){
            		return 1;
            	}else{
            		return -1;
            	}
            }              
        };
        
        Queue<Pair> queue =  new PriorityQueue<Pair>(7, OrderIsdn);  
        queue.add(new Pair(1, 80));
        queue.add(new Pair(2, 9));
        queue.add(new Pair(3, 8));
        System.out.println(queue.poll().id);
	}
	
	public static void main(String args[]){
		List<Set<Integer>> list = new ArrayList<Set<Integer>>();
//		list.add(null);
		System.out.println(list.size());
	}
}
