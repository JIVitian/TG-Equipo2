/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.personas.controladores;


import gui.interfaces.IControladorAMAlumno;
import gui.interfaces.IControladorPersonas;
import gui.interfaces.IGestorPersonas;
import gui.personas.modelos.Alumno;
import gui.personas.modelos.GestorPersonas;
import gui.personas.vistas.VentanaAMAlumno;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

public class ControladorAMAlumno implements IControladorAMAlumno {
    
    private VentanaAMAlumno ventana;
    private Alumno alumno;
    
    /**
     * Constructor
     * @param ventanaPadre (VentanaPersonas en este caso)
     * @param alumno alumno a modificar. Si es null se debe crear uno nuevo.
     */
    public ControladorAMAlumno(Dialog ventanaPadre, Alumno alumno) {
        this.ventana = new VentanaAMAlumno(this, ventanaPadre);
        this.alumno = alumno;
        if(alumno != null){
            ventana.setTitle(IControladorPersonas.ALUMNO_MODIFICAR);
            ventana.verTxtApellidos().setText(alumno.verApellidos());
            ventana.verTxtNombres().setText(alumno.verNombres());
            ventana.verTxtDocumento().setText(String.valueOf(alumno.verDNI()));
            ventana.verTxtDocumento().setEditable(false);
            ventana.verTxtCX().setText(alumno.verCX());
        }  
        else
            ventana.setTitle(IControladorPersonas.ALUMNO_NUEVO);
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
        
        //Evento para cuando el campo txtCx gana foco
        this.ventana.verTxtCX().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoTxtCx(); //accion a ejecutar cuando suceda el evento
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
     * Crea o modifica un alumno si los datos son correctos
     */
    private void guardar() {
        String nombres = this.ventana.verTxtNombres().getText().trim();
        String apellidos = this.ventana.verTxtApellidos().getText().trim();
        String documento = this.ventana.verTxtDocumento().getText().trim();
        String cx = this.ventana.verTxtCX().getText().trim();
               
        int dni;
        if(documento.isEmpty())
            dni = 0;
        else
            dni = Integer.parseInt(documento);
        
        IGestorPersonas gp = GestorPersonas.instanciar();
        String resultado;
        if(alumno == null)
            resultado = gp.nuevoAlumno(apellidos, nombres, dni, cx);
        else
            resultado = gp.modificarAlumno(alumno, apellidos, nombres, cx);
        
        if (!resultado.equals(IGestorPersonas.EXITO_ALUMNOS)) {
            gp.cancelarAlumno();
            this.campoTxtApellido();
            this.campoTxtNombre();
            this.campoTxtDocumento();
            this.campoTxtCx();
            JOptionPane.showMessageDialog(null, resultado, IControladorPersonas.TITULO, JOptionPane.ERROR_MESSAGE);
        }
        else
            this.ventana.dispose();
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
     * Cancela la creacion/modificacion de un alumno
     */
    private void cancelar(){
        IGestorPersonas gestor = GestorPersonas.instanciar();
        gestor.cancelarAlumno();
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
        if (!Character.isLetter(c)) {
        // Sólo se pueden escribir letras, Enter, Del, Backspace y espacio
        switch(c) {
                case KeyEvent.VK_ENTER:
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
            //se quita el contorno rojo al pulsar un caracter valido (el campo deja de estar vacio)
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
        if (!Character.isLetter(c)) {
        // Sólo se pueden escribir letras, Enter, Del, Backspace y espacio
        switch(c) {
                case KeyEvent.VK_ENTER:
                    this.guardar();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    campoTxtNombre();
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.cancelar();
                    break;
                case KeyEvent.VK_DELETE:
                    campoTxtNombre();
                    break;
                case KeyEvent.VK_SPACE:
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
        }
        else
            //se quita el contorno rojo al pulsar un caracter valido (el campo deja de estar vacio)
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
        if (!Character.isDigit(c)) {
        // Sólo se pueden escribir números, Enter, Del y Backspace
        switch(c) {
                case KeyEvent.VK_ENTER:
                    this.guardar();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    campoTxtDocumento();
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.cancelar();
                    break;
                case KeyEvent.VK_DELETE:
                    campoTxtDocumento();
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
        }
        else
            //se quita el contorno rojo al pulsar un caracter valido (el campo deja de estar vacio)
            this.ventana.verTxtDocumento().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }
    
    /**
     * Metodo encargado de cambiar el color de contorno del campo txtDocumento de acuerdo si está vacio o no
     */
    private void campoTxtDocumento(){
        if(this.ventana.verTxtDocumento().getText().trim().isEmpty()){
            this.ventana.verTxtDocumento().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }
        else{
            this.ventana.verTxtDocumento().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
    /**
     * Acción a ejecutar cuando se presiona una tecla en el campo txtCx
     * Sólo se permiten números, Enter, Del y Backspace
     * @param evt evento
     */ 
    @Override
    public void txtCXPresionarTecla(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetterOrDigit(c)) {
        // Sólo se pueden escribir números, Enter, Del y Backspace
        switch(c) {
                case KeyEvent.VK_ENTER:
                    this.guardar();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    campoTxtCx();
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.cancelar();
                    break;
                case KeyEvent.VK_DELETE:
                    campoTxtCx();
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
        }
        else
            //se quita el contorno rojo al pulsar un caracter valido (el campo deja de estar vacio)
            this.ventana.verTxtCX().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }  
    
    /**
     * Metodo encargado de cambiar el color de contorno del campo txtCx de acuerdo si está vacio o no
     */
    private void campoTxtCx(){
        if(this.ventana.verTxtCX().getText().trim().isEmpty()){
            this.ventana.verTxtCX().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }
        else{
            this.ventana.verTxtCX().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
}
