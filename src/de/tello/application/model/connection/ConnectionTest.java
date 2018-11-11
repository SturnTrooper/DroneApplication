package de.tello.application.model.connection;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ConnectionTest {


    public static void main(String[] args) throws IOException {

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

            System.out.println(responseCommand);



        }

        scanner.close();


    }
}
