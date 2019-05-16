/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.client.gui;

import java.awt.Component;
import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author 82wach1bif
 */
public class Utils {
    public static void chooseMapFile(Component parent, Consumer<File> cb) {
          JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("SimulatorMap (.csv)", "csv"));
            chooser.addActionListener((l) -> {
                File file = chooser.getSelectedFile();
                if(file!= null) cb.accept(file);
            });
            
            chooser.showOpenDialog(parent);
    }
}
