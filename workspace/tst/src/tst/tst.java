package tst;

import java.io.*;

public class tst {
	private int a=0;
	private static int[] alist=null;
	private static String[] blist=null;
	
	public tst(){
		alist=new int[4];
		int[] tm={1,2,3,4};
		this.alist=tm;
		String[] tmp={"a","b","c"};
		this.blist=tmp;
	}
	
	public void test(){
		
		int[] b=alist;
		int[] tm={4,3,2,1};
		b[1]=9;
		String[] c=blist;
		String[] tmp1={"d","e","c"};
		c[1]="aaa";
		for(int x:b) System.out.print(x+"   ");
		System.out.println();
		for(String x:c) System.out.print(x+"       ");
		System.out.println();
		for(int x:alist) System.out.print(x+"   ");
		System.out.println();
		for(String x:blist) System.out.print(x+"       ");
	}
	
 public static void main(String[] at){
	tst sss=new tst();
	sss.test();
	

 }


}
