/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.seminarios.modelos;

import gui.interfaces.IGestorSeminarios;
import gui.trabajos.modelos.GestorTrabajos;
import gui.trabajos.modelos.Trabajo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Benjamin
 */
public class GestorSeminarios implements IGestorSeminarios {
    private static GestorSeminarios gestor;
    private Trabajo elTrabajo;
    
    private static final String SEPARADOR = ",";
    
    private File ARCHIVO = new File("Seminarios.txt");
    
    private GestorSeminarios(Trabajo elTrabajo){
        this.elTrabajo = elTrabajo;
        this.leerArchivo();
    }
    
//    private GestorSeminarios(){
//        leerArchivo();
//    }
    
    public static GestorSeminarios instanciar(Trabajo elTrabajo){
        if(gestor == null)
            gestor = new GestorSeminarios(elTrabajo);
        return gestor;
    }
    
//    public static GestorSeminarios instanciar(){
//        if(gestor == null)
//            gestor = new GestorSeminarios();
//        return gestor;
//    }
    
    /**
     * Valida que estén correctos los datos para crear un nuevo seminario
     * Si el seminario está aprobado con observaciones, o desaprobado, se deben especificar las observaciones
     * @param fechaExposicion fecha de exposición del seminario
     * @param notaAprobacion nota de aprobación del seminario
     * @param observaciones observaciones del seminario
     * @return String  - cadena con el resultado de la validación (ERROR | ERROR_OBSERVACIONES | DATOS_CORRECTOS)
     */
    @Override
    public String validarSeminario(LocalDate fechaExposicion, NotaAprobacion notaAprobacion, String observaciones) {
        if(fechaExposicion == null) return ERROR;
        if((notaAprobacion == NotaAprobacion.APROBADO_CO || notaAprobacion == NotaAprobacion.DESAPROBADO) && observaciones.isEmpty()) return ERROR_OBSERVACIONES;
        if(notaAprobacion == NotaAprobacion.APROBADO_SO && !observaciones.isEmpty()) return ERROR_OBSERVACIONES;
        return DATOS_CORRECTOS;
    }

    /**
     * Valida que estén correctos los datos para crear un nuevo seminario
     * Si el seminario está aprobado con observaciones, o desaprobado, se deben especificar las observaciones
     * @param notaAprobacion nota de aprobación del seminario
     * @param observaciones observaciones del seminario
     * @return String  - cadena con el resultado de la validación (ERROR | ERROR_OBSERVACIONES | DATOS_CORRECTOS)
     */
    @Override
    public String validarSeminario(NotaAprobacion notaAprobacion, String observaciones) {
        if((notaAprobacion == NotaAprobacion.APROBADO_CO || notaAprobacion == NotaAprobacion.DESAPROBADO) && observaciones.isEmpty()) return ERROR_OBSERVACIONES;
        if(notaAprobacion == NotaAprobacion.APROBADO_SO && !observaciones.isEmpty()) return ERROR_OBSERVACIONES;
        return DATOS_CORRECTOS;
    }

    /**
     * Guarda todos los seminarios de todos los trabajos
     * @return String  - cadena con el resultado de la operacion (ESCRITURA_OK | ESCRITURA_ERROR)
     */
    @Override
    public String guardarSeminarios() {
        GestorTrabajos gt = GestorTrabajos.instanciar();
        List<Trabajo> trabajos = gt.buscarTrabajos(null);
        for(Trabajo t : trabajos){
            escribirArchivo(t.verSeminarios());
        }
        return ESCRITURA_OK;
    }
    
    /**
     *  Para este método necesito verificar que:
     * - el trabajo no haya finalizado
     * - el trabajo no tenga un seminario expuesto en la fecha de exposición
     * - la fecha de exposición del seminario sea posterior a la fecha de aprobación del trabajo
     * @param fechaExposicion
     * @param notaAprobacion
     * @param observaciones
     * @return String - cadena que informa si se pudo agregar un seminario (EXITO)
     */
    public String nuevoSeminario(LocalDate fechaExposicion, NotaAprobacion notaAprobacion, String observaciones){
        Seminario nuevoSeminario = new Seminario(fechaExposicion, notaAprobacion, observaciones);
        String resultado = validarSeminario(nuevoSeminario.verFechaExposicion(),nuevoSeminario.verNotaAprobacion(),nuevoSeminario.verObservaciones());
        if(!resultado.equals(DATOS_CORRECTOS))
            return resultado;
        if(this.elTrabajo.verSeminarios().contains(nuevoSeminario))
            return DUPLICADOS;
        if(trabajoFinalizado(this.elTrabajo))
            return TRABAJO_FINALIZADO;
        if(this.elTrabajo.verFechaAprobacion() == null)
            return ERROR;
        if(nuevoSeminario.verFechaExposicion().isBefore(this.elTrabajo.verFechaAprobacion()))
            return ERROR_FECHA_EXPOSICION;
        this.elTrabajo.agregarSeminario(nuevoSeminario);
        this.sortList();
        escribirArchivo(this.elTrabajo.verSeminarios());
        return EXITO;
    }
    
