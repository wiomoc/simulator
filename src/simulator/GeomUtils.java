/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

/**
 *
 * @author christophwalcher
 */
public class GeomUtils {

    private GeomUtils() {
    }

    public static Point pointInTheMiddle(Point point1, Point point2) {
        return new Point((point1.getX() - point2.getX()) / 2 + point2.getX(), (point1.getY() - point2.getY()) / 2 + point2.getY());
    }

    public static Point alignInGrid(Point point, int rasterSize) {
        return new Point(point.getX() - point.getX() % rasterSize, point.getY() - point.getY() % rasterSize);
    }
}
