/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trabajos.vistas;

import com.toedter.calendar.JDateChooser;
import gui.interfaces.IControladorModificarAlumno;
import java.awt.Dialog;
import javax.swing.JDialog;
import javax.swing.JTextField;


public class VentanaModificarAlumno extends JDialog{
    private IControladorModificarAlumno controlador;

    /**
     * Constructor 
     * @param controlador controlador de la ventana
     * @param ventanaPadre ventana padre (VentanaAMTrabajo en este caso)
     */
    public VentanaModificarAlumno(IControladorModificarAlumno controlador, Dialog ventanaPadre) {
        super(ventanaPadre, true);
        this.controlador = controlador;
        initComponents();
    }

    public JTextField verTxtRazon() {
        return txtRazon;
    }

    public JDateChooser verFechaDesde() {
        return fechaDesde;
    }

    public JDateChooser verFechaHasta() {
        return dateHasta;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCancelar = new javax.swing.JButton();
        btnAceptar = new javax.swing.JButton();
        txtRazon = new javax.swing.JTextField();
        etqRazon = new javax.swing.JLabel();
        etqFechaHasta = new javax.swing.JLabel();
        dateHasta = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        fechaDesde = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnCancelar.setMnemonic('A');
        btnCancelar.setText("Cancelar");
        btnCancelar.setToolTipText("");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarClic(evt);
            }
        });

        btnAceptar.setMnemonic('A');
        btnAceptar.setText("Aceptar");
        btnAceptar.setToolTipText("Modificar Profesor");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarClic(evt);
            }
        });

        txtRazon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRazonPresionarTecla(evt);
            }
        });

        etqRazon.setText("Razon:");

        etqFechaHasta.setText("Hasta:");

        dateHasta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFechaHastaPresionarTecla(evt);
            }
        });

        jLabel1.setText("Desde:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(etqRazon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addComponent(txtRazon, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(etqFechaHasta)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dateHasta, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                            .addComponent(fechaDesde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(fechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(etqFechaHasta)
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRazon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(etqRazon))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancelar)
                            .addComponent(btnAceptar)))
                    .addComponent(dateHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarClic(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarClic
        this.controlador.btnCancelarClic(evt);
    }//GEN-LAST:event_btnCancelarClic

    private void btnAceptarClic(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarClic
        this.controlador.btnAceptarClic(evt);
    }//GEN-LAST:event_btnAceptarClic

    private void txtRazonPresionarTecla(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRazonPresionarTecla
        this.controlador.txtRazonPresionarTecla(evt);
    }//GEN-LAST:event_txtRazonPresionarTecla

    private void txtFechaHastaPresionarTecla(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaHastaPresionarTecla
        this.controlador.fechaHastaPresionarTecla(evt);
    }//GEN-LAST:event_txtFechaHastaPresionarTecla

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private com.toedter.calendar.JDateChooser dateHasta;
    private javax.swing.JLabel etqFechaHasta;
    private javax.swing.JLabel etqRazon;
    private com.toedter.calendar.JDateChooser fechaDesde;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtRazon;
    // End of variables declaration//GEN-END:variables
}
