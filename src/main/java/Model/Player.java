package Model;

import Controller.Controller;
import Model.Godcards.GodCard;
import Model.Godcards.GodDeck;
import View.cli.Cli;

import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    private String nickName;
    private int turn;
    private boolean challenger; // Possibile rimozione
    private GodCard card;
    private boolean winner;
    private boolean loser;
    private boolean usePower;
    private ArrayList <Worker> workers;
    private Worker selectedWorker;
    private Scanner in; //INPUT
    private String msg = null;  //INPUT

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
     * @param pool ArrayList of the active GodCards
     * @return true or false if the action succeed
     * @throws NullPointerException if requested invalid action on the cards
     */
    public boolean ChooseGod(ArrayList<GodCard> pool, int index){//sistemare con la view
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
        this.in = new Scanner(System.in);
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
        Box box; //Ask for input valid Box
        System.out.println("\nSelect one of your workers");
        box = Controller.askForCoordinates();
        Worker candidate = selectWorker(box);
        if (candidate==null){
            System.out.println("\nNot a valid worker");
            selectWorkerPhase();
            return;
        }
        if (candidate.CanMove()){
            selectedWorker=candidate;
            return;
        }
        else {
            System.out.println("\nThat guy can't move!");
            selectWorkerPhase();
            return;
        }
    }

    /**
     * Start of the movement phase, you must move the selected worker
     */
    public void movePhase(){
        Box box = null; //Ask for input valid box
        System.out.println("\nWhere do you want to move your worker?");
        box = Controller.askForCoordinates();
        if (selectedWorker.LegalMovement(box)){
            selectedWorker.Move(box);
        }
        else{
            System.out.println("\nNot a valid destination");
            movePhase();
            return;
        }
    }

    /**
     * Start of the Build phase, you must build with the selected worker
     */
    public void buildPhase(){
        Box box = null; //Ask for input valid box
        System.out.println("\nWhere do you want to Build?");
        box = Controller.askForCoordinates();
        if (card.getName().equals("Atlas")){
            System.out.println("\nDo you wish to build a Dome? \n yes / no");
            while (msg == null || !msg.equals("yes") && !msg.equals("no")) {
                msg = in.nextLine();
            }
            if (msg.equals("yes")){
                if (selectedWorker.LegalBuild(box)){
                    selectedWorker.BuildDome(box);
                }
            }
        }
        if (selectedWorker.LegalBuild(box)){
            selectedWorker.Build(box);
        }
        else{
            System.out.println("\nIl comune non ti dà il consenso");
            buildPhase();
            return;
        }
    }

    /**
     * Pass your Turn if you made the mandatory actions
     */
    public void endTurn(){
        for (Player players : Game.getInstance().getPlayer()){
            players.getCard().myVictoryCondition();
        }
        Game.getInstance().CheckGameFinished();
        Game.getInstance().NextTurn();
    }

    /**
     * Start your Turn, you can make your actions; if you can't, you lose
     */
    public void turnStart(){
        //new Cli().ShowMap();
        Boolean canDoSomething = false;
        selectedWorker=null;
        if (loser){
            endTurn();
        }
        if ((Game.getInstance().getActualTurn() / Game.getInstance().getPlayer().size()) == 0) {
            this.initializeWorkers();
            this.endTurn();
        }
        if (card.isActivePower()){  //Implementare con la view il modo di scegliere se usare o meno il potere
            System.out.println("\n Would you like to use " + card.getName() + "Power? \n yes / no");
            while (msg == null || !msg.equals("yes") && !msg.equals("no")) {
                msg = in.nextLine();
            }
            if (msg.equals("yes")) {
                card.activeSubroutine();
                usePower = false;
                msg = null;
                endTurn();
            }
            if (msg.equals("no")){
                msg=null;
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
        Box box = null; // TO CHANGE (quando faremo la view e gli i/o)
        System.out.println("\nWhere do you want to place your first Worker?");
        box = Controller.askForCoordinates();
        Worker worker = workers.get(0);
        while (worker.getPosition() == null){
            if (worker.setInitialPosition(box)){
                worker.setState(true);
            }
            else {
                System.out.println("Not a valid position, try again");
                box = Controller.askForCoordinates();
            }

        }
        System.out.println("Where do you want to place your second Worker?");
        box = Controller.askForCoordinates();
        worker = workers.get(1);
        while (worker.getPosition() == null){
            if (worker.setInitialPosition(box)){
                worker.setState(true);
            }
            else {
                System.out.println("\n Not a valid position, try again");
                box = Controller.askForCoordinates();
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
        System.out.println("\n You lost!\n Dumbo's!");
        loser=true;
        Game.getInstance().CheckGameFinished();
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

}
