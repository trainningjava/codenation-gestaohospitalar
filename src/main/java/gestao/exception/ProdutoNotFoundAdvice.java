package gestao.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ProdutoNotFoundAdvice {

    /**
     * Produto sugerido não foi localizado
     * @param ex Parametro do erro
     * @return Mensagem de erro
     */
	@ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ProdutoNotFoundException.class)
    String produtoNotFoundHandler(ProdutoNotFoundException ex){
        return ex.getMessage();
    }

}
