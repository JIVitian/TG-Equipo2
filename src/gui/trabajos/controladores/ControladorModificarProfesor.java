/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.controladores;

import com.toedter.calendar.JDateChooser;
import gui.interfaces.IControladorModificarProfesor;
import gui.interfaces.IGestorPersonas;
import gui.interfaces.IGestorTrabajos;
import gui.personas.modelos.GestorPersonas;
import gui.personas.modelos.Profesor;
import gui.trabajos.modelos.GestorTrabajos;
import gui.trabajos.modelos.RolEnTrabajo;
import gui.trabajos.modelos.Trabajo;
import gui.trabajos.vistas.VentanaModificarProfesor;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;


public class ControladorModificarProfesor implements IControladorModificarProfesor {
    private final VentanaModificarProfesor ventana;
    private Trabajo unTrabajo;
    private RolEnTrabajo unRET;
    
    /**
     * Constructor
     * Muestra la ventana de ModificarProfesor de forma modal
     * @param ventanaPadre
     * @param unTrabajo
     * @param unRET
     */
    public ControladorModificarProfesor(Dialog ventanaPadre, Trabajo unTrabajo, RolEnTrabajo unRET) {
        this.unTrabajo = unTrabajo;
        this.unRET = unRET;
        this.ventana = new VentanaModificarProfesor(this, ventanaPadre);
        
        this.setearVentana();
        this.agregarListeners();
        this.llenarComboBox ();
        
        this.ventana.setLocationRelativeTo(null);
        this.ventana.setVisible(true);
    }
    
    /**
     * Inicializa los campos de VentanaModificarProfesor
     */ 
    private void setearVentana(){
        this.ventana.setTitle(TRABAJO_MODIFICAR);

        //Convierto fechaDesde de LocalDate a Date
        Date fechaDesde = Date.from(this.unRET.verFechaDesde().atStartOfDay(ZoneId.systemDefault()).toInstant());

        //Muestro y desabilito el dateChooser de fechaDesde
        this.ventana.verFechaDesde().setDate(fechaDesde);
        this.ventana.verFechaDesde().setEnabled(false);
        
//        //Muestro la fechaDesde como referencia en el dateChooser de fechaHasta
//        this.ventana.verFechaHasta().setDate(date);
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
     * Finaliza un Profesor en Trabajo mientras lo reemplaza con otro
     * Muestra los correspondientes mensajes flotantes si la operacion tuvo exito o no
     */  
    private void guardar(){
        IGestorTrabajos gsT = GestorTrabajos.instanciar();
        IGestorPersonas gsP = GestorPersonas.instanciar();
        
        LocalDate fechaHasta = obtenerFechaDeJDateChooser(this.ventana.verFechaHasta());
        String razon = this.ventana.verTxtRazon().getText();

        int dniNuevoProf;
        if (this.ventana.verComboProfesores().getSelectedItem() != null) {
            dniNuevoProf = Integer.parseInt((this.ventana.verComboProfesores().getSelectedItem().toString().split("-"))[1].trim());
            String resultado = gsT.reemplazarProfesor(this.unTrabajo, this.unRET.verProfesor(), fechaHasta, razon, gsP.dameProfesor(dniNuevoProf));
            
            if (!resultado.equals(IGestorTrabajos.EXITO)) {
                gsT.cancelar();
                JOptionPane.showMessageDialog(this.ventana, resultado, TRABAJO_MODIFICAR, JOptionPane.ERROR_MESSAGE);
                colorCalendarios();
            }
            else{
                JOptionPane.showMessageDialog(this.ventana, resultado, "", JOptionPane.INFORMATION_MESSAGE );
                this.ventana.dispose();
            }
        } else{
            JOptionPane.showMessageDialog(this.ventana, "Seleccione un profesor de reemplazo", "", JOptionPane.ERROR_MESSAGE);
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
        if (!Character.isLetter(c)) { //sólo se aceptan letras, Enter, Del, Backspace y espacio
            switch(c) {
                case KeyEvent.VK_ENTER: 
                    this.guardar();
                    break;
                case KeyEvent.VK_BACK_SPACE: 
                    colorCalendarios();
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.btnCancelarClic(null);
                    break;
                case KeyEvent.VK_DELETE:
                    colorCalendarios();
                    break;
                case KeyEvent.VK_SPACE:
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
        }
    }
    
    
    private void llenarComboBox (){
        IGestorPersonas gsP = GestorPersonas.instanciar();
        List<Profesor> listaProfes = gsP.buscarProfesores(null);  //creo una lista con todos los profesores
        
        String profesores[] = new String[listaProfes.size()];           //Con este arreglo de cadenas armare los comboBox
        for (int i = 0; i < listaProfes.size(); i++) {
            profesores[i] = listaProfes.get(i).verApellidos() + ", " + listaProfes.get(i).verNombres()  + " - " + listaProfes.get(i).verDNI();
        }
        this.ventana.verComboProfesores().setModel(new javax.swing.DefaultComboBoxModel<>(profesores));
    }
    
    /**
     * Obtiene la fecha de un campo JDateChooser
     * Si no hay seleccionada una fecha devuelve null
     * @param dateChooser campo JDateChooser
     * @return LocalDate - fecha de un campo JDateChooser
     */
    private LocalDate obtenerFechaDeJDateChooser(JDateChooser dateChooser) {
        Date fecha;
        if (dateChooser.getCalendar() != null) {
            fecha = dateChooser.getCalendar().getTime();
            return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        else{
            return null;
        }
    }
    
    /**
     * Le da color al borde de los campos dateChooser de VentanaModificarProfesor
     */
    private void colorCalendarios(){
        if (this.ventana.verFechaHasta().getCalendar() == null || obtenerFechaDeJDateChooser(this.ventana.verFechaHasta()).isBefore(unRET.verFechaDesde())) {
            this.ventana.verFechaHasta().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }else{
            this.ventana.verFechaHasta().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
//    /**
//     * Le da color al borde del campo txtRazon de VentanaModificarProfesor
//     */
//    private void colorTxtRazon(){
//        if (this.ventana.verTxtRazon().getText().trim().isEmpty()) {
//            this.ventana.verTxtRazon().setBorder(BorderFactory.createLineBorder(Color.RED, 1)); //si el campo de texto esta vacio,se resalta en rojo
//        }else{
//            this.ventana.verTxtRazon().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); //si el campo no esta vacio, elborde se vuleve gris(no verde o algo por el estilo,pues no se esta seguro que el valor es correcto
//        }
//    }
}