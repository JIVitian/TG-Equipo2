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
import java.beans.PropertyChangeEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;


public class ControladorAMTrabajo implements IControladorAMTrabajo{
    private final VentanaAMTrabajo ventana;
    private Trabajo unTrabajo;
    IGestorTrabajos gsT ;
    IGestorPersonas gsP;
    IGestorAreas gsA;
    IGestorRolesEnTrabajos gsRET;
    IGestorAlumnosEnTrabajos gsAET;
    
    /**
     * Constructor
     * Muestra la ventana de AMTrabajos de forma modal
     * @param ventanaPadre (VentanaTrabajos en este caso)
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
    
    /**
     * Clase anonima creada para darle un formato a las comboBox de Tutor y Cotutor
     */ 
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
    
    /**
     * Inicializa los campos de VentanaAMTrabajos 
     * segun se desee crear un nuevo trabajo o finalizar uno ya existente
     */ 
    private void setearVentana(){
        if (this.unTrabajo == null) {
            this.ventana.setTitle(IControladorTrabajos.TRABAJO_NUEVO);
            
            //FechaPresentacion
            //Pone automaticamente como sugerencia la fecha de hoy
            LocalDate fechaAp = LocalDate.now();
            Date fechaAprobacion = Date.from(fechaAp.atStartOfDay(ZoneId.systemDefault()).toInstant());
            this.ventana.getjFechaAprobacion().setDate(fechaAprobacion);
            
            //Deshabilito el dateChooser de fechaFinalizacion
            this.ventana.getjFechaFinalizacion().setEnabled(false);
            
            ((ModeloComboTutorCotutor)this.ventana.getjComboTutor().getModel()).seleccionarTutorCotutor(null);
            ((ModeloComboTutorCotutor)this.ventana.getjComboCotutor().getModel()).seleccionarTutorCotutor(null);
            
        } else {
            this.ventana.setTitle(IControladorTrabajos.TRABAJO_MODIFICAR);
            
            //Titulo
            this.ventana.verTxtTitulo().setText(this.unTrabajo.verTitulo());
            this.ventana.verTxtTitulo().setEnabled(false);
            
            //Duracion
            this.ventana.verTxtDuracion().setText(Integer.toString(this.unTrabajo.verDuracion()));
            this.ventana.verTxtDuracion().setEnabled(false);
            
            //FechaPresentacion
            //Convierto de LocalDate a Date
            Date fechaPresentacion = Date.from(this.unTrabajo.verFechaPresentacion().atStartOfDay(ZoneId.systemDefault()).toInstant());
            //Muestro y desabilito el dateChooser de fechaDesde
            this.ventana.getjFechaPresentacion().setDate(fechaPresentacion);
            this.ventana.getjFechaPresentacion().setEnabled(false);
            
            //FechaAprobacion
            //Convierto de LocalDate a Date
            Date fechaAprobacion = Date.from(this.unTrabajo.verFechaAprobacion().atStartOfDay(ZoneId.systemDefault()).toInstant());
            //Muestro y desabilito el dateChooser de fechaDesde
            this.ventana.getjFechaAprobacion().setDate(fechaAprobacion);
            this.ventana.getjFechaAprobacion().setEnabled(false);

            //FechaFinalizacion
            //Pone automaticamente como sugerencia la fecha de hoy
            LocalDate fechaHoy = LocalDate.now();
            Date fechaActual = Date.from(fechaHoy.atStartOfDay(ZoneId.systemDefault()).toInstant());
            this.ventana.getjFechaFinalizacion().setDate(fechaActual);
            
            //Areas
            List<Area> listaAreas = this.unTrabajo.verAreas();
            this.gsA.buscarAreas(null);
            for (int i = 0; i < listaAreas.size(); i++) {
                for (int j = 0; j < this.gsA.buscarAreas(null).size(); j++) {
                    if (this.gsA.buscarAreas(null).get(j).equals(listaAreas.get(i))) {
                        this.ventana.getjTablaAreas().addRowSelectionInterval(j, j);
                    }
                }
            }
            this.ventana.getjTablaAreas().setEnabled(false);
            
            //Alumno
            List<AlumnoEnTrabajo> listaAET = this.unTrabajo.verAlumnos();
            List<Alumno> listaAlumnos = this.gsP.buscarAlumnos(null);
            
            for (int i = 0; i < listaAET.size(); i++) {
                for (int j = 0; j < listaAlumnos.size(); j++) {
                    if (listaAlumnos.get(j).equals(listaAET.get(i).verAlumno())) {
                        this.ventana.getjTablaAlumnos().addRowSelectionInterval(j, j);
                    }
                }
            }
            this.ventana.getjTablaAlumnos().setEnabled(false);
            
            //Tutor
            ((ModeloComboTutorCotutor)this.ventana.getjComboTutor().getModel()).seleccionarTutorCotutor(this.unTrabajo.verTutorOCotutor(Rol.TUTOR));
            this.ventana.getjComboTutor().setEnabled(false);
            
            //Cotutor
            ((ModeloComboTutorCotutor)this.ventana.getjComboCotutor().getModel()).seleccionarTutorCotutor(this.unTrabajo.verTutorOCotutor(Rol.COTUTOR));
            this.ventana.getjComboCotutor().setEnabled(false);
            
            //Jurado
            List<Profesor> listaJurado = this.unTrabajo.verJurado();
            List<Profesor> listaProfesores = this.gsP.buscarProfesores(null);
            
            for (int i = 0; i < listaJurado.size(); i++) {
                for (int j = 0; j < listaProfesores.size(); j++) {
                    if (listaProfesores.get(j).equals(listaJurado.get(i))) {
                        this.ventana.getjTablaJurado().addRowSelectionInterval(j, j);
                    }
                }
            }
            this.ventana.getjTablaJurado().setEnabled(false);
        }
    }
    
