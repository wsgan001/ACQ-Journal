package algorithm.kwIndex.AUF;

import algorithm.kwIndex.AUF.UNode;

/**
@author chenyankai
@Date	Aug 10, 2017
Use anchor union find 
*/
public class UNode {
  public int value = 0;
  public UNode father = null;
  public int rank = -1;
  public int anchor = -1;
  
  public UNode(int x){
	  this.value = x;
  }
  
}


