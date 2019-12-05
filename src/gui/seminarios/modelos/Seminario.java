/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.seminarios.modelos;

import java.time.LocalDate;

public class Seminario {
    private LocalDate fechaExposicion;
    private NotaAprobacion notaAprobacion;
    private String observaciones;

    /**
     * Constructor
     * @param fechaExposicion fecha de exposición del seminario
     * @param notaAprobacion condición del seminario (aprobado, desaprobado, etc)
     * @param observaciones observaciones sobre la condición
     */
    public Seminario(LocalDate fechaExposicion, NotaAprobacion notaAprobacion, String observaciones) {
        this.fechaExposicion = fechaExposicion;
        this.notaAprobacion = notaAprobacion;
        this.observaciones = observaciones;
    }
       
    /**
     * Devuelve la fecha de exposición del seminario
     * @return LocalDate  - fecha de exposición del seminario
     */    
    public LocalDate verFechaExposicion() {
        return this.fechaExposicion;
    }  
    
    /**
     * Devuelve la nota de aprobación del seminario
     * @return NotaAprobacion  - nota de aprobación del seminario
     */        
    public NotaAprobacion verNotaAprobacion() {
        return this.notaAprobacion;
    }

    /**
     * Asigna la nota de aprobación
     * @param notaAprobacion nota de aprobación
     */
    public void asignarNotaAprobacion(NotaAprobacion notaAprobacion) {
        if (notaAprobacion != null)
            this.notaAprobacion = notaAprobacion;
    }
            
    /**
     * Devuelve las observaciones del seminario
     * @return String  - observaciones del seminario
     */
    public String verObservaciones() {
        return this.observaciones;
    }

    /**
     * Asigna las observaciones del seminario
     * @param observaciones observaciones
     */
    public void asignarObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
