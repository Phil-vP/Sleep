#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QPushButton>
#include <QFile>
#include <QLabel>
#include <QLayout>
#include <QGroupBox>
#include <QSlider>

#include <QDebug>
#include <iostream>

#include <opencv2/imgcodecs.hpp>
#include <opencv2/highgui.hpp>



QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    void loadFile();
    void doCanny();
    void setThresh();
    void calcLines();

private:
    Ui::MainWindow *ui;
    QPushButton *loadButton;

    QLabel *originalLabel;
    QPixmap *originalPicture;
    QLabel *originalPictureLabel;

    QLabel *lineLabel;
    QPixmap *linePicture;
    QLabel *linePictureLabel;


    QSlider *thresholdSlider;
    QLabel *sliderPosition;


    QGroupBox *originalBox;
    QGroupBox *linesBox;
    QGroupBox *sliderBox;
    QGroupBox *buttonBox;

    QWidget *centralWidget;
    QLayout *mainWindowLayout;

    int threshold = 0;
    int low_threshold = 0;
    int maxlow_threshold = 100;
    const int ratio = 3;
    const int kernel_size = 3;

    QString filePath = "";

    cv::Mat src, src_gray;
    cv::Mat *dst, *detected_edges;


};
#endif // MAINWINDOW_H
