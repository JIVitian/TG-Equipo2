/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.personas.modelos;

import gui.interfaces.IGestorPersonas;
import gui.trabajos.modelos.AlumnoEnTrabajo;
import gui.trabajos.modelos.GestorTrabajos;
import gui.trabajos.modelos.RolEnTrabajo;
import gui.trabajos.modelos.Trabajo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Ernesto
 */
public class GestorPersonas implements IGestorPersonas{
    private static GestorPersonas gestor;
    private List<Persona> personas = new ArrayList<>(); //listado de todas las personas (alumnos y profesores)
    
    private final String ARCHIVO_PROFESORES = "./Profesores.txt"; //nombre del archivo con todos los profesores
    private final String ARCHIVO_ALUMNOS = "./Alumnos.txt"; //nombre del archivo con todos los alumnos
    
    private int ultimoProfesor; //sirve para manejar la tabla tablaProfesores
    private int ultimoAlumno; //sirve para manejar la tabla tablaAlumnos
    
    /**
     * Constructor
     */
    private GestorPersonas(){
        cargarProfesores();
        cargarAlumnos();
    }
    
    /**
     * Método estático que permite crear una única instancia de GestorPersonas
     * @return GestorPersonas
    */
    public static GestorPersonas instanciar(){
        if(gestor == null){
            gestor = new GestorPersonas();
        }
        return gestor;
    }

    /**
     * Crea un nuevo profesor
     * @param apellidos apellido del profesor
     * @param nombres nombre del profesor
     * @param dni numero de documento del profesor
     * @param cargo cargo del profesor
     * @return cadena con el resultado de la operación (EXITO | ESCRITURA_ERROR | DUPLICADOS | ERROR)
    */  
    @Override
    public String nuevoProfesor(String apellidos, String nombres, int dni, Cargo cargo) {
        //comprobacion del apellido
        if((apellidos == null) || apellidos.isEmpty()){
            return ERROR_PROFESORES;
        }
        
        //comprobacion del nombre
        if((nombres == null) || nombres.isEmpty()){
            return ERROR_PROFESORES;
        }
        
        //comprobacion del dni
        if(dni <= 0){
            return ERROR_PROFESORES;
        }
        
        //comprobacion del cargo
        if(cargo == null){
            return ERROR_PROFESORES;
        }
        
        Profesor prof = new Profesor(apellidos, nombres, dni, cargo);
        if(!personas.contains(prof)){
            personas.add(prof);
            Collections.sort(personas);
            String resultado = this.escribirProfesor();
            if(resultado.equals(ESCRITURA_PROFESORES_OK)){
                this.ultimoProfesor = this.buscarProfesores("").indexOf(prof);
                return EXITO_PROFESORES;
            }
            return resultado;
        }
        return PROFESORES_DUPLICADOS;
    }

    /**
     * Crea un nuevo alumno
     * @param apellidos apellido del alumno
     * @param nombres nombre del alumno
     * @param dni numero de documento del alumno
     * @param cx cx del alumno
     * @return cadena con el resultado de la operación (EXITO | ESCRITURA_ERROR | DUPLICADOS | ERROR)
    */
    @Override
    public String nuevoAlumno(String apellidos, String nombres, int dni, String cx) {
        //comprobacion del apellido
        if((apellidos == null) || apellidos.isEmpty()){
            return ERROR_ALUMNOS;
        }
        
        //comprobacion del nombre
        if((nombres == null) || nombres.isEmpty()){
            return ERROR_ALUMNOS;
        }
        
        //comprobacion del dni
        if(dni <= 0){
            return ERROR_ALUMNOS;
        }
        
        //comprobacion del cargo
        if((cx == null) || cx.isEmpty()){
            return ERROR_ALUMNOS;
        }
        
        Alumno alumno = new Alumno(apellidos, nombres, dni, cx);
        if(!personas.contains(alumno)){
            personas.add(alumno);
            Collections.sort(personas);
            String resultado = this.escribirAlumno();
            if(resultado.equals(ESCRITURA_ALUMNOS_OK)){
                this.ultimoAlumno = this.buscarAlumnos("").indexOf(alumno);
                return EXITO_ALUMNOS;
            }
            return resultado;
        }
        return ALUMNOS_DUPLICADOS;
    }

