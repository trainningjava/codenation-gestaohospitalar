package gestao.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gestao.exception.PacienteNotFoundException;
import gestao.model.Paciente;
import gestao.model.Sexo;
import gestao.service.HospitalService;
import gestao.service.LeitoService;
import gestao.service.PacienteService;
import gestao.service.TratamentoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Stubber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService service;

    @MockBean
    private TratamentoService tratamentoService;
    
    @MockBean
    private LeitoService leitoService;
    
    @MockBean
    private HospitalService hospitalService;

    @Autowired
    private ObjectMapper objectMapper;

    private Validator validator;
    private Map<Long, Paciente> fakeRepository = new HashMap<>();
    private Long idCount = 0L;

    private Stubber getDoAnswerToSave() {
        return Mockito.doAnswer(invocation -> {
            Paciente paciente = (Paciente) invocation.getArguments()[0];
            Set<ConstraintViolation<Paciente>> violations = validator.validate(paciente);
            if (violations.isEmpty()) {
                paciente.setId(++idCount);
                fakeRepository.put(paciente.getId(), paciente);
                return paciente;
            }
            return null;
        });
    }

    private Stubber getDoAnswerToUpdate() {
        return Mockito.doAnswer(invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            Paciente paciente = (Paciente) invocation.getArguments()[1];
            if (this.fakeRepository.containsKey(id)) {
                paciente.setId(id);
                this.fakeRepository.put(id, paciente);
            } else {
                throw new PacienteNotFoundException(id);
            }
            return null;
        });
    }

    private Stubber getDoAnswerToFindById() {
        return Mockito.doAnswer(invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            if (this.fakeRepository.containsKey(id)) {
                Paciente paciente = this.fakeRepository.get(id);
                return paciente;
            } else {
                throw new PacienteNotFoundException(id);
            }
        });
    }

    private Stubber getDoAnswerToList() {
        return Mockito.doAnswer(invocation -> fakeRepository.values().stream().collect(Collectors.toList()));
    }

    @Before
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.getDoAnswerToSave().when(this.service).save(Mockito.any(Paciente.class));
        this.getDoAnswerToList().when(this.service).listAll();
        this.getDoAnswerToUpdate().when(this.service).update(Mockito.anyLong(), Mockito.any(Paciente.class));
        this.getDoAnswerToFindById().when(this.service).findById(Mockito.anyLong());
    }

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    public void shouldSavePaciente() throws Exception {
        Paciente paciente = this.buildValidPaciente();
        this.mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(paciente)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotSaveInvalidPaciente() throws Exception {
        Paciente paciente = this.buildValidPaciente();
        paciente.setCpf("000.000.000.00");
        this.mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(paciente)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnAllPaciente() throws Exception {
        this.mockMvc.perform(get("/pacientes"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldTrowsPacienteNotFoundExceptionWhenUpdateAndNotExists() throws Exception {
        Paciente paciente = this.buildValidPaciente();
        this.mockMvc.perform(put("/pacientes/30000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(paciente)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotReturnPacienteWithInnexistentId() throws Exception {
        this.mockMvc.perform(get("/pacientes/30000"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    private Paciente buildValidPaciente() {
        Paciente paciente = new Paciente();
        paciente.setNomeCompleto("Maria da Silva");
        paciente.setCpf("371.310.164-70");
        paciente.setSexo(Sexo.F);
        paciente.setDataNascimento(LocalDate.of(1992, 10, 20));
        paciente.setEndereco("R. Gen. Lima e Silva, 606 - Cidade Baixa - Porto Alegre - RS, 90050-102");
        paciente.setLatitude(new BigDecimal("-30.038260"));
        paciente.setLongitude(new BigDecimal(-51.221581));
        return paciente;
    }

}
