package hku;

/**
 * @author fangyixiang
 * @date Jul 20, 2015
 */
public class Config {
	public static double kwFreq = 0.01;//consider all the words globally
	public static int topKw = 20;//consider the keywords of each user locally
	
	//stem file paths
	public static String stemFile = "./stemmer.lowercase.txt";
	public static String stopFile = "./stopword.txt";
	
	//dataset file paths
	public static String caseNode = "/home/fangyixiang/Desktop/CCS/dblp/case-node";
	public static String caseGraph = "/home/fangyixiang/Desktop/CCS/dblp/case-graph";
	public static String caseCCS = "/home/fangyixiang/Desktop/CCS/dblp/case-ccs";
	
//	public static String flickrNode = "/home/fangyixiang/Desktop/CCS/flickr/flickr-node";
//	public static String flickrGraph = "/home/fangyixiang/Desktop/CCS/flickr/flickr-graph";
	public static String flickrCCS = "/home/fangyixiang/Desktop/CCS/flickr/flickr-ccs";
	
//	public static String dblpNode = "/home/fangyixiang/Desktop/CCS/dblp/dblp-node";
//	public static String dblpGraph = "/home/fangyixiang/Desktop/CCS/dblp/dblp-graph";
	public static String dblpCCS = "/home/fangyixiang/Desktop/CCS/dblp/dblp-ccs";
	public static String dblpSmpGraph = "/home/fangyixiang/Desktop/CCS/dblp/dblp-smp-graph";
//	
	public static String tencentNode = "/home/fangyixiang/Desktop/CCS/tencent/tencent-node";
	public static String tencentGraph = "/home/fangyixiang/Desktop/CCS/tencent/tencent-graph";
	public static String tencentCCS = "";
//	
	public static String twitterNode = "/home/fangyixiang/Desktop/CCS/twitter/twitter-node";
	public static String twitterGraph = "/home/fangyixiang/Desktop/CCS/twitter/twitter-graph";
	public static String twitterCCS = "";
//	
	public static String dbpediaNode = "/home/fangyixiang/Desktop/CCS/dbpedia/dbpedia-node";
	public static String dbpediaGraph = "/home/fangyixiang/Desktop/CCS/dbpedia/dbpedia-graph";
	public static String dbpediaCCS = "";
//	
	
	
	//Dec 5, 2016 CYK: 
	public static String dblpNode="/Users/chenyankai/Desktop/ACQ/yankai_data/dblp/dblp-node.txt";
	public static String dblpGraph ="/Users/chenyankai/Desktop/ACQ/yankai_data/dblp/dblp-graph.txt";
	public static String dblpquery="/Users/chenyankai/Desktop/yankai_data/dblp/dblp-graph-query=100.txt";
	
	public static String flickrNode="/Users/chenyankai/Desktop/ACQ/yankai_data/flickr/flickr-node.txt";
	public static String flickrGraph="/Users/chenyankai/Desktop/ACQ/yankai_data/flickr/flickr-graph.txt";

	
	public static String DflickrGraph="/Users/chenyankai/Desktop/ACQ/yankai_data/DynamicGraph/flickr-graph.txt";
	public static String DflickrNode="/Users/chenyankai/Desktop/ACQ/yankai_data/DynamicGraph/flickr-nodes.txt";
	
	public static String youtubeGraph="/Users/chenyankai/Desktop/ACQ/yankai_data/DynamicGraph/youtube-graph.txt";
	public static String youtubeNode="/Users/chenyankai/Desktop/ACQ/yankai_data/DynamicGraph/youtube-nodes.txt";
	
	
	//query parameters
	public static int k = 6;//the degree constraint
	
	//the # of queryId examples
	public static int qIdNum = 300;
	
	//save parameters
	public static int ccsSizeThreshold = 50;//community size
	
	//log path
	public static String logFilePath = "./info/log" + System.currentTimeMillis();
	
	
	//CODICIL parameter
	public static int clusterK = 2500;//the number of clusters
	
}
