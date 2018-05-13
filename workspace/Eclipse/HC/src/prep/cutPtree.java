package prep;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import config.Config;

public class cutPtree {
 
	public void cut(String cptree){
		List<String> list = new ArrayList<String>();
		
		try {
			BufferedReader stdIn = new BufferedReader(new FileReader(cptree));
			String line = null;
			while((line=stdIn.readLine())!=null) {
				list.add(line);
			}

			int n20 = (int) (list.size()*0.2);
			int n40 = (int) (list.size()*0.4);			
			int n60 = (int) (list.size()*0.6);
			int n80 = (int) (list.size()*0.8);
			System.out.println(list.size()+" "+n20+" "+n40+" "+n60+" "+n80);
			
			BufferedWriter stdOut = new BufferedWriter(new FileWriter(cptree+"-20"));
			for(int i = 0;i<n20;i++){
				stdOut.write(list.get(i));
				stdOut.newLine();
			}
			stdOut.flush();
			stdOut.close();
			
			stdOut = new BufferedWriter(new FileWriter(cptree+"-40"));
			for(int i =0;i<n40;i++){
				stdOut.write(list.get(i));
				stdOut.newLine();
			}
			stdOut.flush();
			stdOut.close();
			
			stdOut = new BufferedWriter(new FileWriter(cptree+"-60"));
			for(int i =0;i<n60;i++){
				stdOut.write(list.get(i));
				stdOut.newLine();
			}
			stdOut.flush();
			stdOut.close();
			
			stdOut = new BufferedWriter(new FileWriter(cptree+"-80"));
			for(int i =0;i<n80;i++){
				stdOut.write(list.get(i));
				stdOut.newLine();
			}
			
			stdOut.flush();
			stdOut.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args){
		cutPtree cutCPTree = new cutPtree();
		cutCPTree.cut(Config.ACMDLCPtree);
		cutCPTree.cut(Config.DBLPCPTree);
		cutCPTree.cut(Config.pubmedCPtree);
		cutCPTree.cut(Config.FlickrCPTree);
	}
	
}


