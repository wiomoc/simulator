package simulator.client.gui.editor;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import simulator.Point;

/**
 *
 * @author christophwalcher
 */
public class TrackBezier implements IChangeablePath {

    private final ArrayList<Point> points = new ArrayList<>();

    @Override
    public Shape getShape() {
        GeneralPath path = new GeneralPath(0);
        if (points.size() > 0) {
            path.moveTo(points.get(0).getX(), points.get(0).getY());

            for (int i = 0; i < points.size() - 2; i += 2) {
                path.curveTo(points.get(i).getX(), points.get(i).getY(),
                        points.get((i + 1) % points.size()).getX(), points.get((i + 1) % points.size()).getY(),
                        points.get((i + 2) % points.size()).getX(), points.get((i + 2) % points.size()).getY());
            }

        }
        return path;
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
