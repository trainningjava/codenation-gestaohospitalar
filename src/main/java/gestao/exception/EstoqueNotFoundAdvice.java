package gestao.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class EstoqueNotFoundAdvice {

    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(EstoqueNotFoundException.class)
    String estoqueNotFoundHandler(EstoqueNotFoundException ex) {
        return ex.getMessage();
    }

}