    private boolean trabajoFinalizado(Trabajo trabajo){
        return trabajo.verAlumnos().isEmpty() && trabajo.verProfesoresConRoles().isEmpty();
    }
    
    public String modificarSeminario(Seminario seminario, NotaAprobacion notaAprobacion, String observaciones){
        String resultado = validarSeminario(notaAprobacion, observaciones);
        if(!resultado.equals(DATOS_CORRECTOS))
            return resultado;
        seminario.asignarNotaAprobacion(notaAprobacion);
        seminario.asignarObservaciones(observaciones);
        escribirArchivo(this.elTrabajo.verSeminarios());
        return EXITO;
    }
    
    public void sortList(){
        sortList(fComp, elTrabajo);
    }
    
    private void sortList(Comparator<Seminario> comparador, Trabajo enTrabajo){
        Collections.sort(enTrabajo.verSeminarios(), comparador);
    }
    
    Comparator<Seminario> fComp = (s1, s2) -> {
        return s1.verFechaExposicion().compareTo(s2.verFechaExposicion());
    };
    
    private void escribirArchivo(List<Seminario> seminarios){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))){
            for(Seminario s : seminarios){
                
                String formato = "dd/MM/yyyy";
                String fFormateada = s.verFechaExposicion().format(DateTimeFormatter.ofPattern(formato));
                
                String nota = s.verNotaAprobacion().toString();
                
                String observaciones = s.verObservaciones();
                
                bw.write(fFormateada + SEPARADOR + nota + SEPARADOR + " " + observaciones + SEPARADOR);
                bw.newLine();
            }
        }
        catch(IOException e){
            System.out.println(ARCHIVO_INEXISTENTE + " " + ARCHIVO.getName());
        }
    }
    
    /**
     * Lee el archivo Areas.txt
     */
    private void leerArchivo(){
        if(ARCHIVO.exists()){
            try(BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))){
                String cadena;
                while((cadena = br.readLine()) != null){
                    String[] cadenas;
                    cadenas = cadena.split(SEPARADOR);
                    
                    String[] fecha = cadenas[0].split("/");
                    int dia = Integer.parseInt(fecha[0]);
                    int mes = Integer.parseInt(fecha[1]);
                    int anio = Integer.parseInt(fecha[2]);
                    LocalDate nuevaFecha = LocalDate.of(anio,mes,dia);
                    
                    String cadenaNota;
                    switch(cadenas[1]){
                        case "Aprobado S/O":
                            cadenaNota = "APROBADO_SO";
                            break;
                        case "Aprobado C/O":
                            cadenaNota = "APROBADO_CO";
                            break;
                        case "Desaprobado":
                            cadenaNota = "DESAPROBADO";
                            break;
                        default:
                            cadenaNota = null;
                    }
                    NotaAprobacion nota = null;
                    try {
                        nota = NotaAprobacion.valueOf(cadenaNota);
                    }
                    catch(IllegalArgumentException e) {
                        System.out.println("No se pasó un valor correcto de la enumeración.");
                    }
                    
                    String observaciones = cadenas[2];
                    Seminario seminario = new Seminario(nuevaFecha, nota, observaciones.trim());
                    elTrabajo.agregarSeminario(seminario);
                }
            }
            catch(NumberFormatException e){
                System.out.println("No se puede convertir una cadena a enteros.");
            }
            catch (IOException e) {
                System.out.println("No se puede acceder al archivo " + ARCHIVO.getName() );
            }
        }
    }
    
    /**
     * Muestra la fecha de exposición (en formato String) del seminario que se ingresa.
     * @param seminario
     * @return 
     */
    public String verFechaExpSeminario(Seminario seminario){
        String formato = "dd/MM/yyyy";
        String fFormateada = seminario.verFechaExposicion().format(DateTimeFormatter.ofPattern(formato));
        return fFormateada;
    }
    
    public Seminario getSeminario(int pos) {
        return this.elTrabajo.verSeminarios().get(pos);
    }
    
    public int getCantidad(){
        try{
            return this.elTrabajo.verSeminarios().size();
        }
        catch(NullPointerException e){
            return 0;
        }
    }
}
