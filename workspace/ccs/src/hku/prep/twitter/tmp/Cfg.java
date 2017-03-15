/**
 * 
 */
package hku.prep.twitter.tmp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author fang
 * @date 2012-12-10
 */
public class Cfg {
	public static String version = "v16.0";
	public static String rootPath = "e:\\quirky\\";
	public static int ideaNumPage = 400;
	public static int activePage = 10;
	public static int considerPage = 10;
	public static int teamPage = 1;

	public static int connectTime = 7000;//请求时间
	public static int downSlot = 100;// 下载的时间间隔
	public static int downMaxNum = 8;// 若资源难以下载，其最大重复次数

	public static String dateStr = "";
	public static String logStr = "";
	public static String ideaPath = "";
	public static String shopPath = "";
	public static String userPath = "";
	public static String upcomingPath = "";
	public static String projectPath = "";
	public static String testPath = "";
	public static String ipPath = "";
	public static String xlsPath = "";
	public static String logPath = "";
	
	public static boolean previousState = true;
	public static int countProxy = 0;
	public static int countProxyThreshold = 20;
	public static int countProxyUpdate = 5000;//每隔5000次更新一次代理
	
	public static void setValue(){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		dateStr = year + "-" + month + "-" + day;
		logStr = "log-" + year + "-" + month + "-" + day + "-" + hour + "-" + minute + "-" + second;
		
		ideaPath = rootPath + dateStr + "\\webpage\\idea";
		shopPath = rootPath + dateStr + "\\webpage\\shop";
		userPath = rootPath + dateStr + "\\webpage\\user";
		upcomingPath = rootPath + dateStr + "\\webpage\\upcoming";
		projectPath = rootPath + dateStr + "\\webpage\\project";
		ipPath = rootPath + dateStr + "\\webpage\\ip";
		testPath = rootPath + dateStr + "\\webpage";
		xlsPath = rootPath + dateStr;
		logPath = rootPath + dateStr;
		
//		countProxyThreshold = Config.downLoadState * 5;//连续5个页面下载失败重新就更换ip
	}
	
	//downLoadState, 1:succeed, 0:fail, 2: ban, 3: finished
	public static int downLoadState = 1;
	
	public static Set<String> userIDSet = new HashSet<String>();
		
	//代理
	public static int ipIndex = -1;
	public static String ip[][] = {
		{"61.247.45.35", "8080"},
		{"188.138.115.15", "3128"},
		{"82.99.250.36", "8080"},
		{"222.73.233.146", "82"},
		{"221.130.18.152", "80"},
		{"89.218.101.34", "9090"},
		{"213.0.88.86", "8080"},
		{"178.21.112.27", "3128"},
		{"122.52.125.100", "8080"},
		{"183.232.25.44", "80"},
		{"217.25.23.219", "3128"},
		{"222.187.222.118", "8080"}
	};
	
	public List<String> userIDList = new ArrayList<String>();
}
