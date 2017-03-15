package hku.prep.flickr.ip;

/**
 * @author yxfang
 *
 * @date 2015-10-6
 */
public class Test {

	public static void main(String[] args) {
		String line = "<div class=\"ProfileNav\" role=\"navigation\" data-user-id=\"13790742\">";
		String s = "class=\"ProfileNav\" role=\"navigation\" data-user-id=";
		
		if(line.length() >= 50){
			line = line.substring(0, line.lastIndexOf('\"'));
			String newId = line.substring(line.lastIndexOf(('\"')) + 1);
			System.out.println(line);
			System.out.println(newId);
		}
		
		System.out.println(s.length());
	}

}
