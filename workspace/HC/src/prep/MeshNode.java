package prep;

import java.util.ArrayList;
import java.util.Set;

public class MeshNode {
	private String name=null;
	private String code=null;
	private int number=-1;
	private ArrayList<MeshNode> childrenList=null;
	
	public MeshNode(String name,String code, int number){
		this.name = name;
		this.code = code;
		this.number =number;
		this.childrenList = new ArrayList<MeshNode>();
	}
	
	public void setNum(int x){
		this.number=x;
	}
	
	public void setChild(ArrayList<MeshNode> list){
		this.childrenList=list;
	}
	
	
	public String getName(){return this.name;}
	public String getCode(){return this.code;}
	public int getNum(){return this.number;}
	
	public ArrayList<MeshNode> getChildrenList(){return this.childrenList;}
	
	
	
	
	
}
