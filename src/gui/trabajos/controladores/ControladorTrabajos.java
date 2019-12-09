/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.controladores;

import gui.areas.modelos.Area;
import gui.interfaces.IControladorAMTrabajo;
import gui.interfaces.IControladorModificarAlumno;
import gui.interfaces.IControladorModificarProfesor;
import gui.interfaces.IControladorSeminarios;
import gui.interfaces.IControladorTrabajos;
import static gui.interfaces.IControladorTrabajos.OPERACION_NINGUNA;
import static gui.interfaces.IGestorTrabajos.EXITO;
import gui.seminarios.controladores.ControladorSeminarios;
import gui.trabajos.modelos.AlumnoEnTrabajo;
import gui.trabajos.modelos.GestorAlumnosEnTrabajos;
import gui.trabajos.modelos.GestorTrabajos;
import gui.trabajos.modelos.RolEnTrabajo;
import gui.trabajos.modelos.Trabajo;
import gui.trabajos.vistas.VentanaTrabajos;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

public class ControladorTrabajos implements IControladorTrabajos{
    private VentanaTrabajos ventana;
    GestorTrabajos gsT ;
    GestorAlumnosEnTrabajos gsAET ;
    
//sirven para manejar las tbalas
    private int trabajoSeleccionado;
    private int alumnoSeleccionado;
    private int profesorSeleccionado;
    private String operacion;
    private final String ERROR_BORRAR = "No se pudo eliminar el trabajo seleccionado.";
    IControladorSeminarios cSeminarios;
    
    /**
     * Constructor
     * Muestra la ventana de áreas de forma modal
     * @param ventanaPadre ventana padre (VentanaPrincipal en este caso)
     */
    public ControladorTrabajos(Frame ventanaPadre) {
        this.operacion = OPERACION_NINGUNA;
        gsT = GestorTrabajos.instanciar();
        gsAET = GestorAlumnosEnTrabajos.instanciar();
        this.ventana = new VentanaTrabajos(this, ventanaPadre);
        refrescarTrabajos ();
        this.ventana.setLocationRelativeTo(null);
        this.ventana.setVisible(true);
    }
    
    @Override
    public void btnNuevoClic(ActionEvent evt) {
//        this.operacion = OPERACION_ALTA;
        this.trabajoSeleccionado = this.ventana.getTablaTrabajos().getSelectedRow();
        this.alumnoSeleccionado = this.ventana.getTablaAlumnos().getSelectedRow();
        this.profesorSeleccionado = this.ventana.getTablaProfesores().getSelectedRow();
        this.operacion = OPERACION_ALTA;
        IControladorAMTrabajo controlador = new ControladorAMTrabajo(this.ventana, null);
    }

    @Override
    public void btnModificarClic(ActionEvent evt) {
        this.trabajoSeleccionado = this.ventana.getTablaTrabajos().getSelectedRow();
        this.alumnoSeleccionado = this.ventana.getTablaAlumnos().getSelectedRow();
        this.profesorSeleccionado = this.ventana.getTablaProfesores().getSelectedRow();
        
        if (ventana.getTablaTrabajos().getSelectedRow() != -1) {//La notificacion saltara solo si no se encuentra seleccionado un elemento de la tabla 
            Trabajo trabajo = this.trabajoSelecc();
            if (this.trabajoSelecc().verFechaFinalizacion() == null) {
                this.operacion = OPERACION_MODIFICACION;
                ControladorFinalizarTrabajo controlador = new ControladorFinalizarTrabajo(this.ventana, trabajo);
            }else {
                JOptionPane.showMessageDialog(null, "El trabajo ya ha sido finalizado previamente", "", JOptionPane.ERROR_MESSAGE);
                this.operacion = OPERACION_MODIFICACION;
                this.gsT.cancelar();
            }
        }else{
            this.operacion = OPERACION_MODIFICACION;
            this.gsT.cancelar();
            JOptionPane.showMessageDialog(ventana, "Debe seleccionar el Trabajo que desea finalizar.");     
        }
    }

