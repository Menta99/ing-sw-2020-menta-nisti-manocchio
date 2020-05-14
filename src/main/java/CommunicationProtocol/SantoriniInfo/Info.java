package CommunicationProtocol.SantoriniInfo;

import java.io.Serializable;

/**
 * Immutable Class containing all the information for the Client
 */
public class Info implements Serializable {
    private final BoxInfo[][] grid;
    private final GodInfo[] gods;
    private final PlayerInfo[] players;

    /**
     * Constructor of the Class, complete version
     * @param grid playground information
     * @param gods card information
     * @param players player information
     */
    public Info(BoxInfo[][] grid, GodInfo[] gods, PlayerInfo[] players){
        this.grid = grid;
        this.gods = gods;
        this.players = players;
    }

    /**
     * Constructor of the Class, grid version
     * @param grid playground information
     */
    public Info(BoxInfo[][] grid){
        this.grid = grid;
        this.gods = null;
        this.players = null;
    }

    /**
     * Constructor of the Class, card version
     * @param gods card information
     */
    public Info(GodInfo[] gods){
        this.grid = null;
        this.gods = gods;
        this.players = null;
    }

    /**
     * Constructor of the Class, player version
     * @param players player information
     */
    public Info(PlayerInfo[] players){
        this.grid = null;
        this.gods = null;
        this.players = players;
    }

    /**
     * Constructor of the Class, grid + single player version
     * @param grid playground information
     * @param player single player information
     */
    public Info(BoxInfo[][] grid, PlayerInfo player){
        this.grid = grid;
        this.gods = null;
        this.players = new PlayerInfo[1];
        players[0] = player;
    }

    /**
     * Constructor of the Class, single player version
     * @param player single player information
     */
    public Info(PlayerInfo player){
        this.grid = null;
        this.gods = null;
        this.players = new PlayerInfo[1];
        players[0] = player;
    }

    public BoxInfo[][] getGrid() {
        return grid;
    }

    public GodInfo[] getGods() {
        return gods;
    }

    public PlayerInfo[] getPlayers() {
        return players;
    }
}
