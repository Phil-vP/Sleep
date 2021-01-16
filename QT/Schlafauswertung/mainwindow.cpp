#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setMinimumWidth(800);

    // Original Picture
    originalLabel = new QLabel(tr("Original Picture:"));
    // originalPicture = new QPixmap();
    originalPictureLabel = new QLabel();
    // originalPictureLabel->setPixmap(*originalPicture);

    originalBox = new QGroupBox();
    originalBox->setLayout(new QHBoxLayout());

    originalBox->layout()->addWidget(originalLabel);
    originalBox->layout()->addWidget(originalPictureLabel);


    // Line Picture
    lineLabel = new QLabel(tr("Line Picture:"));
    // linePicture = new QPixmap();
    linePictureLabel = new QLabel();
    //linePictureLabel->setPixmap(*linePicture);

    linesBox = new QGroupBox();
    linesBox->setLayout(new QHBoxLayout());

    linesBox->layout()->addWidget(lineLabel);
    linesBox->layout()->addWidget(linePictureLabel);


    // Threshold Slider
    thresholdSlider = new QSlider();
    thresholdSlider->setTickPosition(QSlider::TicksBelow);
    thresholdSlider->setRange(low_threshold, maxlow_threshold);
    thresholdSlider->setOrientation(Qt::Orientation::Horizontal);

    // connect(thresholdSlider, &QSlider::sliderMoved, this, &MainWindow::setThresh);
    // connect(thresholdSlider, &QSlider::sliderReleased, this, &MainWindow::setThresh);
    connect(thresholdSlider, &QSlider::sliderReleased, this, &MainWindow::doCanny);

    sliderPosition = new QLabel("0");
    sliderPosition->setMinimumWidth(20);


    sliderBox = new QGroupBox();
    sliderBox->setLayout(new QHBoxLayout());
    sliderBox->layout()->addWidget(thresholdSlider);
    sliderBox->layout()->addWidget(sliderPosition);




    // Buttons
    loadButton = new QPushButton(tr("Load File"), this);
    connect(loadButton, &QPushButton::released, this, &MainWindow::loadFile);

    buttonBox = new QGroupBox();
    buttonBox->setLayout(new QHBoxLayout());

    buttonBox->layout()->addWidget(loadButton);



    // Main Window

    mainWindowLayout = new QVBoxLayout();

    mainWindowLayout->addWidget(originalBox);
    mainWindowLayout->addWidget(linesBox);
    mainWindowLayout->addWidget(sliderBox);
    mainWindowLayout->addWidget(buttonBox);


    centralWidget = new QWidget();
    centralWidget->setLayout(mainWindowLayout);

    setCentralWidget(centralWidget);
    setWindowTitle("Ja moin");

}

void MainWindow::loadFile()
{
    qDebug() << "LoadFile clicked";
    filePath = "B:\\Projekte\\Schlaf\\Bilder\\1_full.jpg";
    originalPicture = new QPixmap(filePath);
    originalPictureLabel->setPixmap(*originalPicture);
}

void MainWindow::setThresh(){
    threshold = thresholdSlider->value();
    sliderPosition->setText(QString::number(threshold));

    qDebug() << "Moved to " << thresholdSlider->value();
}

void MainWindow::calcLines(){
    qDebug() << "Calculating Lines...";
    src = *new cv::Mat();
    // src = new cv::Mat(cv::imread(cv::String(filePath.toStdString())));
    /*if(src->empty()){
        qDebug() << "Doing nothing as no picture is selected";
        qDebug() << "image Path: " << filePath;
        return;
    }*/
}

void MainWindow::doCanny(){
    setThresh();
    calcLines();
}


MainWindow::~MainWindow()
{
    delete ui;
}

