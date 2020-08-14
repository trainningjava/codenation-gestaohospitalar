package gestao.model;

public enum Sexo {

    M("Masculino"),
    F("Feminino"),
    O("Outro");

    public String value;
    Sexo(String value){
        this.value = value;
    }
}
