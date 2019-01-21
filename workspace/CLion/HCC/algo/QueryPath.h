//
// Created by Yankai Chen on 21/01/2019.
//

#ifndef HCC_QUERYPATH_H
#define HCC_QUERYPATH_H

#include <string>
#include <vector>
#include "HIN_Graph.h"
using namespace std;

class PathNode{
public:
    int id_;
    int type_;
    int next_;
    string attr_;
};


class PathEdge{
public:
    int src_;
    int dst_;
    int edge_type_;
    string attr_;
};


class QueryPath{
public:
    HIN_Graph & hin_graph_;
    vector< vector<PathNode> > node_Vec_;
    vector< vector<PathEdge> > edge_Vec_;


    QueryPath(HIN_Graph & hin_graph);
    QueryPath(const QueryPath & p);

    QueryPath & operator = (const QueryPath & p);

};


#endif //HCC_QUERYPATH_H
