/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.modelos;

import gui.interfaces.IGestorRolesEnTrabajos;
import gui.personas.modelos.Profesor;
import java.time.LocalDate;

public class GestorRolesEnTrabajos implements IGestorRolesEnTrabajos{
    private static GestorRolesEnTrabajos gestor;
    
    /**
     * Constructor
    */                                            
    private GestorRolesEnTrabajos() {    
    }
    
    /**
     * Método estático que permite crear una única instancia de GestorRolesEnTrabajos
     * @return GestorAreas
    */                                                            
    public static GestorRolesEnTrabajos instanciar() {
        if (gestor == null) 
            gestor = new GestorRolesEnTrabajos();            
        return gestor;
    } 
    
    private static boolean validarRET(Profesor profesor, Rol rol, LocalDate fechaDesde){
        if (profesor == null) {
            return false;
        }
        if (rol == null) {
            return false;
        }
        if (fechaDesde == null) {
            return false;
        }
        return true;
    }
    
    /**
     * Crea un nuevo RolEnTrabajo
     * @param profesor profesor que participa en el trabajo en el rol de tutor, cotutor o jurado
     * @param rol rol del profesor en el trabajo
     * @param fechaDesde fecha a partir de la cual el profesor comienza en el trabajo
     * @return RolEnTrabajo  - objeto RolEnTrabajo en caso que ....
    */ 
    @Override
    public RolEnTrabajo nuevoRolEnTrabajo(Profesor profesor, Rol rol, LocalDate fechaDesde) {
        if (validarRET(profesor, rol, fechaDesde) == true) {
            return new RolEnTrabajo(profesor, rol, fechaDesde);
        }
        return null;
    }
}
