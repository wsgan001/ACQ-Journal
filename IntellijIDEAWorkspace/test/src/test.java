public class test {
    public int[] a;
    public int[] combinition;

    public test(int[] b){
        this.a = b;
        combinition = new int[a.length];
    }


    public static void main (String [] args) {
        int a[] ={3,6,4,1,2,2,7,1,8,10};
        test t = new test(a);
        t.findCombinition(13,0,t.a,0,t.a.length,t.combinition);

        }


    void findCombinition(int sum,int pointer, int[] a, int start, int end, int[] c ){

        for(int i=start; i<end; i++){
            sum = sum-a[i];
            c[pointer] = i;
            pointer++;
            if(sum==0){
                for(int j = 0; j<pointer;j++){
//                    System.out.print(a[c[j]]+"   ");
                    System.out.print(c[j]+"   ");// using index; If you want to ouput the value: a[c[j]]
                }
                System.out.println();
            }else {
                findCombinition(sum,pointer,a,i+1,end,c);
            }
            sum = sum+a[i];
            pointer--;
        }
    }
}
