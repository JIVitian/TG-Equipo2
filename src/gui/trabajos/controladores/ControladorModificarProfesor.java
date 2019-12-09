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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;


public class ControladorModificarProfesor implements IControladorModificarProfesor {
    private VentanaModificarProfesor ventana;
    private Trabajo unTrabajo;
    private RolEnTrabajo unRET;
    private GestorPersonas gsP;
    private GestorTrabajos gsT;

    public ControladorModificarProfesor(Dialog ventanaPadre, Trabajo unTrabajo, RolEnTrabajo unRET) {
        this.unTrabajo = unTrabajo;
        this.unRET = unRET;
        gsT = GestorTrabajos.instanciar();
        this.ventana = new VentanaModificarProfesor(this, ventanaPadre);
        gsP = GestorPersonas.instanciar();
        llenarComboBox ();
        this.ventana.setTitle(TRABAJO_MODIFICAR);
        this.ventana.setLocationRelativeTo(null);
        this.ventana.setVisible(true);

//        this.ventana.verComboProfesores().setModel(new ModeloComboProfesores());//Saco el modelo de lo que hicieron en el grupo de personas
//        this.ventana.verComboProfesores().setSelectedIndex(-1);
    }
    
    /**
     * Acción a ejecutar cuando se selecciona el botón Guardar
     * @param evt evento
     */ 
    @Override
    public void btnAceptarClic(ActionEvent evt) {
        this.guardar();
    }
    
    private void guardar(){
        IGestorTrabajos gsT = GestorTrabajos.instanciar();
        IGestorPersonas gsP = GestorPersonas.instanciar();
//        IGestorRolesEnTrabajos gRet = GestorRolesEnTrabajos.instanciar();
        
        LocalDate fechaHasta = obtenerFechaDeJDateChooser(this.ventana.verFechaHasta());
        String razon = this.ventana.verTxtRazon().getText();

        int dniNuevoProf;
        if (this.ventana.verComboProfesores().getSelectedItem() != null) {
            dniNuevoProf = Integer.parseInt((this.ventana.verComboProfesores().getSelectedItem().toString().split("-"))[1].trim());
            String resultado = gsT.reemplazarProfesor(this.unTrabajo, this.unRET.verProfesor(), fechaHasta, razon, gsP.dameProfesor(dniNuevoProf));
            
            if (!resultado.equals(IGestorTrabajos.EXITO)) {
            gsT.cancelar();
            JOptionPane.showMessageDialog(this.ventana, resultado, TRABAJO_MODIFICAR, JOptionPane.ERROR_MESSAGE);
            colorCalendario();
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
                    colorCalendario();
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.btnCancelarClic(null);
                    break;
                case KeyEvent.VK_DELETE:
                    colorCalendario();
                    break;
                case KeyEvent.VK_SPACE:
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
        }
    }
    
    
    private void llenarComboBox (){
        List<Profesor> listaProfes = new ArrayList<>();
        listaProfes = this.gsP.buscarProfesores(null);  //creo una lista con todos los profesores
        
        String profesores[] = new String[listaProfes.size()];           //Con este arreglo de cadenas armare los comboBox
        for (int i = 0; i < listaProfes.size(); i++) {
            profesores[i] = listaProfes.get(i).verApellidos() + ", " + listaProfes.get(i).verNombres()  + "-" + listaProfes.get(i).verDNI();
        }
        this.ventana.verComboProfesores().setModel(new javax.swing.DefaultComboBoxModel<>(profesores));
    }
    
    
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
    
    private void colorCalendario(){
        if (this.ventana.verFechaHasta().getCalendar() == null) {
            this.ventana.verFechaHasta().setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        }else{
            this.ventana.verFechaHasta().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
}