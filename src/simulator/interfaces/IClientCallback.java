/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.interfaces;

import simulator.SimulatorMap;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author 82wach1bif
 */
public interface IClientCallback extends Serializable, Remote {

    void onMessage(String msg) throws RemoteException;

    void onMapLoaded(SimulatorMap map)throws RemoteException;

    void onGameFinished() throws RemoteException;
}
