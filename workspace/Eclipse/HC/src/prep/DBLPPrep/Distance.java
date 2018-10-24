package prep.DBLPPrep;

import java.util.*;

public class Distance {

	//used in edit distance
	private int min(int one, int two, int three){  
        int min = one;  
        if (two < min) {  
            min = two;  
        }  
        if (three < min) {  
            min = three;  
        }  
        return min;  
    }  
	//edit distance : [0:1] 1 means the closest 
	public double editDis(String str1,String str2){
		System.out.println(str1+"   "+str2);
		int d[][]; // matrix 
        int y = str1.length();  
        int x = str2.length();  
        char ch1; // str1 
        char ch2; // str2 
        int temp; // mark the same char (0 or 1)
        if (y == 0) {  
            return x;  
        }  
        if (x == 0) {  
            return y;  
        }  
        d = new int[y + 1][x + 1]; // matrix to compute the edit distance   
        for (int j = 0; j <= x; j++) { // initialize the first line of the matrix
            d[0][j] = j;  
        }  
        for (int i = 0; i <= y; i++) { // initialize the first row of the matrix 
            d[i][0] = i;  
        }  
        for (int i = 1; i <= y; i++) { 
            ch1 = str1.charAt(i - 1);    
            for (int j = 1; j <= x; j++) {  
                ch2 = str2.charAt(j - 1);  
                if (ch1 == ch2) {  
                    temp = 0;  
                } else {  
                    temp = 1;  
                }  
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);  
            }  
        }  
        return 1 - (double) d[y][x] / Math.max(str1.length(), str2.length());  
    }  


	private List<int[]> convertVector(String str1,String str2){
		List<int[]> list = new ArrayList<int[]>();
		Set<Character> wordBag = new HashSet<Character>();
		Set<Character> set1 = new HashSet<Character>();
		Set<Character> set2 = new HashSet<Character>();

		for(char s:str1.toCharArray()){
			wordBag.add(s);
			set1.add(s);
		}
		for(char s:str2.toCharArray()){
			wordBag.add(s);
			set2.add(s);
		}
		int[] vector1 = new int[wordBag.size()];
		int[] vector2 = new int[wordBag.size()];
		
		Iterator<Character> iter = wordBag.iterator();
		
		int index = 0;
		while(iter.hasNext()){
			char cur = iter.next();
			if(set1.contains(cur)) vector1[index] = 1;
			if(set2.contains(cur)) vector2[index] = 1;
			index++;
		}
		list.add(vector1);
		list.add(vector2);
		return list;
	}
	
	//Euclidean Distance: [0,...) O is the closest,
	public double EuclideanDis(String str1,String str2){
		List<int[]> list = convertVector(str1, str2);
		int[] vector1 = list.get(0);
		int[] vector2 = list.get(1);
		double distance = 0; 

		if (vector1.length == vector2.length) {  
            for (int i = 0; i < vector1.length; i++) {  
                double temp = Math.pow((vector1[i] - vector2[i]), 2);  
                distance += temp;  
            }  
            distance = Math.sqrt(distance);  
        }  
        return distance; 	
	}
	
	//Cosine distance: [0,1] 1 means the closest 
	public double cosineDis(String str1,String str2){
		List<int[]> list = convertVector(str1, str2);
		int[] vector1 = list.get(0);
		int[] vector2 = list.get(1);
		double distance = 0; 
		double upper = 0;
		double Asqr = 0;
		double Bsqr = 0;
		if (vector1.length == vector2.length) {  
            for (int i = 0; i < vector1.length; i++) { 
            	upper += vector1[i] * vector2[i];
            	Asqr += vector1[i] * vector1[i];
            	Bsqr += vector2[i] * vector2[i];
            }  
            distance = upper/Math.sqrt(Asqr*Bsqr);  
        }  
		return distance;
	}
	

	public static void main(String[] args){
		String str1 = "apple";
		String str2 = "1";
//		System.out.println(new Distance().cosineDis(str1, str2));
		System.out.println(new Distance().editDis(str1, str2));
	}


}
