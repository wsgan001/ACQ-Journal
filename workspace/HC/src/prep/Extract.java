package prep;

import java.io.IOException;

public class Extract {
	
	public static void main(String[] a){
		String command="gzip -d /Users/chenyankai/GitDefault/workspace/HC/file/medline17n0001.xml.gz";
		try {
			Runtime.getRuntime().exec(command);
//			Runtime.getRuntime().exec("rm -f /Users/chenyankai/GitDefault/workspace/HC/file/medline17n0001.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}