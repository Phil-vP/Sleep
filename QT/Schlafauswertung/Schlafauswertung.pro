QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++11

# You can make your code fail to compile if it uses deprecated APIs.
# In order to do so, uncomment the following line.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += \
    main.cpp \
    mainwindow.cpp

HEADERS += \
    mainwindow.h \

FORMS += \
    mainwindow.ui

TRANSLATIONS += \
    Schlafauswertung_de_DE.ts

INCLUDEPATH += D:\\Anderes\\Libraries\\opencv\\build\\include

LIBS += D:/Anderes/Libraries/opencv-build/bin
LIBS += D:\Anderes\Libraries\opencv-build\bin\libopencv_core451.dll
LIBS += D:\Anderes\Libraries\opencv-build\bin\libopencv_highgui451.dll
LIBS += D:\Anderes\Libraries\opencv-build\bin\libopencv_imgcodecs451.dll
LIBS += D:\Anderes\Libraries\opencv-build\bin\libopencv_imgproc451.dll
LIBS += D:\Anderes\Libraries\opencv-build\bin\libopencv_features2d451.dll
LIBS += D:\Anderes\Libraries\opencv-build\bin\libopencv_calib3d451.dll



# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target
