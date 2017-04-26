import csv
import Config
import pandas as pd


def readFile(fileName):
    csvReader = pd.read_csv(Config.localPath+fileName);
    dict={};
    for index,row in csvReader.head(226).iterrows():
        id=row['sub'];
        songId=row['song'];
        if(dict.__contains__(id)):
            dict1=dict.get(id);
            if(dict1.__contains__(songId)):
                dict1.update({songId:dict1.get(songId)+1});
            else:
                dict1.update({songId:1});
        else:
         dict.update({id:{}});   
                       
        
    print(dict);
        
readFile("listening_history.csv");