import sys
from PyQt5.QtWidgets import QApplication, QLabel, QMainWindow, QGroupBox, QHBoxLayout, QSlider, QPushButton, QVBoxLayout, QWidget
from PyQt5.QtGui import QPixmap
from PyQt5.QtCore import Qt
import cv2 as cv

# Subclass QMainWindow to customise your application's main window
class MainWindow(QMainWindow):


    # thresholdSlider = QSlider
    # originalPictureLabel = QLabel
    # linePictureLabel = QLabel
    # sliderPosition = 0
    # loadButton = QPushButton

    def __init__(self, *args, **kwargs):
        super(MainWindow, self).__init__(*args, **kwargs)

        self.setWindowTitle("Schlafbilder")

        # originalLabel = QLabel("Original Picture:")
        self.originalPictureLabel = QLabel()
        self.originalPictureFilename = ""
        originalBox = QGroupBox()
        originalBox.setLayout(QHBoxLayout())

        originalBox.layout().addWidget(QLabel("Original Picture:"))
        originalBox.layout().addWidget(self.originalPictureLabel)


        self.linePictureLabel = QLabel()
        linesBox = QGroupBox()
        linesBox.setLayout(QHBoxLayout())

        linesBox.layout().addWidget(QLabel("Line Picture:"))
        linesBox.layout().addWidget(self.linePictureLabel)

        self.thresholdSlider = QSlider()
        self.thresholdSlider.setTickPosition(QSlider.TicksBelow)
        self.thresholdSlider.setRange(0, 100)
        self.thresholdSlider.setOrientation(Qt.Horizontal)
        self.sliderPosition = QLabel("0")
        self.sliderPosition.setMinimumWidth(20)


        sliderBox = QGroupBox()
        sliderBox.setLayout(QHBoxLayout())
        sliderBox.layout().addWidget(self.thresholdSlider)
        sliderBox.layout().addWidget(self.sliderPosition)


        self.loadButton = QPushButton("Load File")

        buttonBox = QGroupBox()
        buttonBox.setLayout(QHBoxLayout())
        buttonBox.layout().addWidget(self.loadButton)

        self.setMinimumSize(850, 400)

        mainWindowLayout = QVBoxLayout()
        mainWindowLayout.addWidget(originalBox)
        mainWindowLayout.addWidget(linesBox)
        mainWindowLayout.addWidget(sliderBox)
        mainWindowLayout.addWidget(buttonBox)

        centralWidget = QWidget()
        centralWidget.setLayout(mainWindowLayout)

        self.setCentralWidget(centralWidget)



    def getThresholdSlider(self):
        return self.thresholdSlider

    def getOriginalPictureLabel(self):
        return self.originalPictureLabel

    def getLinePictureLabel(self):
        return self.linePictureLabel

    def getSliderPosition(self):
        return self.sliderPosition

    def getLoadButton(self):
        return self.loadButton

    def updateSliderValue(self):
        self.sliderPosition.setText(str(self.thresholdSlider.value()))

    def setOriginalPicture(self, filename):
        self.originalPictureFilename = filename
        self.originalPictureLabel.setPixmap(QPixmap(filename))

    def doCanny(self):
        print("Canny")

    def asdf(self):

        ratio = 3
        kernel_size = 3

        src = cv.cvtColor(cv.imread(self.originalPictureFilename))
        if src is None:
            print("Error when loading File")
            return False

        src_gray = cv.cvtColor(src, cv.COLOR_BGR2GRAY)
        low_threshold = self.thresholdSlider.value()
        img_blur = cv.blur(src_gray, (3, 3))

        detected_edges = cv.Canny(img_blur, low_threshold, low_threshold*ratio, kernel_size)
        mask = detected_edges != 0
        dst = src * (mask[:, :, None].astype(src.dtype))
        cv.imshow(self.originalPictureLabel, dst)
