package simulator.client.gui;

import java.awt.Component;
import java.io.File;
import java.util.function.Consumer;
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
