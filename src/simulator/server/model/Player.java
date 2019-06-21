package simulator.server.model;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import simulator.Point;
import simulator.SimulatorMap;

import simulator.interfaces.IClientCallback;

/**
 * @author 82wach1bif
 */
public class Player {

    private final IClientCallback clientCallback;

    private final static Color[] COLORS = {Color.CYAN, Color.MAGENTA, Color.RED, Color.GRAY, Color.YELLOW, Color.GREEN, Color.ORANGE, Color.PINK};
    private int id;
    private final String name;
    private List<Point> points;
    private final Color color;
    private boolean alive;

    private int spielerSchnittL = 0;
    private int spielerSchnittR = 0;
    private int spielerSchnittO = 0;
    private int spielerSchnittU = 0;
    private int spielerSchnittSZL = 0;

    public Player(String name, IClientCallback clientCallback) {
        this.clientCallback = clientCallback;
        this.name = name;
        this.color = COLORS[Math.abs(name.hashCode()) % COLORS.length];
        this.points = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addTurn(Point point) {
        points.add(point);
    }

    public Point getLastTurn() {
        return points.get(points.size() - 1);
    }

    public Point getBeforeLastTurn() {
        return points.get(points.size() - 2);
    }

    void kick() {
        try {
            clientCallback.onKicked();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void sendMap(SimulatorMap map) {
        try {
            clientCallback.onMapLoaded(map);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String message) {
        try {
            clientCallback.onMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void sendTurn(Player player, Point point) {
        try {
            clientCallback.onPlayerTurn(point, player.color);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void awaitTurn() {
        try {
            clientCallback.awaitPlayerTurn(getNextPosition());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    Point getNextPosition() {
        if (points.size() == 1) {
            return getLastTurn();
        } else {
            Point last = getLastTurn();
            Point lastLast = getBeforeLastTurn();

            return new Point(last.getX() + (last.getX() - lastLast.getX()),
                    last.getY() + (last.getY() - lastLast.getY()));
        }
    }

    public int getSpielerSchnittL() {
        return spielerSchnittL;
    }

    public void setSpielerSchnittL(int spielerSchnittL) {
        this.spielerSchnittL = spielerSchnittL;
    }

    public int getSpielerSchnittR() {
        return spielerSchnittR;
    }

    public void setSpielerSchnittR(int spielerSchnittR) {
        this.spielerSchnittR = spielerSchnittR;
    }

    public int getSpielerSchnittO() {
        return spielerSchnittO;
    }

    public void setSpielerSchnittO(int spielerSchnittO) {
        this.spielerSchnittO = spielerSchnittO;
    }

    public int getSpielerSchnittU() {
        return spielerSchnittU;
    }

    public void setSpielerSchnittU(int spielerSchnittU) {
        this.spielerSchnittU = spielerSchnittU;
    }

    public int getSpielerSchnittSZL() {
        return spielerSchnittSZL;
    }

    public void setSpielerSchnittSZL(int spielerSchnittSZL) {
        this.spielerSchnittSZL = spielerSchnittSZL;
    }

}
