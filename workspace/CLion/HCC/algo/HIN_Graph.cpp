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
bool HIN_Node::connect(vector<int>& node_vec){
    this->next_Nodes = node_vec;
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

HIN_Edge::HIN_Edge(int &src, int &dst, int &type):srcID(src), dstID(dst), edge_type(type){}

HIN_Edge::HIN_Edge(int &src, int &dst, int &type, string& s):srcID(src), dstID(dst),edge_type(type), key(s){}



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
//bool HIN_Graph::buildGraph(map<int, vector<Edge> > adj, map<int,vector<int> > nodeID_to_type, map<int,string> edge_name){
//}


// ---------------------------------------------------------------
//
//          Query_Node
//
// ---------------------------------------------------------------
/**
 * @Description: construction function
 * @param:
 * @return:
 */
Query_Node::Query_Node():node_type(-1), id(-1), attr(" "){}

query_Node:: Query_Node(int x, int y, str z):node_type(x), id(y), attr(z){}

/**
 * @Description: operator == for path checking
 * @param: HIN_Node
 * @return: bool
 */
bool Query_Node::operator == (HIN_Node& n){
    if(this->node_type != n.node_type) return false;
    if( id!= -1 ) return
}

