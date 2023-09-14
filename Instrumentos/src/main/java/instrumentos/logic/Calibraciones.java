package instrumentos.logic;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

public class Calibraciones {

    public List<Mediciones> getMedicionesList() {
        return medicionesList;
    }

    public void setMedicionesList(List<Mediciones> medicionesList) {
        this.medicionesList = medicionesList;
    }

    List<Mediciones> medicionesList = new ArrayList<>();
    static Instrumento instrumento;     //static???

    String fecha;

    int mediciones;

    int numero;
    public Calibraciones() {
       this(new Instrumento(), "", 0);
    }


    public Calibraciones(Instrumento inst, String fecha, int mediciones) {
        this.instrumento = inst;
        this.fecha = fecha;
        this.mediciones = mediciones;

        // Establece el valor de numero siempre en 0
        this.numero = 0;

        //if (mediciones != 0) {
           // this.numero = 0;
        //}

        if(instrumento!=null && mediciones!=0) {
            int refValor = inst.getMaximo() - inst.getMinimo();
            int refValor2 = refValor / mediciones;
            for (int i = 1; i < mediciones+1; i++) {
                if (i == 1) {
                    Mediciones med = new Mediciones();
                    med.setMedida(1);
                    med.setReferencia("0");
                    med.setLectura("0");
                    medicionesList.add(med);
                }
                else {
                    Mediciones med = new Mediciones();
                    med.setMedida(i);
                    med.setReferencia(String.valueOf(refValor2));
                    med.setLectura("0");
                    medicionesList.add(med);
                    refValor2 += refValor2;
                }
            }
        }



    }

    public static Instrumento getInstrumento() {
        return instrumento;
    }

    public static void setInstrumento(Instrumento instrumen) {
        instrumento = instrumen;
    }

    //public void disminuirCantidad(){
      //  ultiNumero--;
    //}

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int nu) { this.numero = nu; }

    public int getMediciones() {
        return mediciones;
    }

    public void setMediciones(int mediciones) {
        this.mediciones = mediciones;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calibraciones that = (Calibraciones) o;
        return mediciones == that.mediciones && numero == that.numero && Objects.equals(medicionesList, that.medicionesList) && Objects.equals(fecha, that.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicionesList, fecha, mediciones, numero);
    }
}


