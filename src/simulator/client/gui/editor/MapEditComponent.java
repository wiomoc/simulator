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
    private int currentDragged;
    private TrackBezier innerPoints = new TrackBezier();
    private TrackBezier outterPoints = new TrackBezier();
    private StartLine startPoints = new StartLine();
    private IChangeablePath selectedPoints = innerPoints;

    public MapEditComponent() {
        addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                Point point = new Point(e.getX(), e.getY());
                for (Point p : selectedPoints.getPoints()) {
                    if (isPointSelected(p, point)) {
                        int index = selectedPoints.getPoints().indexOf(p);
                        if (SwingUtilities.isRightMouseButton(e)) {
                            selectedPoints.deletePoint(index);
                        } else if (SwingUtilities.isMiddleMouseButton(e)) {
                          selectedPoints.splitPoint(index);
                        } else {
                            currentDragged = index;
                        }
                        repaint();
                        return;
                    }
                }

                selectedPoints.addPoint(point);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentDragged = -1;
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

                if (currentDragged != -1) {
                    Point point = new Point(e.getX(), e.getY());
                  selectedPoints.changeLocation(currentDragged, point);
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


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D gr = (Graphics2D) g;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        gr.setColor(Color.GRAY);
        gr.setStroke(new BasicStroke(5));
        for (Point point : selectedPoints.getPoints()) {
            gr.drawOval(point.getX() - 5, point.getY() - 5, 10, 10);
        }

        gr.setStroke(new BasicStroke(4));
        gr.setColor(Color.BLACK);
        gr.draw(innerPoints.getShape());
        gr.setStroke(new BasicStroke(4));
        gr.setColor(Color.BLUE);
        gr.draw(outterPoints.getShape());
        gr.setStroke(new BasicStroke(4));
        gr.setColor(Color.RED);
        gr.draw(startPoints.getShape());

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
