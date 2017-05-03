import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Project1{
	

	public void read(String fileName,String outfile,int GorS,int flag){
		String file="/Users/chenyankai/Documents/sql_data/allgenre_type.csv";
		String file1="/Users/chenyankai/Documents/sql_data/allsong_mood.csv";
		String file2="/Users/chenyankai/Documents/sql_data/"+fileName;
		ArrayList<String> list=new ArrayList<String>();
		Map<Integer,Map<String,Integer>> map=new HashMap<Integer,Map<String,Integer>>();
		try {	
				BufferedReader bReader=null;
				if(GorS==1){
					bReader=new BufferedReader(new FileReader(file));
				}
				if(GorS==2){
					bReader=new BufferedReader(new FileReader(file1));
				}
				String line="";
				Map<String,Integer> tmp=new HashMap<String,Integer>();
				bReader.readLine();
				while((line=bReader.readLine())!=null){
						tmp.put(line, 0);
				}
				bReader.close();
				
				for(int i=0;i<48;i++){
					Map<String,Integer> map1=new HashMap<String,Integer>();
					for(String x:tmp.keySet()){map1.put(x, 0);}
					map.put(i,map1);
				}
				
				//with two , 
				if(flag==1){
				BufferedReader br2=new BufferedReader(new FileReader(file2));	
				String line2="";
				br2.readLine();
				while((line2=br2.readLine())!=null){
					System.out.println(line2);
					String content[]=line2.split(",");
					int id=Integer.parseInt(content[0]);
					String string=content[1];
					int count=Integer.parseInt(content[2]);
					
					map.get(id).put(string, count);
				}
					
				}
				//with three ,
				if(flag==2){
					BufferedReader br2=new BufferedReader(new FileReader(file2));	
					String line2="";
					br2.readLine();
					while((line2=br2.readLine())!=null){
						System.out.println(line2);
						String content[]=line2.split(",");
						int id=Integer.parseInt(content[0]);
						String string=content[2];
						int count=Integer.parseInt(content[3]);
						
						map.get(id).put(string, count);
					}
					
				}
						
				BufferedWriter bWriter=new BufferedWriter(new FileWriter("/Users/chenyankai/Documents/sql_data/"+outfile));
				for(int x:map.keySet()){
					String write="";
					Map<String,Integer> tmMap=map.get(x);
					for(String y:tmMap.keySet()){
						write=x+","+y+","+tmMap.get(y);
						bWriter.write(write);
						bWriter.newLine();
					}
				}	
				bWriter.flush();
				bWriter.close();
				

			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args){
		Project1 project=new Project1();
		project.read("sub_genre.csv", "fix_sub_genre.csv",1,1);
		project.read("sub_song.csv", "fix_sub_song.csv",2,1);
		project.read("sub_actv_genre.csv", "fix_sub_actv_genre.csv",1,2);
		project.read("sub_actv_song.csv", "fix_sub_actv_song.csv",2,2);
		project.read("sub_mood_genre.csv", "fix_sub_mood_genre.csv",1,2);
		project.read("sub_mood_song.csv", "fix_sub_mood_song.csv",2,2);
		
	}
	
	
	
}
