package simulator.client.gui.editor;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import simulator.Point;

/**
 *
 * @author christophwalcher
 */
public class StartLine implements IChangeablePath {

    private final ArrayList<Point> points;

    public StartLine() {
        points = new ArrayList<>(2);
    }

    public StartLine(Line2D line) {
        points = new ArrayList<>(2);
        points.add(new Point((int) line.getX1(), (int) line.getY1()));
        points.add(new Point((int) line.getX2(), (int) line.getY2()));

    }

    @Override
    public List<Point> getPoints() {
        return points;
    }

    @Override
    public void addPoint(Point newPoint) {
        if (points.size() < 2) {
            points.add(newPoint);
        }
    }

    /**
     * Not supported
     *
     * @param position
     */
    @Override
    public void splitPoint(int position) {
    }

    @Override
    public void changeLocation(int position, Point newPoint) {
        points.set(position, newPoint);
    }

    @Override
    public void deletePoint(int position) {
        points.remove(position);
    }

    @Override
    public Shape getShape() {
        if (points.size() == 2) {
            return new Line2D.Float(points.get(0).getX(), points.get(0).getY(), points.get(1).getX(), points.get(1).getY());
        } else {
            return new GeneralPath(0);
        }
    }

}
