package gestao.exception;

public class PacienteNotFoundException extends RuntimeException {
    public PacienteNotFoundException(Long id) {
        super(String.format("Não foi encotrado nenhum Paciente com este id: %d", id));
    }
}
