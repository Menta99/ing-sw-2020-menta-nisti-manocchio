package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.Worker;

import java.util.ArrayList;

/**
 * Class of GodCard Demeter
 */
public class Demeter extends GodCard {

    /**
     * God's constructor
     */
    public Demeter(){
        this.setName("Demeter");
        this.setPower("You can build twice, but not on the same position");
        setActivePower(true);
    }

    /**
     * Implement Demeter's function
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
     * Check if it has performed the building
     * @return true or false
     */
    @Override
    public boolean myBuild(){
        if(getOwner().getSelectedWorker() == null){
            return false;
        }
        return getOwner().getSelectedWorker().isDidBuild();
    }

    /**
     * If built, remove the box where did build
     * @param adjacentBoxes ArrayList of valid boxes
     * @return adjacentBoxes without box where he built
     */
    @Override
    public ArrayList<Box> specialBuilding(ArrayList<Box> adjacentBoxes){
        if(getOwner().getSelectedWorker().isDidBuild()){
            adjacentBoxes.remove(getOwner().getSelectedWorker().getLastBuilding());
        }
        return adjacentBoxes;
    }
}
