package prep;

import java.util.*;
import java.util.Set;

import algorithm.ProfiledTree.PNode;

/**
 * 
 * @author chenyankai
 * @date Apr 26, 2017
 */

public class MeshNode {
	private String name=null;
	private String oldCode=null;
	private String newCode=null;
	private int DFSNo=-1;
	private int id=-1;
	//9.14 added to check the depth
	private int depth=-1;
	private List<MeshNode> childrenList=null;
	
	public MeshNode(String name,String code, int number){
		this.name = name;
		this.oldCode = code;
		this.id=number;
		this.childrenList = new ArrayList<MeshNode>();
	}
	
	public void setNum(int x){
		this.id=x;
	}
	
	public void setDFSNo(int x){
		this.DFSNo=x;
	}
	
	public void setCode(String x){
		this.newCode=x;
	}
	
	public void setDepth(int x){
		this.depth = x;
	}
	
	public int getDepth(){
		return this.depth;
	}
	
	public void setChild(List<MeshNode> list){
		this.childrenList=list;
	}
	
	
	
	public String getName(){return this.name;}
	public String getoldCode(){return this.oldCode;}
	public String getNewCode(){return this.newCode;}
	public int getDFSNo(){return this.DFSNo;}
	public int getNum(){return this.id;}
	
	public List<MeshNode> getChildrenList(){return this.childrenList;}
	
	public String toString(String indent){
		StringBuilder output = new StringBuilder();
		if(this.depth<=ConfigPubmed.maxDepth){
			output.append("ID:  "+this.newCode+" DFS No. "+this.DFSNo+"\n");
			String newIndent = indent + "   ";
			for (MeshNode child : childrenList) {
				output.append(newIndent+ child.toString(newIndent));
			}
		}
		
		return output.toString();
	}

	
	
	
}
