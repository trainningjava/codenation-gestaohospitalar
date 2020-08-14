package gestao.api;

import gestao.model.Procedimento;
import gestao.service.ProcedimentoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/procedimentos")
@Api(value = "Procedimento")
public class ProcedimentoController {

    @Autowired
    private ProcedimentoService service;

    @PostMapping
    // TODO Implementar método para salvar procedimentos
    public ResponseEntity<?> save(@RequestBody Procedimento procedimento){
        return null;
    }
}
