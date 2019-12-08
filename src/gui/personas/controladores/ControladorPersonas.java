/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.personas.controladores;

import gui.interfaces.IControladorAMAlumno;
import gui.interfaces.IControladorAMProfesor;
import gui.interfaces.IControladorPersonas;
import gui.interfaces.IGestorPersonas;
import gui.personas.modelos.Alumno;
import gui.personas.modelos.GestorPersonas;
import gui.personas.modelos.ModeloTablaAlumnos;
import gui.personas.modelos.ModeloTablaProfesores;
import gui.personas.modelos.Profesor;
import gui.personas.vistas.VentanaPersonas;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Ernesto
 */
public class ControladorPersonas implements IControladorPersonas{
    private VentanaPersonas ventana;
    private int alumSeleccionado;
    private int profSeleccionado;
    private String ERROR_ALUMNO = "¡No hay alumno seleccionado! No se han realizado cambios.";
    private String ERROR_PROFESOR = "¡No hay profesor seleccionado! No se han realizado cambios.";
    
    // Sirve para manejar los profesores y los alumnos seleccionados.
    private String operacionAlumno;
    private String operacionProfesor;

    /**
     * Constructor
     * Muestra la ventana de personas de forma modal
     * @param ventanaPadre ventana padre (VentanaPrincipal en este caso)
     */
    public ControladorPersonas(Frame ventanaPadre) {
        this.ventana = new VentanaPersonas(this, ventanaPadre);
        this.ventana.setTitle(TITULO);
        this.ventana.setLocationRelativeTo(null);
        this.ventana.setVisible(true);
    }

    /**
     * Acción a ejecutar cuando se hace clic en el botón Nuevo de Profesores
     * @param evt evento
     */
    @Override
    public void btnNuevoProfesorClic(ActionEvent evt) {
//        JTable tablaProfesores = this.ventana.verTablaProfesores();
//        this.profSeleccionado = tablaProfesores.getSelectedRow();
        this.operacionProfesor = OPERACION_ALTA_PROFESOR;
        IControladorAMProfesor controlador = new ControladorAMProfesor(this.ventana, null);
    }
    
    /**
     * Acción a ejecutar cuando se hace clic en el botón Nuevo de Alumnos
     * @param evt evento
     */
    @Override
    public void btnNuevoAlumnoClic(ActionEvent evt) {
        this.operacionAlumno = OPERACION_ALTA_ALUMNO;
        IControladorAMAlumno controlador = new ControladorAMAlumno(this.ventana, null);
    }

    /**
     * Acción a ejecutar cuando se hace clic en el botón Modificar de Profesores
     * @param evt evento
     */
    @Override
    public void btnModificarProfesorClic(ActionEvent evt) {
        Profesor prof = this.obtenerProfesorSeleccionado();
        if(prof == null){
          JOptionPane.showMessageDialog(null, ERROR_PROFESOR, IControladorPersonas.TITULO, JOptionPane.WARNING_MESSAGE);
        }
        else{
            this.operacionProfesor = OPERACION_MODIFICACION_PROFESOR;
            IControladorAMProfesor controlador = new ControladorAMProfesor(this.ventana, prof);
        }
    }

    /**
     * Acción a ejecutar cuando se hace clic en el botón Modificar de Alumnos
     * @param evt evento
     */
    @Override
    public void btnModificarAlumnoClic(ActionEvent evt) {
        Alumno alum = this.obtenerAlumnoSeleccionado();      
        if(alum == null){
            JOptionPane.showMessageDialog(null, ERROR_ALUMNO, IControladorPersonas.TITULO, JOptionPane.WARNING_MESSAGE);
        }
        else{
            this.operacionAlumno = OPERACION_MODIFICACION_ALUMNO;
            IControladorAMAlumno controlador = new ControladorAMAlumno(this.ventana, alum);
        }
    }

