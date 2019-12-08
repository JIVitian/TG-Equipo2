/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.personas.modelos;

public abstract class Persona implements Comparable<Persona>{
    private String apellidos;
    private String nombres;
    private int dni;

    /**
     * Constructor
     * @param apellidos apellidos de una persona
     * @param nombres nombres de una persona
     * @param dni dni de una persona
     */
    public Persona(String apellidos, String nombres, int dni) {
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.dni = dni;
    }  

    /**
     * Muestra los apellidos de una persona
     * @return String  - apellidos de una persona
     */
    public String verApellidos() {
        return this.apellidos;
    }

    /**
     * Asigna los apellidos a una persona
     * @param apellidos apellidos de una persona
     */
    public void asignarApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Muestra los nombres de una persona
     * @return String  - nombres de una persona
     */    
    public String verNombres() {
        return this.nombres;
    }

    /**
     * Asigna los nombres a una persona
     * @param nombres nombres de una persona
     */    
    public void asignarNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * Muestra el dni de una persona
     * @return int  - dni de una persona
     */    
    public int verDNI() {
        return this.dni;
    }

    /**
     * Convierte una persona a cadena
     * @return String  - cadena que representa una persona ("apellidos, nombres")
     */
    @Override
    public String toString() {
        return this.apellidos + ", " + this.nombres;
    }    

    /**
     * Devuelve el hashcode de una persona
     * @return int  - hashcode de una persona
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.dni;
        return hash;
    }

    /**
     * Compara si 2 personas son iguales o no según el documento
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
        final Persona other = (Persona) obj;
        if (this.dni != other.dni) {
            return false;
        }
        return true;
    }

    /**
     * Permite ordenar las personas por apellido
     * En caso de tener el mismo apellido, las compara por nombre
     * @param o objeto contra el cual comparar
     * @return int  - resultado de la comparación
     */
    @Override
    public int compareTo(Persona o) {
        int resultApellido = this.apellidos.compareToIgnoreCase(o.verApellidos());
        if(resultApellido == 0) //si los apellidos son iguales, se compara por nombre
            return this.nombres.compareToIgnoreCase(o.verNombres());
        return resultApellido;
    }
}
