package de.tello.application.model.gui;

import de.tello.application.utils.TelloAltimeterScaleTypes;
import de.tello.application.utils.TelloAltimeterTypes;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.util.logging.Logger;

public class TelloAltimeter extends Region {

    private static double MAX_DRONE_ALTITUDE = 30.0; //Meters
    private static double RADIAL_ALTIMETER_ANGLE_OFFSET = 130.0;
    private static double DESCRIPTION_SHIFT_OFFSET = 0.0;

    private Pane altimeterPane;

    private double centerX;
    private double centerY;
    private double radiusX;
    private double radiusY;

    private double altimeterWidth;
    private double altimeterHeight;

    private double altimeterFrameStartingAngle;
    private double altimeterFrameLength;

    private DoubleProperty visualAltitudeIndicatorValue;
    private DoubleProperty altitudeTextPositionX;
    private DoubleProperty altitudeTextPositionY;

    private StringProperty textualAltitudeInformationValue;

    private double actualDroneAltitude;

    private Text textualAltitudeInformation;


    /** Constructor to create an altimeter for the dji tello drone.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param pAltimeterType => type of the altimeter (e.g.: Radial)
     * @param pMaxAltitude => maximum altitude shown by the altimeter
     */
    public TelloAltimeter(TelloAltimeterTypes pAltimeterType, double pMaxAltitude){

        this.altimeterPane = new Pane();
        this.actualDroneAltitude = 0.0;

        MAX_DRONE_ALTITUDE = pMaxAltitude;

        initializeListener();
        setAltimeterSpecs(pAltimeterType);

        System.out.println("Initializing Altimeter complete");

    }

    /** Function to set the x coordinate of the center point of the altimeter gauge. The center coordinate is calculated
     *  based on the desired with of the altimeter.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 28.11.2018
     *
     * @param width => desired width of the altimeter
     */
    private void setCenterX(double width){

        this.centerX = ((width)/2.0);
    }

    /** Function to set the y coordinate of the center point of the altimeter gauge. The center coordinate is calculated
     *  based on the desired height of the altimeter.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param height => desired height of the altimeter
     */
    private void setCenterY(double height){

        this.centerY = (height)/2.0;
    }

    /** Function to set the x radius of the altimeter gauge. The radius is calculated based on the desired width of the
     *  altimeter.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param width => desired width of the altimeter
     */
    private void setRadiusX(double width){

        this.radiusX = (width / 2.0) - 0.2*width;
    }

    /** Function to set the y radius of the altimeter gauge. The radius is calulated based on the desired height of the
     *  altimeter.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param height => desired height of the altimeter
     */
    private void setRadiusY(double height){

        this.radiusY = (height / 2.0) - 0.2*height;
    }

    /** Function to set the desired width of the altimeter.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param width => desired width of the altimeter
     */
    private void setAltimeterWidth(double width){

        this.altimeterWidth = width;

    }

    /** Function to set the desired height of the altimeter.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param height => desired height of the altimeter
     */
    private void setAltimeterHeight(double height){

        this.altimeterHeight = height;

    }

    /** This function takes a given Width and Height to set all the required size
     * 	parameters (e.g. radius or center) of the altimeter.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param width => desired width of altimeter
     * @param height => desired height of altimeter
     */
    private void initializeSizeSettings(double width, double height){

        setCenterX(width);
        setCenterY(height);
        setRadiusX(width);
        setRadiusY(height);
        setAltimeterWidth(width);
        setAltimeterHeight(height);

    }

    /** This function initializes the listeners which will be bound to
     * 	the specific model components of the altimeter in order to update
     * 	their values whenever the altitude of the drone changes.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     */
    private void initializeListener(){

        visualAltitudeIndicatorValue = new SimpleDoubleProperty();
        textualAltitudeInformationValue = new SimpleStringProperty();
        altitudeTextPositionX = new SimpleDoubleProperty();
        altitudeTextPositionY = new SimpleDoubleProperty();

    }

