package de.tello.application.model.connection;

import de.tello.application.view.TelloGraphicalUserInterface;

import java.io.IOException;
import java.net.DatagramPacket;

public class TelloAltitudeStateListener implements Runnable {

    private byte[] sendBuffer;
    private byte[] receiveBuffer;
    private final UDPDroneClient telloUdpClient;
    private final TelloGraphicalUserInterface telloGui;

    public TelloAltitudeStateListener(UDPDroneClient pDroneClient, TelloGraphicalUserInterface telloGui){

        this.telloUdpClient = pDroneClient;
        this.sendBuffer = new byte[1024];
        this.receiveBuffer = new byte[1024];
        this.telloGui = telloGui;

    }


    @Override
    public void run() {

        while(true){

            if(telloUdpClient.isConnected()){

                sendBuffer = "height?".getBytes();

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

                System.out.println(commandResponse.length());
                String height = commandResponse.trim().substring(0, commandResponse.trim().length() - 2);
                System.out.println("NEW HEIGHT: " + height);
                if(!height.isEmpty()){
                    double altitude = Double.valueOf(height) / 10.0;
                    telloGui.getAltimeter().updateAltitude(altitude);
                }



            } else {

                String failureResponse = "Command not executed! Not connected to drone!";

            }

        }

    }
}
