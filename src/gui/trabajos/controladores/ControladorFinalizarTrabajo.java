/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.controladores;

import com.toedter.calendar.JDateChooser;
import gui.interfaces.IGestorTrabajos;
import gui.trabajos.modelos.AlumnoEnTrabajo;
import gui.trabajos.modelos.GestorTrabajos;
import gui.trabajos.modelos.RolEnTrabajo;
import gui.trabajos.modelos.Trabajo;
import gui.trabajos.vistas.VentanaFinalizarTrabajo;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author FAMILIA
 */
public class ControladorFinalizarTrabajo {
    private VentanaFinalizarTrabajo ventana;
    GestorTrabajos gsT = GestorTrabajos.instanciar();
    Trabajo trabajo;

    public ControladorFinalizarTrabajo(Dialog ventana, Trabajo trabajo) {
        IGestorTrabajos gsT = GestorTrabajos.instanciar();
        this.ventana = new VentanaFinalizarTrabajo(ventana, true, this);
        this.trabajo = trabajo;
        this.ventana.setTitle("Finalizar trabajo");
        this.ventana.setLocationRelativeTo(null);
        this.ventana.setVisible(true);
        
    }
    
    public void botonfin (ActionEvent evt){
        LocalDate fechaFinalizacion = obtenerFechaDeJDateChooser(this.ventana.getFechaFin());
        
        
        int confirmacion = JOptionPane.showConfirmDialog(ventana, "¿Desea finalizar el Trabajo?");
        if (confirmacion == 0) {//Si el usuario elige "Si" se procedera a midificar el trabajo seleccionado
            String resultado = gsT.finalizarTrabajo(this.trabajo, fechaFinalizacion);
//            String resultado = gsT.finalizarAlumno(this.unTrabajo, this.unAET.verAlumno(), fechaHasta, razon);

            if (!resultado.equals(IGestorTrabajos.EXITO)) {
                gsT.cancelar();
                JOptionPane.showMessageDialog(null, resultado, "", JOptionPane.ERROR_MESSAGE);
            }
            else{
            JOptionPane.showMessageDialog(this.ventana, "El trabajo se finalizo exitosamente", "", JOptionPane.PLAIN_MESSAGE);
            this.ventana.dispose();
            }
        }
        
//        int confirmacion = JOptionPane.showConfirmDialog(ventana, "¿Desea finalizar el Trabajo?");
//            if (confirmacion == 0) {//Si el usuario elige "Si" se procedera a midificar el trabajo seleccionado

                
                
//                for(AlumnoEnTrabajo a : trabajo.verAlumnosActuales()){
//                    a.asignarFechaHasta(fecha);
//                    a.asignarRazon("Finalizacion del trabajo");
//                }
//                for (RolEnTrabajo ret : trabajo.verProfesoresConRoles()){
//                    if (ret.verRazon() == null){
//                        ret.asignarFechaHasta(fecha);
//                        ret.asignarRazon("Finalizacion del trabajo");
//                    }
//                }
//                 this.ventana.dispose();
//            }
           
    }
    
    public void botonCancelar (ActionEvent evt){
        this.gsT.cancelar();
        this.ventana.dispose();
    }
    
    
    
    
    
    private LocalDate obtenerFechaDeJDateChooser(JDateChooser dateChooser) {        //Convierte a LocalDate la fecha obtenida del JDateChooser
        Date date;
        if (dateChooser.getCalendar() != null) {
            date = dateChooser.getCalendar().getTime();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        else
            return null;
    }
    
}
