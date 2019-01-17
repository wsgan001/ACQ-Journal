cmake_minimum_required(VERSION 3.9)
project(HCC)

set(CMAKE_CXX_STANDARD 11)
include_directories(Eigen /Users/chenyankai/gitRepository/CLionWorkspace/HCC/library)


add_executable(HCC main.cpp util/Extract.cpp)
