/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.seminarios.modelos;

import gui.trabajos.modelos.GestorTrabajos;
import gui.trabajos.modelos.Trabajo;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Benjamin
 */
public class ModeloTabla extends AbstractTableModel {
    private final String fechas = "Fechas";
    private final String notas = "Nota";
    private final String observaciones = "Observaciones";
    private Trabajo elTrabajo;
    
    public ModeloTabla(Trabajo elTrabajo){
        GestorTrabajos gt = GestorTrabajos.instanciar();
        this.elTrabajo = gt.dameTrabajo(elTrabajo.verTitulo());
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                try{
                    return verFechaExpSeminario(elTrabajo.verSeminarios().get(rowIndex));
                }
                catch(IndexOutOfBoundsException e){
                    System.out.println("Indexado inexistente.");
                }
            
            case 1:
                try{
                    return elTrabajo.verSeminarios().get(rowIndex).verNotaAprobacion().toString();
                }
                catch(IndexOutOfBoundsException e){
                    System.out.println("Indexado inexistente.");
                }
                
            case 2:
                try{
                    return elTrabajo.verSeminarios().get(rowIndex).verObservaciones();
                }
                catch(IndexOutOfBoundsException e){
                    System.out.println("Indexado inexistente.");
                }
        }
        return false;
    }

    @Override
    public int getRowCount() {
        try{
            return this.elTrabajo.cantidadSeminarios();
        }
        catch(NullPointerException e){
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return 3;
    }
    
    @Override
    public String getColumnName(int columna) {
        switch(columna){
            case 0: return fechas;
            case 1: return notas;
            case 2: return observaciones;
            default: return null;
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
      
    /**
     * Muestra la fecha de exposición (en formato String) del seminario que se ingresa.
     * @param seminario
     * @return 
     */
    private String verFechaExpSeminario(Seminario seminario){
        String formato = "dd/MM/yyyy";
        String fFormateada = seminario.verFechaExposicion().format(DateTimeFormatter.ofPattern(formato));
        return fFormateada;
    }
}
