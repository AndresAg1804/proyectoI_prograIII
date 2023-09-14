package instrumentos.presentation.Calibraciones;

import instrumentos.Application;
import instrumentos.logic.Instrumento;
import instrumentos.logic.Service;
import instrumentos.logic.Calibraciones;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    View view;
    Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        model.init(Service.instance().search(model.getInstrumento(), new Calibraciones()));
        this.shown();
        view.setController(this);
        view.setModel(model);
    }

    public void search(Instrumento instru, Calibraciones filter) throws Exception {   // hay que pasarle instrumento tambien

        List<Calibraciones> existentes = instru.getCalibraciones();

        List<Calibraciones> rows = Service.instance().search(instru, filter);
        if (existentes.isEmpty()) {
            model.setList(rows);
            instru.setCalibraciones(rows);
            model.setCurrent(rows.get(0));
            model.commit();
            instru.setCalibraciones(existentes);
            throw new Exception("NINGUN REGISTRO COINCIDE");
        }

        if(rows.isEmpty()){
            model.setList(rows);
            instru.setCalibraciones(rows);
            //model.setCurrent(rows.get(0));
            model.commit();
            instru.setCalibraciones(existentes);
        throw new Exception("NO EXISTE CALIBRACIÓN CON ESE NÚMERO");
        }

        model.setList(rows);
        instru.setCalibraciones(rows);
        model.setCurrent(rows.get(0));
        model.commit();
        instru.setCalibraciones(existentes);
    }

    public void edit(int row) throws Exception {     //cambiado
        model.setMode(Application.MODE_EDIT);
        Calibraciones e = model.getInstrumento().getCalibraciones().get(row);
        model.setCurrent(e);
        model.commit();
        model.setListmed(e.getMedicionesList());
        model.commit();
    }

    public void save(Calibraciones e) throws Exception {
        if (model.getInstrumento().getSerie() == "") {
            throw new Exception("No hay instrumento seleccionado");
        }
        if (model.getMode() == 1) {
            Service.instance().create(model.getInstrumento(), e);
            model.setList(Service.instance().search(model.getInstrumento(), new Calibraciones()));
            model.commit();
            //model.getInstrumento().getCalibraciones().add(e);
            //this.search(model.getInstrumento(), new Calibraciones());
        }
        if (model.getMode() == 2) {
            Service.instance().update(model.getInstrumento(), e);
            this.search(model.getInstrumento(), new Calibraciones());
        }
    }

    public void del(int row) throws Exception {

        Calibraciones e = model.getInstrumento().getCalibraciones().get(row);
        // Realiza la eliminación en el servicio (void)
        Service.instance().delete(model.getInstrumento(), e);

        // Verifica si el elemento se ha eliminado correctamente en el modelo local
        //if (model.getInstrumento().getCalibraciones().remove(e)) {
        //updateNumerosSecuenciales();
        // Actualiza la vista con la lista modificada
        int[] cols = {TableModel.NUMERO, TableModel.FECHA, TableModel.MEDICIONES};
        view.getList().setModel(new TableModel(cols, model.getInstrumento().getCalibraciones()));
        //actualizar numero
        //model.getCurrent().disminuirCantidad();
        //} else {
        //  throw new Exception("Error al eliminar el elemento...");
        //}

    }

    private void updateNumerosSecuenciales() {
        List<Calibraciones> calibracionesList = model.getList();
        for (int i = 0; i < calibracionesList.size(); i++) {
            Calibraciones calibracion = calibracionesList.get(i);
            calibracion.setNumero(i + 1); // Actualiza el número secuencial
        }
    }

    public String shown() {
        model.setInstrumento(Calibraciones.getInstrumento());

        String textoInstrumento;
        List<Calibraciones> calibracionesDelInstrumento = model.getInstrumento().getCalibraciones();

        if (!model.getInstrumento().getSerie().isEmpty()) {
            textoInstrumento = model.getInstrumento().getSerie() + " - " + model.getInstrumento().getDescripcion() + "(" + model.getInstrumento().getMinimo() + "-" + model.getInstrumento().getMaximo() + " Grados Celsius)";
        } else {
            textoInstrumento = "Ningún instrumento seleccionado.";
            calibracionesDelInstrumento = new ArrayList<>(); // Crea una lista vacía
        }

        // Actualiza la tabla con la lista de calibraciones (puede estar vacía)
        int[] cols = {TableModel.NUMERO, TableModel.FECHA, TableModel.MEDICIONES};
        view.getList().setModel(new TableModel(cols, calibracionesDelInstrumento));

        return textoInstrumento;
    }

}