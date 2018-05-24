from src.regTrees import *
from src.config import *

# data = loadDataSet(exp2)
# matrix = np.mat(data)
# tree = createTree(matrix, modelLeaf, modelErr, (1,10))
# print(tree)

trainMat = np.mat(loadDataSet(bikeTrain))
testMat = np.mat(loadDataSet(bikeTest))
tree = createTree(trainMat, ops=(1, 20))
yHat = createForeCast(tree, testMat[:, 0])
cor1 = np.corrcoef(yHat, testMat[:, 1], rowvar=0)[0, 1]
print(cor1)

tree1 = createTree(trainMat, modelLeaf, modelErr, (1, 20))
yHat1 = createForeCast(tree1, testMat[:, 0], modelTreeEval)
cor2 = np.corrcoef(yHat1, testMat[:, 1], rowvar = 0)[0, 1]
print(cor2)