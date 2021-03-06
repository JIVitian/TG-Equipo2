/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.seminarios.controladores;

import gui.interfaces.IControladorAMSeminario;
import static gui.interfaces.IGestorSeminarios.EXITO;
import gui.seminarios.modelos.AddItemException;
import gui.seminarios.modelos.GestorSeminarios;
import gui.seminarios.modelos.ModeloComboNotas;
import gui.seminarios.modelos.NotaAprobacion;
import gui.seminarios.modelos.Seminario;
import gui.seminarios.vistas.VentanaAMSeminarios;
import gui.seminarios.vistas.VentanaSeminarios;
import gui.trabajos.modelos.Trabajo;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

/**
 *
 * @author Benjamin
 */
public class ControladorAMSeminario implements IControladorAMSeminario {
    private VentanaAMSeminarios ventana;
    private Seminario seminario;
    private Trabajo elTrabajo;
    
    public ControladorAMSeminario(VentanaSeminarios ventanaPadre, Trabajo elTrabajo){
        this.elTrabajo = elTrabajo;
        this.ventana = new VentanaAMSeminarios(this,ventanaPadre,true);
        this.configurarCombo();
        this.ventana.setTitle("Agregar seminario");
        this.ventana.setTxtGuardar("Guardar");
        this.ventana.setCalendarEnable(true);
        this.ventana.setVisible(true);
        this.ventana.setLocationRelativeTo(null);
    }
    
    public ControladorAMSeminario(VentanaSeminarios ventanaPadre, Seminario seminario, Trabajo elTrabajo){
        this.elTrabajo = elTrabajo;
        this.seminario = seminario;
        this.ventana = new VentanaAMSeminarios(this,ventanaPadre,true);
        this.configurarCombo();
        this.ventana.setTitle("Modificar seminario");
        this.ventana.setTxtGuardar("Modificar");
        this.ventana.setCalendarEnable(false);
        this.ventana.setVisible(true);
        this.ventana.setLocationRelativeTo(null);
    }

    @Override
    public void btnGuardarClic(ActionEvent evt) {
        String estado;
        if(this.seminario == null) {                            //Caso de la agregación de seminarios
            if(this.ventana.getObservaciones().trim().isEmpty()){
                try{
                    if(this.ventana.jNota().getSelectedItem().toString().equals("Aprobado"))
                        estado = this.elTrabajo.nuevoSeminario(this.ventana.jFecha(), NotaAprobacion.APROBADO_SO, this.ventana.getObservaciones().trim());
                    else
                        estado = this.elTrabajo.nuevoSeminario(this.ventana.jFecha(), NotaAprobacion.DESAPROBADO, this.ventana.getObservaciones().trim());
                    if(!estado.equals(EXITO)) throw new AddItemException(estado);
                }
                catch(AddItemException e){
                    JOptionPane.showMessageDialog(this.ventana, e.getMessage(), "Error", ERROR_MESSAGE);
                }
            }
            else{
                try{
                    if(this.ventana.jNota().getSelectedItem().toString().equals("Aprobado"))
                        estado = this.elTrabajo.nuevoSeminario(this.ventana.jFecha(), NotaAprobacion.APROBADO_CO, this.ventana.getObservaciones().trim());
                    else
                        estado = this.elTrabajo.nuevoSeminario(this.ventana.jFecha(), NotaAprobacion.DESAPROBADO, this.ventana.getObservaciones().trim());
                    if(!estado.equals(EXITO)) throw new AddItemException(estado);
                }
                catch(AddItemException e){
                    JOptionPane.showMessageDialog(this.ventana, e.getMessage(), "Error", ERROR_MESSAGE);
                }
            }
        }
        else {                                                  //Caso de la modificación de seminarios
            if(this.ventana.getObservaciones().trim().isEmpty()){
                try{
                    System.out.println(this.ventana.getObservaciones());
                    if(this.ventana.jNota().getSelectedItem().toString().equals("Aprobado"))
                        estado = this.elTrabajo.modificarSeminario(this.seminario, NotaAprobacion.APROBADO_SO, this.ventana.getObservaciones());
                    else
                        estado = this.elTrabajo.modificarSeminario(this.seminario, NotaAprobacion.DESAPROBADO, this.ventana.getObservaciones());
                    if(!estado.equals(EXITO)) throw new AddItemException(estado);
                }
                catch(AddItemException e){
                    JOptionPane.showMessageDialog(this.ventana, e.getMessage(), "Error", ERROR_MESSAGE);
                }
            }
            else{
                try{
                    if(this.ventana.jNota().getSelectedItem().toString().equals("Aprobado"))
                        estado = this.elTrabajo.modificarSeminario(this.seminario, NotaAprobacion.APROBADO_CO, this.ventana.getObservaciones());
                    else
                        estado = this.elTrabajo.modificarSeminario(this.seminario, NotaAprobacion.DESAPROBADO, this.ventana.getObservaciones());
                    if(!estado.equals(EXITO)) throw new AddItemException(estado);
                }
                catch(AddItemException e){
                    JOptionPane.showMessageDialog(this.ventana, e.getMessage(), "Error", ERROR_MESSAGE);
                }
            }
        }
        GestorSeminarios gS = GestorSeminarios.instanciar();
        gS.guardarSeminarios();
        this.ventana.dispose();
    }
    
    public void btnModificarSeminarioClic(ActionEvent evt) {
        String estado;
        if(this.ventana.getObservaciones().trim().isEmpty()){
            try{
                System.out.println(this.ventana.getObservaciones());
                if(this.ventana.jNota().getSelectedItem().toString().equals("Aprobado"))
                    estado = this.elTrabajo.modificarSeminario(this.seminario, NotaAprobacion.APROBADO_SO, this.ventana.getObservaciones());
                else
                    estado = this.elTrabajo.modificarSeminario(this.seminario, NotaAprobacion.DESAPROBADO, this.ventana.getObservaciones());
                if(!estado.equals(EXITO)) throw new AddItemException(estado);
            }
            catch(AddItemException e){
                JOptionPane.showMessageDialog(this.ventana, e.getMessage(), "Error", ERROR_MESSAGE);
            }
        }
        else{
            try{
                if(this.ventana.jNota().getSelectedItem().toString().equals("Aprobado"))
                    estado = this.elTrabajo.modificarSeminario(this.seminario, NotaAprobacion.APROBADO_CO, this.ventana.getObservaciones());
                else
                    estado = this.elTrabajo.modificarSeminario(this.seminario, NotaAprobacion.DESAPROBADO, this.ventana.getObservaciones());
                if(!estado.equals(EXITO)) throw new AddItemException(estado);
            }
            catch(AddItemException e){
                JOptionPane.showMessageDialog(this.ventana, e.getMessage(), "Error", ERROR_MESSAGE);
            }
        }
        this.ventana.dispose();
    }

    @Override
    public void btnCancelarClic(ActionEvent evt) {
        this.ventana.dispose();
    }

    @Override
    public void comboNotaCambiarSeleccion(ActionEvent evt) {
        this.configurarCombo();
    }
    
    private void configurarCombo(){
        try{
            JComboBox<String> combo = this.ventana.jNota();
            combo.setModel(new ModeloComboNotas());
        }
        catch(NullPointerException e){
        }
    }
}
