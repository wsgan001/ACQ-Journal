import pandas as pd
import pandas_datareader as web   # Package and modules for importing data; this code may change depending on pandas version
import datetime
from bokeh.layouts import column
import numpy as np
dates=pd.date_range('20130101',periods=6)

def createDf():
    df=pd.DataFrame(np.random.randn(6,4),index=dates,columns=list('ABCD'))
    return df;

df2=pd.DataFrame([[1,3,4],[1,2,3],[2,5,1]])



# df2=pd.DataFrame({ ("a",createDf()),
#      ("b",createDf()),
#      ("c",createDf()),
#      ("d",createDf()),
#      ("e",createDf()),
#      ("f",createDf()) } 


count=2;
print(np.log(count).var())


    