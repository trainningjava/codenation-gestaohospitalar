package gestao.repository;

import gestao.model.Tratamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TratamentoRepository extends JpaRepository<Tratamento, Long> {

    @Query("SELECT t from Tratamento t WHERE t.internacao.id = :idInternacao")
    List<Tratamento> listTratamentosPacientesInternados(@Param("idInternacao") Long idInternacao);

    @Query("SELECT t " +
            "from Tratamento t " +
            "inner join t.internacao i " +
            "inner join i.paciente p " +
            "WHERE p.id = :idPaciente")
    List<Tratamento> listAllTratamentosPacientes(@Param("idPaciente") Long idPaciente);

    @Query("SELECT t " +
            "from Tratamento t " +
            "inner join t.internacao i " +
            "inner join i.paciente p " +
            "WHERE p.id = :idPaciente " +
            "and t.data between :dtInicial and :dtFinal")
    List<Tratamento> listTratamentosPacientesByData(@Param("idPaciente") Long idPaciente, @Param("dtInicial")LocalDate dtInicial, @Param("dtFinal")LocalDate dtFinal);

}
