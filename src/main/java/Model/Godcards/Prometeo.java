package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.Worker;

import java.util.ArrayList;

/**
 * Class of GodCard Prometeo
 */
public class Prometeo extends GodCard {

    /**
     * Constructor of the god
     */
    public Prometeo(){
        this.setName("Prometeo");
        this.setPower("You can build before and after moving, but, if you do so, you can't climb");
        setActivePower(true);
    }

    /**
     * Implement Prometeo's function
     * @return true or false
     */
    @Override
    public boolean activeSubroutine(){
        boolean canDoSomething=false;
        for (Worker worker : getOwner().getWorkers()) {
        canDoSomething = canDoSomething || worker.CanBuild();
        }
        if (canDoSomething) {
            Worker candidate;
            while(getOwner().getSelectedWorker()==null) {
                candidate = Game.getInstance().getController().VirtualAskWorker();
                if (candidate.CanBuild()) {
                    getOwner().setSelectedWorker(candidate);
                }
            }
            Game.getInstance().getController().BuildPhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
        }
        else {
            Game.getInstance().getController().PlayerLose(getOwner());
            return false;
        }
        if (getOwner().getSelectedWorker().CanMove()){
            Game.getInstance().getController().MovePhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
        }
        else{
            Game.getInstance().getController().PlayerLose(getOwner());
            return false;
        }
        if (getOwner().getSelectedWorker().CanBuild()){
            Game.getInstance().getController().BuildPhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
        }
        else {
            Game.getInstance().getController().PlayerLose(getOwner());
            return false;
        }
        return true;
    }

    /**
     * Checks if it has performed movement
     * @return true or false
     */
    @Override
    public boolean myMovement(){
        return getOwner().isUsePower();
    }

    /**
     * If you use the power you can't climb
     * @param adjacentBoxes ArrayList of valid boxes
     * @return ArrayList of updated boxes
     */
    @Override
    public ArrayList<Box> specialMovement(ArrayList<Box> adjacentBoxes){
        if (getOwner().getSelectedWorker()==null){
            return adjacentBoxes;
        }
        ArrayList<Box> toRemove = new ArrayList<>();
        for (Box box : adjacentBoxes){
            if (box.getStructure().size()>=getOwner().getSelectedWorker().getPosition().getStructure().size()){
                toRemove.add(box);
            }
        }
        for (Box box : toRemove){
            if (adjacentBoxes.contains(box)){
                adjacentBoxes.remove(box);
            }
        }
        return adjacentBoxes;
    }
}
