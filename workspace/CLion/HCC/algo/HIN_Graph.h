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
    int node_id_;
    int node_type_;
    string attr_;//if necessary

    vector<int> linking_ids_;
};

class HIN_Edge{
public:
    int src_;
    int dst_;
    int edge_type_;
    string attr_;

    HIN_Edge();
    HIN_Edge(int& src, int& dst, int& type);
    bool operator == (const HIN_Edge& e);
};

class HIN_Graph{
public:
    map<int, HIN_Node> graph_nodes_;
    map<int, vector<HIN_Edge> > edge_src_;
    map<int, vector<HIN_Edge> > edge_dst_;


    map<int, int> edge_type_2_id_;


};



#endif //HCC_HIN_GRAPH_H
