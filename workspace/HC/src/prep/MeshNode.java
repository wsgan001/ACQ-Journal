package prep;

import java.util.ArrayList;
import java.util.Set;

/**
 * 
 * @author chenyankai
 * @date Apr 26, 2017
 */

public class MeshNode {
	private String name=null;
	private String oldCode=null;
	private String newCode=null;
	private int id=-1;
	private ArrayList<MeshNode> childrenList=null;
	
	public MeshNode(String name,String code, int number){
		this.name = name;
		this.oldCode = code;
		this.id=number;
		this.childrenList = new ArrayList<MeshNode>();
	}
	
	public void setNum(int x){
		this.id=x;
	}
	
	public void setCode(String x){
		this.newCode=x;
	}
	
	public void setChild(ArrayList<MeshNode> list){
		this.childrenList=list;
	}
	
	
	
	public String getName(){return this.name;}
	public String getoldCode(){return this.oldCode;}
	public String getNewCode(){return this.newCode;}
	public int getNum(){return this.id;}
	
	public ArrayList<MeshNode> getChildrenList(){return this.childrenList;}
	
	
	
	
	
}
