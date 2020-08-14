package gestao.service;

import gestao.model.Paciente;
import gestao.model.Sexo;
import gestao.repository.PacienteRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
public class PacienteServiceTest {

    @Autowired
    private PacienteService service;

    @MockBean
    private PacienteRepository repository;
    
    @MockBean
    private LeitoService leitoService;
    
    @MockBean
    private HospitalService hospitalService;

    private Validator validator;
    private Map<Long, Paciente> fakeRepository = new HashMap<>();
    private Long idCount = 0L;

    @TestConfiguration
    static class PacienteServiceTestConfiguration {

        @Bean
        public PacienteService pacienteService() {
            return new PacienteService();
        }
    }

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.getDoAnswerToSave().when(this.repository).save(Mockito.any(Paciente.class));
        this.getDoAnswerToList().when(this.repository).findAll();
        this.getDoAnswerToFindById().when(this.repository).findById(Mockito.anyLong());
    }

    private Stubber getDoAnswerToSave() {
        return Mockito.doAnswer(invocation -> {
            Paciente paciente = (Paciente) invocation.getArguments()[0];
            Set<ConstraintViolation<Paciente>> violations = validator.validate(paciente);
            if (violations.isEmpty()) {
                if (paciente.getId() == null) {
                    paciente.setId(++idCount);
                }
                fakeRepository.put(paciente.getId(), paciente);
                return paciente;
            }
            return null;
        });
    }

    private Stubber getDoAnswerToFindById() {
        return Mockito.doAnswer((Answer) invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            if (this.fakeRepository.containsKey(id)) {
                Paciente paciente = this.fakeRepository.get(id);
                return Optional.of(paciente);
            } else {
                return null;
            }
        });
    }

    private Stubber getDoAnswerToList() {
        return Mockito.doAnswer((Answer) invocation -> {
            return fakeRepository.values().stream().collect(Collectors.toList());
        });
    }

    @Test
    public void contextLoads() {
        Assertions.assertThat(service).isNotNull();
    }

    @Test
    public void shouldSaveValidPaciente(){
        Paciente paciente = this.buildValidPaciente();
        this.service.save(paciente);
        assertThat(paciente.getId(), is(notNullValue()));
    }

    @Test
    public void shouldNotSavePacienteWithInvalidCPF(){
        Paciente paciente = this.buildValidPaciente();
        paciente.setCpf("999.999.999-99");
        paciente = this.service.save(paciente);
        assertThat(paciente, is(nullValue()));
    }

    @Test
    public void shouldFindPacienteById(){
        Paciente paciente = this.buildValidPaciente();
        paciente = this.service.save(paciente);
        assertThat(this.service.findById(paciente.getId()), is(notNullValue()));
    }

    @Test
    public void shouldUpdatePaciente(){
        Paciente paciente = this.buildValidPaciente();
        paciente = this.service.save(paciente);
        paciente.setNomeCompleto("Joana dos Santos");
        this.service.update(paciente.getId(), paciente);
        assertThat(this.service.findById(paciente.getId()).getNomeCompleto(), is("Joana dos Santos"));
    }

    @Test
    public void shouldNotSavePacienteWithoutName(){
        Paciente paciente = this.buildValidPaciente();
        paciente.setNomeCompleto("");
        paciente = this.service.save(paciente);
        assertThat(paciente, is(nullValue()));
    }

    @Test
    public void shouldNotSavePacienteWithFutereDataNascimento(){
        Paciente paciente = this.buildValidPaciente();
        paciente.setDataNascimento(LocalDate.of(2020, 02, 20));
        paciente = this.service.save(paciente);
        assertThat(paciente, is(nullValue()));
    }

    @Test
    public void shouldNotSavePacienteWithInvalidLatitudeOrLongitude(){
        Paciente paciente = this.buildValidPaciente();
        paciente.setLatitude(new BigDecimal("120.2"));
        paciente = this.service.save(paciente);
        assertThat(paciente, is(nullValue()));

        paciente = this.buildValidPaciente();
        paciente.setLongitude(new BigDecimal("180.2"));
        paciente = this.service.save(paciente);
        assertThat(paciente, is(nullValue()));
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
