/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.personas.modelos;

import gui.interfaces.IGestorPersonas;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Ernesto
 */
public class ModeloComboAlumnos extends DefaultComboBoxModel {
    
    /**
     * Constructor
     */
    public ModeloComboAlumnos() {
        IGestorPersonas ga = GestorPersonas.instanciar();
        for (Persona p : ga.buscarAlumnos("")){
            this.addElement(p);
      }  
    }
    
    /**
     * Devuelve el alumno seleccionado
     * @return alum Alumno
     */
    public Alumno obtenerAlumno() {
        return (Alumno)this.getSelectedItem();
    }
    
    /**
     * Selecciona el alumno especificado
     * @param alum Alumno
     */
    public void seleccionarAlumno(Alumno alum){
        this.setSelectedItem(alum);
    }
}
