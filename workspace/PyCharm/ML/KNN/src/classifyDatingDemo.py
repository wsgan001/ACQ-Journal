from src.kNN import *
import matplotlib
import matplotlib.pyplot as plt


def file2Matrix(fileName):
    fr = open(fileName)
    arrayLine = fr.readlines() # notice that it should be readlines() not readline()
    tmpLine = arrayLine[0]
    columnLen = len((tmpLine.strip().split('\t')))
    numberOfLine = len(arrayLine)
    mat = np.zeros((numberOfLine, columnLen-1))
    classLabel = []
    index = 0
    for line in arrayLine:
        line = line.strip()
        listFromLine = line.split('\t')
        mat[index, :] = listFromLine[0:3]
        classLabel.append(int(listFromLine[-1]))
        index += 1
    return mat, classLabel


def datingClassTest(fileName, typesCount):
    hoRatio = 0.10
    datingDataMat, datingLabels = file2Matrix(fileName)
    normalMat, ranges, minVal = autoNorm(datingDataMat)
    m = normalMat.shape[0]
    numberTest = int(m*hoRatio)
    errorCount = 0.0
    for i in range(numberTest):
        classifyResult = classify0(normalMat[i, :], normalMat[numberTest:m, :],
                                   datingLabels[numberTest:m], typesCount)
        print(" the classifier came back with: %d, the real answer is : %d" % (classifyResult, datingLabels[i]))
        if (classifyResult != datingLabels[i]):
            errorCount += 1.0
            print(errorCount)
    print("the total error rate is: %f " % (errorCount / float(numberTest)))


def showPlot(data, label, axisNum1, axisNum2):
    fig = plt.figure()
    ax = fig.add_subplot(111)
    ax.scatter(data[:, axisNum1], data[:, axisNum2], 15.0*np.array(label), 15*np.array(label))
    plt.show()