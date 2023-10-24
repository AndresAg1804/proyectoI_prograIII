package instrumentos.presentation.tipos;

import instrumentos.Application;
import instrumentos.logic.TipoInstrumento;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class Model extends java.util.Observable{
    List<TipoInstrumento> list;
    TipoInstrumento current;
    TipoInstrumento filter;
    int mode;
    int props;

    public Model() {
    }

    public void init(){
        props=0;
        filter = new TipoInstrumento();
        List<TipoInstrumento> list = new ArrayList<TipoInstrumento>();
        this.setList(list);
        mode= Application.MODE_CREATE;
    }

    public void setList(List<TipoInstrumento> list){
        this.list = list;
        props+=LIST;
        setCurrent(new TipoInstrumento());
    }

    public TipoInstrumento getFilter() {
        return filter;
    }

    public void setFilter(TipoInstrumento filter) {
        this.filter = filter;
    }

    public List<TipoInstrumento> getList() {
        return list;
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

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public TipoInstrumento getCurrent() {
        return current;
    }

    public void setCurrent(TipoInstrumento current) {
        props+=CURRENT;
        this.current = current;
    }

    public static int LIST=1;
    public static int CURRENT=2;
}
