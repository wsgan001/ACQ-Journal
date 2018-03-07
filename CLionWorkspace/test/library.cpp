#include "library.h"

#include <iostream>
#include <string>
#include <vector>
using namespace std;

int value(){

    int a[3][4]={0,1,2,3,4,5,6,7,8,9,10,11};
    int (*p)[4] = &a[1];  cout << ** p << endl;
    for (int *q = *p;q!=*p+4;q++) cout<< *q <<endl;

//    for(int *p1 = p+4;p!=p1;p++) cout<< *p <<endl;

    return 0;
}


int main() {
    value();

    return 0;
}

