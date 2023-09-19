package instrumentos.presentation.Calibraciones;

import instrumentos.Application;
import instrumentos.logic.Calibraciones;
import instrumentos.logic.Instrumento;
import instrumentos.logic.Mediciones;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class View implements Observer {
    private JPanel panel;
    private JTextField searchNumero;
    private JButton search;
    private JButton save;

    public JTable getList() {
        return list;
    }

    private JTable list;
    private JButton delete;
    private JLabel searchNumeroLbl;
    private JButton report;
    private JTextField numero;
    private JTextField mediciones;
    private JTextField fecha;
    private JLabel numeroLbl;
    private JLabel medicionesLbl;
    private JLabel fechaLbl;
    private JButton clear;
    private JTable list2;
    private JPanel Medi;
    private JLabel instruField;

    public View() {
        delete.setEnabled(false);
        Medi.setVisible(false);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                //model.setInstrumento(model.getCurrent().getInstrumento());
                instruField.setForeground(Color.RED);
                instruField.setText(controller.shown());
                numero.setEnabled(false);
                clearTextFields();  //agregado por los loles

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaActual = new Date();
                String fechaActualFormateada = dateFormat.format(fechaActual);

                // Establecer la fecha actual formateada como valor por defecto en el TextField 'fecha'
                fecha.setText(fechaActualFormateada);

                Medi.setVisible(false);  //agregado por los loles
            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Calibraciones filter = new Calibraciones();
                    if (!searchNumero.getText().isEmpty()) {
                        // Si el campo no está vacío, llamar a controller.search con el instrumento y el filtro
                        filter.setNumero(Integer.parseInt(searchNumero.getText()));
                        controller.search(model.getInstrumento(), filter);
                    } else {
                        // Si el campo está vacío, llamar a controller.search solo con el instrumento
                        controller.search(model.getInstrumento(), null);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = list.getSelectedRow();
                model.setMode(Application.MODE_EDIT);
                try {
                    fecha.setEnabled(false);
                    mediciones.setEnabled(false);
                    Medi.setVisible(true);
                    Medi.setEnabled(true);
                    controller.edit(row);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }

                delete.setEnabled(true);
            }
        });

        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaActual = new Date();
                String fechaActualFormateada = dateFormat.format(fechaActual);
                fecha.setText(fechaActualFormateada);
                Calibraciones filter = new Calibraciones(model.getInstrumento(),fecha.getText(),Integer.parseInt(mediciones.getText()));

                try {
                    if(!isValid()){
                        throw new Exception("Campos vacios");
                    }
                    controller.save(filter);
                    clearTextFields();
                    Medi.setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
                clearTextFields();
            }
        });
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = list.getSelectedRow();
                try {
                    controller.del(row);
                    Medi.setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
                clearTextFields();
            }
        });

        clear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearTextFields();
                Medi.setVisible(false);
                fecha.setEnabled(true);
                mediciones.setEnabled(true);
            }
        });

        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.generatePdfReport();
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    public void changeList2(int row, int column) {
        if (column == 2 && list2.isCellEditable(row, column)) {
            // Asegúrate de que la celda esté en modo de edición
            list2.setValueAt(list2.getValueAt(row,column),row,column);


            // Luego, obtén el editor de celdas y solicita el foco para empezar a editar
            Component editor = list2.getEditorComponent();
            if (editor != null) {
                editor.requestFocus();

                Mediciones me = model.getCurrent().getMedicionesList().get(row);
                Object cellValue = list2.getValueAt(row, column);

                if (cellValue != null) {
                    me.setLectura(cellValue.toString());
                    try {
                        controller.edit2(row, me);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }


    public JPanel getPanel() {
        return panel;
    }

    Controller controller;
    Model model;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.setMode(Application.MODE_CREATE);
        model.addObserver(this);
    }

    public boolean isCellEdi(int row, int column) {
        // Obtener el modelo de datos de la JTable
        TableModelMediciones table = (TableModelMediciones) list2.getModel();

        // Verificar si la celda en la fila 'row' y columna 'column' es editable
        return table.isCellEditable(row, column);
    }

    @Override
    public void update(Observable updatedModel, Object properties) {
        int changedProps = (int) properties;

        if ((changedProps & Model.LIST) == Model.LIST) {
            int[] cols = {TableModel.NUMERO, TableModel.FECHA, TableModel.MEDICIONES};
            list.setModel(new TableModel(cols, model.getCurrent().getInstrumento().getCalibraciones()));
            list.setRowHeight(30);
            TableColumnModel columnModel = list.getColumnModel();
            columnModel.getColumn(2).setPreferredWidth(200);
        }

        if ((changedProps & Model.LIST2) == Model.LIST2) {
            // Aquí configurarías la segunda tabla (list2) de manera similar a como lo hiciste con la primera tabla (list)
            // Puedes crear un nuevo modelo, configurar columnas, etc., según tus necesidades
            // Por ejemplo:
            int[] cols = {TableModelMediciones.MEDIDA, TableModelMediciones.REFERENCIA, TableModelMediciones.LECTURA};
            list2.setModel(new TableModelMediciones(cols, model.getCurrent().getMedicionesList()));
            list2.setRowHeight(30);
            TableColumnModel columnModel = list2.getColumnModel();
        }

        if ((changedProps & Model.CURRENT) == Model.CURRENT) {
            numero.setText(String.valueOf(model.getCurrent().getNumero()));
            mediciones.setText(String.valueOf(model.getCurrent().getMediciones()));
            fecha.setText(model.getCurrent().getFecha());
        }

        this.panel.revalidate();
    }


    public void clearTextFields(){
        controller.clear();
        delete.setEnabled(false);
    }
    public boolean isValid(){
        if(numero.getText().isEmpty() || mediciones.getText().isEmpty() || fecha.getText().isEmpty()){
            return false;
        }
        return true;
    }

}

