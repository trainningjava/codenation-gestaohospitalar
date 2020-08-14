package gestao.service;

import gestao.repository.ProdutoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ProdutoServiceTest {

    @Autowired
    private ProdutoService service;

    @MockBean
    private ProdutoRepository repository;

    @TestConfiguration
    static class ProductServiceTestConfiguration {

        @Bean
        public ProdutoService produtoService() {
            return new ProdutoService();
        }
    }

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
    }
}
