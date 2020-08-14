package gestao.service;

import gestao.exception.CheckinNotValidException;
import gestao.exception.CheckoutNotValidException;
import gestao.model.Internacao;
import gestao.repository.InternacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InternacaoService {

    @Autowired
    private InternacaoRepository repository;

    public Internacao findById(Long id) {
        Optional<Internacao> optional = this.repository.findById(id);
        if (optional == null || !optional.isPresent()) {
            throw new CheckinNotValidException("O paciente não está internado.");
        }
        return optional.get();
    }

    public Internacao save(Internacao internacao) {
        if (this.findInternacaoAbertaByPaciente(internacao.getPaciente().getId()) != null) {
            throw new CheckinNotValidException();
        }
        return this.repository.save(internacao);
    }

    public Internacao checkout(Long idPaciente, Long idHospital) {
        Internacao internacao = this.findInternacaoAbertaByPaciente(idPaciente);
        if (internacao == null || !internacao.getHospital().getId().equals(idHospital)) {
            throw new CheckoutNotValidException("O paciente não está internado neste hospital.");
        } else {
            internacao.setDataSaida(LocalDateTime.now());
            internacao = this.save(internacao);
        }
        return internacao;
    }

    public Internacao findInternacaoAbertaByPaciente(Long idPaciente) {
        return this.repository.findOpenedInternacaoByPaciente(idPaciente);
    }

}
