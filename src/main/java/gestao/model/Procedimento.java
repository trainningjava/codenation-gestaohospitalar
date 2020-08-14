package gestao.model;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "procedimentos")
public class Procedimento {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    private String nome;
    private String descricao;

    @OneToMany(mappedBy = "procedimento", cascade = ALL, orphanRemoval = true)
    private List<Tratamento> tratamento;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Tratamento> getTratamento() {
        return tratamento;
    }

    public void setTratamento(List<Tratamento> tratamento) {
        this.tratamento = tratamento;
    }
}
