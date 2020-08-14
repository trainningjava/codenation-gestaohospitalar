package gestao.api;

import gestao.model.Hospital;
import gestao.model.Paciente;
import gestao.model.Tratamento;
import gestao.service.PacienteService;
import gestao.service.TratamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/pacientes")
@Api(value = "Paciente")
public class PacienteController {

    @Autowired
    private PacienteService service;

    @Autowired
    private TratamentoService tratamentoService;

    @ResponseBody
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Paciente paciente) throws URISyntaxException {
        return ResponseEntity.created(new URI(this.service.save(paciente).getId().toString())).build();
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<List<Paciente>> listAll(){
        return ResponseEntity.ok(this.service.listAll());
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.service.findById(id));
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> update(@PathVariable("id") Long id, @Valid @RequestBody Paciente paciente) {
        this.service.update(id, paciente);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @GetMapping("/{id}/tratamentos")
    public ResponseEntity<List<Tratamento>> listAllTratamentos(@PathVariable("id") Long id) {
        return ok(this.tratamentoService.listTratamentosPacientes(id));
    }

    @ResponseBody
    @GetMapping("/{id}/tratamentos/{dataInicial}/{dataFinal}")
    public ResponseEntity<List<Tratamento>> listAllTratamentosByData(@PathVariable("id") Long id,
            @PathVariable("dataInicial") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dataInicial,
            @PathVariable("dataFinal") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dataFinal) {
        return ok(this.tratamentoService.listTratamentosPacientes(id, dataInicial, dataFinal));
    }

    @ResponseBody
    @GetMapping("/{id}/hospitais-proximos")
    // TODO Listar todos os hospitais próximos do paciente
    public ResponseEntity<Hospital> listAllNearby(@PathVariable("id") Long id) {
        return ok(service.findNearby(id));
    }
}
