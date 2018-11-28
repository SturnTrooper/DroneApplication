package de.tello.application.control;

import de.tello.application.model.connection.UDPDroneClient;
import de.tello.application.model.video.TelloVideoStreamListener;
import de.tello.application.view.TelloGraphicalUserInterface;

import java.io.IOException;
import java.util.concurrent.*;

public class TelloGraphicalUserInterfaceController {

    private final TelloGraphicalUserInterface telloGUI;

    private UDPDroneClient droneClient;

    private TelloVideoStreamListener videoStreamListener;
    private ScheduledExecutorService videoFrameExecutor;

    private TelloStateListener stateListener;
    private ScheduledExecutorService stateListenerExecutor;

    private TelloStateHandler stateHandler;
    private ScheduledExecutorService stateHandlerExecutor;

    private boolean isDroneConnected;

    private BlockingQueue<String> messageQueue;



    public TelloGraphicalUserInterfaceController(TelloGraphicalUserInterface gui, String pHostname, int pPort){

        this.messageQueue = new ArrayBlockingQueue<String>(1000); //TODO: Fine Tuning of capacity
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

            startStateListener();
            startStateHandler();

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

        this.videoFrameExecutor = Executors.newSingleThreadScheduledExecutor();
        this.videoFrameExecutor.scheduleAtFixedRate(this.videoStreamListener, 0, 33, TimeUnit.MILLISECONDS);

    }

    private void startStateListener(){

        this.stateListener = new TelloStateListener("0.0.0.0",8890, this.messageQueue);

        this.stateListenerExecutor = Executors.newSingleThreadScheduledExecutor();
        this.stateListenerExecutor.scheduleAtFixedRate(this.stateListener, 0, 1000, TimeUnit.MILLISECONDS);

    }

    private void startStateHandler(){

        this.stateHandler = new TelloStateHandler(this.telloGUI, this.messageQueue);

        this.stateHandlerExecutor = Executors.newSingleThreadScheduledExecutor();
        this.stateHandlerExecutor.scheduleAtFixedRate(this.stateHandler,0,7000,TimeUnit.MILLISECONDS);


    }

    public void fireCommand(String pCommand){



        try {
            String battery = droneClient.executeDroneCommand("battery ?");
            System.out.println(battery);
            String response = droneClient.executeDroneCommand(pCommand);
            System.out.println("Response for command " + pCommand + " " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
