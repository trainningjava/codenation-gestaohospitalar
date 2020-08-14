package gestao.exception;

public class CheckoutNotValidException extends RuntimeException {
    public CheckoutNotValidException(String mensagem) {
        super(mensagem);
    }
}
