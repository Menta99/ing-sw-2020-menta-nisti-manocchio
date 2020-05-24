package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.Worker;

import java.util.ArrayList;

/**
 * Class of GodCard Hephaestus
 */
public class Hephaestus extends GodCard {

    /**
     * God's constructor
     */
    public Hephaestus(){
        this.setName("Hephaestus");
        this.setPower("You can build again on the same position, but not a Dome");
        setActivePower(true);
    }

    /**
     * Implement God's function
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
            getOwner().getSelectedWorker().Build(getOwner().getSelectedWorker().getLastBuilding());
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
        if(getOwner().getSelectedWorker()==null){
            return false;
        }
        return getOwner().isUsePower();
    }

    /**
     * If you built you can rebuild but not a Dome
     * @param adjacentBoxes ArrayList of valid boxes
     * @return ArrayList of updated boxes
     */
    @Override
    public ArrayList<Box> specialBuilding(ArrayList<Box> adjacentBoxes){
        ArrayList<Box> toRemove = new ArrayList<>();
        if(!getOwner().getSelectedWorker().isDidBuild()){
            for (Box box : adjacentBoxes){
                if (box.getUpperLevel().getValue()>1){
                    toRemove.add(box);
                }
            }
            adjacentBoxes.removeAll(toRemove);
        }
        return adjacentBoxes;
    }
}
