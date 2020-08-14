package gestao.service;

import gestao.exception.HospitalNotFoundException;
import gestao.model.Hospital;
import gestao.model.Leito;
import gestao.model.TipoLeito;
import gestao.repository.HospitalRepository;
import gestao.repository.LeitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeitoService {

    @Autowired
    private LeitoRepository leitoRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    public Leito save(Long hospitalId, TipoLeito tipo) {
        Hospital recovered = hospitalRepository
                .findById(hospitalId)
                .orElseThrow(() -> new HospitalNotFoundException((hospitalId)));

        Leito leito = new Leito();
        leito.setIsLivre(true);
        leito.setHospital(recovered);
        leito.setTipoLeito(tipo);

        leitoRepository.save(leito);

        return leito;
    }

    public int howManyBedsAreFree(Long idHospital) {
        Hospital recovered = hospitalRepository
                .findById(idHospital)
                .orElseThrow(() -> new HospitalNotFoundException((idHospital)));

        Optional<List<Leito>> returnedList = leitoRepository
                .findAllByIsLivreAndHospital(recovered);

        if (returnedList.isPresent()) {
            return returnedList.get().size();
        }
        return -1;
    }


}
