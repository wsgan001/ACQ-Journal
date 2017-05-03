import java.io.*;

public class TextProcess {
	
	public void process(String fileName,String out){
		
		try {
			BufferedReader bReader=new BufferedReader(new FileReader(fileName));
			BufferedWriter bWriter=new BufferedWriter(new FileWriter(out));
			String line=null;
			bReader.readLine();
			while((line=bReader.readLine())!=null){
				String s[]=line.split(",");
				String code=s[0].substring(1, s[0].length()-1);
				bWriter.write(code);
				bWriter.newLine();
			}
			bWriter.flush();
			bWriter.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args){
		TextProcess process=new TextProcess();
		process.process("/Users/chenyankai/Desktop/companylist.csv","/Users/chenyankai/Desktop/code.csv");
	
	}
	
	
}
