package hku.algo.previous;

/**
 * @author fangyixiang
 * @date Feb 19, 2016
 */
public class CodicilTest {

	public static void main(String[] args) {
		int graph[][] = new int[10][];
		int a1[] = {2, 3, 4, 5};graph[1] = a1;
		int a2[] = {1, 3, 4, 5};graph[2] = a2;
		int a3[] = {1, 2, 4};	graph[3] = a3;
		int a4[] = {1, 2, 3, 6};graph[4] = a4;
		int a5[] = {1, 2, 7};	graph[5] = a5;
		int a6[] = {4};			graph[6] = a6;
		int a7[] = {5};			graph[7] = a7;
		int a8[] = {9};			graph[8] = a8;
		int a9[] = {8};			graph[9] = a9;
		
		String nodes[][] = new String[10][];
		String k1[] = {"A", "w", "x", "y"};     nodes[1] = k1;
		String k2[] = {"B", "x"};				nodes[2] = k2;
		String k3[] = {"C", "x", "y"};			nodes[3] = k3;
		String k4[] = {"D", "x", "y", "z"};		nodes[4] = k4;
		String k5[] = {"E", "y", "z"};			nodes[5] = k5;
		String k6[] = {"F", "y"};    			nodes[6] = k6;
		String k7[] = {"G", "x", "y"};			nodes[7] = k7;
		String k8[] = {"H", "y", "z"};			nodes[8] = k8;
		String k9[] = {"I", "x"};				nodes[9] = k9;

		Codicil cd = new Codicil(graph, nodes);
		cd.handle();
	}

}
