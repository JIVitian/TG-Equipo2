/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.controladores;

import com.toedter.calendar.JDateChooser;
import gui.interfaces.IControladorModificarAlumno;
import gui.interfaces.IGestorTrabajos;
import gui.trabajos.modelos.AlumnoEnTrabajo;
import gui.trabajos.modelos.GestorTrabajos;
import gui.trabajos.modelos.Trabajo;
import gui.trabajos.vistas.VentanaModificarAlumno;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;


public class ControladorModificarAlumno implements IControladorModificarAlumno {
    private final VentanaModificarAlumno ventana;
    private Trabajo unTrabajo;
    private AlumnoEnTrabajo unAET;
    
    /**
     * Constructor
     * Muestra la ventana de ModificarAlumno de forma modal
     * @param ventanaPadre
     * @param unTrabajo
     * @param unAET
     */
    public ControladorModificarAlumno(Dialog ventanaPadre, Trabajo unTrabajo, AlumnoEnTrabajo unAET) {
        this.unTrabajo = unTrabajo;
        this.unAET = unAET;
        this.ventana = new VentanaModificarAlumno(this, ventanaPadre);
        
        this.setearVentana();
        this.agregarListeners();
        
        this.ventana.setLocationRelativeTo(null);
        this.ventana.setVisible(true);
    }
    
