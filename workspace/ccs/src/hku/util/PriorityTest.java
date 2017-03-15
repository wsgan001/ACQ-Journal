package hku.util;

import hku.Config;

import java.util.Comparator;  
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.PriorityQueue;  
import java.util.Queue;  

/**
 * @author fangyixiang
 * @date Jul 21, 2015
 */

class Cand{
	public int vertexId = -1;
	public int incidence = 0;
	public Cand(int vertexId, int incidence){
		this.vertexId = vertexId;
		this.incidence = incidence;
	}
}

public class PriorityTest {
	
	private double sim(String a[], String b[]){
		Set<String> set = new HashSet<String>();
		for(int i = 1;i < a.length;i ++)   set.add(a[i]);
		
		double share = 0.0;
		for(int i = 1;i < b.length;i ++){
			if(set.contains(b[i])){
				share += 1;
			}else{
				set.add(b[i]);
			}
		}
		
		System.out.println("share:" + share + "  set.size=" + set.size());
		
		return share / set.size();
	}
	
	public static void main1(String[] args) {
		Comparator<Integer> OrderIsdn =  new Comparator<Integer>(){  
            public int compare(Integer o1, Integer o2) {  
                return o2.intValue() - o1.intValue();
            }  
        };  
        Queue<Integer> priorityQueue =  new PriorityQueue<Integer>(11, OrderIsdn);
        priorityQueue.add(1);
        
        priorityQueue.add(4);
        priorityQueue.add(5);
        priorityQueue.add(2);
        priorityQueue.add(3);
        priorityQueue.remove(3);
        
        
        System.out.println(priorityQueue.poll());
        System.out.println(priorityQueue.poll());
        System.out.println(priorityQueue.poll());
        System.out.println(priorityQueue.poll());
	}
	
	public static void main(String args[]){
		Comparator<Cand> orderIsdn =  new Comparator<Cand>(){
			public int compare(Cand o1, Cand o2) {
				if(o2.incidence > o1.incidence){
					return 1;
				}else if(o2.incidence < o1.incidence){
					return -1;
				}else{
					return 0;
				}
            }
        };
        
		Queue<Cand> priorityQueue =  new PriorityQueue<Cand>(2, orderIsdn);
		priorityQueue.add(new Cand(1, 10));
		priorityQueue.add(new Cand(3, 30));
		priorityQueue.add(new Cand(2, 20));
		priorityQueue.add(new Cand(4, 40));
		
		while(priorityQueue.size() > 0){
			Cand c = priorityQueue.poll();
			System.out.println(c.vertexId + " " + c.incidence);
		}
	}
}