//
// Created by Yankai Chen on 21/01/2019.
//

#include "HIN_Graph.h"


HIN_Edge::HIN_Edge() {}

HIN_Edge::HIN_Edge(int &src, int &dst, int &type):src_(src), dst_(dst),edge_type_(type){}


bool bool HIN_Edge::operator==(const HIN_Edge &e) {
    if(this->src_ != e.src_) return false;
    if(this->dst_ != e.dst_) return false;
    if(this->edge_type_ != e.edge_type_) return false;
    return true;
}