    /**
     * Inicializa los campos de VentanaModificarAlumno
     */ 
    private void setearVentana(){
        this.ventana.setTitle(TRABAJO_MODIFICAR);
        
        //Convierto fechaDesde de LocalDate a Date
        Date fechaDesde = Date.from(this.unAET.verFechaDesde().atStartOfDay(ZoneId.systemDefault()).toInstant());

        //Muestro y desabilito la fechaDesde
        this.ventana.verFechaDesde().setDate(fechaDesde);
        this.ventana.verFechaDesde().setEnabled(false);
        
//        //Muestro la fechaDesde como referencia en el dateChooser de fechaHasta
//        this.ventana.verFechaHasta().setDate(fechaDesde);
        //Muestro la fecha actual como referencia en el dateChooser de fechaHasta
        LocalDate fechaHoy = LocalDate.now();
        Date fechaActual = Date.from(fechaHoy.atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (fechaDesde.before(fechaActual)) {
            this.ventana.verFechaHasta().setDate(fechaActual);
        } else {
            this.ventana.verFechaHasta().setDate(fechaDesde);
        }
        
    }
    
    /**
     * Agrega listeners a cada elemento de la ventana, recuadrandolo
     * Recuadra en rojo cuando sea incorrecto
     * Recuadra en gris cuando sea valido
     */ 
    private void agregarListeners(){
        //-----------------------------------------------------------------------------------//
        //Agrego listeners a los elementos de la ventana para marcar en rojo cuando esten mal//
        //-----------------------------------------------------------------------------------//
       
//        //Listener de txtRazon
//        this.ventana.verTxtRazon().addFocusListener(new java.awt.event.FocusAdapter() {
//            @Override
//            public void focusGained(java.awt.event.FocusEvent evt) {
//                colorTxtRazon();
//            }
//        });
        
        //Listener fechaHasta
        this.ventana.verFechaHasta().getDateEditor().addPropertyChangeListener((PropertyChangeEvent e) -> {
            if ("date".equals(e.getPropertyName())) {
                colorCalendarios();
            }
        });
    }
    
    /**
     * Acción a ejecutar cuando se selecciona el botón Guardar
     * @param evt evento
     */   
    @Override
    public void btnAceptarClic(ActionEvent evt) {
        this.guardar();
    }
    
    /**
     * Finaliza un Alumno en Trabajo
     * Muestra los correspondientes mensajes flotantes si la operacion tuvo exito o no
     */  
    private void guardar(){
        LocalDate fechaHasta = obtenerFechaDeJDateChooser(this.ventana.verFechaHasta());
        String razon = this.ventana.verTxtRazon().getText();
    
        IGestorTrabajos gsT = GestorTrabajos.instanciar();
        int confirmacion = JOptionPane.showConfirmDialog(ventana, "¿Desea finalizar el Alumno?");
        
        if (confirmacion == 0) {//Si el usuario elige "Si" se procedera a midificar el trabajo seleccionado
        String resultado = gsT.finalizarAlumno(this.unTrabajo, this.unAET.verAlumno(), fechaHasta, razon);
        
            if (!resultado.equals(IGestorTrabajos.EXITO)) {
                gsT.cancelar();
                JOptionPane.showMessageDialog(null, resultado, TRABAJO_MODIFICAR, JOptionPane.ERROR_MESSAGE);
                colorCalendarios();
            }
            else{
                JOptionPane.showMessageDialog(this.ventana, "El alumno se finalizo exitosamente", TRABAJO_MODIFICAR, JOptionPane.PLAIN_MESSAGE);
                this.ventana.dispose();
            }
        }
    }

    /**
     * Acción a ejecutar cuando se selecciona el botón Cancelar
     * @param evt evento
     */    
    @Override
    public void btnCancelarClic(ActionEvent evt) {
        IGestorTrabajos gsT = GestorTrabajos.instanciar();
        gsT.cancelar();
        this.ventana.dispose();
    }

    /**
     * Acción a ejecutar cuando se presiona una tecla en el campo txtRazon
     * @param evt evento
     */
    @Override
    public void txtRazonPresionarTecla(KeyEvent evt) {
        char c = evt.getKeyChar();            
        if (!Character.isLetter(c)) { //sólo se aceptan letras, Enter, Del, Backspace y espacio
            switch(c) {
                case KeyEvent.VK_ENTER: 
                    this.guardar();
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.btnCancelarClic(null);
                    break;
                case KeyEvent.VK_BACK_SPACE:  
                    break;
                case KeyEvent.VK_DELETE:
                    break;
                case KeyEvent.VK_SPACE:
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
        }else{
            this.ventana.verTxtRazon().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }

    /**
     * Acción a ejecutar cuando se presiona una tecla en el campo fechaHasta
     * @param evt evento
     */
    @Override
    public void fechaHastaPresionarTecla(KeyEvent evt) {
        char c = evt.getKeyChar();            
//        if (!Character.isLetter(c)) { //sólo se aceptan letras, Enter, Del, Backspace y espacio
            switch(c) {
                case KeyEvent.VK_ENTER: 
                    this.guardar();
                    break;
                case KeyEvent.VK_BACK_SPACE:  
                    colorCalendarios();
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.btnCancelarClic(null);
                case KeyEvent.VK_DELETE:
                    colorCalendarios();
                    break;
                case KeyEvent.VK_SPACE:
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
//        }
    }
    
    /**
     * Obtiene la fecha de un campo JDateChooser
     * Si no hay seleccionada una fecha devuelve null
     * @param dateChooser campo JDateChooser
     * @return LocalDate - fecha de un campo JDateChooser
     */
    private LocalDate obtenerFechaDeJDateChooser(JDateChooser dateChooser) { //Convierte a LocalDate la fecha obtenida del JDateChooser
        Date date;
        if (dateChooser.getCalendar() != null) {
            date = dateChooser.getCalendar().getTime();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        else{
            return null;
        }
    }
    
    /**
     * Le da color al borde de los campos dateChooser de VentanaModificarAlumno
     */
    private void colorCalendarios(){
        if (this.ventana.verFechaHasta().getCalendar() == null || obtenerFechaDeJDateChooser(this.ventana.verFechaHasta()).isBefore(unAET.verFechaDesde())) {
            this.ventana.verFechaHasta().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }else{
            this.ventana.verFechaHasta().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
//    /**
//     * Le da color al borde del campo txtRazon de VentanaModificarAlumno
//     */
//    private void colorTxtRazon(){
//        if (this.ventana.verTxtRazon().getText().trim().isEmpty()) {
//            this.ventana.verTxtRazon().setBorder(BorderFactory.createLineBorder(Color.RED, 1)); //si el campo de texto esta vacio,se resalta en rojo
//        }else{
//            this.ventana.verTxtRazon().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); //si el campo no esta vacio, elborde se vuleve gris(no verde o algo por el estilo,pues no se esta seguro que el valor es correcto
//        }
//    }
}
