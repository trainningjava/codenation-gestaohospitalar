package gestao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "pacientes")
@JsonIgnoreProperties(value = {"internacao"}, allowGetters = true)
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "{paciente.nome.not.blank}")
    @Column(name = "nome_completo")
    private String nomeCompleto;

    @NotNull(message = "{paciente.data.nascimento.not.blank}")
    @Past(message = "{paciente.data.nascimento.past}")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @NotNull(message = "{paciente.sexo.not.blank}")
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @NotBlank(message = "{paciente.CPF.not.blank}")
    @CPF(message = "{paciente.cpf.invalido}")
    private String cpf;

    private String endereco;

    @NotNull(message = "{paciente.latitude.not.blank}")
    @Range(min = -90, max = 90, message = "{paciente.latitude.invalid}")
    private BigDecimal latitude;

    @NotNull(message = "{paciente.longitude.not.blank}")
    @Range(min = -180, max = 180, message = "{paciente.longitude.invalid}")
    private BigDecimal longitude;

    @OneToMany(mappedBy = "paciente", cascade = ALL, orphanRemoval = true)
    private List<Internacao> internacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Internacao> getInternacao() {
        return internacao;
    }

    public void setInternacao(List<Internacao> internacao) {
        this.internacao = internacao;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf != null) {
            this.cpf = cpf.replaceAll("\\D", "");
        }
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    
    public void getInfo() {
    	System.out.println("Nome: " + this.nomeCompleto);
    	System.out.println("CPF: " + this.cpf);
    	System.out.println("Data de Nascimento: " + this.dataNascimento);
    	System.out.println("Sexo: " + sexo);
    }
}
