package EXP;

public class test {
	private int[] a = new int[2];
	private int k = -1;
	
	
	public test(){
		a[0]=1;
		a[1]=2;
		k=3;
	}
	
	public int[] geta(){
		return a;
		}
	
	public static void main(String[] args){
		test s= new test();
		int[] a=s.a;
		s = null;
		for(int x:a) System.out.println(x);
		System.out.println(s==null);
	}
	
	
}
