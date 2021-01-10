package sample;

import javafx.scene.image.ImageView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class SleepWindow extends Frame {

    private String imagePath;

    public SleepWindow(){
        Button loadButton = new Button("Load");
        Button linesButton = new Button("Make Lines");


        loadButton.addActionListener(e -> loadImage());
        linesButton.addActionListener(e -> calcLines());

        Panel bottomPanel = new Panel(new FlowLayout());

        bottomPanel.add(loadButton);
        bottomPanel.add(linesButton);



        this.setMinimumSize(new Dimension(500, 500));
        // this.setLayout(new GridLayout(3, 1));
        this.setLayout(new BorderLayout());
        // this.add(new Label("Moin"));
        this.add(bottomPanel, BorderLayout.SOUTH);

       this.addWindowListener(new WindowAdapter() {
                               public void windowClosing(WindowEvent we) {
                                   dispose();
                               }
                           }
       );

        this.setTitle("Schlaf Gedöns");

        this.setVisible(true);
    }




    private void loadImage(){
        System.out.println("Loading Image");

       FileDialog fd = new FileDialog(this, "Bild auswählen", FileDialog.LOAD);
       fd.setFile("*.jpg");
       fd.setVisible(true);

       String filename = new File(fd.getFile()).getAbsolutePath();

       if(filename == null){
           System.out.println("Cancelled");
           return;
       }
       else{
           System.out.println("Filename: " + filename);
       }

       imagePath = filename;

       ImageIcon imagePanelIcon = new ImageIcon(imagePath, "Schlafbild");

       // JLabel imageLabel = new JLabel("Moinsen");
       JLabel imageLabel = new JLabel(imagePanelIcon);
       this.add(imageLabel, BorderLayout.CENTER);

       this.pack();

       System.out.println("Feddig");


    }


    private void calcLines(){
       System.out.println("Calculating Lines");
    }


}
