package Model;

import java.util.ArrayList;

public class Player {
    private String nickName;
    private int turn;
    private boolean challenger;
    private GodCard card;
    private boolean winner;
    private ArrayList <Worker> workers;

    /**
     * Draws the GodCards of the Game, made by the Challenger
     * @param deck ArrayList of GodCard
     * @return ArrayList of chosen GodCard or null if the player isn't the Challenger
     */
    public ArrayList<GodCard> Draw(GodDeck deck, int[] index){//da modificare con la view
        if (challenger) {
            ArrayList<GodCard> gods = new ArrayList<GodCard>();
            GodCard chosen = null;
            int i = 0;
            while(i < Game.getInstance().getPlayer().size()){//attenzione rischio vuotezza, controllare durante l'implementazione dello start
                chosen = Game.getInstance().getDeck().Draw(index[i]);
                if (chosen != null) {
                    gods.add(chosen);
                    chosen = null;
                    i++;
                }
            }
            return gods;
        }
        else {
            return null;
        }
    }

    /**
     * Choose the GodCard of the Player
     * @param pool ArrayList of the active GodCards
     * @return true or false if the action succeed
     */
    public boolean ChooseGod(ArrayList<GodCard> pool, int index){//sistemare con la view
        if (card == null) {
            GodCard pick = Game.getInstance().getActiveCards().get(index);
            if (!pick.isPicked()) {
                pick.setPicked(true);
                this.card = pick;
                return true;
            }
        }
        return false;
    }

    /**
     * Constructor of Player
     */
    public Player(String nickName) {
        this.nickName = nickName;
        this.turn = 0;//Game.getInstance().getPlayer().size();
        this.workers = new ArrayList<Worker>();
        this.workers.add(new Worker());//aggiunge i due worker, da modificare per divinità strane
        this.workers.add(new Worker());
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
     */
    public Pawn selectWorker(Box box){
        if (box.getUpperLevel() == PawnType.WORKER) {
            return box.getStructure().get(box.getStructure().size()-1);
        }
        else {
            return null;
        }
    }

    /**
     * Start of the movement phase, you must move the selected worker
     */
    public void movePhase(){}

    /**
     * Start of the Build phase, you must build with the selected worker
     */
    public void buildPhase(){}

    /**
     * Pass your Turn if you made the mandatory actions
     */
    public void endTurn(){
    }

    /**
     * Start your Turn, you can make your actions; if you can't, you lose
     */
    public void turnStart(){
        Boolean canDoSomething = false;
        if ((Game.getInstance().getActualTurn() / Game.getInstance().getPlayer().size()) == 0) {
            this.initializeWorkers();
            this.endTurn();
        }
        else {
            for (Worker worker : workers) {
                canDoSomething = canDoSomething || worker.CanMove();
            }
                if (canDoSomething) {
                    this.movePhase();
                    canDoSomething = false;
                }
                else {
                    //player loses
                }
            }
            for (Worker worker : workers) {
                canDoSomething = canDoSomething || worker.CanBuild();
                if (canDoSomething) {
                    this.buildPhase();
                }
                else {
                    //player loses;
                }
            }
            this.endTurn();
        }

    /**
     * Initialize workers
     */
    private void initializeWorkers(){
        Box box = new Box(0,0); // TO CHANGE (quando faremo la view e gli i/o)
        Worker worker;
        while (workers.size() < 2 /* + Gods Special effects */){
            //wait input box
            worker= new Worker();
            if (worker.setInitialPosition(box)){
                workers.add(worker);
                worker.setOwner(this);
                worker.setState(true);
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
}
