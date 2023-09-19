package instrumentos.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlID;

import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Mediciones {

    @XmlID
    String Medida;

    String Referencia;

    String Lectura;


    public String getMedida() {
        return Medida;
    }

    public void setMedida(String medida) {
        Medida = medida;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String referencia) {
        Referencia = referencia;
    }

    public String getLectura() {
        return Lectura;
    }

    public void setLectura(String lectura) {
        Lectura = lectura;
    }

    public Mediciones() {
        this("", "", "");
    }

    public Mediciones(String me, String re, String lec){
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
