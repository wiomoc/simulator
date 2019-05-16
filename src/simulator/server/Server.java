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

    HashMap<String, Game> games = new HashMap<>();

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Registry localRegistry = LocateRegistry.createRegistry(8888);

        Server server = new Server();
        
        UnicastRemoteObject.exportObject(server);
        
        localRegistry.bind("simulator", server);
    }

    @Override
    public IRemoteGame createAndJoinGame(String name, String code, SimulatorMap map,
            String playerName, IClientCallback callback) {
        Player player = new Player(playerName, callback);
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(player);
        Game game = new Game(playerList, map, name, code);
        games.put(name, game);

        return new RemoteGame(game, player);
    }

    @Override
    public IRemoteGame joinGame(String name, String code, String playerName, 
            IClientCallback callback) {
        Game game = games.get(name);
        if (!game.getCode().equals(code)) {
            return null;
        }

        Player player = new Player(playerName, callback);

        game.join(player);
        return new RemoteGame(game, player);
    }

    @Override
    public Set<String> listGames() {
        return games.keySet();
    }

}
