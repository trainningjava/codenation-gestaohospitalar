package gestao.tipo;

/**
 * Identifica o tipo do sangue
 * <li>{@link #A}</li>
 * <li>{@link #B}</li>
 * <li>{@link #O}</li>
 * <li>{@link #AB}</li>
 */
public enum Sangue {
    /**
     * Sangue tipo A
     */
    A("A"),
    /**
     * Sangue tipo B
     */
    B("B"),
    /**
     * Sangue tipo O
     */
    O("C"),
    /**
     * Sangue tipo AB
     */
    AB("AB");

    public String value;
    Sangue(String value){
        this.value = value;
    }
}
