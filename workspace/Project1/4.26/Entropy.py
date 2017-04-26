import csv
import pandas as pd
import math


localPath="/Users/chenyankai/Desktop/";


def readFile(fileName):
    csvReader = pd.read_csv(localPath+fileName);
    dict={}; #key:id Value:dictionary
    dictNum={};#key:id value:number of songs one has listened
    
    for index,row in csvReader.iterrows():
        id=row['sub'];
        songId=row['song'];
        if(id!=id):break; # if id is not a number,break.
        if(dict.__contains__(id)):
            dictNum[id]+=1;
            dict1=dict.get(id);
            if(dict1.__contains__(songId)):
                dict1[songId]+=1;
            else:
                dict1[songId]=1;
        else:
            dictNum[id]=1;
            dict[id]={songId:1};
        
    return dict,dictNum;



def entory(index,dict,dictNum):
    sum=dictNum.get(index);
    value=0;
    dict1= dict.get(index);
    for i in dict1.keys():
        p=dict1.get(i)/sum;
        value+=-(p*math.log(p,2));
        
    return  value;
   
def WriteFile(list,fileName):
    df=pd.DataFrame(list);
    df.to_csv(localPath+fileName,index=False,header=['id','entory']);
    
def main(fileName,outfile):
    dict=readFile(fileName)[0];  
    dictNum=readFile(fileName)[1];
    list=[];
    for id in dict.keys():
        list.append((id,entory(id, dict, dictNum)));
    WriteFile(list, outfile);
    return list;


main("listening_history.csv","test.csv");