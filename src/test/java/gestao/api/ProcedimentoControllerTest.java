package gestao.api;

import gestao.service.ProcedimentoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebMvcTest(ProcedimentoController.class)
public class ProcedimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcedimentoService service;

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(mockMvc).isNotNull();
    }
}
