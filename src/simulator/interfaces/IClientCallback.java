/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.interfaces;

import java.awt.Color;
import simulator.SimulatorMap;

import java.rmi.Remote;
import java.rmi.RemoteException;
import simulator.Point;

/**
 * @author 82wach1bif
 */
public interface IClientCallback extends Remote {

    void onMessage(String msg) throws RemoteException;

    void onMapLoaded(SimulatorMap map)throws RemoteException;

    void onGameFinished() throws RemoteException;
    
    void awaitPlayerTurn(Point point) throws RemoteException;
    
    void onPlayerTurn(Point point, Color color) throws RemoteException;
}
