from src.adaBoost import *
from src.config import *


dataArr, labelArr = loadDataSet(horseColicTraining2)
classifierArray = adaBoostTrainDS(dataArr, labelArr, 10)
testArr, testLabelArr = loadDataSet(horseColicTest2)
prediction = adaClassify(testArr, classifierArray)
errArr = np.mat(np.ones((67, 1)))
sum = errArr[prediction != np.mat(testLabelArr).T].sum()
print(sum)







