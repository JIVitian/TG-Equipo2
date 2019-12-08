/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.seminarios.modelos;

import gui.trabajos.modelos.Trabajo;
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
    private String filtro;
    private GestorSeminarios gs;
    private List<Seminario> buscados = null;
    
    public ModeloTabla(Trabajo elTrabajo){
        this.elTrabajo = elTrabajo;
        gs = GestorSeminarios.instanciar();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                try{
                    return gs.verFechaExpSeminario(elTrabajo.verSeminarios().get(rowIndex));
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
}
