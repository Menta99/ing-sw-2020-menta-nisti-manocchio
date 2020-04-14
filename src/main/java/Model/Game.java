package Model;

import Model.Godcards.GodCard;
import Model.Godcards.GodDeck;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private static Game instance = null;
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
        try {
            this.actualPlayer = players.get(0);
        }
        catch (NullPointerException e){
            System.err.println("No one in game");
            e.printStackTrace();
        }
        this.id = new Random().nextInt(10000);
        this.winner = null;
        this.gameFinished = false;
        actualPlayer.turnStart();
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
        if (gameFinished){
            return;
        }
        actualPlayer.turnStart();
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
    public int getId() {
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

    /**
     * Checks if the game is finished
     */
    public void CheckGameFinished() {
        int counter = 0;
        for (Player player : players){
            if (player.isLoser()){
                counter++;
            }
            if (player.isWinner()){
                gameFinished = true;
                winner = player;
            }
        }
        if (counter == players.size() - 1){
            gameFinished = true;
            for (Player player : players){
                if (!player.isLoser()){
                    winner = player;
                    player.setWinner(true);
                }
            }
        }
        if (gameFinished){
            System.out.println("\nCongratulation" + winner.getNickName() + "won the Game !");
        }
    }
}
