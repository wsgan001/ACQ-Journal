import scipy

from scipy.stats import chisquare as st 
import numpy as np

# observe=np.array([25,38,40,20,37,44]);
# expect=scipy.stats.chisquare(observe);
# print(expect);

def func(mtrx):
   print(chi2_contingency(mtrx));
        
    
func([]);