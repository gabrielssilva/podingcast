package gabrielssilva.podingcast.controller;

import org.apache.http.entity.StringEntity;

public class Web {

    private static Web instance;

    private Web() {

    }

    public static Web getInstance() {
        if (instance == null) {
            instance = new Web();
        } else {
            // Do nothing.
        }

        return instance;
    }

    public void send(StringEntity data) {

    }

}
