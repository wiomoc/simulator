/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import javax.swing.JPanel;
import simulator.SimulatorMap;

/**
 *
 * @author 82wach1bif
 */
public class MapComponent extends JPanel {

    SimulatorMap map;

    public void setMap(SimulatorMap map) {
        this.map = map;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (map == null) {
            return;
        }

        Graphics2D gr = (Graphics2D) g;

        gr.scale((float) getWidth() / map.getWidth(), (float) getHeight() / map.getHeigth());

        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Area area = new Area(map.getOuter());
        area.subtract(new Area(map.getInner()));

        gr.setColor(Color.BLUE);
        gr.fill(area);

        gr.setColor(Color.RED);
        gr.setStroke(new BasicStroke(5));
        gr.draw(map.getStartLine());
    }

}
