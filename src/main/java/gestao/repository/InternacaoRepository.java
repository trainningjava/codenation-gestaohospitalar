package gestao.repository;

import gestao.model.Internacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InternacaoRepository extends JpaRepository<Internacao, Long> {

    @Query("SELECT i from Internacao i WHERE i.paciente.id = :idPaciente and i.dataSaida is null")
    Internacao findOpenedInternacaoByPaciente(@Param("idPaciente") Long idPaciente);
}
