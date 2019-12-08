/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.personas.controladores;

import gui.interfaces.IControladorAMProfesor;
import gui.interfaces.IControladorPersonas;
import gui.interfaces.IGestorPersonas;
import gui.personas.modelos.Cargo;
import gui.personas.modelos.GestorPersonas;
import gui.personas.modelos.ModeloComboCargos;
import gui.personas.modelos.Profesor;
import gui.personas.vistas.VentanaAMProfesor;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class ControladorAMProfesor implements IControladorAMProfesor{
    private VentanaAMProfesor ventana;
    private Profesor profesor;

    /**
     * Constructor
     * @param ventanaPadre (VentanaPersonas en este caso)
     * @param profesor profesor a modificar. Si es null se debe crear uno nuevo.
     */
    public ControladorAMProfesor(Dialog ventanaPadre, Profesor profesor) {
        this.ventana = new VentanaAMProfesor(this, ventanaPadre);
        this.profesor = profesor;
        if(profesor != null){
            ventana.setTitle(IControladorPersonas.PROFESOR_MODIFICAR);
            ventana.verTxtApellidos().setText(profesor.verApellidos());
            ventana.verTxtNombres().setText(profesor.verNombres());
            ventana.verTxtDocumento().setText(String.valueOf(profesor.verDNI()));
            ventana.verTxtDocumento().setEditable(false);
        }
        else
            ventana.setTitle(IControladorPersonas.PROFESOR_NUEVO);
        this.inicializarComboCargos(this.ventana.verComboCargo());
        this.agregarEventos();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }
    
    /**
     * Metodo para agregar eventos especificos adicionales
     */
    private void agregarEventos(){
        //Evento para cuando el campo txtApellido gana foco
        this.ventana.verTxtApellidos().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoTxtApellido(); //accion a ejecutar cuando suceda el evento
            }
        });
        
        //Evento para cuando el campo txtNombre gana foco
        this.ventana.verTxtNombres().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoTxtNombre(); //accion a ejecutar cuando suceda el evento
            }
        });
        
        //Evento para cuando el campo txtDocumento gana foco
        this.ventana.verTxtDocumento().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoTxtDocumento(); //accion a ejecutar cuando suceda el evento
            }
        });
    }
    
     /**
     * Acción a ejecutar cuando se hace clic en el botón Guardar
     * @param evt evento
     */ 
    @Override
    public void btnGuardarClic(ActionEvent evt) {
        this.guardar();
    }
    
    /**
     * Crea o modifica un profesor si los datos con correctos
     */
    private void guardar(){
        String apellido = this.ventana.verTxtApellidos().getText().trim();
        String nombre = this.ventana.verTxtNombres().getText().trim();
        int dni;
        if(!this.ventana.verTxtDocumento().getText().trim().isEmpty())
            dni = Integer.valueOf(this.ventana.verTxtDocumento().getText().trim());
        else
            dni=0;
        Cargo cargo = (Cargo)this.ventana.verComboCargo().getSelectedItem();
        
        IGestorPersonas gestor = GestorPersonas.instanciar();
        String resultado;
        if(profesor == null)
            resultado = gestor.nuevoProfesor(apellido, nombre, dni, cargo);
        else
            resultado = gestor.modificarProfesor(profesor, apellido, nombre, cargo);
        
        if(resultado.equals(IGestorPersonas.EXITO_PROFESORES)){
            this.ventana.dispose();
        }
        else{
            gestor.cancelarProfesor();
            this.campoTxtApellido();
            this.campoTxtNombre();
            this.campoTxtDocumento();
            JOptionPane.showMessageDialog(null, resultado,IControladorPersonas.TITULO, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Accion a ejecutar cuando se hace clic en Cancelar
     * @param evt 
     */
    @Override
    public void btnCancelarClic(ActionEvent evt) {
        this.cancelar();
    }
    
    /**
     * Cancela la creacion/modificacion de un profesor
     */
    private void cancelar(){
        IGestorPersonas gestor = GestorPersonas.instanciar();
        gestor.cancelarProfesor();
        this.ventana.dispose();
    }

    /**
     * Acción a ejecutar cuando se presiona una tecla en el campo txtApellidos
     * Sólo se permiten letras, Enter, Del, Backspace y espacio
     * @param evt evento
     */ 
    @Override
    public void txtApellidosPresionarTecla(KeyEvent evt) {
        char c = evt.getKeyChar();            
        if (!Character.isLetter(c)) { //sólo se aceptan letras, Enter, Del, Backspace y espacio
            switch(c) {
                case KeyEvent.VK_ENTER:  //cuando se pulse Enter
                    this.guardar();
                    break;
                case KeyEvent.VK_BACK_SPACE: 
                    campoTxtApellido();
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.cancelar();
                    break;
                case KeyEvent.VK_DELETE:
                    campoTxtApellido();
                    break;
                case KeyEvent.VK_SPACE:
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
        }
        else
            this.ventana.verTxtApellidos().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }
    
    /**
     * Metodo encargado de cambiar el color de contorno del campo txtApellido de acuerdo si está vacio o no
     */
    private void campoTxtApellido(){
        if(this.ventana.verTxtApellidos().getText().trim().isEmpty()){ //comprueba si el campo esta vacio
            this.ventana.verTxtApellidos().setBorder(BorderFactory.createLineBorder(Color.RED, 1)); //si esta vacio, resalta el campo de rojo
            
        }
        else{
            this.ventana.verTxtApellidos().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); //si no esta vacio, quita el color rojo si estaba remarcado
        }
    }
    
    /**
     * Acción a ejecutar cuando se presiona una tecla en el campo txtNombres
     * Sólo se permiten letras, Enter, Del, Backspace y espacio
     * @param evt evento
     */ 
    @Override
    public void txtNombresPresionarTecla(KeyEvent evt) {
        char c = evt.getKeyChar();            
        if (!Character.isLetter(c)) { //sólo se aceptan letras, Enter, Del, Backspace y espacio
            switch(c) {
                case KeyEvent.VK_ENTER: 
                    this.guardar();
                    break;
                case KeyEvent.VK_BACK_SPACE: 
                    campoTxtNombre();
                    break;
                case KeyEvent.VK_DELETE:
                    campoTxtNombre();
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.cancelar();
                    break;
                case KeyEvent.VK_SPACE:
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
        }
        else
            this.ventana.verTxtNombres().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }
    
    /**
     * Metodo encargado de cambiar el color de contorno del campo txtNombre de acuerdo si está vacio o no
     */
    private void campoTxtNombre(){
        if(this.ventana.verTxtNombres().getText().trim().isEmpty()){
            this.ventana.verTxtNombres().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            
        }
        else{
            this.ventana.verTxtNombres().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }

    /**
     * Acción a ejecutar cuando se presiona una tecla en el campo txtDocumento
     * Sólo se permiten nimeros (digitos), Enter, Del y Backspace
     * @param evt evento
     */ 
    @Override
    public void txtDocumentoPresionarTecla(KeyEvent evt) {
        char c = evt.getKeyChar();            
        if (!Character.isDigit(c)) { //sólo se aceptan numeros, Enter, Del, Backspace y espacio
            switch(c) {
                case KeyEvent.VK_ENTER: 
                    this.guardar();
                    break;
                case KeyEvent.VK_BACK_SPACE: 
                    campoTxtDocumento();
                    break;
                case KeyEvent.VK_DELETE:
                    campoTxtDocumento();
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.cancelar();
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
        }
        else{
            this.ventana.verTxtDocumento().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
    /**
     * Metodo encargado de cambiar el color de contorno del campo txtDocumento de acuerdo si está vacio o no
     */
    private void campoTxtDocumento(){
        if(this.ventana.verTxtDocumento().getText().trim().length() < 1){
            this.ventana.verTxtDocumento().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }
        else{
            this.ventana.verTxtDocumento().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
    private void inicializarComboCargos(JComboBox comboCargos){
        ModeloComboCargos mcc = new ModeloComboCargos();
        comboCargos.setModel(mcc);
        
        if(this.profesor != null){
            mcc.seleccionarCargo(profesor.verCargo());
        }
    }
}