    /**
     * Busca si existe un profesor con el apellido especificado (total o parcialmente)
     * Si no se especifica un apellido de profesor, devuelve todas los profesores
     * Este método es necesario para las clases ModeloTablaProfesores y ModeloComboProfesores
     * @param apellidos apellido del profesor a buscar
     * @return List<Profesor>  - lista de profesores, ordenados por apellido (y nombre si tienen el mismo apellido), cuyos apellidos coincidan con el especificado
    */ 
    @Override
    public List<Profesor> buscarProfesores(String apellidos) {
        List<Profesor> profesores = new ArrayList<>();
        if((apellidos != null) && !apellidos.isEmpty()){
            for(Persona p : personas){
                if(p instanceof Profesor){
                    if(p.verApellidos().trim().toLowerCase().contains(apellidos.trim().toLowerCase())){
                        Profesor prof = (Profesor)p;
                        profesores.add(prof);
                    }
                    
                }
            }
        }
        else
            for(Persona p : personas){
                if(p instanceof Profesor){
                    Profesor prof = (Profesor)p;
                    profesores.add(prof);
                }
            }
        if(profesores.isEmpty())
            this.ultimoProfesor =  -1;
        return profesores;
    }

    /**
     * Busca si existe un alumno con el apellido especificado (total o parcialmente)
     * Si no se especifica un apellido de alumno, devuelve todas los alumnos
     * Este método es necesario para las clases ModeloTablaAlumnos y ModeloComboAlumnos
     * @param apellidos apellido del alumno a buscar
     * @return List<Alumno>  - lista de alumnos, ordenados por apellido (y nombre si tienen el mismo apellido), cuyos apellidos coincidan con el especificado
    */ 
    @Override
    public List<Alumno> buscarAlumnos(String apellidos) {
        List<Alumno> alumnos = new ArrayList<>();
        if((apellidos != null) && !apellidos.isEmpty()){
            for(Persona p : personas){
                if(p instanceof Alumno){
                    if(p.verApellidos().trim().toLowerCase().contains(apellidos.trim().toLowerCase())){
                        Alumno alum = (Alumno)p;
                        alumnos.add(alum);
                    }
                    
                }
            }
        }
        else
            for(Persona p : personas){
                if(p instanceof Alumno){
                    Alumno alum = (Alumno)p;
                        alumnos.add(alum);
                }
            }
        if(alumnos.isEmpty())
            this.ultimoAlumno = -1;
        return alumnos;
    }

    /**
     * Busca si existe un profesor cuyo documento coincida con el especificado
     * Si existe un profesor con el documento especificado, lo devuelve
     * Si no hay un profesor con el documento especicado, devuelve null
     * A este método lo usa la clase GestorTrabajos
     * @param documento documento del profesor a buscar
     * @return Profesor  - objeto Profesor cuyo documento coincida con el especificado, o null
     */
    @Override
    public Profesor dameProfesor(int documento) {
        if(documento <= 0)
            return null;
        for(Persona p : personas){
            if(p instanceof Profesor){
                if(p.verDNI() == documento){
                    return (Profesor)p;
                }
            }
        }
        return null;
    }

    /**
     * Busca si existe un alumno cuyo cx coincida con el especificado
     * Si existe un alumno con el cx especificado, lo devuelve
     * Si no hay un alumno con el cx especicado, devuelve null
     * A este método lo usa la clase GestorTrabajos
     * @param cx cx del alumno a buscar
     * @return Alumno  - objeto Alumno cuyo cx coincida con el especificado, o null
     */
    @Override
    public Alumno dameAlumno(String cx) {
        if(cx == null || cx.isEmpty())
            return null;
        for(Persona p : personas){
            if(p instanceof Alumno){
                if(((Alumno) p).verCX().trim().toLowerCase().contains(cx.trim().toLowerCase())){
                    return (Alumno)p;
                }
            }
        }
        return null;
    }

