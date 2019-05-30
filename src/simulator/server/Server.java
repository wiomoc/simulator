package simulator.server;

import java.io.File;
import simulator.SimulatorMap;
import simulator.interfaces.IClientCallback;
import simulator.interfaces.IRemoteGame;
import simulator.interfaces.IServer;
import simulator.server.model.Game;
import simulator.server.model.Player;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author 82wach1bif
 */
public class Server implements IServer, Serializable {

    private HashMap<String, Game> games = new HashMap<>();
    private HashMap<String, SimulatorMap> maps = new HashMap<>();

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Registry localRegistry = LocateRegistry.createRegistry(8888);

        Server server = new Server();

        server.addMap("default", SimulatorMap.loadFromFile(new File("game.csv")));
        UnicastRemoteObject.exportObject(server, 0);
        localRegistry.bind("simulator", server);
    }

    private Server() { }

    @Override
    public void addMap(String name, SimulatorMap map) {
        maps.put(name, map);
    }

    @Override
    public IRemoteGame createAndJoinGame(String name, String mapName,
            String playerName, int playerCount, IClientCallback callback) throws RemoteException {
        Player player = new Player(playerName, callback);
        Game game = new Game(maps.get(mapName), name, playerCount);
        game.join(player);
        games.put(name, game);
        callback.onMessage("Code: " + game.getCode());

        return (IRemoteGame) UnicastRemoteObject.exportObject(new RemoteGame(game, player), 8888);
    }

    @Override
    public IRemoteGame joinGame(String name, String code, String playerName,
            IClientCallback callback) throws RemoteException {
        Game game = games.get(name);
        if (!game.getCode().equals(code)) {
            return null;
        }

        Player player = new Player(playerName, callback);

        game.join(player);
        return (IRemoteGame) UnicastRemoteObject.exportObject(new RemoteGame(game, player), 8888);
    }

    @Override
    public List<String> listGames() {
        return new ArrayList<>(games.keySet());
    }

    @Override
    public List<String> listMaps() {
        return new ArrayList<>(maps.keySet());
    }

}
