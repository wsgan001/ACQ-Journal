from src.svmMLiA import *
from src.config import *


dataArr, labelArr = loadDataSetDemo(testSet)
b, alphas = smoP(dataArr, labelArr, 0.6, 0.001, 40)
print(b, alphas[alphas>0])