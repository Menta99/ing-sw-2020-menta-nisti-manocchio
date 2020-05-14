package Controller;

import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import CommunicationProtocol.SantoriniInfo.Info;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import Controller.SavedData.GameData;
import Model.Box;
import Model.Game;
import Model.Player;
import Model.Worker;
import Server.ClientHandler;
import Server.Server;
import VirtualView.CoordinateType;
import VirtualView.VirtualView;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Integer.parseInt;

/**
 * Class of the controller of MVC pattern
 */
public class Controller implements Runnable{
    private final VirtualView virtual;
    private final Game match;
    private int playerNum = 2;
    private ArrayList<ClientHandler> handlers;
    private AtomicBoolean active;
    private AtomicBoolean loadedGame;
    private GameData gameData;

    /**
     * Constructor of the class
     */
    public Controller(){
        this.match = Game.getInstance();
        this.virtual = new VirtualView(match);
        this.handlers = new ArrayList<>();
        this.active = new AtomicBoolean(true);
        this.loadedGame = new AtomicBoolean(false);
    }

    /**
     * Setting and Start of a new Game
     * When finished updates the Server in order to start a new Game
     */
    @Override
    public void run() {
        try {
            Clean();
            LobbyCreation();
            if (!loadedGame.get()) {
                VirtualWelcome();
                StartGame();
            }
            gameData = new GameData();
            while (active.get()) {
                TurnStart(match.getActualPlayer());
            }

        }
        catch (NullPointerException | ConcurrentModificationException e){
            System.out.println("[C] - Controller Closed");
        }
        finally {
            Server.UpdateServer();
        }
    }

    /**
     * Creates the Lobby and sends to the players the Welcome Packet
     */
    public void LobbyCreation(){
        int counter = 0;
        System.out.println("[3] - Start Lobby Creation");
        InitializePlayer(counter);
        counter++;
        for(; counter < playerNum; counter++){
            if (loadedGame.get()){
                OldLobbyCreation();
                virtual.WelcomePacket(false);
                return;
            }
            InitializePlayer(counter);
        }
        System.out.println("[4] - Lobby Completed");
    }

    /**
     * Restores the old lobby from an interrupted Game
     * Resort all the Handlers
     */
    public void OldLobbyCreation(){
        playerNum = parseInt(Game.getInstance().getSavedGame().get(2));
        for (int i=handlers.size(); i<playerNum; i=handlers.size()){
            InitializePlayer(i);
        }
        sortHandlers();
    }

