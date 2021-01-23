package sleep;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;


public class CannyDetectorApp {
    private static final int MAX_LOW_THRESHOLD = 100;
    private static final int RATIO = 3;
    private static final int KERNEL_SIZE = 3;
    private static final Size BLUR_SIZE = new Size(3,3);
    private int lowThresh = 0;
    private Mat src;
    private Mat srcBlur = new Mat();
    private Mat detectedEdges = new Mat();
    private Mat dst = new Mat();
    private JFrame frame;
    private JLabel imgLabel;

    private JButton calcButton;
    private JPanel centralPanel;

    private JTextArea resultField;

    private JTextField time_start;
    private JTextField time_end;

    private Checkbox dayWrap;

    public CannyDetectorApp(String[] args) {
        // Create and set up the window.
        frame = new JFrame("Edge Map (Canny detector demo)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set up the content pane.
        addComponentsToPane(frame.getContentPane());
        // Use the content pane's default BorderLayout. No need for
        // setLayout(new BorderLayout());
        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private void loadNewPicture(){

        FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        fd.setDirectory("B:\\");
        fd.setFile("*.jpg");
        fd.setVisible(true);
        String imagePath = fd.getDirectory() + fd.getFile();
        //String imagePath = "B:/Projekte/Schlaf/Bilder/1_full.jpg";
        Mat src_new = Imgcodecs.imread(imagePath);

        if (src_new.empty()) {
            System.out.println("Empty image: " + imagePath);
            return;
        }

        src = src_new;
        System.out.println("src: " + src.toString());

        Container pane = frame.getContentPane();

        Image img = HighGui.toBufferedImage(src);

        JPanel picturePanel = new JPanel();
        picturePanel.setLayout(new BoxLayout(picturePanel, BoxLayout.Y_AXIS));
        picturePanel.add(new JLabel(new ImageIcon(img)));
        picturePanel.add(new JLabel(" "));

        imgLabel = new JLabel(new ImageIcon(img));
        picturePanel.add(imgLabel);

        resultField = new JTextArea();
        resultField.setMinimumSize(new Dimension(500, 500));

        picturePanel.add(resultField);

        pane.add(picturePanel, BorderLayout.CENTER);


        frame.pack();


        calcButton.setEnabled(true);
    }
    private void addComponentsToPane(Container pane) {
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
        sliderPanel.add(new JLabel("Min Threshold:"));
        JSlider slider = new JSlider(0, MAX_LOW_THRESHOLD, 0);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                lowThresh = source.getValue();
                update();
            }
        });
        sliderPanel.add(slider);

        pane.add(sliderPanel, BorderLayout.PAGE_START);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        calcButton = new JButton("Berechnen");

        calcButton.addActionListener(e -> calculate());
        calcButton.setEnabled(false);

        JButton loadButton = new JButton("Bild Laden");
        loadButton.addActionListener(e -> loadNewPicture());

        Dimension d = new Dimension(250,80);
        time_start = new JTextField("   00:00");
        time_start.setMinimumSize(d);
        time_end = new JTextField("   00:00");
        time_end.setMinimumSize(d);
        dayWrap = new Checkbox();

        buttonPanel.add(loadButton);
        buttonPanel.add(new JLabel("Anfangszeit:"));
        buttonPanel.add(time_start);
        buttonPanel.add(new JLabel("Endzeit:"));
        buttonPanel.add(time_end);

        buttonPanel.add(calcButton);

