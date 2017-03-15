package hku.variant.variant4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryV3Reader {

	public List<int[]> readIdArr(String filename){
		List<int[]> idList=new ArrayList<int[]>();
		try {
			BufferedReader stdin=new BufferedReader(new FileReader(filename));
			String line=null;
			while((line=stdin.readLine())!=null){
				String idPart=line.substring(0, line.indexOf('\t'));//'\t'
				String[] idStrings=idPart.trim().split(" ");
				int[] idArr=new int[idStrings.length];
				for(int i=0;i<idStrings.length;i++) idArr[i]=Integer.parseInt(idStrings[i]);
				idList.add(idArr);
			}
		} catch (Exception e) { e.printStackTrace(); }
		return idList;
	}
	
	public List<String[]> readKwSet(String filename){
		List<String[]> kwSetList=new ArrayList<String[]>();
		try {
			BufferedReader bReader=new BufferedReader(new FileReader(filename));
			String line=null;
			while((line=bReader.readLine())!=null){
				String kwPart=line.substring(line.indexOf('\t')+1);
				String[] kwStrings=kwPart.trim().split(" ");
				kwSetList.add(kwStrings);
			}
		} catch (Exception e) { e.printStackTrace(); }
		return kwSetList;
	}
	
	
	public static void main(String[] args){
		String filename="/Users/chenyankai/Desktop/dblp-graph-query-Qsize=2.txt";
		QueryV3Reader qReader=new QueryV3Reader();
		qReader.readIdArr(filename);
		qReader.readKwSet(filename);
	}
	
	
}
