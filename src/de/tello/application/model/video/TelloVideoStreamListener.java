package de.tello.application.model.video;

import de.tello.application.view.TelloGraphicalUserInterface;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

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

            //TelloUtils.onFXThread(graphicalUserInterface.getVideoFeed(),frameAsImage);

            Platform.runLater(new Runnable() {
                @Override public void run() {

                    graphicalUserInterface.getVideoFeed().setImage(frameAsImage);
                }
            });
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

        return currentFrame;
    }

    /** This function converts the frame to an image, that in turn will be
     *  shown on the GUI screen.
     *
     *  @author Florian Sturn
     *  @version 1.0
     *  @date 25.11.2018
     *
     * @param pFrame
     * @return
     */
    private Image convertFrameToImage(Mat pFrame){

        try
        {
            return SwingFXUtils.toFXImage(createBufferedImage(pFrame), null);
        }
        catch (Exception e)
        {
            System.err.println("Cannot convert the Mat obejct: " + e);
            return null;
        }
    }

    /** Function to create a buffered image out of the H264 frame. The created image
     *  will be returned and shown in the graphical user interface.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 25.11.2018
     *
     * @param pOriginal
     * @return
     */
    private static BufferedImage createBufferedImage(Mat pOriginal) {

        // init
        BufferedImage image = null;

        int width = pOriginal.width();
        int height = pOriginal.height();
        int channels = pOriginal.channels();

        byte[] sourcePixels = new byte[width * height * channels];
        pOriginal.get(0, 0, sourcePixels);

        if (pOriginal.channels() > 1)
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        }
        else
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }

}
