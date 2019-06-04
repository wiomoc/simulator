package simulator.client.gui.editor;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import simulator.GeomUtils;
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
       return SimulatorMap.createPath(points);
    }

    @Override
    public List<Point> getPoints() {
        return points;
    }

    @Override
    public void addPoint(Point newPoint) {
        if (points.size() > 0) {
            points.add(GeomUtils.pointInTheMiddle(newPoint, points.get(points.size() - 1)));
        } else {
            points.add(new Point(newPoint.getX() + 10, newPoint.getY() + 10));
        }
        points.add(newPoint);
    }

    @Override
    public void splitPoint(int position) {
        points.add(position + 1, GeomUtils.pointInTheMiddle(points.get(position), points.get(position + 1)));
        points.add(position, GeomUtils.pointInTheMiddle(points.get(position - 1), points.get(position)));
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
