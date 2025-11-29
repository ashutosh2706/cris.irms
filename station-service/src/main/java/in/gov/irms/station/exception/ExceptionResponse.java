package in.gov.irms.station.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@JsonPropertyOrder({"status", "error", "message", "details", "timeStamp", "path", "enquiryId"})
public class ExceptionResponse {
    private final Integer status;
    private final String path;
    private final String timeStamp;
    private final String error;
    private final String message;
    private final String enquiryId;

    public ExceptionResponse(HttpStatus httpStatus, HttpServletRequest request, Exception e) {
        this.status = httpStatus.value();
        this.timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.error = e.getClass().getSimpleName();
        this.message = e.getMessage();
        this.enquiryId = UUID.randomUUID().toString().substring(0, 8);
        this.path = request.getRequestURI();
    }

    public Integer getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getEnquiryId() {
        return enquiryId;
    }
}
