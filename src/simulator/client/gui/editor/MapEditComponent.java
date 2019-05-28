package simulator.client.gui.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import simulator.SimulatorMap;

import javax.swing.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import simulator.Point;

/**
 * @author 82wach1bif
 */
public class MapEditComponent extends JPanel {

    private SimulatorMap map;
    private Point currentDragged;
    private ArrayList<Point> innerPoints = new ArrayList<>();
    private ArrayList<Point> outterPoints = new ArrayList<>();
    private ArrayList<Point> startPoints = new ArrayList<>();
    private ArrayList<Point> selectedPoints = innerPoints;

    public MapEditComponent() {
        addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                Point point = new Point(e.getX(), e.getY());
                for (Point p : selectedPoints) {
                    if (isPointSelected(p, point)) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            selectedPoints.remove(p);
                        } else if (SwingUtilities.isMiddleMouseButton(e)) {
                            int index = selectedPoints.indexOf(p);
                            selectedPoints.set(index, new Point(p.getX() + 8, p.getY() + 8));
                            selectedPoints.add(index, new Point(p.getX() - 8, p.getY() - 8));
                        } else {
                            currentDragged = p;
                        }
                        repaint();
                        return;
                    }
                }
                selectedPoints.add(point);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentDragged = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = new Point(e.getX(), e.getY());
                if (currentDragged != null) {
                    currentDragged = point;
                    repaint();
                }
            }
        });
    }

    private static boolean isPointSelected(Point point, Point clickedPoint) {
        int x = clickedPoint.getX() - point.getX();
        int y = clickedPoint.getY() - point.getY();

        return Math.sqrt(x * x + y * y) < 10;
    }

    private void drawPoints(Graphics2D gr, ArrayList<Point> points) {
        GeneralPath path = new GeneralPath();
        boolean first = true;
        for (Point point : points) {
            if (first) {
                path.moveTo(point.getX(), point.getY());
                first = false;
            } else {
                path.lineTo(point.getX(), point.getY());
            }
        }
        gr.draw(path);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D gr = (Graphics2D) g;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        gr.setColor(Color.GRAY);
        gr.setStroke(new BasicStroke(5));
        for (Point point : selectedPoints) {
            gr.drawOval(point.getX() - 5, point.getY() - 5, 10, 10);
        }

        gr.setStroke(new BasicStroke(4));
        gr.setColor(Color.BLACK);
        drawPoints(gr, innerPoints);
        gr.setStroke(new BasicStroke(4));
        gr.setColor(Color.BLUE);
        drawPoints(gr, outterPoints);
        gr.setStroke(new BasicStroke(4));
        gr.setColor(Color.RED);
        drawPoints(gr, startPoints);

        if (map == null) {
            return;
        }

        gr.scale((float) getWidth() / map.getWidth(), (float) getHeight() / map.getHeigth());

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

    }

    void selectInner() {
        selectedPoints = innerPoints;
        repaint();
    }

    void selectOutter() {
        selectedPoints = outterPoints;
        repaint();
    }

    void selectStart() {
        selectedPoints = startPoints;
        repaint();
    }
}
