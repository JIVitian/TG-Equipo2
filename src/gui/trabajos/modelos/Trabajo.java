/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.modelos;

import gui.seminarios.modelos.Seminario;
import gui.areas.modelos.Area;
import gui.interfaces.IGestorSeminarios;
import gui.interfaces.IGestorTrabajos;
import gui.personas.modelos.Profesor;
import gui.seminarios.modelos.GestorSeminarios;
import gui.seminarios.modelos.NotaAprobacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Trabajo implements Comparable<Trabajo>{                 
    private String titulo;
    private int duracion;
    private List<Area> areas = new ArrayList<>();
    private LocalDate fechaPresentacion; //fecha en que se presenta el trabajo al encargado de recibirlo
    private LocalDate fechaAprobacion; //fecha en que se aprueba en comisión la propuesta de trabajo
    private LocalDate fechaFinalizacion; //fecha en la que finaliza el trabajo (los alumnos lo presentan)   
    private List<RolEnTrabajo> ret = new ArrayList<>();
    private List<AlumnoEnTrabajo> aet = new ArrayList<>();
    private List<Seminario> seminarios = new ArrayList<>();
    
    private int ultimoSeminario = - 1;
    //sirve para manejar la tabla tablaSeminarios

    /**
     * Constructor para crear un trabajo nuevo (sin fecha de exposición)
     * @param titulo título del trabajo
     * @param duracion duración del trabajo (en meses)
     * @param areas áreas del trabajo
     * @param fechaPresentacion fecha de presentación de la propuesta de trabajo
     * @param fechaAprobacion fecha en la que se aprobó la propuesta de trabajo
     * @param ret profesores que intervienen en el trabajo con sus respectivos roles (jurado, tutor y/o cotutor)
     * @param aet alumnos que participan en el trabajo
     */
    public Trabajo(String titulo, int duracion, List<Area> areas, LocalDate fechaPresentacion, LocalDate fechaAprobacion, List<RolEnTrabajo> ret, List<AlumnoEnTrabajo> aet) {
        this(titulo, duracion, areas, fechaPresentacion, fechaAprobacion, null, ret, aet);
    }
    
    /**
     * Constructor para crear un trabajo nuevo
     * @param titulo título del trabajo
     * @param duracion duración del trabajo (en meses)
     * @param areas áreas del trabajo
     * @param fechaPresentacion fecha de presentación de la propuesta de trabajo
     * @param fechaAprobacion fecha en la que se aprobó la propuesta de trabajo
     * @param fechaFinalizacion fecha de finalización del trabajo
     * @param ret profesores que intervienen en el trabajo con sus respectivos roles (jurado, tutor y/o cotutor)
     * @param aet alumnos que participan en el trabajo
     */
    public Trabajo(String titulo, int duracion, List<Area> areas, LocalDate fechaPresentacion, LocalDate fechaAprobacion, LocalDate fechaFinalizacion, List<RolEnTrabajo> ret, List<AlumnoEnTrabajo> aet) {
        this.titulo = titulo;
        this.duracion = duracion;
        this.areas = areas;
        this.fechaPresentacion = fechaPresentacion;
        this.fechaAprobacion = fechaAprobacion;
        this.fechaFinalizacion = fechaFinalizacion;
        this.ret = ret;
        this.aet = aet;
    }
        
    /**
     * Devuelve el título del trabajo
     * @return String  - título del trabajo
     */
    public String verTitulo() {
        return this.titulo;
    }

    /**
     * Devuelve la duración del trabajo
     * @return int  - duración del trabajo
     */    
    public int verDuracion() {
        return this.duracion;
    }
    
    /**
     * Devuelve las áreas del trabajo
     * @return List<Area>  - áreas del trabajo
     */            
    public List<Area> verAreas() {
        return this.areas;
    }    

    /**
     * Devuelve la fecha de presentación del trabajo
     * @return LocalDate  - fecha de presentación del trabajo
     */    
    public LocalDate verFechaPresentacion() {
        return this.fechaPresentacion;
    }

    /**
     * Devuelve la fecha de aprobación del trabajo
     * @return LocalDate  - fecha de aprobación del trabajo
     */        
    public LocalDate verFechaAprobacion() {
        return this.fechaAprobacion;
    }

    /**
     * Devuelve la fecha de finalización del trabajo
     * @return LocalDate  - fecha de finalización del trabajo
     */        
    public LocalDate verFechaFinalizacion() {
        return this.fechaFinalizacion;
    }

    /**
     * Asigna la fecha de finalización del trabajo
     * @param fechaFinalizacion fecha de finalización del trabajo
     */
    public void asignarFechaFinalizacion(LocalDate fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }     
        
    /**
     * Devuelve el último profesor con el rol especificado (TUTOR | COTUTOR)
     * El último tutor o cotutor es el que tiene su fecha de finalización nula
     * Si no hay cotutor, devuelve null
     * @param rol rol que cumple el profesor
     * @return Profesor  - profesor con el rol especificado
     */
    public Profesor verTutorOCotutor(Rol rol) {
        if (rol == Rol.JURADO) {
            return null;
        }
        for(RolEnTrabajo ret0 : this.ret){
            if(ret0.verRol() == rol){ //Solo se permite un Tutor y un cotutor
                if(ret0.verFechaHasta() == null){
                    return ret0.verProfesor();
                }
            }
        }
        
        return null;
    }

    /**
     * Devuelve el jurado del trabajo, ordenado por apellido y nombre
     * El jurado es el último, o sea quienes tienen fecha de finalización nula
     * @return List<Profesor>  - lista con el jurado del trabajo
     */
    public List<Profesor> verJurado() {
        List<Profesor> listaJurados = new ArrayList<>();
        
        for (RolEnTrabajo jurado : this.ret) {
            if (jurado.verRol() == Rol.JURADO) {
                if (jurado.verFechaHasta() == null) {
                    listaJurados.add(jurado.verProfesor());
                }
            }
        }
        Collections.sort(listaJurados); //UTILIZA EL compareTo DE LA CLASE RET
        return listaJurados;
    }
    
    /**
     * Devuelve la lista de profesores con sus roles en el trabajo
     * La lista viene ordenada de la siguiente forma:
     * 1. Primero los tutores, luego los cotutores y luego el jurado
     * 2. Si hay 2 o más tutores (o 2 o más cotutores), se ordenan por la fecha desde la que empezaron en el proyecto
     * 3. En el caso del jurado, se ordenan por la fecha en que empezaron en el proyecto, y luego por apellido y nombre
     * @return List<RolEnTrabajo>  - lista de profesores con sus roles en el trabajo
     */
    public List<RolEnTrabajo> verProfesoresConRoles() {
        Collections.sort(this.ret); //UTILIZA EL compareTo DE LA CLASE RET
        return this.ret;
    }
    
    /**
     * Devuelve la lista de alumnos del trabajo (los que actualmente participan y los que no)
     * La lista viene ordenada de la siguiente forma:
     * 1. Los alumnos se ordenan por la fecha en que comenzaron en el proyecto, y luego por apellido y nombre
     * @return List<AlumnoEnTrabajo>  - lista de alumnos del trabajo (los que actualmente participan y los que no)
     */
    public List<AlumnoEnTrabajo> verAlumnos() {
        Collections.sort(this.aet); //UTILIZA EL compareTo DE LA CLASE AET
        return this.aet;
    }    
    
    /**
     * Devuelve la lista de los alumnos que actualmente participan del trabajo (sin fecha de finalización)
     * La lista viene ordenada de la siguiente forma:
     * 1. Los alumnos se ordenan por la fecha en que comenzaron en el trabajo, y luego por apellido y nombre
     * @return List<AlumnoEnTrabajo>  - lista de los alumnos que actualmente participan del trabajo
     */
    public List<AlumnoEnTrabajo> verAlumnosActuales() {
        List<AlumnoEnTrabajo> listaAetActuales = new ArrayList<>();
        
        for(AlumnoEnTrabajo aet : this.aet){
            if(aet.verFechaHasta() == null){
                listaAetActuales.add(aet); //SI NO TIENE FECHA HASTA LO AGREGA A LA LISTA
            }
        }
        Collections.sort(listaAetActuales);  //UTILIZA EL compareTo DE LA CLASE AET
        return listaAetActuales;
    }        
    
    /**
     * Devuelve la cantidad de profesores con el rol especificado en el trabajo
     * @param rol rol de los profesores
     * @return int  - cantidad de profesores con el rol especificado en el trabajo
     */
    public int cantidadProfesoresConRol(Rol rol) {
        List<Profesor> listaProfConRol = new ArrayList<>();
        
        for(RolEnTrabajo ret0 : this.ret){
            if (ret0.verRol() == rol) {
                listaProfConRol.add(ret0.verProfesor());
            }
        }
        return listaProfConRol.size();
    }
    
    /**
     * Devuelve la cantidad de alumnos (actuales y no) en el trabajo
     * @return int  - cantidad de alumnos en el trabajo
     */
    public int cantidadAlumnos() {
        return this.aet.size();
    }    
    
    /**
     * Devuelve la cantidad de seminarios que tiene el trabajo
     * @return int  - cantidad de seminarios que tiene el trabajo
     */
    public int cantidadSeminarios() {
        return this.seminarios.size();
    }
    
    /**
     * Informa si el trabajo tiene presentado seminarios
     * @return boolean  - true si el trabajo tiene al menos un seminario, false en caso contrario
     */
    public boolean tieneSeminarios() {
        return !this.seminarios.isEmpty();
    }
                
    /**
     * Informa si el profesor especificado participa en el trabajo
     * @param profesor profesor a buscar
     * @return boolean  - true si el profesor participa en el trabajo, false en caso constrario
     */
    public boolean tieneEsteProfesor(Profesor profesor) {
        for(RolEnTrabajo ret0 : this.ret){
            if (ret0.verProfesor().equals(profesor)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Agrega el profesor con su rol al trabajo
     * No puede haber 2 profesores iguales en el trabajo
     * @param rolEnTrabajo 
     */
    public void agregarRolEnTrabajo(RolEnTrabajo rolEnTrabajo) {
        if (!this.ret.contains(rolEnTrabajo)) {
            this.ret.add(rolEnTrabajo);
        }
    }
            
    /**
     * Agrega el seminario especificado 
     * Este método se usa cuando se leen los seminarios del archivo
     * @param seminario seminario a agregar
     */
    public void agregarSeminario(Seminario seminario) {
        if (!this.seminarios.contains(seminario)) {
            this.seminarios.add(seminario);
        }
    }
    
    /**
     * Informa si el trabajo tiene o no el seminario especificado
     * @param seminario seminario a buscar
     * @return boolean  - true si el trabajo tiene el seminario especificado, false en caso contrario
     */
    public boolean tieneEsteSeminario(Seminario seminario) {
        for(Seminario s : this.seminarios){
            if(s.equals(seminario)){ //UTILIZA EL HASH-EQUALS DE LA CLASE SEMINARIO
               return true; 
            }
        }
        return false;
    }
    
    /**
     * Crea un seminario siempre y cuando no haya otro con la misma fecha
     * Y que la fecha de exposición del seminario sea posterior a la de aprobación del trabajo
     * Si el seminario está aprobado con observaciones, o desaprobado, se deben especificar las observaciones
     * @param fechaExposicion fecha de exposición del seminario
     * @param notaAprobacion nota de aprobación del seminario
     * @param observaciones observaciones del seminario
     * @return String  - cadena con el resultado de la operación (TRABAJO_FINALIZADO | ERROR_FECHA_EXPOSICION | DUPLICADOS | ERROR | ERROR_OBSERVACIONES | EXITO)
     */
    public String nuevoSeminario(LocalDate fechaExposicion, NotaAprobacion notaAprobacion, String observaciones) {
        IGestorSeminarios gS = GestorSeminarios.instanciar();
        
        if (this.estaFinalizado()) {
            return IGestorSeminarios.TRABAJO_FINALIZADO;
        }
        for(Seminario s : this.seminarios){
            if(s.verFechaExposicion() == fechaExposicion){
                return IGestorSeminarios.DUPLICADOS;
            }
        }
        if (fechaExposicion.isBefore(this.fechaAprobacion)) { 
            return IGestorSeminarios.ERROR_FECHA_EXPOSICION;
        } 
        
        String resultado = gS.validarSeminario(fechaExposicion, notaAprobacion, observaciones);
        
        if (resultado.equals(IGestorSeminarios.DATOS_CORRECTOS)) {//VALIDAR DEBE RETORNAR EXITO
            Seminario semi = new Seminario(fechaExposicion, notaAprobacion, observaciones);
            
            if(this.tieneEsteSeminario(semi) == false){
                this.seminarios.add(semi);
                return IGestorSeminarios.EXITO;
            } else {
                return IGestorSeminarios.DUPLICADOS;
            }
        } else {
            return IGestorSeminarios.ERROR;
        }
    }
        
    
    /**
     * Modifica un seminario siempre y cuando no haya otro con la misma fecha
     * Si el seminario está aprobado con observaciones, o desaprobado, se deben especificar las observaciones
     * @param seminario seminario a modificar
     * @param notaAprobacion nota de aprobación del seminario
     * @param observaciones observaciones del seminario
     * @return String  - cadena con el resultado de la operación (ERROR | ERROR_OBSERVACIONES | EXITO)
     */    
    public String modificarSeminario(Seminario seminario, NotaAprobacion notaAprobacion, String observaciones) {
        IGestorSeminarios gS = GestorSeminarios.instanciar();
        String resultado = gS.validarSeminario(notaAprobacion, observaciones);
        
        if (resultado.equals(IGestorSeminarios.DATOS_CORRECTOS)) {
            if (this.tieneEsteSeminario(seminario)) {
                seminario.asignarNotaAprobacion(notaAprobacion);
                seminario.asignarObservaciones(observaciones);
                
                return IGestorSeminarios.EXITO;
            } else {
                return IGestorTrabajos.SEMINARIO_INEXISTENTE;
            }
        }
        return resultado;
    }

    
    /**
     * Devuelve la posición del último seminario agregado/modificado
     * Sirve para manejar la tabla tablaSeminarios
     * Cada vez que se agrega/modifica un seminario, este valor toma la posición del seminario agregado/modificado en el ArrayList
     * @return int  - posición del último seminario agregado/modificado
     */
    public int verUltimoSeminario() {
        return this.ultimoSeminario;
    }
    
    /**
     * Devuelve los seminarios ordenados según su fecha de exposición
     * @return List<Seminario>  - lista de seminarios ordenada según la fecha de exposición
     */
    public List<Seminario> verSeminarios() {
        Collections.sort(this.seminarios);
        return this.seminarios;
    }
        
    /**
     * Informa si el trabajo está o no finalizado
     * @return boolean  - true si el trabajo está finalizado, false en caso contrario
     */
    public boolean estaFinalizado() {
        if(fechaFinalizacion != null){
            return true;
        }
        return false;
    }

    /**
     * Cancela el agregado/modificación del seminario
     * Sirve para manejar la tabla de seminarios
     */
    public void cancelar() {
        this.ultimoSeminario = -1;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.titulo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Trabajo other = (Trabajo) obj;
        return this.titulo.trim().equalsIgnoreCase(other.titulo.trim()); //COMPARA LOS TITULOS DE AMBOS TRABAJOS
    }

    @Override
    public int compareTo(Trabajo t) {
        if (this.fechaPresentacion.compareTo(t.fechaPresentacion) == 0) {
            return this.titulo.trim().compareToIgnoreCase(t.titulo.trim());
        } else {
            return -this.fechaPresentacion.compareTo(t.fechaPresentacion); //ORDEN DESCENDENTE
        }
//        if (this.fechaAprobacion.compareTo(t.fechaAprobacion) == 0) {
//            return this.titulo.trim().compareToIgnoreCase(t.titulo.trim());
//        } else {
//            return -this.fechaAprobacion.compareTo(t.fechaAprobacion); //ORDEN DESCENDENTE
//        }
    }
}
