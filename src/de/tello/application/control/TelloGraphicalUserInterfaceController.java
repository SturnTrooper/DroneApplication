package de.tello.application.control;

import de.tello.application.model.connection.UDPDroneClient;
import de.tello.application.model.video.TelloVideoStreamListener;
import de.tello.application.view.TelloGraphicalUserInterface;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TelloGraphicalUserInterfaceController {

    private final TelloGraphicalUserInterface telloGUI;

    private UDPDroneClient droneClient;
    private TelloVideoStreamListener videoStreamListener;
    private ScheduledExecutorService videoFrameExecuter;

    private boolean isDroneConnected;



    public TelloGraphicalUserInterfaceController(TelloGraphicalUserInterface gui, String pHostname, int pPort){

        this.telloGUI = gui;
        this.droneClient = new UDPDroneClient(pHostname,pPort);

    }

    /** Function which sets up the drone's sdk mode and opens the video feed, so it can be displayed
     *  on the graphical user interface.
     *
     * @throws IOException
     */
    public void initConnection() throws IOException {

        isDroneConnected = droneClient.establishConnection();
        String response;
        boolean isVideoFeedOpen = false;

        if(isDroneConnected == true){
            System.out.println("Initialize SDK mode");
            response = droneClient.executeDroneCommand("command");
            System.out.println("Response from Drone is: " + response);

            System.out.println("Open video stream");
            response = droneClient.executeDroneCommand("streamon");

            if(response.equals("ok")){
                isVideoFeedOpen = true;
            }

            if(isVideoFeedOpen == true){
                startVideoTransmission();
            } else {
                System.out.println("Transmission not possible. No open video feed available!");
            }
        } else {
            System.out.println("No connection to Drone established!");
        }
    }

    /** Function to start the video stream transmission. The processing of the drone's video feed runs in
     *  a background thread. A new frame will be grabbed every 33ms so that we archive a frame rate of
     *  30 frames per second (FPS).
     *
     *  @author Florian Sturn
     *  @version 1.0
     *  @date 25.11.2018
     *
     */
    private void startVideoTransmission(){

        this.videoStreamListener = new TelloVideoStreamListener(this.telloGUI);

        this.videoFrameExecuter = Executors.newSingleThreadScheduledExecutor();
        this.videoFrameExecuter.scheduleAtFixedRate(this.videoStreamListener, 0, 33, TimeUnit.MILLISECONDS);

    }
}
