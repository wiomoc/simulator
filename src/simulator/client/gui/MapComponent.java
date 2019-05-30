package simulator.client.gui;

import simulator.Point;
import simulator.SimulatorMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author 82wach1bif
 */
public class MapComponent extends JPanel {

    private SimulatorMap map;
    private Area track;
    private ArrayList<Turn> turns;
    private Point currentPointer;
    private Consumer<Integer> turnCallback;

    class Turn {

        private final Point point;
        private final Color color;

        Turn(Point pointer, Color color) {
            this.point = pointer;
            this.color = color;
        }

        Point getPoint() {
            return point;
        }

        Color getColor() {
            return color;
        }
    }

    public MapComponent() {
        turns = new ArrayList<>();

    }

    public void setMap(SimulatorMap map) {
        this.map = map;
        track = new Area(SimulatorMap.createPath(map.getOuter()));
        track.subtract(new Area(SimulatorMap.createPath(map.getInner())));
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
            int digit = Character.digit(e.getKeyChar(), 10);
            if (digit > 0) {
                turnCallback.accept(digit);
                currentPointer = null;
            }
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

        gr.setColor(Color.BLUE);
        gr.fill(track);

        gr.setColor(Color.GRAY);
        gr.setStroke(new BasicStroke(1));
        for (int x = 0; x < map.getWidth(); x += map.getRasterSize()) {
            gr.drawLine(x, 0, x, map.getHeigth());
        }

        for (int y = 0; y < map.getHeigth(); y += map.getRasterSize()) {
            gr.drawLine(0, y, map.getWidth(), y);
        }

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
            gr.drawChars(new char[]{'5'}, 0, 1, currentPointer.getX(), currentPointer.getY());
            gr.drawChars(new char[]{'6'}, 0, 1, currentPointer.getX() + map.getRasterSize(), currentPointer.getY());
            gr.drawChars(new char[]{'7'}, 0, 1, currentPointer.getX() - map.getRasterSize(), currentPointer.getY() - map.getRasterSize());
            gr.drawChars(new char[]{'8'}, 0, 1, currentPointer.getX(), currentPointer.getY() - map.getRasterSize());
            gr.drawChars(new char[]{'9'}, 0, 1, currentPointer.getX() + map.getRasterSize(), currentPointer.getY() - map.getRasterSize());
        }
    }
}