    /**
     * Accept a Client connection, then initializes the Player and his ClientHandler
     * @param position number of the player
     */
    public void InitializePlayer(int position){
        try {
            Socket client = Server.getServer().accept();
            ClientHandler handler = new ClientHandler(client, position);
            if (handler.getActive().get()) {
                handlers.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Problem initializing a new Player");
        }
    }

    /**
     * Sends the Welcome Packet and starts the Chose-God phase
     */
    public void VirtualWelcome(){
        System.out.println("[5] - Game start");
        virtual.WelcomePacket(true);
        virtual.ChooseGodPhase(handlers.get(0));
    }

    /**
     * Set Up a new Game selecting the actual player and generating a random ID
     */
    public void StartGame(){
        match.setActualPlayer(handlers.get(0).getPlayer());
        match.setId(new Random().nextInt(10000));
    }

    /**
     * Clean the game and prepare for a new one
     */
    public void Clean(){
        match.CleanGame();
        match.setController(this);
    }

    /**
     * Start the Turn, performing the different phases and checking the game finish conditions
     * @param player actual Player that performs the turn
     */
    public void TurnStart(Player player){
        gameData.SaveAll();
        player.setSelectedWorker(null);
        if (player.isLoser()){
            EndTurn();
            return;
        }
        virtual.TurnStartMessage();
        boolean canDoSomething = false;
        player.SetUpWorkers();
        if ((match.getActualTurn() / handlers.size()) == 0) {
            InitializeWorkers(player);
            EndTurn();
            return;
        }
        if (player.getCard().isActivePower()){
            player.setUsePower(VirtualAskPower());
            if (player.isUsePower()) {
                player.getCard().activeSubroutine();
                player.setUsePower(false);
                if(!player.isLoser()) {
                    EndTurn();
                }
                return;
            }
        }
        for (Worker worker : player.getWorkers()) {
            canDoSomething = canDoSomething || worker.CanMove();
        }
        if (canDoSomething) {
            SelectWorkerPhase(player);
            MovePhase(player);
            if(!active.get()){
                return;
            }
        }
        else {
            PlayerLose(player);
            return;
        }
        if (player.getSelectedWorker().CanBuild()){
            BuildPhase(player);
        }
        else {
            PlayerLose(player);
            return;
        }
        EndTurn();
    }

    /**
     * Initialize the starting position of a Player's workers
     * @param player actual Player that performs the positioning
     */
    public void InitializeWorkers(Player player){
        ClientHandler handler = handlers.get(match.getPlayer().indexOf(player));
        Box box;
        for(Worker worker : player.getWorkers()){
            box = virtual.AskCoordinates(handler, CoordinateType.INITIAL);
            while (worker.getPosition() == null){
                if (worker.setInitialPosition(box)){
                    worker.setState(true);
                }
                else {
                    virtual.NotValidDest(handler);
                    box = virtual.AskCoordinates(handler, CoordinateType.INITIAL);
                }
            }
            if(player.getWorkers().indexOf(worker) == 0) {
                UpdateAll(true, false);
            }
        }
    }

    /**
     * Select a Worker to play with
     * @param player actual Player that performs the selection
     */
    public void SelectWorkerPhase(Player player){
        ClientHandler handler = handlers.get(match.getPlayer().indexOf(player));
        Worker candidate = virtual.AskWorker(handler);
        if (candidate.CanMove()){
            player.setSelectedWorker(candidate);
        }
        else {
            SelectWorkerPhase(player);
        }
    }

    /**
     * Move Phase of a Player
     * @param player actual Player that performs the movement
     */
    public void MovePhase(Player player){
        ClientHandler handler = handlers.get(match.getPlayer().indexOf(player));
        Update(false, false);
        Box box = virtual.AskCoordinates(handler, CoordinateType.MOVE);
        if (player.getSelectedWorker().LegalMovement(box)){
            player.getSelectedWorker().Move(box);
        }
        else{
            virtual.NotValidDest(handler);
            MovePhase(player);
            return;
        }
        CheckGameFinished();
    }

    /**
     * Build Phase of a Player
     * @param player actual Player that performs the building
     */
    public void BuildPhase(Player player){
        ClientHandler handler = handlers.get(match.getPlayer().indexOf(player));
        UpdateAll(false, true);
        Box box = virtual.AskCoordinates(handler, CoordinateType.BUILD);
        if (player.getCard().getName().equals("Atlas")){
            if (virtual.AskPower(handler)){
                if (player.getSelectedWorker().LegalBuild(box)){
                    player.getSelectedWorker().BuildDome(box);
                    return;
                }
            }
        }
        if (player.getSelectedWorker().LegalBuild(box)){
            player.getSelectedWorker().Build(box);
        }
        else{
            virtual.NotValidDest(handler);
            BuildPhase(player);
            return;
        }
    }

    /**
     * End Turn after you made the mandatory actions
     */
    public void EndTurn(){
        for (Player player : match.getPlayer()){
            if(!player.isLoser()) {
                player.getCard().myVictoryCondition();
            }
        }
        CheckGameFinished();
        NextTurn();
    }

    /**
     * Goes to the next round, if the game is finished ends the controller execution
     */
    public void NextTurn(){
        if(match.isGameFinished()){
            active.set(false);
            return;
        }
        else {
            match.IncrementActualTurn();
            match.setActualPlayer(match.getPlayer().get(match.getActualTurn() % match.getPlayer().size()));
        }
    }

    /**
     * The player loses and he's removed from the game
     * @param player player that lose the game
     */
    public void PlayerLose(Player player){
        virtual.Lose(handlers.get(match.getPlayer().indexOf(player)));
        player.setLoser(true);
        for (Worker worker : player.getWorkers()){
            worker.getPosition().setOccupied(false);
            worker.getPosition().getStructure().remove(worker.getPosition().getStructure().size()-1);
            player.setSelectedWorker(null);
            player.setWorkers(null);
        }
        EndTurn();
    }

    /**
     * Checks if the game is finished
     */
    public void CheckGameFinished() {
        int counter = 0;
        for (Player player : match.getPlayer()){
            if (player.isLoser()){
                counter++;
            }
            if (player.isWinner()){
                match.setGameFinished(true);
                match.setWinner(player);
            }
        }
        if (counter == match.getPlayer().size() - 1){
            match.setGameFinished(true);
            for (Player player : match.getPlayer()){
                if (!player.isLoser()){
                    match.setWinner(player);
                    player.setWinner(true);
                }
            }
        }
        if (match.isGameFinished()){
            gameData.ResetData();
            System.out.println("[F] - Game finished");
            for(ClientHandler handler : handlers){
                virtual.GameFinished(handler);
            }
            active.set(false);
        }
    }

    /**
     * Request to the Virtual View to ask if the Player wants to use his power
     * @return true or false
     */
    public boolean VirtualAskPower(){
        return virtual.AskPower(handlers.get(match.getPlayer().indexOf(match.getActualPlayer())));
    }

    /**
     * Request to the Virtual View to Ask the Player to chose a Worker
     * @return Worker chosen by the player
     */
    public Worker VirtualAskWorker(){
        return virtual.AskWorker(handlers.get(match.getPlayer().indexOf(match.getActualPlayer())));
    }

    /**
     * Send an Update message to all players
     * @param generic type of map, true if is generic, false if it specific to a game phase
     * @param phase phase of the game
     */
    public void UpdateAll(boolean generic, boolean phase){
        ClientHandler actual = handlers.get(match.getPlayer().indexOf(match.getActualPlayer()));
        Info info1 = new Info(virtual.MapInfo(generic, phase), new PlayerInfo(actual));
        Info info2 = new Info(virtual.MapInfo(true, false), new PlayerInfo(actual));
        virtual.Echo(actual, new CommandMsg(CommandType.UPDATE_ACTION, info1), new CommandMsg(CommandType.UPDATE_ACTION, info2));
    }

    /**
     * Send an Update message to a player
     * @param generic type of map, true if is generic, false if it specific to a game phase
     * @param phase phase of the game
     */
    public void Update(boolean generic, boolean phase){
        ClientHandler actual = handlers.get(match.getPlayer().indexOf(match.getActualPlayer()));
        virtual.UpdateMap(actual, generic, phase);
    }

    public VirtualView getVirtual() {
        return virtual;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public ArrayList<ClientHandler> getHandlers() {
        return handlers;
    }

    public AtomicBoolean getActive() {
        return active;
    }

    public void setActive(AtomicBoolean active) {
        this.active = active;
    }

    public void setLoadedGame(AtomicBoolean loadedGame) {
        this.loadedGame = loadedGame;
    }

    public AtomicBoolean getLoadedGame() {
        return loadedGame;
    }

    public void sortHandlers(){
        ArrayList<ClientHandler> sortedList = new ArrayList<>();
        for (Player player : Game.getInstance().getPlayer()){
            for (ClientHandler handler : handlers){
                if (player.getNickName().equals(handler.getNickName())){
                    sortedList.add(handler);
                }
            }
        }
        handlers = sortedList;
    }
}