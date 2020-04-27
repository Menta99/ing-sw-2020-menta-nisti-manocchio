package Controller;

import ComunicationProtocol.CliCommandMsg;
import Model.*;
import Server.ClientHandler;
import Server.Server;
import View.Colors;
import VirtualView.VirtualView;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
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
        Clean();
        LobbyCreation();
        VirtualWelcome();
        VirtualGodPhase(handlers.get(0));
        StartGame();
        while (active.get()){
            TurnStart(match.getActualPlayer());
        }
        Server.UpdateServer();
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
        for(ClientHandler handler : handlers){
            if(!match.getPlayer().get(handlers.indexOf(handler)).isView()) {
                virtual.CliWelcomeScreen(handler);
            }
            else{
                //gui
            }
        }
    }

    /**
     * Challenger's ChooseGodPhase is starting
     * @param challenger
     */
    public void VirtualGodPhase(ClientHandler challenger){
        if (!challenger.getPlayer().isView()){
            virtual.CliChooseGodPhase(challenger);
        }
        else {
            //gui
        }
    }

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
        VirtualTurnStart(player.isView());
        Boolean canDoSomething = false;
        player.SetUpWorkers();
        if ((match.getActualTurn() / handlers.size()) == 0) {
            InitializeWorkers(player);
            EndTurn();
            return;
        }
        if (player.getCard().isActivePower()){
            player.setUsePower(VirtualAskPower(player.isView()));
            if (player.isUsePower()) {
                player.getCard().activeSubroutine();
                player.setUsePower(false);
                EndTurn();
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
            Lose(player);
            return;
        }
        if (player.getSelectedWorker().CanBuild()){
            BuildPhase(player);
        }
        else {
            Lose(player);
            return;
        }
        EndTurn();
    }

    /**
     * Initialize workers
     */
    public void InitializeWorkers(Player player){
        Box box;
        for(Worker worker : player.getWorkers()){
            box = VirtualAskPlace(player.getWorkers().indexOf(worker) + 1, player.isView());
            while (worker.getPosition() == null){
                if (worker.setInitialPosition(box)){
                    worker.setState(true);
                }
                else {
                    VirtualNotValidDest(player.isView());
                    box = VirtualAskPlace(1, player.isView());
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
        Worker candidate;
        candidate = VirtualAskWorker(player.isView());
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
        Update(false, false);
        Box box = VirtualAskMovement(player.isView());
        if (player.getSelectedWorker().LegalMovement(box)){
            player.getSelectedWorker().Move(box);
        }
        else{
            VirtualNotValidDest(player.isView());
            MovePhase(player);
            return;
        }
        CheckGameFinished();
    }

    /**
     * Start of the Build phase, you must build with the selected worker
     */
    public void BuildPhase(Player player){
        UpdateAll(false, true);
        Box box = VirtualAskBuilding(player.isView());
        if (player.getCard().getName().equals("Atlas")){
            if (VirtualAskPower(player.isView())){
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
            VirtualNotValidDest(player.isView());
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
    public void Lose(Player player){
        VirtualLose(player.isView());
        player.setLoser(true);
        //CheckGameFinished();
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
            VirtualGameFinished();
            active.set(false);
        }
    }

    /**
     * Call the corresponding method in the virtual view class
     * @param view
     */
    public void VirtualTurnStart(boolean view){
        if (!view){
            virtual.CliTurnStartMessage();
        }
        else{

        }
    }

    /**
     * Call the corresponding method in the virtual view class
     * @param view
     */
    public boolean VirtualAskPower(boolean view){
        if (!view){
            return virtual.CliAskForPower(handlers.get(match.getPlayer().indexOf(match.getActualPlayer())));
        }
        return true; //else gui
    }

    /**
     * Call the corresponding method in the virtual view class
     * @param view
     */
    public Worker VirtualAskWorker(boolean view){
        if (!view){
            return virtual.CliAskForWorker(handlers.get(match.getPlayer().indexOf(match.getActualPlayer())));
        }
        return null; //else gui
    }

    /**
     * Call the corresponding method in the virtual view class
     * @param view
     */
    public Box VirtualAskMovement(boolean view){
        if (!view){
            return virtual.CliAskForMovement(handlers.get(match.getPlayer().indexOf(match.getActualPlayer())));
        }
        return null; //else gui
    }

    /**
     * Call the corresponding method in the virtual view class
     * @param view
     */
    public Box VirtualAskBuilding(boolean view){
        if (!view){
            return virtual.CliAskForBuilding(handlers.get(match.getPlayer().indexOf(match.getActualPlayer())));
        }
        return null; //else gui
    }

    /**
     * Call the corresponding method in the virtual view class
     * @param view
     */
    public void VirtualNotValidDest(boolean view){
        if (!view){
            virtual.CliNotValidDestination(handlers.get(match.getPlayer().indexOf(match.getActualPlayer())));
            return;
        }
        return; //else gui
    }

    /**
     * Call the corresponding method in the virtual view class
     * @param workerNumber
     * @param view
     * @return
     */
    public Box VirtualAskPlace(int workerNumber, boolean view){
        if (!view){
            return virtual.CliAskForPlacement(handlers.get(match.getPlayer().indexOf(match.getActualPlayer())), workerNumber);
        }
        return null; //else gui
    }

    /**
     * Call the corresponding method in the virtual view class
     * @param view
     */
    public void VirtualLose(boolean view){
        if (!view){
            virtual.CliLose(handlers.get(match.getPlayer().indexOf(match.getActualPlayer())));
        }
        //else gui
    }

    /**
     * Call the corresponding method in the virtual view class
     * @param
     */
    public void VirtualGameFinished(){
        System.out.println("[F] - Game finished");
        for(ClientHandler handler : handlers){
            if(!match.getPlayer().get(handlers.indexOf(handler)).isView()) {
                virtual.CliGameFinished(handler);
            }
            else{
                //gui
            }
        }
        //Server.GameClose();//supposed common part
    }

    /**
     * Updating message for all players
     * @param generic
     * @param phase
     */
    public void UpdateAll(boolean generic, boolean phase){
        ClientHandler actual = handlers.get(match.getPlayer().indexOf(match.getActualPlayer()));
        CliCommandMsg msg1 = virtual.MapInfo(generic, phase, "");
        CliCommandMsg msg2 = virtual.MapInfo(true, false, "Look at " + match.getActualPlayer().getColor() + match.getActualPlayer().getNickName() + Colors.RESET + "'s move");
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