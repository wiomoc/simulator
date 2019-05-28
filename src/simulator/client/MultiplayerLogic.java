/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.client;

import java.awt.Color;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.LinkedList;
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
    
    SimulatorMap map;
    LinkedList<Player> players;
    GameState state;
    Listener listener;
    IServer server;
    IRemoteGame game;
    
    private class Player {
        
        public Player(int id) {
            this.id = id;
        }
        
        int id;
    }
    
    public enum GameState {
        UNCONNECTED,
        CONNECTED,
        MAP_LOADED,
        AWAIT_PLAYER_TURN,
        FINISHED
    }
    
    public interface Listener {
        
        void onMessage(String msg);
        
        void onMapLoaded(SimulatorMap map);
        
        void onPlayerTurn(Point point, Color color);
        
        void awaitPlayerTurn(Point point);
        
        void onGameFinished();
        
        void onGameStateChange(GameState state);
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
            setGameState(GameState.AWAIT_PLAYER_TURN);
            listener.awaitPlayerTurn(point);
        }
        
        @Override
        public void onPlayerTurn(Point point, Color color) throws RemoteException {
            listener.onPlayerTurn(point, color);
        }
        
    }
    
    public MultiplayerLogic(Listener listener) {
        this.listener = listener;
        setGameState(GameState.UNCONNECTED);
        listener.onMessage("Hello to Simulator");
        listener.onMessage("Please connect to Server");
        
        listener.onMapLoaded(SimulatorMap.loadFromFile(new File("game.csv")));
        
        listener.onPlayerTurn(new Point(100, 100), Color.yellow);
        listener.onPlayerTurn(new Point(200, 100), Color.red);
        listener.awaitPlayerTurn(new Point(192, 192));
    }
    
    private void setGameState(GameState state) {
        this.state = state;
        listener.onGameStateChange(state);
    }
    
    public void loadMap(File file) {
        if (state != GameState.CONNECTED) {
            throw new IllegalStateException();
        }
        map = SimulatorMap.loadFromFile(file);
        listener.onMapLoaded(map);
        setGameState(GameState.MAP_LOADED);
        listener.onMessage("Map loaded");
    }
    
    public void connect(String host) {
        if (state != GameState.UNCONNECTED) {
            throw new IllegalStateException();
        }
        try {
            Registry registry = LocateRegistry.getRegistry(host, 8888);
            server = (IServer) registry.lookup("simulator");
            setGameState(GameState.CONNECTED);
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
    
    public void createGame(String name) {
        if (state != GameState.MAP_LOADED) {
            throw new IllegalStateException();
        }
        
        try {
            game = server.joinGame(name, "123", "testPlayer", new ClientCallback());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    public void joinGame(String name, String code, String player) {
        if (state != GameState.CONNECTED) {
            throw new IllegalStateException();
        }
        
        try {
            game = server.joinGame(name, code, player,
                    (IClientCallback) UnicastRemoteObject.exportObject(new ClientCallback(), 0));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    public List<String> listGames() {
        if (state != GameState.CONNECTED) {
            throw new IllegalStateException();
        }
        
        try {
            return server.listGames();
        } catch (RemoteException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public void setPlayerTurn(int position) {
        if (state != GameState.AWAIT_PLAYER_TURN) {
            throw new IllegalStateException();
        }
        
        try {
            game.setPlayerTurn(position);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
    }
    
    public void quitGame() {
    }
    
}
