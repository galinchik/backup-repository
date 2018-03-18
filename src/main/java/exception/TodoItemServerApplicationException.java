package exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class TodoItemServerApplicationException extends Exception {

    private HttpStatus httpStatus;

    public TodoItemServerApplicationException(String message, Exception cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
