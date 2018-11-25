package de.tello.application.model.connection;

import java.io.IOException;
import java.net.*;

public class UDPDroneClient {

     private String hostname;
     private int port;
     private DatagramSocket connectionSocket;
     private InetAddress serverAddress;
     private byte[] sendBuffer;
     private byte[] receiveBuffer;

     private boolean isConnected;


     public UDPDroneClient(String pHostname, int pPort){

          this.hostname = pHostname;
          this.port = pPort;
          this.sendBuffer = new byte[1024];
          this.receiveBuffer = new byte[1024];
          this.isConnected = false;

     }

     public boolean establishConnection(){

         boolean isEstablished = false;

         try {
             serverAddress = InetAddress.getByName(hostname);
             connectionSocket = new DatagramSocket();
             isEstablished = true;
             isConnected = true;
         } catch (UnknownHostException e) {
             System.out.println("Establishing Connection with " + hostname + " failed!");
             System.out.println(e.getMessage());
             //e.printStackTrace();
         } catch (SocketException e){
             System.out.println("Unable to open connection socket!");
             System.out.println(e.getMessage());

         }

         return isEstablished;

     }

    /** Function to send a command to the dji tello drone. The response from the drone (e.g. "OK" or "Error")
     *  will be returned. In case of no drone connection the command will not be executed and an error message
     *  will be returned.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 25.11.2018
     *
     * @param pCommand
     * @return
     * @throws IOException
     */
     public String executeDroneCommand(String pCommand) throws IOException {

         if(isConnected == true){

             sendBuffer = pCommand.getBytes();

             DatagramPacket request = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, port);
             connectionSocket.send(request);

             DatagramPacket response = new DatagramPacket(receiveBuffer, receiveBuffer.length);
             connectionSocket.receive(response);

             String commandResponse = new String(receiveBuffer, 0, response.getLength());

             return commandResponse;

         } else {

             String failureResponse = "Command not executed! Not connected to drone!";

             return failureResponse;
         }

     }
}
