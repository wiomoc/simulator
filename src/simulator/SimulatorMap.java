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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.shape.Path;

/**
 *
 * @author 82wach1bif
 */
public class SimulatorMap implements Serializable {

    private GeneralPath outer;
    private GeneralPath inner;
    private Line2D startLine;
    private int width;
    private int heigth;

    private SimulatorMap(int width, int heigth, GeneralPath outer, GeneralPath inner, Line2D startLine) {
        this.width = width;
        this.heigth = heigth;
        this.outer = outer;
        this.inner = inner;
        this.startLine = startLine;
    }

    public GeneralPath getOuter() {
        return outer;
    }

    public GeneralPath getInner() {
        return inner;
    }

    public Line2D getStartLine() {
        return startLine;
    }

    public int getWidth() {
        return width;
    }

    public int getHeigth() {
        return heigth;
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

    private static GeneralPath createPath(Point[] points) {
        GeneralPath path = new GeneralPath(0);

        path.moveTo(points[0].getX(), points[0].getY());

        for (int i = 0; i < points.length; i += 2) {
            path.curveTo(points[i].getX(), points[i].getY(),
                    points[(i + 1) % points.length].getX(), points[(i + 1) % points.length].getY(),
                    points[(i + 2) % points.length].getX(), points[(i + 2) % points.length].getY());
        }

        path.closePath();

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
            Point[] outterPoints = new Point[countOuter];
            line = readLine(reader);

            for (int i = 0; i < countOuter; i++) {
                outterPoints[i] = new Point(line.get(i * 2), line.get(i * 2 + 1));
            }

            line = readLine(reader);
            int countInner = line.get(0);
            Point[] innerPoints = new Point[countInner];
            line = readLine(reader);

            for (int i = 0; i < countOuter; i++) {
                innerPoints[i] = new Point(line.get(i * 2), line.get(i * 2 + 1));
            }

            line = readLine(reader);
            int rasterSize = line.get(0);
            line = readLine(reader);
            Line2D startLine = new Line2D.Float(line.get(0), line.get(1),line.get(2), line.get(3));


            return new SimulatorMap(width, height, createPath(outterPoints), createPath(innerPoints), startLine);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }

    }
}
