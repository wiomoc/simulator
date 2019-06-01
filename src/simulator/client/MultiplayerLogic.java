package simulator.client;

import java.awt.Color;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import simulator.Point;

import simulator.SimulatorMap;
import simulator.interfaces.IClientCallback;
import simulator.interfaces.IRemoteGame;
import simulator.interfaces.IServer;

/**
 * @author 82wach1bif
 */
public class MultiplayerLogic {

    private SimulatorMap map;
    private Listener listener;
    private IServer server;
    private IRemoteGame game;
    private boolean connected;
    private boolean inGame;

    public interface Listener {

        void onMessage(String msg);

        void onMapLoaded(SimulatorMap map);

        void onPlayerTurn(Point point, Color color);

        void awaitPlayerTurn(Point point);

        void onGameFinished();

        void onGameStateChange();

        void onReset();
    }

    public class ClientCallback implements IClientCallback {

        @Override
        public void onMessage(String msg) {
            listener.onMessage(msg);
        }

        @Override
        public void onMapLoaded(SimulatorMap map) {
            listener.onMapLoaded(map);
        }

        @Override
        public void onGameFinished() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void awaitPlayerTurn(Point point) throws RemoteException {
            listener.awaitPlayerTurn(point);
        }

        @Override
        public void onPlayerTurn(Point point, Color color) throws RemoteException {
            listener.onPlayerTurn(point, color);
        }

        @Override
        public void onKicked() throws RemoteException {
            inGame = false;
            listener.onGameStateChange();
            listener.onReset();
            listener.onMessage("Kicked by Server");
        }

    }

    public MultiplayerLogic(Listener listener) {
        this.listener = listener;
        listener.onMessage("Hello to Simulator");
        listener.onMessage("Please connect to Server");

        listener.onMapLoaded(SimulatorMap.loadFromFile(new File("game.csv")));

        listener.onPlayerTurn(new Point(100, 100), Color.yellow);
        listener.onPlayerTurn(new Point(200, 100), Color.red);
        listener.awaitPlayerTurn(new Point(192, 192));
    }

    public void loadMap(File file) {
        map = SimulatorMap.loadFromFile(file);
        listener.onMapLoaded(map);
        listener.onMessage("Map loaded");
    }

    public void connect(String host) {
        if (connected) {
            throw new IllegalStateException();
        }
        try {
            Registry registry = LocateRegistry.getRegistry(host, 8888);
            server = (IServer) registry.lookup("simulator");
            connected = true;
            listener.onGameStateChange();
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(MultiplayerLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage(String msg) {
        try {
            game.message(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void createGame(String gameName, String mapName, String playerName, int playerCount) {
        if (!connected || inGame) {
            throw new IllegalStateException();
        }

        try {
            game = server.createAndJoinGame(gameName, mapName, playerName, playerCount,
                    (IClientCallback) UnicastRemoteObject.exportObject(new ClientCallback(), 0));
            inGame = true;
            listener.onGameStateChange();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void joinGame(String gameName, String code, String playerName) {
        if (!connected || inGame) {
            throw new IllegalStateException();
        }

        try {
            game = server.joinGame(gameName, code, playerName,
                    (IClientCallback) UnicastRemoteObject.exportObject(new ClientCallback(), 0));
            inGame = true;
            listener.onGameStateChange();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<String> listGames() {
        if (!connected) {
            throw new IllegalStateException();
        }

        try {
            return server.listGames();
        } catch (RemoteException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<String> listMaps() {
        if (!connected) {
            throw new IllegalStateException();
        }

        try {
            return server.listMaps();
        } catch (RemoteException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void setPlayerTurn(int position) {
        try {
            game.setPlayerTurn(position);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void uploadMap(String name, SimulatorMap map) {
        if (!connected) {
            throw new IllegalStateException();
        }

        try {
            server.addMap(name, map);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void exitGame() {
        if (!inGame) {
            throw new IllegalStateException();
        }
        try {
            game.exit();
            inGame = false;
            listener.onGameStateChange();
            listener.onReset();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isInGame() {
        return inGame;
    }
}
