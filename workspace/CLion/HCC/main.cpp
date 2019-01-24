//
// Created by Yankai Chen on 17/01/2019.
//

#include <iostream>
#include "dataReader/DataReader.h"

using namespace std;

int main(){
    map<int, vector<Edge>> adj;
    map<int, vector<int> > nodeID_to_type;
    map<int, string> edge_name;
    string location = "/Users/chenyankai/Documents/HINDataset/DBLP/";
    string dblpADJFile = location + "dblpAdj.txt";
    string nodeID2TypeFile = location + "dblpTotalType.txt";
    string edgeNameFile = location + "dblpType.txt";

    dataReader::readADJ(dblpADJFile, adj);
    dataReader::readNodeID2Type(nodeID2TypeFile, nodeID_to_type);
    dataReader::readEdgeName(edgeNameFile, edge_name);

    dataReader::printADJ(adj);


//    dataReader::printNodeID2Type(nodeID_to_type);
//    dataReader::printEdgeName(edge_name);
};