    /**
     * Modifica un profesor
     * @param profesor profesor a editar
     * @param apellidos apellidos del profesor
     * @param nombres nombres del profesor
     * @param cargo cargo del profesor
     * @return cadena con el resultado de la operación (PROFESOR_INEXISTENTE | ERROR_PROFESORES | ESCRITURA_PROFESORES_ERROR | EXITO_PROFESORES)
    */  
    @Override
    public String modificarProfesor(Profesor profesor, String apellidos, String nombres, Cargo cargo) {
        if(!personas.contains(profesor)){
            return PROFESOR_INEXISTENTE;
        }
        
        if(apellidos == null || apellidos.trim().isEmpty()){
            return ERROR_PROFESORES;
        }
        if(nombres == null || nombres.trim().isEmpty()){
            return ERROR_PROFESORES;
        }
        if(cargo == null){
            return ERROR_PROFESORES;
        }
        for(Persona p : personas){
            if(p.equals(profesor)){
                Profesor pMod = (Profesor) p;
                pMod.asignarApellidos(apellidos);
                pMod.asignarNombres(nombres);
                pMod.asignarCargo(cargo);
            }
        }
        Collections.sort(personas);
        String resultado = this.escribirProfesor();
        if(resultado.equals(ESCRITURA_PROFESORES_OK)){
            this.ultimoProfesor = this.buscarProfesores("").indexOf(profesor);
            return EXITO_PROFESORES;
        }
        return resultado;
    }

    /**
     * Modifica un alumno
     * @param alumno alumno a editar
     * @param apellidos apellidos del alumno
     * @param nombres nombres del alumno
     * @param cx cx del alumno
     * @return cadena con el resultado de la operación (ALUMNO_INEXISTENTE | ERROR_ALUMNOS | ESCRITURA_ALUMNOS_ERROR | EXITO_ALUMNOS)
    */  
    @Override
    public String modificarAlumno(Alumno alumno, String apellidos, String nombres, String cx) {
        if(!personas.contains(alumno)){
            return ALUMNO_INEXISTENTE;
        }
        
        if(apellidos == null || apellidos.trim().isEmpty()){
            return ERROR_ALUMNOS;
        }
        if(nombres == null || nombres.trim().isEmpty()){
            return ERROR_ALUMNOS;
        }
        if(cx == null || cx.trim().isEmpty()){
            return ERROR_ALUMNOS;
        }
        for(Persona p : personas){
            if(p.equals(alumno)){
                Alumno aMod = (Alumno) p;
                aMod.asignarApellidos(apellidos);
                aMod.asignarNombres(nombres);
                aMod.asignarCX(cx);
            }
        }
        Collections.sort(personas);
        String resultado = this.escribirAlumno();
        if(resultado.equals(ESCRITURA_ALUMNOS_OK)){
            this.ultimoAlumno = this.buscarAlumnos("").indexOf(alumno);
            return EXITO_ALUMNOS;
        }
        return resultado;
    }

    /**
     * Borra un profesor siempre y cuando no figure como tutor, cotutor y/o jurado de algún trabajo
     * @param profesor profesor a borrar
     * @return String  - cadena con el resultado de la operación (TRABAJO_CON_PROFESOR | EXITO_PROFESORES | ESCRITURA_PROFESORES_ERROR)
     */
    @Override
    public String borrarProfesor(Profesor profesor) {
        if(!personas.contains(profesor)){
            return PROFESOR_INEXISTENTE;
        }
        GestorTrabajos gTrabajos = GestorTrabajos.instanciar();
        if(gTrabajos.hayTrabajosConEsteProfesor(profesor)){
            return TRABAJO_CON_PROFESOR;
        }
        this.ultimoProfesor = this.buscarProfesores("").indexOf(profesor);
        personas.remove(profesor);
        String resultado = this.escribirProfesor();
        if(resultado.equals(ESCRITURA_PROFESORES_OK))
            return EXITO_PROFESORES;
        return resultado;
    }

