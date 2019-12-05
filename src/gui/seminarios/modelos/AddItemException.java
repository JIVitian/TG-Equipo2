/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.seminarios.modelos;

/**
 * Excepción que ocurre cuando la fecha de aprobación, y/o, nota de aprobación son nulas.
 * @author Benjamin
 */
public class AddItemException extends Exception {
    
    public AddItemException(){
        
    }
    
    public AddItemException(String msg){
        super(msg);
    }
    
    @Override
    public String getMessage(){
        return super.getMessage();
    }
}
