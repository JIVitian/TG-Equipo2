/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.modelos;

import gui.personas.modelos.Alumno;
import java.time.LocalDate;

public class AlumnoEnTrabajo {
    private Alumno alumno;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private String razon;    
    
    /**
     * Constructor
     * @param alumno alumno que participa en el trabajo
     * @param fechaDesde fecha a partir de la cual el alumno comienza en el trabajo
     * Cuando un alumno comienza, no tiene fecha de finalización, y por lo tanto tampoco una razón por la cual dejó de participar
     */
    public AlumnoEnTrabajo(Alumno alumno, LocalDate fechaDesde) {
        this(alumno, fechaDesde, null, null);    
    }
    
    /**
     * Constructor
     * A este constructor se lo usa cuando se lee del archivo donde están los trabajos
     * @param alumno alumno que participa en el trabajo
     * @param fechaDesde fecha a partir de la cual el alumno comienza en el trabajo
     * @param fechaHasta fecha hasta la cual el alumno participó en el trabajo
     * @param razon razón por la cual el alumno dejó de participar en el trabajo
     */
    public AlumnoEnTrabajo(Alumno alumno, LocalDate fechaDesde, LocalDate fechaHasta, String razon) {
        this.alumno = alumno;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.razon = razon;    
    }
    
    /**
     * Muestra el alumno del trabajo
     * A este método lo usa la clase ModeloTablaAlumnosEnTrabajos
     * @return Alumno  - objeto Alumno que participa en el trabajo
     */
    public Alumno verAlumno() {
        return this.alumno;
    }

    /**
     * Muestra la fecha a partir de la cual el alumno comienza en el trabajo
     * A este método lo usa la clase ModeloTablaAlumnosEnTrabajos
     * @return LocalDate  - objeto LocalDate con la fecha a partir de la cual el alumno comienza en el trabajo
     */
    public LocalDate verFechaDesde() {
        return this.fechaDesde;
    }

    /**
     * Muestra la fecha hasta la cual el alumno participó en el trabajo
     * A este método lo usa la clase ModeloTablaAlumnosEnTrabajos
     * @return LocalDate  - objeto LocalDate con la fecha hasta la cual el alumno participó en el trabajo
     */    
    public LocalDate verFechaHasta() {
        return this.fechaHasta;
    }

    /**
     * Muestra la razón por la cual el alumno dejó de participar en el trabajo
     * A este método lo usa la clase ModeloTablaAlumnosEnTrabajos
     * @return String  - cadena con la razón por la cual el alumno dejó de participar en el trabajo
     */
    public String verRazon() {
        return this.razon;
    }

    /**
     * Asigna la fecha de finalización de un alumno en el trabajo
     * @param fechaHasta fecha de finalización de un alumno en el trabajo
     */    
    public void asignarFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    /**
     * Asigna la razón por la cual un alumno finalizó en el trabajo
     * @param razon razón por la cual un alumno finalizó en el trabajo
     */        
    public void asignarRazon(String razon) {
        this.razon = razon;
    }    
}
