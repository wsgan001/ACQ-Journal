import numpy as np
import operator


def createDataSet():
    # a demo to create a data set
    group = np.array([[1.0, 1.1],
                     [1.0, 1.0],
                     [0, 0],
                     [0, 0.1]])
    label = ['A', 'A', 'B', 'B']
    return group, label


def autoNorm(dataSet):
    minVal = dataSet.min(0)
    maxVal = dataSet.max(0)
    normalDataSet = np.zeros(np.shape(dataSet))
    m = dataSet.shape[0]
    range = maxVal - minVal
    normalDataSet = dataSet -np.tile(minVal, (m, 1))
    normalDataSet = normalDataSet/np.tile(range, (m, 1))
    return normalDataSet, range, minVal


def classify0(inX, dataSet, label, k):
    # KNN algorithm
    # step 1: compute the Euclidean distance in form of the matrix
    dataSetSize = dataSet.shape[0]
    diffMat = np.tile(inX, (dataSetSize, 1))-dataSet
    sqDiffMat = diffMat**2
    sqDistance = (sqDiffMat.sum(axis=1))**0.5
    sortedDistanceIndices = sqDistance.argsort()

    # step 2: counting the frequency of k vertices with the smallest distance
    classCount = {}
    for i in range(k):
        voteLabel = label[sortedDistanceIndices[i]]
        classCount[voteLabel] = classCount.get(voteLabel, 0) + 1

    # step 3: sort in decreasing order and output the label of the closest vertex
    sortedClassCount = sorted(classCount.items(), key=operator.itemgetter(1), reverse=True)
    return sortedClassCount[0][0]
