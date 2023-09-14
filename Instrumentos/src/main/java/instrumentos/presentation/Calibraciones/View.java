package instrumentos.presentation.Calibraciones;

import instrumentos.Application;
import instrumentos.logic.Calibraciones;
import instrumentos.logic.Instrumento;

import javax.swing.*;
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
    private JPanel Mediciones;
    private JLabel instruField;

    public View() {
        delete.setEnabled(false);
        Mediciones.setVisible(false);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                instruField.setForeground(Color.RED);
                instruField.setText(controller.shown());
                clearTextFields();  //agregado por los loles

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaActual = new Date();
                String fechaActualFormateada = dateFormat.format(fechaActual);

                // Establecer la fecha actual formateada como valor por defecto en el TextField 'fecha'
                fecha.setText(fechaActualFormateada);

                Mediciones.setVisible(false);  //agregado por los loles
            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Calibraciones filter = new Calibraciones();
                    filter.setNumero(Integer.parseInt(searchNumero.getText()));
                    controller.search(model.getInstrumento(), filter);
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
                    Mediciones.setVisible(true);
                    Mediciones.setEnabled(true);
                    controller.edit(row);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
                numero.setEnabled(false);
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
                //filter.setMediciones(Integer.parseInt(mediciones.getText()));
                //filter.setFecha(fecha.getText());
               // filter.setNumero(Integer.parseInt(numero.getText()));
                try {
                    if(!isValid()){
                        throw new Exception("Campos vacios");
                    }
                    controller.save(filter);
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
                Mediciones.setVisible(false);
            }
        });
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

    @Override
    public void update(Observable updatedModel, Object properties) {
        int changedProps = (int) properties;

        if ((changedProps & Model.LIST) == Model.LIST) {
            int[] cols = {TableModel.NUMERO, TableModel.FECHA, TableModel.MEDICIONES};
            list.setModel(new TableModel(cols, model.getInstrumento().getCalibraciones()));
            list.setRowHeight(30);
            TableColumnModel columnModel = list.getColumnModel();
            columnModel.getColumn(2).setPreferredWidth(200);
        }

        if ((changedProps & Model.LIST2) == Model.LIST2) {
            // Aquí configurarías la segunda tabla (list2) de manera similar a como lo hiciste con la primera tabla (list)
            // Puedes crear un nuevo modelo, configurar columnas, etc., según tus necesidades
            // Por ejemplo:
            int[] cols2 = {TableModelMediciones.MEDIDA, TableModelMediciones.REFERENCIA, TableModelMediciones.LECTURA};
            list2.setModel(new TableModelMediciones(cols2, model.getCurrent().getMedicionesList()));
            list2.setRowHeight(30);
        }

        if ((changedProps & Model.CURRENT) == Model.CURRENT) {
            numero.setText(String.valueOf(model.getCurrent().getNumero()));
            mediciones.setText(String.valueOf(model.getCurrent().getMediciones()));
            fecha.setText(model.getCurrent().getFecha());
        }

        this.panel.revalidate();
    }


    public void clearTextFields(){
        model.setMode(Application.MODE_CREATE);
        numero.setText(null);
        mediciones.setText(null);
        fecha.setText(null);
        numero.setEnabled(true);
        delete.setEnabled(false);
    }
    public boolean isValid(){
        if(numero.getText().isEmpty() || mediciones.getText().isEmpty() || fecha.getText().isEmpty()){
            return false;
        }
        return true;
    }

}

