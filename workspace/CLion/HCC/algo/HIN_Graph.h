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
    int node_id;
    int node_type;
    string key;//if necessary

    vector<int> next_Nodes;
    vector<int> next_edges;
    HIN_Node();
    HIN_Node(int& x, int& y);
    HIN_Node(int& x, int& y, string& z);
    bool connect(vector<int>& node_vec, vector<int>& edge_vec);

    bool operator = (const HIN_Node& v);
};


class HIN_Edge{
public:
    int src;
    int dst;
    int edge_type;
    string key;

    HIN_Edge();
    HIN_Edge(int& src, int& dst, int& type);
    HIN_Edge(int& src, int& dst, int& type, string& s);
    bool operator == (const HIN_Edge& e);
};


class HIN_Graph{
public:
    map<int, HIN_Node> graph_nodes;
    map<int, HIN_Edge> src_edges;//key:src_nodeID val: HIN_edge
    map<int, HIN_Edge> dst_edges;

    HIN_Graph();

    static bool buildGraph(map<int, vector<Edge> > adj, map<int,vector<int> > nodeID_to_type);

};



#endif //HCC_HIN_GRAPH_H
