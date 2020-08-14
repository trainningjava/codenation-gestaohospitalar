package gestao.service;

import gestao.exception.HospitalNotFoundException;
import gestao.model.Hospital;
import gestao.repository.HospitalRepository;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
public class HospitalServiceTest {

    private Validator validator;
    private Map<Long, Hospital> fakeRepository = new HashMap<>();
    private Long idCount = 0L;

    @Autowired
    private HospitalService service;

    @MockBean
    private HospitalRepository repository;

    @TestConfiguration
    static class HospitalServiceTestContextConfiguration {

        @Bean
        public HospitalService hospitalService() {
            return new HospitalService();
        }
    }

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.getDoAnswerToSave().when(this.repository).save(Mockito.any(Hospital.class));
        this.getDoAnswerToList().when(this.repository).findAll();
        this.getDoAnswerToFindById().when(this.repository).findById(Mockito.anyLong());
    }

    private Stubber getDoAnswerToSave() {
        return Mockito.doAnswer(invocation -> {
            Hospital hospital = (Hospital) invocation.getArguments()[0];
            Set<ConstraintViolation<Hospital>> violations = validator.validate(hospital);
            if (violations.isEmpty()) {
                if (hospital.getId() == null) {
                    hospital.setId(++idCount);
                }
                fakeRepository.put(hospital.getId(), hospital);
                return hospital;
            }
            return null;
        });
    }

    private Stubber getDoAnswerToFindById() {
        return Mockito.doAnswer((Answer) invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            if (this.fakeRepository.containsKey(id)) {
                Hospital hospital = this.fakeRepository.get(id);
                return Optional.of(hospital);
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
    public void shouldRetunEmpityListWhenDontHaveHospitais() {
        List<Hospital> hospitals = this.service.listAll();
        assertThat(hospitals.isEmpty(), is(true));
    }

    @Test
    public void shouldRetunListWithSizeEqualsToCountOfHospitais() {
        Hospital hospt1 = this.buildValidHospital();
        Hospital hospt2 = this.buildValidHospital();
        hospt1.setNome("Santa Casa");
        hospt2.setNome("PUC");

        this.service.save(hospt1);
        this.service.save(hospt2);
        List<Hospital> hospitals = this.service.listAll();
        assertThat(hospitals.size(), is(2));
    }

    @Test
    public void shouldSaveHospital() {
        Hospital hospital = this.buildValidHospital();
        Hospital saved = this.service.save(hospital);
        assertThat(saved.getId(), is(notNullValue()));
    }

    @Test
    public void shouldNotSaveHospitalWithoutName() {
        Hospital hospital = this.buildValidHospital();
        hospital.setNome(null);
        Hospital saved = this.service.save(hospital);
        assertThat(saved, is(nullValue()));
    }

    @Test
    public void shouldNotSaveHospitalWithoutEndereco() {
        Hospital hospital = this.buildValidHospital();
        hospital.setEndereco(null);
        Hospital saved = this.service.save(hospital);
        assertThat(saved, is(nullValue()));
    }

    @Test
    public void shouldNotSaveHospitalWithInvalidLatitude() {
        Hospital hospital = this.buildValidHospital();
        hospital.setLatitude(new BigDecimal("90.1"));
        Hospital saved = this.service.save(hospital);
        assertThat(saved, is(nullValue()));
    }

    @Test
    public void shouldNotSaveHospitalWithInvalidLongitude() {
        Hospital hospital = this.buildValidHospital();
        hospital.setLongitude(new BigDecimal("-190"));
        Hospital saved = this.service.save(hospital);
        assertThat(saved, is(nullValue()));
    }

    @Test
    public void shouldReturnAllHospital() {
        List<Hospital> hospitals = service.listAll();
        assertThat(hospitals, hasSize(0));
    }

    @Test
    public void shouldUpdateHospitalExistente() {
        Hospital hospital = this.buildValidHospital();
        hospital = this.service.save(hospital);
        hospital.setNome("Novo Nome");
        this.service.update(hospital.getId(), hospital);
        String nomeAtualizado = this.service.findBy(hospital.getId()).getNome();
        assertThat(nomeAtualizado, is("Novo Nome"));
    }

    @Test(expected = HospitalNotFoundException.class)
    public void shouldReturnHospitalNotFoundExceptionWhenNotExists() {
        Hospital hospital = this.buildValidHospital();
        this.service.update(900L, hospital);
    }

    @Test(expected = HospitalNotFoundException.class)
    public void shouldThrowsHospitalNotFoundExceptionWhenNotExists() {
        Hospital hospital = this.service.findBy(999L);
    }

    @Test
    public void shouldReturnHospitalWhenExists() {
        Hospital hospital = this.buildValidHospital();
        hospital = this.service.save(hospital);
        assertThat(this.service.findBy(hospital.getId()).getId(), is(hospital.getId()));
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
