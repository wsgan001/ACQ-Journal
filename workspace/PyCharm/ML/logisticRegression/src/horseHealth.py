from src.logRegres import *
from src.config import horseTest, horseTrain


def classifyVector(inX, weights):
    prob = sigmoid(sum(inX * weights))
    if prob > 0.5:
        return 1.0
    else:
        return 0.0


def colicTest():
    frTrain = open(horseTrain)
    frTest = open(horseTest)
    trainSet = []
    trainLabels = []
    for line in frTrain.readlines():
        currentLine = line.strip().split('\t')
        lineArr = []
        num = len(currentLine)
        for i in range(21):
            lineArr.append(float(currentLine[i]))
        trainSet.append(lineArr)
        trainLabels.append(float(currentLine[21]))
    trainWeights = stocGradAscent1(np.array(trainSet), trainLabels, 500)
    errorCount = 0
    numTestVec = 0.0

    for line in frTest.readlines():
        numTestVec += 1.0
        currentLine = line.strip().split('\t')
        lineArr = []
        num = len(currentLine)
        for i in range(21):
            lineArr.append(float(currentLine[i]))
        if int(classifyVector(np.array(lineArr), trainWeights)) != int(currentLine[21]):
            errorCount += 1
    errorRate = (float(errorCount)/numTestVec)
    print("the error rate of this test is : %f" % errorRate)
    return errorRate


# def colicTest():
#     frTrain = open(horseTrain); frTest = open(horseTest)
#     trainingSet = []; trainingLabels = []
#     for line in frTrain.readlines():
#         currLine = line.strip().split('\t')
#         lineArr =[]
#         for i in range(21):
#             lineArr.append(float(currLine[i]))
#         trainingSet.append(lineArr)
#         trainingLabels.append(float(currLine[21]))
#     trainWeights = stocGradAscent1(np.array(trainingSet), trainingLabels, 1000)
#     errorCount = 0; numTestVec = 0.0
#     for line in frTest.readlines():
#         numTestVec += 1.0
#         currLine = line.strip().split('\t')
#         lineArr =[]
#         for i in range(21):
#             lineArr.append(float(currLine[i]))
#         if int(classifyVector(np.array(lineArr), trainWeights))!= int(currLine[21]):
#             errorCount += 1
#     errorRate = (float(errorCount)/numTestVec)
#     print("the error rate of this test is: %f" % errorRate)
#     return errorRate



def multiTest():
    numTests = 10
    errorSum = 0.0
    for k in range(numTests):
        errorSum += colicTest()
    print("after %d iterations the average error rate is: %f" % (numTests, errorSum/float(numTests)))

