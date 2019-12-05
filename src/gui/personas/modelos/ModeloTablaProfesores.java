/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.personas.modelos;

import gui.interfaces.IGestorPersonas;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Clase para mostrar los profesores en una tabla
 */
public class ModeloTablaProfesores extends AbstractTableModel {
    public static final String COLUMNA_APELLIDO = "Apellido/s";
    public static final String COLUMNA_NOMBRE = "Nombre/s";
    public static final String COLUMNA_DNI = "DNI";
    public static final String COLUMNA_CARGO = "Cargo";
    
    private List<Profesor> profesores; 
    private List<String> nombresColumnas = new ArrayList<>();
    
    
    /**
    * Constructor
    * @param apellido apellido que se usa para filtrar la búsqueda de profesores
    */    
    public ModeloTablaProfesores(String apellido){
        this.nombresColumnas.add(COLUMNA_APELLIDO);
        this.nombresColumnas.add(COLUMNA_NOMBRE);
        this.nombresColumnas.add(COLUMNA_DNI);
        this.nombresColumnas.add(COLUMNA_CARGO);
        IGestorPersonas gPersonas = GestorPersonas.instanciar();
        this.profesores = gPersonas.buscarProfesores(apellido);
    }
    
    /**
    * Obtiene la cantidad de filas de la tabla
    * @return int  - cantidad de filas de la tabla
    */    
    @Override
    public int getRowCount() {
        return this.profesores.size();
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
        Profesor prof = this.profesores.get(fila);
        switch(columna){
            case 0:
                return prof.verApellidos();
            case 1: 
                return prof.verNombres();
            case 2:
                return prof.verDNI();
            default:
                return String.valueOf(prof.verCargo());
        }
    }
    
    /**
    * Devuelve el profesor correspondiente a la fila especificada dentro de la tabla
    * @param fila fila dentro de la tabla
    * @return Profesor - objeto Profesor correspondiente a la fila que se especifica
    * @see Profesor
    */   
    public Profesor obtenerProfesor(int fila){
        return this.profesores.get(fila);
    }
}
