package algorithm;

import java.util.*;
import java.util.Map.Entry;


public class Test {

	public static void main(String[] args){
		
	Set<Integer> set = new HashSet<Integer>();
	set.add(1);
	set.add(2);
	set.add(4);
	set.add(3);
	
	Set<Integer> set1 = new HashSet<Integer>();
	set1.add(1);
	set1.add(4);
	set1.add(2);
	set1.add(5);
	
	Set<Set<Integer>> bSet=new HashSet<Set<Integer>>();
	List<Set<Integer>> list = new ArrayList<Set<Integer>>(2);
	
	list.add(set); list.add(set1);
	
	
	Set<List<Set<Integer>>> visited = new HashSet<List<Set<Integer>>>(); 
	visited.add(list);
	
	
	
	Set<Integer> set2 = new HashSet<Integer>();
	set2.add(1);
	set2.add(2);
	set2.add(3);
	set2.add(4);
	
	Set<Integer> set3 = new HashSet<Integer>();
	set3.add(1);
	set3.add(5);
	set3.add(2);
	set3.add(4);
	
	List<Set<Integer>> list1 = new ArrayList<Set<Integer>>(2);
	list1.add(set2); list1.add(set3);
	
	
	boolean[] tese = new boolean[4];
	for(boolean s:tese) System.out.println(s);
	
	Set<Integer> ser12 = null;
	System.out.println(ser12.isEmpty()+"   sadasdas");
	
	System.out.println(visited.contains(list1));
	
	
//	bSet.add(set);
//	System.out.println(bSet.contains(set1));
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
