package simulator;

import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 82wach1bif
 */
public class SimulatorMap implements Serializable {

    private final ArrayList<Point> outer;
    private final ArrayList<Point> inner;
    private final Line2D.Float firstStartLine;
    private final Line2D.Float secondStartLine;
    private final int width;
    private final int heigth;
    private final int rasterSize;

    public SimulatorMap(int width, int heigth,
            int rastersize,
            ArrayList<Point> outer,
            ArrayList<Point> inner,
            Line2D.Float firstStartLine,
            Line2D.Float secondStartLine) {
        this.width = width;
        this.heigth = heigth;
        this.rasterSize = rastersize;
        this.outer = outer;
        this.inner = inner;
        this.firstStartLine = firstStartLine;
        this.secondStartLine = secondStartLine;
    }

    public ArrayList<Point> getOuter() {
        return outer;
    }

    public ArrayList<Point> getInner() {
        return inner;
    }

    public Line2D getFirstStartLine() {
        return firstStartLine;
    }

    public Line2D getSecondStartLine() {
        return secondStartLine;
    }

    public int getWidth() {
        return width;
    }

    public int getHeigth() {
        return heigth;
    }

    public int getRasterSize() {
        return rasterSize;
    }

    private static List<Integer> readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(line.split(","))
                .map(String::trim)
                .map(item -> {
                    if (item.indexOf('"') == 0 && item.indexOf('"') == item.length() - 1
                            || item.indexOf('\'') == 0 && item.indexOf('\'') == item.length() - 1) {
                        item = item.substring(1, item.length() - 1);
                    }
                    return item;
                })
                .map(Integer::parseInt)
                .collect(Collectors.toList());

    }

    public static GeneralPath createPath(ArrayList<Point> points) {
        GeneralPath path = new GeneralPath(0);
        if (points.size() > 0) {
            path.moveTo(points.get(0).getX(), points.get(0).getY());

            for (int i = 0; i < points.size(); i += 2) {
                path.curveTo(points.get(i).getX(), points.get(i).getY(),
                        points.get((i + 1) % points.size()).getX(), points.get((i + 1) % points.size()).getY(),
                        points.get((i + 2) % points.size()).getX(), points.get((i + 2) % points.size()).getY());
            }

            path.closePath();
        }
        return path;
    }

    public static SimulatorMap loadFromFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            List<Integer> line = readLine(reader);

            int width = line.get(0);
            int height = line.get(1);

            line = readLine(reader);
            int countOuter = line.get(0);
            ArrayList<Point> outerPoints = new ArrayList<>(countOuter);
            line = readLine(reader);

            for (int i = 0; i < countOuter; i++) {
                outerPoints.add(new Point(line.get(i * 2), line.get(i * 2 + 1)));
            }

            line = readLine(reader);
            int countInner = line.get(0);
            ArrayList<Point> innerPoints = new ArrayList<>(countInner);
            line = readLine(reader);

            for (int i = 0; i < countInner; i++) {
                innerPoints.add(new Point(line.get(i * 2), line.get(i * 2 + 1)));
            }

            line = readLine(reader);
            int rasterSize = line.get(0);
            line = readLine(reader);
            Line2D.Float startLine = new Line2D.Float(line.get(0), line.get(1), line.get(2), line.get(3));

            return new SimulatorMap(width, height, rasterSize, outerPoints, innerPoints, startLine, null);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public void saveToFile(File file) {
        try {
            PrintStream writer = new PrintStream(file);
            writer.print(width);
            writer.print(',');
            writer.println(heigth);
            writer.println(outer.size());
            writer.println(outer.stream().map(p -> p.getX() + "," + p.getY()).collect(Collectors.joining(",")));
            writer.println(inner.size());
            writer.println(inner.stream().map(p -> p.getX() + "," + p.getY()).collect(Collectors.joining(",")));
            writer.println(rasterSize);
            writer.print((int) firstStartLine.getX1());
            writer.print(',');
            writer.print((int) firstStartLine.getY1());
            writer.print(',');
            writer.print((int) firstStartLine.getX2());
            writer.print(',');
            writer.println((int) firstStartLine.getY2());
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public Area getArea() {
        Area area = new Area(createPath(outer));
        area.subtract(new Area(createPath(inner)));
        return area;
    }
}
