from src.regression import *
from src.config import *


xArr, yArr = loadData(ex0)
ans = lwlr(xArr[0], xArr, yArr, 1.0)
print(ans)
# ws = standRegres(xArr, yArr)
# yHat = np.mat(xArr)*ws
# cor = np.corrcoef(yHat.T, np.mat(yArr))
# print(yArr)
# print(cor)