package gestao.api;

import gestao.model.Produto;
import gestao.service.ProdutoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/produtos")
@Api(value = "Produto")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    /**
     * Salva o produto cadastrado
     * @param produto Salva todos os parametros da classe produto
     * @return Retorna o Id do produto
     * @throws URISyntaxException Se não foi possível salvar o produto
     */
    @ResponseBody
    @PostMapping
    //TODO adicionar anotação @valid em produto
    public ResponseEntity<?> save(@RequestBody Produto produto) throws URISyntaxException {
    	Produto saved = service.save(produto);
        return created(new URI(saved.getId().toString())).build();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Produto> findById(@PathVariable("id") Long id) {
        return ok(service.findBy(id));
    }

    /**
     * Lista todos os produtos
     * @return Lista dos produtos
     */
    @ResponseBody
    @GetMapping
    public ResponseEntity<List<Produto>> listAll() {
        return ok(service.listAll());
    }

    /**
     * Busca parcial pelo nome do produto
     * @param nome Nome parcial do produto
     * @return Lista dos produtos que foram encontrados
     */
    @GetMapping("/por-nome")
    public ResponseEntity<List<Produto>> pesquiar(@PathVariable("nome") String nome) {
        return ok(service.pesquisar(nome));
    }

    /**
     * Exclui o produto
     * @param id identificação do produto
     * @return Salvo com sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Produto> delete(@PathVariable("id") Long id) {
        service.remove(id);
        return ok().build();
    }

}
