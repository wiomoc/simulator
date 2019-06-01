package simulator.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author 82wach1bif
 */
public interface IRemoteGame extends Remote {
    
    void message(String msg) throws RemoteException;

    void setPlayerTurn(int position) throws RemoteException;
    
    void exit() throws RemoteException;
}
