package instrumentos.presentation.Instrumentos;

import instrumentos.logic.Instrumento;
import instrumentos.logic.TipoInstrumento;

import java.util.List;
import java.util.Observer;

public class Model extends java.util.Observable{
    List<Instrumento> list;
    Instrumento current;

    List<TipoInstrumento> listTypes;
    int mode;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

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

    public void init(List<Instrumento> list){
        setList(list);
        setCurrent(new Instrumento());
    }

    public List<Instrumento> getList() {
        return list;
    }

    public List<TipoInstrumento> getListTypes() {
        return listTypes;
    }

    public void setListTypes(List<TipoInstrumento> listTypes) {
        this.listTypes = listTypes;
        changedProps += TYPES;
    }

    public void setList(List<Instrumento> list){
        this.list = list;
        changedProps +=LIST;
    }

    public Instrumento getCurrent() {
        return current;
    }
    public void setCurrent(Instrumento current) {
        changedProps +=CURRENT;
        this.current = current;
    }

    public static int NONE=0;
    public static int LIST=1;
    public static int CURRENT=2;

    public static  int TYPES=4;
}
