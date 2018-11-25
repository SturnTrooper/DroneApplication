package de.tello.application.model.connection;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UDPClient implements Runnable {


    private DatagramSocket clientSocket;
    private InetAddress address;
    private int port;

    boolean isStart = false;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    int picCount = 0;

    byte[] picture;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();


    boolean isFrame = false;

    private byte[] outData;

    public UDPClient(String pHostname, int pPort) throws SocketException, UnknownHostException {


        this.address = InetAddress.getByName(pHostname);
        this.port = pPort;

        //this.outData = new byte[512];



    }



    @Override
    public void run() {

       // DatagramPacket in = new DatagramPacket(outData, outData.length);
        boolean isStarted = false;
        int packageNumber = 1;

        outData = new byte[1024];


        try {
            clientSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        byte[] packetBuffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(packetBuffer, 1024);

        while(packageNumber < 30){



            try {
                clientSocket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();

            }

            //ByteArrayInputStream bin = new ByteArrayInputStream(packet.getData());
            //DataInputStream din = new DataInputStream(bin);

            //byte seqNumByte = packetBuffer[0];
            //String s2 = String.format("%8s", Integer.toBinaryString(seqNumByte & 0xFF)).replace(' ', '0');
            System.out.println("----- PACKAGE " + packageNumber + " ----------");

            System.out.println(packet.getLength());
            StringBuilder builder = new StringBuilder();

            for(int i = 0; i < packet.getLength() ; i++){

                int k = packet.getData()[i] & 0xFF;
                builder.append(Integer.toHexString(k));
                builder.append(" ");
            }

            System.out.println(builder.toString());







/*
            for(int i=0; i < 8; i++){

                byte seqNumByte = packetBuffer[i];
                System.out.println(seqNumByte & 0x1f);
                String s2 = String.format("%8s", Integer.toBinaryString(seqNumByte & 0xFF)).replace(' ', '0');
                System.out.println(i + ": " + s2);
            }

            for(int i =0; i < 8; i++){

                byte test = packetBuffer[i];
                short a = test;
                int b = a & 0xFF;
                System.out.println(packetBuffer[i] + "|" + b);

            }
*/
            byte b0 = packetBuffer[0];
            String s0 = String.format("%8s", Integer.toBinaryString(b0 & 0xFF)).replace(' ', '0');
            byte b1 = packetBuffer[1];
            String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
            byte b2 = packetBuffer[2];
            String s2 = String.format("%8s", Integer.toBinaryString(b2 & 0xFF)).replace(' ', '0');
            byte b3 = packetBuffer[3];
            String s3 = String.format("%8s", Integer.toBinaryString(b3 & 0xFF)).replace(' ', '0');
            //byte b0 = packetBuffer[4];
            //received.bytes[6] & 0x1f
            /*
            if(s0.equals("00000000") && s1.equals("00000000") && s2.equals("00000000") && s3.equals("00000001")){

                if(isFrame == false){
                    isFrame = true;
                    //picture = packet.getData();
                    try {
                        baos.write(packet.getData());

                        //baos.write(Arrays.copyOfRange(packet.getData(),2,packet.getData().length));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    picture = baos.toByteArray();
                    System.out.println(picture.length);

                    H264Decoder _decoder = new H264Decoder();
                    ByteBuffer bb = ByteBuffer.wrap( baos.toByteArray() );
                    bb.rewind();
                    // Create a buffer to hold the output picture which is big enough
                    Picture outBuffer = Picture.create( 1920, 1088, ColorSpace.YUV420 );
                    Picture pic = _decoder.decodeFrame( bb, outBuffer.getData() );
                    BufferedImage bufferedImage = AWTUtil.toBufferedImage( pic );

                    try {
                        ImageIO.write(bufferedImage, "jpg", new File("/Users/florian/Downloads/filename_" + packageNumber + ".jpg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
                    /*
                    BufferedImage image = null;
                    try {
                        image = ImageIO.read( new ByteArrayInputStream(picture));
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                    if(image == null){
                        System.out.println("FUCK");
                    }

                    try {
                        ImageIO.write(image, "jpg", new File("/Users/florian/Downloads/filename_" + packageNumber + ".jpg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    */
/*
                    //For new package
                    baos.reset();
                    try {
                        baos.write(packet.getData());
                        //baos.write(Arrays.copyOfRange(packet.getData(),2,packet.getData().length));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                try {
                    //baos.write(Arrays.copyOfRange(packet.getData(),2,packet.getData().length));
                    baos.write(packet.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }





            //ByteBuffer b = ByteBuffer.wrap(seqNumByte).order(ByteOrder.LITTLE_ENDIAN);

            //int seq = b.getInt();
            //int sub_seq = b.getInt();

            //System.out.println("----- PACKAGE " + packageNumber + " ----------");
            //System.out.println("SEQ: " + seq);
            //System.out.println("SUB-SEQ: " + sub_seq);



/*
            byte[] bla = new byte[in.getLength()];
            bla = in.getData();
            String modifiedSentence = new String(in.getData());

            int fragment_type = bla[0] & 0x1F;
            int nal_type = bla[1] & 0x1F;
            int start_bit = bla[1] & 0x80;
            int end_bit = bla[1] & 0x40;
*/


/*
            System.out.println("-------- PACKAGE " + packageNumber + " ---------");
            System.out.println("FragmentType: " + fragment_type);
            System.out.println("NAL_TYPE: " + nal_type);
            System.out.println("START_BIT " + start_bit);
            System.out.println("END_BIT " + end_bit);

            //java.nio.ByteBuffer.wrap(bytes).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt()
            byte hm = ByteBuffer.wrap(bla).order(ByteOrder.LITTLE_ENDIAN).get(1);
            //byte[] blaOrdered = ByteBuffer.wrap(bla).order(ByteOrder.LITTLE_ENDIAN).get

            System.out.println("Sequence_Number: " + bla[0]);
            System.out.println("SUB_SEQUENCE: " + bla[1]);
            int packets_perFrame = bla[1] & 0x7f;
            System.out.println("PACKETS_PER_FRAME: " + packets_perFrame);
            int dji_nal = bla[6] & 0x1f;
            System.out.println("DJI NAL: " + dji_nal);
            System.out.println("LITTLE-ENDIAN: " + Integer.valueOf(hm));

            //String message = new String(bla, StandardCharsets.US_ASCII);

            if(bla[2] == 0 && bla[3] == 0 && bla[4] == 0 && bla[5] == 1){
                System.out.println("Array: " + Arrays.toString(bla));

           }

            int length = bla.length;

            byte [] pic = Arrays.copyOfRange(bla,1,bla.length);

            BufferedImage image = null;
            try {
                image = ImageIO.read( new ByteArrayInputStream( pic ) );
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ImageIO.write(image, "BMP", new File("/Users/florian/Downloads/filename.bmp"));
            } catch (IOException e) {
                e.printStackTrace();
            }

*/
            //System.out.println(message);

            //String message = new String(in.getData(),0,in.getLength());
           //System.out.println("Message: " + message);
           // System.out.println("[" + Integer.parseInt(String.valueOf(in.getData()[0]),16) + "|" + in.getData()[1] + "]");


            //System.out.println("----- PACKAGE " + packageNumber + " ----------");
            //System.out.println("LAST: " + in.getData()[in.getData().length-1] + " | " + in.getData()[in.getData().length-2]);

            //System.out.println(in.getAddress());
            //System.out.println(in.getPort());

            //byte [] h1 = Arrays.copyOfRange(outData,0,1);
            //ByteBuffer bb = ByteBuffer.wrap(outData);
            //int num = bb.getInt();

            //System.out.println("Short: " + num);


            //byte seqByte = outData[0];
            //short seqShort = seqByte;
            //int b = seqShort & 0xFFFF;
            //int uint_81 = seqByte & 0xFF;

            //System.out.println("SEQ: " + b);

            //byte seqByte2 = outData[1];
            //short seqShort2 = seqByte;
            //int b2 = seqShort2 & 0xFFFF;
            //int uint_8 = seqByte2 & 0xFF;

            //System.out.println("SUB_SEQ: " + b2);

            //StringBuffer buffer = new StringBuffer();

           // System.out.println(outData[0] + " | " + outData[1]);
           // in.setLength(outData.length);


/*
            Character.forDigit((bla[0] & 0xF), 16);

            for(int i=0; i < bla.length; i++){
                buffer.append(Character.forDigit((bla[i] >> 4) & 0xF, 16));
                buffer.append(Character.forDigit((bla[i] & 0xF), 16));
                buffer.append(" | ");
            }

            */

            //ByteBuffer bb = ByteBuffer.wrap(bla);
            //bb.order( ByteOrder.LITTLE_ENDIAN);
            //byte [] bla2 = bb.array();

           // System.out.println(String.valueOf(bla[0]& 0xffffffffl) + "|" + String.valueOf(bla[1] & 0xffffffffl));


/*


            if(in.getData()[0] == 0 && in.getData()[1] == 0){

                if(isStart == false){
                    try {
                        outputStream.write(Arrays.copyOfRange(in.getData(),1,in.getData().length));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isStart = true;
                } else {

                    byte [] pic = outputStream.toByteArray();
                    System.out.println(pic.length);

                    BufferedImage image = null;
                    try {
                        image = ImageIO.read( new ByteArrayInputStream( pic ) );
                    } catch (IOException e) {
                        System.out.println("Read Exception");
                        e.printStackTrace();
                    }
                    try {
                        ImageIO.write(image, "BMP", new File("/Users/florian/Downloads/filename.bmp"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


            }

            if(isStart == true){
                try {
                    outputStream.write(Arrays.copyOfRange(in.getData(),1,in.getData().length));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

*/
/*
            if(fragment_type == 28){
            //    if(start_bit == 0 && end_bit == 0){
            //        System.out.println("Length: " + in.getData().length);
            //        System.out.println("START PACKAGE: " + packageNumber);
            //    }
                if(start_bit == 1){
                    System.out.println("START PACKAGE: " + packageNumber);
                }
            }

            if(start_bit == 0 && end_bit == 1){
                System.out.println("END PACKAGE: " + packageNumber);
            }

*/


            packageNumber++;
/*

            byte[] received = in.getData();

            if (received[2] == 0 && received[3] == 0 && received[4] == 0 && received[5] == 1){

                int nal = (received[6] & 0x1f);
                isStarted = true;

            }

            if(isStarted){

            }
*/
        }



    }
}
