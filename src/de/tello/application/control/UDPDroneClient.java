package de.tello.application.control;

import java.io.IOException;
import java.net.*;

public class UDPDroneClient {

    private String hostname;
    private int port;
    private DatagramSocket connectionSocket;
    private InetAddress serverAddress;
    private byte[] sendBuffer;
    private byte[] receiveBuffer;

    public UDPDroneClient(String pHostname, int pPort){

        this.hostname = pHostname;
        this.port = pPort;
        this.sendBuffer = new byte[512];
        this.receiveBuffer = new byte[512];

    }

    public boolean establishConnection()  {

        System.out.println("Establish connection with: " + hostname);

        boolean isEstablished = false;

        try {
            serverAddress = InetAddress.getByName(hostname);
            connectionSocket = new DatagramSocket();
            isEstablished = true;
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

    public String executeCommand(String pCommand) throws IOException {

        sendBuffer = pCommand.getBytes();

        DatagramPacket request = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, port);
        connectionSocket.send(request);


        DatagramPacket response = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        connectionSocket.receive(response);

        String responseMessage = new String(receiveBuffer, 0, response.getLength());

        return responseMessage;

    }

}
