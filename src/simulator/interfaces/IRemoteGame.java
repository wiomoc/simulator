/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author 82wach1bif
 */
public interface IRemoteGame extends Remote {
    
    void message(String msg) throws RemoteException;

    void playerTurn(String msg) throws RemoteException;

    void setPlayerTurn(int position) throws RemoteException;
}
