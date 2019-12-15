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
    
    private static final String SEPARADOR = "~";
    
    private File ARCHIVO = new File("Seminarios.txt");
    
    private GestorSeminarios(){
        this.leerArchivo();
    }
    
    public static GestorSeminarios instanciar(){
        if(gestor == null)
            gestor = new GestorSeminarios();
        return gestor;
    }
    
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
        this.escribirArchivo(gt.buscarTrabajos(null));
        for(Trabajo t : gt.buscarTrabajos(null))
            this.sortList(t);
        return ESCRITURA_OK;
    }
//    
//    /**
//     *  Para este método necesito verificar que:
//     * - el trabajo no haya finalizado
//     * - el trabajo no tenga un seminario expuesto en la fecha de exposición
//     * - la fecha de exposición del seminario sea posterior a la fecha de aprobación del trabajo
//     * @param elTrabajo
//     * @param fechaExposicion
//     * @param notaAprobacion
//     * @param observaciones
//     * @return String - cadena que informa si se pudo agregar un seminario (EXITO)
//     */
//    public String nuevoSeminario(Trabajo elTrabajo, LocalDate fechaExposicion, NotaAprobacion notaAprobacion, String observaciones){
//        Seminario nuevoSeminario = new Seminario(fechaExposicion, notaAprobacion, observaciones);
//        String resultado = validarSeminario(nuevoSeminario.verFechaExposicion(),nuevoSeminario.verNotaAprobacion(),nuevoSeminario.verObservaciones());
//        if(!resultado.equals(DATOS_CORRECTOS))
//            return resultado;
//        if(elTrabajo.verSeminarios().contains(nuevoSeminario))
//            return DUPLICADOS;
//        if(trabajoFinalizado(elTrabajo))
//            return TRABAJO_FINALIZADO;
//        if(elTrabajo.verFechaAprobacion() == null)
//            return ERROR;
//        if(nuevoSeminario.verFechaExposicion().isBefore(elTrabajo.verFechaAprobacion()))
//            return ERROR_FECHA_EXPOSICION;
//        elTrabajo.agregarSeminario(nuevoSeminario);
//        sortList(elTrabajo);
//        return EXITO;
//    }
//    
//    private boolean trabajoFinalizado(Trabajo trabajo){
//        return trabajo.verAlumnos().isEmpty() && trabajo.verProfesoresConRoles().isEmpty();
//    }
//    
//    public String modificarSeminario(Seminario seminario, NotaAprobacion notaAprobacion, String observaciones){
//        String resultado = validarSeminario(notaAprobacion, observaciones);
//        if(!resultado.equals(DATOS_CORRECTOS))
//            return resultado;
//        seminario.asignarNotaAprobacion(notaAprobacion);
//        seminario.asignarObservaciones(observaciones);
//        return EXITO;
//    }
    
    private void sortList(Trabajo elTrabajo){
        sortList(fComp, elTrabajo);
    }
    
    private void sortList(Comparator<Seminario> comparador, Trabajo enTrabajo){
        Collections.sort(enTrabajo.verSeminarios(), comparador);
    }
    
    Comparator<Seminario> fComp = (s1, s2) -> {
        return (-1)*s1.verFechaExposicion().compareTo(s2.verFechaExposicion());
    };
    
    /**
     * Sobrescribe lo que hay en el archivo ARCHIVO, de nombre Seminarios.txt
     * Escribe todos los seminarios de una lista de seminarios.
     * @param seminarios 
     */
    private void escribirArchivo(List<Trabajo> trabajos){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))){
            for(Trabajo t : trabajos){
                for(Seminario s : t.verSeminarios()){
                    String titulo = t.verTitulo();

                    String formato = "dd/MM/yyyy";
                    String fFormateada = s.verFechaExposicion().format(DateTimeFormatter.ofPattern(formato));

                    String nota = s.verNotaAprobacion().toString();

                    String observaciones = s.verObservaciones();

                    bw.write(titulo + SEPARADOR + fFormateada + SEPARADOR + nota + SEPARADOR + " " + observaciones + SEPARADOR);
                    bw.newLine();
                }
            }
        }
        catch(IOException e){
            System.out.println(ARCHIVO_INEXISTENTE + " " + ARCHIVO.getName());
        }
    }
    
    /**
     * Lee el archivo Seminarios.txt
     * La idea es que se recorra todo el archivo de una vez, y que, por cada
     * línea, se analice primero el nombre de un trabajo.
     * Al existir una coincidencia con un trabajo de la lista de todos los
     * trabajos, se leerá el resto de atributos de la línea para agregar el
     * seminario al trabajo encontrado.
     */
    private void leerArchivo(){
        int b = 0;
        if(ARCHIVO.exists()){
            try(BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))){
                String cadena;
                while((cadena = br.readLine()) != null){
                    String[] cadenas;
                    cadenas = cadena.split(SEPARADOR);
                    
                    //Borrado artificial del primer caracter del título del primer trabajo por error en la comparación
                    if(b == 0)
                        cadenas[0] = cadenas[0].substring(1);
                    
                    GestorTrabajos gt = GestorTrabajos.instanciar();
                    List<Trabajo> trabajosBuscados = gt.buscarTrabajos(cadenas[0]);
                    if(trabajosBuscados.size() == 1){
                        Trabajo trabajoConNuevoSeminario = trabajosBuscados.get(0);

                        String[] fecha = cadenas[1].split("/");
                        int dia = Integer.parseInt(fecha[0]);
                        int mes = Integer.parseInt(fecha[1]);
                        int anio = Integer.parseInt(fecha[2]);
                        LocalDate nuevaFecha = LocalDate.of(anio,mes,dia);

                        String cadenaNota;
                        switch(cadenas[2]){
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

                        String observaciones = cadenas[3];
                        Seminario seminario = new Seminario(nuevaFecha, nota, observaciones.trim());
                        trabajoConNuevoSeminario.agregarSeminario(seminario);
                        this.sortList(trabajoConNuevoSeminario);
                    }
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
}