    /** This function initializes the parameters which are unique to each specific
     * 	altimeter type.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param type => type of the altimeter (e.g.: Radial)
     */
    private void setAltimeterSpecs(TelloAltimeterTypes type){

        String tachoMeterType = type.getType();

        switch(tachoMeterType){

            case "RADIAL_280":

                altimeterFrameStartingAngle = -130.0;
                altimeterFrameLength = 280.0;
                RADIAL_ALTIMETER_ANGLE_OFFSET = 130.0;
                DESCRIPTION_SHIFT_OFFSET = 50.0;
                break;

            case "RADIAL_180":

                altimeterFrameStartingAngle = 180.0;
                altimeterFrameLength = 180.0;
                RADIAL_ALTIMETER_ANGLE_OFFSET = 180.0;
                DESCRIPTION_SHIFT_OFFSET = 0.0;
                break;

            case "RADIAL_180_LEFT":

                altimeterFrameStartingAngle = -90.0;
                altimeterFrameLength = 180.0;
                RADIAL_ALTIMETER_ANGLE_OFFSET = 90.0;
                DESCRIPTION_SHIFT_OFFSET = 90.0;
                break;

            case "RADIAL_270_LEFT":

                altimeterFrameStartingAngle = -90.0;
                altimeterFrameLength = 270.0;
                RADIAL_ALTIMETER_ANGLE_OFFSET = 90.0;
                DESCRIPTION_SHIFT_OFFSET = 90.0;
                break;

        }

    }

    /** Function to create the graphical components of the altimeter. The graphical components
     *  are created based on the width, height, scaling and type of the desired altimeter.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param width => desired width of the altimeter
     * @param height => desired height of the altimeter
     * @param scaling => scaling of the altimeter (e.g.: 5 steps)
     */
    public void createAltimeterComponents(double width, double height, TelloAltimeterScaleTypes scaling){

        initializeSizeSettings(width,height);

        //Create TachoMeter frame
        Arc altimeterFrame = createAltimeterFrame();
        Rectangle altimeterMeterBackground = new Rectangle(width,height);
        altimeterMeterBackground.setFill(Color.TRANSPARENT);

        Group bigAltimeterTickmarks = createBigTickMarks(scaling.getScaling());
        Group analogAltimeterDisplay = createAnalogAltimeterDisplay();
        Group smallAltimeterTickmarks = createSmallTickMarks(scaling.getScaling());
        Group tickMarkDescriptions = createTickMarkerDescriptions(scaling.getScaling());

        Arc currentAltitudeIndication = initializeGraphicalAltitudeIndicator();

        altimeterPane.getChildren().setAll(altimeterMeterBackground,currentAltitudeIndication,altimeterFrame,smallAltimeterTickmarks,bigAltimeterTickmarks, analogAltimeterDisplay, tickMarkDescriptions);

        getChildren().setAll(altimeterPane);

    }


    /** Function that creates the textual representation of the drones current altitude.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @return => a group object containing the textual altitude indicator
     *            as well as its corresponding unit label.
     */
    private Group createAnalogAltimeterDisplay(){

        Group components = new Group();

        textualAltitudeInformation = new Text("0");
        textualAltitudeInformation.setFont(new Font(altimeterWidth/5.0));
        textualAltitudeInformation.setFill(Color.WHITE);
        textualAltitudeInformation.setOpacity(0.4);

        double width = textualAltitudeInformation.getLayoutBounds().getWidth();
        double height = textualAltitudeInformation.getLayoutBounds().getHeight();

        //display.setX(centerX - (width/2.0));
        textualAltitudeInformation.setY(centerY);
        textualAltitudeInformation.textProperty().bind(textualAltitudeInformationValue);

        textualAltitudeInformation.translateXProperty().bind(altitudeTextPositionX);
        textualAltitudeInformation.translateYProperty().bind(altitudeTextPositionY);

        altitudeTextPositionX.set(centerX- (width/2.0));

        components.getChildren().add(textualAltitudeInformation);

        Text description = createAltitudeMetricLabel(height);

        components.getChildren().add(description);

        return components;
    }

