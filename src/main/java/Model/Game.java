package Model;

import Controller.Controller;
import Model.Godcards.GodCard;
import Model.Godcards.GodDeck;

import java.util.ArrayList;

public class Game {
    private static Game instance = null;
    private Controller controller;
    private int id;
    private ArrayList<Player> players;
    private Player actualPlayer;
    private PlayGround map;
    private GodDeck deck;
    private ArrayList<GodCard> activeCards;
    private int actualTurn;
    private Player winner;
    private boolean gameFinished;

    /**
     * Private Constructor of Game (Singleton)
     */
    private Game() {
        this.id = -1;
        this.players = new ArrayList<>();
        this.actualPlayer = null;
        this.map = PlayGround.getInstance();
        this.deck = new GodDeck();
        this.activeCards = null;
        this.actualTurn = 0;
        this.winner = null;
        this.gameFinished = false;
    }

    /**
     * Getter of the Game istances (Singleton), creates a new object if none is present
     *
     * @return istance of Game
     */
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    /**
     * Cleans the Game and the Playground, preparing them for a new game
     */
    public void CleanGame(){
        this.id = -1;
        this.players = new ArrayList<>();
        this.actualPlayer = null;
        this.deck = new GodDeck();
        this.activeCards = null;
        this.actualTurn = 0;
        this.winner = null;
        this.gameFinished = false;
        this.map.Clean();
    }

    /**
     * Increments the actualTurn by 1
     */
    public void IncrementActualTurn(){
        this.actualTurn++;
    }

    /**
     * Extracts the active cards
     * @return true or false if it succeed
     * @throws NullPointerException if requested invalid action on the cards
     */
    public boolean ExtractCard(ArrayList<Integer> index){
        if (activeCards == null) {
            try{
                activeCards = this.getPlayer().get(0).Draw(this.getDeck(), index);
            }
            catch (NullPointerException e){
                e.printStackTrace();
                return false;
            }
            if (activeCards != null) {
                return true;
            }
        }
        return false;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Getter of id
     */
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter of the players' ArrayList
     */
    public ArrayList<Player> getPlayer() {
        return this.players;
    }

    /**
     * Getter of actualPlayer
     */
    public Player getActualPlayer() {
        return this.actualPlayer;
    }

    /**
     * Setter of ActualPlayer
     */
    public void setActualPlayer(Player actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    /**
     * Getter of deck
     */
    public GodDeck getDeck() {
        return this.deck;
    }

    /**
     * Getter of activeCards
     */
    public ArrayList<GodCard> getActiveCards() {
        return this.activeCards;
    }

    /**
     * Getter of the actual turn
     */
    public int getActualTurn() {
        return this.actualTurn;
    }

    /**
     * Getter of winner Player
     */
    public Player getWinner() {
        return this.winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    /**
     * Getter of gameFinished
     */
    public boolean isGameFinished() {
        return this.gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }
}