    /**
     * Actualiza los elementos de la VentanaAMTrabajos
     * Esto incluye las tablas de Areas, Alumnos y Jurados
     * Ademas llena los comboBox de Tutor y Cotutor
     */ 
    private void refrescar() {            //utilizo este metodo para llenar las tablas y los combo box
        List<Profesor> listaProfesores = this.gsP.buscarProfesores(null);  //creo una lista con todos los profesores
        List<Alumno> listaAlumnos = this.gsP.buscarAlumnos(null);      //creo una lista con todos los alumnos
        List<Area> listaAreas = this.gsA.buscarAreas(null);          //creo una lista con todos las areas
        
        //Lleno la tabla de areas
        String[][] matrizAreas = new String[(listaAreas.size())] [1];
        for (int i = 0; i < listaAreas.size(); i++) {
            matrizAreas[i][0] = listaAreas.get(i).verNombre();
        }
        this.ventana.getjTablaAreas().setModel(new javax.swing.table.DefaultTableModel(
            matrizAreas,
            new String [] {
                "Area"
            }
        ){
            @Override       //por defecto, el modelo Default de tabla si permite editar el contenido de las celdas
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

//        //Con este arreglo de cadenas armare los comboBox
//        String profesores[] = new String[listaProfes.size()];
//        for (int i = 0; i < listaProfes.size(); i++) {
//            profesores[i] = listaProfes.get(i).verApellidos() + ", " + listaProfes.get(i).verNombres()  + " - " + listaProfes.get(i).verDNI();
//        }
//        
//        //Lleno los comboBox
//        this.ventana.getjComboTutor().setModel(new javax.swing.DefaultComboBoxModel<>(profesores));         
//        this.ventana.getjComboCotutor().setModel(new javax.swing.DefaultComboBoxModel<>(profesores));
        this.ventana.getjComboTutor().setModel(new ModeloComboTutorCotutor());
        this.ventana.getjComboCotutor().setModel(new ModeloComboTutorCotutor());

        //Llena la tabla de Jurados
        String[][] matrizProfesores = new String[(listaProfesores.size())] [2];
        for (int i = 0; i < listaProfesores.size(); i++) {
            matrizProfesores[i][0] = listaProfesores.get(i).verApellidos() + ", " + listaProfesores.get(i).verNombres() ;
        }
        
        for (int i = 0; i < listaProfesores.size(); i++) {
            matrizProfesores [i][1] = Integer.toString(listaProfesores.get(i).verDNI());
        }
        
        this.ventana.getjTablaJurado().setModel(new javax.swing.table.DefaultTableModel(
            matrizProfesores,
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
        String[][] matrizAlumnos = new String[(listaAlumnos.size())] [2];             
        for (int i = 0; i < listaAlumnos.size(); i++) {
            matrizAlumnos[i][0] = listaAlumnos.get(i).verApellidos() + ", " + listaAlumnos.get(i).verNombres();
        }
        for (int i = 0; i < listaAlumnos.size(); i++) {
            matrizAlumnos [i][1] = listaAlumnos.get(i).verCX();
        }
        
        this.ventana.getjTablaAlumnos().setModel(new javax.swing.table.DefaultTableModel(
            matrizAlumnos,
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
    
    /**
     * Finaliza el trabajo que se haya seleccionado
     */ 
    private void modificarTrabajo(){
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
    
    /**
     * Crea un nuevo trabajo donde sus parametros seran los valores leidos 
     * de los campos de la VentanaAMTrabajos
     */ 
    private void nuevoTrabajo(){
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
        LocalDate fechaPres = obtenerFechaDeJDateChooser(this.ventana.getjFechaPresentacion());
        LocalDate fechaAp = obtenerFechaDeJDateChooser(this.ventana.getjFechaAprobacion());
        
        //Areas del trabajo
        List<Area> listaAreas = new ArrayList<>();
        int[] seleccionadosA = this.ventana.getjTablaAreas().getSelectedRows();
        
        for (int i: seleccionadosA) {
            listaAreas.add(this.gsA.dameArea(this.ventana.getjTablaAreas().getValueAt(i, 0).toString()));
        }
        
        //Roles En Trabajo
        List<RolEnTrabajo> listaRET = new ArrayList<>();

        //Jurados
        int[] seleccionadosJ = this.ventana.getjTablaJurado().getSelectedRows();
        for(int i : seleccionadosJ){
            int dniJurado ;
            dniJurado = Integer.parseInt(this.ventana.getjTablaJurado().getValueAt(i, 1).toString());
            listaRET.add(gsRET.nuevoRolEnTrabajo(gsP.dameProfesor(dniJurado), Rol.JURADO, fechaAp));
        }
        //Tutor
        if (this.ventana.getjComboTutor().getSelectedItem() != null) {
            listaRET.add(gsRET.nuevoRolEnTrabajo((Profesor) this.ventana.getjComboTutor().getSelectedItem(), Rol.TUTOR, fechaPres));
        }
        //Cotutor
        if (this.ventana.getjComboCotutor().getSelectedItem() != null) {
            listaRET.add(gsRET.nuevoRolEnTrabajo((Profesor) this.ventana.getjComboCotutor().getSelectedItem(), Rol.COTUTOR, fechaPres));
        }
        
        //Alumnos en trabajo
        List<AlumnoEnTrabajo> listaAET = new ArrayList<>();
        
        int[] seleccionadosAl = this.ventana.getjTablaAlumnos().getSelectedRows();
        for(int i : seleccionadosAl){
            String cxAl = this.ventana.getjTablaAlumnos().getValueAt(i, 1).toString();
            listaAET.add(gsAET.nuevoAlumnoEnTrabajo(gsP.dameAlumno(cxAl), fechaPres));
        }
        
        //Tratar de crearlo
        String resultado = gsT.nuevoTrabajo(titulo, duracion, fechaPres, fechaAp, listaAreas, listaRET, listaAET);
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
    
    /**
     * Agrega listeners a cada elemento de la ventana, recuadrandolo
     * Recuadra en rojo cuando sea incorrecto
     * Recuadra en gris cuando sea valido
     */ 
    private void agregarListeners(){
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
        
        //Listener fechaAprobacion
        this.ventana.getjFechaAprobacion().getDateEditor().addPropertyChangeListener((PropertyChangeEvent e) -> {
            if ("date".equals(e.getPropertyName())) {
                colorCalendarios();
            }
        });
        
        //Listener fechaPresentacion
        this.ventana.getjFechaPresentacion().getDateEditor().addPropertyChangeListener((PropertyChangeEvent e) -> {
            if ("date".equals(e.getPropertyName())) {
                colorCalendarios();
            }
        });
        
        //Listener fechaFinalizacion
        this.ventana.getjFechaFinalizacion().getDateEditor().addPropertyChangeListener((PropertyChangeEvent e) -> {
            if ("date".equals(e.getPropertyName())) {
                colorCalendarios();
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
    
    /**
     * Obtiene la fecha de un campo JDateChooser
     * Si no hay seleccionada una fecha devuelve null
     * @param dateChooser campo JDateChooser
     * @return LocalDate - fecha de un campo JDateChooser
     */
    private LocalDate obtenerFechaDeJDateChooser(JDateChooser dateChooser) {        //Convierte a LocalDate la fecha obtenida del JDateChooser
        Date date;
        if (dateChooser.getCalendar() != null) {
            date = dateChooser.getCalendar().getTime();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        else
            return null;
    }
    
    /**
     * Le da color al borde del campo txtTitulo de VentanaAMTrabajos
     */
    private void colorTxtTitulo(){
        if (this.ventana.getTxtTitulo().getText().trim().isEmpty()) {
            this.ventana.getTxtTitulo().setBorder(BorderFactory.createLineBorder(Color.RED, 1)); //si el campo de texto esta vacio,se resalta en rojo
        }else{
            this.ventana.getTxtTitulo().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); //si el campo no esta vacio, elborde se vuleve gris(no verde o algo por el estilo,pues no se esta seguro que el valor es correcto
        }
    }
    
    /**
     * Le da color al borde del campo txtDuracion de VentanaAMTrabajos
     */
    private void colorTxtDuracion(){
        if (this.ventana.getTxtDuracion().getText().trim().isEmpty()) {
            this.ventana.getTxtDuracion().setBorder(BorderFactory.createLineBorder(Color.RED, 1)); //si el campo de texto esta vacio,se resalta en rojo
        }else{
            this.ventana.getTxtDuracion().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); //si el campo no esta vacio, elborde se vuleve gris(no verde o algo por el estilo,pues no se esta seguro que el valor es correcto
        }
    }
    
    /**
     * Le da color a la fila seleccionada del campo tablaAreas de VentanaAMTrabajos
     */
    private void colorAreas(){
        if (this.ventana.getjTablaAreas().getSelectedRows().length < 1) {   //si se selecciona menos de un area se resalta en rojo la tabla
            this.ventana.getjTablaAreas().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }else{
            this.ventana.getjTablaAreas().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
    /**
     * Le da color a la fila seleccionada del campo tablaAlumnos de VentanaAMTrabajos
     */
    private void colorAlumnos(){
        if (this.ventana.getjTablaAlumnos().getSelectedRows().length < 1) {   //si se selecciona menos de un alumno se resalta en rojo la tabla
            this.ventana.getjTablaAlumnos().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }else{
            this.ventana.getjTablaAlumnos().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
    /**
     * Le da color a la fila seleccionada del campo tablaJurados de VentanaAMTrabajos
     */
    private void colorJurados(){
        if (this.ventana.getjTablaJurado().getSelectedRows().length < 3 ||this.ventana.getjTablaJurado().getSelectedRows().length > 3) {   //si se selecciona menos o mas de 3 jurados se resalta en rojo la tabla
                this.ventana.getjTablaJurado().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }else{
            this.ventana.getjTablaJurado().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }
    
    /**
     * Le da color al borde de los campos dateChooser de VentanaAMTrabajos
     */
    private void colorCalendarios(){
        if (this.ventana.getjFechaPresentacion().getCalendar() == null) {
            this.ventana.getjFechaPresentacion().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }else{
            this.ventana.getjFechaPresentacion().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
        
        if (this.ventana.getjFechaAprobacion().getCalendar() == null || this.ventana.getjFechaPresentacion().getDate().compareTo(this.ventana.getjFechaAprobacion().getDate()) > 0) {
            this.ventana.getjFechaAprobacion().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }else{
            this.ventana.getjFechaAprobacion().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
        
        if(this.ventana.getjFechaFinalizacion().isEnabled()){
            if(this.ventana.getjFechaFinalizacion().getCalendar() == null || this.ventana.getjFechaAprobacion().getDate().compareTo(this.ventana.getjFechaFinalizacion().getDate()) > 0){
                this.ventana.getjFechaFinalizacion().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            }else{
                this.ventana.getjFechaFinalizacion().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            }
        }
    }
}
