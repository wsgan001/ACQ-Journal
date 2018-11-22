#include <iostream>
class C{
    public:
        int cyk;
};

class d {
protected:
    int x;
};
class A:public C{
    friend class d;
    public:
    class B;
     class B:public C{
        int xbw = d.x;

    };

};




int main() {

    A a1;


    std::cout << "Hello, World!" << std::endl;
    return 0;
}