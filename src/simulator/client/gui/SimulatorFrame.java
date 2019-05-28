/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.client.gui;

import java.awt.Color;
import javax.swing.JOptionPane;
import simulator.Point;
import simulator.SimulatorMap;
import simulator.client.MultiplayerLogic;

/**
 *
 * @author 82wach1bif
 */
public class SimulatorFrame extends javax.swing.JFrame {

    private MultiplayerLogic logic;
    private SimulatorMap map;

    private class LogicListener implements MultiplayerLogic.Listener {

        @Override
        public void onMessage(String msg) {
            SimulatorFrame.this.jTextAreaMessages.setText(SimulatorFrame.this.jTextAreaMessages.getText() + msg + "\n");
        }

        @Override
        public void onMapLoaded(SimulatorMap map) {
            SimulatorFrame.this.mapComponent.setMap(map);
        }

        @Override
        public void onPlayerTurn(Point point, Color color) {
            SimulatorFrame.this.mapComponent.onPlayerTurn(point, color);
        }

        @Override
        public void onGameFinished() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onGameStateChange(MultiplayerLogic.GameState state) {
            //jMenuItemOpenEditor.setEnabled(state == MultiplayerLogic.GameState.CONNECTED);
            //jMenuItemGameJoin.setEnabled(state == MultiplayerLogic.GameState.CONNECTED);
            jMenuItemGameStart.setEnabled(state == MultiplayerLogic.GameState.MAP_LOADED);

        }

        @Override
        public void awaitPlayerTurn(Point point) {
            mapComponent.requestFocus();
            SimulatorFrame.this.mapComponent.awaitPlayerTurn(point, (turn) -> {
                logic.setPlayerTurn(turn);
            });
        }
    }

    /**
     * Creates new form SimulatorFrame
     */
    public SimulatorFrame() {
        initComponents();
        logic = new MultiplayerLogic(new LogicListener());
        logic.connect("localhost");

        jMenuItemOpenEditor.addActionListener((a) -> {
           SimulatorEditor.open();
        });

        jMenuItemGameStart.addActionListener((a) -> {
            int playerCount = Integer.parseUnsignedInt(JOptionPane.showInputDialog(this, "Wie viele Spieler ?"));

            // logic.startGame(playerCount);
        });

        jMenuItemGameQuit.addActionListener((a) -> {
            logic.quitGame();
            System.exit(0);
        });

        jMenuItemGameJoin.addActionListener((a) -> {
            GameJoinDialog.openDialog(this, logic);
        });

        jTextFieldMessage.addActionListener((a) -> {
            logic.sendMessage(jTextFieldMessage.getText());
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jTextAreaMessages = new javax.swing.JTextArea();
        jTextFieldMessage = new javax.swing.JTextField();
        mapComponent = new simulator.client.gui.MapComponent();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemOpenEditor = new javax.swing.JMenuItem();
        jMenuItemGameStart = new javax.swing.JMenuItem();
        jMenuItemGameJoin = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemGameQuit = new javax.swing.JMenuItem();

        jMenuItem3.setText("jMenuItem3");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulator");

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));

        jTextAreaMessages.setEditable(false);
        jTextAreaMessages.setColumns(20);
        jTextAreaMessages.setRows(5);
        jPanel1.add(jTextAreaMessages);
        jPanel1.add(jTextFieldMessage);

        jScrollPane1.setViewportView(jPanel1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout mapComponentLayout = new javax.swing.GroupLayout(mapComponent);
        mapComponent.setLayout(mapComponentLayout);
        mapComponentLayout.setHorizontalGroup(
            mapComponentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 773, Short.MAX_VALUE)
        );
        mapComponentLayout.setVerticalGroup(
            mapComponentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 376, Short.MAX_VALUE)
        );

        getContentPane().add(mapComponent, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Spiel");

        jMenuItemOpenEditor.setText("Öffne Editor");
        jMenuItemOpenEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenEditorActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemOpenEditor);

        jMenuItemGameStart.setText("Spiel starten");
        jMenu1.add(jMenuItemGameStart);

        jMenuItemGameJoin.setText("Spiel beitreten");
        jMenu1.add(jMenuItemGameJoin);
        jMenu1.add(jSeparator1);

        jMenuItemGameQuit.setText("Beenden");
        jMenu1.add(jMenuItemGameQuit);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemOpenEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenEditorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemOpenEditorActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SimulatorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SimulatorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SimulatorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SimulatorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new SimulatorFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItemGameJoin;
    private javax.swing.JMenuItem jMenuItemGameQuit;
    private javax.swing.JMenuItem jMenuItemGameStart;
    private javax.swing.JMenuItem jMenuItemOpenEditor;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTextArea jTextAreaMessages;
    private javax.swing.JTextField jTextFieldMessage;
    private simulator.client.gui.MapComponent mapComponent;
    // End of variables declaration//GEN-END:variables
}