    @Override
    public void btnBorrarClic(ActionEvent evt) {
        if (ventana.getTablaTrabajos().getSelectedRow() != -1) {//La notificacion saltara solo si no se encuentra seleccionado un elemento de la tabla
            Trabajo trabajo = this.trabajoSelecc();
            this.trabajoSeleccionado = this.ventana.getTablaTrabajos().getSelectedRow();
            this.alumnoSeleccionado = this.ventana.getTablaAlumnos().getSelectedRow();
            this.profesorSeleccionado = this.ventana.getTablaProfesores().getSelectedRow();
            this.operacion = OPERACION_BAJA;
            int confirmacion = JOptionPane.showConfirmDialog(ventana, "¿Eliminar Trabajo?");
            
            if (confirmacion == 0) {//Si el usuario elige "Si" se procedera a eliminar el trabajo seleccionada
                String resultado = gsT.borrarTrabajo(trabajo);
                JOptionPane.showMessageDialog(this.ventana, resultado, "", JOptionPane.INFORMATION_MESSAGE);
                if (resultado.equals(EXITO)) {
                    this.trabajoSeleccionado = this.gsT.verUltimoTrabajo();
                }
            } else{
                this.gsT.cancelar();
            }
        }else{
            JOptionPane.showMessageDialog(ventana, "Debe seleccionar el Trabajo que desea borrar.");     
        }
    }

    @Override
    public void btnSeminariosClic(ActionEvent evt) {
        if (ventana.getTablaTrabajos().getSelectedRow() != -1) {//La notificacion saltara solo si no se encuentra seleccionado un elemento de la tabla
            this.operacion = OPERACION_SEMINARIOS;
            this.trabajoSeleccionado = this.ventana.getTablaTrabajos().getSelectedRow();
            this.alumnoSeleccionado = this.ventana.getTablaAlumnos().getSelectedRow();
            this.profesorSeleccionado = this.ventana.getTablaProfesores().getSelectedRow();
            Trabajo trabajo = trabajoSelecc();
            IControladorSeminarios controlador = new ControladorSeminarios (this.ventana, trabajo);
        }
        else {
            JOptionPane.showMessageDialog(ventana, "Debe seleccionar un Trabajo.");
        }
    }

    @Override
    public void btnModificarProfesorClic(ActionEvent evt) {
        this.trabajoSeleccionado = this.ventana.getTablaTrabajos().getSelectedRow();
        this.alumnoSeleccionado = this.ventana.getTablaAlumnos().getSelectedRow();
        this.profesorSeleccionado = this.ventana.getTablaProfesores().getSelectedRow();
        
        if (ventana.getTablaProfesores().getSelectedRow() != -1) {//La notificacion saltara solo si no se encuentra seleccionado un elemento de la tabla 
            Trabajo trabajo = trabajoSelecc();
            RolEnTrabajo profesor = trabajo.verProfesoresConRoles().get(this.ventana.getTablaProfesores().getSelectedRow());
            if(profesor.verRazon()==null){      //Controlo si el profesor ya ha sido finalizado
                this.operacion = OPERACION_PROFESORES;
                IControladorModificarProfesor controlador = new ControladorModificarProfesor(this.ventana, trabajo, profesor);
            }
            else {
                JOptionPane.showMessageDialog(null, "El profesor ya ha sido finalizado", "", JOptionPane.ERROR_MESSAGE);
                this.gsT.cancelar();
                this.operacion = OPERACION_PROFESORES;
            }    
        }
        else {
            this.operacion =OPERACION_PROFESORES;
            this.gsT.cancelar();
            JOptionPane.showMessageDialog(ventana, "Debe seleccionar un Profesor.");
        }
    }
    @Override
    public void btnModificarAlumnoClic(ActionEvent evt) {
        this.trabajoSeleccionado = this.ventana.getTablaTrabajos().getSelectedRow();
        this.alumnoSeleccionado = this.ventana.getTablaAlumnos().getSelectedRow();
        this.profesorSeleccionado = this.ventana.getTablaProfesores().getSelectedRow();
        
        if (ventana.getTablaAlumnos().getSelectedRow() != -1) {//La notificacion saltara solo si un elemento de la tabla se encuentra seleccionado
            Trabajo trabajo = trabajoSelecc();
            AlumnoEnTrabajo alumno = trabajo.verAlumnos().get(this.ventana.getTablaAlumnos().getSelectedRow());
            if (alumno.verRazon() == null) {    //Controlo si el alumno ya ha sido finalizado
                this.operacion = OPERACION_ALUMNOS;
                IControladorModificarAlumno controlador = new ControladorModificarAlumno(this.ventana, trabajo, alumno);
                }
            else {
                JOptionPane.showMessageDialog(null, "El alumno ya ha sido finalizado", "", JOptionPane.ERROR_MESSAGE);
                this.gsT.cancelar();
                this.operacion = OPERACION_ALUMNOS;
            }
        } else {
            this.operacion = OPERACION_ALUMNOS;
            this.gsT.cancelar();
            JOptionPane.showMessageDialog(ventana, "Debe seleccionar un Alumno.");
        }
    }

