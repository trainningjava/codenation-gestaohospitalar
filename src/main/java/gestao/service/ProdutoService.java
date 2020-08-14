package gestao.service;

import gestao.exception.ProdutoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gestao.repository.ProdutoRepository;

import gestao.model.Produto;

import java.util.List;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repository;
	
	public Produto save(Produto produto) {
		return repository.save(produto);
	}	
	
	public Produto findBy(Long id) {
		return repository.findById(id).orElseThrow(() -> new ProdutoNotFoundException(id)); 
	}

	public void remove(Long id) {
		repository.deleteById(id);
	}

	public List<Produto> listAll() {
		return repository.findAll();
	}

	public List<Produto>  pesquisar(String nome) {
		return repository.pesquisarProdutos(nome);
	}

}
