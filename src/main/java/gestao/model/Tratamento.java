package gestao.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "tratamentos")
public class Tratamento {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @NotNull
    @PastOrPresent
    private LocalDateTime data;

    @NotNull
    @ManyToOne
    @MapsId("internacao_id")
    private Internacao internacao;

    @ManyToOne
    @MapsId("produto_id")
    private Produto produto;

    @ManyToOne
    @MapsId("procedimento_id")
    private Procedimento procedimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Internacao getInternacao() {
        return internacao;
    }

    public void setInternacao(Internacao internacao) {
        this.internacao = internacao;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Procedimento getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(Procedimento procedimento) {
        this.procedimento = procedimento;
    }
}
