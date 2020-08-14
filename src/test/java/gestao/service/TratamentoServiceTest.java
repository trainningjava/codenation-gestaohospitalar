package gestao.service;

import gestao.exception.HospitalNotFoundException;
import gestao.model.*;
import gestao.repository.HospitalRepository;
import gestao.repository.TratamentoRepository;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
public class TratamentoServiceTest {

    private Validator validator;
    private Map<Long, Tratamento> fakeRepository = new HashMap<>();
    private Long idCount = 0L;

    @Autowired
    private TratamentoService service;

    @MockBean
    private TratamentoRepository repository;

    @TestConfiguration
    static class TratamentoServiceTestContextConfiguration {

        @Bean
        public TratamentoService tratamentoService() {
            return new TratamentoService();
        }
    }

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.getDoAnswerToSave().when(this.repository).save(Mockito.any(Tratamento.class));
        this.getDoAnswerToFindById().when(this.repository).findById(Mockito.anyLong());
    }

    private Stubber getDoAnswerToSave() {
        return Mockito.doAnswer(invocation -> {
            Tratamento tratamento = (Tratamento) invocation.getArguments()[0];
            Set<ConstraintViolation<Tratamento>> violations = validator.validate(tratamento);
            if (violations.isEmpty()) {
                if (tratamento.getId() == null) {
                    tratamento.setId(++idCount);
                }
                fakeRepository.put(tratamento.getId(), tratamento);
                return tratamento;
            }
            return null;
        });
    }

    private Stubber getDoAnswerToFindById() {
        return Mockito.doAnswer((Answer) invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            if (this.fakeRepository.containsKey(id)) {
                Tratamento tratamento = this.fakeRepository.get(id);
                return Optional.of(tratamento);
            } else {
                return null;
            }
        });
    }

    @Test
    public void shouldSaveTratamento() {
        Tratamento tratamento = this.buildValidTratamento();
        tratamento = this.service.save(tratamento);
        assertThat(tratamento.getId(), is(notNullValue()));
    }

    @Test
    public void contextLoads() {
        Assertions.assertThat(service).isNotNull();
    }

    private Tratamento buildValidTratamento() {
        Tratamento tratamento = new Tratamento();
        tratamento.setInternacao(new Internacao());
        tratamento.setData(LocalDateTime.now());
        return tratamento;
    }

    private Internacao buildValidInternacao() {
        Hospital hospital = this.buildValidHospital();
        Paciente paciente = this.buildValidPaciente();

        hospital.setId(1L);
        paciente.setId(1L);

        Internacao internacao = new Internacao();
        internacao.setHospital(hospital);
        internacao.setPaciente(paciente);
        internacao.setDataEntrada(LocalDateTime.now());
        return internacao;
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

    private Hospital buildValidHospital() {
        Hospital hospital = new Hospital();
        hospital.setNome("Hospital de Teste");
        hospital.setEndereco("Av. Brasil, 902");
        hospital.setLatitude(new BigDecimal("10.058"));
        hospital.setLongitude(new BigDecimal("-52.12"));
        return hospital;
    }
}
