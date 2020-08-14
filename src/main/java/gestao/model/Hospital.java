package gestao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "hospitais")
@JsonIgnoreProperties(value = {"estoques"}, allowGetters = true)
public class Hospital {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @NotBlank(message = "{hospital.nome.not.blank}")
    private String nome;

    @NotBlank(message = "{hospital.endereco.not.blank}")
    private String endereco;

    @NotNull(message = "{hospital.latitude.not.blank}")
    @Range(min = -90, max = 90, message = "{hospital.latitude.invalid}")
    private BigDecimal latitude;

    @NotNull(message = "{hospital.longitude.not.blank}")
    @Range(min = -180, max = 180, message = "{hospital.longitude.invalid}")
    private BigDecimal longitude;

    @OneToMany(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "hospital_id", referencedColumnName = "id")
    private Set<Estoque> estoques;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String name) {
        this.nome = name;
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

    public Set<Estoque> getEstoques() {
        return estoques;
    }

    public void setEstoques(Set<Estoque> estoques) {
        this.estoques = estoques;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Hospital hospital = (Hospital) o;
        return Objects.equals(getId(), hospital.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
