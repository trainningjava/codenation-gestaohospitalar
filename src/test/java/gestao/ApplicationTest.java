package gestao;

import gestao.api.HospitalController;
import gestao.api.PacienteController;
import gestao.api.ProcedimentoController;
import gestao.api.ProdutoController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private HospitalController hospitalController;

    @Autowired
    private PacienteController pacienteController;

    @Autowired
    private ProcedimentoController procedimentoController;

    @Autowired
    private ProdutoController produtoController;

    @Test
    public void contextLoads() {
        assertThat(hospitalController).isNotNull();
        assertThat(pacienteController).isNotNull();
        assertThat(procedimentoController).isNotNull();
        assertThat(produtoController).isNotNull();
    }
}
