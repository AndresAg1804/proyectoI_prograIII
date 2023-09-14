package instrumentos.presentation.Instrumentos;

import instrumentos.Application;
import instrumentos.logic.Calibraciones;
import instrumentos.logic.Instrumento;
import instrumentos.logic.TipoInstrumento;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;


public class View implements Observer {
    private JPanel panel;
    private JTextField searchDescripcion;
    private JButton search;
    private JButton save;

    public JTable getList() {
        return list;
    }

    private JTable list;
    private JButton delete;
    private JLabel searchDescripLbl;
    private JButton report;
    private JTextField serie;
    private JTextField minimo;
    private JTextField descripcion;
    private JLabel serieLbl;
    private JLabel minimoLbl;
    private JLabel descripcionLbl;
    private JButton clear;
    private JLabel toleranciaLbl;
    private JTextField tolerancia;
    private JLabel maximoLbl;
    private JTextField maximo;
    private JComboBox tipo;
    private JLabel tipoLbl;
    private JTable list2;

    public View() {
        delete.setEnabled(false);

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                controller.shown();
            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Instrumento filter = new Instrumento();
                    filter.setDescripcion(searchDescripcion.getText());
                    controller.search(filter);
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
                    controller.edit(row);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
                serie.setEnabled(false);
                delete.setEnabled(true);
            }
        });
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Instrumento filter = new Instrumento();
                filter.setDescripcion(descripcion.getText());
                filter.setSerie(serie.getText());

                try {
                    filter.setMinimo(Integer.parseInt(minimo.getText()));
                    filter.setMaximo(Integer.parseInt(maximo.getText()));
                    filter.setTolerancia(Integer.parseInt(tolerancia.getText()));
                    filter.setTipo((TipoInstrumento) tipo.getSelectedItem());
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
                Calibraciones.setInstrumento(new Instrumento());
                clearTextFields();
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
            int[] cols = {TableModel.SERIE, TableModel.DESCRIPCION, TableModel.MINIMO, TableModel.MAXIMO, TableModel.TOLERANCIA};
            list.setModel(new TableModel(cols, model.getList()));
            list.setRowHeight(30);
            TableColumnModel columnModel = list.getColumnModel();
            columnModel.getColumn(1).setPreferredWidth(200);
        }
        if((changedProps & Model.TYPES)== Model.TYPES){
            tipo.setModel(new DefaultComboBoxModel(model.getListTypes().toArray(new TipoInstrumento[0])));
            if(model.getMode() == Application.MODE_EDIT){
                tipo.setSelectedItem(model.getCurrent().getTipo());
            }
        }
        if ((changedProps & Model.CURRENT) == Model.CURRENT) {
            serie.setText(model.getCurrent().getSerie());
            descripcion.setText(model.getCurrent().getDescripcion());
            try {
                minimo.setText(String.valueOf(model.getCurrent().getMinimo()));
                maximo.setText(String.valueOf(model.getCurrent().getMaximo()));
                tolerancia.setText(String.valueOf(model.getCurrent().getTolerancia()));
                tipo.setSelectedItem(model.getCurrent().getTipo());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        this.panel.revalidate();
    }

    public void clearTextFields(){
        controller.clear();

        serie.setEnabled(true);
        delete.setEnabled(false);
    }
    public boolean isValid(){
        if(serie.getText().isEmpty() || minimo.getText().isEmpty() || descripcion.getText().isEmpty() || tolerancia.getText().isEmpty() || maximo.getText().isEmpty() || tipo.getSelectedItem() == null){
            return false;
        }
        return true;
    }


}

