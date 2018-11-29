package de.tello.application.model.gui;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

public class TelloBattery extends Region {


    private static final String BATTERY_MODEL = "M 2.4092525,-7.5e-5 C 1.0807307,-7.5e-5 0,1.07776 0,2.40256 v 20.19473 c 0,1.3248 1.0807307,2.402635 2.4092525,2.402635 H 31.292391 c 1.328455,0 2.409252,-1.077835 2.409252,-2.402635 V 2.40256 C 33.701577,1.07776 32.620846,-7.5e-5 31.292391,-7.5e-5 Z m 1.1470902,1.9897 h 3.4914769 c 0.7631975,0 1.381775,0.616935 1.381775,1.37814 v 18.26432 h 1.3e-4 c 0,0.76087 -0.6185805,1.37801 -1.3816455,1.37801 H 3.5563427 c -0.7631977,0 -1.3817753,-0.61714 -1.3817753,-1.37801 V 3.367765 c 0,-0.761135 0.6187105,-1.37814 1.3817753,-1.37814 z m 7.6945333,0 h 3.491477 c 0.763198,0 1.381775,0.616935 1.381775,1.37814 v 18.26432 c 0,0.76087 -0.61871,1.37801 -1.381775,1.37801 h -3.491477 c -0.763198,0 -1.3817754,-0.61714 -1.3817754,-1.37801 V 3.367765 c 0,-0.761135 0.6187104,-1.37814 1.3817754,-1.37814 z m 7.694663,0 h 3.491477 c 0.763197,0 1.381645,0.616935 1.381645,1.37814 v 18.26432 c 0,0.76087 -0.618647,1.37801 -1.381645,1.37801 h -3.491477 c -0.763264,0 -1.381775,-0.61714 -1.381775,-1.37801 V 3.367765 c 0,-0.761135 0.618776,-1.37814 1.381775,-1.37814 z m 7.694533,0 h 3.491477 c 0.763197,0 1.381775,0.616935 1.381775,1.37814 v 18.26432 c 0,0.76087 -0.618644,1.37801 -1.381775,1.37801 h -3.491477 c -0.763198,0 -1.381775,-0.61714 -1.381776,-1.37801 V 3.367765 c 0,-0.761135 0.618778,-1.37814 1.381776,-1.37814 z m 8.517164,3.390935 c -0.131982,0 -0.238578,0.18113 -0.238578,0.404245 V 19.54755 c 0,0.223245 0.106596,0.404245 0.238578,0.404245 h 0.238577 c 1.328455,0 2.409123,-1.077835 2.409123,-2.402635 V 7.783195 C 37.805135,6.458395 36.724268,5.38056 35.395813,5.38056 Z";
    private static final double MODEL_STROKE_WIDTH = 2.0;
    private static final double BATTERY_READOUT_SIZE = 33.5;

    private DoubleProperty batteryRectangleWidth;
    private double conversionFactor;

    private Rectangle batteryChargeLevel;

    private Pane pane;


    public TelloBattery(){

        this.pane = new Pane();

        this.batteryChargeLevel = new Rectangle();
        this.batteryRectangleWidth = new SimpleDoubleProperty(29.5);

        this.conversionFactor = (BATTERY_READOUT_SIZE - MODEL_STROKE_WIDTH - MODEL_STROKE_WIDTH) / 100.0;

        initializeGraphicalComponents();

    }

    /**
     *
     */
    private void initializeGraphicalComponents(){

        SVGPath batteryModel = new SVGPath();
        batteryModel.setContent(BATTERY_MODEL);
        batteryModel.setStroke(Color.WHITE);
        batteryModel.setFill(Color.WHITE);
        batteryModel.setOpacity(0.4);
        //33.5 = 100 %->  33.5 - 2 - 2 = 29.5 -> 0.295
        //2px links rechts
        batteryChargeLevel.widthProperty().bind(batteryRectangleWidth);
        batteryChargeLevel.setHeight(21.0);
        batteryChargeLevel.setTranslateX(2.0);
        batteryChargeLevel.setTranslateY(2.0);
        batteryChargeLevel.setOpacity(0.4);

        //#5BC236
        batteryChargeLevel.setFill(Color.web("#5BC236"));

        pane.getChildren().addAll(batteryChargeLevel,batteryModel);
        getChildren().add(pane);

    }

    /**
     *
     * @param pBatteryCharge
     * @return
     */
    private Color determineBatteryChargeColor(double pBatteryCharge){

        if(pBatteryCharge >= 30.0){
            return Color.web("#5BC236");
        } else if(pBatteryCharge >= 20 && pBatteryCharge < 30){
            return Color.web("#FFCC00");
        } else {
            return Color.web("#FF0000");
        }
    }

    /**
     *
     * @param pBatteryCharge
     */
    public void updateBatteryReadout(String pBatteryCharge){

        double chargeInPercent = Double.valueOf(pBatteryCharge);
        System.out.println("Double: " + chargeInPercent);
        double rectAngleWidth = chargeInPercent * conversionFactor;
        System.out.println("Width: " + rectAngleWidth);

        if(rectAngleWidth < batteryRectangleWidth.getValue()){

            Platform.runLater(new Runnable() {
                @Override public void run() {

                    batteryRectangleWidth.set(rectAngleWidth);

                    batteryChargeLevel.setFill(determineBatteryChargeColor(chargeInPercent));
                    System.out.println("DONE!");

                }
            });
        }
    }

}
