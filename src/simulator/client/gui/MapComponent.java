/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.JPanel;
import simulator.Point;
import simulator.SimulatorMap;

/**
 *
 * @author 82wach1bif
 */
public class MapComponent extends JPanel {

    class Turn {

        private Point point;
        private Color color;

        public Turn(Point pointer, Color color) {
            this.point = pointer;
            this.color = color;
        }

        public Point getPoint() {
            return point;
        }

        public Color getColor() {
            return color;
        }
    }

    private SimulatorMap map;
    private ArrayList<Turn> turns;
    private Point currentPointer;
    private Consumer<Integer> turnCallback;

    public MapComponent() {
        turns = new ArrayList<>();

    }

    public void setMap(SimulatorMap map) {
        this.map = map;
        this.repaint();
    }

    public void onPlayerTurn(Point pointer, Color color) {
        turns.add(new Turn(pointer, color));
        this.repaint();
    }

    public void awaitPlayerTurn(Point pointer, Consumer<Integer> callback) {
        this.currentPointer = pointer;
        this.turnCallback = callback;
        this.repaint();
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        if (currentPointer != null) {
            switch (e.getKeyChar()) {
                case '1':
                    turnCallback.accept(1);
                    break;
                case '2':
                    turnCallback.accept(2);
                    break;
                case '3':
                    turnCallback.accept(3);
                    break;
                case '4':
                    turnCallback.accept(4);
                    break;
                case '6':
                    turnCallback.accept(6);
                    break;
                case '7':
                    turnCallback.accept(7);
                    break;
                case '8':
                    turnCallback.accept(8);
                    break;
                case '9':
                    turnCallback.accept(9);
                    break;
                default:
                    System.out.println(e.getKeyChar());
                    return;

            }
            currentPointer = null;
        }
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

        gr.setColor(Color.GRAY);
        gr.setStroke(new BasicStroke(1));
        for (int x = 0; x < map.getWidth(); x += map.getRasterSize()) {
            gr.drawLine(x, 0, x, map.getHeigth());
        }

        for (int y = 0; y < map.getHeigth(); y += map.getRasterSize()) {
            gr.drawLine(0, y, map.getWidth(), y);
        }

        Area area = new Area(map.getOuter());
        area.subtract(new Area(map.getInner()));

        gr.setColor(Color.BLUE);
        gr.fill(area);

        gr.setColor(Color.RED);
        gr.setStroke(new BasicStroke(5));
        gr.draw(map.getFirstStartLine());

        gr.setStroke(new BasicStroke(5));
        for (Turn turn : turns) {
            gr.setColor(turn.getColor());
            gr.drawRoundRect(turn.getPoint().getX(), turn.getPoint().getY(),
                    map.getRasterSize() / 2, map.getRasterSize() / 2,
                    map.getRasterSize() / 2 - 1, map.getRasterSize() / 2 - 1);
        }

        gr.setColor(Color.BLACK);
        if (currentPointer != null) {
            gr.setFont(new Font("Sans Serif", Font.PLAIN, map.getRasterSize()));
            gr.drawChars(new char[]{'1'}, 0, 1, currentPointer.getX() - map.getRasterSize(), currentPointer.getY() + map.getRasterSize());
            gr.drawChars(new char[]{'2'}, 0, 1, currentPointer.getX(), currentPointer.getY() + map.getRasterSize());
            gr.drawChars(new char[]{'3'}, 0, 1, currentPointer.getX() + map.getRasterSize(), currentPointer.getY() + map.getRasterSize());
            gr.drawChars(new char[]{'4'}, 0, 1, currentPointer.getX() - map.getRasterSize(), currentPointer.getY());
            gr.drawChars(new char[]{'6'}, 0, 1, currentPointer.getX() + map.getRasterSize(), currentPointer.getY());
            gr.drawChars(new char[]{'7'}, 0, 1, currentPointer.getX() - map.getRasterSize(), currentPointer.getY() - map.getRasterSize());
            gr.drawChars(new char[]{'8'}, 0, 1, currentPointer.getX(), currentPointer.getY() - map.getRasterSize());
            gr.drawChars(new char[]{'9'}, 0, 1, currentPointer.getX() + map.getRasterSize(), currentPointer.getY() - map.getRasterSize());
        }
    }

}
