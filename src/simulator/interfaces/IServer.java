/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import simulator.SimulatorMap;

/**
 * @author 82wach1bif
 */
public interface IServer extends Remote, Serializable {
    IRemoteGame createAndJoinGame(String name, String code, SimulatorMap map, String playerName, IClientCallback callback) throws RemoteException;

    IRemoteGame joinGame(String name, String code, String playerName, IClientCallback callback) throws RemoteException;

    Set<String> listGames() throws RemoteException;
}
