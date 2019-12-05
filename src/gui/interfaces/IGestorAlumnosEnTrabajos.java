/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.interfaces;

import gui.personas.modelos.Alumno;
import gui.trabajos.modelos.AlumnoEnTrabajo;
import java.time.LocalDate;

public interface IGestorAlumnosEnTrabajos {

    /**
     * Crea un nuevo AlumnoEnTrabajo
     * @param alumno alumno que participa en el trabajo
     * @param fechaDesde fecha a partir de la cual el alumno comienza en el trabajo
     * @return AlumnoEnTrabajo  - objeto AlumnoEnTrabajo en caso que ....
    */                                                                    
    public AlumnoEnTrabajo nuevoAlumnoEnTrabajo(Alumno alumno, LocalDate fechaDesde);    
   
}
