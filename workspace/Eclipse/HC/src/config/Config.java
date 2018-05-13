package config;

public class Config {

	public static String workSpace = "/home/fangyixiang/Desktop/HC/log/";
	
	public static String pubMedDataWorkSpace = "/home/fangyixiang/Desktop/HC/dataspace/pubmed/";
	public static String acmccsDataWorkSpace = "/home/fangyixiang/Desktop/HC/dataspace/ACM_CCS/";
	public static String DBLPDataWorkSpace="/home/fangyixiang/Desktop/HC/dataspace/DBLP/";
	public static String FlickrDataWorkSpace="/home/fangyixiang/Desktop/HC/dataspace/Flickr/";
	
	public static int k=4;
	

	
	public static String logFileName=workSpace+System.currentTimeMillis();
	
	public static String ACMDLGraph = acmccsDataWorkSpace +"edges.txt";
	public static String ACMDLNode = acmccsDataWorkSpace +"nodes.txt";
	public static String ACMDLCPtree= acmccsDataWorkSpace +"CPTree.txt";
	
	public static String pubMedGraph =pubMedDataWorkSpace+"edge-40.txt";
	public static String pubMedNode =	pubMedDataWorkSpace+"node-40.txt";
	public static String pubmedCPtree = pubMedDataWorkSpace+"cptree.txt";
	
	public static String dblpGraph = DBLPDataWorkSpace+"dblp-pcs-graph";
	//average keywords:37.79
	public static String dblpNode1 = DBLPDataWorkSpace+"dblp-pcs-node-1.txt";
	//average keywords:67.25
//	public static String dblpNode2 = DBLPDataWorkSpace+"dblp-pcs-node-2.txt"; 
	//average keywords:93.56
//	public static String dblpNode3 = DBLPDataWorkSpace+"dblp-pcs-node-3.txt";

	public static String DBLPCPTree= DBLPDataWorkSpace+"CPTree";

	public static String FlickrGraph = FlickrDataWorkSpace+"flickr-pcs-graph.txt";
	

	//average keywords:26.6
	public static String FlickrNode1 = FlickrDataWorkSpace+"flickr-pcs-node-1.txt";
	//average keywords:47
//	public static String FlickrNode2 = FlickrDataWorkSpace+"flickr-pcs-node-2.txt";
	//average keywords:64
//	public static String FlickrNode3 = FlickrDataWorkSpace+"flickr-pcs-node-3.txt";
	
	public static String FlickrCPTree= FlickrDataWorkSpace+"CPTree.txt";
	
	
	public static String ACMDLDiversityQueryFile4 = acmccsDataWorkSpace+"queryDiversityFile4.txt";
	public static String ACMDLDiversityQueryFile5 = acmccsDataWorkSpace+"queryDiversityFile5.txt";
	public static String ACMDLDiversityQueryFile6 = acmccsDataWorkSpace+"queryDiversityFile6.txt";
	public static String ACMDLDiversityQueryFile7 = acmccsDataWorkSpace+"queryDiversityFile7.txt";
	public static String ACMDLDiversityQueryFile8 = acmccsDataWorkSpace+"queryDiversityFile8.txt";
	
	public static String pubMedDiversityQueryFile4 = pubMedDataWorkSpace+"queryDiversityFile4.txt";
	public static String pubMedDiversityQueryFile5 = pubMedDataWorkSpace+"queryDiversityFile5.txt";
	public static String pubMedDiversityQueryFile6 = pubMedDataWorkSpace+"queryDiversityFile6.txt";
	public static String pubMedDiversityQueryFile7 = pubMedDataWorkSpace+"queryDiversityFile7.txt";
	public static String pubMedDiversityQueryFile8 = pubMedDataWorkSpace+"queryDiversityFile8.txt";
	
	public static String dblpDiversityQueryFile4 = DBLPDataWorkSpace+"queryDiversityFile4.txt";
	public static String dblpDiversityQueryFile5 = DBLPDataWorkSpace+"queryDiversityFile5.txt";
	public static String dblpDiversityQueryFile6 = DBLPDataWorkSpace+"queryDiversityFile6.txt";
	public static String dblpDiversityQueryFile7 = DBLPDataWorkSpace+"queryDiversityFile7.txt";
	public static String dblpDiversityQueryFile8 = DBLPDataWorkSpace+"queryDiversityFile8.txt";
	
	public static String flickrDiversityQueryFile4 = FlickrDataWorkSpace+"queryDiversityFile4.txt";
	public static String flickrDiversityQueryFile5 = FlickrDataWorkSpace+"queryDiversityFile5.txt";
	public static String flickrDiversityQueryFile6 = FlickrDataWorkSpace+"queryDiversityFile6.txt";
	public static String flickrDiversityQueryFile7 = FlickrDataWorkSpace+"queryDiversityFile7.txt";
	public static String flickrDiversityQueryFile8 = FlickrDataWorkSpace+"queryDiversityFile8.txt";
	
	
	
	
	
	
}
