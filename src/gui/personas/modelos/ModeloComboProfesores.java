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
public class ModeloComboProfesores extends DefaultComboBoxModel{
    
    /**
     * Constructor
     */
    public ModeloComboProfesores(){
        IGestorPersonas gPersonas = GestorPersonas.instanciar();
        for(Persona p : gPersonas.buscarProfesores("")){
            this.addElement(p);
        }
    }
    
    /**
     * Devuelve el profesor seleccionado
     * @return prof profesor
     */
    public Profesor obtenerProfesor(){
        return (Profesor)this.getSelectedItem();
    }
    
    /**
     * Selecciona el profesor especificado
     * @param prof profesor
     */
    public void seleccionarProfesor(Profesor prof){
        this.setSelectedItem(prof);
    }
}
