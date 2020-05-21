package Model;

import Controller.Controller;
import Controller.SavedData.GameData;
import Controller.SavedData.MapData;
import Controller.SavedData.PlayerData;
import Controller.SavedData.WorkerData;
import Model.Godcards.GodCard;
import Model.Godcards.GodDeck;
import Model.Godcards.GodFactory;
import Model.Godcards.GodsEnum;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Class representing the game
 */
public class Game {
    private static Game instance = null;
    private Controller controller;
    private int id;
    private ArrayList<Player> players;
    private Player actualPlayer;
    private final PlayGround map;
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
     * Getter of the Game instance (Singleton), creates a new object if none is present
     * @return instance of Game
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

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Player> getPlayer() {
        return this.players;
    }

    public Player getActualPlayer() {
        return this.actualPlayer;
    }

    public void setActualPlayer(Player actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    public GodDeck getDeck() {
        return this.deck;
    }

    public ArrayList<GodCard> getActiveCards() {
        return this.activeCards;
    }

    public int getActualTurn() {
        return this.actualTurn;
    }

    public Player getWinner() {
        return this.winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean isGameFinished() {
        return this.gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public void loadGame() {
        ArrayList<String> gameData = getSavedGame();
        Game myGame = Game.getInstance();
        myGame.id = parseInt(gameData.get(0));
        myGame.actualTurn = parseInt(gameData.get(1));
        int playerNumber = parseInt(gameData.get(2));
        loadPlayers(playerNumber, gameData);
        for (Player player : players){
            if (!player.isLoser()){
                player.loadWorkers(gameData, players.indexOf(player));
            }
        }
        PlayGround.getInstance().loadMap(gameData);
        setActualPlayer(players.get(actualTurn % players.size()));
    }

    public void loadPlayers(int playerNumber, ArrayList<String> gameInfo){
        GodCard card;
        activeCards = new ArrayList<>();
        int playerDataIndex = GameData.Size() + MapData.Size() + 1;
        for (int i=0; i < playerNumber; i++){
            new Player(gameInfo.get(playerDataIndex));
            //card = new GodFactory().create(GodsEnum.valueOf((gameInfo.get(playerDataIndex+1).toUpperCase())));
            card = deck.getCardList().get(GodsEnum.valueOf((gameInfo.get(playerDataIndex+1).toUpperCase())).ordinal());
            card.setChosen(true);
            card.setPicked(true);
            card.setOwner(players.get(i));
            players.get(i).setGod(card);
            Game.getInstance().getActiveCards().add(players.get(i).getCard());
            if (gameInfo.get(playerDataIndex-1).equals("1")){
                players.get(i).setLoser(true);
            }
            playerDataIndex += 2* WorkerData.Size() + PlayerData.Size(); //nextPlayerNickname
        }
    }

    public ArrayList<String> getSavedGame() {
        String fileName = ("temp/savedGame.txt");
        ArrayList<String> gameData = new ArrayList<>();
        String singleData;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            try {
                while ((singleData = reader.readLine()) != null) {
                    gameData.add(singleData);
                }
                reader.close();
            } catch (IOException e) {
                System.out.println("Error during File read");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find data");
        }
        return gameData;
    }
}