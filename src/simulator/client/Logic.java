/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.client;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.LinkedList;
import simulator.SimulatorMap;

/**
 *
 * @author 82wach1bif
 */
public class Logic {
    
    SimulatorMap map;
    LinkedList<Player> players;
    GameState state;
    Listener listener;
    
    private class Player {
        
        public Player(int id) {
            this.id = id;
        }
        
        int id;
    }
    
    public enum GameState {
        UNINTIALIZED,
        AWAIT_GAME_START,
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
    
    public Logic(Listener listener) {
        this.listener = listener;
        setGameState(GameState.UNINTIALIZED);
        listener.onMessage("Hello to Simulator");
    }
    
    private void setGameState(GameState state) {
        this.state = state;
        listener.onGameStateChange(state);
    }
    
    public void loadGame(File file) {
        if (state != GameState.UNINTIALIZED) {
            throw new IllegalStateException();
        }
        map = SimulatorMap.loadFromFile(file);
        listener.onMapLoaded(map);
        setGameState(GameState.AWAIT_GAME_START);
        listener.onMessage("Map loaded");
    }
    
    public void startGame(int playerCount) {
        if (state != GameState.AWAIT_GAME_START) {
            throw new IllegalStateException();
        }
        players = new LinkedList<>();
        for (int i = 1; i <= playerCount; i++) {
            players.add(new Player(i));
        }
        setGameState(GameState.AWAIT_PLAYER_TURN);
         listener.onMessage("Game started with "+ playerCount + " players");
         listener.onMessage("Awaiting Turn from Player 1");
    }
    
    public void setPlayerTurn(int position) {
        if (state != GameState.AWAIT_PLAYER_TURN) {
            throw new IllegalStateException();
        }
    }
    
    public void quitGame() {
    }
    
}
