package algorithm;

import java.util.*;


public class Test {

	public static void main(String[] args){
		
	Set<Integer> set = new HashSet<Integer>();
	set.add(1);
	set.add(2);
	set.add(3);
	set.add(4);
	
	
	Set<Integer> set1 = new HashSet<Integer>();
	set1.add(1);
	set1.add(2);
	set1.add(3);
	set1.add(4);
	
	Set<Set<Integer>> bSet=new HashSet<Set<Integer>>();
	bSet.add(set);
	System.out.println(bSet.contains(set1));
//	Map<Integer, Set<Integer>> map = new HashMap<Integer,Set<Integer>>();
//	map.put(1, set);
//	map.put(2, set1);
//	Iterator<Integer> keyIter=map.keySet().iterator();
//	while(keyIter.hasNext()){
//		int key=keyIter.next();
//		if(key==1) keyIter.remove();
//	}
//	
//	keyIter=map.keySet().iterator();
//	while(keyIter.hasNext()){
//		System.out.println(keyIter.next());
//	}
		
	}
	
	
	
	
	
	
	
}