package simulator.server.model;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.List;
import simulator.Point;
import simulator.SimulatorMap;

import simulator.interfaces.IClientCallback;

/**
 * @author 82wach1bif
 */
public class Player {

    private final IClientCallback clientCallback;
    private int id;
    private String name;
    private List<Point> points;
    private Color color;
    private boolean alive;

    public Player(String name, IClientCallback clientCallback) {
        this.clientCallback = clientCallback;
        this.name = name;
    }

    public void sendMessage(String message) {
        try {
            clientCallback.onMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    String getName() {
        return name;
    }

    void sendMap(SimulatorMap map) {
        try {
            clientCallback.onMapLoaded(map);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
