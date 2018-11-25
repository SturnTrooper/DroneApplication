package de.tello.application.view;

import de.tello.application.control.TelloGraphicalUserInterfaceController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class TelloGraphicalUserInterface extends Application {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private ImageView videoFeed;


    @Override
    public void start(Stage primaryStage) throws Exception {


        BorderPane layout = new BorderPane();
        videoFeed = new ImageView();
        layout.setCenter(videoFeed);

        TelloGraphicalUserInterfaceController controller = new TelloGraphicalUserInterfaceController(this,"192.168.10.1",8889);

        Scene scene = new Scene(layout,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.show();

        controller.initConnection();

    }

    public ImageView getVideoFeed(){
        return this.videoFeed;
    }



    public static void main(String[] args){

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }


}
