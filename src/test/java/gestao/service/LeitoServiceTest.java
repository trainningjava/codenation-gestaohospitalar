package gestao.service;

import gestao.exception.HospitalNotFoundException;
import gestao.model.Hospital;
import gestao.model.Leito;
import gestao.model.TipoLeito;
import gestao.repository.HospitalRepository;
import gestao.repository.LeitoRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
public class LeitoServiceTest {

    @MockBean
    private LeitoRepository leitoRepository;

    @MockBean
    private HospitalRepository hospitalRepository;

    @Autowired
    private LeitoService service;

    @TestConfiguration
    static class LeitoServiceTestConfiguration {
        @Bean
        public LeitoService leitoService() {
            return new LeitoService();
        }
    }

    @Before
    public void init() {
        Mockito.when(leitoRepository.findAllByIsLivreAndHospital(this.getHospital()))
                .thenReturn(
                        Optional.of(
                                this.listToBeTested(this.getHospital())
                                        .get()
                                        .stream()
                                        .filter(Leito::getIsLivre)
                                        .collect(Collectors.toList())));

        Mockito.when(leitoRepository.save(this.getLeito()))
                .thenReturn(this.getLeito());

        Mockito.when(hospitalRepository.findById(this.getHospital().getId()))
                .thenReturn(
                        Optional.of(
                                this.getHospital()));

    }

    @Test
    public void shouldReturnTwoBeds() {
        //Arrange
        Hospital hospital = this.getHospital();

        //Act
        int assertValue = service.howManyBedsAreFree(hospital.getId());

        //Assert
        assertEquals(assertValue, 2);
    }

    @Test(expected = HospitalNotFoundException.class)
    public void shouldReturnExceptionIfHospitalDoesntExists() {
        //Arrange
        Hospital hospital = new Hospital();
        hospital.setId(2L);

        //Act
        service.howManyBedsAreFree(hospital.getId());

        //Assert
        //excepted param
    }

    @Test
    public void shouldReturnSavedBed() {
        //Arrange
        Hospital hospital = new Hospital();
        hospital.setId(1L);

        //Act
        Leito saved = service.save(hospital.getId(), TipoLeito.CIRURGICO);

        //Assert
        assertEquals(saved, this.getLeito());
    }

    @Test(expected = HospitalNotFoundException.class)
    public void shouldReturnExceptionWhenTryToSaveWithInvalidHospital() {
        Hospital hospital = new Hospital();
        hospital.setId(2L);

        //Act
        service.save(hospital.getId(), TipoLeito.CIRURGICO);

        //Assert
        //expect exception param
    }

    @NotNull
    private Optional<List<Leito>> listToBeTested(Hospital hospital) {
        ArrayList<Leito> arrayList = new ArrayList<>();
        Leito leito_1 = new Leito();
        leito_1.setHospital(hospital);
        leito_1.setIsLivre(true);

        Leito leito_2 = new Leito();
        leito_2.setHospital(hospital);
        leito_2.setIsLivre(false);

        Leito leito_3 = new Leito();
        leito_3.setHospital(hospital);
        leito_3.setIsLivre(true);

        arrayList.add(leito_1);
        arrayList.add(leito_2);
        arrayList.add(leito_3);

        return Optional.of(arrayList);

    }

    private Hospital getHospital() {
        Hospital hospital = new Hospital();
        hospital.setId(1L);
        return hospital;
    }

    private Leito getLeito() {
        Leito leito = new Leito();
        leito.setTipoLeito(TipoLeito.CIRURGICO);
        leito.setHospital(this.getHospital());
        leito.setIsLivre(true);
        return leito;
    }


}
