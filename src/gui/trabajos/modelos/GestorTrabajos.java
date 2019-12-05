/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.modelos;

import gui.areas.modelos.Area;
import gui.areas.modelos.GestorAreas;
import gui.interfaces.IGestorAreas;
import gui.interfaces.IGestorPersonas;
import gui.interfaces.IGestorTrabajos;
import gui.personas.modelos.Alumno;
import gui.personas.modelos.Profesor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorTrabajos implements IGestorTrabajos {
    private final String NOMBRE_ARCHIVO = "./Trabajos.txt";
    //nombre del archivo con los trabajos    
    private final char SEPARADOR = ';'; 
    //caracter usado como separador 
    private final String VALORES_NULOS = "-";
    //cadena usada para los valores nulos (fecha de exposición y/o cotutor)
    
    private List<Trabajo> trabajos = new ArrayList<>();    
    private static GestorTrabajos gestor;
    
    private int ultimoTrabajo;
    //sirve para manejar la tabla tablaTrabajos
    
    /**
     * Constructor
    */                                            
    private GestorTrabajos() {   
 
    }
    
    /**
     * Método estático que permite crear una única instancia de GestorTrabajos
     * @return GestorTrabajos
    */                                                            
    public static GestorTrabajos instanciar() {
        if (gestor == null) 
            gestor = new GestorTrabajos();            
        return gestor;
    }     
    
    /**
     * Crea un nuevo trabajo
     * La fecha de aprobación debe ser igual o posterior a la de presentación
     * El tutor y el cotutor (en caso que hubiera) deben ser distintos
     * El jurado debe estar formado por 3 profesores distintos
     * El tutor no puede pertenecer al jurado
     * El cotutor (si hubiera) tampoco puede pertenecer al jurado
     * Por lo menos debe participar un alumno, y el mismo no debe estar actualmente en otro trabajo (con fecha de finalización no nula)
     * Si hay más de un alumno, deben ser distintos y ninguno debe estar en otro trabajo actualmente (con fecha de finalización no nula)
     * @param titulo título del trabajo
     * @param duracion duración del trabajo (en meses)
     * @param fechaPresentacion fecha en que se presenta el trabajo a la comisión académica para tratar su aprobación
     * @param fechaAprobacion fecha en que la comisión académica aprueba la propuesta de trabajo
     * @param areas áreas del trabajo
     * @param profesores lista con los profesores que actúan como tutor, cotutor (si hubiera) y jurado
     * @param aet alumnos que realizan el trabajo
     * @return String  - cadena con el resultado de la operación (ERROR_TITULO_DURACION | ERROR_AREAS | ERROR_FECHAS | ERROR_TUTOR_COTUTOR | ERROR_JURADO | ERROR_ALUMNOS | ESCRITURA_ERROR | EXITO)
    */                                                                    
    @Override
    public String nuevoTrabajo(String titulo, int duracion, LocalDate fechaPresentacion, LocalDate fechaAprobacion, List<Area> areas, List<RolEnTrabajo> profesores, List<AlumnoEnTrabajo> aet) {
        return null;
    }   
       
    /**
     * Busca si existe un trabajo con el título especificado (total o parcialmente)
     * Si no se especifica un título, devuelve todos los trabajos
     * Obtiene todos los trabajos creados, ordenados según el criterio especificado
     * Este método es usado por la clase ModeloTablaTrabajos
     * @param titulo título del trabajo
     * @return List<Trabajo>  - lista con los trabajos ordenados según el criterio especificado
     */
    @Override
    public List<Trabajo> buscarTrabajos(String titulo) {
        return null;
    }   
    
    /**
     * Busca si existe un trabajo que coincida con el título especificado
     * Si no hay un trabajo con el título especicado, devuelve null
     * @param titulo título del trabajo a buscar
     * @return Trabajo  - objeto Trabajo cuyo título coincida con el especificado, o null
     */
    @Override
    public Trabajo dameTrabajo(String titulo) {
        return null;
    }    
    
    /**
     * Busca si hay al menos un trabajo con el profesor especificado
     * A este método lo usa la clase GestorPersonas
     * @param profesor profesor a buscar
     * @return boolean  - true si hay al menos un trabajo con el profesor especificado
     */
    @Override
    public boolean hayTrabajosConEsteProfesor(Profesor profesor) {
        return false;
    }   
    
    /**
     * Busca si hay al menos un trabajo con el alumno especificado
     * A este método lo usa la clase GestorPersonas
     * @param alumno alumno a buscar
     * @return boolean  - true si hay al menos un trabajo con el alumno especificado
     */
    @Override
    public boolean hayTrabajosConEsteAlumno(Alumno alumno) {
        return false;
    }   
    
    /**
     * Finaliza un trabajo asignándole su fecha de exposición, con lo cual termina el trabajo
     * Cuando termina un trabajo, también termina la participación de todos los profesores (tutor, cotutor y jurado) y alumnos en el mismo
     * @param trabajo trabajo a finalizar
     * @param fechaFinalizacion fecha en que los alumnos exponen el trabajo
     * @return String  - cadena con el resultado de la operación (ERROR_FECHA_EXPOSICION | ESCRITURA_ERROR | EXITO)
    */                                                                    
    @Override
    public String finalizarTrabajo(Trabajo trabajo, LocalDate fechaFinalizacion) {
        return null;
    }    
    
    /**
     * Borra un trabajo siempre y cuando no tenga seminarios presentados
     * @param trabajo trabajo a borrar
     * @return String  - cadena con el resultado de la operación (TRABAJO_CON_SEMINARIO | ESCRITURA_ERROR | EXITO)
     */
    @Override
    public String borrarTrabajo(Trabajo trabajo) {
        return null;
    }

    /**
     * Reemplaza un profesor del trabajo. 
     * Al profesor que se reemplaza se le asigna su fecha de finalización y razón por la que finaliza su tarea
     * El nuevo profesor tiene el mismo rol del profesor que reemplaza, y comienza su tarea en la fecha en que finaliza el profesor que se reemplaza
     * El nuevo profesor no puede ocupar 
     * @param trabajo trabajo al cual se reemplazará un profesor
     * @param profesorReemplazado profesor que se reemplaza
     * @param fechaHasta fecha de finalización del profesor que se reemplaza (debe ser posterior a la fecha de inicio)
     * @param razon razón por la que se reemplaza al profesor
     * @param nuevoProfesor nuevo profesor
     * @return String  - cadena con el resultado de la operación (TRABAJO_INEXISTENTE | TRABAJO_REEMPLAZAR_PROFESOR_ERROR | TRABAJO_REEMPLAZAR_PROFESOR_DUPLICADO | TRABAJO_REEMPLAZAR_PROFESOR_INEXISTENTE | TRABAJO_REEMPLAZAR_PROFESOR_ERROR | ESCRITURA_ERROR | EXITO)
     */
    @Override
    public String reemplazarProfesor(Trabajo trabajo, Profesor profesorReemplazado, LocalDate fechaHasta, String razon, Profesor nuevoProfesor) {
        return null;
    }

    /**
     * Permite que un alumno pueda terminar su participación en el trabajo
     * @param trabajo trabajo al cual se finalizará la participación del alumno
     * @param alumno alumno que finaliza su participación en el trabajo
     * @param fechaHasta fecha de finalización del alumno en el trabajo (debe ser posterior a la fecha de inicio)
     * @param razon razón por la que el alumno finaliza su participación en el trabajo
     * @return String  - cadena con el resultado de la operación (TRABAJO_INEXISTENTE | TRABAJO_FINALIZAR_ALUMNO_ERROR | TRABAJO_FINALIZAR_ALUMNO_INEXISTENTE | TRABAJO_FINALIZAR_ALUMNO_ERROR | ESCRITURA_ERROR | EXITO)
     */
    @Override
    public String finalizarAlumno(Trabajo trabajo, Alumno alumno, LocalDate fechaHasta, String razon) {
        return null;
    }                    
            
    /**
     * Busca si hay al menos un trabajo con el área especificada
     * A este método lo usa la clase GestorAreas
     * @param area área a buscar
     * @return boolean  - true si hay al menos un trabajo con el área especificada
     */
    @Override
    public boolean hayTrabajosConEsteArea(Area area) {
        return false;
    }

    /**
     * Devuelve la posición del último trabajo agregado/modificado
     * Sirve para manejar la tabla tablaTrabajos
     * Si cuando se agrega/modifica un trabajo se cancela la operación, devuelve - 1
     * Cada vez que se agrega/modifica un trabajo, este valor toma la posición del trabajo agregado/modificado en el ArrayList
     * @return int  - posición del último trabajo agregado/modificado
     */    
    @Override
    public int verUltimoTrabajo() {
        return this.ultimoTrabajo;
    }
        
    /**
     * Asigna en -1 la variable que controla el último trabajo agregado/modificado
     * Sirve para manejar la tabla tablaTrabajos
     */
    @Override
    public void cancelar() {
    }
                    

}
