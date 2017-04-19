import sys
from PyQt5.QtWidgets import (QApplication, QWidget, QRadioButton,
    QPushButton, QButtonGroup, QHBoxLayout, QVBoxLayout, QGridLayout,
    QFileDialog)
from PyQt5.QtCore import Qt
import numpy as np
import pandas as pd
from timeit import default_timer as timer
from datetime import datetime
from PyQt5.Qt import QLabel, QTextEdit, QTextBrowser
from sympy.printing.pretty.pretty_symbology import vobj
import share as cal

class MiniPricerInterface(QWidget):
    case = 0
    CVType = 0
    calculateR = []
    def __init__(self):
        super().__init__()
        self.initUI()

    def initUI(self):

        hboxLyt = QHBoxLayout()
        vboxClickLyt = QVBoxLayout()
        vboxButtonLyt = QVBoxLayout()
        vbox=QVBoxLayout()
        self.result=QTextEdit()
        self.result.isReadOnly()

        funcBtnGroup = QButtonGroup(self) # Number group
        funcBtnGroup1 = QButtonGroup(self)

        r0 = QRadioButton("EU call/put option")
        r1 = QRadioButton("Implied Volatility")
        r2 = QRadioButton("US call/put option")
        r3 = QRadioButton("Geometric Asian option")
        r4 = QRadioButton("Arithmetic Asian option")
        r5 = QRadioButton("Geometric basket option")
        r6 = QRadioButton("Arithmetic basket option")
        self.r7 = QRadioButton("Standard")
        self.r8 = QRadioButton("Control Variate")
        self.r9 = QRadioButton()
        self.r7.setVisible(False)
        self.r8.setVisible(False)
        self.r9.setVisible(False)


        funcBtnGroup.addButton(r0,1)
        funcBtnGroup.addButton(r1,2)
        funcBtnGroup.addButton(r2,3)
        funcBtnGroup.addButton(r3,4)
        funcBtnGroup.addButton(r4,5)
        funcBtnGroup.addButton(r5,6)
        funcBtnGroup.addButton(r6,7)
        funcBtnGroup1.addButton(self.r7,8)
        funcBtnGroup1.addButton(self.r8,9)
        funcBtnGroup1.addButton(self.r9)
        vboxClickLyt.addWidget(r0)
        vboxClickLyt.addWidget(r1)
        vboxClickLyt.addWidget(r2)
        vboxClickLyt.addWidget(r3)
        vboxClickLyt.addWidget(r4)
        vboxClickLyt.addWidget(r5)
        vboxClickLyt.addWidget(r6)


        btn = QPushButton('Select File', self)
        calButton = QPushButton("Calculate")
        #btn.setToolTip('This is a <b>QPushButton</b> widget')
        btn.resize(btn.sizeHint())
        btn.move(50, 50)

        funcBtnGroup.buttonClicked[int].connect(self.funcBtnGroup_clicked)
        funcBtnGroup1.buttonClicked[int].connect(self.funcBtnGroup1_clicked)
        btn.clicked.connect(self.showDialog)
        calButton.clicked.connect(self.showResult)
#-----------------------------------------------
        vboxClickLyt.setAlignment(Qt.AlignTop)
        vboxButtonLyt.addWidget(self.r7)
        vboxButtonLyt.addWidget(self.r8)
        vboxButtonLyt.addWidget(btn)
        vboxButtonLyt.addWidget(calButton)
        hboxLyt.addLayout(vboxClickLyt)
        hboxLyt.addLayout(vboxButtonLyt)
        vbox.addLayout(hboxLyt)
        vbox.addWidget(self.result)
        self.setLayout(vbox)

        self.resize(600,600)
        self.setGeometry(300, 300, 400, 320)
        self.setWindowTitle('MiniPricer')
        #self.setWindowIcon(QIcon('web.png'))
        self.show()



    def funcBtnGroup_clicked(self,buttonid):
        self.result.setText("Please select input file and Calculate")
        self.calculateR.clear()
        self.case = buttonid
        self.r9.setChecked(True)
        print(self.case)
        if self.case==5 or self.case==7:
            self.r7.setVisible(True)
            self.r8.setVisible(True)

        else:
            self.r7.setVisible(False)
            self.r8.setVisible(False)

    def funcBtnGroup1_clicked(self,buttonid):
        self.CVType=buttonid
        self.calculateR.clear()


    def parseOptCsv(self,fname):
        oringaldata = pd.read_csv(fname)
        data = oringaldata.dropna()
        data['cp'] = data['Type'].map({'put': -1, 'put': -1, 'call': 1, 'Call': 1})
        # data.drop('Type',axis=1, inplace=True)
        dataArray = np.asarray(data.drop('Type', 1).values)

        s=100
        r=0.05
        T=3
        m=100000
        cVar = 'NULL'
        #baskVol=[]
        nOfAsset = 2
        if self.CVType == 9:
            cVar = 'y'
        if self.case == 1:pass
        if self.case == 2:pass
        if self.case == 3:pass
        if self.case == 4:
            for item in dataArray:
                Rtmp = cal.geoAsianOption(StockPrice=s, vol=item[0], K=item[1], N=item[2], cp=item[3],
                                            rate=r, T=T)#,  path=m, conVar=cVar)
                self.calculateR.append(Rtmp)
        if self.case == 5:
            for item in dataArray:
                ts = timer()
                Rtmp = cal.arithAsianOption(StockPrice=s, vol=item[0], K=item[1], step=item[2], cp=item[3],
                                            rate=r, T=T,  path=m, conVar=cVar)
                print('arithAsian time: ',timer()-ts)
                self.calculateR.append(Rtmp)
        if self.case == 6:
            for item in dataArray:
                Rtmp = cal.geoBasketOption(StockPrice=item[:nOfAsset], vol=item[nOfAsset+1:nOfAsset+nOfAsset+1], K=item[nOfAsset], corr=item[nOfAsset+nOfAsset+1], cp=item[nOfAsset+nOfAsset+2],
                                            rate=r, T=T, n=nOfAsset)#,  path=m, conVar=cVar)
                self.calculateR.append(Rtmp)
        if self.case == 7 :
            for item in dataArray:
                Rtmp = cal.arithBasketOption(StockPrice=item[:nOfAsset], vol=item[nOfAsset+1:nOfAsset+nOfAsset+1], K=item[nOfAsset], corr=item[nOfAsset+nOfAsset+1], cp=item[nOfAsset+nOfAsset+2],
                                            rate=r, T=T, n=nOfAsset, path=m, conVar=cVar)
                self.calculateR.append(Rtmp)


    def showDialog(self):
        print(self.case)
        fname = QFileDialog.getOpenFileName(self, 'Open file', sys.path[0])

        if fname[0]:
            self.parseOptCsv(fname[0])
            self.showResult()

    def showResult(self):
        re = list(map(repr,self.calculateR))
        re.insert(0,str(len(self.calculateR))+' option price calculation completed:')
        re.insert(0,str(datetime.now().replace(microsecond=0)))
        output="\n".join(re)
        self.result.setText(output)


if __name__ == '__main__':

    app = QApplication(sys.argv)
    ex = MiniPricerInterface()
    sys.exit(app.exec_())
