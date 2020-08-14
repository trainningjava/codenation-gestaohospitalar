package gestao.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class NotValidAdvice {

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(CheckinNotValidException.class)
    String checkinNotValidHandler(CheckinNotValidException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(CheckoutNotValidException.class)
    String checkoutNotValidHandler(CheckoutNotValidException ex){
        return ex.getMessage();
    }
}
