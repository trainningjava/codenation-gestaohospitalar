package gestao.exception;

public class HospitalNotFoundException extends RuntimeException {
    public HospitalNotFoundException(Long id) {
        super(String.format("Não foi encotrado nenhum Hospital com este id: %d", id));
    }
}
