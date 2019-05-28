package simulator.client.gui.editor;

import java.awt.Shape;
import java.util.List;
import simulator.Point;

/**
 *
 * @author christophwalcher
 */
public interface IChangeablePath {
    Shape getShape();
    List<Point> getPoints();
    void addPoint(Point newPoint);
    void splitPoint(int position);
    void changeLocation(int position, Point newPoint);
    void deletePoint(int position);
}
