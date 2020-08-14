package gestao.exception;

public class EstoqueNotFoundException extends RuntimeException {
    public EstoqueNotFoundException(Long id) {
        super(String.format("Não foi encotrado nenhum Estoque com este id: %d", id));
    }
}
