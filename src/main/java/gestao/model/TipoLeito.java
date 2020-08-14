package gestao.model;

public enum TipoLeito {

    CLINICO("Clínico"),
    CIRURGICO("Cirúrgico"),
    OBSTETRICO("Obstétrico"),
    PEDIATRICO("Pediátrico");

    String value;

    TipoLeito(String value) {
        this.value = value;
    }
}
