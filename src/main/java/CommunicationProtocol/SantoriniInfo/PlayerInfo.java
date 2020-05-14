package CommunicationProtocol.SantoriniInfo;

import Model.Game;
import Model.Player;
import Server.ClientHandler;
import View.Colors;

import java.io.Serializable;

/**
 * Immutable Class containing player information
 */
public class PlayerInfo implements Serializable {
    private final int position;
    private final String name;
    private final Colors color;
    private final int god;

    /**
     * Constructor of the Class, complete version
     * @param position index of the player
     * @param name nickname of the player
     * @param color color of the player
     * @param god card of the player
     */
    public PlayerInfo(int position, String name, Colors color, int god){
        this.position = position;
        this.name = name;
        this.color = color;
        this.god = god;
    }

    /**
     * Constructor of the Class, ClientHandler version
     * @param player ClientHandler of the player
     */
    public PlayerInfo(ClientHandler player){
        this.position = player.getPlayerNum();
        this.name = player.getNickName();
        this.color = player.getPlayer().getColor();
        this.god = Game.getInstance().getDeck().getCardList().indexOf(player.getPlayer().getCard());
    }

    /**
     * Constructor of the Class, Player version
     * @param player Player to which belongs information
     */
    public PlayerInfo(Player player){
        this.position = Game.getInstance().getPlayer().indexOf(player);
        this.name = player.getNickName();
        this.color = player.getColor();
        this.god = Game.getInstance().getDeck().getCardList().indexOf(player.getCard());
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public Colors getColor() {
        return color;
    }

    public int getGod() {
        return god;
    }
}
