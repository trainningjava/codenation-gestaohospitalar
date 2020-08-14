package gestao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "internacoes")
@JsonIgnoreProperties(value = {"hospital"}, allowGetters = true)
public class Internacao {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @NotNull(message = "{internacao.paciente.not.null}")
    @ManyToOne
    @MapsId("paciente_id")
    private Paciente paciente;

    @NotNull(message = "{internacao.hospital.not.null}")
    @ManyToOne
    @MapsId("hospital_id")
    private Hospital hospital;

    @NotNull(message = "{internacao.data.entrada.not.null}")
    @PastOrPresent(message = "{internacao.data.entrada.not.future}")
    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada;

    @PastOrPresent(message = "{internacao.data.saida.not.future}")
    @Column(name = "data_saida")
    private LocalDateTime dataSaida;

    @OneToMany(mappedBy = "internacao", cascade = ALL, orphanRemoval = true)
    private List<Tratamento> tratamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public List<Tratamento> getTratamento() {
        return tratamento;
    }

    public void setTratamento(List<Tratamento> tratamento) {
        this.tratamento = tratamento;
    }
}
