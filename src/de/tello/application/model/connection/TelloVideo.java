package de.tello.application.model.connection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import de.tello.application.view.TelloGraphicalUserInterface;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class TelloVideo implements Runnable{

    private static final String SERVER_ADDRESS = "0.0.0.0";
    private static final int SERVER_PORT = 11111;
    private TelloGraphicalUserInterface telloGui;

    public TelloVideo(TelloGraphicalUserInterface gui){

        this.telloGui = gui;

    }


    @Override
    public void run() {

        String addr = "udp://" + SERVER_ADDRESS + ":" + String.valueOf(SERVER_PORT);
        VideoCapture cap = new VideoCapture(addr);



        int packetNumber = 0;

        while(cap.isOpened()){

            Mat frame = new Mat();
            cap.read(frame);

            Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
            MatOfByte buffer = new MatOfByte();

            Imgcodecs.imencode(".png", frame, buffer);

            Image imageToShow = new Image(new ByteArrayInputStream(buffer.toArray()));

            Platform.runLater(new Runnable() {
                @Override public void run() {

                    telloGui.getVideoFeed().setImage(imageToShow);
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
