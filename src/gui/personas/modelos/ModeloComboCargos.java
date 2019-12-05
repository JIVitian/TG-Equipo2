/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.personas.modelos;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Usuario
 */
public class ModeloComboCargos extends DefaultComboBoxModel{
    
    /**
     * Constructor
     */    
    public ModeloComboCargos(){
        for(Cargo c : Cargo.values()){
            this.addElement(c);
        }
    }
    
    /**
     * Devuelve el Cargo seleccionado
     * @return Cargo - cargo seleccionado
     */
    public Cargo obtenerCargo(){
        return (Cargo)this.getSelectedItem();
    }
    
    /**
     * Selecciona el cargo especificado
     * @param cargo cargo
     */
    public void seleccionarCargo(Cargo cargo){
        this.setSelectedItem(cargo);
    }
    
    
}
