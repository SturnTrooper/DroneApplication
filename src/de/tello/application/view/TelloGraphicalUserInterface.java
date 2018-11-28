package de.tello.application.view;

import de.tello.application.control.TelloGraphicalUserInterfaceController;
import de.tello.application.model.gui.TelloBattery;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class TelloGraphicalUserInterface extends Application {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private ImageView videoFeed;
    private TelloGraphicalUserInterfaceController controller;
    private TelloBattery battery;


    @Override
    public void start(Stage primaryStage) throws Exception {

        HBox header = new HBox();


        BorderPane layout = new BorderPane();
        videoFeed = new ImageView();
        battery = new TelloBattery();

        layout.setCenter(videoFeed);
        header.getChildren().add(battery);
        layout.setTop(header);

        controller = new TelloGraphicalUserInterfaceController(this,"192.168.10.1",8889);





        Scene scene = new Scene(layout,960,720); //1920, 1080

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

    public static void main(String[] args){

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }


}
