package simulator.client.gui.editor;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import simulator.Point;
import simulator.SimulatorMap;

/**
 *
 * @author christophwalcher
 */
public class TrackBezier implements IChangeablePath {

    private final ArrayList<Point> points;

    public TrackBezier() {
        points = new ArrayList<>();
    }

    public TrackBezier(ArrayList<Point> points) {
        this.points = points;
    }

    @Override
    public Shape getShape() {
       return SimulatorMap.createPath(points.toArray(Point[]::new));
    }

    private static Point pointInTheMiddle(Point point1, Point point2) {
        return new Point((point1.getX() - point2.getX()) / 2 + point2.getX(), (point1.getY() - point2.getY()) / 2 + point2.getY());
    }

    @Override
    public List<Point> getPoints() {
        return points;
    }

    @Override
    public void addPoint(Point newPoint) {
        if (points.size() > 0) {
            points.add(pointInTheMiddle(newPoint, points.get(points.size() - 1)));
        } else {
            points.add(new Point(newPoint.getX() + 10, newPoint.getY() + 10));
        }
        points.add(newPoint);
    }

    @Override
    public void splitPoint(int position) {
        points.add(position + 1, pointInTheMiddle(points.get(position), points.get(position + 1)));
        points.add(position, pointInTheMiddle(points.get(position - 1), points.get(position)));
    }

    @Override
    public void changeLocation(int position, Point newPoint) {
        points.set(position, newPoint);
    }

    @Override
    public void deletePoint(int position) {
        points.remove(position);
        if (points.size() > 0) {
            if (position >= points.size()) {
                points.remove(position - 1);
            } else {
                points.remove(position);
            }
        }
    }

}
