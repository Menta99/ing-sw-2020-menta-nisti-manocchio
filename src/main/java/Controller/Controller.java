package Controller;

import ComunicationProtocol.*;
import Model.*;
import Model.Godcards.GodCard;
import Server.ClientHandler;
import Server.Server;
import VirtualView.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class controller of MVC pattern
 */
public class Controller implements Runnable{
    private VirtualView virtual;
    private Game match;
    private int playerNum = 2;
    private ArrayList<ClientHandler> handlers;
    private AtomicBoolean active;

    /**
     * Constructor of the class
     */
    public Controller(){
        this.match = Game.getInstance();
        this.virtual = new VirtualView(match);
        this.handlers = new ArrayList<>();
        this.active = new AtomicBoolean(true);
    }

    /**
     * Override of run method in Runnable: setting and start of a new game;
     */
    @Override
    public void run() {
        try {
            Clean();
            LobbyCreation();
            VirtualWelcome();
            StartGame();
            while (active.get()) {
                TurnStart(match.getActualPlayer());
            }
        }
        catch (NullPointerException | ConcurrentModificationException e){
        }
        finally {
            Server.UpdateServer();
        }
    }

    /**
     * Initializing phase for each player
     */
    public void LobbyCreation(){
        int counter = 0;
        System.out.println("[3] - Start Lobby Creation");
        InitializePlayer(counter);
        counter++;
        for(; counter < playerNum; counter++){
            InitializePlayer(counter);
        }
        System.out.println("[4] - Lobby Completed");
    }

    /**
     * Each client's connecting to server...
     * @param position
     */
    public void InitializePlayer(int position){
        try {
            Socket client = Server.getServer().accept();
            ClientHandler handler = new ClientHandler(client, position);
            handlers.add(handler);
            new Thread(handler).start();
        } catch (IOException e) {
            System.err.println("Problem initializing a new Player");
        }
    }

    /**
     * Print the welcome screen starting the game
     */
    public void VirtualWelcome(){
        System.out.println("[5] - Game start");
        GodInfo[] gods = new GodInfo[14];
        PlayerInfo[] players = new PlayerInfo[playerNum];
        ArrayList<GodCard> deck = Game.getInstance().getDeck().getCardList();
        ArrayList<Player> list = Game.getInstance().getPlayer();
        for(GodCard card : deck){
            gods[deck.indexOf(card)] = new GodInfo(deck.indexOf(card), card.getName(), card.getPower(), false);
        }
        for(Player player : list){
            players[list.indexOf(player)] = new PlayerInfo(list.indexOf(player), player.getNickName(), player.getColor(), -1);
        }
        virtual.WelcomePacket(gods, players);
        virtual.ChooseGodPhase(handlers.get(0));
    } //ready

    /**
     * Set Up a new Game, generating a random ID
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
     * Start your Turn, you can make your actions; if you can't, you lose
     */
    public void TurnStart(Player player){
        player.setSelectedWorker(null);
        if (player.isLoser()){
            EndTurn();
            return;
        }
        virtual.TurnStartMessage();
        Boolean canDoSomething = false;
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
     * Initialize workers
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
     * You must Select a valid worker
     */
    public void SelectWorkerPhase(Player player){
        ClientHandler handler = handlers.get(match.getPlayer().indexOf(player));
        Worker candidate;
        candidate = virtual.AskWorker(handler);
        if (candidate.CanMove()){
            player.setSelectedWorker(candidate);
        }
        else {
            SelectWorkerPhase(player);
        }
    }

    /**
     * Start of the movement phase, you must move the selected worker
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
     * Start of the Build phase, you must build with the selected worker
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
     * Pass your Turn if you made the mandatory actions
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
     * Goes to the next round
     * @throws NullPointerException if requested invalid action on the players
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
            System.out.println("[F] - Game finished");
            for(ClientHandler handler : handlers){
                virtual.GameFinished(handler);
            }
            active.set(false);
        }
    }

    /**
     * Call the corresponding method in the virtual view class
     */
    public boolean VirtualAskPower(){
        return virtual.AskPower(handlers.get(match.getPlayer().indexOf(match.getActualPlayer())));
    }//removable

    /**
     * Call the corresponding method in the virtual view class
     */
    public Worker VirtualAskWorker(){
        return virtual.AskWorker(handlers.get(match.getPlayer().indexOf(match.getActualPlayer())));
    }//removable

    /**
     * Updating message for all players
     * @param generic
     * @param phase
     */
    public void UpdateAll(boolean generic, boolean phase){
        ClientHandler actual = handlers.get(match.getPlayer().indexOf(match.getActualPlayer()));
        ArrayList<Integer> list= new ArrayList<>();
        list.add(match.getPlayer().indexOf(match.getActualPlayer()));
        list.add(0);
        CliCommandMsg msg1 = new CliCommandMsg(CommandType.UPDATE, SubCommandType.DEFAULT, virtual.MapInfo(generic, phase), null, null, list);
        CliCommandMsg msg2 = new CliCommandMsg(CommandType.UPDATE, SubCommandType.DEFAULT, virtual.MapInfo(true, false), null, null, list);
        virtual.Echo(actual, msg1, msg2);
    }

    /**
     * Updating message for a player
     * @param generic
     * @param phase
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
}