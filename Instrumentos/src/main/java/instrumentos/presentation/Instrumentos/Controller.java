package instrumentos.presentation.Instrumentos;

import instrumentos.Application;
import instrumentos.logic.Calibraciones;
import instrumentos.logic.Service;
import instrumentos.logic.Instrumento;
import instrumentos.logic.TipoInstrumento;

import java.util.List;

public class Controller{
    View view;
    Model model;

    public Controller(View view, Model model) {
        model.init(Service.instance().search(new Instrumento()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setListTypes(Service.instance().search(new TipoInstrumento()));
        model.commit();
    }

    public void search(Instrumento filter) throws  Exception{
        List<Instrumento> rows = Service.instance().search(filter);
        if (rows.isEmpty()){
            throw new Exception("NINGUN REGISTRO COINCIDE");
        }
        model.setList(rows);
        model.setCurrent(rows.get(0));
        model.commit();
    }

    public void edit(int row) throws Exception{
        Instrumento e = model.getList().get(row);
        Calibraciones.setInstrumento(e);
        model.setCurrent(Service.instance().read(e));
        model.commit();
    }

    public void save(Instrumento e) throws Exception {
        if (model.mode == 1) {
            Service.instance().create(e);
            this.search(new Instrumento());
        }
        if(model.mode==2) {
            Service.instance().update(e);
            this.search(new Instrumento());
        }
    }

    public void del(int row) throws Exception{

        Instrumento e = model.getList().get(row);
        // Realiza la eliminación en el servicio (void)
        Service.instance().delete(e);

        // Verifica si el elemento se ha eliminado correctamente en el modelo local
        if (model.getList().remove(e)) {
            // Actualiza la vista con la lista modificada
           int[] cols = {TableModel.SERIE, TableModel.DESCRIPCION, TableModel.MINIMO, TableModel.MAXIMO, TableModel.TOLERANCIA};
           view.getList().setModel(new TableModel(cols, model.getList()));
        } else {
            throw new Exception("Error al eliminar el elemento...");
        }

    }
    public void clear(){
        model.setCurrent(new Instrumento());
        //Calibraciones.setInstrumento(model.getCurrent());
        model.setMode(Application.MODE_CREATE);
        model.commit();
    }
    public void shown(){
        model.setListTypes(Service.instance().search(new TipoInstrumento()));
        model.commit();
    }

}