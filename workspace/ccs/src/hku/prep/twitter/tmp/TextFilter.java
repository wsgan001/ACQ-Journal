package hku.prep.twitter.tmp;

import hku.util.STEMExt;
import java.util.*;
import hku.util.StopFilter;

import java.io.*;
/**
 * @author fangyixiang
 * @date Oct 6, 2015
 */
public class TextFilter {

	public void handle(){
		STEMExt stem = new STEMExt();
		StopFilter stop = new StopFilter();
		
		String textPath = "/home/fangyixiang/Desktop/CCS/twitter/twitter_text1/";
		String path = "/home/fangyixiang/Desktop/CCS/twitter/twitterDataset/";
		File folder = new File(path);
		File files[] = folder.listFiles();
		
		int count1 = 0;
		Map<String, Integer> nameMap = new HashMap<String, Integer>();
		try{
			BufferedWriter stdout = new BufferedWriter(new FileWriter(textPath));
			for(int i = 0;i < files.length;i ++){
				File file = files[i];
				
				BufferedReader stdin = new BufferedReader(new FileReader(file));
					
				String line = null;
				while((line = stdin.readLine()) != null){
					line = line.toLowerCase();
					String s[] = line.split("\t");
						
					if(s.length >= 5){
						String id = s[0];
						String name = s[1];
						String text = s[4];
							
						String w[] = text.split(" ");
						String newText = "";
						for(String word:w){
							word = filerSpec(word); //filter special symbols								word = stem.extSTEM(word);
							if(stop.contains(word) == false){
								newText += word + " ";
							}
						}
						
						if(newText.length() > 0){
							stdout.write(id + "\t");
							stdout.write(newText);
							stdout.newLine();
							
							count1 += 1;
							if(nameMap.containsKey(name)){
								nameMap.put(name, nameMap.get(name) + 1);
							}else{
								nameMap.put(name, 1);
							}
						}
					}
				}
				stdin.close();
			}
			stdout.flush();
			stdout.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("tweets:" + count1);
		System.out.println("names:" + nameMap.size());
		
		
		try{
			String tPath = "/home/fangyixiang/Desktop/CCS/twitter/twitter_user.txt";
			BufferedWriter stdout = new BufferedWriter(new FileWriter(tPath));
			int countNum = 0;
			for(Map.Entry<String, Integer> entry:nameMap.entrySet()){
				if(entry.getValue() >= 4){
//					System.out.println(entry.getKey() + " " + entry.getValue());
					countNum += 1;
					stdout.write(entry.getKey());
					stdout.newLine();
				}
			}
			stdout.flush();
			stdout.close();
			System.out.println("count4:" + countNum);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//filter special characters
	private String filerSpec(String word){
		if(word.startsWith("@"))   return "";
		if(word.startsWith("http://"))   return "";
		
		String rs = "";
		for(int i = 0;i < word.length();i ++){
			char c = word.charAt(i);
			if(c >= 'a' && c <= 'z'){
				rs += c;
			}
			if(c >= 0 && c <= 9){
				rs += c;
			}
		}
		
		return rs;
	}
	
	public static void main(String[] args) {
		TextFilter filter = new TextFilter();
		filter.handle();
	}

}
