package de.tello.application.model.connection;

import de.tello.application.view.TelloGraphicalUserInterface;

import java.io.IOException;
import java.net.DatagramPacket;

public class TelloCommandSender implements Runnable {

    private byte[] sendBuffer;
    private byte[] receiveBuffer;
    private final UDPDroneClient telloUdpClient;
    private String command;

    public TelloCommandSender(UDPDroneClient pDroneClient, String command){

        this.telloUdpClient = pDroneClient;
        this.sendBuffer = new byte[1024];
        this.receiveBuffer = new byte[1024];
        this.command = command;

    }


    @Override
    public void run() {

        if(telloUdpClient.isConnected()){

            sendBuffer = command.getBytes();

            DatagramPacket request = new DatagramPacket(sendBuffer, sendBuffer.length, telloUdpClient.getServerAddress(), telloUdpClient.getPort());
            try {
                telloUdpClient.getConnectionSocket().send(request);
            } catch (IOException e) {
                e.printStackTrace();
            }

            DatagramPacket response = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            try {
                telloUdpClient.getConnectionSocket().receive(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String commandResponse = new String(receiveBuffer, 0, response.getLength());


        } else {

            String failureResponse = "Command not executed! Not connected to drone!";

        }
    }
}
