/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.interfaces;

import gui.personas.modelos.Profesor;
import gui.trabajos.modelos.Rol;
import gui.trabajos.modelos.RolEnTrabajo;
import java.time.LocalDate;

public interface IGestorRolesEnTrabajos {

    /**
     * Crea un nuevo RolEnTrabajo
     * @param profesor profesor que participa en el trabajo en el rol de tutor, cotutor o jurado
     * @param rol rol del profesor en el trabajo
     * @param fechaDesde fecha a partir de la cual el profesor comienza en el trabajo
     * @return RolEnTrabajo  - objeto RolEnTrabajo en caso que ....
    */                                                                    
    public RolEnTrabajo nuevoRolEnTrabajo(Profesor profesor, Rol rol, LocalDate fechaDesde);    
    
}
