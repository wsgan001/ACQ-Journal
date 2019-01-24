//
// Created by Yankai Chen on 21/01/2019.
//

#ifndef HCC_SIMCALCULATOR_H
#define HCC_SIMCALCULATOR_H

#include "HIN_Graph.h"

using namespace std;

class simCalculator{
public:
    static double pathCount(HIN_Graph& hin_graph, Query_path& q_path);
    static double PathSim();
    static void PCRW();
};




#endif //HCC_SIMCALCULATOR_H
