package gestao.exception;

public class TratamentoNotFoundException extends RuntimeException {
    public TratamentoNotFoundException(Long id) {
        super(String.format("Não foi encotrado nenhum Tratamento com este id: %d", id));
    }
}
