
public class test {

	public void test(int i){
		i++;
		if(i!=100) test(i);
		System.out.println(i);
	}
	
	
public static void main(String[] args){
		test t=new test();
		t.test(1);
	}
}
