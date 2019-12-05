/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.interfaces;

import gui.seminarios.modelos.NotaAprobacion;
import java.time.LocalDate;

public interface IGestorSeminarios {
    //Constantes para las operaciones de E/S
    public static final String LECTURA_ERROR = "Error al leer los seminarios";
    public static final String LECTURA_OK = "Se pudieron leer los seminarios";
    public static final String ARCHIVO_INEXISTENTE = "No existe el archivo";
    public static final String ESCRITURA_ERROR = "Error al guardar los seminarios";
    public static final String ESCRITURA_OK = "Se pudieron guardar los seminarios";    
    
    //Constantes para los seminarios
    public static final String ERROR = "La fecha y/o nota de aprobación no pueden ser nulas";  
    public static final String ERROR_OBSERVACIONES = "Se deben especificar las observaciones";        
    public static final String DATOS_CORRECTOS = "Datos correctos para crear un seminario";    
    public static final String EXITO = "Seminario creado/modificado con éxito";
    
    //A estas constantes las usa Trabajo
    public static final String TRABAJO_FINALIZADO = "No se puede agregar un seminario ya que el trabajo finalizó";
    public static final String DUPLICADOS = "El trabajo ya tiene un seminario expuesto es esa fecha";    
    public static final String ERROR_FECHA_EXPOSICION = "La fecha de exposición del seminario debe ser posterior a la de aprobación del trabajo";                   
        
    /**
     * Valida que estén correctos los datos para crear un nuevo seminario
     * Si el seminario está aprobado con observaciones, o desaprobado, se deben especificar las observaciones
     * @param fechaExposicion fecha de exposición del seminario
     * @param notaAprobacion nota de aprobación del seminario
     * @param observaciones observaciones del seminario
     * @return String  - cadena con el resultado de la validación (ERROR | ERROR_OBSERVACIONES | DATOS_CORRECTOS)
     */
    public String validarSeminario(LocalDate fechaExposicion, NotaAprobacion notaAprobacion, String observaciones);

    /**
     * Valida que estén correctos los datos para crear un nuevo seminario
     * Si el seminario está aprobado con observaciones, o desaprobado, se deben especificar las observaciones
     * @param notaAprobacion nota de aprobación del seminario
     * @param observaciones observaciones del seminario
     * @return String  - cadena con el resultado de la validación (ERROR | ERROR_OBSERVACIONES | DATOS_CORRECTOS)
     */
    public String validarSeminario(NotaAprobacion notaAprobacion, String observaciones);
    
    /**
     * Guarda todos los seminarios de todos los trabajos
     * @return String  - cadena con el resultado de la operacion (ESCRITURA_OK | ESCRITURA_ERROR)
     */    
    public String guardarSeminarios();
}
