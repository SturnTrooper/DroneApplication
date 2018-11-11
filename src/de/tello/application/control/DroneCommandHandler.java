package de.tello.application.control;

import de.tello.application.model.connection.UDPDroneClient;

public class DroneCommandHandler {

    private UDPDroneClient droneClient;
    private boolean isConnectionAlive;

    public DroneCommandHandler(String pHostname, int pPort){

        this.droneClient =  new UDPDroneClient(pHostname,pPort);
        this.isConnectionAlive = false;

    }


    public void establishConnectionWithDrone(){

        isConnectionAlive = droneClient.establishConnection();
    }

















    private void validateControlResponseMessage(String pCommand, String pMessage){

        switch(pMessage){
            case "ok":
                System.out.println("Successfully executed command " + "[" + pCommand + "]");
            case "error":
                System.out.println("Execution of command " + "[" + pCommand + "]" + " was not successful!");
            default:
                System.out.println("Shit happens");
        }
    }
}
