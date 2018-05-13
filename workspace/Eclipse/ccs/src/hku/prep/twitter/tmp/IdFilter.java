package hku.prep.twitter.tmp;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
/**
 * @author fangyixiang
 * @date Oct 6, 2015
 * link the id in two different twitter dataset
 */
public class IdFilter {

	public List<Integer> readId(){
		List<Integer> list = new ArrayList<Integer>();
		try{
			String path = "";
			BufferedReader stdin = new BufferedReader(new FileReader(path));
			
			String line = null;
			while((line = stdin.readLine()) != null){
				
			}
			stdin.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
