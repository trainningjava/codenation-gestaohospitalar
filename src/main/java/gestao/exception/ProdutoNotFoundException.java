package gestao.exception;

public class ProdutoNotFoundException extends RuntimeException {

	/**
	 * Mensagem padronizada, quando não encontra o produto
	 * @param id A identificação do produto
	 */
	public ProdutoNotFoundException(Long id) {
		super(String.format("Não foi encotrado nenhum produto com este id: %d", id));
	}

}
