package Model;

import Controller.Controller;
import Model.Godcards.GodCard;
import Model.Godcards.GodDeck;
import View.Colors;
import View.cli.Cli;
import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    private Controller controller;
    private Colors color;
    private String nickName;
    private int turn;
    private boolean challenger; // Possibile rimozione
    private GodCard card;
    private boolean winner;
    private boolean loser;
    private boolean usePower;
    private ArrayList <Worker> workers;
    private Worker selectedWorker;

    /**
     * Draws the GodCards of the Game, made by the Challenger
     * @param deck ArrayList of GodCard
     * @return ArrayList of chosen GodCard or null if the player isn't the Challenger
     * @throws NullPointerException if requested invalid action on the cards
     */
    public ArrayList<GodCard> Draw(GodDeck deck, int[] index){//da modificare con la view
        if (challenger) {
            ArrayList<GodCard> gods = new ArrayList<GodCard>();
            GodCard chosen = null;
            int i = 0;
            try {
                while (i < Game.getInstance().getPlayer().size()) {//attenzione rischio vuotezza, controllare durante l'implementazione dello start
                    chosen = Game.getInstance().getDeck().Draw(index[i]);
                    if (chosen != null) {
                        gods.add(chosen);
                        chosen = null;
                        i++;
                    }
                }
                return gods;
            }
            catch (NullPointerException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }
    /**
     * Choose the GodCard of the Player
     * @return true or false if the action succeed
     * @throws NullPointerException if requested invalid action on the cards
     */
    public boolean ChooseGod(int index){//sistemare con la view
        try {
            if (card == null) {
                GodCard pick = Game.getInstance().getActiveCards().get(index);
                if (!pick.isPicked()) {
                    pick.setPicked(true);
                    this.card = pick;
                    pick.setOwner(this);
                    return true;
                }
            }
            return false;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Constructor of Player
     */
    public Player(String nickName) {
        this.controller = new Controller();
        this.nickName = nickName;
        this.winner = false;
        this.loser = false;
        this.usePower = false;
        this.turn = 0;
        this.workers = new ArrayList<Worker>();
        this.workers.add(new Worker());
        this.workers.add(new Worker());
        this.workers.get(0).setOwner(this);
        this.workers.get(1).setOwner(this);
        Game.getInstance().getPlayer().add(this);
        if (Game.getInstance().getPlayer().size() == 1) {
            this.challenger = true;
        }
        else {
            this.challenger = false;
        }
        if (Game.getInstance().getPlayer().size()==1){
            this.color = Colors.GREEN;
        }
        if (Game.getInstance().getPlayer().size()==2){
            this.color = Colors.BLUE;
        }
        if (Game.getInstance().getPlayer().size()==3){
            this.color = Colors.RED;
        }
    }

    /**
     * Getter of the Worker on the Box selected
     * @param box selected box
     * @return worker o null if no Worker is present on the Box selected
     * @throws NullPointerException if requested invalid action on a box
     */
    public Worker selectWorker(Box box){
        try {
            if (box.getUpperLevel() == PawnType.WORKER) {
                Worker toReturn = (Worker)box.getStructure().get(box.getStructure().size()-1);
                if(toReturn.getOwner().equals(this)){
                    return (Worker) box.getStructure().get(box.getStructure().size() - 1);
                }
            }
            return null;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Getter of the selected worker
     * @return
     */
    public Worker getSelectedWorker() {
        return selectedWorker;
    }

    /**
     * You must Select a valid worker
     */
    public void selectWorkerPhase(){
        controller.ShowMapRequest();
        Worker candidate;
        candidate = controller.askForWorker();
        if (candidate.CanMove()){
            selectedWorker = candidate;
        }
        else {
            selectWorkerPhase();
        }
    }

    /**
     * Start of the movement phase, you must move the selected worker
     */
    public void movePhase(){
        controller.ShowLegalMovementRequest();
        Box box = controller.askForMovement();
        if (selectedWorker.LegalMovement(box)){
            selectedWorker.Move(box);
        }
        else{
            controller.notValidDestination();
            movePhase();
            return;
        }
        Game.getInstance().CheckGameFinished();
    }

    /**
     * Start of the Build phase, you must build with the selected worker
     */
    public void buildPhase(){
        controller.ShowLegalBuildingRequest();
        Box box = controller.askForBuilding();
        if (card.getName().equals("Atlas")){
            if (controller.askForPower()){
                if (selectedWorker.LegalBuild(box)){
                    selectedWorker.BuildDome(box);
                    return;
                }
            }
        }
        if (selectedWorker.LegalBuild(box)){
            selectedWorker.Build(box);
        }
        else{
            controller.notValidDestination();
            buildPhase();
            return;
        }
    }

    /**
     * Pass your Turn if you made the mandatory actions
     */
    public void endTurn(){
        for (Player players : Game.getInstance().getPlayer()){
            if(!players.isLoser()) {
                players.getCard().myVictoryCondition();
            }
        }
        Game.getInstance().CheckGameFinished();
        Game.getInstance().NextTurn();
    }

    /**
     * Start your Turn, you can make your actions; if you can't, you lose
     */
    public void turnStart(){
        selectedWorker=null;
        if (loser){
            endTurn();
        }
        controller.TurnStart();
        Boolean canDoSomething = false;
        for (Worker worker : workers){
            worker.setDidBuild(false);
            worker.setMoved(false);
            worker.setDidClimb(false);
            worker.setLastPosition(worker.getPosition());
        }
        if ((Game.getInstance().getActualTurn() / Game.getInstance().getPlayer().size()) == 0) {
            this.initializeWorkers();
            this.endTurn();
        }
        if (card.isActivePower()){
            usePower = controller.askForPower();
            if (usePower) {
                card.activeSubroutine();
                usePower = false;
                endTurn();
            }
        }
        for (Worker worker : workers) {
                canDoSomething = canDoSomething || worker.CanMove();
            }
        if (canDoSomething) {
            this.selectWorkerPhase();
            this.movePhase();
            canDoSomething = false;
        }
        else {
            lose();
        }
        if (selectedWorker.CanBuild()){
            this.buildPhase();
        }
        else {
            lose();
        }
        this.endTurn();
    }

    /**
     * Initialize workers
     */
    private void initializeWorkers(){
        controller.ShowMapRequest();
        Box box = controller.askForPlacement(1);
        Worker worker = workers.get(0);
        while (worker.getPosition() == null){
            if (worker.setInitialPosition(box)){
                worker.setState(true);
            }
            else {
                controller.notValidDestination();
                box = controller.askForPlacement(1);
            }
        }
        controller.ShowMapRequest();
        box = controller.askForPlacement(2);
        worker = workers.get(1);
        while (worker.getPosition() == null){
            if (worker.setInitialPosition(box)){
                worker.setState(true);
            }
            else {
                controller.notValidDestination();
                box = controller.askForCoordinates();
            }
        }
    }

    /**
     * Getter method for workers
     */
    public ArrayList<Worker> getWorkers(){
        return this.workers;
    }

    /**
     * Setter method used in winning condition
     * @param winner Player who won the Game*/
    public void setWinner(boolean winner){
        this.winner = winner;
    }

    /**
     * The player loses and he's removed from the game
     */
    public void lose(){
        controller.lose();
        loser=true;
        Game.getInstance().CheckGameFinished();
        for (Worker worker : workers){
            worker.getPosition().setOccupied(false);
            worker.getPosition().getStructure().remove(worker.getPosition().getStructure().size()-1);
            selectedWorker=null;
            workers = null;
        }
        endTurn();
    }

    /** Getter method of card
     *
     */
    public GodCard getCard() {
        return card;
    }

    public String getNickName(){
        return this.nickName;
    }

    public boolean isLoser() {
        return loser;
    }

    public boolean isWinner() {
        return winner;
    }

    public Colors getColor() {
        return color;
    }

    public Controller getController() {
        return controller;
    }

    public void setSelectedWorker(Worker selectedWorker) {
        this.selectedWorker = selectedWorker;
    }

    public boolean isUsePower() {
        return usePower;
    }
}
