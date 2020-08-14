package gestao.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gestao.model.Produto;
import org.junit.Ignore;
import gestao.service.ProdutoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @Ignore
    public void shouldSaveProduct() throws Exception {
        Produto produto = new Produto("Neosaldina", "120mg");
        doAnswer(invocationOnMock -> {
            Produto produto1 = (Produto) invocationOnMock.getArguments()[0];
            produto1.setId(1L);
            return produto1;
        }).when(service).save(produto);

        mockMvc.perform(post("/produtos")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Ignore
    public void shouldReturnProductById() throws Exception {
        this.mockMvc.perform(get("/produtos/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Ignore
    // TODO Corrigir Teste de retornar exceção HospitalNotFoundException quando for passado um hopistal com id inexistente
    public void shouldNotReturnHospitalWithInnexistentId() throws Exception {
        this.mockMvc.perform(get("/produtos/30000"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
