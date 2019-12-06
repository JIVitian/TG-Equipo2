/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.modelos;

import gui.interfaces.IGestorAlumnosEnTrabajos;
import gui.personas.modelos.Alumno;
import java.time.LocalDate;

public class GestorAlumnosEnTrabajos implements IGestorAlumnosEnTrabajos{
    
    private static GestorAlumnosEnTrabajos gestor;
    
    /**
     * Constructor
    */                                            
    private GestorAlumnosEnTrabajos() {   
    }
    
    /**
     * Método estático que permite crear una única instancia de GestorTrabajos
     * @return GestorTrabajos
    */                                                            
    public static GestorAlumnosEnTrabajos instanciar() {
        if (gestor == null) 
            gestor = new GestorAlumnosEnTrabajos();            
        return gestor;
    }  
    
    private static boolean validarAET(Alumno alumno, LocalDate fechaDesde){
        if (alumno == null) {
            return false;
        }
        if (fechaDesde == null) {
            return false;
        }
        return true;
    }
    
    /**
     * Crea un nuevo AlumnoEnTrabajo
     * @param alumno alumno que participa en el trabajo
     * @param fechaDesde fecha a partir de la cual el alumno comienza en el trabajo
     * @return AlumnoEnTrabajo  - objeto AlumnoEnTrabajo en caso que ....
    */  
    @Override
    public AlumnoEnTrabajo nuevoAlumnoEnTrabajo(Alumno alumno, LocalDate fechaDesde) {
        if (validarAET(alumno, fechaDesde) == true) {
            return new AlumnoEnTrabajo(alumno, fechaDesde);
        }
        return null;
    }
}
