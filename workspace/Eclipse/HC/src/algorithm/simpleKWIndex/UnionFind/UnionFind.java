package algorithm.simpleKWIndex.UnionFind;
/**
@author chenyankai
@Date	Sep 26, 2017
 union find operations
*/
public class UnionFind {
	
	public void makeSet(UNode x){
		x.root = x;
		x.rank = 0;		
	}
	
	public UNode find(UNode x){
		if(x.root!=x) {
			x.root = find(x.root);
		}
	return x.root;
	}

	public void union(UNode x,UNode y){
		UNode xRoot = find(x);
		UNode yRoot = find(y);
		
		if(xRoot==yRoot) return;
		
		//x and y are not already in same set. Merge them.
		if(xRoot.rank < yRoot.rank){
			xRoot.root = yRoot;
		}else if(xRoot.rank > yRoot.rank){
			yRoot.root = xRoot;
		}else{
			yRoot.root = xRoot;
			xRoot.rank = xRoot.rank + 1;
		}
		
	}
	
	
}
