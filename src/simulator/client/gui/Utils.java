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

    private Utils() {
    }

    public static void chooseMapFileOpen(Component parent, Consumer<File> cb) {
        generateChooser(cb).showOpenDialog(parent);
    }

    public static void chooseMapFileSave(Component parent, Consumer<File> cb) {
        generateChooser(cb).showSaveDialog(parent);
    }

    private static JFileChooser generateChooser(Consumer<File> cb) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("SimulatorMap (.csv)", "csv"));
        chooser.addActionListener((l) -> {
            File file = chooser.getSelectedFile();
            if (file != null) {
                cb.accept(file);
            }
        });
        return chooser;
    }
}
