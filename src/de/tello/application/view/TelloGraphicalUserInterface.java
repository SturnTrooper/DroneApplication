package de.tello.application.view;

import de.tello.application.control.TelloGraphicalUserInterfaceController;
import de.tello.application.model.gui.TelloAltimeter;
import de.tello.application.model.gui.TelloBattery;
import de.tello.application.utils.TelloAltimeterScaleTypes;
import de.tello.application.utils.TelloAltimeterTypes;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class TelloGraphicalUserInterface extends Application {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private ImageView videoFeed;
    private TelloGraphicalUserInterfaceController controller;
    private TelloBattery battery;
    private TelloAltimeter altimeter;


    @Override
    public void start(Stage primaryStage) throws Exception {

        HBox header = new HBox();


        StackPane root = new StackPane();
        BorderPane layout = new BorderPane();
        videoFeed = new ImageView();
        battery = new TelloBattery();
        altimeter = new TelloAltimeter(TelloAltimeterTypes.RADIAL_270_DEGREES_LEFT,10.0);
        altimeter.createAltimeterComponents(350,350, TelloAltimeterScaleTypes.ONE_STEP_SCALING);
        altimeter.setTranslateY(720 - 350);

        //layout.setCenter(videoFeed);
        header.getChildren().add(battery);
        layout.setTop(header);
        layout.setRight(altimeter);

        root.getChildren().add(videoFeed);
        root.getChildren().add(layout);

        controller = new TelloGraphicalUserInterfaceController(this,"192.168.10.1",8889);





        Scene scene = new Scene(root,960,720); //1920, 1080

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.S) {
                System.out.println("Starting Drone");
                this.controller.fireCommand("takeoff");
            }

            if (e.getCode() == KeyCode.L) {
                System.out.println("Landing drone");
                this.controller.fireCommand("land");
            }

            if (e.getCode() == KeyCode.UP) {
                System.out.println("Move forward");
                this.controller.fireCommand("forward 30");
            }

            if(e.getCode() == KeyCode.U){
                System.out.println("Change Altitude");
                this.controller.fireCommand("up 20");
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();

        controller.initConnection();

    }

    public ImageView getVideoFeed(){
        return this.videoFeed;
    }

    public TelloBattery getBattery() {
        return this.battery;
    }

    public TelloAltimeter getAltimeter() {
        return this.altimeter;
    }

    public static void main(String[] args){

/*
        String bla = "pitch:0;roll:6;yaw:0;vgx:0;vgy:0;vgz:0;templ:82;temph:85;tof:10;h:0;bat:17;baro:406.92;time:0;agx:-7.00;agy:-113.00;agz:-1005.00;";
        String[] blub = bla.split(";");

        for(int i = 0; i < blub.length; i++){
            System.out.println(blub[i]);
            String[] test = blub[i].split(":");
            System.out.println(test[0] + " " + test[1]);
        }
*/
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }


}
