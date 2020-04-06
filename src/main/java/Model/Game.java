package Model;

import java.util.ArrayList;

public class Game {
    private static Game instance = null;
    private String id;
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
        this.id = null;
        this.players = new ArrayList<Player>();
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
     * Getter of the players' ArrayList
     *
     * @return player
     */
    public ArrayList<Player> getPlayer() {
        return this.players;
    }

    /**
     * Getter of the actual turn
     *
     * @return actual turn
     */
    public int getActualTurn() {
        return this.actualTurn;
    }

    /**
     * Starts a new game
     * @throws NullPointerException if requested invalid action on the players
     */
    public void StartGame(){
        //this.players = people;
        this.map = PlayGround.getInstance();
        try {
            this.actualPlayer = players.get(0);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        this.id = "0000"; // TO CHANGE
        this.winner = null;
        this.gameFinished = false;
    }
    /**
     * Goes to the next round
     * @throws NullPointerException if requested invalid action on the players
     */
    public void NextTurn(){
        this.actualTurn++;
        try{
            this.actualPlayer = this.players.get(this.actualTurn % this.players.size());
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    /**
     * Extracts the active cards
     * @return true or false if it succeed
     * @throws NullPointerException if requested invalid action on the cards
     */
    public boolean ExtractCard(int[] index){
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

    /**
     * Getter of gameFinished
     *
     * @return true or false if the game finished of not
     */
    public boolean getGameFinished() {
        return this.gameFinished;
    }

    /**
     * Getter of winner Player
     *
     * @return Player who won the game
     */
    public Player getWinner() {
        return this.winner;
    }

    /**
     * Getter of deck
     *
     * @return deck of GodCards
     */
    public GodDeck getDeck() {
        return this.deck;
    }

    /**
     * Getter of actualPlayer
     *
     * @return Player
     */
    public Player getActualPlayer() {
        return this.actualPlayer;
    }

    /**
     * Getter of id
     *
     * @return id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter of activeCard
     *
     * @return active GodCards
     */
    public ArrayList<GodCard> getActiveCards() {
        return this.activeCards;
    }

    /**
     * Delete all the game (support method for testing)
     */
    public void Delete() {
        instance = null;
    }

}
