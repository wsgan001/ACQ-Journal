import matplotlib.pyplot as plt

# define the format of nodes and arrow
decisionTreeNode = dict(boxstyle="sawtooth", fc="0.8")
leafNode = dict(boxstyle="round4", fc="0.8")
arrow_args = dict(arrowstyle="<-")


# the core function to draw the decision tree node (including the middle and leaf nodes)
def plotNode(nodeTxt, centerPt, parentPt, nodeType):
    createPlot.ax1.annotate(nodeTxt, xy=parentPt, xycoords='axes fraction',
                            xytext=centerPt, textcoords='axes fraction',
                            va="center", ha="center", bbox=nodeType, arrowprops=arrow_args)


# a demo to create a middle node and a leaf node
# def createPlotDemo():
#     fig = plt.figure(1, facecolor="white")
#     fig.clf()
#     createPlotDemo.ax1 = plt.subplot(111, frameon=False)
#     plotNode('decisionTree node', (0.5, 0.1), (0.1, 0.5), decisionTreeNode)
#     plotNode('leaf node', (0.8, 0.1), (0.3, 0.8), leafNode)
#     plt.show()


def getNumleafs(decisionTree):
    numLeafs = 0
    firstStr = list(decisionTree.keys())[0]
    secondDict = decisionTree[firstStr]
    for key in secondDict.keys():
        if type(secondDict[key]).__name__ == 'dict':
            numLeafs += getNumleafs(secondDict[key])
        else:   numLeafs += 1
    return numLeafs


def getTreeDepth(decisionTree):
    maxDepth = 0
    firstStr = list(decisionTree.keys())[0]
    secondDict = decisionTree[firstStr]
    for key in secondDict.keys():
        if type(secondDict[key]).__name__ == 'dict':
            thisDepth = 1 + getTreeDepth(secondDict[key])
        else: thisDepth = 1
        if thisDepth > maxDepth: maxDepth = thisDepth
    return maxDepth


# test
def retrieveTreeDemo(i):
    listOfTrees = [{'no surfacing': {0: 'no', 1: {'flippers':
                    {0: 'no', 1: 'yes'}}}},
                   {'no surfacing': {0: 'no', 1: {'flippers':
                    {0: {'head': {0: 'no', 1: 'yes'}}, 1: 'no'}}}}
                   ]
    return listOfTrees[i]


# fill in the information between parent and current nodes
def plotMidText(cntrPt, parentPt, txtString):
    xMid = (parentPt[0] - cntrPt[0])/2.0 +cntrPt[0]
    yMid = (parentPt[1] - cntrPt[1])/2.0 +cntrPt[1]
    createPlot.ax1.text(xMid, yMid, txtString)


# function to draw the tree
def plotTree(decisionTree, parentPt, nodeTxt):
    numLeafs = getNumleafs(decisionTree)
    depth = getTreeDepth(decisionTree)
    firstStr = list(decisionTree.keys())[0]
    cntrPt = (plotTree.xOff + (1.0 + float(numLeafs))/ 2.0 /plotTree.totalW,
              plotTree.yOff)
    plotMidText(cntrPt, parentPt, nodeTxt)
    plotNode(firstStr, cntrPt, parentPt, decisionTreeNode)
    secondDict = decisionTree[firstStr]
    plotTree.yOff = plotTree.yOff - 1.0/plotTree.totalD
    for key in secondDict.keys():
        if type(secondDict[key]).__name__ == "dict":
            plotTree(secondDict[key], cntrPt, str(key))
        else:
            plotTree.xOff = plotTree.xOff + 1.0/plotTree.totalW
            plotNode(secondDict[key], (plotTree.xOff, plotTree.yOff),
                     cntrPt, leafNode)
            plotMidText((plotTree.xOff, plotTree.yOff), cntrPt, str(key))
    plotTree.yOff = plotTree.yOff + 1.0/plotTree.totalD


# function to draw the decision tree
def createPlot(inTree):
    fig = plt.figure(1, facecolor="white")
    fig.clf()
    axprops = dict(xticks=[], yticks=[])
    createPlot.ax1 = plt.subplot(111, frameon=False, **axprops)
    plotTree.totalW = float(getNumleafs(inTree))
    plotTree.totalD = float(getTreeDepth(inTree))
    plotTree.xOff = -0.5/ plotTree.totalW
    plotTree.yOff = 1.0
    plotTree(inTree, (0.5, 1.0), '')
    plt.show()







