package de.tello.application.model.connection;

import java.io.IOException;
import java.net.*;

public interface UDPDroneClient {


     boolean establishConnection();

     String executeCommand(String pCommand) throws IOException;

}
