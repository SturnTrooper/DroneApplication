package de.tello.application.control;

import de.tello.application.view.TelloGraphicalUserInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class TelloStateHandler implements Runnable {

    private final BlockingQueue<String> messageQueue;
    private final TelloGraphicalUserInterface telloGui;

    public TelloStateHandler(TelloGraphicalUserInterface pTelloGui,BlockingQueue<String> pMessageQueue){

        this.messageQueue = pMessageQueue;
        this.telloGui = pTelloGui;

    }

    @Override
    public void run() {

        try {

            if(!this.messageQueue.isEmpty()){

                String rawTelloStateData = this.messageQueue.take();

                if(rawTelloStateData != null && !rawTelloStateData.isEmpty()){

                    Map<String,String> parsedData = parseTelloStateData(rawTelloStateData.trim());

                    //Update Battery state
                    updateBatteryState(parsedData.get("bat"));
                    //updateAltimeter(parsedData.get("h"));

                }


            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     *
     * @param pRawStateData
     * @return
     */
    private Map<String,String> parseTelloStateData(String pRawStateData){

        String[] dataTuples = pRawStateData.split(";");
        Map<String,String> parsedData = new HashMap<String,String>();

        for(int i=0; i < dataTuples.length; i++){


            String[] dataPair = dataTuples[i].split(":");
            String key = dataPair[0];
            String value = dataPair[1];

            parsedData.put(key,value);

        }

        return parsedData;
    }

    /**
     *
     * @param pBatteryCharge
     */
    private void updateBatteryState(String pBatteryCharge){

        System.out.println("Update Battery");

        this.telloGui.getBattery().updateBatteryReadout(pBatteryCharge);

    }

    private void updateAltimeter(String pAltitude){

        System.out.println(pAltitude);

        double currentAltitude = Double.valueOf(pAltitude) / 100.0;

        this.telloGui.getAltimeter().updateAltitude(currentAltitude);

    }
}
