package instrumentos.presentation.Calibraciones;

import instrumentos.Application;
import instrumentos.logic.Calibraciones;
import instrumentos.logic.Instrumento;
import instrumentos.logic.Mediciones;
import instrumentos.logic.TipoInstrumento;

import java.util.List;
import java.util.Observer;

public class Model extends java.util.Observable{

    public List<Mediciones> getListmed() {
        return listmed;
    }

    public void setListmed(List<Mediciones> listmediciones) {
        this.listmed = listmediciones;
        changedProps +=LIST2;
    }

    List<Mediciones> listmed;
    List<Calibraciones> list;
    Calibraciones current;

    public Instrumento getInstrumento() {
        return instrumento;
    }

    Instrumento instrumento;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    int mode;
    int changedProps = NONE;

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        commit();
    }

    public void commit(){
        setChanged();
        notifyObservers(changedProps);
        changedProps = NONE;
    }

    public Model() {
    }

    public void init(List<Calibraciones> list){
        setList(list);
        setCurrent(new Calibraciones());
    }

    public void setInstrumento(Instrumento instru) {
        instrumento=instru;
    }
    public List<Calibraciones> getList() {
        return list;
    }
    public void setList(List<Calibraciones> list){
        this.list = list;
        changedProps +=LIST;
    }


    public Calibraciones getCurrent() {
        return current;
    }
    public void setCurrent(Calibraciones current) {
        changedProps +=CURRENT;
        this.current = current;
    }

    public static int NONE=0;
    public static int LIST=1;
    public static int CURRENT=2;
    public static int LIST2 = 4;

}
