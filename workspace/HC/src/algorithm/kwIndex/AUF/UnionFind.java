package algorithm.kwIndex.AUF;

import algorithm.kwIndex.AUF.UNode;

/**
@author chenyankai
@Date	Aug 10, 2017
Use anchor union find
*/
public class UnionFind {
	public void makeSet(UNode x){
		x.father = x;
		x.rank = 0;
		x.anchor = x.value;
	}
	
	public UNode find(UNode x){
		if(x.father != x){
			x.father = find(x.father);
		}
		return x.father;
	}
	
	public void union(UNode x, UNode y){
		UNode xRoot = find(x);
		UNode yRoot = find(y);
		
		if(xRoot ==yRoot) return ;
		
		if(xRoot.rank < yRoot.rank){
			xRoot.father = yRoot;
		}else if (xRoot.rank > yRoot.rank){
			yRoot.father = xRoot;
		}else{
			yRoot.father = xRoot;
			xRoot.rank = xRoot.rank+1;
		}
	}

}
