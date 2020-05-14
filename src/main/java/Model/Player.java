package Model;

import Controller.SavedData.GameData;
import Controller.SavedData.MapData;
import Controller.SavedData.PlayerData;
import Controller.SavedData.WorkerData;
import Model.Godcards.GodCard;
import Model.Godcards.GodDeck;
import View.Colors;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Class representing a player
 */
public class Player {
    private Colors color;
    private final String nickName;
    private final boolean challenger;
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
        this.nickName = nickName;
        this.winner = false;
        this.loser = false;
        this.usePower = false;
        this.workers = new ArrayList<>();
        this.workers.add(new Worker());
        this.workers.add(new Worker());
        this.workers.get(0).setOwner(this);
        this.workers.get(1).setOwner(this);
        Game.getInstance().getPlayer().add(this);
        this.challenger = Game.getInstance().getPlayer().size() == 1;
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
     */
    public ArrayList<GodCard> Draw(GodDeck deck, ArrayList<Integer> index){
        if (challenger) {
            ArrayList<GodCard> gods = new ArrayList<>();
            GodCard chosen;
            int i = 0;
            try {
                while (i < Game.getInstance().getPlayer().size()) {
                    chosen = deck.Draw(index.get(i));
                    if (chosen != null) {
                        gods.add(chosen);
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
     */
    public boolean ChooseGod(int index){
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

    public GodCard getCard() {
        return card;
    }

    public boolean isWinner() {
        return winner;
    }

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

    public ArrayList<Worker> getWorkers(){
        return this.workers;
    }

    public void setWorkers(ArrayList<Worker> workers) {
        this.workers = workers;
    }

    public Worker getSelectedWorker() {
        return selectedWorker;
    }

    public void setSelectedWorker(Worker selectedWorker) {
        this.selectedWorker = selectedWorker;
    }

    public void setGod(GodCard card) {
        this.card=card;
    }

    public void loadWorkers(ArrayList<String> gameInfo, int playerNumber){
        int workerDataIndex = MapData.Size() + GameData.Size() + PlayerData.Size()*(playerNumber+1) + (2*WorkerData.Size())*playerNumber;
        for (Worker worker : workers){
            worker.setOwner(this);
            if (gameInfo.get(workerDataIndex).equals("1")) {
                worker.setMoved(true);
                setSelectedWorker(worker);
            } else {
                worker.setMoved(false);
            }
            worker.setDidClimb(gameInfo.get(workerDataIndex + 1).equals("1"));
            worker.setDidBuild(gameInfo.get(workerDataIndex + 2).equals("1"));
            if (!gameInfo.get(workerDataIndex+3).equals("-1")){
                worker.setInitialPosition(PlayGround.getInstance().getBox(parseInt(gameInfo.get(workerDataIndex+3)), parseInt(gameInfo.get(workerDataIndex+4))));
            }
            if(!gameInfo.get(workerDataIndex+5).equals("-1")){
                worker.setLastPosition(PlayGround.getInstance().getBox(parseInt(gameInfo.get(workerDataIndex+5)), parseInt(gameInfo.get(workerDataIndex+6))));
            }
            workerDataIndex += WorkerData.Size();
        }
    }

}