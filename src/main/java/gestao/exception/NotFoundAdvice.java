package gestao.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class NotFoundAdvice {

    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(HospitalNotFoundException.class)
    String hospitalNotFoundHandler(HospitalNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(PacienteNotFoundException.class)
    String pacienteNotFoundHandler(PacienteNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(TratamentoNotFoundException.class)
    String tratamentoNotFoundHandler(TratamentoNotFoundException ex){
        return ex.getMessage();
    }
}
