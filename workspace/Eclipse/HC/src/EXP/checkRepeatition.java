package EXP;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import config.Config;

public class checkRepeatition {
	public void check(){
		Map<String, Integer> check = new HashMap<String,Integer>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(Config.pubMedDataWorkSpace+"buffer.txt"));
			String line = null;
			while((line=bReader.readLine())!=null){
				if(check.containsKey(line)){
					int x = check.get(line);
					check.put(line, x++);
				}
				else {
					check.put(line, 1);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for(Iterator<String> iterator= check.keySet().iterator();iterator.hasNext();){
			String xString = iterator.next();
			int count = check.get(xString);
			if(count!=1) System.out.println("hehe ");
//			if(count>=2) System.out.println("string: "+xString+ " count: "+count);
		}
		
	}
	
	
	
	public static void main(String[] args){
		checkRepeatition check = new checkRepeatition();
		check.check();
	}
	
}
