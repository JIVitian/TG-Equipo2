/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.modelos;

import gui.areas.modelos.Area;
import gui.areas.modelos.GestorAreas;
import gui.interfaces.IGestorAlumnosEnTrabajos;
import gui.interfaces.IGestorAreas;
import gui.interfaces.IGestorPersonas;
import gui.interfaces.IGestorRolesEnTrabajos;
import gui.interfaces.IGestorTrabajos;
import gui.personas.modelos.Alumno;
import gui.personas.modelos.GestorPersonas;
import gui.personas.modelos.Profesor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorTrabajos implements IGestorTrabajos {
    private final String NOMBRE_ARCHIVO = "./Trabajos.txt";
    //nombre del archivo con los trabajos    
    private final char SEPARADOR = ';'; 
//    private final char SEPARADOR_INTERNO = '|';
//    private final char SEPARADOR_PARAMETRO = '_';
    private final char SEPARADOR_FECHAS = '/';
    //caracter usado como separador 
    private final String VALORES_NULOS = "-";
    //cadena usada para los valores nulos (fecha de exposición y/o cotutor)
    private final String PATRON_FECHAS = "dd/MM/yyyy";
    
    private List<Trabajo> trabajos = new ArrayList<>();    
    private static GestorTrabajos gestor;
    
    private int ultimoTrabajo;
    //sirve para manejar la tabla tablaTrabajos
    
    /**
     * Constructor
    */                                            
    private GestorTrabajos() {   
        this.leerArchivo();
    }
    
    /**
     * Método estático que permite crear una única instancia de GestorTrabajos
     * @return GestorTrabajos
    */                                                            
    public static GestorTrabajos instanciar() {
        if (gestor == null) 
            gestor = new GestorTrabajos();            
        return gestor;
    }     

    /**
     * Un getter para la lista de trabajos
     * @return trabajos lista de todos los trabajos existentes 
     */
    public List<Trabajo> verTrabajos() {
        return trabajos;
    }
    
    /**
     * Crea un nuevo trabajo
     * La fecha de aprobación debe ser igual o posterior a la de presentación
     * El tutor y el cotutor (en caso que hubiera) deben ser distintos
     * El jurado debe estar formado por 3 profesores distintos
     * El tutor no puede pertenecer al jurado
     * El cotutor (si hubiera) tampoco puede pertenecer al jurado
     * Por lo menos debe participar un alumno, y el mismo no debe estar actualmente en otro trabajo (con fecha de finalización no nula)
     * Si hay más de un alumno, deben ser distintos y ninguno debe estar en otro trabajo actualmente (con fecha de finalización no nula)
     * @param titulo título del trabajo
     * @param duracion duración del trabajo (en meses)
     * @param fechaPresentacion fecha en que se presenta el trabajo a la comisión académica para tratar su aprobación
     * @param fechaAprobacion fecha en que la comisión académica aprueba la propuesta de trabajo
     * @param areas áreas del trabajo
     * @param profesores lista con los profesores que actúan como tutor, cotutor (si hubiera) y jurado
     * @param aet alumnos que realizan el trabajo
     * @return String  - cadena con el resultado de la operación (ERROR_TITULO_DURACION | ERROR_AREAS | ERROR_FECHAS | ERROR_TUTOR_COTUTOR | ERROR_JURADO | ERROR_ALUMNOS | ESCRITURA_ERROR | EXITO)
    */                                                                    
    @Override
    public String nuevoTrabajo(String titulo, int duracion, LocalDate fechaPresentacion, LocalDate fechaAprobacion, List<Area> areas, List<RolEnTrabajo> profesores, List<AlumnoEnTrabajo> aet) {
        boolean hayTutor = false;
        List<Profesor> listaTutores = new ArrayList<>();
        List<Profesor> listaCotutores = new ArrayList<>();
        List<Profesor> listaJurados = new ArrayList<>();
        
        //VERIFICA EL TITULO Y LA DURACION
        if (titulo == null || titulo.trim().isEmpty() || duracion <= 0) {
            return ERROR_TITULO_DURACION;
        }  
        
        //VERIFICA LA LISTA DE AREAS
        if (areas == null || areas.isEmpty()) {
            return ERROR_AREAS;
        }
        
        //VERIFICA FECHAS DE PRESENTACION Y APROBACION
        if (fechaPresentacion == null || fechaAprobacion == null){
            return ERROR_FECHAS;
        }
       
        //VERIFICA QUE LA FECHAAPROBACION SEA POSTERIOR A LA DE PRESENTACION
        if (fechaAprobacion.isBefore(fechaPresentacion)) {
            return ERROR_FECHAS;
        }
        
        //SEPARA A CADA PROFESOR SEGUN SU ROL Y LOS AÑADE A UNA LISTA
        for(RolEnTrabajo ret0 : profesores){
            if (ret0.verRol() == Rol.TUTOR) {
                hayTutor = true;
                listaTutores.add(ret0.verProfesor());
            }
            if (ret0.verRol() == Rol.COTUTOR) {
                listaCotutores.add(ret0.verProfesor());
            }
            if (ret0.verRol() == Rol.JURADO) {
                listaJurados.add(ret0.verProfesor());
            }
        }
        
        //VERIFICA SI NO HAY UN TUTOR EN EL TRABAJO
        if (hayTutor == false) {
            return ERROR_TUTOR_COTUTOR;
        }
        
        //VERIFICA SI EL TUTOR Y COTUTOR SON IGUALES
        if (hayTutor == true && listaCotutores.size() > 0) {
            for(Profesor p1 : listaTutores){
                for(Profesor p2 : listaCotutores){
                    if (p1.equals(p2)) {
                        return ERROR_TUTOR_COTUTOR;
                    }
                }
            }
        }
        
        //VERIFICA QUE HAYA SOLO 3 JURADOS
        if (listaJurados.size() != 3) {
            return ERROR_JURADO;
        }
        
        //VERIFICA QUE LOS JURADOS NO SEAN IGUALES ENTRE SI
        if (listaJurados.size() == 3) {
            if ((listaJurados.get(0).equals(listaJurados.get(1))) || 
                (listaJurados.get(1).equals(listaJurados.get(2))) || 
                (listaJurados.get(2).equals(listaJurados.get(0)))) {
                    return ERROR_JURADO;
            }
        }
        
        //VERIFICA QUE EL TUTOR NO ESTE EN EL JURADO
        if (hayTutor == true) {
            for(Profesor p1 : listaTutores){
                for(Profesor p2 : listaJurados){
                    if (p1.equals(p2)) {
                        return ERROR_JURADO;
                    }
                }
            }
        }
        
        //VERIFICA QUE EL COTUTOR NO ESTE EN EL JURADO
        if (listaCotutores.size() > 0) {
            for(Profesor p1 : listaCotutores){
                for(Profesor p2 : listaJurados){
                    if (p1.equals(p2)) {
                        return ERROR_JURADO;
                    }
                }
            }
        }
        
        //VERIFICA QUE EXISTA ALGUN ALUMNO EN EL TRABAJO
        if (aet == null || aet.isEmpty()) {
            return ERROR_ALUMNOS;
        }
        
        //VERIFICA QUE EL AET NO SE ENCUENTRE EN OTRO TRABAJO
        for(AlumnoEnTrabajo aet1 : aet){
            for(Trabajo t : this.trabajos){
                if (t.verFechaFinalizacion() != null) {
                    for(AlumnoEnTrabajo aet2 : t.verAlumnos()){
                        if (aet1.verAlumno().equals(aet2.verAlumno())) {
                            return ERROR_ALUMNOS;
                        }
                    }
                }
            }
        }
        
        //VERIFICA QUE NO ESTE REPETIDO EL MISMO AET
        //VERIFICAR. NO SE SI ESTA BIEN
        if (aet.size() > 1) {
            int contador = 0;
            for (int i = 0; i < aet.size() - 1; i++) {
//SE REPITE HASTA QUE LA POSICION SEA LA PENULTIMA DE LA LISTA, DE MODO QUE LLEGUE A COMPARAR EL PENULTIMO CON EL ULTIMO ELEMENTO
                for (int j = aet.indexOf(i) + 1; j < aet.size() ; j++) { 
//SE REPITE HASTA QUE LA POSICION DE J SEA LA ULTIMA DE LA LISTA
                    if (aet.get(i).equals(aet.get(j))) {
                        contador ++;
                        if (contador > 1) {
                            return ERROR_ALUMNOS;
                        }
                    }
                }
            }
        }

        Trabajo trabajo = new Trabajo(titulo.trim(), duracion, areas, fechaPresentacion, fechaAprobacion, profesores, aet);
        if (!this.trabajos.contains(trabajo)) {
            this.trabajos.add(trabajo);
        }else{
            return DUPLICADOS;
        }
        
//        ordenar(this.trabajos);
        Collections.sort(this.trabajos);

        String resultado = this.escribirArchivo();
        if (resultado.equals(ESCRITURA_OK)) {
            this.ultimoTrabajo = this.trabajos.indexOf(trabajo);
            return EXITO;
        }
        return resultado;
    }
    
//    /**
//     * Ordena una lista de trabajos en forma descendente segun fecha de presentacion. 
//     * En caso de haber 2 trabajos con la misma fecha de presentación, 
//     * estos dos se ordenaran según el título ascendentemente.
//     * @param lista lista de trabajos que se desea ordenar
//     */
//    public void ordenar(List<Trabajo> lista){
//            Comparator<Trabajo> tComp = new Comparator<Trabajo>() {
//                public int compare(Trabajo t1, Trabajo t2) {//Defino como voy a ordenar a la lista.
//                    if (t1.verFechaPresentacion().compareTo(t2.verFechaPresentacion()) != 0) {//Si los apellidos son distintos no hay problema
//                        return -t1.verFechaPresentacion().compareTo(t2.verFechaPresentacion());
//                    } else {//Si dos apellidos son iguales, comparare los nombres.
//                        return t1.verTitulo().toUpperCase().compareTo(t2.verTitulo().toUpperCase());
//                    }
//                }
//            };
//            Collections.sort(lista, tComp);
//    }
       
    /**
     * Busca si existe un trabajo con el título especificado (total o parcialmente)
     * Si no se especifica un título, devuelve todos los trabajos
     * Obtiene todos los trabajos creados, ordenados según el criterio especificado
     * Este método es usado por la clase ModeloTablaTrabajos
     * @param titulo título del trabajo
     * @return List<Trabajo>  - lista con los trabajos ordenados según el criterio especificado
     */
    @Override
    public List<Trabajo> buscarTrabajos(String titulo) {
        if (titulo != null) {
            List<Trabajo> trabajosBuscados = new ArrayList<>(); //ArrayList auxiliar
            for(Trabajo trabajo : this.trabajos) {
                if (trabajo.verTitulo().toLowerCase().contains(titulo.toLowerCase()))
                    trabajosBuscados.add(trabajo); //Si encuentra el trabajo solicitado lo agrega al ArrayList auxiliar
            }
//            ordenar(trabajosBuscados);
            Collections.sort(trabajosBuscados);
            return trabajosBuscados;  //Devuelve el ArrayList aux con todas las coincidencias
        }
        return this.trabajos; //todas los trabajos
    }   
    
    /**
     * Busca si existe un trabajo que coincida con el título especificado
     * Si no hay un trabajo con el título especicado, devuelve null
     * @param titulo título del trabajo a buscar
     * @return Trabajo  - objeto Trabajo cuyo título coincida con el especificado, o null
     */
    @Override
    public Trabajo dameTrabajo(String titulo) {
        for(Trabajo trabajo : this.trabajos) {
            if (trabajo.verTitulo().equalsIgnoreCase(titulo.trim()))
                return trabajo;
        }
        return null;
    }    
    
    /**
     * Busca si hay al menos un trabajo con el profesor especificado
     * A este método lo usa la clase GestorPersonas
     * @param profesor profesor a buscar
     * @return boolean  - true si hay al menos un trabajo con el profesor especificado
     */
    @Override
    public boolean hayTrabajosConEsteProfesor(Profesor profesor) {
        for(Trabajo trabajo : this.trabajos){
            for(RolEnTrabajo ret : trabajo.verProfesoresConRoles()){
                if (ret.verProfesor().equals(profesor)) {
                    return true;
                }
            }
        }
        return false;
    }   
    
    /**
     * Busca si hay al menos un trabajo con el alumno especificado
     * A este método lo usa la clase GestorPersonas
     * @param alumno alumno a buscar
     * @return boolean  - true si hay al menos un trabajo con el alumno especificado
     */
    @Override
    public boolean hayTrabajosConEsteAlumno(Alumno alumno) {
        for(Trabajo trabajo : this.trabajos){
            for(AlumnoEnTrabajo aet : trabajo.verAlumnos()){
                if (aet.verAlumno().equals(alumno)) {
                    return true;
                }
            }
        }
        return false;
    }   
    
    /**
     * Finaliza un trabajo asignándole su fecha de exposición, con lo cual termina el trabajo
     * Cuando termina un trabajo, también termina la participación de todos los profesores (tutor, cotutor y jurado) y alumnos en el mismo
     * @param trabajo trabajo a finalizar
     * @param fechaFinalizacion fecha en que los alumnos exponen el trabajo
     * @return String  - cadena con el resultado de la operación (ERROR_FECHA_EXPOSICION | ESCRITURA_ERROR | EXITO)
    */                                                                    
    @Override
    public String finalizarTrabajo(Trabajo trabajo, LocalDate fechaFinalizacion) {
        if (fechaFinalizacion == null) {
            return ERROR_FECHA_EXPOSICION;
        }
        if (!fechaFinalizacion.isAfter(trabajo.verFechaAprobacion())){
            return ERROR_FECHA_EXPOSICION;
        }    
        
        if (trabajos.contains(trabajo)) {
            trabajo.asignarFechaFinalizacion(fechaFinalizacion);

            trabajo.verProfesoresConRoles().forEach((ret) -> {
                ret.asignarFechaHasta(fechaFinalizacion);
//                if (ret.verRazon().equals(VALORES_NULOS)) {
//                    ret.asignarRazon(TRABAJO_FINALIZACION);
//                }
                ret.asignarRazon(TRABAJO_FINALIZACION);
            });

            trabajo.verAlumnos().forEach((aet) -> {
                aet.asignarFechaHasta(fechaFinalizacion);
//                if (aet.verRazon().equals(VALORES_NULOS)) {
//                    aet.asignarRazon(TRABAJO_FINALIZACION);
//                }
                aet.asignarRazon(TRABAJO_FINALIZACION);
            });

            String resultado = this.escribirArchivo();
            if (resultado.equals(ESCRITURA_OK)) {
                this.ultimoTrabajo = this.trabajos.indexOf(trabajo);
                return EXITO;
            }
            return resultado;
        }
        return ESCRITURA_ERROR;
    }
    
    /**
     * Borra un trabajo siempre y cuando no tenga seminarios presentados
     * @param trabajo trabajo a borrar
     * @return String  - cadena con el resultado de la operación (TRABAJO_CON_SEMINARIO | ESCRITURA_ERROR | EXITO)
     */
    @Override
    public String borrarTrabajo(Trabajo trabajo) {
        if (trabajo.tieneSeminarios()) {
            return TRABAJO_CON_SEMINARIO;
        }
        
        if (this.trabajos.contains(trabajo)) {
            this.ultimoTrabajo = this.trabajos.indexOf(trabajo);
            this.trabajos.remove(trabajo);
            //        this.ultimoTrabajo = this.trabajos.size()-1; //Ultimo trabajo en la lista
//            ordenar(this.trabajos);
            Collections.sort(this.trabajos);
            String resultado = this.escribirArchivo();
            return (resultado.equals(ESCRITURA_OK) ? EXITO : resultado);
        }
        return TRABAJO_INEXISTENTE;
    }

    /**
     * Reemplaza un profesor del trabajo. 
     * Al profesor que se reemplaza se le asigna su fecha de finalización y razón por la que finaliza su tarea
     * El nuevo profesor tiene el mismo rol del profesor que reemplaza, y comienza su tarea en la fecha en que finaliza el profesor que se reemplaza
     * El nuevo profesor no puede ocupar 
     * @param trabajo trabajo al cual se reemplazará un profesor
     * @param profesorReemplazado profesor que se reemplaza
     * @param fechaHasta fecha de finalización del profesor que se reemplaza (debe ser posterior a la fecha de inicio)
     * @param razon razón por la que se reemplaza al profesor
     * @param nuevoProfesor nuevo profesor
     * @return String  - cadena con el resultado de la operación (TRABAJO_INEXISTENTE | TRABAJO_REEMPLAZAR_PROFESOR_ERROR | TRABAJO_REEMPLAZAR_PROFESOR_DUPLICADO | TRABAJO_REEMPLAZAR_PROFESOR_INEXISTENTE | TRABAJO_REEMPLAZAR_PROFESOR_ERROR | ESCRITURA_ERROR | EXITO)
     */
    @Override
    public String reemplazarProfesor(Trabajo trabajo, Profesor profesorReemplazado, LocalDate fechaHasta, String razon, Profesor nuevoProfesor) {
        IGestorRolesEnTrabajos gRet = GestorRolesEnTrabajos.instanciar();
        
        if (this.trabajos.contains(trabajo)) {
            if (razon == null) {
                return TRABAJO_REEMPLAZAR_PROFESOR_ERROR;
            }
            if (fechaHasta == null) {
                    return TRABAJO_FINALIZAR_ALUMNO_ERROR;
            }

            for (RolEnTrabajo ret : trabajo.verProfesoresConRoles()) {
                if (ret.verProfesor().equals(nuevoProfesor)) {
                    return TRABAJO_REEMPLAZAR_PROFESOR_DUPLICADO;
                }

                if(ret.verProfesor().equals(profesorReemplazado)) {
                    if (fechaHasta.isBefore(ret.verFechaDesde())) {
                        return TRABAJO_REEMPLAZAR_PROFESOR_ERROR;
                    }
                    ret.asignarFechaHasta(fechaHasta);
                    if (razon.isEmpty()) {
                        ret.asignarRazon(VALORES_NULOS); //SI NO TIENE RAZON DE BAJA LE ASIGNA UN -
                    }else{
                        ret.asignarRazon(razon);
                    }

                    //CREA UN NUEVO RET SIN FechaHasta NI Razon
                    trabajo.agregarRolEnTrabajo(gRet.nuevoRolEnTrabajo(nuevoProfesor, ret.verRol(), fechaHasta));

                    String resultado = this.escribirArchivo();
                    if (resultado.equals(ESCRITURA_OK)) {
                        this.ultimoTrabajo = this.trabajos.indexOf(trabajo);
                        return EXITO;
                    }
                    return resultado;
                }
            }
        }
        return TRABAJO_INEXISTENTE;
    }

    /**
     * Permite que un alumno pueda terminar su participación en el trabajo
     * @param trabajo trabajo al cual se finalizará la participación del alumno
     * @param alumno alumno que finaliza su participación en el trabajo
     * @param fechaHasta fecha de finalización del alumno en el trabajo (debe ser posterior a la fecha de inicio)
     * @param razon razón por la que el alumno finaliza su participación en el trabajo
     * @return String  - cadena con el resultado de la operación (TRABAJO_INEXISTENTE | TRABAJO_FINALIZAR_ALUMNO_ERROR | TRABAJO_FINALIZAR_ALUMNO_INEXISTENTE | TRABAJO_FINALIZAR_ALUMNO_ERROR | ESCRITURA_ERROR | EXITO)
     */
    @Override
    public String finalizarAlumno(Trabajo trabajo, Alumno alumno, LocalDate fechaHasta, String razon) {
        if (this.trabajos.contains(trabajo)) {
//          if (trabajo.verAlumnos().isEmpty()) {
//              return TRABAJO_FINALIZAR_ALUMNO_INEXISTENTE; //Esto se controla al crear un nuevo trabajo
//          }
            if (alumno == null) {
                return TRABAJO_FINALIZAR_ALUMNO_ERROR;
            }
            if (razon == null) {
                    return TRABAJO_FINALIZAR_ALUMNO_ERROR;
            }
            if (fechaHasta == null) {
                return TRABAJO_FINALIZAR_ALUMNO_ERROR;
            }

            for(AlumnoEnTrabajo aet : trabajo.verAlumnosActuales()){
                if (fechaHasta.isBefore(aet.verFechaDesde())) {
                    return TRABAJO_FINALIZAR_ALUMNO_ERROR;
                }
                if (aet.verAlumno().equals(alumno)) {
                    aet.asignarFechaHasta(fechaHasta);
                    if (razon.isEmpty()) {
                        aet.asignarRazon(VALORES_NULOS); //SI NO TIENE RAZON DE BAJA LE ASIGNA UN -
                    }else{
                        aet.asignarRazon(razon);
                    }

                    String resultado = this.escribirArchivo();
                    if (resultado.equals(ESCRITURA_OK)) {
                        this.ultimoTrabajo = this.trabajos.indexOf(trabajo);
                        return EXITO;
                    }
                    return resultado;
                } else {
                    return TRABAJO_FINALIZAR_ALUMNO_INEXISTENTE;
                }
            }
        }
        return TRABAJO_INEXISTENTE;
    }                    
            
    /**
     * Busca si hay al menos un trabajo con el área especificada
     * A este método lo usa la clase GestorAreas
     * @param area área a buscar
     * @return boolean  - true si hay al menos un trabajo con el área especificada
     */
    @Override
    public boolean hayTrabajosConEsteArea(Area area) {
        for(Trabajo t : this.trabajos){
            if (t.verAreas().contains(area)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve la posición del último trabajo agregado/modificado
     * Sirve para manejar la tabla tablaTrabajos
     * Si cuando se agrega/modifica un trabajo se cancela la operación, devuelve - 1
     * Cada vez que se agrega/modifica un trabajo, este valor toma la posición del trabajo agregado/modificado en el ArrayList
     * @return int  - posición del último trabajo agregado/modificado
     */    
    @Override
    public int verUltimoTrabajo() {
        return this.ultimoTrabajo;
    }
        
    /**
     * Asigna en -1 la variable que controla el último trabajo agregado/modificado
     * Sirve para manejar la tabla tablaTrabajos
     */
    @Override
    public void cancelar() {
        this.ultimoTrabajo = -1;
    }
                    
    /**
     * Lee del archivo de texto y carga el ArrayList empleando un try con recursos
     * https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
     * Formato del archivo:
     *  nombre 1
     *  nombre 2
     *  nombre 3
     * @return String  - cadena con el resultado de la operacion (ARCHIVO_INEXISTENTE | LECTURA_OK | LECTURA_ERROR)
     */
    private String leerArchivo() {
        File file = new File(NOMBRE_ARCHIVO);
        if (file.exists()) {
            IGestorAreas ga = GestorAreas.instanciar();
            IGestorPersonas gsP = GestorPersonas.instanciar();
            IGestorRolesEnTrabajos gRet = GestorRolesEnTrabajos.instanciar();
            IGestorAlumnosEnTrabajos gAet = GestorAlumnosEnTrabajos.instanciar();
            
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String cadena;
                while((cadena = br.readLine()) != null) {
                    String[] vector = cadena.split(Character.toString(SEPARADOR));
                    String cadenaTitulo = vector[0];
                    
                    //LEO LA DURACION Y LA CONVIERTO A INT
                    String cadenaDuracion = vector[1];
                    int duracion = Integer.parseInt(cadenaDuracion);
                    
                    //LEO LA CANTIDAD DE AREAS QUE HAY Y LA CONVIERTO A INT
                    String cadenaCantAreas = vector[2];
                    int cantAreas = Integer.parseInt(cadenaCantAreas);
                    
                    //LEO LAS AREAS, LAS SEPARO Y LAS CONVIERTO EN AREA PARA AGREGARLAS A UNA LIST
                    List<Area> listaAreas = new ArrayList<>();
                    for (int i = 3; i < 3 + cantAreas; ) {
                        String nombreArea = vector[i++];
                        listaAreas.add(ga.dameArea(nombreArea));
                    }
//                    String cadenaAreas = vector[3];
//                    String[] vectorArea = cadenaAreas.split(Character.toString(SEPARADOR_INTERNO));
//                    
//                    List<Area> listaAreas = new ArrayList<>();
//                    for (int i = 0; i < cantAreas; i++) {
//                        String nombreArea = vectorArea[i];
//                        listaAreas.add(ga.dameArea(nombreArea));
//                    }
                    
                    //LEO Y CONVIERTO LA FECHA A LOCALDATE
                    int posFechaP = 3 + cantAreas;
                    String cadenaFechaPresentacion = vector[posFechaP];
                    LocalDate fechaPresentacion = this.convertirStringAFecha(cadenaFechaPresentacion);
                    
                    //LEO Y CONVIERTO LA FECHA A LOCALDATE
                    int posFechaAp = posFechaP + 1;
                    String cadenaFechaAprobacion = vector[posFechaAp];
                    LocalDate fechaAprobacion = this.convertirStringAFecha(cadenaFechaAprobacion);
                    
                    //LEO Y CONVIERTO LA FECHA A LOCALDATE
                    int posFechaF = posFechaAp + 1;
                    String cadenaFechaFinalizacion = vector[posFechaF];
                    LocalDate fechaFinalizacion;
                    if (cadenaFechaFinalizacion.equals(VALORES_NULOS)) {
                        fechaFinalizacion = null;
                    } else {
                        fechaFinalizacion = this.convertirStringAFecha(cadenaFechaFinalizacion);
                    }
                    
                    //LEO LA CANTIDAD DE RET QUE HAY Y LA CONVIERTO A INT
                    int posCantProf = posFechaF + 1;
                    String cadenaCantProf = vector[posCantProf];
                    int cantProfes = Integer.parseInt(cadenaCantProf);
                    
                    //LEO LOS RET, LOS SEPARO Y LUEGO CONVIERTO SUS PARAMETROS PARA AGREGARLOS A UNA LIST
                    int posPrimerProf = posCantProf + 1;
                    List<RolEnTrabajo> listaRET = new ArrayList<>();
                    
                    for (int i = posPrimerProf; i < posPrimerProf + (cantProfes * 5); ) {
                        
                        String cadenaDNI = vector[i++];
                        int retDNI = Integer.parseInt(cadenaDNI);
                        
                        var cadenaRol = vector[i++];
                        Rol retRol = this.convertirStringARol(cadenaRol);
                        
                        String cadenaFechaDesde = vector[i++];
                        LocalDate fechaDesde = this.convertirStringAFecha(cadenaFechaDesde);
                        
                        String cadenaFechaHasta = vector[i++];
                        LocalDate fechaHasta = null;
                        if (!cadenaFechaHasta.equals(VALORES_NULOS)) {
                            fechaHasta = this.convertirStringAFecha(cadenaFechaHasta);
                        }
                        
                        String cadenaRazon = vector[i++];
                        if (cadenaRazon.equals(VALORES_NULOS)) {
                            cadenaRazon = null;
                        }
                        
                        if (fechaHasta == null || cadenaRazon == null) {
                            listaRET.add(gRet.nuevoRolEnTrabajo(gsP.dameProfesor(retDNI), retRol, fechaDesde));
                        } else {
                            listaRET.add(new RolEnTrabajo(gsP.dameProfesor(retDNI), retRol, fechaDesde, fechaHasta, cadenaRazon));
                        }
                    }
                    
//                    String cadenaProfesoresConRoles = vector[posPrimerProf];
//                    String[] vectorRET = cadenaProfesoresConRoles.split(Character.toString(SEPARADOR_INTERNO));
//
//                    for (int i = 0; i < cantProfesoresConRoles; i++) {
//                        String cadenaParamRET = vectorRET[i];
//                        String cadenaParamRET = vectorRET[i];
//                        String[] vectorParamRET = cadenaParamRET.split(Character.toString(SEPARADOR_PARAMETRO));
//                        
//                        String cadenaDNI = vectorParamRET[0];
//                        int retDNI = Integer.parseInt(cadenaDNI);
//                        
//                        var cadenaRol = vectorParamRET[1];
//                        Rol retRol = this.convertirStringARol(cadenaRol);
//                        
//                        String cadenaFechaDesde = vectorParamRET[2];
//                        LocalDate fechaDesde = this.convertirStringAFecha(cadenaFechaDesde);
//                        
//                        String cadenaFechaHasta = vectorParamRET[3];
//                        LocalDate fechaHasta = null;
//                        if (!cadenaFechaHasta.equals(VALORES_NULOS)) {
//                            fechaHasta = this.convertirStringAFecha(cadenaFechaHasta);
//                        }
//                        
//                        String cadenaRazon = vectorParamRET[4];
//                        if (cadenaRazon.equals(VALORES_NULOS)) {
//                            cadenaRazon = null;
//                        }
//                        
//                        if (fechaHasta == null || cadenaRazon == null) {
//                            listaRET.add(gRet.nuevoRolEnTrabajo(gsP.dameProfesor(retDNI), retRol, fechaDesde));
//                        } else {
//                            listaRET.add(new RolEnTrabajo(gsP.dameProfesor(retDNI), retRol, fechaDesde, fechaHasta, cadenaRazon));
//                        }
//                    }
                    
                    
                    
                    //LEO LA CANTIDAD DE AET QUE HAY Y LA CONVIERTO A INT
                    int posCantAlumnos = posPrimerProf + cantProfes * 5;
                    String cadenaCantAlumnos = vector[posCantAlumnos];
                    int cantAlumnos = Integer.parseInt(cadenaCantAlumnos);
                    
                    //LEO LOS AET, LOS SEPARO Y LUEGO CONVIERTO SUS PARAMETROS PARA AGREGARLOS A UNA LIST
                    int posPrimerAlumno = posCantAlumnos + 1;

                    
                    List<AlumnoEnTrabajo> listaAET = new ArrayList<>();
                    for (int i = posPrimerAlumno; i < posPrimerAlumno + (cantAlumnos * 4); ) {
                        String cadenaCX = vector[i++];
                        
                        String cadenaFechaDesde = vector[i++];
                        LocalDate fechaDesde = this.convertirStringAFecha(cadenaFechaDesde);
                        
                        String cadenaFechaHasta = vector[i++];
                        LocalDate fechaHasta = null;
                        if (!cadenaFechaHasta.equals(VALORES_NULOS)) {
                            fechaHasta = this.convertirStringAFecha(cadenaFechaHasta);
                        }
                        
                        String cadenaRazon = vector[i++];
                        if (cadenaRazon.equals(VALORES_NULOS)) {
                            cadenaRazon = null;
                        }
                        
                        if (fechaHasta == null || cadenaRazon == null) {
                            listaAET.add(gAet.nuevoAlumnoEnTrabajo(gsP.dameAlumno(cadenaCX), fechaDesde));
                        } else {
                            listaAET.add(new AlumnoEnTrabajo(gsP.dameAlumno(cadenaCX), fechaDesde, fechaHasta, cadenaRazon));
                        }
                    }
                    
//                    String cadenaAlumnos = vector[10];
//                    String[] vectorAET = cadenaAlumnos.split(Character.toString(SEPARADOR_INTERNO));
//                    for (int i = 0; i < cantAlumnos; i++) {
//                        String cadenaParamAET = vectorAET[i];
//                        String[] vectorParamAET = cadenaParamAET.split(Character.toString(SEPARADOR_PARAMETRO));
//                        
//                        String cadenaCX = vectorParamAET[0];
//                        
//                        String cadenaFechaDesde = vectorParamAET[1];
//                        LocalDate fechaDesde = this.convertirStringAFecha(cadenaFechaDesde);
//                        
//                        String cadenaFechaHasta = vectorParamAET[2];
//                        LocalDate fechaHasta = null;
//                        if (!cadenaFechaHasta.equals(VALORES_NULOS)) {
//                            fechaHasta = this.convertirStringAFecha(cadenaFechaHasta);
//                        }
//                        
//                        String cadenaRazon = vectorParamAET[3];
//                        if (cadenaRazon.equals(VALORES_NULOS)) {
//                            cadenaRazon = null;
//                        }
//                        
//                        if (fechaHasta == null || cadenaRazon == null) {
//                            listaAET.add(gAet.nuevoAlumnoEnTrabajo(gsP.dameAlumno(cadenaCX), fechaDesde));
//                        } else {
//                            listaAET.add(new AlumnoEnTrabajo(gsP.dameAlumno(cadenaCX), fechaDesde, fechaHasta, cadenaRazon));
//                        }
//                    }
                    
                    Trabajo unTrabajo = new Trabajo(cadenaTitulo, duracion, listaAreas, 
                                                    fechaPresentacion, fechaAprobacion, fechaFinalizacion, 
                                                    listaRET, listaAET);
                    this.trabajos.add(unTrabajo);
                }
                return LECTURA_OK;
            }
            catch (IOException ioe) {
                return LECTURA_ERROR;
            }
        }
        return ARCHIVO_INEXISTENTE;
    }                    
    
    /**
     * Escribe en el archivo de texto el ArrayList
     * Formato del archivo:
     *  nombre 1
     *  nombre 2
     *  nombre 3
     * @return String  - cadena con el resultado de la operacion (ESCRITURA_OK | ESCRITURA_ERROR)
     */
    private String escribirArchivo() {
        File file = new File(NOMBRE_ARCHIVO);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {     
            for(Trabajo unTrabajo : this.trabajos) {
                //ESCRIBO EL TITULO
                String cadena = unTrabajo.verTitulo() + SEPARADOR;
                
                //ESCRIBO LA DURACION
                cadena += Integer.toString(unTrabajo.verDuracion()) + SEPARADOR;
                
                //ESCRIBO LA CANTIDAD DE AREAS QUE TIENE EL TRABAJO
                //SI HAY MAS DE UN AREA, LAS ESCRIBO CON UN SEPARADOR DISTINTO
                int cantAreas = unTrabajo.verAreas().size();
                cadena += cantAreas;
                
                for (int i = 0; i < cantAreas; i++) {
                    cadena += SEPARADOR + unTrabajo.verAreas().get(i).verNombre();
                }
//                for (int i = 0; i < cantAreas; i++) {
//                    if (i == 0) {
//                        cadena += SEPARADOR + unTrabajo.verAreas().get(i).verNombre();
//                    } else {
//                        cadena += SEPARADOR_INTERNO + unTrabajo.verAreas().get(i).verNombre();
//                    }
//                }
                cadena += SEPARADOR;
                
                //CONVIERTO A STRING LA fechaPresentacion DANDOLE EL FORMATO PATRON Y LUEGO ESCRIBO
                String fechaPresentacion = unTrabajo.verFechaPresentacion().format(DateTimeFormatter.ofPattern(PATRON_FECHAS));
                cadena += fechaPresentacion + SEPARADOR;
                
                //CONVIERTO A STRING LA fechaAprobacion DANDOLE EL FORMATO PATRON Y LUEGO ESCRIBO
                String fechaAprobacion = unTrabajo.verFechaAprobacion().format(DateTimeFormatter.ofPattern(PATRON_FECHAS));
                cadena += fechaAprobacion + SEPARADOR;
                
                //CONVIERTO A STRING LA fechaFinalizacion DANDOLE EL FORMATO PATRON Y LUEGO ESCRIBO
                //LA fechaFinalizacion ES EL UNICO PARAMETRO QUE PUEDE SER O NO NULL
                if (unTrabajo.verFechaFinalizacion() != null) {
                    String fechaFinalizacion = unTrabajo.verFechaFinalizacion().format(DateTimeFormatter.ofPattern(PATRON_FECHAS));
                    cadena += fechaFinalizacion + SEPARADOR;
                } else {
                    cadena += VALORES_NULOS + SEPARADOR;
                }
                
                //ESCRIBO CADA RolEnTrabajo CON UN SEPARADOR DISTINTO
                int cantTutor = 0;
                int cantCotutor = 0;
                int cantJurado = 0;
                
                for (RolEnTrabajo ret : unTrabajo.verProfesoresConRoles()) {
                    if (ret.verRol() == Rol.TUTOR) {
                        cantTutor++;
                    }
                    if (ret.verRol() == Rol.COTUTOR) {
                        cantCotutor++;
                    }
                    if (ret.verRol() == Rol.JURADO) {
                        cantJurado++;
                    }
                }
                
                int cantRET = cantTutor + cantCotutor + cantJurado;
//                int cantRET = unTrabajo.cantidadProfesoresConRol();
                cadena += Integer.toString(cantRET) + SEPARADOR;

                for (int i = 0; i < cantRET; i++) {
                    cadena += Integer.toString(unTrabajo.verProfesoresConRoles().get(i).verProfesor().verDNI()) + SEPARADOR;
                    cadena += unTrabajo.verProfesoresConRoles().get(i).verRol().toString() + SEPARADOR;
                    
                    //CONVIERTO A STRING LA fechaAprobacion DANDOLE EL FORMATO PATRON Y LUEGO ESCRIBO
                    String fechaDesde = unTrabajo.verProfesoresConRoles().get(i).verFechaDesde().format(DateTimeFormatter.ofPattern(PATRON_FECHAS));
                    cadena += fechaDesde + SEPARADOR;
                    
                    //CONVIERTO A STRING LA fechaHasta DANDOLE EL FORMATO PATRON Y LUEGO ESCRIBO
                    //LA fechaHasta PUEDE SER O NO NULL
                    if (unTrabajo.verProfesoresConRoles().get(i).verFechaHasta() != null) {
                        String fechaHasta = unTrabajo.verProfesoresConRoles().get(i).verFechaHasta().format(DateTimeFormatter.ofPattern(PATRON_FECHAS));
                        cadena += fechaHasta + SEPARADOR;
                    } else {
                        cadena += VALORES_NULOS + SEPARADOR;
                    }
                    
                    //SI LA RAZON ES NULL ESCRIBO UN -, SINO LA ESCRIBO
                    //SI LLEGO AL FIN DE LOS RET, ESCRIBO PERO SIN SEPARADOR
                    if (unTrabajo.verProfesoresConRoles().get(i).verRazon() != null) {
                        cadena += unTrabajo.verProfesoresConRoles().get(i).verRazon() + SEPARADOR;
                    } else {
                        cadena += VALORES_NULOS + SEPARADOR;
                    }
                }
//                for (int i = 0; i < cantRET; i++) {
//                    cadena += Integer.toString(unTrabajo.verProfesoresConRoles().get(i).verProfesor().verDni()) + SEPARADOR_PARAMETRO;
//                    cadena += unTrabajo.verProfesoresConRoles().get(i).verRol().toString().toLowerCase() + SEPARADOR_PARAMETRO;
//                    
//                    //CONVIERTO A STRING LA fechaAprobacion DANDOLE EL FORMATO PATRON Y LUEGO ESCRIBO
//                    String fechaDesde = unTrabajo.verProfesoresConRoles().get(i).verFechaDesde().format(DateTimeFormatter.ofPattern(PATRON_FECHAS));
//                    cadena += fechaDesde + SEPARADOR_PARAMETRO;
//                    
//                    //CONVIERTO A STRING LA fechaHasta DANDOLE EL FORMATO PATRON Y LUEGO ESCRIBO
//                    //LA fechaHasta PUEDE SER O NO NULL
//                    if (unTrabajo.verProfesoresConRoles().get(i).verFechaHasta() != null) {
//                        String fechaHasta = unTrabajo.verProfesoresConRoles().get(i).verFechaHasta().format(DateTimeFormatter.ofPattern(PATRON_FECHAS));
//                        cadena += fechaHasta + SEPARADOR_PARAMETRO;
//                    } else {
//                        cadena += VALORES_NULOS + SEPARADOR_PARAMETRO;
//                    }
//                    
//                    //SI LA RAZON ES NULL ESCRIBO UN -, SINO LA ESCRIBO
//                    //SI LLEGO AL FIN DE LOS RET, ESCRIBO PERO SIN SEPARADOR
//                    if (unTrabajo.verProfesoresConRoles().get(i).verRazon() != null) {
//                        if (unTrabajo.verProfesoresConRoles().indexOf(unTrabajo.verProfesoresConRoles().get(i)) != unTrabajo.verProfesoresConRoles().size()) {
//                            cadena += unTrabajo.verProfesoresConRoles().get(i).verRazon() + SEPARADOR_INTERNO;
//                        } else {
//                            cadena += unTrabajo.verProfesoresConRoles().get(i).verRazon();
//                        }
//                    } else {
////                        if (unTrabajo.verProfesoresConRoles().indexOf(unTrabajo.verProfesoresConRoles().get(i)) != unTrabajo.verProfesoresConRoles().size()) {
//                        if (unTrabajo.verProfesoresConRoles().indexOf(unTrabajo.verProfesoresConRoles().get(i)) != cantRET - 1) {
//                            cadena += VALORES_NULOS + SEPARADOR_INTERNO;
//                        } else {
//                            cadena += VALORES_NULOS;
//                        }
//                    }
//                }
                
                //ESCRIBO CADA AlumnoEnTrabajo CON UN SEPARADOR DISTINTO
                int cantAET = unTrabajo.verAlumnos().size();
                cadena += Integer.toString(cantAET) + SEPARADOR;
                
                for (int i = 0; i < cantAET; i++) {
                    cadena += unTrabajo.verAlumnos().get(i).verAlumno().verCX() + SEPARADOR;
                    
                    //CONVIERTO A STRING LA fechaAprobacion DANDOLE EL FORMATO PATRON Y LUEGO ESCRIBO
                    String fechaDesde = unTrabajo.verAlumnos().get(i).verFechaDesde().format(DateTimeFormatter.ofPattern(PATRON_FECHAS));
                    cadena += fechaDesde + SEPARADOR;
                    
                    //CONVIERTO A STRING LA fechaHasta DANDOLE EL FORMATO PATRON Y LUEGO ESCRIBO
                    //LA fechaHasta PUEDE SER O NO NULL
                    if (unTrabajo.verAlumnos().get(i).verFechaHasta() != null) {
                        String fechaHasta = unTrabajo.verAlumnos().get(i).verFechaHasta().format(DateTimeFormatter.ofPattern(PATRON_FECHAS));
                        cadena += fechaHasta + SEPARADOR;
                    } else {
                        cadena += VALORES_NULOS + SEPARADOR;
                    }
                    
                    //SI LA RAZON ES NULL ESCRIBO UN -, SINO LA ESCRIBO
                    //SI LLEGO AL FIN DE LOS AET, ESCRIBO PERO SIN SEPARADOR
                    if (unTrabajo.verAlumnos().get(i).verRazon() != null) {
                        if (unTrabajo.verAlumnos().indexOf(unTrabajo.verAlumnos().get(i)) != unTrabajo.verAlumnos().size()) {
                            cadena += unTrabajo.verAlumnos().get(i).verRazon() + SEPARADOR;
                        } else {
                            cadena += unTrabajo.verAlumnos().get(i).verRazon();
                        }
                    } else {
//                        if (unTrabajo.verAlumnos().indexOf(unTrabajo.verAlumnos().get(i)) != unTrabajo.verAlumnos().size()) {
                        if (unTrabajo.verAlumnos().indexOf(unTrabajo.verAlumnos().get(i)) != cantAET - 1) {
                            cadena += VALORES_NULOS + SEPARADOR;
                        } else {
                            cadena += VALORES_NULOS;
                        }
                    }
                }
//                for (int i = 0; i < cantAET; i++) {
//                    cadena += unTrabajo.verAlumnos().get(i).verAlumno().verCX() + SEPARADOR_PARAMETRO;
//                    
//                    //CONVIERTO A STRING LA fechaAprobacion DANDOLE EL FORMATO PATRON Y LUEGO ESCRIBO
//                    String fechaDesde = unTrabajo.verAlumnos().get(i).verFechaDesde().format(DateTimeFormatter.ofPattern(PATRON_FECHAS));
//                    cadena += fechaDesde + SEPARADOR_PARAMETRO;
//                    
//                    //CONVIERTO A STRING LA fechaHasta DANDOLE EL FORMATO PATRON Y LUEGO ESCRIBO
//                    //LA fechaHasta PUEDE SER O NO NULL
//                    if (unTrabajo.verAlumnos().get(i).verFechaHasta() != null) {
//                        String fechaHasta = unTrabajo.verAlumnos().get(i).verFechaHasta().format(DateTimeFormatter.ofPattern(PATRON_FECHAS));
//                        cadena += fechaHasta + SEPARADOR_PARAMETRO;
//                    } else {
//                        cadena += VALORES_NULOS + SEPARADOR_PARAMETRO;
//                    }
//                    
//                    //SI LA RAZON ES NULL ESCRIBO UN -, SINO LA ESCRIBO
//                    //SI LLEGO AL FIN DE LOS AET, ESCRIBO PERO SIN SEPARADOR
//                    if (unTrabajo.verAlumnos().get(i).verRazon() != null) {
//                        if (unTrabajo.verAlumnos().indexOf(unTrabajo.verAlumnos().get(i)) != unTrabajo.verAlumnos().size()) {
//                            cadena += unTrabajo.verAlumnos().get(i).verRazon() + SEPARADOR_INTERNO;
//                        } else {
//                            cadena += unTrabajo.verAlumnos().get(i).verRazon();
//                        }
//                    } else {
////                        if (unTrabajo.verAlumnos().indexOf(unTrabajo.verAlumnos().get(i)) != unTrabajo.verAlumnos().size()) {
//                        if (unTrabajo.verAlumnos().indexOf(unTrabajo.verAlumnos().get(i)) != cantAET - 1) {
//                            cadena += VALORES_NULOS + SEPARADOR_INTERNO;
//                        } else {
//                            cadena += VALORES_NULOS;
//                        }
//                    }
//                }
                
                bw.write(cadena);
                bw.newLine();
            }
            return ESCRITURA_OK;
        } 
        catch (IOException ioe) {
            return ESCRITURA_ERROR;            
        }
    }
    
    private LocalDate convertirStringAFecha(String cadenaFecha){
        String[] vector = cadenaFecha.split(Character.toString(SEPARADOR_FECHAS));
        
        int day = Integer.parseInt(vector[0]);
        int month = Integer.parseInt(vector[1]);
        int year = Integer.parseInt(vector[2]);
        
        return LocalDate.of(year, month, day);
    }
    
    private Rol convertirStringARol(String cadenaRol){
        switch(cadenaRol){
            case "TUTOR": return Rol.TUTOR;
            case "COTUTOR": return Rol.COTUTOR;
//            case "JURADO": return Rol.JURADO;
            default: return Rol.JURADO;
        }
    }
    
}
