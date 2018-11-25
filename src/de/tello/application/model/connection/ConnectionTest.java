package de.tello.application.model.connection;

import org.opencv.core.Core;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ConnectionTest {


    public static DatagramSocket receiver;

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) throws IOException, InterruptedException {

        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        receiver = new DatagramSocket();

        String hostname = "192.168.10.1";
        int port = 8889;

        InetAddress address = InetAddress.getByName(hostname);
        DatagramSocket socket = new DatagramSocket();

        //byte[] buffer = new byte[512];

        //DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
        //socket.send(request);


        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.print("Enter Command : ");
            String input = scanner.nextLine();

            if ("q".equals(input)) {
                System.out.println("Exit!");
                break;
            }



            byte[] sendBuffer = new byte[512];
            sendBuffer = input.getBytes();

            DatagramPacket request = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
            socket.send(request);

            byte[] reiciveBuffer = new byte[512];

            DatagramPacket response = new DatagramPacket(reiciveBuffer, reiciveBuffer.length);
            socket.receive(response);

            String responseCommand = new String(reiciveBuffer, 0, response.getLength());


            for(int i =0; i < response.getLength(); i++){

                byte test = reiciveBuffer[i];
                short a = test;
                int b = a & 0xFFFF;
                System.out.println(reiciveBuffer[i] + "|" + b);

            }



            System.out.println(responseCommand);

            for(int i =0; i < response.getLength(); i++){

                byte test = response.getData()[i];
                short a = test;
                int b = a & 0xFFFF;
                System.out.println(reiciveBuffer[i] + "|" + b);

            }

            if("streamon".equals(input)){

                //new Thread(new UDPClient("0.0.0.0",11111)).start();
                //new Thread(new TelloVideo()).start();

            }



        }

        scanner.close();


    }


    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    public static void startVideoListener() throws IOException, InterruptedException {


        Thread thread = new Thread(){
            public void run(){
                byte[] reiciveBuffer = new byte[512];
                System.out.println("in thread");
                DatagramPacket response = new DatagramPacket(reiciveBuffer, reiciveBuffer.length);
                try {
                    receiver.receive(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String responseCommand = new String(reiciveBuffer, 0, response.getLength());
                System.out.println(responseCommand);
            }
        };

        thread.start();
        thread.sleep(1000);





    }

}
