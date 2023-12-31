package instrumentos.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlID;

import java.util.Objects;

public class Mediciones {

    String Medida;

    String Referencia;

    int Lectura;

    Calibraciones calibraciones;

    public Calibraciones getCalibraciones() {
        return calibraciones;
    }

    public void setCalibraciones(Calibraciones calibraciones) {
        this.calibraciones = calibraciones;
    }

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

    public int getLectura() {
        return Lectura;
    }

    public void setLectura(int lectura) {
        Lectura = lectura;
    }

    public Mediciones() {
        this("", "", 0);
    }

    public Mediciones(String me, String re, int lec){
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
