package instrumentos.presentation.Instrumentos;

import instrumentos.Application;
import instrumentos.logic.Instrumento;
import instrumentos.logic.TipoInstrumento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observer;

public class Model extends java.util.Observable{
    List<Instrumento> list;
    Instrumento current;
    Instrumento filter;
    int mode;
    int props;
    List<TipoInstrumento> listTypes;

    public Model() {
    }

    public void init(List<TipoInstrumento> listTypes){
        props=0;
        filter = new Instrumento();
        List<Instrumento> rows = new ArrayList<Instrumento>();
        this.setList(rows);
        mode= Application.MODE_CREATE;

        this.setListTypes(listTypes);
    }

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        commit();
    }

    public void commit(){
        setChanged();
        notifyObservers(props);
        props=0;
    }

    public Instrumento getFilter() {
        return filter;
    }

    public void setFilter(Instrumento filter) {
        this.filter = filter;
    }

    public List<Instrumento> getList() {
        return list;
    }

    public void setList(List<Instrumento> list){
        this.list = list;
        props+=LIST;
        setCurrent(new Instrumento());
    }

    public Instrumento getCurrent() {
        return current;
    }

    public void setCurrent(Instrumento current) {
        props+=CURRENT;
        this.current = current;
    }

    public List<TipoInstrumento> getListTypes() {
        return listTypes;
    }

    public void setListTypes(List<TipoInstrumento> listTypes) {
        this.listTypes = listTypes;
        props+=TYPES;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public static int LIST=1;
    public static int CURRENT=2;
    public static int TYPES=4;
}
