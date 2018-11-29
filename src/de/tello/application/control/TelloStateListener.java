package de.tello.application.control;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class TelloStateListener implements Runnable {

    private String hostname;
    private int port;

    private DatagramSocket connectionSocket;
    private InetSocketAddress serverAddress;
    private byte[] receiveBuffer;

    private final BlockingQueue<String> messageQueue;

    public TelloStateListener(String pHostname, int pPort,BlockingQueue<String> pMessageQueue){

        this.hostname = pHostname;
        this.port = pPort;
        this.receiveBuffer = new byte[1024];

        this.messageQueue = pMessageQueue;

        establishConnection();

    }

    private void establishConnection(){

        try {
            serverAddress = new InetSocketAddress(hostname,port);
            connectionSocket = new DatagramSocket(null);
            connectionSocket.bind(serverAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void run() {

        DatagramPacket response = new DatagramPacket(receiveBuffer, receiveBuffer.length);

        try {
            connectionSocket.receive(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String currentDroneState = new String(receiveBuffer, 0, response.getLength());

        try {
            this.messageQueue.put(currentDroneState);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
