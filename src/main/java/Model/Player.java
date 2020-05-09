package Model;

import Controller.Controller;
import Model.Godcards.GodCard;
import Model.Godcards.GodDeck;
import View.Colors;

import java.util.ArrayList;

/**
 * Class representing a player
 */
public class Player {
    private Controller controller;
    private Colors color;
    private String nickName;
    private boolean challenger; // possible exceed
    private GodCard card;
    private boolean winner;
    private boolean loser;
    private boolean usePower;
    private ArrayList <Worker> workers;
    private Worker selectedWorker;

    /**
     * Constructor of Player
     */
    public Player(String nickName) {
        this.controller = Game.getInstance().getController();
        this.nickName = nickName;
        this.winner = false;
        this.loser = false;
        this.usePower = false;
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
     * SetUp the workers of the player in the Turn Start phase
     */
    public void SetUpWorkers(){
        for (Worker worker : workers){
            worker.setDidBuild(false);
            worker.setMoved(false);
            worker.setDidClimb(false);
            worker.setLastPosition(worker.getPosition());
        }
    }

    /**
     * Draws the GodCards of the Game, made by the Challenger
     * @param deck ArrayList of GodCard
     * @return ArrayList of chosen GodCard or null if the player isn't the Challenger
     * @throws NullPointerException if requested invalid action on the cards
     */
    public ArrayList<GodCard> Draw(GodDeck deck, ArrayList<Integer> index){
        if (challenger) {
            ArrayList<GodCard> gods = new ArrayList<GodCard>();
            GodCard chosen = null;
            int i = 0;
            try {
                while (i < Game.getInstance().getPlayer().size()) {
                    chosen = Game.getInstance().getDeck().Draw(index.get(i));
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

    public Colors getColor() {
        return color;
    }

    public String getNickName(){
        return this.nickName;
    }

    /**
     * Getter method of card
     */
    public GodCard getCard() {
        return card;
    }

    public boolean isWinner() {
        return winner;
    }

    /**
     * Setter method used in winning condition
     * @param winner Player who won the Game*/
    public void setWinner(boolean winner){
        this.winner = winner;
    }

    public boolean isLoser() {
        return loser;
    }

    public void setLoser(boolean loser) {
        this.loser = loser;
    }

    public boolean isUsePower() {
        return usePower;
    }

    public void setUsePower(boolean usePower) {
        this.usePower = usePower;
    }

    /**
     * Getter method for workers
     */
    public ArrayList<Worker> getWorkers(){
        return this.workers;
    }

    public void setWorkers(ArrayList<Worker> workers) {
        this.workers = workers;
    }

    /**
     * Getter of the selected worker
     */
    public Worker getSelectedWorker() {
        return selectedWorker;
    }

    public void setSelectedWorker(Worker selectedWorker) {
        this.selectedWorker = selectedWorker;
    }
}