package de.tello.application.utils;

public enum TelloAltimeterScaleTypes {

    ONE_STEP_SCALING(1),
    FIVE_STEP_SCALING(5),
    TEN_STEP_SCALING(10),
    TWENTY_STEP_SCALING(20);

    int scaling;

    TelloAltimeterScaleTypes(int value){
        this.scaling = value;
    }

    public int getScaling(){
        return this.scaling;
    }






}
