import sys
from PyQt5.QtWidgets import (QApplication, QWidget, QRadioButton,
    QPushButton, QButtonGroup, QHBoxLayout, QVBoxLayout, QGridLayout,
    QFileDialog)
from PyQt5.QtCore import Qt
from matplotlib.pyplot import connect
from _signal import signal
from click._unicodefun import click
from sqlalchemy.sql.expression import false
from tkinter.constants import BUTT
from PyQt5.Qt import QLabel, QTextEdit, QTextBrowser
from sympy.printing.pretty.pretty_symbology import vobj

class MiniPricerInterface(QWidget):
    id = 0

    def __init__(self):
        super().__init__()
#         self.display()
        self.initUI()

    def initUI(self):

        hboxLyt = QHBoxLayout()
        vboxLytClick = QVBoxLayout()
        vboxLytButton = QVBoxLayout()
        vbox=QVBoxLayout()
        self.result=QTextEdit()
        self.result.isReadOnly()
        
        funcBtnGroup = QButtonGroup(self) # Number group
    
       
        r0 = QRadioButton("Q1 EU call/put option")
        r1 = QRadioButton("Q2 Implied Volatility")
        r2 = QRadioButton("Q3 US call/put option")
        r3 = QRadioButton("Q4 Geometric Asian option")
        r4 = QRadioButton("Q5 Arithmetic Asian option")
        r5 = QRadioButton("Q6 Geometric basket option")
        r6 = QRadioButton("Q7 Arithmetic basket option")
        self.r7 = QRadioButton("Standard")
        self.r8 = QRadioButton("Control Variate")
        self.r7.setVisible(False)
        self.r8.setVisible(False)
        
        funcBtnGroup.addButton(r0,0)
        funcBtnGroup.addButton(r1,1)
        funcBtnGroup.addButton(r2,2)
        funcBtnGroup.addButton(r3,3)
        funcBtnGroup.addButton(r4,4)
        funcBtnGroup.addButton(r5,5)
        funcBtnGroup.addButton(r6,6)
        vboxLytClick.addWidget(r0)
        vboxLytClick.addWidget(r1)
        vboxLytClick.addWidget(r2)
        vboxLytClick.addWidget(r3)
        vboxLytClick.addWidget(r4)
        vboxLytClick.addWidget(r5)
        vboxLytClick.addWidget(r6)

        
        btn = QPushButton('Select File', self)
        calButton = QPushButton("Calculate")
        #btn.setToolTip('This is a <b>QPushButton</b> widget')
        btn.resize(btn.sizeHint())
        btn.move(50, 50)
    
        funcBtnGroup.buttonClicked[int].connect(self.funcBtnGroup_clicked)
        btn.clicked.connect(self.showDialog)
#-----------------------------------------------
        vboxLytClick.setAlignment(Qt.AlignTop)
        vboxLytButton.addWidget(self.r7)
        vboxLytButton.addWidget(self.r8)
        vboxLytButton.addWidget(btn)
        vboxLytButton.addWidget(calButton)
        hboxLyt.addLayout(vboxLytClick)
        hboxLyt.addLayout(vboxLytButton)
        vbox.addLayout(hboxLyt)
        vbox.addWidget(self.result)
        self.setLayout(vbox)

        self.resize(600,600)
        self.setGeometry(300, 300, 400, 320)
        self.setWindowTitle('MiniPricer')
        #self.setWindowIcon(QIcon('web.png'))
        self.show() 
    

    def funcBtnGroup_clicked(self,buttonid):
        case = buttonid
        if case==4 or case==6:
            self.r7.setVisible(True)
            self.r8.setVisible(True) 
        else:
            self.r7.setVisible(False)
            self.r8.setVisible(False)
            self.result.setText("aaaa")

    def display(self):
        
        self.initUI()
        
#         

    def showDialog(self):
        print(self.id)
        fname = QFileDialog.getOpenFileName(self, 'Open file', sys.path[0])

        if fname[0]:
            print(fname[0])

if __name__ == '__main__':

    app = QApplication(sys.argv)
    ex = MiniPricerInterface()
    sys.exit(app.exec_())