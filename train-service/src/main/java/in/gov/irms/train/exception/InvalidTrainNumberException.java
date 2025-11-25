package in.gov.irms.train.exception;

import java.io.Serial;

/**
 * TrainNumber provided doesn't exist,
 * Returns 400 BAD REQUEST
 */
public class InvalidTrainNumberException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public InvalidTrainNumberException(String message) {
        super(message);
    }
}