    /**
     * Acción a ejecutar cuando se selecciona el botón Borrar de la parte de profesores
     * @param evt evento
     */ 
    @Override
    public void btnBorrarProfesorClic(ActionEvent evt) {
        Profesor prof = this.obtenerProfesorSeleccionado();
        if(prof != null){
            this.operacionProfesor = OPERACION_BAJA_PROFESOR;
            IGestorPersonas gestor = GestorPersonas.instanciar();
            int opcion = JOptionPane.showConfirmDialog(null, CONFIRMACION_PROFESOR, TITULO, JOptionPane.YES_NO_OPTION);
            if(opcion == JOptionPane.YES_OPTION){
                String resultado = gestor.borrarProfesor(prof);
                if(!resultado.equals(IGestorPersonas.EXITO_PROFESORES)){
                    gestor.cancelarProfesor();
                    JOptionPane.showMessageDialog(null, resultado, TITULO, JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                gestor.cancelarProfesor();
            }
        }
        else{
            JOptionPane.showMessageDialog(null, ERROR_PROFESOR, IControladorPersonas.TITULO, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Acción a ejecutar cuando se selecciona el botón Borrar de la parte de alumnos
     * @param evt evento
     */ 
    @Override
    public void btnBorrarAlumnoClic(ActionEvent evt) {
        Alumno alum = this.obtenerAlumnoSeleccionado();
        if(alum != null){
            this.operacionAlumno = OPERACION_BAJA_ALUMNO;
            IGestorPersonas gestor = GestorPersonas.instanciar();
            int opcion = JOptionPane.showConfirmDialog(null, CONFIRMACION_ALUMNO, TITULO, JOptionPane.YES_NO_OPTION);
            if(opcion == JOptionPane.YES_OPTION){
                String resultado = gestor.borrarAlumno(alum);
                if(!resultado.equals(IGestorPersonas.EXITO_ALUMNOS)){
                    gestor.cancelarAlumno();
                    JOptionPane.showMessageDialog(null, resultado, TITULO, JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                gestor.cancelarAlumno();
            }
        }
        else{
            JOptionPane.showMessageDialog(null, ERROR_ALUMNO, IControladorPersonas.TITULO, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Acción a ejecutar cuando se hace clic en el botón Volver
     * @param evt evento
     */ 
    @Override
    public void btnVolverClic(ActionEvent evt) {
        this.ventana.dispose();
    }

    /**
     * Acción a ejecutar cuando se selecciona el botón Buscar de profesores
     * @param evt evento
     */  
    @Override
    public void btnBuscarProfesorClic(ActionEvent evt) {
        this.buscarProfesor();
    }

    /**
     * Acción a ejecutar cuando se selecciona el botón Buscar de alumnos
     * @param evt evento
     */  
    @Override
    public void btnBuscarAlumnoClic(ActionEvent evt) {
        this.buscarAlumno();
    }

    /**
     * Acción a ejecutar cuando se presiona una tecla en el campo txtBuscarProfesor
     * @param evt evento
     */ 
    @Override
    public void txtApellidosProfesorPresionarTecla(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
            this.buscarProfesor();
    }

    /**
     * Acción a ejecutar cuando se presiona una tecla en el campo txtBuscarAlumno
     * @param evt evento
     */ 
    @Override
    public void txtApellidosAlumnoPresionarTecla(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
            this.buscarAlumno();
    }
    
    /**
     * Obtiene el profesor seleccionado en la tabla de profesores
     * Si no hay un profesor seleccionado, devuelve null
     * @return Profesor  - profesor seleccionado
     */
    private Profesor obtenerProfesorSeleccionado(){
        JTable tablaProfesores = this.ventana.verTablaProfesores();
        if(tablaProfesores.getSelectedRow() != -1){
            ModeloTablaProfesores mtp = (ModeloTablaProfesores)tablaProfesores.getModel();
            this.profSeleccionado = tablaProfesores.getSelectedRow();
            return mtp.obtenerProfesor(this.profSeleccionado);
        }
        else{
            this.profSeleccionado = -1;
            return null;
        }
    }
    
    /**
     * Obtiene el alumno seleccionado en la tabla de profesores
     * Si no hay un alumno seleccionado, devuelve null
     * @return Alumno  - alumno seleccionado
     */
    private Alumno obtenerAlumnoSeleccionado(){
        JTable tablaAlumnos = this.ventana.verTablaAlumnos();
        if(tablaAlumnos.getSelectedRow() != -1){
            ModeloTablaAlumnos mta = (ModeloTablaAlumnos)tablaAlumnos.getModel();
            this.alumSeleccionado = tablaAlumnos.getSelectedRow();
            return mta.obtenerAlumno(this.alumSeleccionado);
        }
        else{
            this.alumSeleccionado = -1;
            return null;
        }
    }
    
    /**
     * Muestra en la tabla los profesores cuyo apellido coincidan con el apellido especificado
     */
    private void buscarProfesor(){
        String apellido;
        if(this.ventana.verTxtApellidoProfesor().getText().trim().isEmpty())
            apellido = null;
        else
            apellido = this.ventana.verTxtApellidoProfesor().getText().trim();
        ModeloTablaProfesores mtp = new ModeloTablaProfesores(apellido);
        JTable tablaProfesores = this.ventana.verTablaProfesores();
        tablaProfesores.setModel(mtp);
        if(mtp.getRowCount() > 0)
            tablaProfesores.setRowSelectionInterval(0, 0);
        this.establecerPropiedadesTablaProfesores();
    }
    
     /**
     * Muestra en la tabla los alumnos cuyo apellido coincidan con el apellido especificado
     */
    private void buscarAlumno(){
        String apellido;
        if(this.ventana.verTxtApellidoAlumno().getText().trim().isEmpty())
            apellido = null;
        else
            apellido = this.ventana.verTxtApellidoAlumno().getText().trim();
        ModeloTablaAlumnos mta = new ModeloTablaAlumnos(apellido);
        JTable tablaAlumnos = this.ventana.verTablaAlumnos();
        tablaAlumnos.setModel(mta);
        if(mta.getRowCount() > 0)
            tablaAlumnos.setRowSelectionInterval(0, 0);
        this.establecerPropiedadesTablaAlumnos();
    }
    
    /**
     * Acción a ejecutar cuando la ventana obtenga el foco
     * @param evt evento
     */
    @Override
    public void ventanaGanaFoco(WindowEvent evt) {
        
        //La ventana gana el foco cuando:
        //  1. Se presiona el botón "Personas" en la ventana principal
	//  2. Se vuelve de la ventana de alta de una persona
        //  3. Se vuelve de borrar una persona

        //1.  Implica que las tablas no tiene asignado un ModeloTablaAlumnos y un ModeloTablaProfesores
        //    Hay que asignarles, si alguna tiene filas, hay que seleccionar la primera

        //2. y 3. Implica que las tablas ya tienen asignado un ModeloTablaAlumnos y un ModeloTablaProfesores
        //2.  Se puede volver:
            //2.1 Sin haber creado ningúna persona: seleccionar la persona que estaba seleccionada
            //2.2 Habiendo creado una persona: seleccionar la persona recién creada
            
        //3. Se puede volver:
            //3.1 Sin haber borrado una persona: seleccionar la persona que estaba seleccionada
            //3.2 Habiendo borrado una persona: si hay filas, seleccionar la primera
        
        JTable tablaAlumnos = this.ventana.verTablaAlumnos();
        if(tablaAlumnos.getModel() instanceof ModeloTablaAlumnos){
            this.seleccionarAlumnoEnTabla(tablaAlumnos);
        }
        else{
            this.inicializarTablaAlumnos(tablaAlumnos);
        }
        this.operacionAlumno = OPERACION_NINGUNA_ALUMNO;
            
        JTable tablaProfesores = this.ventana.verTablaProfesores();
        if(tablaProfesores.getModel() instanceof ModeloTablaProfesores){
            this.seleccionarProfesorEnTabla(tablaProfesores);
        }
        else{
            this.inicializarTablaProfesores(tablaProfesores);
        }
        this.operacionProfesor = OPERACION_NINGUNA_PROFESOR;
        
        this.establecerPropiedadesTablaAlumnos(); 
        this.estadoBtnsModificarEliminarAlumno(tablaAlumnos);
        this.establecerPropiedadesTablaProfesores();
        this.estadoBtnsModificarEliminarProfesor(tablaProfesores);
    }
    
    /**
     * Habilita o deshabilita los botones para modificar o eliminar profesores si no hay ninguno
     * @param tablaProfesores tabla de profesores
     */
    private void estadoBtnsModificarEliminarProfesor(JTable tablaProfesores){
        if(tablaProfesores.getRowCount() > 0){
            this.ventana.btnModificarProfesor().setEnabled(true);
            this.ventana.btnBorrarProfesor().setEnabled(true);
        }
        else{
            this.ventana.btnModificarProfesor().setEnabled(false);
            this.ventana.btnBorrarProfesor().setEnabled(false);
        }
    }
    
    /**
     * Habilita o deshabilita los botones para modificar o eliminar profesores si no hay ninguno
     * @param tablaProfesores tabla de alumnos
     */
    private void estadoBtnsModificarEliminarAlumno(JTable tablaProfesores){
        if(tablaProfesores.getRowCount() > 0){
            this.ventana.btnModificarAlumno().setEnabled(true);
            this.ventana.btnBorrarAlumno().setEnabled(true);
        }
        else{
            this.ventana.btnModificarAlumno().setEnabled(false);
            this.ventana.btnBorrarAlumno().setEnabled(false);
        }
    }
    
    /**
     * Inicializa la tabla de alumnos cuando se muestra la ventana por primera vez
     * @param tablaAlumnos tabla de alumnos.
     */
    private void inicializarTablaAlumnos(JTable tablaAlumnos){
        ModeloTablaAlumnos mta = new ModeloTablaAlumnos("");
        tablaAlumnos.setModel(mta);
        tablaAlumnos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //se establece que solo se podrá seleccionar una sola fila;
        
        if(mta.getRowCount() > 0){
            this.alumSeleccionado = 0;
            tablaAlumnos.setRowSelectionInterval(this.alumSeleccionado, this.alumSeleccionado);
        }
        else
            this.alumSeleccionado = -1;
    }
    
    /**
     * Inicializa la tabla de profesores cuando se muestra la ventana por primera vez
     * @param tablaProfesores tabla de profesores.
     */
    private void inicializarTablaProfesores(JTable tablaProfesores){
        ModeloTablaProfesores mtp = new ModeloTablaProfesores("");
        tablaProfesores.setModel(mtp);
        tablaProfesores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //se establece que solo se podrá seleccionar una sola fila;
        
        if(mtp.getRowCount() > 0){
            this.profSeleccionado = 0;
            tablaProfesores.setRowSelectionInterval(this.profSeleccionado, this.profSeleccionado);
        }
        else
            this.profSeleccionado = -1;
    }
    
    /**
     * Selecciona una fila en la tabla tablaAlumnos según la operación realizada
     * @param tablaAlumnos  tabla de alumnos
     */
    private void seleccionarAlumnoEnTabla(JTable tablaAlumnos){
        IGestorPersonas gestor = GestorPersonas.instanciar();
        ModeloTablaAlumnos mta = (ModeloTablaAlumnos)tablaAlumnos.getModel();
        if(this.operacionAlumno.equals(OPERACION_ALTA_ALUMNO)){ //se vuelve de la ventana AMAlumno
            if(gestor.verUltimoAlumno() == -1){ //no se creó ningun Alumno: se selecciona el que estaba seleccionado
                if(this.alumSeleccionado != -1){ //hay filas
                    tablaAlumnos.setRowSelectionInterval(this.alumSeleccionado,this.alumSeleccionado);
                }
                
            }else{ //se creó  un alumno: lo selecciona
                //mta.fireTableDataChanged(); //se refresca la tabla
                mta = new ModeloTablaAlumnos(null); //alternativa para refrescar la tabla
                tablaAlumnos.setModel(mta);
                tablaAlumnos.setRowSelectionInterval(gestor.verUltimoAlumno(), gestor.verUltimoAlumno());
            }
        }
        else if(this.operacionAlumno.equals(OPERACION_MODIFICACION_ALUMNO)){ //se vuelve de la ventana
            if(gestor.verUltimoAlumno() == -1)
                tablaAlumnos.setRowSelectionInterval(this.alumSeleccionado, this.alumSeleccionado);
            else{ //si se modifica un alumno
                //mta.fireTableDataChanged(); //se refresca la tabla
                mta = new ModeloTablaAlumnos(null); //alternativa para refrescar la tabla
                tablaAlumnos.setModel(mta);
                tablaAlumnos.setRowSelectionInterval(gestor.verUltimoAlumno(), gestor.verUltimoAlumno());
            }
        }
        else if(this.operacionAlumno.equals(OPERACION_BAJA_ALUMNO)){
            if(gestor.verUltimoAlumno() == -1){ //no se borró ningun Alumno, se selecciona el que estaba seleccionado
                tablaAlumnos.setRowSelectionInterval(this.alumSeleccionado, this.alumSeleccionado);
            }
            else{ //se borró un Alumno
                //mta.fireTableDataChanged(); //se refresca la tabla
                mta = new ModeloTablaAlumnos(null); //alternativa para refrescar la tabla
                tablaAlumnos.setModel(mta);
                if(mta.getRowCount() >0){
                    this.alumSeleccionado = 0;
                    tablaAlumnos.setRowSelectionInterval(this.alumSeleccionado, this.alumSeleccionado);
                }
                else
                    this.alumSeleccionado = -1;
            }
        }
    }
    
    /**
     * Selecciona una fila en la tabla tablaProfesores según la operación realizada
     * @param tablaProfesores  tabla de profesores
     */
    private void seleccionarProfesorEnTabla(JTable tablaProfesores){
        IGestorPersonas gestor = GestorPersonas.instanciar();
        ModeloTablaProfesores mtp = (ModeloTablaProfesores)tablaProfesores.getModel();
        if(this.operacionProfesor.equals(OPERACION_ALTA_PROFESOR)){ //se vuelve de la ventana AMProfesor
            if(gestor.verUltimoProfesor() == -1){ //no se creó ningun profesor: se selecciona el que estaba seleccionado
                if(this.profSeleccionado != -1){ //hay filas
                    tablaProfesores.setRowSelectionInterval(this.profSeleccionado,this.profSeleccionado);
                }
                
            }else{ //se creó  un profesor: lo selecciona
                //mtp.fireTableDataChanged(); //se refresca la tabla
                mtp = new ModeloTablaProfesores(null); //alternativa para refrescar la tabla
                tablaProfesores.setModel(mtp);
                tablaProfesores.setRowSelectionInterval(gestor.verUltimoProfesor(), gestor.verUltimoProfesor());
            }
        }
        else if(this.operacionProfesor.equals(OPERACION_MODIFICACION_PROFESOR)){ //se vuelve de la ventana
            if(gestor.verUltimoProfesor() == -1)
                tablaProfesores.setRowSelectionInterval(this.profSeleccionado, this.profSeleccionado);
            else{ //si se modifica un profesor
                //mtp.fireTableDataChanged(); //se refresca la tabla
                mtp = new ModeloTablaProfesores(null); //alternativa para refrescar la tabla
                tablaProfesores.setModel(mtp);
                tablaProfesores.setRowSelectionInterval(gestor.verUltimoProfesor(), gestor.verUltimoProfesor());
            }
        }
        else if(this.operacionProfesor.equals(OPERACION_BAJA_PROFESOR)){
            if(gestor.verUltimoProfesor() == -1){ //no se borró ningun profesor, se selecciona el que estaba seleccionado
                tablaProfesores.setRowSelectionInterval(this.profSeleccionado, this.profSeleccionado);
            }
            else{ //se borró un profesor
                //mtp.fireTableDataChanged(); //se refresca la tabla
                mtp = new ModeloTablaProfesores(null); //alternativa para refrescar la tabla
                tablaProfesores.setModel(mtp);
                if(mtp.getRowCount() >0){
                    this.profSeleccionado = 0;
                    tablaProfesores.setRowSelectionInterval(this.profSeleccionado, this.profSeleccionado);
                }
                else
                    this.profSeleccionado = -1;
            }
        }
    }
    
    /**
     * Metodo encargado de bloquear la personalizacion de columnas por parte del usuario sobre la tabla de profesores
     * Tambien se encarga de centrar los datos en la columna de Documento
     */
    private void establecerPropiedadesTablaProfesores(){
        JTable tablaProfesores = this.ventana.verTablaProfesores();
        tablaProfesores.getTableHeader().setReorderingAllowed(false); //evita que se pueda cambiar de lugar las columnas para la tabla de profesores
        TableColumn columnaProfesorApellido = tablaProfesores.getColumn(ModeloTablaProfesores.COLUMNA_APELLIDO); //Obtengo la columna para Apellido de la tabla de profesores
        TableColumn columnaProfesorNombre = tablaProfesores.getColumn(ModeloTablaProfesores.COLUMNA_NOMBRE); //Obtengo la columna para Nombre de la tabla de profesores
        TableColumn columnaProfesorDNI = tablaProfesores.getColumn(ModeloTablaProfesores.COLUMNA_DNI); //Obtengo la columna para DNI de la tabla de profesores
        TableColumn columnaProfesorCargo = tablaProfesores.getColumn(ModeloTablaProfesores.COLUMNA_CARGO); //Obtengo la columna para Cargo de la tabla de profesores
        
        //A cada columna de profesor se establece que no puede cambiar su ancho, y la columna DNI puede tener un ancho maximo de 65 pixeles
        columnaProfesorApellido.setResizable(false); 
        columnaProfesorNombre.setResizable(false);
        columnaProfesorDNI.setMaxWidth(65);
        columnaProfesorDNI.setResizable(false);
        columnaProfesorCargo.setResizable(false);
        
	//Crea un renderizador de celda personalizado para centrar los datos, solo se la aplica a la columna de DNI
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer(); //se crea el renderizador
        centerRenderer.setHorizontalAlignment( JLabel.CENTER ); //se establecen que los datos serán centrados
        columnaProfesorDNI.setCellRenderer(centerRenderer); //se establece el renderizador para la columna 
    }
    
    /**
     * Metodo encargado de bloquear la personalizacion de columnas por parte del usuario sobre la tabla de alumnos
     * Tambien se encarga de centrar los datos en la columna de Documento
     */
    private void establecerPropiedadesTablaAlumnos(){
        JTable tablaAlumnos = this.ventana.verTablaAlumnos();
        tablaAlumnos.getTableHeader().setReorderingAllowed(false); //evita que se pueda cambiar de lugar las columnas para la tabla de alumnos
        TableColumn columnaAlumnoApellido = tablaAlumnos.getColumn(ModeloTablaAlumnos.COLUMNA_APELLIDO); //Obtengo la columna para Apellido de la tabla de alumnos
        TableColumn columnaAlumnoNombre = tablaAlumnos.getColumn(ModeloTablaAlumnos.COLUMNA_NOMBRE); //Obtengo la columna para Nombre de la tabla de alumnos
        TableColumn columnaAlumnoDNI = tablaAlumnos.getColumn(ModeloTablaAlumnos.COLUMNA_DNI); //Obtengo la columna para DNI de la tabla de alumnos
        TableColumn columnaAlumnoCx = tablaAlumnos.getColumn(ModeloTablaAlumnos.COLUMNA_CX); //Obtengo la columna para Cx de la tabla de alumnos
        
        //A cada columna de alumno se establece que no puede cambiar su ancho, y la columna DNI puede tener un ancho maximo de 65 pixeles
        columnaAlumnoApellido.setResizable(false); 
        columnaAlumnoNombre.setResizable(false);
        columnaAlumnoDNI.setMaxWidth(65);
        columnaAlumnoDNI.setResizable(false);
        columnaAlumnoCx.setResizable(false);
        
	//Crea un renderizador de celda personalizado para centrar los datos, solo se la aplica a la columna de DNI y CX
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer(); //se crea el renderizador
        cr.setHorizontalAlignment( JLabel.CENTER ); //se establecen que los datos serán centrados
        columnaAlumnoDNI.setCellRenderer(cr); //se establece el renderizador para la columna DNI
        columnaAlumnoCx.setCellRenderer(cr); //se establece el renderizador para la columna CX
    }
}
