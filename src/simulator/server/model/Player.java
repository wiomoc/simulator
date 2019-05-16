/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.server.model;

import java.awt.Color;
import java.util.List;
import simulator.interfaces.IClientCallback;
import simulator.interfaces.IRemoteGame;

/**
 *
 * @author 82wach1bif
 */
public class Player {
    
    private IClientCallback clientCallback;
    private int id;
    private String name;
    private List<Integer> points;
    private Color color;
    private boolean alive;

    public Player(String name, IClientCallback clientCallback) {
        this.clientCallback = clientCallback;
        this.name = name;
    }
    
    public void sendMessage(String message) {
        clientCallback.onMessage(message);
    }

    String getName() {
       return name;
    }
    
    
}
