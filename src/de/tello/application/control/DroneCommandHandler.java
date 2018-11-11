package de.tello.application.control;

public class DroneCommandHandler {















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