    /**
     * Borra un alumno siempre y cuando no figure en algún trabajo
     * @param alumno alumno a borrar
     * @return String  - cadena con el resultado de la operación (TRABAJO_CON_ALUMNO | EXITO_ALUMNOS | ESCRITURA_ALUMNOS_ERROR) 
     */
    @Override
    public String borrarAlumno(Alumno alumno) {
        if(!personas.contains(alumno)){
            return ALUMNO_INEXISTENTE;
        }
        GestorTrabajos gTrabajos = GestorTrabajos.instanciar();
        if(gTrabajos.hayTrabajosConEsteAlumno(alumno)){
            return TRABAJO_CON_ALUMNO;
        }
        this.ultimoAlumno = this.buscarAlumnos("").indexOf(alumno);
        personas.remove(alumno);
        String resultado = this.escribirAlumno();
        if(resultado.equals(ESCRITURA_ALUMNOS_OK))
            return EXITO_ALUMNOS;
        return resultado;
    }
    
    /**
     * Lee del archivo de texto y carga el ArrayList con profesores empleando un try con recursos
     * Formato del archivo:
     *  apellidoProfesor 1;nombreProfesor 1;documentoProfesor 1;cargoProfesor 1 
     *  apellidoProfesor 2;nombreProfesor 2;documentoProfesor 2;cargoProfesor 2 
     *  apellidoProfesor 3;nombreProfesor 3;documentoProfesor 3;cargoProfesor 3 
     * @return String  - cadena con el resultado de la operacion (ARCHIVO_INEXISTENTE | LECTURA_OK | LECTURA_ERROR)
     */
    private String cargarProfesores(){
        File file = new File(ARCHIVO_PROFESORES);
        if(file.exists()){
            try(BufferedReader br = new BufferedReader(new FileReader(file))){
                String linea;
                while((linea = br.readLine()) != null) {
                    String[] datos = linea.split(";");
                    String apellido = datos[0];
                    String nombre = datos[1];
                    int dni = Integer.valueOf(datos[2]);
                    Cargo cargo = Cargo.valueOf(datos[3]);
                    Persona p = new Profesor(apellido, nombre, dni, cargo);
                    this.personas.add(p);
                }
                return LECTURA_PROFESORES_OK;
            }
            catch(IOException ioe){
                return LECTURA_PROFESORES_ERROR;
            }
        }
        return ARCHIVO_PROFESORES_INEXISTENTE;
    }
    
    /**
     * Lee del archivo de texto y carga el ArrayList con alumnos empleando un try con recursos
     * Formato del archivo:
     *  apellidoAlumno 1;nombreAlumno 1;documentoAlumno 1;cxAlumno 1 
     *  apellidoAlumno 2;nombreAlumno 2;documentoAlumno 2;cxAlumno 2 
     *  apellidoAlumno 3;nombreAlumno 3;documentoAlumno 3;cxAlumno 3 
     * @return String  - cadena con el resultado de la operacion (ARCHIVO_INEXISTENTE | LECTURA_OK | LECTURA_ERROR)
     */
    private String cargarAlumnos(){
        File file = new File(ARCHIVO_ALUMNOS);
        if(file.exists()){
            try(BufferedReader br = new BufferedReader(new FileReader(file))){
                String linea;
                while((linea = br.readLine()) != null) {
                    String[] datos = linea.split(";");
                    String apellido = datos[0];
                    String nombre = datos[1];
                    int dni = Integer.valueOf(datos[2]);
                    String cx = datos[3];
                    Persona p = new Alumno(apellido, nombre, dni, cx);
                    this.personas.add(p);
                }
                return LECTURA_ALUMNOS_OK;
            }
            catch(IOException ioe){
                return LECTURA_ALUMNOS_ERROR;
            }
        }
        return ARCHIVO_ALUMNOS_INEXISTENTE;
    }
    
