package gestao.api;

import gestao.model.*;
import gestao.service.*;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/hospitais")

@Api(value = "Hospital")
public class HospitalController {

    @Autowired
    private HospitalService service;

    @Autowired
    private InternacaoService internacaoService;

    @Autowired
    private TratamentoService tratamentoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private LeitoService leitoService;

    @ResponseBody
    @GetMapping
    public ResponseEntity<List<Hospital>> listAll() {
        return ok(service.listAll());
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Hospital hospital) throws URISyntaxException {
        Hospital saved = service.save(hospital);
        return created(new URI(saved.getId().toString())).build();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Hospital> findById(@PathVariable("id") Long id) {
        return ok(service.findBy(id));
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable("id") Long id) {
        service.remove(id);
        return ok().build();
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody Hospital hospital) {
        service.update(id, hospital);
        return ok().build();
    }

    @GetMapping("/{id}/pacientes")
    public ResponseEntity<List<Paciente>> listAllPatientAdmitted(@PathVariable("id") Long id) {
        return ok(this.pacienteService.listPacientesInternadosSemAlta(id));
    }

    @GetMapping("/{id}/historico-de-admissoes")
    public ResponseEntity<List<Paciente>> listAllPatient(@PathVariable("id") Long id) {
        return ok(this.pacienteService.listPacientesInternados(id));
    }

    @ResponseBody
    @PutMapping("/{id}/pacientes/{paciente}/checkout")
    public ResponseEntity<?> checkout(@PathVariable("id") Long id, @PathVariable("paciente") Long idPaciente) {
        this.internacaoService.checkout(idPaciente, id);
        return ok().build();
    }

    @ResponseBody
    @PostMapping("/internacoes/{id}/tratamentos")
    public ResponseEntity<?> addTratamento(@PathVariable("id") Long id, @RequestBody List<@Valid Tratamento> tratamentos) {
        Internacao internacao = this.internacaoService.findById(id);
        for (Tratamento tratamento : tratamentos) {
            tratamento.setInternacao(internacao);
            tratamento = this.tratamentoService.save(tratamento);
        }
        return ok(tratamentos);
    }

    @ResponseBody
    @GetMapping("/{id}/pacientes/{paciente}/internados/tratamentos")
    public ResponseEntity<List<Tratamento>> listTratamentosDePacientesInternados(@PathVariable("id") Long id, @PathVariable("paciente") Long paciente) {
        Internacao internacao = this.internacaoService.findInternacaoAbertaByPaciente(paciente);
        return ok(this.tratamentoService.listTratamentosPacientesInternados(internacao, paciente, id));
    }

    @GetMapping("/{id}/estoque")
    public ResponseEntity<?> listEstoque(@PathVariable("id") Long id) {
        return ok(service.getEstoqueBy(id));
    }

    @GetMapping("/{id}/estoque/{produto}")
    public ResponseEntity<?> findProdutoFromEstoque(@PathVariable("id") Long id, @PathVariable("produto") Long idProduto) {
        return ok(service.getProdutoFromEstoque(id, idProduto));
    }

    @PostMapping("/{id}/pacientes/{paciente}/checkin")
    public ResponseEntity<?> checkin(@PathVariable("id") Long id, @PathVariable("paciente") Long idPaciente) {
        Paciente paciente = pacienteService.findById(idPaciente);
        Hospital hospital = service.findBy(id);
        Internacao internacao = new Internacao();
        internacao.setHospital(hospital);
        internacao.setPaciente(paciente);
        internacao.setDataEntrada(LocalDateTime.now());
        internacaoService.save(internacao);
        return ok().build();
    }

    /**
     * Return a integer that represents the number of empty beds by hospital
     * @param id
     */
    @ResponseBody
    @GetMapping(value = "/{id}/leitos-livres", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> howManyBedsAreFree(@PathVariable("id") Long id) {
        return ok(this.leitoService.howManyBedsAreFree(id));
    }

    /**
     * Save a instance of Leito in database
     * btw: by default the leito's status is free
     * @param id
     * @param leito
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/{id}/leitos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> save(@PathVariable("id") Long id, @Valid @RequestBody Leito leito) {
        Leito saved = this.leitoService.save(id, leito.getTipoLeito());
        return ok(saved);
    }
}