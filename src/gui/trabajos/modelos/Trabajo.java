/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.modelos;

import gui.seminarios.modelos.Seminario;
import gui.areas.modelos.Area;
import static gui.interfaces.IGestorSeminarios.EXITO;
import static gui.interfaces.IGestorTrabajos.ERROR_FECHA_EXPOSICION;
import static gui.interfaces.IGestorTrabajos.SEMINARIO_DUPLICADO;
import static gui.interfaces.IGestorTrabajos.SEMINARIO_ERROR;
import static gui.interfaces.IGestorTrabajos.SEMINARIO_EXITO;
import static gui.interfaces.IGestorTrabajos.SEMINARIO_INEXISTENTE;
import gui.seminarios.modelos.GestorSeminarios;
import gui.seminarios.modelos.NotaAprobacion;
import gui.personas.modelos.Profesor;
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
        List<Profesor> cotutores = new ArrayList<>();
        
        for(RolEnTrabajo ret0 : this.ret){
            if(ret0.verRol() == rol){ //Solo se permite un Tutor y un tuto
                if(ret0.verFechaHasta() == null){
                    return ret0.verProfesor();
                }
            }
            if (ret0.verRol() == Rol.COTUTOR) {
                cotutores.add(ret0.verProfesor());
            }
        }
        if (cotutores.isEmpty()) {
            return null;
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
        
//        Comparator<Persona> cmpProfesores = new Comparator<Persona>(){
//            public int compare(Persona p1, Persona p2){//Defino como voy a ordenar a la lista.
//                if(!p1.verApellidos().equalsIgnoreCase(p2.verApellidos())){//Si los apellidos son distintos no hay problema
//                    return p1.verApellidos().toUpperCase().compareTo(p2.verApellidos().toUpperCase());
//                }else{//Si dos apellidos son iguales, comparare los nombres
//                    return p1.verNombres().toUpperCase().compareTo(p2.verNombres().toUpperCase());
//                }
//            }
//        };
        
        for (RolEnTrabajo jurado : this.ret) {
            if (jurado.verRol() == Rol.JURADO) {
                if (jurado.verFechaHasta() == null) {
                    listaJurados.add(jurado.verProfesor());
                }
            }
        }
        
//        Collections.sort(listaJurados, cmpProfesores);
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
        
//        Comparator<RolEnTrabajo> cmpRET = (RolEnTrabajo ret1, RolEnTrabajo ret2) -> {
//            if (ret1.verRol().compareTo(ret2.verRol()) == 0) {
//                if (ret1.verFechaDesde().compareTo(ret2.verFechaDesde()) == 0) {
//                    return ret1.verProfesor().compareTo(ret2.verProfesor()); //NECESITA QUE ESTE OVERRIDE EL COMPARABLE EN PROFESOR
//                } else {
//                    return ret1.verFechaDesde().compareTo(ret2.verFechaDesde());
//                }
//            } else {
//                return ret1.verRol().compareTo(ret2.verRol());
//            }
//        };

//        Collections.sort(this.ret, cmpRET);
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
        
//        Comparator<AlumnoEnTrabajo> cmpAET = (AlumnoEnTrabajo aet1, AlumnoEnTrabajo aet2) -> {
//            //Defino como voy a ordenar a la lista.
//            if (!aet1.verFechaDesde().equals(aet2.verFechaDesde())) { //Si las fechas de inicio son distintas no hay problema
//                return aet1.verFechaDesde().compareTo(aet2.verFechaDesde());
//            } else { //Si dos fechas inicio son iguales, comparare los apellidos
//                if (!aet1.verAlumno().verApellidos().equalsIgnoreCase(aet2.verAlumno().verApellidos())) { //Si los apellidos son distintos no hay problema
//                    return aet1.verAlumno().verApellidos().compareToIgnoreCase(aet2.verAlumno().verApellidos());
//                } else { //Si dos apellidos son iguales, comparare los nombres
//                    return aet1.verAlumno().verNombres().compareToIgnoreCase(aet2.verAlumno().verNombres());
//                }
//            }
//        };

//        Collections.sort(this.aet, cmpAET);
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
        
//        Comparator<AlumnoEnTrabajo> cmpAET = new Comparator<AlumnoEnTrabajo>() {
//            public int compare(AlumnoEnTrabajo aet1, AlumnoEnTrabajo aet2) { //Defino como voy a ordenar a la lista.
//                if (!aet1.verFechaDesde().equals(aet2.verFechaDesde())) { //Si las fechas de inicio son distintas no hay problema
//                    return aet1.verFechaDesde().compareTo(aet2.verFechaDesde());
//                } else { //Si dos apellidos son iguales, comparare los apellidos
//                    if (!aet1.verAlumno().verApellidos().equalsIgnoreCase(aet2.verAlumno().verApellidos())) { //Si los apellidos son distintos no hay problema
//                        return aet1.verAlumno().verApellidos().toUpperCase().compareTo(aet2.verAlumno().verApellidos().toUpperCase());
//                    } else { //Si dos apellidos son iguales, comparare los nombres
//                        return aet1.verAlumno().verNombres().toUpperCase().compareTo(aet2.verAlumno().verNombres().toUpperCase());
//                    }
//                }
//            }
//        };
        
        for(AlumnoEnTrabajo aet : this.aet){
            if(aet.verFechaHasta() == null){
                listaAetActuales.add(aet); //SI NO TIENE FECHA HASTA LO AGREGA A LA LISTA
            }
        }
        
//        Collections.sort(listaAetActuales, cmpAET);
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
//            for(RolEnTrabajo ret0 : this.ret){
//                for(RolEnTrabajo ret1 : this.ret){
//                    if (!ret0.verProfesor().equals(ret1.verProfesor())) { //UTILIZA EL HASH-EQUALS DE LA CLASE PERSONAS
//                        this.ret.add(rolEnTrabajo);
//                    }
//                }
//            }
//        }
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
        GestorSeminarios gS = GestorSeminarios.instanciar();

        for(Seminario s : this.seminarios){
            if(s.verFechaExposicion() == fechaExposicion){
                return SEMINARIO_DUPLICADO;
            }
        }
        if (!fechaExposicion.isBefore(fechaAprobacion)) { //FECHA DE EXPOSICION NO ES ANTES QUE LA DE APROBACION
            if (gS.validarSeminario(fechaExposicion, notaAprobacion, observaciones).toUpperCase().equalsIgnoreCase(EXITO)) {//VALIDAR DEBE RETORNAR EXITO
                Seminario semi = new Seminario(fechaExposicion, notaAprobacion, observaciones);
                if(!this.seminarios.contains(semi)){
                    this.seminarios.add(semi);
                    return SEMINARIO_EXITO;
                }
                return SEMINARIO_DUPLICADO;
            } else {
                return SEMINARIO_ERROR;
            }
        } else {
            return ERROR_FECHA_EXPOSICION;
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
        GestorSeminarios gS = GestorSeminarios.instanciar();
        
        for(Seminario s : this.seminarios){
            if(s.equals(seminario)){
                if(gS.validarSeminario(notaAprobacion, observaciones).equalsIgnoreCase(EXITO)){
                    s.asignarNotaAprobacion(notaAprobacion);
                    s.asignarObservaciones(observaciones);
                    return gS.validarSeminario(notaAprobacion, observaciones);
                }
                return gS.validarSeminario(notaAprobacion, observaciones);
            }
        }
        return SEMINARIO_INEXISTENTE;
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
//        Collections.sort(this.seminarios);
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
