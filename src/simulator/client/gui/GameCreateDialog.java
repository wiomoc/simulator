package simulator.client.gui;

import simulator.client.MultiplayerLogic;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author 82wach1bif
 */
public class GameCreateDialog extends JDialog {

    private DefaultListModel<String> gameListModel = new DefaultListModel<>();

    private GameCreateDialog(JFrame parent, MultiplayerLogic logic) {
        super(parent, true);
        logic.listMaps().forEach(gameListModel::addElement);
        initComponents();
        jButtonCancel.addActionListener((l) -> {
            this.setVisible(false);
        });

        jButtonOk.addActionListener((l) -> {
            this.setVisible(false);
            logic.createGame(jTextFieldGameName.getText(),jList.getSelectedValue(), jTextFieldPlayerName.getText(), (int) jSpinnerPlayerCount.getValue());
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
        jTextFieldGameName.addKeyListener(keyListener);
        jTextFieldPlayerName.addKeyListener(keyListener);
        jSpinnerPlayerCount.addChangeListener((c) -> validateInput());

    }

    private void validateInput() {
        jButtonOk.setEnabled(jList.getSelectedValue() != null
                && !jTextFieldGameName.getText().isEmpty()
                && !jTextFieldPlayerName.getText().isEmpty()
                && jSpinnerPlayerCount.getValue() != null);
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
        jTextFieldGameName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldPlayerName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSpinnerPlayerCount = new javax.swing.JSpinner();

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

        jPanel2.setLayout(new java.awt.GridLayout(3, 2));

        jLabel1.setText("Spielname");
        jPanel2.add(jLabel1);
        jPanel2.add(jTextFieldGameName);

        jLabel3.setText("Spielername");
        jPanel2.add(jLabel3);
        jPanel2.add(jTextFieldPlayerName);

        jLabel2.setText("Spieler anzahl");
        jPanel2.add(jLabel2);
        jPanel2.add(jSpinnerPlayerCount);

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
            java.util.logging.Logger.getLogger(GameCreateDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameCreateDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameCreateDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameCreateDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            GameCreateDialog dialog = new GameCreateDialog(owner, logic);
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerPlayerCount;
    private javax.swing.JTextField jTextFieldGameName;
    private javax.swing.JTextField jTextFieldPlayerName;
    // End of variables declaration//GEN-END:variables
}