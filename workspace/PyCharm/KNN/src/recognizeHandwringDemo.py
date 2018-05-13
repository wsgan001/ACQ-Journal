from src.kNN import *
import numpy as np
from os import listdir


def img2Vector(fileName):
    returnVec = np.zeros((1, 1024))
    fr = open(fileName)
    for i in range(32):
        lineStr = fr.readline()
        for j in range(32):
            returnVec[0, 32*i+j] = int(lineStr[j])
    return returnVec


def handwritingClassTest(trainList, testList):
    hwLabels = []
    trainingFileList = listdir(trainList)
    m = len(trainingFileList)
    trainMat = np.zeros((m, 1024))
    for i in range(m):
        fileNameStr = trainingFileList[i]
        fileStr = fileNameStr.split(".")[0]
        classNumStr = int(fileStr.split('_')[0])
        hwLabels.append(classNumStr)
        trainMat[i, :] = img2Vector('%s/%s' % (trainList, fileNameStr))

    testFileList = listdir(testList)
    errorCount = 0.0
    mTest = len(testFileList)
    print(mTest)
    for i in range(mTest):
        fileNameStr = testFileList[i]
        fileStr = fileNameStr.split('.')[0]
        classNumStr = int(fileStr.split('_')[0])
        vectorUnderTest = img2Vector('%s/%s' %(testList, fileNameStr))
        classifierResult = classify0(vectorUnderTest, trainMat, hwLabels, 3)
        print("the classifier came back with: %d. the real answer is: %d" % (classifierResult, classNumStr))
        if (classifierResult != classNumStr): errorCount += 1.0
    print("\n the total number of error is: %d" % errorCount)
    print("\n the total error rate is : %f" % (errorCount/float(mTest)))