    /** Function which creates the altitude unit (e.g.: m, km) label.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param height => desired height of the altimeter
     * @return => text object containing the unit representation of the altimeter
     */
    private Text createAltitudeMetricLabel(double height){

        Text metric = new Text("m");
        metric.setFont(new Font(0.6 * (altimeterWidth/10.0)));
        metric.setFill(Color.WHITE);

        double width = metric.getLayoutBounds().getWidth();

        metric.setX(centerX - (width/2.0));
        metric.setY(centerY + 0.25*height);

        metric.setOpacity(0.4);



        return metric;

    }

    /** This function creates the frame of the altimeter. The frame depends on the chosen
     *  TelloAltimeterType and the desired size of the altimeter.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     *
     * @return => arc object representing the frame of the altimeter
     */
    private Arc createAltimeterFrame(){

        Arc frame = new Arc(centerX, centerY,radiusX,radiusY,altimeterFrameStartingAngle,-altimeterFrameLength);
        frame.setType(ArcType.OPEN);
        frame.setStrokeWidth(5.0);
        frame.setStroke(Color.WHITE);
        frame.setStrokeLineCap(StrokeLineCap.BUTT);
        frame.setFill(null);
        frame.setOpacity(0.6);

        return frame;
    }

    /** This function initializes the graphical altitude indicator of the altimeter.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @return => arc object representing the graphical altitude information
     */
    private Arc initializeGraphicalAltitudeIndicator(){

        Arc indicator = new Arc(centerX, centerY, radiusX + 0.05*radiusX, radiusY + 0.05*radiusY, altimeterFrameStartingAngle, -100.0 * (altimeterFrameLength/140.0));
        indicator.setType(ArcType.OPEN);
        indicator.setStrokeWidth(0.1*radiusX);
        indicator.setStroke(Color.BLUE);
        indicator.setStrokeLineCap(StrokeLineCap.BUTT);
        indicator.setFill(null);
        indicator.lengthProperty().bind(visualAltitudeIndicatorValue);
        indicator.setOpacity(0.8);

        return indicator;
    }

    /** This function creates the big tickmarks for the altimeter. The number of tickmarks
     *  depends on the maximum altitude to be displayed on the altimeter as well as the
     *  chosen altitude interval (e.g.: 20 or 10 steps between altitude markers)
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param altitudeInterval => altitude interval steps
     * @return => group object containing the tickmarks for the altimeter
     */
    private Group createBigTickMarks(double altitudeInterval){

        Group tickMarks = new Group();
        double targetAngle = 0.0;

        double numberOfBigTickMarks = MAX_DRONE_ALTITUDE / altitudeInterval;
        double angleSteps = altimeterFrameLength / numberOfBigTickMarks;

        for(double i = 0.0; i <= numberOfBigTickMarks; i++){

            Rotate tickMarkerPosition;

            targetAngle = calculateTargetAngle(i,angleSteps);
            tickMarkerPosition = new Rotate(targetAngle);

            Line tickMarker = new Line();

            tickMarker = new Line(radiusX,0,radiusX + 0.2*radiusX,0);
            tickMarker.setStrokeWidth(5.0);
            tickMarker.setStroke(Color.WHITE);
            tickMarker.setStrokeLineCap(StrokeLineCap.ROUND);
            tickMarker.setOpacity(0.6);

            tickMarker.setTranslateX(centerX);
            tickMarker.setTranslateY(centerY);
            tickMarker.getTransforms().add(tickMarkerPosition);
            tickMarks.getChildren().add(tickMarker);

        }

        return tickMarks;
    }

