from src.tree import *
from src.treePlotter import *
from src.config import *



# dataSet, labels = createDataSetDemo()
# print(createTree(dataSet, labels))
# createPlotDemo()
# tree = retrieveTreeDemo(0)
# storeTree(tree, classifierAddress)
# tree = loadTree(classifierAddress)
# createPlot(tree)

fr = open(lensesAddress)
lenses = [inst.split('\t') for inst in fr.readlines()]
lensesLabels = ['age', 'prescript', 'astigmatic', 'tearRate']
lensesTree = createTree(lenses, lensesLabels)
# createPlot(lensesTree)
# storeTree(lensesTree, "%s/%s" %(address,"lensesTree.txt"))
createPlot(loadTree("%s/%s" %(address,"lensesTree.txt")))