package gestao.service;

import gestao.exception.PacienteNotFoundException;
import gestao.exception.TratamentoNotFoundException;
import gestao.model.Internacao;
import gestao.model.Tratamento;
import gestao.repository.TratamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TratamentoService {

    @Autowired
    private TratamentoRepository repository;

    public Tratamento save(Tratamento tratamento) {
        return repository.save(tratamento);
    }

    public Tratamento findBy(Long id){
        Optional<Tratamento> optional = repository.findById(id);
        if (optional == null || !optional.isPresent()) {
            throw new TratamentoNotFoundException(id);
        }
        return optional.get();
    }

    public List<Tratamento> listTratamentosPacientesInternados(Internacao internacao, Long idPaciente, Long idHospital) {
        if (internacao == null || !internacao.getHospital().getId().equals(idHospital)) {
            throw new PacienteNotFoundException(idPaciente);
        }
        return this.repository.listTratamentosPacientesInternados(internacao.getId());
    }

    public List<Tratamento> listTratamentosPacientes(Long idPaciente) {
        return this.repository.listAllTratamentosPacientes(idPaciente);
    }

    public List<Tratamento> listTratamentosPacientes(Long idPaciente, LocalDate dataInicial, LocalDate dataFinal) {
        return this.repository.listTratamentosPacientesByData(idPaciente, dataInicial, dataFinal);
    }

    public void update(Long id, Tratamento tratamento){
        Tratamento saved = this.findBy(id);
        saved.setData(tratamento.getData());
        saved.setInternacao(tratamento.getInternacao());
        saved.setProcedimento(tratamento.getProcedimento());
        saved.setProduto(tratamento.getProduto());
        this.repository.save(saved);
    }

    public void remove(Long id) {
        repository.deleteById(id);
    }
}
