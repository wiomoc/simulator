package simulator.server.model;

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import simulator.GeomUtils;
import simulator.Point;
import simulator.SimulatorMap;

/**
 *
 * @author 82wach1bif
 */
public class Game {

    private final LinkedList<Player> players;
    private final SimulatorMap map;
    private Area trackArea;
    private String name;
    private final String code;
    private int playerCount;
    private Timer timer;
    private TimerTask turnTimerTask;

    public Game(SimulatorMap map, String name, int playerCount) {
        this.players = new LinkedList<>();
        this.map = map;
        this.name = name;
        this.code = Integer.toString(Math.abs(new Random().nextInt()) + 999999).substring(0, 6);
        this.playerCount = playerCount;
    }

    public List<Player> getPlayers() {
        return players;
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

    public synchronized void join(Player player) {
        if (playerCount == players.size()) {
            throw new IllegalStateException("Enough Players");
        }

        players.add(player);
        player.sendMap(map);

        if (playerCount == players.size()) {
            startGame();
        } else {
            broadcastMessage(playerCount - players.size() + " more player required");
        }
    }

    public void onMessage(Player player, String message) {
        broadcastMessage(player.getName() + ": " + message);
    }

    public synchronized void setPlayerTurn(Player player, int position) {
        if (players.peekFirst() != player) {
            throw new IllegalStateException();
        }
        players.addLast(players.pollFirst());

        if (turnTimerTask != null) {
            turnTimerTask.cancel();
        }

        Point lastPosition = player.getLastTurn();
        Point point;
        switch (position) {
            case 1:
                point = new Point(lastPosition.getX() - map.getRasterSize(), lastPosition.getY() + map.getRasterSize());
                break;
            case 2:
                point = new Point(lastPosition.getX(), lastPosition.getY() + map.getRasterSize());
                break;
            case 3:
                point = new Point(lastPosition.getX() + map.getRasterSize(), lastPosition.getY() + map.getRasterSize());
                break;
            case 4:
                point = new Point(lastPosition.getX() - map.getRasterSize(), lastPosition.getY());
                break;
            case 5:
                point = lastPosition;
                break;
            case 6:
                point = new Point(lastPosition.getX() + map.getRasterSize(), lastPosition.getY());
                break;
            case 7:
                point = new Point(lastPosition.getX() - map.getRasterSize(), lastPosition.getY() - map.getRasterSize());
                break;
            case 8:
                point = new Point(lastPosition.getX(), lastPosition.getY() - map.getRasterSize());
                break;
            case 9:
                point = new Point(lastPosition.getX() + map.getRasterSize(), lastPosition.getY() - map.getRasterSize());
                break;
            default:
                throw new IllegalArgumentException();
        }

        if (!trackArea.contains(point.getX(), point.getY())) {
            //System.err.println("OUT");
        } else if (map.getFirstStartLine().intersectsLine(lastPosition.getX(), lastPosition.getY(),
                point.getX(), point.getY())) {

            System.err.println(Math.acos(((point.getX() - lastPosition.getX())
                    * (map.getFirstStartLine().getX1() - map.getFirstStartLine().getX2())
                    + (point.getY() - lastPosition.getY())
                    * (map.getFirstStartLine().getY1() - map.getFirstStartLine().getY2()))
                    / (Math.sqrt((point.getX() - lastPosition.getX()) * (point.getX() - lastPosition.getX())
                            + (point.getY() - lastPosition.getY()) * (point.getY() - lastPosition.getY()))
                    * Math.sqrt((map.getFirstStartLine().getX1() - map.getFirstStartLine().getX2()) * (map.getFirstStartLine().getX1() - map.getFirstStartLine().getX2())
                            + (map.getFirstStartLine().getY1() - map.getFirstStartLine().getY2()) * (map.getFirstStartLine().getY1() - map.getFirstStartLine().getY2())))));

            System.err.println("CROSS");
        }

        player.addTurn(point);
        broadcastTurn(player, point);

        nextPlayerOnTurn();
    }

    private void startGame() {
        trackArea = map.getArea();
        timer = new Timer();

        Line2D startline = map.getFirstStartLine();

        Point middle = GeomUtils.pointInTheMiddle(new Point((int) startline.getX1(), (int) startline.getY1()), new Point((int) startline.getX2(), (int) startline.getY2()));
        Point start = GeomUtils.alignInGrid(middle, map.getRasterSize());
        if (!trackArea.contains(start.getX(), start.getY())) {
            throw new IllegalArgumentException("Invalid Track");
        }

        for (Player player : players) {
            player.addTurn(start);
            broadcastTurn(player, start);
        }
        nextPlayerOnTurn();
    }

    private void nextPlayerOnTurn() {
        final Player player = players.peekFirst();

        turnTimerTask = new TimerTask() {
            @Override
            public void run() {
                player.kick();
                exit(player);
            }
        };

        timer.schedule(turnTimerTask, 30000);
        player.awaitTurn();
    }

    private void broadcastMessage(String message) {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    private void broadcastTurn(Player player, Point point) {
        for (Player p : players) {
            p.sendTurn(player, point);
        }
    }

    public void exit(Player player) {
        if (players.peekFirst() == player) {
            if (turnTimerTask != null) {
                turnTimerTask.cancel();
            }
            players.pollFirst();
            if (players.size() > 0) {
                nextPlayerOnTurn();
            }
        } else {
            players.remove(player);
        }
    }
}
