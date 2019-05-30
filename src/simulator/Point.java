package simulator;

import java.io.Serializable;

/**
 *
 * @author 82wach1bif
 */
public class Point implements Serializable {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
