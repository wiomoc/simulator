package simulator.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import simulator.SimulatorMap;

/**
 *
 * @author 82wach1bif
 */
public class Game {

    private List<Player> players;
    private SimulatorMap map;
    private String name;
    private String code;
    private int playerCount;

    public Game(SimulatorMap map, String name, int playerCount) {
        this.players = new ArrayList<>();
        this.map = map;
        this.name = name;
        this.code = Integer.toString(new Random().nextInt() + 999999).substring(0, 6);
        this.playerCount = playerCount;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public SimulatorMap getMap() {
        return map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void join(Player player) {
        if (playerCount == players.size()) {
            throw new IllegalStateException("Enough Players");
        }

        players.add(player);
        player.sendMap(map);

        if (playerCount == players.size()) {
            //start game
        } else {
           broadcastMessage(playerCount - players.size() + " more player required");
        }
    }
    
    private void broadcastMessage(String message) {
        for (Player other : players) {
            other.sendMessage(message);
        }
    }

    public void onMessage(Player player, String message) {
        broadcastMessage(player.getName() + ": " + message);
    }

    public void setPlayerTurn(Player player, int position) {

    }
}
