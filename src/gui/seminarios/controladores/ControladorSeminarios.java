/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.seminarios.controladores;

import gui.interfaces.IControladorAMSeminario;
import gui.interfaces.IControladorSeminarios;
import gui.seminarios.modelos.ModeloTabla;
import gui.seminarios.modelos.Seminario;
import gui.seminarios.vistas.VentanaSeminarios;
import gui.trabajos.modelos.Trabajo;
import gui.trabajos.vistas.VentanaTrabajos;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Benjamin
 */
public class ControladorSeminarios implements IControladorSeminarios {
    private VentanaSeminarios ventana;
    private Trabajo elTrabajo;
    
    public ControladorSeminarios(VentanaTrabajos ventanaPadre, Trabajo elTrabajo){
        try{
            this.elTrabajo = elTrabajo;
            this.ventana = new VentanaSeminarios(this,ventanaPadre,true);
            this.configurarTabla();
            this.ventana.setModificarEnabled(false);
            this.ventana.setTitle("Seminarios");
            this.ventana.setTxtTituloTrabajo("Seminarios del trabajo \"" + this.elTrabajo.verTitulo() + "\"");
            this.ventana.setVisible(true);
            this.ventana.setLocationRelativeTo(null);
        }
        catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "No se encontró el trabajo especificado", "Error de paso de datos", ERROR_MESSAGE);
            this.ventana.dispose();
        }
    }
    
    /**
     * Configura la tabla donde se muestran las areas
     */
    private void configurarTabla(){
       JTable tablaSeminarios = this.ventana.getTable();
       tablaSeminarios.setModel(new ModeloTabla(this.elTrabajo));               //Seteo el modelo creado en una clase
       tablaSeminarios.setCellSelectionEnabled(true);                           //Para poder seleccionar una celda
       tablaSeminarios.getTableHeader().setReorderingAllowed(false);            //Para poder reordenar columnas
       tablaSeminarios.getTableHeader().setResizingAllowed(false);              //Para poder cambiar tamaño
       tablaSeminarios.getColumnModel().getColumn(0).setPreferredWidth(9);      //Para setear el ancho predeterminado de la columna
       tablaSeminarios.getColumnModel().getColumn(1).setPreferredWidth(20);
       tablaSeminarios.getColumnModel().getColumn(2).setPreferredWidth(30);
       tablaSeminarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);   //Modo de selección (con selección simple, se pueden seleccionar muchas columnas a la vez)
    }
    
    @Override
    public void btnNuevoSeminarioClic(ActionEvent evt) {
        IControladorAMSeminario controlador = new ControladorAMSeminario(this.ventana, this.elTrabajo);
    }

    @Override
    public void btnModificarSeminarioClic(ActionEvent evt) {
        int index = this.ventana.getTable().getSelectedRow();                                   //Obtengo la fila en la que estoy
        Seminario seminario = this.elTrabajo.verSeminarios().get(index);                        //Obtengo el seminario de la fila indexada
        IControladorAMSeminario controlador = new ControladorAMSeminario(this.ventana, seminario, this.elTrabajo);
    }

    @Override
    public void btnVolverClic(ActionEvent evt) {
        this.ventana.dispose();
    }

    @Override
    public void ventanaGanaFoco(WindowEvent evt) {
        this.configurarTabla();
        try{
            this.ventana.getTable().setRowSelectionInterval(this.elTrabajo.verSeminarios().size()-1, this.elTrabajo.verSeminarios().size()-1);
        }
        catch(IllegalArgumentException e){
            
        }
    }
}
