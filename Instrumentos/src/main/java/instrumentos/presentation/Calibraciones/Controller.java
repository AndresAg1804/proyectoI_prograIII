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
        model.setCurrent(new Calibraciones());
        try {
            model.init(Service.instance().search(new Calibraciones()));
        } catch (Exception e) {}
        this.shown();
        view.setController(this);
        view.setModel(model);
    }

    public void search(Instrumento instru, Calibraciones filter) throws Exception {

        List<Calibraciones> existentes = Service.instance().searchCalibracionesByInstrumento(instru.getSerie());

        if(filter==null){
            instru.setCalibraciones(existentes);
            model.setList(existentes);
            //model.setCurrent(existentes.get(0));
            model.commit();
            return;
        }

        List<Calibraciones> rows = Service.instance().search(filter);

        if (existentes.isEmpty()&&filter!=null) {
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
        //model.setCurrent(rows.get(0));
        model.commit();
        instru.setCalibraciones(existentes);
    }

    public void edit(int row) throws Exception {
        model.setMode(Application.MODE_EDIT);
        Calibraciones e = model.getCurrent().getInstrumento().getCalibraciones().get(row);
       // model.setCurrent(Service.instance().read(e));
        model.setInstrumento(model.getCurrent().getInstrumento());
        e.setInstrumento(model.getCurrent().getInstrumento());
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
        if (model.getCurrent().getInstrumento().getSerie() == "") {
            throw new Exception("No hay instrumento seleccionado");
        }
        if (model.getMode() == 1) {
            //e.setNumero("");
            e.setInstrumento(model.getCurrent().getInstrumento());
            Service.instance().create(e);
            //model.getCurrent().getInstrumento().getCalibraciones().add(e);
            model.getCurrent().getInstrumento().getCalibraciones().add(e);
            model.setList(Service.instance().search(new Calibraciones()));
            model.commit();

        }
        else {
            Service.instance().update(model.getCurrent());
            for(Mediciones med : model.getCurrent().getMedicionesList()){
                Service.instance().update(med);
            }
            model.setList(Service.instance().search(new Calibraciones()));
            model.commit();
        }
    }

    public void del(int row) throws Exception {

        Calibraciones e = model.getCurrent().getInstrumento().getCalibraciones().get(row);
        // Realiza la eliminación en el servicio (void)
        Service.instance().delete(e);


        // Actualiza la vista con la lista modificada
        int[] cols = {TableModel.NUMERO, TableModel.FECHA, TableModel.MEDICIONES};
        view.getList().setModel(new TableModel(cols, model.getCurrent().getInstrumento().getCalibraciones()));


    }

    public String shown() {
        //model.setInstrumento();

        String textoInstrumento;

        List<Calibraciones> calibracionesDelInstrumento = Service.instance().searchCalibracionesByInstrumento(model.getCurrent().getInstrumento().getSerie());

        if (!model.getCurrent().getInstrumento().getSerie().isEmpty()) {
            textoInstrumento = model.getCurrent().getInstrumento().getSerie() + " - " + model.getCurrent().getInstrumento().getDescripcion() + "(" + model.getCurrent().getInstrumento().getMinimo() + "-" + model.getCurrent().getInstrumento().getMaximo() + " Grados Celsius)";
        } else {
            textoInstrumento = "Ningún instrumento seleccionado.";
            calibracionesDelInstrumento = new ArrayList<>(); // Crea una lista vacía
        }

        model.getCurrent().getInstrumento().setCalibraciones(calibracionesDelInstrumento);
        // Actualiza la tabla con la lista de calibraciones (puede estar vacía)
        int[] cols = {TableModel.NUMERO, TableModel.FECHA, TableModel.MEDICIONES};
        view.getList().setModel(new TableModel(cols, calibracionesDelInstrumento));

        return textoInstrumento;
    }

    public void clear() {
        //model.setCurrent(new Calibraciones());
        model.setMode(Application.MODE_CREATE);
        model.commit();
    }

    public void generatePdfReport() throws Exception {
        Document document = new Document();

        try {
            List<Calibraciones> list = Service.instance().search(new Calibraciones());
            System.out.println("Número de elementos en list: " + list.size());

            PdfWriter.getInstance(document, new FileOutputStream("reporteCalibraciones.pdf"));
            document.open();

            // Título del reporte
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Reporte de Calibraciones", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            Image img = Image.getInstance("Instrumentos/src/main/resources/instrumentos/presentation/icons/LogoUNA.svg.png");
            img.setAlignment(Element.ALIGN_CENTER);
            img.scaleToFit(300, 200);
            document.add(img);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Font tableDataFont = FontFactory.getFont(FontFactory.HELVETICA);

            list.forEach(calibracion -> {
                // Encabezados de la tabla de calibración

                Paragraph titleCali = new Paragraph("Calibracion # "+ String.valueOf(calibracion.getNumero()), titleFont);

                try {
                    document.add(titleCali);
                    document.add(new Paragraph(" "));
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }

                PdfPTable table = new PdfPTable(3); // 3 columnas
                table.setWidthPercentage(100);

                PdfPCell cell = new PdfPCell(new Phrase("Numero", tableHeaderFont));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Fecha", tableHeaderFont));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Mediciones", tableHeaderFont));
                table.addCell(cell);

                // Datos de la calibración
                table.addCell(new Phrase(String.valueOf(calibracion.getNumero()), tableDataFont));
                table.addCell(new Phrase(calibracion.getFecha(), tableDataFont));
                table.addCell(new Phrase(String.valueOf(calibracion.getMediciones()), tableDataFont));

                // Encabezados de la tabla de mediciones
                PdfPTable table2 = new PdfPTable(3); // 3 columnas
                table2.setWidthPercentage(100);

                PdfPCell cell2 = new PdfPCell(new Phrase("Medida", tableHeaderFont));
                table2.addCell(cell2);
                cell2 = new PdfPCell(new Phrase("Referencia", tableHeaderFont));
                table2.addCell(cell2);
                cell2 = new PdfPCell(new Phrase("Lectura", tableHeaderFont));
                table2.addCell(cell2);

                // Datos de las mediciones
                for (Mediciones medicion : calibracion.getMedicionesList()) {
                    table2.addCell(new Phrase(String.valueOf(medicion.getMedida()), tableDataFont));
                    table2.addCell(new Phrase(String.valueOf(medicion.getReferencia()), tableDataFont));
                    table2.addCell(new Phrase(String.valueOf(medicion.getLectura()), tableDataFont));
                }

                // Agrega la tabla de calibración al documento
                try {
                    document.add(table);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                Paragraph titleMedi = new Paragraph("Mediciones de la calibracion # "+ String.valueOf(calibracion.getNumero()), titleFont);

                try {
                    document.add(titleMedi);
                    document.add(new Paragraph(" "));
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
                // Agrega la tabla de mediciones al documento
                try {
                    document.add(table2);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                try {
                    document.add(new Paragraph(" "));
                    document.add(new Paragraph(" "));
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
            });

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Creadores: Anner Andrés Angulo Gutiérrez y Marcos Emilio Vásquez Díaz", tableDataFont));

            document.close();
        } catch (Exception e) {
            throw new Exception("Error al generar el reporte");
        }
    }


    public void setInstrumento(Instrumento e) {
        model.setInstrumento(e);
        model.getCurrent().setInstrumento(e);
        model.commit();
    }
}