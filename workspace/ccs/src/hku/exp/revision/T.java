package hku.exp.revision;

import hku.Config;
import hku.algo.DataReader;

import java.util.HashSet;
import java.util.Set;

/**
 * @author fangyixiang
 * @date Jun 2, 2016
 */
public class T {
	
	private boolean contain(String nodes[][], int id, Set<String> set){
		int count = 0;
		for(int i = 1;i < nodes[id].length;i ++){
			String word = nodes[id][i];
			if(set.contains(word)){
				System.out.println("word:" + word);
				count += 1;
			}
		}
		System.out.println("count:" + count);
		if(count == set.size())   return true;
		else                      return false;
	}
	
	public static void main(String[] args) {
		DataReader dataReader = new DataReader(Config.dblpGraph, Config.dblpNode);
		int graph[][] = dataReader.readGraph();
		String nodes[][] = dataReader.readNode();
		
		Set<String> set = new HashSet<String>();
		set.add("mine");
		set.add("data");
		set.add("pattern");
		set.add("database");
		
		T t = new T();
//		t.contain(nodes, 15418, set);
//		t.contain(nodes, 15765, set);//bu jiajun 0
//		t.contain(nodes, 15743, set);//dik lun lee 2
//		t.contain(nodes, 15910, set);//vipin kumar 3
//		t.contain(nodes, 5033, set);//2
//		t.contain(nodes, 17148, set);//sheng zhong
//		t.contain(nodes, 7617, set);//ramasamy uthurusamy
//		t.contain(nodes, 20079, set);//david lo
//		t.contain(nodes, 16170, set);
//		t.contain(nodes, 15239, set);//laks v. s. lakshmanan], set)
		t.contain(nodes, 261, set);//hans peter
	}

}
/*
/home/fangyixiang/Desktop/CCS/dblp/dblp-node the # of nodes in G:977288
/home/fangyixiang/Desktop/CCS/dblp/dblp-graph the # of edges in G:6864546
the average degree:7.024076833031819
the avg # of keywords in each node:11.835294201913868
1-select:18115[jae-gil lee]
1-select:17539[guozhu dong]
1-select:15252[philip s. yu]
1-select:17139[wei wang 0009]
1-select:16357[jiong yang]
1-select:1778[jeffrey xu yu]
1-select:11276[jianyong wang]
1-select:15386[jian pei]
2-select:20079[david lo] 3212[limsoon wong]
2-select:18439[yu zheng] 18819[sanjay chawla]
2-select:7617[ramasamy uthurusamy] 18916[padhraic smyth]
2-select:15765[jiajun bu] 15793[chengqi zhang]
2-select:15239[laks v. s. lakshmanan] 1645[toon calders]
2-select:15239[laks v. s. lakshmanan] 16791[fosca giannotti]
2-select:15239[laks v. s. lakshmanan] 17643[dino pedreschi]
2-select:17148[sheng zhong] 3239[aidong zhang]
2-select:15857[reynold cheng] 17331[ben kao]
2-select:15265[beng chin ooi] 15264[mong-li lee]
2-select:16170[bhavani m. thuraisingham] 15328[shashi shekhar]
2-select:5033[umeshwar dayal] 15637[elke a. rundensteiner]
2-select:15743[dik lun lee] 15908[wilfred ng]
2-select:261[hans-peter kriegel] 19790[jörg sander]
2-select:15418[wen-chih peng] 500990[zhung-xun liao]
2-select:15418[wen-chih peng] 19244[suh-yin lee]
2-select:15398[david wai-lok cheung] 17905[yongqiao xiao]
2-select:15910[vipin kumar] 15327[jaideep srivastava]

k:6 size:27
19790:jörg sander
500990:zhung-xun liao
17539:guozhu dong
1645:toon calders
16791:fosca giannotti
15238:jiawei han
17331:ben kao
18819:sanjay chawla
19244:suh-yin lee
1778:jeffrey xu yu
15793:chengqi zhang
15327:jaideep srivastava
11276:jianyong wang
15252:philip s. yu
3212:limsoon wong
17139:wei wang 0009
15386:jian pei
17905:yongqiao xiao
18115:jae-gil lee
16357:jiong yang
17643:dino pedreschi
15328:shashi shekhar
18916:padhraic smyth
15264:mong-li lee
3239:aidong zhang
15908:wilfred ng
15637:elke a. rundensteiner

1-select:18115[jae-gil lee]
1-select:17539[guozhu dong]
1-select:15252[philip s. yu]
1-select:17139[wei wang 0009]
1-select:16357[jiong yang]
1-select:1778[jeffrey xu yu]
1-select:11276[jianyong wang]
1-select:15386[jian pei]
2-select:20079[david lo] 3212[limsoon wong]
2-select:18439[yu zheng] 18819[sanjay chawla]
2-select:7617[ramasamy uthurusamy] 18916[padhraic smyth]
2-select:15765[jiajun bu] 15793[chengqi zhang]
2-select:15239[laks v. s. lakshmanan] 1645[toon calders]
2-select:15239[laks v. s. lakshmanan] 16791[fosca giannotti]
2-select:15239[laks v. s. lakshmanan] 17643[dino pedreschi]
2-select:17148[sheng zhong] 3239[aidong zhang]
2-select:15857[reynold cheng] 17331[ben kao]
2-select:15265[beng chin ooi] 15264[mong-li lee]
2-select:16170[bhavani m. thuraisingham] 15328[shashi shekhar]
2-select:5033[umeshwar dayal] 15637[elke a. rundensteiner]
2-select:15743[dik lun lee] 15908[wilfred ng]
2-select:261[hans-peter kriegel] 19790[jörg sander]
2-select:15418[wen-chih peng] 500990[zhung-xun liao]
2-select:15418[wen-chih peng] 19244[suh-yin lee]
2-select:15398[david wai-lok cheung] 17905[yongqiao xiao]
2-select:15910[vipin kumar] 15327[jaideep srivastava]

k:8 size:27
19790:jörg sander
500990:zhung-xun liao
17539:guozhu dong
1645:toon calders
16791:fosca giannotti
15238:jiawei han
17331:ben kao
18819:sanjay chawla
19244:suh-yin lee
1778:jeffrey xu yu
15793:chengqi zhang
15327:jaideep srivastava
11276:jianyong wang
15252:philip s. yu
3212:limsoon wong
17139:wei wang 0009
15386:jian pei
17905:yongqiao xiao
18115:jae-gil lee
16357:jiong yang
17643:dino pedreschi
15328:shashi shekhar
18916:padhraic smyth
15264:mong-li lee
3239:aidong zhang
15908:wilfred ng
15637:elke a. rundensteiner

1-select:18115[jae-gil lee]
1-select:17539[guozhu dong]
1-select:15252[philip s. yu]
1-select:17139[wei wang 0009]
1-select:16357[jiong yang]
1-select:1778[jeffrey xu yu]
1-select:11276[jianyong wang]
1-select:15386[jian pei]
2-select:20079[david lo] 3212[limsoon wong]
2-select:18439[yu zheng] 18819[sanjay chawla]
2-select:7617[ramasamy uthurusamy] 18916[padhraic smyth]
2-select:15765[jiajun bu] 15793[chengqi zhang]
2-select:15239[laks v. s. lakshmanan] 1645[toon calders]
2-select:15239[laks v. s. lakshmanan] 16791[fosca giannotti]
2-select:15239[laks v. s. lakshmanan] 17643[dino pedreschi]
2-select:17148[sheng zhong] 3239[aidong zhang]
2-select:15857[reynold cheng] 17331[ben kao]
2-select:15265[beng chin ooi] 15264[mong-li lee]
2-select:16170[bhavani m. thuraisingham] 15328[shashi shekhar]
2-select:5033[umeshwar dayal] 15637[elke a. rundensteiner]
2-select:15743[dik lun lee] 15908[wilfred ng]
2-select:261[hans-peter kriegel] 19790[jörg sander]
2-select:15418[wen-chih peng] 500990[zhung-xun liao]
2-select:15418[wen-chih peng] 19244[suh-yin lee]
2-select:15398[david wai-lok cheung] 17905[yongqiao xiao]
2-select:15910[vipin kumar] 15327[jaideep srivastava]

k:10 size:27
19790:jörg sander
500990:zhung-xun liao
17539:guozhu dong
1645:toon calders
16791:fosca giannotti
15238:jiawei han
17331:ben kao
18819:sanjay chawla
19244:suh-yin lee
1778:jeffrey xu yu
15793:chengqi zhang
15327:jaideep srivastava
11276:jianyong wang
15252:philip s. yu
3212:limsoon wong
17139:wei wang 0009
15386:jian pei
17905:yongqiao xiao
18115:jae-gil lee
16357:jiong yang
17643:dino pedreschi
15328:shashi shekhar
18916:padhraic smyth
15264:mong-li lee
3239:aidong zhang
15908:wilfred ng
15637:elke a. rundensteiner

*/