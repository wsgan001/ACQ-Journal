package hku.exp.revision;

import hku.Config;

/**
 * @author fangyixiang
 * @date May 16, 2016
 */
public class MainTest {

	public static void main(String[] args) {
//		LocalDecExp lde = new LocalDecExp();
//		lde.exp(Config.flickrGraph, Config.flickrNode);
//		lde.exp(Config.dblpGraph, Config.dblpNode);
//		lde.exp(Config.tencentGraph, Config.tencentNode);
//		lde.exp(Config.dbpediaGraph, Config.dbpediaNode);
		
		InvertedListExp ive = new InvertedListExp();
		ive.exp(Config.flickrGraph, Config.flickrNode);
		ive.exp(Config.dblpGraph, Config.dblpNode);
		ive.exp(Config.tencentGraph, Config.tencentNode);
		ive.exp(Config.dbpediaGraph, Config.dbpediaNode);
	}

}
