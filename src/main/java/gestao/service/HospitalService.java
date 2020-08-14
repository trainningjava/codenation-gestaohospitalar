package gestao.service;

import gestao.exception.HospitalNotFoundException;
import gestao.model.Estoque;
import gestao.model.Hospital;
import gestao.model.Produto;
import gestao.repository.HospitalRepository;
import gestao.repository.LeitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository repository;

    public List<Hospital> listAll() {
        return stream(repository.findAll().spliterator(), true).collect(toList());
    }

    public Hospital save(Hospital hospital) {
        return repository.save(hospital);
    }

    public Hospital findBy(Long id) {
        Optional<Hospital> optionalHospital = repository.findById(id);
        if (optionalHospital == null || !optionalHospital.isPresent()) {
            throw new HospitalNotFoundException(id);
        }
        return optionalHospital.get();
    }

    public void update(Long id, Hospital hospital) {
        Optional<Hospital> optionalHospital = repository.findById(id);
        if (optionalHospital != null && optionalHospital.isPresent()) {
            Hospital savedHospital = optionalHospital.get();
            savedHospital.setNome(hospital.getNome());
            savedHospital.setLongitude(hospital.getLongitude());
            savedHospital.setLatitude(hospital.getLatitude());
            savedHospital.setEndereco(hospital.getEndereco());

            this.repository.save(savedHospital);
        } else {
            throw new HospitalNotFoundException(id);
        }
    }

    public void remove(Long id) {
        repository.deleteById(id);
    }

    public Set<Estoque> getEstoqueBy(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new HospitalNotFoundException(id))
                .getEstoques();
    }

    public Produto getProdutoFromEstoque(Long idHospital, Long idProduto) {
        repository.findById(idHospital).orElseThrow(() -> new HospitalNotFoundException(idHospital)).getEstoques().stream();
        return null;
    }

}
