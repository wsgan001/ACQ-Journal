//
// Created by Yankai Chen on 19/01/2019.
//
#include "dataProcess.h"
#include <map>
map<int, int> old2new;
map<int, int> new2old;

//read index file
void readIndexFile(string fileNameIn, string fileNameOut){
    ifstream infile(fileNameIn);
    cout <<  "reading file completed" << endl;
    string line;

    if(infile){
        int newID = 0;
        while(getline(infile,line)){
            vector<string> str;
            split(line, str, "\t");
            int old = stoi(str[0]);
//            cout << old << "   " << newID << endl;
            old2new.insert(pair<int, int> (old, newID));
            new2old.insert(pair<int, int> (newID++, old));
        }
    }
    infile.close();


    ofstream outfile(fileNameOut);
    map<int, int>:: iterator iter = new2old.begin();
    while(iter != new2old.end()){
        outfile << iter->first << "\t" <<iter->second << endl;
        iter++;
    }
    outfile.close();
}


void processData(string fileNameIn, string fileNameOut){
    map<int, vector<int>> newIDEdges;

    ifstream infile(fileNameIn);
    cout << "reading file"<<endl;
    string line;


    //read old records
    if(infile){
        int edgeAll = 0;
        while(getline(infile, line)){
            vector<string> str;
            split(line,str, "\t");
            int head = stoi(str[0]);
            int tail = stoi(str[1]);
            int edge = stoi(str[2]);
            int newHead = old2new.at(head);
            int newTail = old2new.at(tail);
            map<int, vector<int>>::iterator iter = newIDEdges.find(newHead);
            if(iter == newIDEdges.end()){
                vector<int> vec;
                vec.push_back(newTail);
                newIDEdges.insert(pair<int, vector<int>> (newHead, vec));
            }else{
                (iter->second).push_back(newTail);
            }
        }
    }else{
        cout<<"no such file"<<endl;
    }
    infile.close();
    cout<<"read file closed"<<endl;

    //write newID and its edges
    ofstream outfile(fileNameOut);

    for(int i = 0; i<newIDEdges.size();i++){
        cout << i << endl;
        vector<int> tails =  newIDEdges.at(i);
        for(int x: tails) {

            string line = to_string(i)+ "\t" + to_string(x);
            outfile << line << endl;
        }
    }

    outfile.close();

}




int main(){

    readIndexFile(fileLocation+"dblpTotalType.txt",fileLocation+"newIndex.txt");
    processData(fileLocation + "dblpAdj.txt",fileLocation + "new.txt");

}
