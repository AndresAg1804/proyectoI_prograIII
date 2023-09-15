package instrumentos.presentation.Calibraciones;

import instrumentos.Application;
import instrumentos.logic.Instrumento;
import instrumentos.logic.Mediciones;
import instrumentos.logic.Service;
import instrumentos.logic.Calibraciones;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;

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

    public void edit2(int row, Mediciones medi) throws Exception {
        model.setMode(Application.MODE_EDIT);

        // Obtener la lista actual de mediciones
        List<Mediciones> med = model.getCurrent().getMedicionesList();

        // Actualizar la medición en la posición 'row' con la nueva medición 'medi'
        med.set(row, medi);

        // Actualizar la lista de mediciones en el modelo
        model.setListmed(med);
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

    public void clear() {
        model.setCurrent(new Calibraciones());
        model.setMode(Application.MODE_CREATE);
        model.commit();
    }

    public void generatePdfReport() throws Exception {
        Document document = new Document();

        try {
            List<Calibraciones> list = Service.instance().search(model.getInstrumento(),new Calibraciones());
            List<Mediciones> list2 = list.stream().flatMap(calibracion -> calibracion.getMedicionesList().stream()).collect(Collectors.toList());
            PdfWriter.getInstance(document, new FileOutputStream("reporteCalibraciones.pdf"));
            document.open();

            // Título del reporte
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Reporte de Instrumentos", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            Image img = Image.getInstance("Instrumentos/src/main/resources/instrumentos/presentation/icons/LogoUNA.svg.png"); // Reemplaza con la ruta de tu imagen
            img.setAlignment(Element.ALIGN_CENTER);
            img.scaleToFit(300, 200); // Ajusta el tamaño de la imagen según tus necesidades
            document.add(img);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // Tabla de contenido
            PdfPTable table = new PdfPTable(3); // 3 columnas
            table.setWidthPercentage(100);
            PdfPTable table2 = new PdfPTable(3); // 3 columnas
            table.setWidthPercentage(50);

            // Encabezados de la tabla
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            PdfPCell cell = new PdfPCell(new Phrase("Numero", tableHeaderFont));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Fecha", tableHeaderFont));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Mediciones", tableHeaderFont));
            table.addCell(cell);

            PdfPCell cell2 = new PdfPCell(new Phrase("Medida", tableHeaderFont));
            table2.addCell(cell2);
            cell2 = new PdfPCell(new Phrase("Referencia", tableHeaderFont));
            table2.addCell(cell2);
            cell2 = new PdfPCell(new Phrase("Lectura", tableHeaderFont));
            table2.addCell(cell2);

            // Datos de la tabla
            Font tableDataFont = FontFactory.getFont(FontFactory.HELVETICA);
            for (Calibraciones calibracion : list) {
                table.addCell(new Phrase(String.valueOf(calibracion.getNumero()), tableDataFont));
                table.addCell(new Phrase(calibracion.getFecha(), tableDataFont));
                table.addCell(new Phrase(String.valueOf(calibracion.getMediciones()), tableDataFont));
                for(Mediciones medicion : calibracion.getMedicionesList()){
                    table2.addCell(new Phrase(String.valueOf(medicion.getMedida()), tableDataFont));
                    table2.addCell(new Phrase(String.valueOf(medicion.getReferencia()), tableDataFont));
                    table2.addCell(new Phrase(String.valueOf(medicion.getLectura()), tableDataFont));
                }

            }

            document.add(table);
            document.add(table2);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Creadores: Anner Andrés Angulo Gutiérrez y Marcos Emilio Vásquez Díaz", tableDataFont));

            document.close();
        } catch (Exception e) {
            throw new Exception("Error al generar el reporte");
        }
    }

}