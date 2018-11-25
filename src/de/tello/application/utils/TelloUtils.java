package de.tello.application.utils;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;

public final class TelloUtils {


    public static <T> void onFXThread(final ObjectProperty<T> property, final T value){
        Platform.runLater(() -> {
            property.set(value);
        });
    }


}
