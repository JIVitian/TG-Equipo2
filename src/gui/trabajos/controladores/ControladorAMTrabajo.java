/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.controladores;

import com.toedter.calendar.JDateChooser;
import gui.areas.modelos.Area;
import gui.areas.modelos.GestorAreas;
import gui.interfaces.IControladorAMTrabajo;
import gui.interfaces.IControladorTrabajos;
import gui.interfaces.IGestorAlumnosEnTrabajos;
import gui.interfaces.IGestorAreas;
import gui.interfaces.IGestorPersonas;
import gui.interfaces.IGestorRolesEnTrabajos;
import gui.interfaces.IGestorTrabajos;
import static gui.interfaces.IGestorTrabajos.EXITO;
import gui.personas.modelos.Alumno;
import gui.personas.modelos.GestorPersonas;
import gui.personas.modelos.Profesor;
import gui.trabajos.modelos.AlumnoEnTrabajo;
import gui.trabajos.modelos.GestorAlumnosEnTrabajos;
import gui.trabajos.modelos.GestorRolesEnTrabajos;
import gui.trabajos.modelos.GestorTrabajos;
import gui.trabajos.modelos.Rol;
import gui.trabajos.modelos.RolEnTrabajo;
import gui.trabajos.modelos.Trabajo;
import gui.trabajos.vistas.VentanaAMTrabajo;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;


public class ControladorAMTrabajo implements IControladorAMTrabajo{
    private VentanaAMTrabajo ventana;
    IGestorTrabajos gsT ;
    IGestorPersonas gsP;
    IGestorAreas gsA;
    IGestorRolesEnTrabajos gsRET;
    IGestorAlumnosEnTrabajos gsAET;
    private Trabajo unTrabajo;
    
    /**
     * Constructor
     * @param ventanaPadre (VentanaAreas en este caso)
     * @param unTrabajo
     */    
    public ControladorAMTrabajo(Dialog ventanaPadre, Trabajo unTrabajo) {
        this.unTrabajo = unTrabajo;
        gsT = GestorTrabajos.instanciar();
        gsP = GestorPersonas.instanciar();
        gsA = GestorAreas.instanciar();
        gsRET = GestorRolesEnTrabajos.instanciar();
        gsAET = GestorAlumnosEnTrabajos.instanciar();
        this.ventana = new VentanaAMTrabajo(this, ventanaPadre);
        refrescar();
        
        this.setearVentana();
        this.agregarListeners();
        this.ventana.setLocationRelativeTo(null);
        this.ventana.setVisible(true);
    }
    
    private class ModeloComboTutorCotutor extends DefaultComboBoxModel{
        public ModeloComboTutorCotutor() {
            for (Profesor p : gsP.buscarProfesores(null)) {
                this.addElement(p);
            }
        }
        public Profesor obtenerTutorCotutor(){
            return (Profesor)this.getSelectedItem();
        }
        public void seleccionarTutorCotutor(Profesor p){
            this.setSelectedItem(p);
        }
    };
    
