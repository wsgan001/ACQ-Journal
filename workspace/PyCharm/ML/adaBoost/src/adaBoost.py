import numpy as np


def loadSimpleData():
    dataMat = np.mat([[1.0, 2.1],
                     [2.0, 1.1],
                     [1.3, 1.0],
                     [2.0, 1.0]])
    classLabels = [1.0, 1.0, -1.0, -1.0, 1.0]
    return dataMat, classLabels


def stumpClassify(dataMatrix, dimen, threshVal, threshIneq):
    m = np.shape(dataMatrix)[0]
    retArray = np.ones((m, 1))
    if threshIneq == 'lt':
        retArray[dataMatrix[:, dimen] <= threshVal] = -1.0
    else:
        retArray[dataMatrix[:, dimen] > threshVal] = -1.0
    return retArray


def buildStump(dataArr, classLabels, D):
    dataMat = np.mat(dataArr); labelMat = np.mat(classLabels).T
    m, n = np.shape(dataMat)
    numSteps = 10.0; bestStump = {}; bestClasEst = np.mat(np.zeros(m, 1))
    minError = np.inf
    for i in range(n):
        rangeMin = dataMat[:, i].min(); rangeMax = dataMat[:, i].max()
        stepSize = (rangeMax - rangeMin)/numSteps
        for j in range(-1, int(numSteps)+1):
            for inequal in ['lt', 'gt']:
                threshVal = (rangeMin + float(j) * stepSize)
                predictVals = stumpClassify(dataMat, i, threshVal, inequal)
                errArr = np.mat(np.zeros((m, 1)))
                errArr[predictVals == labelMat] = 0
                weightedError = D.T*errArr
                print("split: dim %d, thresh %.2f, thresh ineqal: %s, the weighted error is %.3f" % (i, threshVal, inequal, weightedError))
                if weightedError < minError:
                    minError = weightedError
                    bestClasEst = predictVals.copy()
                    bestStump['dim'] = i
                    bestStump['thresh'] = threshVal
                    bestStump['ineq'] = inequal
    return bestStump, minError, bestClasEst

