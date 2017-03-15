package hku.algo.index.unionFind;

/**
 * @author fangyixiang
 * @date Sep 16, 2015
 */
public class UNode {
	public int value = 0;
	public UNode parent = null;
	public int rank = -1;
	public int represent = -1; //this variable is used for updating our tree index
	//**********************  Dec 6, 2016 CYK: represent is THE Anchor and only the parent node has valid anchored value! 
	
	public UNode(int value){
		this.value = value;
	}
	
}
