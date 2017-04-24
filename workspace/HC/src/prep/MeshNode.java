package prep;

import java.util.ArrayList;
import java.util.Set;

public class MeshNode {
	private String name=null;
	private int code=-1;
	private ArrayList<MeshNode> childrenList=null;
	
	public MeshNode(String name,int code){
		this.name = name;
		this.code = code;
		this.childrenList = new ArrayList<MeshNode>();
	}
	
	public void setChild(ArrayList<MeshNode> list){
		this.childrenList=list;
	}
	
	public String getName(){return this.name;}
	public int getCode(){return this.code;}
	
	public ArrayList<MeshNode> getChildrenList(){return this.childrenList;}
	
	
	
	
	
}
