/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.personas.modelos;

import gui.interfaces.IGestorPersonas;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Clase para mostrar los alumnos en una tabla
 */
public class ModeloTablaAlumnos extends AbstractTableModel{
    public static final String COLUMNA_APELLIDO = "Apellido/s";
    public static final String COLUMNA_NOMBRE = "Nombre/s";
    public static final String COLUMNA_DNI = "DNI";
    public static final String COLUMNA_CX = "Cx";
    
    private List<Alumno> alumnos;
    private List<String> nombresColumnas = new ArrayList<>();
    
    /**
    * Constructor
    * @param apellido apellido que se usa para filtrar la búsqueda de alumnos
    */    
    public ModeloTablaAlumnos(String apellido){
        this.nombresColumnas.add(COLUMNA_APELLIDO);
        this.nombresColumnas.add(COLUMNA_NOMBRE);
        this.nombresColumnas.add(COLUMNA_DNI);
        this.nombresColumnas.add(COLUMNA_CX);
        IGestorPersonas gp = GestorPersonas.instanciar();
        this.alumnos = gp.buscarAlumnos(apellido);
    }
    
    /**
    * Obtiene la cantidad de filas de la tabla
    * @return int  - cantidad de filas de la tabla
    */ 
    @Override
    public int getRowCount() {
        return this.alumnos.size();
    }
    
    /**
    * Obtiene la cantidad de columnas de la tabla
    * @return int  - cantidad de columnas de la tabla
    */  
    @Override
    public int getColumnCount() {
        return this.nombresColumnas.size();
    }
    
    /**
    * Obtiene el nombre de una columna
    * @param columna columna sobre la que se quiere obtener el nombre
    * @return String  - nombre de la columna especificada
    */ 
    @Override
    public String getColumnName(int columna) {
        return this.nombresColumnas.get(columna);
    }

    /**
    * Obtiene el valor para la celda especificada
    * @param fila fila de la celda
    * @param columna columna de la celda
    * @return Object  - valor de la celda
    */  
    @Override
    public Object getValueAt(int fila, int columna) {
        Alumno alum = this.alumnos.get(fila);
        switch(columna){
            case 0:
                return alum.verApellidos();
            case 1:
                return alum.verNombres();
            case 2:
                return alum.verDNI();
            default:
                return alum.verCX();
        }
    }
    
    /**
    * Devuelve el alumno correspondiente a la fila especificada dentro de la tabla
    * @param fila fila dentro de la tabla
    * @return Alumno - objeto Alumno correspondiente a la fila que se especifica
    * @see Alumno
    */ 
    public Alumno obtenerAlumno(int fila){
        return this.alumnos.get(fila);
    }
    
}