package EXP;

import java.util.*;

public class test {

	public static void main(String[] args){
		Set<Integer> bigSet = new HashSet<Integer>();
		Set<Integer> smallSet = new HashSet<Integer>();
		bigSet.add(1);
		bigSet.add(4);
		bigSet.add(2);
		bigSet.add(0);
		smallSet.add(0);
		smallSet.add(2);
		System.out.println(bigSet.containsAll(smallSet));
	}
	
	
}
