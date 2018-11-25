package de.tello.application.de.tello.application.model.video;

import de.tello.application.view.TelloGraphicalUserInterface;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;

public class TelloVideoStreamListener implements Runnable {

    private static final String SERVER_ADDRESS = "0.0.0.0";
    private static final int SERVER_PORT = 11111;

    private TelloGraphicalUserInterface graphicalUserInterface;

    private VideoCapture videoStream;

    public TelloVideoStreamListener(TelloGraphicalUserInterface pGraphicalUserInferface){

        this.graphicalUserInterface = pGraphicalUserInferface;

        initializeVideoListener();
    }

    private void initializeVideoListener(){

        String videoAddress = "udp://" + SERVER_ADDRESS + ":" + String.valueOf(SERVER_PORT);
        videoStream = new VideoCapture(videoAddress);

    }

    @Override
    public void run() {

        if(videoStream.isOpened()){

            Mat videoFrame = obtainVideoFrame();
            Image frameAsImage = convertFrameToImage(videoFrame);










        }

    }

    /** Function to grab a single frame of the drone's video feed manipulate it and provide it
     *  for further processing.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 24.11.2018
     *
     * @return
     */
    private Mat obtainVideoFrame(){

        Mat currentFrame = new Mat();

        if(videoStream.isOpened()){

            videoStream.read(currentFrame);

        }

        //Manipulation like chainging color of frame can be done here ...

        return currentFrame;
    }

    /** This function converts the frame to an image, that in turn will be
     *  shown on the GUI screen. Differennt image types can be choosen (e.g.
     *  .bmp, .png, .jpeg ...).
     *
     *  @author Florian Sturn
     *  @version 1.0
     *  @date 25.11.2018
     *
     * @param pFrame
     * @return
     */
    private Image convertFrameToImage(Mat pFrame){

        MatOfByte frameBuffer = new MatOfByte();

        Imgcodecs.imencode(".png", pFrame, frameBuffer);
        Image imageToShow = new Image(new ByteArrayInputStream(frameBuffer.toArray()));

        return imageToShow;
    }
}
