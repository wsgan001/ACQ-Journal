package tst;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class timestmap {

	public void read(String filename,int x){
			String line=null;
		try {
			BufferedReader bReader=new BufferedReader(new FileReader(filename));
				bReader.readLine();
			while(( line=bReader.readLine())!=null){
				line=line.trim();
				String[] strings=line.split("\t");
				String time[]=strings[2].split(",");
				if(Integer.parseInt(time[1])!=x) 
					System.out.println(time[1]);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public static void main(String[] a){
//		 String file="/Users/chenyankai/Desktop/info/fl_days_version";
//		 timestmap t1=new timestmap();
//		 t1.read(file,133);
		 String string="123456";
		 System.out.print(string.substring(0, 3));
	 }
	
	
}
