package hku.algo.index.edgeUpdate;

import java.io.*;
import java.nio.Buffer;
import java.util.*;

import hku.Config;
import hku.algo.DataReader;
import hku.algo.TNode;
import hku.algo.index.AdvancedIndex;

public class EvolvingGprah {
	TNode[] invert=null;
	int[] core=null;
	TNode root=null;
	int [][] graph=null;
	String[][] nodes=null;
	
	private void readData(String infile,String outfile,String nodefile,String end){
		Map<Integer, Set<Integer>> map=new HashMap<Integer, Set<Integer>>();
		int size=-1;
		try {
			BufferedReader brReader=new BufferedReader(new FileReader(infile));
			String line=null;
			while((line=brReader.readLine())!=null){
				line=line.trim();
				String[] s=line.split("\t");
				if(!s[2].equals(end)){
					int u=Integer.parseInt(s[0]);
					int v=Integer.parseInt(s[1]);
					int larger=u>v?u:v;
					if(larger>size) size=larger;
					if(map.containsKey(u))	map.get(u).add(v);
					else{
						Set<Integer> list=new HashSet<Integer>();
						list.add(v);
						map.put(u, list);
					}
				
					if(map.containsKey(v))	map.get(v).add(u);
					else{
						Set<Integer> list=new HashSet<Integer>();
						list.add(u);
						map.put(v, list);
					}
				}
				if(s[2].equals(end)) break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!map.isEmpty()){
			try {
				BufferedWriter brWriter=new BufferedWriter(new FileWriter(outfile));
				BufferedWriter brWriter1=new BufferedWriter(new FileWriter(nodefile));
				Set<Integer> set=map.keySet();
				
				for(int x=1;x<size+1;x++){
					String line=x+" ";
					if(map.containsKey(x)){
						Set<Integer> list=map.get(x);
						for(int y:list) line=line+y+" ";
					}
					brWriter.write(line);
					brWriter.newLine();
					
					String str=x+"\t"+x+"\t";
					brWriter1.write(str);
					brWriter1.newLine();
				}
			brWriter.flush();
			brWriter.close();
			brWriter1.flush();
			brWriter1.close();
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void completeGraph(String file1,String file2,String nodefile1,String nodefile2){
		DataReader d1Reader=new DataReader(file1, nodefile1);
		DataReader d2Reader=new DataReader(file2, nodefile2);
		int[][] graph1=d1Reader.readGraph();
		int[][] graph2=d2Reader.readGraph();
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(file1,true));
			BufferedWriter bw1=new BufferedWriter(new FileWriter(nodefile1,true));
			for(int i=graph1.length;i<graph2.length;i++){
				bw.write(i+" ");
				bw.newLine();
				bw1.write(i+"\t"+i+"\t");
				bw1.newLine();
			}
			bw.flush();
			bw.close();
			bw1.flush();
			bw1.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private void getHistory(int[][]graph1,int[][] graph2,String history){
	
		try{
			BufferedWriter b1=new BufferedWriter(new FileWriter(history));
			for(int i=1;i<graph1.length;i++){
				int locate=i;
				int[] g1=graph1[i];
				int[] g2=graph2[i];
				//find deleted edge
				for(int w=0;w<g1.length;w++){
					int u=g1[w];
					boolean isDeleted=true;
					for(int y=0;y<g2.length;y++){
						int v=g2[y];
						if(u==v) {
							isDeleted=false;
							break;
						}
					}
					if(isDeleted){
						String s=-1+","+locate+","+u;//-1 delete	
						b1.write(s);
						b1.newLine();
					}
				}
				
				//find inserted edge
				for(int w=0;w<g2.length;w++){
					int u=g2[w];
					boolean isInserted=true;
					for(int y=0;y<g1.length;y++){
						int v=g1[y];
						if(u==v) {
							isInserted=false;
							break;
						}
					}
					if(isInserted){
						String s=1+","+locate+","+u;//-1 insert	
						b1.write(s);
						b1.newLine();
					}
				}
			}
			b1.flush();
			b1.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void getinsertNum(String infile){
		Map<String, Integer> map=new HashMap<String, Integer>();
		try{
			BufferedReader bReader=new BufferedReader(new FileReader(infile));
			String line=null;
			while((line=bReader.readLine())!=null){
				String[] s=line.split("\t");
				if(map.containsKey(s[2])) {
					int x=map.get(s[2]);
					map.put(s[2], ++x);
				}else map.put(s[2], 1);
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		for(Iterator<String> it=map.keySet().iterator();it.hasNext();){
			String string=it.next();
			System.out.println(string + " num: "+map.get(string));
		}
		
	}
	
	private void getDelete(String infile,String outfile,int num){
		Map<Integer, Set<Integer>> map=new HashMap<Integer, Set<Integer>>();
		try {
			BufferedReader bReader=new BufferedReader(new FileReader(infile));
			BufferedWriter bWriter=new BufferedWriter(new FileWriter(outfile));
			String line=null;
			int count=0;
			while((line=bReader.readLine())!=null){
				String[] s=line.split("\t");
				int u=Integer.parseInt(s[0]);
				int v=Integer.parseInt(s[1]);
				if(count!=num){
					bWriter.write(-1+","+u+","+v);
					bWriter.newLine();
					count++;
				}
				}
			bWriter.flush();
			bWriter.close();
		}catch (Exception e) {
		// TODO: handle exception
		}
	}
	
	
	private void getInsertHis(String infile,int size,String outpath,String out,String begin,String end){
		String outfile=outpath+out+".txt";
		List<String> list=new ArrayList<String>();
		try {
			BufferedReader bReader=new BufferedReader(new FileReader(infile));
			BufferedWriter bWriter=new BufferedWriter(new FileWriter(outfile));
			String line=null;
			boolean flag=false;
			while((line=bReader.readLine())!=null){
				String[] s=line.split("\t");
				if(s[2].equals(begin)) flag=true;
				if(flag){
					int u=Integer.parseInt(s[0]);
					int v=Integer.parseInt(s[1]);
					if(u<size && v<size) list.add(u+","+v);
				}
				if(s[2].equals(end)) break;
			}
			int count=0;
			while(count!=200){
				Random random=new Random();
				int x=random.nextInt(list.size());
				bWriter.write(list.get(x));
				bWriter.newLine();
				count++;
			}
			bWriter.flush();
			bWriter.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	private void get5Group(String file,int size){
		String outpath="/Users/chenyankai/Desktop/ACQ/info/";
		getInsertHis(file,size, outpath, "0-20", "2007-04-14", "2007-05-04");
		getInsertHis(file,size,outpath, "20-40", "2007-05-04", "2007-05-24");
		getInsertHis(file,size,outpath, "40-60", "2007-05-24", "2007-06-13");
		getInsertHis(file,size,outpath, "60-80", "2007-06-13", "2007-07-04");
		getInsertHis(file,size, outpath, "80-100", "2007-07-04", "2007-07-23");
	}
	
	public void test(String graphfile,String nodefile,String history){
		DataReader d1=new DataReader(graphfile, nodefile);
		graph=d1.readGraph();
		nodes=d1.readNode();
		AdvancedIndex ad=new AdvancedIndex(graph, nodes);
		root=ad.build();
		core=ad.getCore();
		invert=ad.getInvert();
		EdgeInsertion eInsertion=new EdgeInsertion(root, invert, graph, nodes, core);
		EdgeDeletion edgeDeletion=new EdgeDeletion(root, invert, graph, nodes, core);
//		edgeDeletion.deleteEdge(2,135776);
		double dynamic=0;
		double rebuild=0;
		try {
			BufferedReader br=new BufferedReader(new FileReader(history));
			String line=null;
			int count=0;
			
			while((line=br.readLine())!=null&&count!=200){
				String[] s=line.split(",");
				System.out.println("count "+count);
//				DataReader d1=new DataReader(graphfile, nodefile);
//				int[][] graph=d1.readGraph();
//				String[][] node=d1.readNode();
//				long time1=System.nanoTime();
//				AdvancedIndex ad=new AdvancedIndex(graph, node);
//				TNode root=ad.build();
//				rebuild+=System.nanoTime()-time1;
//				EdgeInsertion eInsertion=new EdgeInsertion(root, ad.getInvert(), graph, node, ad.getCore());
//				EdgeDeletion edgeDeletion=new EdgeDeletion(root, ad.getInvert(), graph, node, ad.getCore());
				long time=System.nanoTime();
				if(Integer.parseInt(s[0])==1){
					eInsertion.insertEdge(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
					count++;
				}
				if(Integer.parseInt(s[0])==-1) {
					edgeDeletion.deleteEdge(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
					count++;
				}
				dynamic+=(System.nanoTime()-time);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println("time gap "+rebuild/dynamic);
	}
	
	public static void main(String[] a){
//		String inpath1="/Users/chenyankai/Desktop/ACQ/info/flickr-growth.txt";
//		String outpath1="/Users/chenyankai/Desktop/ACQ/info/initial.txt";
//		String nodefile1="/Users/chenyankai/Desktop/ACQ/info/node.txt";
//		String inpath2="/Users/chenyankai/Desktop/info/20170201.as-rel.txt";
		String outpath2="/Users/chenyankai/Desktop/ACQ/info/initial1.txt";
		String nodefile2="/Users/chenyankai/Desktop/ACQ/info/node2.txt";
		EvolvingGprah eg=new EvolvingGprah();
		
//		eg.readData(inpath1, outpath2,nodefile2,"2006-11-03");
//		eg.completeGraph(outpath1, outpath2, nodefile1,nodefile2);
//		String insertfile="/Users/chenyankai/Desktop/ACQ/info/insert.txt";
//		eg.getInsertHis(outpath2, insertfile, "2006-11-03", "2006-11-04");
//		String removefile="/Users/chenyankai/Desktop/ACQ/info/flickr-growth-removed.txt";
//		String removeout="/Users/chenyankai/Desktop/ACQ/info/removed.txt";
		String youtube="/Users/chenyankai/Desktop/ACQ/info/youtube-growth.txt";
		String yout="/Users/chenyankai/Desktop/ACQ/info/youtube-graph.txt";
		String yNode="/Users/chenyankai/Desktop/ACQ/info/youtube-node.txt";
//		eg.readData(youtube, yout, yNode, "2007-04-14");
		DataReader d1=new DataReader(yout, yNode);
		int size=d1.readGraph().length;
		eg.get5Group(youtube,size);
//		eg.getDelete(removefile,removeout,100);
//		eg.getinsertNum(removefile);
//		String hisPath="/Users/chenyankai/Desktop/info/history.txt";
//		eg.getHistory(d1.readGraph(),d2.readGraph(), hisPath);
//		eg.test(outpath1, nodefile1, hisPath);
//		long time=System.nanoTime();
		
//		DataReader d1=new DataReader(Config.flickrGraph, Config.flickrNode);
//		int[][] graph=d1.readGraph();
//		String[][] nodes=d1.readNode();
//		AdvancedIndex adIndex=new AdvancedIndex(graph, nodes);
//		TNode root=adIndex.build();
//		System.out.println((System.nanoTime()-time)/1000000);
//		eg.test(outpath2, nodefile2, removeout);
		
	}
	
}
