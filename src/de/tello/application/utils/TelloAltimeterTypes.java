package de.tello.application.utils;

public enum TelloAltimeterTypes {


    RADIAL_280_DEGREES("RADIAL_280"),
    RADIAL_180_DEGREES("RADIAL_180"),
    RADIAL_180_DEGREES_ROTATED_LEFT("RADIAL_180_LEFT"),
    RADIAL_270_DEGREES_LEFT("RADIAL_270_LEFT");

    String type;

    TelloAltimeterTypes(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }




}
