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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;


public class ControladorAMTrabajo implements IControladorAMTrabajo{
    private VentanaAMTrabajo ventana;
    GestorTrabajos gsT ;
    GestorPersonas gsP;
    GestorAreas gsA;
    GestorRolesEnTrabajos gsRET;
    GestorAlumnosEnTrabajos gsAET;
    /**
     * Constructor
     * @param ventanaPadre (VentanaAreas en este caso)
     */    
    public ControladorAMTrabajo(Dialog ventanaPadre, Trabajo unTrabajo) {
        gsT = GestorTrabajos.instanciar();
        gsP = GestorPersonas.instanciar();
        gsA = GestorAreas.instanciar();
        gsRET = GestorRolesEnTrabajos.instanciar();
        gsAET = GestorAlumnosEnTrabajos.instanciar();
        this.ventana = new VentanaAMTrabajo(this, ventanaPadre);
        refrescar();
        this.ventana.setTitle(IControladorTrabajos.TRABAJO_NUEVO);
        this.agregarListeners();
        this.ventana.setLocationRelativeTo(null);
        this.ventana.setVisible(true);
        
        
        
    }
    
    private void refrescar() {            //utilizo este metodo para llenar las tablas y los combo box
        
        List<Profesor> listaProfes = new ArrayList<Profesor>();
        List<Alumno> listaAlumnos = new ArrayList<Alumno>();
        List<Area> listaAreas = new ArrayList<Area>();
        
        listaProfes = this.gsP.buscarProfesores(null);  //creo una lista con todos los profesores
        listaAlumnos=this.gsP.buscarAlumnos(null);      //creo una lista con todos los alumnos
        listaAreas=this.gsA.buscarAreas(null);          //creo una lista con todos las areas
        
        
        String matrizA[][] = new String[(listaAreas.size())] [1];   //Lleno la tabla de areas
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

        String profesores[] = new String[listaProfes.size()];           //Con este arreglo de cadenas armare los comboBox
        for (int i = 0; i < listaProfes.size(); i++) {
            profesores[i] = listaProfes.get(i).verApellidos() + "," + listaProfes.get(i).verNombres()  + "," + listaProfes.get(i).verDNI();
        }
        this.ventana.getjComboTutor().setModel(new javax.swing.DefaultComboBoxModel<>(profesores));         //Lleno los comboBox
        this.ventana.getjComboCotutor().setModel(new javax.swing.DefaultComboBoxModel<>(profesores));


        String matrizp[][] = new String[(listaProfes.size())] [2];             //Llena la tabla de Jurados
        for (int i = 0; i < listaProfes.size(); i++) {
            matrizp[i][0] = listaProfes.get(i).verApellidos() + "," + listaProfes.get(i).verNombres() ;
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

        String[][] matrizal = new String[(listaAlumnos.size())] [2];             //Llena la tabla de Alumnos
        for (int i = 0; i < listaAlumnos.size(); i++) {
            matrizal[i][0] = listaAlumnos.get(i).verApellidos() + "," + listaAlumnos.get(i).verNombres();
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
        LocalDate fechaP;
        LocalDate fechaA;
        
        fechaP = obtenerFechaDeJDateChooser(this.ventana.getjFechaPresentacion());
        fechaA = obtenerFechaDeJDateChooser(this.ventana.getjFechaAprobacion());
        
        
        //Areas del trabajo
        List<Area> listaAreas = new ArrayList<Area>();
        int[] seleccionadosA = this.ventana.getjTablaAreas().getSelectedRows();
        
        for (int i: seleccionadosA) {
            listaAreas.add(this.gsA.dameArea(this.ventana.getjTablaAreas().getValueAt(i, 0).toString()));
        }
        
        //Roles En Trabajo
        //Jurados
//        List<Profesor> listaJurados = new ArrayList<Profesor>();
        List<RolEnTrabajo> listaRET = new ArrayList <RolEnTrabajo>();
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
            dniT = Integer.parseInt(this.ventana.getjComboTutor().getSelectedItem().toString().split(",")[2]);
            listaRET.add(gsRET.nuevoRolEnTrabajo(gsP.dameProfesor(dniT), Rol.TUTOR, fechaP));
        }
        //Cotutor
        int dniCo;
        if (this.ventana.getjComboCotutor().getSelectedItem() != null) {
            dniCo = Integer.parseInt(this.ventana.getjComboCotutor().getSelectedItem().toString().split(",")[2]);
            listaRET.add(gsRET.nuevoRolEnTrabajo(gsP.dameProfesor(dniCo), Rol.COTUTOR, fechaP));
        }
        
        
        //Alumnos en trabajo
        List<AlumnoEnTrabajo> listaAET = new ArrayList<AlumnoEnTrabajo>();
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
