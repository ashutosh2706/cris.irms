package in.gov.irms.train.exception;

import java.io.Serial;

public class StationServiceException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public StationServiceException(String message) {
        super(message);
    }
}
