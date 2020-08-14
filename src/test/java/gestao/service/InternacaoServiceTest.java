package gestao.service;

import gestao.exception.CheckinNotValidException;
import gestao.exception.CheckoutNotValidException;
import gestao.exception.HospitalNotFoundException;
import gestao.model.Hospital;
import gestao.model.Internacao;
import gestao.model.Paciente;
import gestao.model.Sexo;
import gestao.repository.HospitalRepository;
import gestao.repository.InternacaoRepository;
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
public class InternacaoServiceTest {

    private Validator validator;
    private Map<Long, Internacao> fakeRepository = new HashMap<>();
    private Long idCount = 0L;

    @Autowired
    private InternacaoService service;

    @MockBean
    private InternacaoRepository repository;

    @TestConfiguration
    static class InternacaoServiceTestContextConfiguration {

        @Bean
        public InternacaoService internacaoService() {
            return new InternacaoService();
        }
    }

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.getDoAnswerToSave().when(this.repository).save(Mockito.any(Internacao.class));
        this.getDoAnswerToList().when(this.repository).findAll();
        this.getDoAnswerToFindById().when(this.repository).findById(Mockito.anyLong());
        this.getDoAnswerToFindOpenedInternacaoByPaciente().when(this.repository).findOpenedInternacaoByPaciente(Mockito.anyLong());
    }

    private Stubber getDoAnswerToSave() {
        return Mockito.doAnswer(invocation -> {
            Internacao internacao = (Internacao) invocation.getArguments()[0];
            Set<ConstraintViolation<Internacao>> violations = validator.validate(internacao);
            if (violations.isEmpty()) {
                if (internacao.getId() == null) {
                    internacao.setId(++idCount);
                }
                fakeRepository.put(internacao.getId(), internacao);
                return internacao;
            }
            return null;
        });
    }

    private Stubber getDoAnswerToFindById() {
        return Mockito.doAnswer((Answer) invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            if (this.fakeRepository.containsKey(id)) {
                Internacao internacao = this.fakeRepository.get(id);
                return Optional.of(internacao);
            } else {
                return null;
            }
        });
    }

    private Stubber getDoAnswerToFindOpenedInternacaoByPaciente() {
        return Mockito.doAnswer((Answer) invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            return this.fakeRepository.values()
                    .stream()
                    .filter(internacao -> (internacao.getPaciente().getId().equals(id)
                            && internacao.getDataSaida() == null))
                    .findAny()
                    .orElse(null);
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
    public void shouldDoCheckin() {
        Internacao internacao = this.buildValidInternacao();
        internacao = this.service.save(internacao);
        assertThat(internacao.getId(), is(notNullValue()));
    }

    @Test(expected = CheckinNotValidException.class)
    public void shouldDoNotCheckinIfPacienteHasOpenedInternacao() {
        Internacao internacao = this.buildValidInternacao();
        this.service.save(internacao);

        Internacao novaInternacao = this.buildValidInternacao();
        this.service.save(novaInternacao);
    }

    @Test
    public void shouldDoNotCheckinWithFutureDate() {
        Internacao internacao = this.buildValidInternacao();
        internacao.setDataEntrada(LocalDateTime.now().plusDays(1));
        internacao = this.service.save(internacao);
        assertThat(internacao, is(nullValue()));
    }

    @Test
    public void shouldDoCheckout() {
        Internacao internacao = this.buildValidInternacao();
        internacao = this.service.save(internacao);
        internacao = this.service.checkout(internacao.getPaciente().getId(), internacao.getHospital().getId());
        assertThat(internacao.getDataSaida(), is(notNullValue()));
    }

    @Test(expected = CheckoutNotValidException.class)
    public void shouldThrowExceptionWhenCheckoutAndNotExistsCheckin() {
        this.service.checkout(9L, 99L);
    }

    @Test(expected = CheckoutNotValidException.class)
    public void shouldThrowExceptionWhenCheckoutAndIdHospitalDifferent() {
        Internacao internacao = this.buildValidInternacao();
        internacao = this.service.save(internacao);
        this.service.checkout(internacao.getPaciente().getId(), 99L);
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
