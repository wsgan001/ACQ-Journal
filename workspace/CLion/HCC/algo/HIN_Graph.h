//
// Created by Yankai Chen on 21/01/2019.
//

#ifndef HCC_HIN_GRAPH_H
#define HCC_HIN_GRAPH_H

#include <string>
#include <iostream>
#include <vector>
#include <map>
using namespace std;

class HIN_Node{
public:
    int node_id;//key
    int node_type;
    string attr;//if necessary

    vector<int> next_Nodes;

    HIN_Node();
    HIN_Node(int& x, int& y);
    HIN_Node(int& x, int& y, string& z);
    bool connect(vector<int>& node_vec);


};


class HIN_Edge{
public:
    int srcID;
    int dstID;
    int edge_type;//key
    string attr;

    HIN_Edge();
    HIN_Edge(int& src, int& dst, int& type);
    HIN_Edge(int& src, int& dst, int& type, string& s);

};


class HIN_Graph{
public:
    map<int, HIN_Node> graph_nodes;
    map<int, vector<int> > nodeType_nodeID; //key:node_type, val: nodeIDs
    map<pair<int,int>, vector<HIN_Edge> > src_edges;//key:< srcID, dstID >val: HIN_edge

    HIN_Graph();

//    static bool buildGraph(map<int, vector<Edge> > adj, map<int,vector<int> > nodeID_to_type);

};




class Query_Node{
    int node_type; //key
    int id;//optional
    string attr;
    Query_Node();
    Query_Node(int x, int y, str z);
    bool operator == (HIN_Node& n);
};


class Query_Edge{
    int edge_type;//key
    int disID_type;
    int edge_type;
    string attr;//optional
    bool operator == (HIN_Edge& e);
};


class Query_Path{
    vector<Query_Node> node_vec;
    map< <pair<Query_Node,Query_Node>>, Query_Edge > path;
    Query_Path();
    Query_Path(string pathStr);

};


#endif //HCC_HIN_GRAPH_H
