package gestao.tipo;

/**
 * Identifica o Fator RH
 * <li>{@link #POSITIVO}</li>
 * <li>{@link #NEGATIVO}</li>
 */
public enum FatorRH {
    /**
     * Fator RH Positivo
     */
    POSITIVO("POSITIVO"),
    /**
     * Fator RH Negativo
     */
    NEGATIVO("NEGATIVO");
    public String value;
    FatorRH(String value){
        this.value = value;
    }

}
