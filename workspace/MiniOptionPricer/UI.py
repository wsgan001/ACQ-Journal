import sys
from PyQt5.QtWidgets import (QApplication, QWidget, QRadioButton,
    QPushButton, QButtonGroup, QHBoxLayout, QVBoxLayout, QGridLayout,
    QFileDialog)
from PyQt5.QtCore import Qt
from _signal import signal
from click._unicodefun import click
from PyQt5.Qt import QLabel, QTextEdit, QTextBrowser
from sympy.printing.pretty.pretty_symbology import vobj
from share import *

class MiniPricerInterface(QWidget):
    case = 0

    def __init__(self):
        super().__init__()
#         self.display()
        self.initUI()

    def initUI(self):

        hboxLyt = QHBoxLayout()
        vboxClickLyt = QVBoxLayout()
        vboxButtonLyt = QVBoxLayout()
        vbox=QVBoxLayout()
        self.result=QTextEdit()
        self.result.isReadOnly()

        funcBtnGroup = QButtonGroup(self) # Number group


        r0 = QRadioButton("EU call/put option")
        r1 = QRadioButton("Implied Volatility")
        r2 = QRadioButton("US call/put option")
        r3 = QRadioButton("Geometric Asian option")
        r4 = QRadioButton("Arithmetic Asian option")
        r5 = QRadioButton("Geometric basket option")
        r6 = QRadioButton("Arithmetic basket option")
        self.r7 = QRadioButton("Standard")
        self.r8 = QRadioButton("Control Variate")
        self.r7.setVisible(False)
        self.r8.setVisible(False)

        funcBtnGroup.addButton(r0,1)
        funcBtnGroup.addButton(r1,2)
        funcBtnGroup.addButton(r2,3)
        funcBtnGroup.addButton(r3,4)
        funcBtnGroup.addButton(r4,5)
        funcBtnGroup.addButton(r5,6)
        funcBtnGroup.addButton(r6,7)
        funcBtnGroup.addButton(self.r7,8)
        funcBtnGroup.addButton(self.r8,9)
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
        btn.clicked.connect(self.showDialog)
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
        self.case = buttonid
        print(self.case)
        if self.case==5 or self.case==7 or self.case==8 or self.case==9:
            self.r7.setVisible(True)
            self.r8.setVisible(True)
        else:
            self.r7.setVisible(False)
            self.r8.setVisible(False)


    def display(self):
        self.initUI()

#

    def showDialog(self):
        print(self.case)
        fname = QFileDialog.getOpenFileName(self, 'Open file', sys.path[0])

        if fname[0]:
            print(fname[0])
#             r = arithAsianOption(100,0.3,0.05,3,100,50,100000,conVar='NULL')
            self.result.setText(repr(r))



if __name__ == '__main__':

    app = QApplication(sys.argv)
    ex = MiniPricerInterface()
    sys.exit(app.exec_())