    private void setearVentana(){
//        this.ventana.getjComboTutor().setModel(new ModeloComboTutorCotutor());
//        this.ventana.getjComboCotutor().setModel(new ModeloComboTutorCotutor());
        
        if (this.unTrabajo == null) {
            this.ventana.setTitle(IControladorTrabajos.TRABAJO_NUEVO);
            this.ventana.getjFechaFinalizacion().setEnabled(false);
            
            ((ModeloComboTutorCotutor)this.ventana.getjComboTutor().getModel()).seleccionarTutorCotutor(null);
            ((ModeloComboTutorCotutor)this.ventana.getjComboCotutor().getModel()).seleccionarTutorCotutor(null);
//            this.ventana.getjComboTutor().setSelectedItem(null);
//            this.ventana.getjComboCotutor().setSelectedItem(null);
            
            
        } else {
            this.ventana.setTitle(IControladorTrabajos.TRABAJO_MODIFICAR);
            
            //Titulo
            this.ventana.verTxtTitulo().setText(this.unTrabajo.verTitulo());
            this.ventana.verTxtTitulo().setEnabled(false);
            
            //Duracion
            this.ventana.verTxtDuracion().setText(Integer.toString(this.unTrabajo.verDuracion()));
            this.ventana.verTxtDuracion().setEnabled(false);
            
            //Fechas
            GregorianCalendar fechaP = GregorianCalendar.from(this.unTrabajo.verFechaPresentacion().atStartOfDay(ZoneId.systemDefault()));
            this.ventana.getjFechaPresentacion().setCalendar(fechaP);
            this.ventana.getjFechaPresentacion().setEnabled(false);
            
            GregorianCalendar fechaAp = GregorianCalendar.from(this.unTrabajo.verFechaAprobacion().atStartOfDay(ZoneId.systemDefault()));
            this.ventana.getjFechaAprobacion().setCalendar(fechaAp);
            this.ventana.getjFechaAprobacion().setEnabled(false);
            
            //Areas
            List<Area> listaAreas = this.unTrabajo.verAreas();
//            int[] seleccionadosA = new int[listaAreas.size()];
//            int i = 0;
//
//            for (Area area : listaAreas) {
//                seleccionadosA[i++] = gsA.ordenArea(area);
//            }
//            this.ventana.getjTablaAreas().getSelectedRows().setSelectionMode(seleccionadosA);
            this.ventana.getjTablaAreas().setEnabled(false);
            
            //Alumno
            this.ventana.getjTablaAlumnos().setEnabled(false);
            
            //Tutor
            ((ModeloComboTutorCotutor)this.ventana.getjComboTutor().getModel()).seleccionarTutorCotutor(this.unTrabajo.verTutorOCotutor(Rol.TUTOR));
            this.ventana.getjComboTutor().setEnabled(false);
            
            //Cotutor
            ((ModeloComboTutorCotutor)this.ventana.getjComboCotutor().getModel()).seleccionarTutorCotutor(this.unTrabajo.verTutorOCotutor(Rol.COTUTOR));
//            this.ventana.getjComboCotutor().setSelectedItem(this.unTrabajo.verTutorOCotutor(Rol.COTUTOR));
            this.ventana.getjComboCotutor().setEnabled(false);
            
            //Jurado
            this.ventana.getjTablaJurado().setEnabled(false);
        }
    }
    
