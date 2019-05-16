/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.interfaces;

import java.rmi.Remote;
import java.util.List;
import java.util.Set;
import simulator.SimulatorMap;

/**
 *
 * @author 82wach1bif
 */
public interface IServer extends Remote {
    IRemoteGame createAndJoinGame(String name, String code, SimulatorMap map, String playerName, IClientCallback callback);
    
    IRemoteGame joinGame(String name, String code, String playerName, IClientCallback callback);
    
    Set<String> listGames();
}
