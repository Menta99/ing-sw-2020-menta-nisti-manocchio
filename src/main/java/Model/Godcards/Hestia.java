package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.Worker;

import java.util.ArrayList;

/**
 * Class of GodCard Hestia
 */
public class Hestia extends GodCard {

    /**
     * Constructor of the God
     */
    public Hestia(){
        this.setName("Hestia");
        this.setPower("You can build twice, but the second building must not be on a Border position");
        setActivePower(true);
    }

    /**
     * Implement Hestia's power
     * @return true or false
     */
    @Override
    public boolean activeSubroutine() {
        boolean canDoSomething=false;
        for (Worker worker : getOwner().getWorkers()) {
            canDoSomething = canDoSomething || worker.CanMove();
        }
        if (canDoSomething) {
            Game.getInstance().getController().SelectWorkerPhase(getOwner());
            Game.getInstance().getController().MovePhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
        }
        else {
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
     * Checks if it has performed building
     * @return true or false
     */
    @Override
    public boolean myBuild(){
        if(getOwner().getSelectedWorker()!=null){
            return getOwner().getSelectedWorker().isDidBuild();
        }
        return false;
    }

    /**
     * If built remove the border boxes
     * @param adjacentBoxes ArrayList of valid boxes
     * @return ArrayList of updated boxes
     */
    @Override
    public ArrayList<Box> specialBuilding(ArrayList<Box> adjacentBoxes){
        ArrayList<Box> toRemove = new ArrayList<>();
        if (getOwner().getSelectedWorker().isDidBuild()){
            for(Box box : adjacentBoxes){
                if(box.isBorder()) {
                    toRemove.add(box);
                }
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