    private void refrescar() {            //utilizo este metodo para llenar las tablas y los combo box
        List<Profesor> listaProfes = this.gsP.buscarProfesores(null);  //creo una lista con todos los profesores
        List<Alumno> listaAlumnos = this.gsP.buscarAlumnos(null);      //creo una lista con todos los alumnos
        List<Area> listaAreas = this.gsA.buscarAreas(null);          //creo una lista con todos las areas
        
        //Lleno la tabla de areas
        String matrizA[][] = new String[(listaAreas.size())] [1];
        for (int i = 0; i < listaAreas.size(); i++) {
            matrizA[i][0] = listaAreas.get(i).verNombre();
        }
        this.ventana.getjTablaAreas().setModel(new javax.swing.table.DefaultTableModel(
            matrizA,
            new String [] {
                "Area"
            }
        ){
            @Override       //por defecto, el modelo Default de tabla si permite editar el contenido de las celdas
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        //Con este arreglo de cadenas armare los comboBox
        String profesores[] = new String[listaProfes.size()];
        for (int i = 0; i < listaProfes.size(); i++) {
            profesores[i] = listaProfes.get(i).verApellidos() + ", " + listaProfes.get(i).verNombres()  + " - " + listaProfes.get(i).verDNI();
        }
        
        //Lleno los comboBox
//        this.ventana.getjComboTutor().setModel(new javax.swing.DefaultComboBoxModel<>(profesores));         
//        this.ventana.getjComboCotutor().setModel(new javax.swing.DefaultComboBoxModel<>(profesores));
        this.ventana.getjComboTutor().setModel(new ModeloComboTutorCotutor());
        this.ventana.getjComboCotutor().setModel(new ModeloComboTutorCotutor());

        //Llena la tabla de Jurados
        String matrizp[][] = new String[(listaProfes.size())] [2];
        for (int i = 0; i < listaProfes.size(); i++) {
            matrizp[i][0] = listaProfes.get(i).verApellidos() + ", " + listaProfes.get(i).verNombres() ;
        }
        
        for (int i = 0; i < listaProfes.size(); i++) {
            matrizp [i][1] = Integer.toString(listaProfes.get(i).verDNI());
        }
        this.ventana.getjTablaJurado().setModel(new javax.swing.table.DefaultTableModel(
            matrizp,
            new String [] {
                "Jurado", "DNI"
            }
        ){
            @Override       //por defecto, el modelo Default de tabla si permite editar el contenido de las celdas
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        //Llena la tabla de Alumnos
        String[][] matrizal = new String[(listaAlumnos.size())] [2];             
        for (int i = 0; i < listaAlumnos.size(); i++) {
            matrizal[i][0] = listaAlumnos.get(i).verApellidos() + ", " + listaAlumnos.get(i).verNombres();
        }
        for (int i = 0; i < listaAlumnos.size(); i++) {
            matrizal [i][1] = listaAlumnos.get(i).verCX();
        }
        
        this.ventana.getjTablaAlumnos().setModel(new javax.swing.table.DefaultTableModel(
            matrizal,
            new String [] {
                "Alumnos", "CX"
            }
        ){
            @Override       //por defecto, el modelo Default de tabla si permite editar el contenido de las celdas
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        
        this.ventana.getjTablaAreas().getTableHeader().setReorderingAllowed(false);       // no se pueden reordenar las columnas
        this.ventana.getjTablaJurado().getTableHeader().setReorderingAllowed(false);       // no se pueden reordenar las columnas
        this.ventana.getjTablaAlumnos().getTableHeader().setReorderingAllowed(false);       // no se pueden reordenar las columnas
    }
    
    
    /**
     * Acción a ejecutar cuando se selecciona el botón Guardar
     * @param evt evento
     */   
    @Override
    public void btnGuardarClic(ActionEvent evt) {
        if (this.unTrabajo == null) {
            this.nuevoTrabajo();
        } else {
        this.modificarTrabajo();
        }
    }
    
    public void modificarTrabajo(){
        LocalDate fechaF = this.obtenerFechaDeJDateChooser(this.ventana.getjFechaFinalizacion());
        
        String resultado = gsT.finalizarTrabajo(this.unTrabajo, fechaF);
        if (!resultado.equals(EXITO)) {
            gsT.cancelar();
            JOptionPane.showMessageDialog(this.ventana, resultado, "", JOptionPane.WARNING_MESSAGE);
            this.colorCalendarios();
        } else {
            JOptionPane.showMessageDialog(this.ventana, resultado, "", JOptionPane.INFORMATION_MESSAGE);
            this.ventana.dispose();
        }
    }
    
    public void nuevoTrabajo(){
        //Titulo del trabajo
        String titulo = this.ventana.getTxtTitulo().getText().trim();
        
        //Duracion del trabajo
        int duracion;
        if (this.ventana.getTxtDuracion().getText().trim().isEmpty()){
            duracion = -1;
        }else{
            duracion = Integer.parseInt(this.ventana.getTxtDuracion().getText().trim());
        }
        
        //Fechas del Trabajo
        LocalDate fechaP = obtenerFechaDeJDateChooser(this.ventana.getjFechaPresentacion());
        LocalDate fechaA = obtenerFechaDeJDateChooser(this.ventana.getjFechaAprobacion());
        
        //Areas del trabajo
        List<Area> listaAreas = new ArrayList<>();
        int[] seleccionadosA = this.ventana.getjTablaAreas().getSelectedRows();
        
        for (int i: seleccionadosA) {
            listaAreas.add(this.gsA.dameArea(this.ventana.getjTablaAreas().getValueAt(i, 0).toString()));
        }
        
        //Roles En Trabajo
        //Jurados
//        List<Profesor> listaJurados = new ArrayList<Profesor>();
        List<RolEnTrabajo> listaRET = new ArrayList<>();
//        
        int[] seleccionadosJ = this.ventana.getjTablaJurado().getSelectedRows();
        for(int i : seleccionadosJ){
            int dniJ ;
            dniJ = Integer.parseInt(this.ventana.getjTablaJurado().getValueAt(i, 1).toString());
            listaRET.add(gsRET.nuevoRolEnTrabajo(gsP.dameProfesor(dniJ), Rol.JURADO, fechaA));
        }
        //Tutor
        int dniT;
        if (this.ventana.getjComboTutor().getSelectedItem() != null) {
            dniT = Integer.parseInt(this.ventana.getjComboTutor().getSelectedItem().toString().split("-")[1].trim());
            listaRET.add(gsRET.nuevoRolEnTrabajo(gsP.dameProfesor(dniT), Rol.TUTOR, fechaP));
        }
        //Cotutor
        int dniCo;
        if (this.ventana.getjComboCotutor().getSelectedItem() != null) {
            dniCo = Integer.parseInt(this.ventana.getjComboCotutor().getSelectedItem().toString().split("-")[1].trim());
            listaRET.add(gsRET.nuevoRolEnTrabajo(gsP.dameProfesor(dniCo), Rol.COTUTOR, fechaP));
        }
        
        //Alumnos en trabajo
        List<AlumnoEnTrabajo> listaAET = new ArrayList<>();
        int[] seleccionadosAl = this.ventana.getjTablaAlumnos().getSelectedRows();
        for(int i : seleccionadosAl){
            String cxAl;
            cxAl = this.ventana.getjTablaAlumnos().getValueAt(i, 1).toString();
            listaAET.add(gsAET.nuevoAlumnoEnTrabajo(gsP.dameAlumno(cxAl), fechaP));
        }
        
        //Tratar de crearlo
        String resultado = gsT.nuevoTrabajo(titulo, duracion, fechaP, fechaA, listaAreas, listaRET, listaAET);
        if (!resultado.equals(EXITO)) { //No se pudo crear un trabajo
            JOptionPane.showMessageDialog(this.ventana, resultado, "", JOptionPane.WARNING_MESSAGE);
            this.colorTxtDuracion();    //resalta en rojo los errores mas obvios
            this.colorAreas();
            this.colorAlumnos();
            this.colorJurados();
            this.colorCalendarios();
        } else {
            JOptionPane.showMessageDialog(this.ventana, resultado, "", JOptionPane.INFORMATION_MESSAGE);
            this.ventana.dispose();
        }
    }

    /**
     * Acción a ejecutar cuando se selecciona el botón Cancelar
     * @param evt evento
     */ 
    @Override
    public void btnCancelarClic(ActionEvent evt) {
        IGestorTrabajos ga = GestorTrabajos.instanciar();
        ga.cancelar();
        this.ventana.dispose();
    }
    
    private void agregarListeners(){
        //-----------------------------------------------------------------------------------//
        //Agrego listeners a los elementos de la ventana para marcar en rojo cuando esten mal//
        //-----------------------------------------------------------------------------------//
       
        //Listener de txtTitulo
        this.ventana.getTxtTitulo().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorTxtTitulo();
            }
        });
        
        //Listener txtDuracion
        this.ventana.getTxtDuracion().addFocusListener(new java.awt.event.FocusAdapter() {
            JDialog ventana = this.ventana;
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorTxtDuracion();
            }
        });
        
        //Listener tablaAreas
        this.ventana.getjTablaAreas().getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {    //la tabla tiene un listener para saber cuando se cambia la seleccion
            if (!e.getValueIsAdjusting()) {
                colorAreas();
            }
        });
        
        //Listener tablaAlumnos
        this.ventana.getjTablaAlumnos().getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {    //la tabla tiene un listener para saber cuando se cambia la seleccion
            if (!e.getValueIsAdjusting()) {
                colorAlumnos();
            }
        });
        
        //Listener tablaJurados
        this.ventana.getjTablaJurado().getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {    //la tabla tiene un listener para saber cuando se cambia la seleccion
            if (!e.getValueIsAdjusting()) {
                colorJurados();
            }
        });
    }

    /**
     * Acción a ejecutar cuando se presiona una tecla en el campo txtDuracion
     * Sólo se permiten letras, Enter, Del, Backspace y espacio
     * @param evt evento
     */
    @Override
    public void txtDuracionPresionarTecla(KeyEvent evt) {
        char c = evt.getKeyChar();            
        if (!Character.isDigit(c)) { //sólo se aceptan letras, Enter, Del, Backspace y espacio
            switch(c) {
                case KeyEvent.VK_ENTER: 
                    this.btnGuardarClic(null);
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.btnCancelarClic(null);
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    this.colorTxtDuracion();
                    break;
                case KeyEvent.VK_DELETE:
//                case KeyEvent.VK_SPACE:
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
        }else{  //La duracion es correcta, no lepongo verde o algo similar porque quedaria mal que elresto no se pueda poner verde
            this.ventana.getTxtDuracion().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }

    /**
     * Acción a ejecutar cuando se presiona una tecla en el campo txtTitulo
     * Sólo se permiten letras, Enter, Del, Backspace y espacio
     * @param evt evento
     */
    @Override
    public void txtTituloPresionarTecla(KeyEvent evt) {
        char c = evt.getKeyChar();            
        if (!Character.isLetter(c)) { //sólo se aceptan letras, Enter, Del, Backspace y espacio
            switch(c) {
                case KeyEvent.VK_ENTER: 
                    this.btnGuardarClic(null);
                    break;
                case KeyEvent.VK_ESCAPE:
                    this.btnCancelarClic(null);
                    break;
                case KeyEvent.VK_BACK_SPACE:    
                    colorTxtTitulo();
                    break;
                case KeyEvent.VK_DELETE:
                    colorTxtTitulo();
                    break;
                case KeyEvent.VK_SPACE:
                    break;
                default:
                    evt.consume(); //consume el evento para que no sea procesado por la fuente
            }
        }else{//se escribe en elcampo, es posiblemente valido
            this.ventana.getTxtTitulo().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
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
    
    private void colorTxtTitulo(){
        if (this.ventana.getTxtTitulo().getText().trim().isEmpty()) {
            this.ventana.getTxtTitulo().setBorder(BorderFactory.createLineBorder(Color.RED, 2)); //si el campo de texto esta vacio,se resalta en rojo
        }else{
            this.ventana.getTxtTitulo().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); //si el campo no esta vacio, elborde se vuleve gris(no verde o algo por el estilo,pues no se esta seguro que el valor es correcto
        }
    }
    
    private void colorTxtDuracion(){
        if (this.ventana.getTxtDuracion().getText().trim().isEmpty()) {
            this.ventana.getTxtDuracion().setBorder(BorderFactory.createLineBorder(Color.RED, 2)); //si el campo de texto esta vacio,se resalta en rojo
        }else{
            this.ventana.getTxtDuracion().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); //si el campo no esta vacio, elborde se vuleve gris(no verde o algo por el estilo,pues no se esta seguro que el valor es correcto
        }
    }
    
    private void colorAreas(){
        if (this.ventana.getjTablaAreas().getSelectedRows().length < 1) {   //si se selecciona menos de un area se resalta en rojo la tabla
            this.ventana.getjTablaAreas().setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        }else{
            this.ventana.getjTablaAreas().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
    private void colorAlumnos(){
        if (this.ventana.getjTablaAlumnos().getSelectedRows().length < 1) {   //si se selecciona menos de un alumno se resalta en rojo la tabla
            this.ventana.getjTablaAlumnos().setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        }else{
            this.ventana.getjTablaAlumnos().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
    private void colorJurados(){
        if (this.ventana.getjTablaJurado().getSelectedRows().length < 3 ||this.ventana.getjTablaJurado().getSelectedRows().length > 3) {   //si se selecciona menos o mas de 3 jurados se resalta en rojo la tabla
                this.ventana.getjTablaJurado().setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        }else{
            this.ventana.getjTablaJurado().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
    private void colorCalendarios(){
        if (this.ventana.getjFechaAprobacion().getCalendar() == null) {
            this.ventana.getjFechaAprobacion().setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        }else{
            this.ventana.getjFechaAprobacion().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
        
        if (this.ventana.getjFechaPresentacion().getCalendar() == null) {
            this.ventana.getjFechaPresentacion().setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        }else{
            this.ventana.getjFechaPresentacion().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
}
