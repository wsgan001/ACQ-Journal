//
// Created by Yankai Chen on 21/01/2019.
//

#include "HIN_Graph.h"



// -----------------------------------------------------
//
//             HIN_Node
//
// ------------------------------------------------------
/**
 * @Description: three construction functions of CLASS HIN_Node 
 * @param:
 * @return:
 */
HIN_Node::HIN_Node() {}

HIN_Node::HIN_Node(int &x, int &y):node_id(x), node_type(y){}

HIN_Node::HIN_Node(int &x, int &y, string &z):node_id(x), node_type(y), key(z) {}

/**
 * @Description: assign the neighbour nodes and edges to a node
 * @param: vector<int>& *2: node_vec and edge_vec
 * @return: bool
 */
bool HIN_Node::connect(vector<int>& node_vec, vector<int>& edge_vec){
    this->next_Nodes = node_vec;
    this->next_edges = edge_vec;
    return true;
}

// -----------------------------------------------------
//
//             HIN_Edge
//
// ------------------------------------------------------

/**
 * @Description: three construction functions of CLASS HIN_Edge
 * @param: 
 * @return: 
 */
HIN_Edge::HIN_Edge() {}

HIN_Edge::HIN_Edge(int &src, int &dst, int &type):src(src), dst(dst), edge_type(type){}

HIN_Edge::HIN_Edge(int &src, int &dst, int &type, string& s):src(src), dst(dst),edge_type(type), key(s){}


/**
 * @Description: operator == overloading funciton
 * @param: const HIN_Edge
 * @return: bool
 */
bool HIN_Edge::operator==(const HIN_Edge &e) {
    if(this->src != e.src) return false;
    if(this->dst != e.dst) return false;
    if(this->edge_type != e.edge_type) return false;
    return true;
}


// -----------------------------------------------------
//
//             HIN_Graph
//
// ------------------------------------------------------
/**
 * @Description: construction function for CLASS HIN_Graph
 * @param:
 * @return:
 */
HIN_Graph::HIN_Graph() {}

/**
 * @Description: build a HIN_Graph object
 * @param:
 * @return:
 */
bool HIN_Graph::buildGraph(map<int, vector<Edge> > adj, map<int,vector<int> > nodeID_to_type, map<int,string> edge_name){


}