    /**
     * Acción a ejecutar cuando se selecciona el botón Volver
     * @param evt evento
     */   
    @Override
    public void btnVolverClic(ActionEvent evt) {
        this.ventana.dispose();
    }
    @Override
    public void ventanaGanaFoco(WindowEvent evt) {
        JTable tablaT = this.ventana.getTablaTrabajos();
        
        //----------------------------------------------//
        //La ventana puede ganar foco en distintos casos//
        //----------------------------------------------//
        
        if(this.operacion.equals(OPERACION_NINGUNA) ){      //si se entra a la ventana desde el menu principal
            refrescarTrabajos ();
            if (tablaT.getRowCount()>0) {      //si hay trabajos cargados
                this.trabajoSeleccionado = 0;
                this.alumnoSeleccionado = 0;
                this.profesorSeleccionado = 0;
                tablaT.setRowSelectionInterval(trabajoSeleccionado, trabajoSeleccionado);    //se selecciona el primer trabajo de la tabla trabajo
            }
        }
        
        if (this.operacion.equals(OPERACION_ALTA)){     //se gana foco luego de tratar de crear un trabajo
            if (gsT.verUltimoTrabajo() == -1) {  //no se creó ningún trabajo
                if (this.trabajoSeleccionado != (-1)){ //si habia algo seleecionado anteriormente
                }
            }
            else {  //se creó un trabajo
                this.alumnoSeleccionado = 0;
                this.profesorSeleccionado = 0;
                refrescarTrabajos(); //se refresca la tabla de Trabajos
                tablaT.setRowSelectionInterval(gsT.verUltimoTrabajo(), gsT.verUltimoTrabajo());    //se selecciona el trabajo creado
            }   
        }

        if(this.operacion.equals(OPERACION_MODIFICACION)){  //se vuleve de tratar de finalizar un trabajo.
            if (gsT.verUltimoTrabajo() != -1) {     //se finalizó un trabajo
                this.refrescarTrabajos();    //se refresca la tabla 
                tablaT.setRowSelectionInterval(gsT.verUltimoTrabajo(), gsT.verUltimoTrabajo()); //se selecciona el trabajo finalizado
            }
        }

        if(this.operacion.equals(OPERACION_BAJA)){  //se vuleve de tratar de eliminar un trabajo
            if (gsT.verUltimoTrabajo() != -1) {     //si se borro un trabajo
                this.alumnoSeleccionado = 0;
                this.profesorSeleccionado = 0;
                if (tablaT.getRowCount() > 0){     //si es que queda algun trabajo
                    this.trabajoSeleccionado = 0;
                    this.alumnoSeleccionado = 0;
                    this.profesorSeleccionado = 0;
                    this.refrescarTrabajos();    //se refresca la tabla 
                    tablaT.setRowSelectionInterval(this.trabajoSeleccionado, this.trabajoSeleccionado);     //se selecciona el primer trabajo de la tabla
                }
            }else{  //no se borro ningun trabajo
                tablaT.setRowSelectionInterval(this.trabajoSeleccionado, this.trabajoSeleccionado);     //se selecciona el ultimo trabajo 
            }
        }

        if(this.operacion.equals(OPERACION_SEMINARIOS)){  //se vuleve de la ventana seminarios
            tablaT.setRowSelectionInterval(this.trabajoSeleccionado, this.trabajoSeleccionado);
        }
        
        if(this.operacion.equals(OPERACION_PROFESORES)){  //se vuleve de tratar de finalizar un profesor
            if(this.gsT.verUltimoTrabajo() != -1){      //se finalizo un profesor
                this.refrescarTrabajos();    //se refresca la tabla 
                tablaT.setRowSelectionInterval(this.trabajoSeleccionado, this.trabajoSeleccionado); //se selecciona el trabajo finalizado
            }
        }
        
        if(this.operacion.equals(OPERACION_ALUMNOS)){  //se vuleve de tratar modificar un alumno
            if(this.gsT.verUltimoTrabajo() != -1){      //se finalizo un alumno
                this.refrescarTrabajos();    //se refresca la tabla 
                tablaT.setRowSelectionInterval(this.trabajoSeleccionado, this.trabajoSeleccionado); //se selecciona el trabajo finalizado
            }
        }   
        this.operacion = OPERACION_NINGUNA;
    }
    
    
    private void refrescarTrabajos (){
        List<Trabajo> listaTrabajos = new ArrayList<>();
        listaTrabajos = gsT.buscarTrabajos(null);
        
        
        String matrizt[][]= new String [listaTrabajos.size()][6];   //creo y lleno una matriz que usare en el constructor del DefaultTableModel
        for (int i = 0; i < listaTrabajos.size(); i++) {
            matrizt[i][0] = listaTrabajos.get(i).verTitulo() ;
            matrizt[i][1] = Integer.toString(listaTrabajos.get(i).verDuracion());
            
            String areas = null;
            for(Area area : listaTrabajos.get(i).verAreas()){
                if (areas == null) {
                    areas = area.verNombre();
                }else{
                    areas +=  "-" + area.verNombre();
                }
            }
            matrizt[i][2] = areas ;
            
            matrizt[i][3] = listaTrabajos.get(i).verFechaPresentacion().toString();
            matrizt[i][4] = listaTrabajos.get(i).verFechaAprobacion().toString();
            
            if(listaTrabajos.get(i).verFechaFinalizacion() != null){
                matrizt[i][5] = listaTrabajos.get(i).verFechaFinalizacion().toString();
            } else{
                matrizt[i][5] = "-";
            }
        }
        
        this.ventana.getTablaTrabajos().setModel(new javax.swing.table.DefaultTableModel(
            matrizt,
            new String [] {
                "Titulo", "Duracion", "Areas", "Presentacion", "Aprobacion", "Exposicion"
            }
        ){
            @Override       //por defecto, el modelo Default de tabla si permite editar el contenido de las celdas
            
            public boolean isCellEditable(int row, int column) {
            return false;
        }
        });
        
        this.ventana.getTablaTrabajos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);      //Solo se puede seleccionar un trabajo a la vez
        
