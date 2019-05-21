/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.server;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import simulator.SimulatorMap;
import simulator.interfaces.IClientCallback;
import simulator.interfaces.IServer;
import simulator.server.model.Game;
import simulator.interfaces.IRemoteGame;
import simulator.server.model.Player;

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

        UnicastRemoteObject.exportObject(server, 0);

        localRegistry.bind("simulator", server);
    }

    private Server() {
        games.put("ABC", new Game(new ArrayList<>(), null, "", "12"));
    }

    @Override
    public void addMap(SimulatorMap map) {
        maps.put(map.getName(), map);
    }

    @Override
    public IRemoteGame createAndJoinGame(String name, String code, String mapName,
            String playerName, IClientCallback callback) throws RemoteException {
        Player player = new Player(playerName, callback);
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(player);
        Game game = new Game(playerList, maps.get(mapName), name, code);
        games.put(name, game);

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
