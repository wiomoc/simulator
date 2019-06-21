package simulator.server.model;

import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
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

    private LinkedList<Player> players;
    private SimulatorMap map;
    private Area trackArea;
    private String name;
    private String code;
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

        timer.schedule(turnTimerTask, 10000);
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
    
    private boolean endCondition(){
        boolean gewonnen = false;
        double outerMaxX = 0;
        double outerMinX = 0;
        double outerMaxY = 0;
        double outerMinY = 0;
        int anzahlSchnittL = 0;
        int anzahlSchnittR = 0;
        int anzahlSchnittO = 0;
        int anzahlSchnittU = 0;        

        //festellen der groeßten x und y werten von den Outerpoints
        for(int i = 0; i<(map.getOuter().size()-1); i++){
            outerMaxX = Math.max(map.getOuter().get(i).getX(), map.getOuter().get(i+1).getX());
            outerMinX = Math.min(map.getOuter().get(i).getX(), map.getOuter().get(i+1).getX());
            outerMaxY = Math.max(map.getOuter().get(i).getY(), map.getOuter().get(i+1).getY());
            outerMinY = Math.min(map.getOuter().get(i).getY(), map.getOuter().get(i+1).getY());
        }

        //Erstellen der Kontrolllinien die geschnitten werden
        Point2D.Double mitte = new Point2D.Double((outerMaxX-outerMinX)/2, (outerMaxY-outerMinY)/2);
        Line2D waagrechte = new Line2D.Double(outerMinX, mitte.y, outerMaxX, mitte.y);
        Line2D senkrechte = new Line2D.Double(mitte.x, outerMinY, mitte.x, outerMaxY);
        Line2D ziellinie = map.getFirstStartLine();

        Line2D playerline; //letzte pos und vorletzte pos des players

        GeneralPath outer = map.createPath(map.getOuter());

        //Ueberpruefung wie oft die Linien bei einer Runde geschnitten werden
        for (int j = 0; j < outerMaxY + 5; j = j + 5) {
            if(outer.intersects(mitte.x, j, 1, 5)){
                if(j< mitte.y){
                    anzahlSchnittO++;
                }
                else{
                    anzahlSchnittU++;
                }
            }
        }

        for (int i = 0; i < outerMaxX + 5; i = i + 5) {
            if(outer.intersects(i, mitte.y, 5, 1)){
                if(i< mitte.x){
                    anzahlSchnittL++;
                }
                else{
                    anzahlSchnittR++;
                }
            }
        }

        //Ueberpruefung ob der Spieler die Kontrolllinien oft genug geschnitten hat
        int pos1x = players.peekFirst().getLastTurn().getX();
        int pos1y = players.peekFirst().getLastTurn().getY();
        int pos2x = players.peekFirst().getBeforeLastTurn().getX();
        int pos2y = players.peekFirst().getBeforeLastTurn().getY();
           
        playerline = new Line2D.Double(pos1x, pos1y, pos2x, pos2y);

        if(playerline.intersectsLine(ziellinie)){ //intersectsline zwei linien kreuzen sich?
            players.peekFirst().setSpielerSchnittSZL(players.peekFirst().getSpielerSchnittSZL()+1);
        }
        else if(playerline.intersectsLine(waagrechte)){
            if(pos1x < mitte.x){
                players.peekFirst().setSpielerSchnittL(players.peekFirst().getSpielerSchnittL()+1);
            }else{
                players.peekFirst().setSpielerSchnittR(players.peekFirst().getSpielerSchnittR()+1);
            }
        }else if(playerline.intersectsLine(senkrechte)){
            if(pos1y < mitte.y){
                players.peekFirst().setSpielerSchnittO(players.peekFirst().getSpielerSchnittO()+1);
            }else{
                players.peekFirst().setSpielerSchnittU(players.peekFirst().getSpielerSchnittU()+1);
            }
        }
        if(players.peekFirst().getSpielerSchnittSZL()==2 && players.peekFirst().getSpielerSchnittL()==anzahlSchnittL
            && players.peekFirst().getSpielerSchnittR()==anzahlSchnittR && players.peekFirst().getSpielerSchnittO()==anzahlSchnittO
            && players.peekFirst().getSpielerSchnittU()==anzahlSchnittU){
            gewonnen = true;
        }
        return gewonnen; //broadcast an alle welcher spieler gewonnen hat
    }
}
