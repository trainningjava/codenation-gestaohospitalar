package gestao.tipo;

/**
 * Identifica o tipo do produto
 * <li>{@link #REMEDIO}</li>
 * <li>{@link #SANGUE}</li>
 */
public enum Tipo {

    /**
     * Produto é Remedio
     */
    REMEDIO("REMEDIO"),
    /**
     * Produto é Bolsa de Sangue
     */
    SANGUE("SANGUE");
    public String value;
    Tipo(String value){
        this.value = value;
    }
}
