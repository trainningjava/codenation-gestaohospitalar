package gestao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "leitos")
public class Leito {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotBlank(message = "{leito.tipo.not.blank}")
    @Enumerated(EnumType.STRING)
    private TipoLeito tipoLeito;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    private boolean isLivre;

    public TipoLeito getTipoLeito() {
        return tipoLeito;
    }

    public void setTipoLeito(TipoLeito tipoLeito) {
        this.tipoLeito = tipoLeito;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public boolean getIsLivre() {
        return this.isLivre;
    }

    public void setIsLivre(boolean isLivre){
        this.isLivre = isLivre;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof Leito){
            if (((Leito) obj).hospital != null && this.hospital != null) {
                if (((Leito) obj).hospital.getId().equals(this.hospital.getId())
                        && ((Leito) obj).tipoLeito.equals(this.tipoLeito)
                        && ((Leito) obj).isLivre == this.isLivre) {
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
}
