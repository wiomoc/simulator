/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 82wach1bif
 */
public class SimulatorMap implements Serializable {

    private String name;
    private Point[] outer;
    private Point[] inner;
    private Line2D.Float firstStartLine;
    private Line2D.Float secondStartLine;
    private int width;
    private int heigth;
    private int rasterSize;

    public SimulatorMap(String name,
            int width, int heigth,
            int rastersize,
            Point[] outer,
            Point[] inner,
            Line2D.Float firstStartLine,
            Line2D.Float secondStartLine) {
        this.name = name;
        this.width = width;
        this.heigth = heigth;
        this.rasterSize = rastersize;
        this.outer = outer;
        this.inner = inner;
        this.firstStartLine = firstStartLine;
        this.secondStartLine = secondStartLine;
    }

    public Point[] getOuter() {
        return outer;
    }

    public Point[] getInner() {
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

    public String getName() {
        return name;
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

    public static GeneralPath createPath(Point[] points) {
        GeneralPath path = new GeneralPath(0);
        if (points.length > 0) {
            path.moveTo(points[0].getX(), points[0].getY());

            for (int i = 0; i < points.length; i += 2) {
                path.curveTo(points[i].getX(), points[i].getY(),
                        points[(i + 1) % points.length].getX(), points[(i + 1) % points.length].getY(),
                        points[(i + 2) % points.length].getX(), points[(i + 2) % points.length].getY());
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
            Point[] outerPoints = new Point[countOuter];
            line = readLine(reader);

            for (int i = 0; i < countOuter; i++) {
                outerPoints[i] = new Point(line.get(i * 2), line.get(i * 2 + 1));
            }

            line = readLine(reader);
            int countInner = line.get(0);
            Point[] innerPoints = new Point[countInner];
            line = readLine(reader);

            for (int i = 0; i < countInner; i++) {
                innerPoints[i] = new Point(line.get(i * 2), line.get(i * 2 + 1));
            }

            line = readLine(reader);
            int rasterSize = line.get(0);
            line = readLine(reader);
            Line2D.Float startLine = new Line2D.Float(line.get(0), line.get(1), line.get(2), line.get(3));

            String name = file.getName();

            return new SimulatorMap(name, width, height, rasterSize, outerPoints, innerPoints, startLine, null);
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
            writer.println(outer.length);
            writer.println(Arrays.stream(outer).map(p -> p.getX() + "," + p.getY()).collect(Collectors.joining(",")));
            writer.println(inner.length);
            writer.println(Arrays.stream(inner).map(p -> p.getX() + "," + p.getY()).collect(Collectors.joining(",")));
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
}
