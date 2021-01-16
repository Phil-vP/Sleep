from PyQt5.QtWidgets import QApplication, QFileDialog
from MainWindow import MainWindow
import cv2

mainWindow = None
allWidgets = {}


def createWindow():
    global allWidgets
    global mainWindow
    print("Created a new window")
    window = MainWindow()
    window.show()
    mainWindow = window
    allWidgets["mainWindow"] = window

def connect():
    mainWindow.getLoadButton().clicked.connect(loadButtonClicked)
    mainWindow.getThresholdSlider().sliderReleased.connect(sliderMoved)
    # print(mainWindow.getLoadButton())

def doSomething():
    app = QApplication([])
    print("Doing something...")
    createWindow()
    connect()
    app.exec()

def loadButtonClicked():
    print("loadButton was clicked")
    if loadPicture():
        calculateCanny()


def sliderMoved():
    mainWindow.updateSliderValue()
    print("Slider was moved to " + str(mainWindow.getThresholdSlider().value()))

def loadPicture():
    fd = QFileDialog()
    fd.setAcceptMode(QFileDialog.AcceptOpen)
    fd.setFileMode(QFileDialog.ExistingFile)
    # fd.setFilter("Image files (*.png *.jpg *.jpeg)")
    print("All options set")
    # filetupel = fd.getOpenFileName()
    images = ["png", "jpg", "jpeg"]

    # filename = filetupel[0]
    filename = "B:/Projekte/Schlaf/Bilder/1_full.jpg"
    print(filename)

    if filename.split(".")[-1] in images:
        print("Image selected")
    else:
        print("No image selected")
        return False

    mainWindow.setOriginalPicture(filename)
    return True

def calculateCanny():
    print("Canny")
    mainWindow.doCanny()


if __name__== "__main__":
    print("GeeksForGeeks")
    print("Your OpenCV version is: " + cv2.__version__)
    # doSomething()