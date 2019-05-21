/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import simulator.SimulatorMap;

/**
 * @author 82wach1bif
 */
public interface IServer extends Remote {
    IRemoteGame createAndJoinGame(String name, String code, String mapName, String playerName, IClientCallback callback) throws RemoteException;

    IRemoteGame joinGame(String name, String code, String playerName, IClientCallback callback) throws RemoteException;

    List<String> listGames() throws RemoteException;

    void addMap(SimulatorMap map) throws RemoteException;

    List<String> listMaps() throws RemoteException;
}
