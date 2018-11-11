package de.tello.application.model.connection;

import java.io.IOException;

public class UDPDroneVideoClient  implements UDPDroneClient{

    @Override
    public boolean establishConnection() {
        return false;
    }

    @Override
    public String executeCommand(String pCommand) throws IOException {
        return null;
    }
}
