//
// Created by Yankai Chen on 21/01/2019.
//

#include "QueryPath.h"


QueryPath::QueryPath(HIN_Graph & hg): hin_graph_(hg) {};

QueryPath::QueryPath(const QueryPath &p): hin_graph_(p.hin_graph_) {
    this->node_Vec_ = p.node_Vec_;
    this->edge_Vec_ = p.edge_Vec_;
}

QueryPath & QueryPath::operator = (const QueryPath &p) {
    hin_graph_ = p.hin_graph_;
    node_Vec_ = p.node_Vec_;
     edge_Vec_ = p.edge_Vec_;
    return *this;
}




