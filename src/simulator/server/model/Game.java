package simulator.server.model;

import java.util.List;
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

    public Game(List<Player> players, SimulatorMap map, String name, String code) {
        this.players = players;
        this.map = map;
        this.name = name;
        this.code = code;
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
        players.add(player);
    }

    public void onMessage(Player player, String message) {
        for (Player other : players) {
            other.sendMessage(player.getName() + ": " + message);
        }
    }

    public void setPlayerTurn(Player player, int position) {
        
    }
}