    /**
     * Escribe en un archivo de texto los profesores existentes del ArrayList
     * Formato del archivo:
     *  apellidoProfesor 1;nombreProfesor 1;documentoProfesor 1;cargoProfesor 1 
     *  apellidoProfesor 2;nombreProfesor 2;documentoProfesor 2;cargoProfesor 2 
     *  apellidoProfesor 3;nombreProfesor 3;documentoProfesor 3;cargoProfesor 3 
     * @return String  - cadena con el resultado de la operacion (ESCRITURA_OK | ESCRITURA_ERROR)
     */
    private String escribirProfesor(){
        File file = new File(ARCHIVO_PROFESORES);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            for(Persona persona : personas){
                if(persona instanceof Profesor){
                    Profesor prof = (Profesor) persona;
                    bw.write(prof.verApellidos().concat(";")
                            .concat(prof.verNombres()).concat(";")
                            .concat(String.valueOf(prof.verDNI())).concat(";")
                            .concat(prof.verCargo().name())
                            );
                    bw.newLine();
                }
            }
            return ESCRITURA_PROFESORES_OK;
        }
        catch(IOException ioe){
            return ESCRITURA_PROFESORES_ERROR;
        }
    }
    
    /**
     * Escribe en un archivo de texto los alumnos existentes del ArrayList
     * Formato del archivo:
     *  apellidoAlumno 1;nombreAlumno 1;documentoAlumno 1;cxAlumno 1 
     *  apellidoAlumno 2;nombreAlumno 2;documentoAlumno 2;cxAlumno 2 
     *  apellidoAlumno 3;nombreAlumno 3;documentoAlumno 3;cxAlumno 3 
     * @return String  - cadena con el resultado de la operacion (ESCRITURA_OK | ESCRITURA_ERROR)
     */
    private String escribirAlumno(){
        File file = new File(ARCHIVO_ALUMNOS);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            for(Persona persona : personas){
                if(persona instanceof Alumno){
                    Alumno a = (Alumno) persona;
                    bw.write(a.verApellidos().concat(";")
                            .concat(a.verNombres()).concat(";")
                            .concat(String.valueOf(a.verDNI())).concat(";")
                            .concat(a.verCX())
                            );
                    bw.newLine();
                }
            }
            return ESCRITURA_ALUMNOS_OK;
        }
        catch(IOException ioe){
            return ESCRITURA_ALUMNOS_ERROR;
        }
    }

    /**
     * Devuelve el orden que ocupa el profesor en todo el conjunto de profesores
     * Si no existe el profesor especificado, devuelve -1
     * Este método es necesario para poder seleccionar los 3 profesores que integran el jurado en una JList
     * @param profesor profesor al cual se le determina el orden
     * @return int  - orden que ocupa el profesor
     */
    @Override
    public int ordenProfesor(Profesor profesor) {
        return this.buscarProfesores("").indexOf(profesor);
    }

    /**
     * Devuelve el orden que ocupa el alumno en todo el conjunto de alumnos
     * Si no existe el alumno especificado, devuelve -1
     * Este método es necesario para poder seleccionar los alumnos que participan en el trabajo en una JList
     * @param alumno alumno al cual se le determina el orden
     * @return int  - orden que ocupa el alumno
     */
    @Override
    public int ordenAlumno(Alumno alumno) {
        return this.buscarAlumnos("").indexOf(alumno);
    }

    /**
     * Devuelve la posición del último profesor agregado
     * Sirve para manejar la tabla tablaProfesores
     * Si cuando se agrega un profesor se cancela la operación, devuelve - 1
     * Cada vez que se agrega un profesor, este valor toma la posición del profesor agregado en el ArrayList
     * @return int  - posición del último profesor agregado
     */
    @Override
    public int verUltimoProfesor() {
        return this.ultimoProfesor;
    }

    /**
     * Devuelve la posición del último alumno agregado
     * Sirve para manejar la tabla tablaAlumnos
     * Si cuando se agrega un alumno se cancela la operación, devuelve - 1
     * Cada vez que se agrega un alumno, este valor toma la posición del alumno agregado en el ArrayList
     * @return int  - posición del último alumno agregado
     */
    @Override
    public int verUltimoAlumno() {
        return this.ultimoAlumno;
    }

    /**
     * Asigna en -1 la variable que controla el último profesor agregado/modificado
     * Sirve para manejar la tabla tablaProfesores
     */
    @Override
    public void cancelarProfesor() {
        this.ultimoProfesor = -1;
    }

    /**
     * Asigna en -1 la variable que controla el último alumno agregado/modificado
     * Sirve para manejar la tabla tablaAlumnos
     */
    @Override
    public void cancelarAlumno() {
        this.ultimoAlumno = -1;
    }
}
