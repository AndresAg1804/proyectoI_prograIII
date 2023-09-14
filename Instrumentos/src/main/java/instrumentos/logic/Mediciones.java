package instrumentos.logic;

import java.util.Objects;

public class Mediciones {
    public int getMedida() {
        return Medida;
    }

    public void setMedida(int medida) {
        Medida = medida;
    }

    int Medida;

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String referencia) {
        Referencia = referencia;
    }

    String Referencia;

    public String getLectura() {
        return Lectura;
    }

    public void setLectura(String lectura) {
        Lectura = lectura;
    }

    String Lectura;

    public Mediciones() {
        this(0, "", "");
    }

    public Mediciones(int me, String re, String lec){
        this.Medida = me;
        this.Referencia = re;
        this.Lectura = lec;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mediciones that = (Mediciones) o;
        return Medida == that.Medida && Objects.equals(Referencia, that.Referencia) && Objects.equals(Lectura, that.Lectura);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Medida, Referencia, Lectura);
    }
}
