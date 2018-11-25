package de.tello.application.control;

import de.tello.application.model.connection.TelloVideo;
import de.tello.application.view.TelloGraphicalUserInterface;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TelloGraphicalUserInterfaceController {

    private static final String SERVER_ADDRESS = "0.0.0.0";
    private static final int SERVER_PORT = 11111;

    private final TelloGraphicalUserInterface telloGUI;

    private ScheduledExecutorService timer;

    private VideoCapture cap;



    public TelloGraphicalUserInterfaceController(TelloGraphicalUserInterface gui){

        this.telloGUI = gui;

    }

    public void initConnection() throws IOException {

        //receiver = new DatagramSocket();

        String hostname = "192.168.10.1";
        int port = 8889;

        InetAddress address = InetAddress.getByName(hostname);
        DatagramSocket socket = new DatagramSocket();

        byte[] sendBuffer = new byte[512];
        String initCommand = "command";
        sendBuffer = initCommand.getBytes();

        DatagramPacket request = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
        socket.send(request);

        byte[] reiciveBuffer = new byte[512];

        DatagramPacket response = new DatagramPacket(reiciveBuffer, reiciveBuffer.length);
        socket.receive(response);

        String responseCommand = new String(reiciveBuffer, 0, response.getLength());

        System.out.println(responseCommand);

        String startVideoStream = "streamon";
        sendBuffer = startVideoStream.getBytes();

        request = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
        socket.send(request);

        reiciveBuffer = new byte[512];

        response = new DatagramPacket(reiciveBuffer, reiciveBuffer.length);
        socket.receive(response);

        responseCommand = new String(reiciveBuffer, 0, response.getLength());

        System.out.println(responseCommand);

        //new Thread(new TelloVideo(this.telloGUI)).start();

        String addr = "udp://" + SERVER_ADDRESS + ":" + String.valueOf(SERVER_PORT);
        this.cap = new VideoCapture(addr);

        handleVideoUpdate();
    }


    private void handleVideoUpdate(){



        if(cap.isOpened()){

            Runnable frameGrabber = new Runnable() {

                @Override
                public void run()
                {
                    // effectively grab and process a single frame
                    Mat frame = grabFrame();
                    // convert and show the frame
                    Image imageToShow = mat2Image(frame);
                    //updateImageView(currentFrame, imageToShow);

                    Platform.runLater(new Runnable() {
                        @Override public void run() {

                            telloGUI.getVideoFeed().setImage(imageToShow);
                        }
                    });
                }
            };

            this.timer = Executors.newSingleThreadScheduledExecutor();
            this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

        }





    }

    private Image mat2Image(Mat frame)
    {
        try
        {
            return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
        }
        catch (Exception e)
        {
            System.err.println("Cannot convert the Mat obejct: " + e);
            return null;
        }
    }

    private BufferedImage matToBufferedImage(Mat original)
    {
        // init
        BufferedImage image = null;
        int width = original.width(), height = original.height(), channels = original.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1)
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

    private Mat grabFrame()
    {
        // init everything
        Mat frame = new Mat();

        // check if the capture is open
        if (this.cap.isOpened())
        {
            try
            {
                // read the current frame
                this.cap.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty())
                {
                    //Imgproc.cvtColor(frame, frame, Imgproc.COLOR_YUV2RGB);
                }

            }
            catch (Exception e)
            {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

}
