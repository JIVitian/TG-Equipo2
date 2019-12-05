/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.modelos;

import gui.personas.modelos.Profesor;
import java.time.LocalDate;

public class RolEnTrabajo {
    private Profesor profesor;
    private Rol rol;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private String razon;

    /**
     * Constructor
     * @param profesor profesor que participa en el trabajo en el rol de tutor, cotutor o jurado
     * @param rol rol del profesor en el trabajo
     * @param fechaDesde fecha a partir de la cual el profesor comienza en el trabajo
     * Cuando un profesor comienza, no tiene fecha de finalización, y por lo tanto tampoco una razón por la cual dejó de participar
     */
    public RolEnTrabajo(Profesor profesor, Rol rol, LocalDate fechaDesde) {
        this(profesor, rol, fechaDesde, null, null);
    }
    
    /**
     * Constructor
     * A este constructor se lo usa cuando se lee del archivo donde están los trabajos
     * @param profesor profesor que participa en el trabajo en el rol de tutor, cotutor o jurado
     * @param rol rol del profesor en el trabajo
     * @param fechaDesde fecha a partir de la cual el profesor comienza en el trabajo
     * @param fechaHasta fecha hasta la cual el profesor participó en el trabajo
     * @param razon razón por la cual el profesor dejó de participar en el trabajo
     */    
    public RolEnTrabajo(Profesor profesor, Rol rol, LocalDate fechaDesde, LocalDate fechaHasta, String razon) {
        this.profesor = profesor;
        this.rol = rol;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.razon = razon;
    }    

    /**
     * Muestra el profesor del trabajo
     * A este método lo usa la clase ModeloTablaRolesEnTrabajos
     * @return Profesor  - objeto Profesor que participa en el trabajo
     */    
    public Profesor verProfesor() {
        return this.profesor;
    }
        
    /**
     * Muestra el rol del profesor en el trabajo
     * A este método lo usa la clase ModeloTablaRolesEnTrabajos
     * @return Rol  - objeto Rol del profesor que participa en el trabajo
     */        
    public Rol verRol() {
        return this.rol;
    }

    /**
     * Muestra la fecha a partir de la cual el profesor comienza en el trabajo
     * A este método lo usa la clase ModeloTablaRolesEnTrabajos
     * @return LocalDate  - objeto LocalDate con la fecha a partir de la cual el profesor comienza en el trabajo
     */    
    public LocalDate verFechaDesde() {
        return this.fechaDesde;
    }
    
    /**
     * Muestra la fecha hasta la cual el profesor participó en el trabajo
     * A este método lo usa la clase ModeloTablaRolesEnTrabajos
     * @return LocalDate  - objeto LocalDate con la fecha hasta la cual el profesor participó en el trabajo
     */        
    public LocalDate verFechaHasta() {
        return this.fechaHasta;
    }

    /**
     * Muestra la razón por la cual el profesor dejó de participar en el trabajo
     * A este método lo usa la clase ModeloTablaAlumnosEnTrabajos
     * @return String  - cadena con la razón por la cual el profesor dejó de participar en el trabajo
     */    
    public String verRazon() {
        return this.razon;
    }

    /**
     * Asigna la fecha de finalización de un profesor en el trabajo
     * @param fechaHasta fecha de finalización de un profesor en el trabajo
     */
    public void asignarFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    /**
     * Asigna la razón por la cual un profesor finalizó en el trabajo
     * @param razon razón por la cual un profesor finalizó en el trabajo
     */    
    public void asignarRazon(String razon) {
        this.razon = razon;
    }
}
