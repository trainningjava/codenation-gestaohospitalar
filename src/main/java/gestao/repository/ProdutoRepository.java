package gestao.repository;

import gestao.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     *
     * @param nome Pesquisa parcial do nome do Produto
     * @return Lista todos os produtos relacionados
     */
    @Query("from Produto where nome like concat(?1, '%')")
    List<Produto> pesquisarProdutos (String nome);

}
