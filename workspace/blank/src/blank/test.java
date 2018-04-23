package blank;

import java.util.*;

public class test {

	   public String[] findWords(String[] words) {
	       StringBuffer out =new StringBuffer();
		   for(String word:words){
			   boolean flag = true; 
		        char[] character = word.toCharArray();
		        int id = identify(character[0]);
		        for (int i=1;i<character.length;i++){
		            if (identify(character[i])!=id) {
		                flag= false;
		                break;
		        }
		        if(flag=true) out.append(word+" ");
		    }
		   }
	        return out.toString().trim().split(" ");
	    
	}
	    
	    
	    public int identify(char x){
	        if(x=='Q'||x=='W'||x=='E'||x=='R'||x=='T'||x=='Y'||x=='U'||x=='I'||x=='O'||x=='P') return 1;
	        else if(x='A'|| x=='S'||x=='D'||x=='F'||x=='G'||x=='H'||x=='J'||x=='K'||x=='L') return 2;
	        else return 3;
	        
	    }
	    
	   public static void main(String[] a){
	    	test t = new test();
	    	String string = null;
	    	string.
	    	System.out.println(x);
	   }
	
	
	}
	
	

