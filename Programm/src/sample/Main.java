package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("title");
        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane);

        ImageView myImageView = new ImageView();

        Button button_load = new Button("Load");
        Button button_calc = new Button("Calc");

        button_load.setOnAction((event) -> {
            System.out.println("Load clicked");
            selectImage(myImageView);
        });

        button_calc.setOnAction((event) -> {
            System.out.println("Calc Clicked");
            farbenBerechnen(myImageView);
        });

        HBox rootBox = new HBox();
        rootBox.getChildren().addAll(button_load, button_calc, myImageView);
        rootBox.setAlignment(Pos.CENTER);

        // presumably you intended this somewhere?
        pane.setCenter(rootBox);


        //Toolbar
        HBox toolbarArea = new HBox( 10 );
        toolbarArea.setPadding( new Insets( 10 ) );
        toolbarArea.setAlignment(Pos.CENTER);

        primaryStage.setMinHeight(200);
        primaryStage.setMinWidth(800);
        primaryStage.setScene(scene);
        primaryStage.show();

        //Puts buttons on bottom bar
        toolbarArea.getChildren().addAll( button_load, button_calc );
        pane.setBottom( toolbarArea );
    }

    private void selectImage(ImageView imageView){
        /**
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Image image = new Image(file.toURI().toString()); **/
            Image image = new Image("file:/B:/Projekte/Schlaf/Bilder/1_full.jpg");
            ColorAdjust monochrome = new ColorAdjust();
            monochrome.setSaturation(-1);
            imageView.setEffect(monochrome);
            imageView.setImage(image);
        //}
    }

    private void farbenBerechnen(ImageView imageView){
        Image img = imageView.getImage();

        double height = img.getHeight();
        double width = img.getWidth();

        // int y = (int)(height / 2);
        int y = 5;
        int maxX = (int) width;

        String[] colorArray = new String[500];

        PixelReader pr = img.getPixelReader();

        int counter = 0;
        int colorCounter = 0;
        Color prevColor = Color.BLACK;

        for(int x = 0; x < maxX; x++){
            Color c = pr.getColor(x, y);
            c = c.grayscale();
            System.out.println(x + ": " + c.toString());

            if(!c.toString().equals(prevColor.toString())){
                String str = c + " for " + counter + " Turns";
                colorArray[colorCounter] = str;
                colorCounter++;
                prevColor = c;
                counter = 0;
            }
            counter++;
        }

        System.out.println("Image height: " + height + ", width: " + width);
        for(String s : colorArray){
            System.out.println(s);
        }

    }




    public static void main(String[] args) {
        launch(args);
    }
}
