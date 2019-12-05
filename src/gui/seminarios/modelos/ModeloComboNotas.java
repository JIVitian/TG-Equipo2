/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.seminarios.modelos;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Benjamin
 */
public class ModeloComboNotas extends DefaultComboBoxModel {    
    /**
     * Constructor
     */
    public ModeloComboNotas() {
        this.addElement("Aprobado");
        this.addElement("Desaprobado");
    }
}
