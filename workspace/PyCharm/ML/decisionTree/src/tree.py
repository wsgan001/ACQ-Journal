from math import log
import operator


def createDataSetDemo():
    dataSet = [[1, 1, 'yes'],
               [1, 1, 'yes'],
               [1, 0, 'no'],
               [0, 1, 'no'],
               [0, 1, 'no']]
    labels = ['no surfacing', 'flippers']
    return dataSet, labels


# compute the entropy (information gain)
def calculateEntropy(dataSet):
    numEntropies = len(dataSet)
    labelCounts = {} # create a dictionary (key: class, value: count)
    for featVect in dataSet:
        currentLabel = featVect[-1]
        if currentLabel not in labelCounts.keys():
            labelCounts[currentLabel] = 0
        labelCounts[currentLabel] += 1
    entropy = 0.0
    for key in labelCounts:
        prob = float(labelCounts[key])/numEntropies
        entropy -= prob* log(prob, 2)
    return entropy

# split the dataset
def splitDataSet(dataSet, axis, value):
    retDataSet = []
    for featVec in dataSet:
        if featVec[axis] == value:
            reducedFeatVec = featVec[:axis]
            reducedFeatVec.extend(featVec[axis+1:])
            retDataSet.append(reducedFeatVec)
    return retDataSet

# select the best attribute to split the dataset
def selectAttributeToSplict(dataSet):
    numAttributes = len(dataSet[0])-1
    baseEntropy = calculateEntropy(dataSet)
    bestInfoGain = 0.0
    bestAttributeIndex = -1

    for i in range(numAttributes):
        attributeList = [example[i] for example in dataSet]
        uniqueVals = set(attributeList)
        newEntropy = 0.0
        for value in uniqueVals:
            subDataSet = splitDataSet(dataSet, i , value)
            prob = len(subDataSet)/ float(len(dataSet))
            newEntropy += prob*calculateEntropy(subDataSet)
        infoGain = baseEntropy - newEntropy
        if (infoGain > bestInfoGain):
            bestInfoGain = infoGain
            bestAttributeIndex = i
    return bestAttributeIndex


# vote the class with the most frequency
def majorityCnt(classList):
    classCount = {}
    for vote in classList:
        if vote not in classCount.keys(): classCount[vote] = 0
        classCount += 1
    sortedClassCount = sorted(classCount.items(), key=operator.itemgetter(1), reverse=True)
    return sortedClassCount[0][0]


def createTree(dataSet, labels):
    classList = [example[-1] for example in dataSet]
    if classList.count(classList[0]) == len(classList): return classList[0]

    if len(dataSet[0]) == 1:
        return majorityCnt(classList)
    bestAttr = selectAttributeToSplict(dataSet)
    bestLabel = labels[bestAttr]
    tree = {bestLabel: {} }
    del(labels[bestAttr])
    attrValues = [example[bestAttr] for example in dataSet]
    uniqueVals = set(attrValues)
    for value in uniqueVals:
        subLabels = labels[:]
        tree[bestLabel][value] = createTree(splitDataSet(dataSet, bestAttr, value), subLabels)
    return tree


# classify function
def classify(inTree, labels, testVec):
    firstStr = list(inTree.keys())[0]
    secondDict = inTree[firstStr]
    # .index() helps to find the first attribute which matches firstStr
    attrIndex = labels.index(firstStr)
    for key in secondDict.keys():
        if testVec[attrIndex] == key:
            if type(secondDict[key]).__name__ == 'dict':
                classLabel = classify(secondDict[key], labels, testVec)
            else:
                classLabel = secondDict[key]
    return classLabel


# function to store the decision tree
# using the serialization of pickle package
def storeTree(inTree, fileName):
    import pickle as pk # pickle: serialization module which helps to store the objects
    fw = open(fileName, 'wb')
    pk.dump(inTree, fw)
    fw.close()


def loadTree(fileName):
    import pickle
    fr = open(fileName, 'rb')
    return pickle.load(fr)
