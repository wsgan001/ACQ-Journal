cmake_minimum_required(VERSION 3.9)
project(HCC)

set(CMAKE_CXX_STANDARD 11)
include_directories(Eigen /Users/chenyankai/gitRepository/workspace/CLion/HCC/library)


add_executable(HCC main.cpp
#        util/Extract.cpp
#        util/DBLP/dataProcess.cpp
#        util/DBLP/dataProcess.h
#        util/Tool.cpp
#        util/Tool.h
#        algo/SimCalculator.h
#        algo/SimCalculator.cpp
#        algo/QueryPath.h
#        algo/QueryPath.cpp
        algo/HIN_Graph.h
        algo/HIN_Graph.cpp
        dataReader/DataReader.h
        dataReader/DataReader.cpp)
