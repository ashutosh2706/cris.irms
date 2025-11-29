package in.gov.irms.station.exception;

import java.io.Serial;

public class NoSuchStationException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public NoSuchStationException(String message) {
        super(message);
    }
}