        this.ventana.getTablaTrabajos().getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {    //la tabla tiene un listener para saber cuando se cambia la seleccion
            if (!e.getValueIsAdjusting()) {
                if (this.operacion.equals(OPERACION_NINGUNA)) { //se llama este metodo al crear la VentanaTrabajos o al seleccionar una fila el usuario
                    if (trabajoSelecc() != null) {
                        this.alumnoSeleccionado = 0;        //se selecciona la primera fila de alumnos
                        this.profesorSeleccionado = 0;      //se selecciona la primera fila de profesores
                        refrescarProfes(trabajoSelecc());   
                        refrescaralumnos(trabajoSelecc());
                    }
                }else               //se llama este metodo al ganar foco desde una ventana que no es el menu principal
                {                   
                    if (trabajoSelecc() != null) {
                        refrescarProfes(trabajoSelecc());   
                        refrescaralumnos(trabajoSelecc());
                        return;
                    }
                }
            }
        });
        this.ventana.getTablaTrabajos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);      //para que solo se pueda seleccionar una fila a la vez
        this.ventana.getTablaTrabajos().getTableHeader().setReorderingAllowed(false);       // no se pueden reordenar las columnas
    }
    
    
    private void refrescaralumnos (Trabajo trabajo){
        //Tabla Alumnos en trabajo
        
        String matrizAl[][] = new String[trabajo.verAlumnos().size()][6];       //creo y lleno una matriz que usare en el constructor del DefaultTableModel

        for (int i = 0; i < trabajo.verAlumnos().size(); i++) {
            matrizAl[i][0] = trabajo.verAlumnos().get(i).verAlumno().verApellidos();
            matrizAl[i][1] = trabajo.verAlumnos().get(i).verAlumno().verNombres();
            matrizAl[i][2] = trabajo.verAlumnos().get(i).verAlumno().verCX();
            matrizAl[i][3] = trabajo.verAlumnos().get(i).verFechaDesde().toString();

             if( trabajo.verAlumnos().get(i).verFechaHasta() != null){
                 matrizAl[i][4] = trabajo.verAlumnos().get(i).verFechaHasta().toString();
            } else{
            matrizAl[i][4] = "-";
            }

            if( trabajo.verAlumnos().get(i).verRazon() != null){
            matrizAl[i][5] = trabajo.verAlumnos().get(i).verRazon();                
            } else{
            matrizAl[i][5] = "-";
            }
        }
        
        this.ventana.getTablaAlumnos().setModel(new javax.swing.table.DefaultTableModel(
           matrizAl
            ,
            new String [] {
                "Apellidos", "Nombres", "CX", "Desde", "Hasta", "Razón"
            }
        )
        {
            @Override       //por defecto, el modelo Default de tabla si permite editar el contenido de las celdas
            public boolean isCellEditable(int row, int column) {
            return false;
        }
        });
        
        this.ventana.getTablaAlumnos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);      //seteo para que solo se pueda seleccionar una fila a la vez
        this.ventana.getTablaAlumnos().getTableHeader().setReorderingAllowed(false);      //no se pueden reordenar las columnas
        this.ventana.getTablaAlumnos().setRowSelectionInterval(this.alumnoSeleccionado, this.alumnoSeleccionado);     //selecciona un alumno
    }
    
    
    private void refrescarProfes (Trabajo trabajo){
        //Tabla RolesEn Trabajo
        
        String matrizPr[][] = new String[trabajo.verProfesoresConRoles().size()][5];    //creo y lleno una matriz que usare en el constructor del DefaultTableModel

        for (int i = 0; i < trabajo.verProfesoresConRoles().size(); i++) {
            matrizPr[i][0] = trabajo.verProfesoresConRoles().get(i).verProfesor().verApellidos() + "," + trabajo.verProfesoresConRoles().get(i).verProfesor().verNombres() ;
            matrizPr[i][1] = trabajo.verProfesoresConRoles().get(i).verRol().toString();
            matrizPr[i][2] = trabajo.verProfesoresConRoles().get(i).verFechaDesde().toString();

             if( trabajo.verProfesoresConRoles().get(i).verFechaHasta() != null){
                 matrizPr[i][3] = trabajo.verProfesoresConRoles().get(i).verFechaHasta().toString();
            } else{
            matrizPr[i][3] = "-";
            }

            if( trabajo.verProfesoresConRoles().get(i).verRazon() != null){
            matrizPr[i][4] = trabajo.verProfesoresConRoles().get(i).verRazon();                
            } else{
            matrizPr[i][4] = "-";
            }
        }
        this.ventana.getTablaProfesores().setModel(new javax.swing.table.DefaultTableModel(
            matrizPr,
            new String [] {
                "Profesor", "Rol", "Desde", "Hasta", "Razon"
            }
        )
        {
            @Override       //por defecto, el modelo Default de tabla si permite editar el contenido de las celdas
            
            public boolean isCellEditable(int row, int column) {
            return false;
        }
        });
        
        this.ventana.getTablaProfesores().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);      //seteo para que solo se pueda seleccionar una fila a la vez
        this.ventana.getTablaProfesores().getTableHeader().setReorderingAllowed(false);      //no se pueden reordenar las columnas
        this.ventana.getTablaProfesores().setRowSelectionInterval(this.profesorSeleccionado, this.profesorSeleccionado);    //se selecciona un profesor de la tabla
        
    }
    
    
    public Trabajo trabajoSelecc(){
        if (this.ventana.getTablaTrabajos().getSelectedRow()!= -1) {
             return gsT.dameTrabajo(this.ventana.getTablaTrabajos().getValueAt(this.ventana.getTablaTrabajos().getSelectedRow(), 0).toString());
        }
        return null;
    }
}

