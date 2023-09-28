package colin.julien.test.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class ParsingOrderException extends Exception{
    private static final Logger LOG = LoggerFactory.getLogger(ParsingOrderException.class);
    private final HttpStatus httpStatus;


    public ParsingOrderException(String errMessage) {
        super(errMessage);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ParsingOrderException(String errMessage, Throwable err) {
        super(errMessage, err);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ParsingOrderException(String errMessage,  HttpStatus httpStatus) {
        super(errMessage);
        this.httpStatus = httpStatus;
    }


    @Override
    public void printStackTrace() {
        LOG.error("Error status: {}", this.httpStatus);
        super.printStackTrace();
    }

}
