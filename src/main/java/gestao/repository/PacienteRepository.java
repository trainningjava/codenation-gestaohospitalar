package gestao.repository;

import gestao.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Query("SELECT p from Internacao i inner join i.paciente p WHERE i.hospital.id = :idHospital and i.dataSaida is null")
    List<Paciente> listPacientesInternadosSemAlta(@Param("idHospital") Long idHospital);

    @Query("SELECT p from Internacao i inner join i.paciente p WHERE i.hospital.id = :idHospital")
    List<Paciente> listPacientesInternados(@Param("idHospital") Long idHospital);
}
