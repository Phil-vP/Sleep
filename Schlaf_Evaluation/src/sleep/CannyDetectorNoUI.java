package sleep;

import javafx.util.Pair;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;


public class CannyDetectorNoUI {
    private static final int MAX_LOW_THRESHOLD = 100;
    private static final int RATIO = 3;
    private static final int KERNEL_SIZE = 3;
    private static final Size BLUR_SIZE = new Size(3,3);
    private static final int lowThresh = 50;
    private String[] colors = {"Leicht", "Tief", "REM", "wach"};
    // private String[] colors = {"Türkis", "Blau  ", "Pink  ", "Orange"};

    Map<String, Pair<String, String>> timeMap;

    public CannyDetectorNoUI() {
        timeMap = new HashMap<>(){{
            put("crop_VP01_N1.JPG", new Pair<>("22:20", "05:12"));
            put("crop_VP01_N2.JPG", new Pair<>("22:03", "06:09"));
            put("crop_VP01_N3.JPG", new Pair<>("23:05", "05:41"));
            put("crop_VP02_N1.JPG", new Pair<>("01:52", "06:06"));
            put("crop_VP02_N2.JPG", new Pair<>("00:37", "04:16"));
            put("crop_VP02_N3.JPG", new Pair<>("00:30", "06:35"));
            put("crop_VP03_N1.JPG", new Pair<>("00:10", "05:17"));
            put("crop_VP03_N2.JPG", new Pair<>("00:25", "06:28"));
            put("crop_VP04_N1.JPG", new Pair<>("22:24", "05:58"));
            put("crop_VP04_N2.JPG", new Pair<>("23:16", "06:09"));
            put("crop_VP05_N1.JPG", new Pair<>("22:33", "05:35"));
            put("crop_VP05_N2.JPG", new Pair<>("00:04", "05:47"));
            put("crop_VP05_N3.JPG", new Pair<>("22:45", "05:55"));
            put("crop_VP06_N1.JPG", new Pair<>("23:30", "06:16"));
            put("crop_VP06_N2.JPG", new Pair<>("23:34", "06:15"));
            put("crop_VP06_N3.JPG", new Pair<>("22:08", "05:41"));
            put("crop_VP07_N1.JPG", new Pair<>("22:33", "06:02"));
            put("crop_VP07_N2.JPG", new Pair<>("01:02", "05:53"));
            put("crop_VP08_N1.JPG", new Pair<>("23:50", "05:58"));
            put("crop_VP08_N2.JPG", new Pair<>("22:52", "06:10"));
            put("crop_VP09_N1.JPG", new Pair<>("00:28", "07:09"));
            put("crop_VP09_N2.JPG", new Pair<>("23:20", "06:08"));
            put("crop_VP10_N1.JPG", new Pair<>("23:54", "06:50"));
            put("crop_VP10_N2.JPG", new Pair<>("23:55", "06:37"));
            put("crop_VP11_N1.JPG", new Pair<>("23:17", "06:00"));
            put("crop_VP11_N2.JPG", new Pair<>("22:41", "05:46"));
            put("crop_VP11_N3.JPG", new Pair<>("22:35", "05:50"));
            put("crop_VP12_N2.JPG", new Pair<>("22:47", "06:49"));
            put("crop_VP12_N3.JPG", new Pair<>("23:22", "06:05"));
            put("crop_VP13_N1.JPG", new Pair<>("00:41", "06:22"));
            put("crop_VP13_N2.JPG", new Pair<>("23:12", "03:05"));
            put("crop_VP13_N3.JPG", new Pair<>("22:38", "05:23"));
            put("crop_VP15_N1.JPG", new Pair<>("23:30", "06:20"));
            put("crop_VP15_N2.JPG", new Pair<>("00:19", "06:14"));
            put("crop_VP16_N1.JPG", new Pair<>("23:13", "02:34"));
            put("crop_VP16_N2.JPG", new Pair<>("23:08", "04:16"));
            put("crop_VP17_N1.JPG", new Pair<>("22:06", "05:41"));
            put("crop_VP17_N2.JPG", new Pair<>("22:04", "05:28"));
            put("crop_VP18_N1.JPG", new Pair<>("22:19", "06:05"));
            put("crop_VP18_N2.JPG", new Pair<>("23:03", "06:40"));
            put("crop_VP18_N3.JPG", new Pair<>("22:21", "05:45"));
            put("crop_VP19_N1.JPG", new Pair<>("22:45", "01:01"));
            put("crop_VP19_N2.JPG", new Pair<>("23:18", "05:14"));
            put("crop_VP19_N3.JPG", new Pair<>("23:27", "05:41"));
            put("crop_VP20_N1.JPG", new Pair<>("23:20", "06:57"));
            put("crop_VP20_N2.JPG", new Pair<>("22:38", "05:48"));
            put("crop_VP20_N3.JPG", new Pair<>("22:40", "06:04"));
            put("crop_VP21_N1.JPG", new Pair<>("23:22", "06:30"));
            put("crop_VP21_N2.JPG", new Pair<>("23:44", "08:56"));
            put("crop_VP21_N3.JPG", new Pair<>("23:51", "05:09"));
            put("crop_VP22_N1.JPG", new Pair<>("23:30", "06:58"));
            put("crop_VP22_N2.JPG", new Pair<>("22:43", "10:36"));
            put("crop_VP22_N3.JPG", new Pair<>("23:31", "01:49"));
            put("crop_VP23_N1.JPG", new Pair<>("01:25", "06:30"));
            put("crop_VP23_N2.JPG", new Pair<>("01:51", "06:27"));
            put("crop_VP23_N3.JPG", new Pair<>("23:36", "06:32"));
            put("crop_VP24_N1.JPG", new Pair<>("22:41", "06:31"));
            put("crop_VP24_N2.JPG", new Pair<>("23:04", "06:01"));
            put("crop_VP25_N1.JPG", new Pair<>("21:49", "06:12"));
            put("crop_VP25_N2.JPG", new Pair<>("23:28", "05:59"));
            put("crop_VP26_N1.JPG", new Pair<>("23:03", "05:57"));
            put("crop_VP26_N2.JPG", new Pair<>("22:46", "06:17"));
            put("crop_VP27_N2.JPG", new Pair<>("21:55", "05:42"));
            put("crop_VP27_N3.JPG", new Pair<>("23:40", "05:17"));
            put("crop_VP28_N1.JPG", new Pair<>("23:56", "06:16"));
            put("crop_VP28_N2.JPG", new Pair<>("23:02", "06:00"));
            put("crop_VP29_N1.JPG", new Pair<>("22:05", "05:56"));
            put("crop_VP29_N2.JPG", new Pair<>("22:10", "06:06"));
            put("crop_VP29_N3.JPG", new Pair<>("23:40", "06:24"));
            put("crop_VP30_N1.JPG", new Pair<>("22:55", "11:01"));
            put("crop_VP30_N2.JPG", new Pair<>("22:47", "05:33"));
            put("crop_VP30_N3.JPG", new Pair<>("22:03", "05:49"));
            put("crop_VP31_N1.JPG", new Pair<>("22:13", "05:45"));
            put("crop_VP31_N2.JPG", new Pair<>("21:59", "05:20"));
            put("crop_VP32_N1.JPG", new Pair<>("22:30", "06:43"));
            put("crop_VP32_N2.JPG", new Pair<>("22:24", "05:58"));
            put("crop_VP33_N1.JPG", new Pair<>("21:52", "05:36"));
            put("crop_VP33_N2.JPG", new Pair<>("22:44", "05:54"));
            put("crop_VP34_N1.JPG", new Pair<>("22:39", "06:06"));
            put("crop_VP34_N2.JPG", new Pair<>("21:55", "02:52"));
            put("crop_VP34_N3.JPG", new Pair<>("21:42", "02:27"));
            put("crop_VP35_N1.JPG", new Pair<>("23:22", "06:10"));
        }};
    }

