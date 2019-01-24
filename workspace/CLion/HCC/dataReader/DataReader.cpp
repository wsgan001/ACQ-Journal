//
// Created by Yankai Chen on 22/01/2019.
//
#include "DataReader.h"
#include <fstream>
#include <sstream>
#include <iostream>
using namespace std;

// ---------------------------------------------------------------
//
//             read functions
//
// ---------------------------------------------------------------

/**
 * @Description: To read adjacency edges from file
 * @param: string:filePath;      map<int, vector<Edge> >& :adjacency content
 * @return: true
 */
bool dataReader::readADJ(string path, map<int, vector<Edge> >& adj_Map) {
    ifstream adj_in(path.c_str(), ios::in);
    int src, dst, edge_type;

    while(adj_in >> src >> dst >> edge_type){
        Edge temp_Edge(src, dst, edge_type);
        if(adj_Map.find(src) == adj_Map.end()){
            vector<Edge> tmp;
            adj_Map.insert(make_pair(src, tmp));
        }
        adj_Map[src].push_back(temp_Edge);
    }
    adj_in.close();
 return true;
}


/**
 * @Description: To read neighbour types of one node from file
 * @param: string: filePath;    map<int, vector<int>>: node and its neighbours' types
 * @return: true
 */
bool dataReader::readNodeID2Type(string path, map<int, vector<int> > &nodeID_to_type) {
    ifstream inStream(path.c_str(), ios::in);
    string line;
    int id;
    int typeID;
    while(getline(inStream, line)){
        istringstream istr(line);
        istr >> typeID;
        while(istr >> id){
            nodeID_to_type[typeID].push_back(id);
        }

    }
    inStream.close();
    return true;

}


/**
 * @Description: To read edge names from file
 * @param: string: file path;  map<int, string> edge name map
 * @return: true
 */
bool dataReader::readEdgeName(string path, map<int, string> &edge_Name) {
    ifstream inStream(path.c_str(), ios::in);
    int id;
    string name;
    while(inStream >> name >> id){
        edge_Name[id] = name;
    }
    inStream.close();
    return true;
}



// ---------------------------------------------------------------
//
//          print functions
//
// ---------------------------------------------------------------
/**
 * @Description: print adj_map
 * @param: const map<int, vector<Edge>>: adj_map
 * @return: void
 */
void dataReader::printADJ(const map<int, vector<Edge> > &adj_Map) {
    cout << "now checking ADJ map." <<endl;
    cout << "map size: " << adj_Map.size() << endl;

    int edgeNum = 0;
    for(map<int, vector<Edge>>::const_iterator iter = adj_Map.begin(); iter != adj_Map.end(); iter++){
        edgeNum += iter->second.size();
    }
    cout << "Total Edge number: " << edgeNum <<endl;
    cout << "******************************************" <<endl;
}

/**
 * @Description: print nodeID2Type
 * @param: const map<int, vector<int> >& nodeID_to_type: node and its neighbours' types
 * @return: void
 */
void dataReader::printNodeID2Type(const map<int, vector<int> >& nodeID_to_type){
    cout << "now checking NodeID_to_Type map:" << endl;
    int count = 0;

    map<int, vector<int> >::const_iterator iter = nodeID_to_type.begin();
    for(; iter != nodeID_to_type.end(); iter++){
        count += iter->second.size();
    }
    cout << "Total number: " << count <<endl;
    cout << "******************************************" <<endl;
}

/**
 * @Description: print edgeName
 * @param: const map<int, string> &edge_Name: edge name map
 * @return: void
 */
void dataReader::printEdgeName(const map<int, string> &edge_Name) {
    cout << "now checking edge_Name map:" << endl;
    int count = edge_Name.size();

    cout << "Total Edge number: " << count <<endl;
    cout << "******************************************" <<endl;
}
