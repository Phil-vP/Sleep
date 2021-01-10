#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    // Original Picture
    originalLabel = new QLabel(tr("Original Picture:"));
    originalPicture = new QPixmap();
    originalPictureLabel = new QLabel();
    originalPictureLabel->setPixmap(*originalPicture);

    originalBox = new QGroupBox();
    originalBox->setLayout(new QHBoxLayout());

    originalBox->layout()->addWidget(originalLabel);
    originalBox->layout()->addWidget(originalPictureLabel);


    // Line Picture
    lineLabel = new QLabel(tr("Line Picture:"));
    linePicture = new QPixmap();
    linePictureLabel = new QLabel();
    linePictureLabel->setPixmap(*linePicture);

    linesBox = new QGroupBox();
    linesBox->setLayout(new QHBoxLayout());

    linesBox->layout()->addWidget(lineLabel);
    linesBox->layout()->addWidget(linePictureLabel);


    // Threshold Slider
    thresholdSlider = new QSlider();
    thresholdSlider->setTickPosition(QSlider::TicksBelow);
    thresholdSlider->setRange(0, 100);
    thresholdSlider->setOrientation(Qt::Orientation::Horizontal);

    connect(thresholdSlider, &QSlider::sliderReleased, this, &MainWindow::setThresh);

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
  // resize button
    qDebug() << "LoadFile clicked";
}

void MainWindow::setThresh(){
    threshold = thresholdSlider->value();
    sliderPosition->setText(QString::number(threshold));

    qDebug() << "Moved to " << thresholdSlider->value();
}


MainWindow::~MainWindow()
{
    delete ui;
}