    public void calculateFolder(String path){
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(f -> processImage(f.toString()));
        }
        catch(Exception e){
            System.out.println("Error when getting files");
        }
    }

    private void processImage(String fullPath){

        System.out.println("fullPath: " + fullPath);

        if(!fullPath.contains("crop")){
            System.out.println("No crop picture\n");
            return;
        }


        int lastSlash = fullPath.lastIndexOf("\\");
        String dirPath = fullPath.substring(0, lastSlash+1);
        String filename = fullPath.substring(lastSlash+1);

        System.out.println("Picture: " + filename);
        //
        Mat src = Imgcodecs.imread(fullPath);

        if (src.empty()) {
            System.out.println("Empty image: " + fullPath + "\n");
            return;
        }

        Mat srcBlur = new Mat();
        Mat detectedEdges = new Mat();
        Mat dst;

        System.out.println("src: " + src.toString());

        // CANNY
        Imgproc.blur(src, srcBlur, BLUR_SIZE);
        Imgproc.Canny(srcBlur, detectedEdges, lowThresh, lowThresh * RATIO, KERNEL_SIZE, false);
        dst = new Mat(src.size(), CvType.CV_8UC3, Scalar.all(0));
        src.copyTo(dst, detectedEdges);

        //

        int counter = 0;
        List<Integer> indexList = new ArrayList<>();


        for(int col = 0; col < dst.cols(); col++){
            double[] dA = dst.get((int)(dst.rows() / 2), col);

            if(DoubleStream.of(dA).sum() == 0.0){
                continue;
            }

            counter++;

            indexList.add(col);
        }

        indexList.add(dst.cols());

        System.out.println("Total number of lines: " + counter);
        System.out.println(indexList);

        Pair timeTupel;
        try {
            System.out.println("Looking for key with name " + filename);
            System.out.println();
            timeTupel = timeMap.get(filename);
        }
        catch (Exception e){
            System.out.println("ERROR, no Time Tupel found for " + filename + "\n");
            return;
        }


        System.out.println("Time Tupel: " + timeTupel);

        LocalTime startTime = LocalTime.parse(timeTupel.getKey().toString(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(timeTupel.getValue().toString(), DateTimeFormatter.ofPattern("HH:mm"));

        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime);


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

        // int seconds_pP = (int) seconds / dst.cols();
        double seconds_pP = ((double)seconds ) / dst.cols();
        System.out.println("Duration: " + seconds);
        System.out.println("cols: " + dst.cols());
        System.out.println("seconds per Pixel: " + seconds_pP);


        String resultText = "";

        int index_1 = 0;

        LocalTime start = startTime;

        for(int index_2 : indexList){

            int closest = closestColor(src.get((int)src.rows()/2, (int)(index_1 + index_2)/2));

            // LocalTime start = startTime.plusSeconds((long)Math.floor(seconds_pP * index_1));
            LocalTime end = startTime.plusSeconds((long)Math.floor(seconds_pP * index_2));

            System.out.println("index_2: " + index_2 + ", value: " + seconds_pP * index_2);

            // long duration_sec = (long) Math.floor((index_2 - index_1) * seconds_pP);

            long duration_sec = start.until(end, ChronoUnit.SECONDS);
            if(duration_sec < 0) duration_sec += 86400;

            // long end_sec = Math.round(index_2 * seconds_pP) % 86400;
            System.out.println("Area going from " + start.toString() + " to " + end.toString() + "; closest color: " + colors[closest] + ", Dauer: " + (int) duration_sec/60 + ":" + duration_sec % 60 + "min");
            resultText += String.format("Zeitraum: %8s - %8s; Schlafphase: %6s, Dauer: %02d:%02d min\n", start, end, colors[closest], (int) duration_sec/60, duration_sec % 60);

            index_1 = index_2;
            start = end;
        }

        try {
            String new_name = filename.substring(5, filename.length() - 4);
            new_name += ".txt";
            fullPath = dirPath + new_name;
            System.out.println("Writing to Path " + fullPath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));
            writer.write(resultText);
            writer.close();
        }
        catch (Exception e){
            System.out.println("Error when writing file");
            return;
        }

        System.out.println();
        System.out.println("----------------");
        System.out.println();
    }


    private int closestColor(double[] inputColor){
        double[] tuerkis = {136, 193, 31};
        double[] blue = {255, 154, 2};
        double[] pink = {152, 1, 255};
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

        double min = 1000;
        int min_index = 0;

        for(int i = 0; i < 4; i++){
            if(colorDiff[i] < min){
                // System.out.println("Updating index to " + i);
                min = colorDiff[i];
                min_index = i;
            }
        }
        return min_index;

    }

    public static void main(String[] args) {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Create a new instance and run it
        CannyDetectorNoUI cdui = new CannyDetectorNoUI();
        cdui.calculateFolder("B:\\Projekte\\Schlaf\\Auswertung\\");
    }
}
