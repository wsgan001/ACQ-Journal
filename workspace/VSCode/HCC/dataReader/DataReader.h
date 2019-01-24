//
// Created by Yankai Chen on 21/01/2019.
//

#ifndef HCC_DATAREADER_H
#define HCC_DATAREADER_H
#include <string>
#include <map>
#include <vector>
using namespace std;

class Edge{
public:
    int src;
    int dst;
    int edge_type;

    Edge(int x, int y, int z):src(x), dst(y), edge_type(z){}
};


class dataReader{

public:
    //to read data from the files
    static bool readADJ(string path, map<int, vector<Edge> >& map);
    static bool readNodeID2Type(string path, map<int, vector<int> >& nodeID_to_type );
    static bool readEdgeName(string path, map<int, string>& edge_Name );

    //to print the data for checking
    static void printADJ(const map<int, vector<Edge> >& map);
    static void printNodeID2Type(const map<int, vector<int> >& nodeID_to_type);
    static void printEdgeName(const map<int, string>& edge_Name);
 };


#endif //HCC_DATAREADER_H