    /** This function creates the small tickmarks for the altimeter. he number of tickmarks
     *  depends on the maximum altitude to be displayed on the altimeter as well as the
     *  chosen altitude interval.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param altitudeInterval => altitude interval steps
     * @return => group object containing the small tickmarks of the altimeter
     */
    private Group createSmallTickMarks(double altitudeInterval){

        Group tickMarks = new Group();
        double targetAngle = 0.0;

        double numberOfBigTickMarks = MAX_DRONE_ALTITUDE / altitudeInterval;
        double numberOfSmallTickMarks = 10 * numberOfBigTickMarks;
        double angleSteps = altimeterFrameLength / numberOfSmallTickMarks;

        for(double i = 0.0; i <= numberOfSmallTickMarks; i++){

            Rotate tickMarkerPosition;

            targetAngle = calculateTargetAngle(i,angleSteps);
            tickMarkerPosition = new Rotate(targetAngle);

            Line tickMarker = new Line();

            tickMarker = new Line(radiusX,0,radiusX + 0.1*radiusX,0);
            tickMarker.setStrokeWidth(2.0);
            tickMarker.setStroke(Color.WHITE);
            tickMarker.setStrokeLineCap(StrokeLineCap.ROUND);
            tickMarker.setOpacity(0.4);

            tickMarker.setTranslateX(centerX);
            tickMarker.setTranslateY(centerY);
            tickMarker.getTransforms().add(tickMarkerPosition);
            tickMarks.getChildren().add(tickMarker);

        }

        return tickMarks;
    }

    /** This function creates the labeling of the big altimeter tickmarks.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param altimeterInterval => altitude interval steps
     * @return => group containing the tickmark descriptions of the altimeter
     */
    private Group createTickMarkerDescriptions(double altimeterInterval){

        Group tickMarkDescriptions = new Group();

        double targetAngle = 0.0;
        double numberOfDescriptions = MAX_DRONE_ALTITUDE / altimeterInterval;
        double angleSteps = altimeterFrameLength / numberOfDescriptions;

        for(double i = 0.0; i <= numberOfDescriptions; i++){

            Rotate textRotation;
            targetAngle = calculateTargetAngle(i,angleSteps) - RADIAL_ALTIMETER_ANGLE_OFFSET - DESCRIPTION_SHIFT_OFFSET;

            double x = centerX - 0.85*radiusX * Math.cos(Math.toRadians(-targetAngle)) - 0.07*radiusX;
            double y = centerY + 0.85*radiusY * Math.sin(Math.toRadians(-targetAngle)) + 5.0;

            String speedDescription = String.valueOf(Math.round(i*altimeterInterval));

            Text description = new Text(x,y,speedDescription);
            description.setFill(Color.WHITE);
            description.setFont(Font.font("Verdana", FontWeight.BOLD, 0.04*altimeterWidth)); //0.02

            textRotation = new Rotate(0);
            description.getTransforms().add(textRotation);

            description.setOpacity(0.6);

            tickMarkDescriptions.getChildren().add(description);

        }

        return tickMarkDescriptions;
    }

    /** Function to calculate the target for the graphical components of the altimeter.
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param factor
     * @param angleStep
     * @return => calculated target angle
     */
    private double calculateTargetAngle(double factor, double angleStep){

        return factor * angleStep + RADIAL_ALTIMETER_ANGLE_OFFSET;
    }

    /** Function to update the altitude on the graphical user interface
     *
     * @author Florian Sturn
     * @version 1.0
     * @date 29.11.2018
     *
     * @param currentAltitude => altitude transmitted by the drone
     */
    public void updateAltitude(double currentAltitude){

        if(currentAltitude < actualDroneAltitude || currentAltitude > actualDroneAltitude){

            System.out.println("Altitude changed. Updating GUI");

            this.actualDroneAltitude = currentAltitude;

            Platform.runLater(new Runnable(){

                @Override
                public void run() {

                    System.out.println("Updating Altitude: " + currentAltitude);

                    visualAltitudeIndicatorValue.set(-actualDroneAltitude * (altimeterFrameLength / MAX_DRONE_ALTITUDE));
                    textualAltitudeInformationValue.set(String.valueOf(actualDroneAltitude)); //Math.round(actualDroneAltitude)

                    double width = textualAltitudeInformation.getLayoutBounds().getWidth();

                    altitudeTextPositionX.set(centerX - width / 2.0);

                    System.out.println("GUI updated!");
                }
            });
        }

    }
}