        pane.add(buttonPanel, BorderLayout.SOUTH);

    }

    private void calculate(){

        System.out.println("CalcButton");
        System.out.println(dst.toString());
        System.out.println("Cols: " + dst.cols());
        System.out.println("Rows: " + dst.rows());

        int counter = 0;

        resultField.setText("");;

        List<Integer> indexList = new ArrayList<Integer>();

        for(int col = 0; col < dst.cols(); col++){
            double[] dA = dst.get((int)(dst.rows() / 2), col);

            if(DoubleStream.of(dA).sum() == 0.0){
                continue;
            }

            counter++;

            indexList.add(col);

            // System.out.println("Something at column " + col + ":");
            String st = "";
            for(double d : dA){
                st += " " + Double.toString(d);
            }
            // System.out.println(st);
            // System.out.println();
        }

        indexList.add(dst.cols());

        System.out.println("Total number of lines: " + counter);
        System.out.println(indexList);

        int index_1 = 0;

        String[] colors = {"Türkis", "Blau  ", "Pink  ", "Orange"};

        LocalTime startTime = LocalTime.parse(time_start.getText().strip(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(time_end.getText().strip(), DateTimeFormatter.ofPattern("HH:mm"));

        Duration duration;
        long start_seconds = startTime.toSecondOfDay();
        long seconds;

        if (startTime.isBefore(endTime)){
            duration = Duration.between(startTime, endTime);
            seconds = duration.getSeconds();
        }
        else{
            duration = Duration.between(endTime, startTime);
            long tfh = 24*60*60;
            seconds = tfh - duration.getSeconds();
        }

        int seconds_pP = (int) seconds / dst.cols();
        System.out.println("Duration: " + seconds);
        System.out.println("seconds per Pixel: " + seconds_pP);
        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime + "\n");


        String resultText = "";

        for(int index_2 : indexList){
            int closest = closestColor(src.get((int)src.rows()/2, (int)(index_1 + index_2)/2));
            // System.out.println("Area going from " + index_1 + " to " + (index_2 - 1) + "; closest color: " + colors[closest]);
            // LocalTime start = startTime.plusSeconds(seconds_pP * index_1);
            // LocalTime end = startTime.plusSeconds(seconds_pP * index_2);

            long duration_sec = (index_2 - index_1) * seconds_pP;

            long start_sec = (start_seconds + index_1 * seconds_pP) % 86400;
            long end_sec = (start_seconds + index_2 * seconds_pP) % 86400;
            LocalTime start = LocalTime.ofSecondOfDay(start_sec);
            LocalTime end = LocalTime.ofSecondOfDay(end_sec);
            System.out.println("Area going from " + start + " to " + end + "; closest color: " + colors[closest] + ", Dauer: " + duration_sec + " Sekunden");
            resultText += String.format("Zeitraum: %s - %s; Schlafphase: %s, Dauer: %d Sekunden", start, end, colors[closest], duration_sec);
            resultText += "\n";
            index_1 = index_2;
        }

        resultField.setText(resultText);


    }

    private void update() {
        Imgproc.blur(src, srcBlur, BLUR_SIZE);
        Imgproc.Canny(srcBlur, detectedEdges, lowThresh, lowThresh * RATIO, KERNEL_SIZE, false);
        dst = new Mat(src.size(), CvType.CV_8UC3, Scalar.all(0));
        src.copyTo(dst, detectedEdges);
        // System.out.println(dst.toString());
        Image img = HighGui.toBufferedImage(dst);
        imgLabel.setIcon(new ImageIcon(img));
        frame.repaint();
    }

    private int closestColor(double[] inputColor){
        // Color tuerkis = new Color(31, 193, 136);
        // double[] tuerkis = {31, 193, 136};
        double[] tuerkis = {136, 193, 31};

        // Color blue = new Color(2, 154, 255);
        // double[] blue = {2, 154, 255};
        double[] blue = {255, 154, 2};

        // Color pink = new Color(255, 1, 152);
        // double[] pink = {255, 1, 152};
        double[] pink = {152, 1, 255};

        // Color orange = new Color(250, 97, 4);
        // double[] orange = {250, 97, 4};
        double[] orange = {4, 97, 250};

        double diff_t = 0;
        double diff_b = 0;
        double diff_p = 0;
        double diff_o = 0;


        for(int i = 0; i < 3; i++){
            diff_t += Math.abs(inputColor[i] - tuerkis[i]);
            diff_b += Math.abs(inputColor[i] - blue[i]);
            diff_p += Math.abs(inputColor[i] - pink[i]);
            diff_o += Math.abs(inputColor[i] - orange[i]);
        }

        double[] colorDiff = {diff_t, diff_b, diff_p, diff_o};


        /*
        System.out.println();
        System.out.println("########");
        System.out.println("inputColor: " + Arrays.toString(inputColor));
        System.out.println("Difference Türkis: " + diff_t);
        System.out.println("Difference Blau: " + diff_b);
        System.out.println("Difference Pink: " + diff_p);
        System.out.println("Difference Orange: " + diff_o);
        */

        double min = 1000;
        int min_index = 0;

        for(int i = 0; i < 4; i++){
            if(colorDiff[i] < min){
                // System.out.println("Updating index to " + i);
                min = colorDiff[i];
                min_index = i;
            }
            else{
                // System.out.println("Anscheinend ist " + colorDiff[i] + " > " + min);
            }
        }

        /*
        System.out.println("########");
        System.out.println();
        */

        return min_index;

    }

    public static void main(String[] args) {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CannyDetectorApp(args);
            }
        });
    }
}
