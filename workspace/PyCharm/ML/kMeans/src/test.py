from src.kMeans import *
from src.config import *


# dataMat = np.mat(loadDataSet(testSet))
# centroids, clustAssing = kMeans(dataMat, 4)
# print(centroids, clustAssing)
# print(distEclud(dataMat[0], dataMat[1]))

dataMat2 = np.mat(loadDataSet(testSet2))
centroids, clustAssing = biKmeans(dataMat2, 3)
print(centroids)