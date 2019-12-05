/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.personas.modelos;

import java.util.Objects;

public class Alumno extends Persona {
    private String cx;

    /**
     * Constructor
     * @param apellidos apellidos de un alumno
     * @param nombres nombres de un alumno
     * @param dni dni de un alumno
     * @param cx cx de un alumno
     */    
    public Alumno(String apellidos, String nombres, int dni, String cx) {
        super(apellidos, nombres, dni);
        this.cx = cx;
    }

    /**
     * Muestra el cx de un alumno
     * @return String  - cx de un alumno
     */        
    public String verCX() {
        return this.cx;
    }

    /**
     * Asigna el cx a un alumno
     * @param cx cx de un alumno
     */        
    public void asignarCX(String cx) {
        this.cx = cx;
    }

    /**
     * Devuelve el hashcode de un alumno
     * @return int  - hashcode de un alumno
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.cx);
        return hash;
    }

    /**
     * Compara si 2 personas son iguales o no según el documento
     * Si ambas personas a comparar son alumnos, se compara tambien su CX
     * @param obj objeto contra el cual comparar
     * @return boolean  - resultado de la comparación
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass().getSuperclass() != obj.getClass().getSuperclass()) {
            return false;
        }
        if(obj.getClass() == Profesor.class){
            final Persona other1 = (Persona) obj;
            if(this.verDNI() != other1.verDNI()){
                return false;
            }
        }
        if(obj.getClass() == Alumno.class){
            final Alumno other = (Alumno) obj;
            if(!Objects.equals(this.cx,other.cx) && this.verDNI() != other.verDNI()){
                return false;
            }
        }
        return true;
    }
    
    
}
