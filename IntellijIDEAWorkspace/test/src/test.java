public class test {

    static int [] f = new int [100];
    static int size = 20;
    public static void main (String [] args) {
        f [0] = 1;
        f [1] = 2;
        for (int n = 2;n<size;n++) {
            f [n] = recurse (n-1)+1;
            System.out.print(f [n]+" ");
        }
    }

    static int recurse (int left) {
        if (left == 0) return 1;
        if (left == 1) return 2;
        int max = 0;
        for (int i = 1;i <= left;i++)
            max = Math.max (max,f[i] * recurse (left - i));

        return max;
    }
}
