package gestao.service;

import gestao.repository.ProcedimentoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ProcedimentoServiceTest {

    @Autowired
    private ProcedimentoService service;

    @MockBean
    private ProcedimentoRepository repository;

    @TestConfiguration
    static class ProcedimentoServiceTestConfiguration {

        @Bean
        public ProcedimentoService procedimentoService() {
            return new ProcedimentoService();
        }
    }

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
    }
}
