package simulator.server;

import java.rmi.RemoteException;
import simulator.interfaces.IRemoteGame;
import simulator.server.model.Game;
import simulator.server.model.Player;

/**
 *
 * @author 82wach1bif
 */
public class RemoteGame implements IRemoteGame {
    private Game game;
    private Player player;
    
     public RemoteGame(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public void message(String msg) {
      game.onMessage(player, msg);
    }

    @Override
    public void setPlayerTurn(int position) throws RemoteException {
       game.setPlayerTurn(player, position);
    }
    
}
