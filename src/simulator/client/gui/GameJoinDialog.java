/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.client.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import simulator.client.MultiplayerLogic;

/**
 *
 * @author 82wach1bif
 */
public class GameJoinDialog extends javax.swing.JDialog {

    public DefaultListModel<String> gameListModel = new DefaultListModel<>();

    /**
     * Creates new form GameList
     */
    private GameJoinDialog(JFrame parent, MultiplayerLogic logic) {
        super(parent, true);
        logic.listGames().forEach(gameListModel::addElement);
        initComponents();
        jButtonCancel.addActionListener((l) -> {
            this.setVisible(false);
        });

        jButtonOk.addActionListener((l) -> {
            this.setVisible(false);
            logic.joinGame(jList.getSelectedValue(), jPasswordFieldCode.getText(), jTextFieldPlayerName.getText());
        });

        jButtonOk.setEnabled(false);

        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                validateInput();
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };

        jList.addListSelectionListener((a) -> validateInput());
        jTextFieldPlayerName.addKeyListener(keyListener);
        jPasswordFieldCode.addKeyListener(keyListener);
    }

    private void validateInput() {
        jButtonOk.setEnabled(jList.getSelectedValue() != null
                && !jTextFieldPlayerName.getText().isEmpty()
                && !jPasswordFieldCode.getText().isEmpty());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jButtonCancel = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldPlayerName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPasswordFieldCode = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jList.setModel(gameListModel);
        jScrollPane1.setViewportView(jList);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.PAGE_START);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonCancel.setLabel("Cancel");
        jPanel1.add(jButtonCancel);

        jButtonOk.setText("Ok");
        jPanel1.add(jButtonOk);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.GridLayout(2, 2));

        jLabel1.setText("Playername");
        jPanel2.add(jLabel1);
        jPanel2.add(jTextFieldPlayerName);

        jLabel2.setText("Code");
        jPanel2.add(jLabel2);
        jPanel2.add(jPasswordFieldCode);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void openDialog(JFrame owner, MultiplayerLogic logic) {
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
            java.util.logging.Logger.getLogger(GameJoinDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameJoinDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameJoinDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameJoinDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            GameJoinDialog dialog = new GameJoinDialog(owner, logic);
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordFieldCode;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldPlayerName;
    // End of variables declaration//GEN-END:variables
}
